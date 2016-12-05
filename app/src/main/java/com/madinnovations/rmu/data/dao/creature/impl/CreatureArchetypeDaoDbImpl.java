/**
 * Copyright (C) 2016 MadInnovations
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.dao.creature.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.creature.CreatureArchetypeDao;
import com.madinnovations.rmu.data.dao.creature.schemas.ArchetypeLevelsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.ArchetypeSkillsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureArchetypeSchema;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetypeLevel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link CreatureArchetype} objects in a SQLite database.
 */
@Singleton
public class CreatureArchetypeDaoDbImpl extends BaseDaoDbImpl<CreatureArchetype>
		implements CreatureArchetypeDao, CreatureArchetypeSchema {
	private SkillCategoryDao skillCategoryDao;

	/**
	 * Creates a new instance of CreatureArchetypeDaoImpl
	 *
	 * @param skillCategoryDao  a {@link SkillCategoryDao} instance
	 * @param helper  an SQLiteOpenHelper instance
	 */
	@Inject
	public CreatureArchetypeDaoDbImpl(SQLiteOpenHelper helper, SkillCategoryDao skillCategoryDao) {
		super(helper);
		this.skillCategoryDao = skillCategoryDao;
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumns() {
		return COLUMNS;
	}

	@Override
	protected String getIdColumnName() {
		return COLUMN_ID;
	}

	@Override
	protected int getId(CreatureArchetype instance) {
		return instance.getId();
	}

	@Override
	protected void setId(CreatureArchetype instance, int id) {
		instance.setId(id);
	}

	@Override
	protected CreatureArchetype cursorToEntity(@NonNull Cursor cursor) {
		CreatureArchetype instance = new CreatureArchetype();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		instance.setRealmStat1(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STAT1_IS_REALM)) == 1);
		if(cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_STAT1_NAME))) {
			instance.setStat1(null);
		}
		else {
			instance.setStat1(Statistic.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STAT1_NAME))));
		}
		instance.setRealmStat2(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STAT2_IS_REALM)) == 1);
		if(cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_STAT2_NAME))) {
			instance.setStat2(null);
		}
		else {
			instance.setStat2(Statistic.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STAT2_NAME))));
		}
		setSkillCategories(instance);
		instance.setSpells(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SPELLS)));
		instance.setRoles(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLES)));
		setLevels(instance);

		return instance;
	}

	@Override
	protected ContentValues getContentValues(CreatureArchetype instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(9);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(8);
		}
		values.put(COLUMN_NAME, instance.getName());
		values.put(COLUMN_DESCRIPTION, instance.getDescription());
		values.put(COLUMN_STAT1_IS_REALM, instance.isRealmStat1());
		if(instance.getStat1() == null) {
			values.putNull(COLUMN_STAT1_NAME);
		}
		else {
			values.put(COLUMN_STAT1_NAME, instance.getStat1().name());
		}
		values.put(COLUMN_STAT2_IS_REALM, instance.isRealmStat2());
		if(instance.getStat2() == null) {
			values.putNull(COLUMN_STAT2_NAME);
		}
		else {
			values.put(COLUMN_STAT2_NAME, instance.getStat2().name());
		}
		values.put(COLUMN_SPELLS, instance.getSpells());
		values.put(COLUMN_ROLES, instance.getRoles());

		return values;
	}

	@Override
	protected boolean saveRelationships(SQLiteDatabase db, CreatureArchetype instance) {
		boolean result = true;
		String selectionArgs[] = { String.valueOf(instance.getId()) };
		String selection = ArchetypeSkillsSchema.COLUMN_ARCHETYPE_ID + " = ?";

		db.delete(ArchetypeSkillsSchema.TABLE_NAME, selection, selectionArgs);

		for(SkillCategory skillCategory : instance.getPrimarySkills()) {
			result &= (db.insertWithOnConflict(ArchetypeSkillsSchema.TABLE_NAME, null,
											   getArchetypeSkillCategoryValues(instance.getId(), skillCategory.getId(), 0),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}

		for(SkillCategory skillCategory : instance.getSecondarySkills()) {
			result &= (db.insertWithOnConflict(ArchetypeSkillsSchema.TABLE_NAME, null,
											   getArchetypeSkillCategoryValues(instance.getId(), skillCategory.getId(), 1),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}

		for(SkillCategory skillCategory : instance.getTertiarySkills()) {
			result &= (db.insertWithOnConflict(ArchetypeSkillsSchema.TABLE_NAME, null,
											   getArchetypeSkillCategoryValues(instance.getId(), skillCategory.getId(), 2),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}

		selection = ArchetypeLevelsSchema.COLUMN_ARCHETYPE_ID + " = ?";
		db.delete(ArchetypeLevelsSchema.TABLE_NAME, selection, selectionArgs);

		for(int i = 0; i < instance.getLevels().size(); i++) {
			CreatureArchetypeLevel level = instance.getLevels().valueAt(i);
			result &= (db.insertWithOnConflict(ArchetypeLevelsSchema.TABLE_NAME, null,
											   getArchetypeLevelValues(level, instance.getId()),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}

		return result;
	}

	private ContentValues getArchetypeSkillCategoryValues(int archetypeId, int skillCategoryId, int priority) {
		ContentValues values = new ContentValues(3);

		values.put(ArchetypeSkillsSchema.COLUMN_ARCHETYPE_ID, archetypeId);
		values.put(ArchetypeSkillsSchema.COLUMN_SKILL_ID, skillCategoryId);
		values.put(ArchetypeSkillsSchema.COLUMN_PRIORITY, priority);

		return values;
	}

	private ContentValues getArchetypeLevelValues(CreatureArchetypeLevel instance, int archetypeId) {
		ContentValues values = new ContentValues(22);

		values.put(ArchetypeLevelsSchema.COLUMN_ARCHETYPE_ID, archetypeId);
		values.put(ArchetypeLevelsSchema.COLUMN_LEVEL, instance.getLevel());
		values.put(ArchetypeLevelsSchema.COLUMN_ATTACK, instance.getAttack());
		values.put(ArchetypeLevelsSchema.COLUMN_ATTACK2, instance.getAttack2());
		values.put(ArchetypeLevelsSchema.COLUMN_DEF_BONUS, instance.getDefensiveBonus());
		values.put(ArchetypeLevelsSchema.COLUMN_BODY_DEV, instance.getBodyDevelopment());
		values.put(ArchetypeLevelsSchema.COLUMN_PRIME_SKILL, instance.getPrimeSkill());
		values.put(ArchetypeLevelsSchema.COLUMN_SECONDARY_SKILL, instance.getSecondarySkill());
		values.put(ArchetypeLevelsSchema.COLUMN_POWER_DEV, instance.getPowerDevelopment());
		values.put(ArchetypeLevelsSchema.COLUMN_SPELLS, instance.getSpells());
		values.put(ArchetypeLevelsSchema.COLUMN_TALENT_DP, instance.getTalentDP());
		values.put(ArchetypeLevelsSchema.COLUMN_AGILITY, instance.getAgility());
		values.put(ArchetypeLevelsSchema.COLUMN_CONS_STAT, instance.getConstitutionStat());
		values.put(ArchetypeLevelsSchema.COLUMN_CONSTITUTION, instance.getConstitution());
		values.put(ArchetypeLevelsSchema.COLUMN_EMPATHY, instance.getEmpathy());
		values.put(ArchetypeLevelsSchema.COLUMN_INTUITION, instance.getIntuition());
		values.put(ArchetypeLevelsSchema.COLUMN_MEMORY, instance.getMemory());
		values.put(ArchetypeLevelsSchema.COLUMN_PRESENCE, instance.getPresence());
		values.put(ArchetypeLevelsSchema.COLUMN_QUICKNESS, instance.getQuickness());
		values.put(ArchetypeLevelsSchema.COLUMN_REASONING, instance.getReasoning());
		values.put(ArchetypeLevelsSchema.COLUMN_SELF_DISC, instance.getSelfDiscipline());
		values.put(ArchetypeLevelsSchema.COLUMN_STRENGTH, instance.getStrength());

		return values;
	}

	private void setSkillCategories(@NonNull CreatureArchetype instance) {
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		final String selection = ArchetypeSkillsSchema.COLUMN_ARCHETYPE_ID + " = ?";

		Cursor cursor = super.query(ArchetypeSkillsSchema.TABLE_NAME, ArchetypeSkillsSchema.COLUMNS, selection,
				selectionArgs, ArchetypeSkillsSchema.COLUMN_PRIORITY);
		List<SkillCategory> primaryList = new ArrayList<>();
		List<SkillCategory> secondaryList = new ArrayList<>();
		List<SkillCategory> tertiaryList = new ArrayList<>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(ArchetypeSkillsSchema.COLUMN_SKILL_ID));
			short priority = cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeSkillsSchema.COLUMN_PRIORITY));
			SkillCategory skillCategory = skillCategoryDao.getById(mappedId);
			if(skillCategory != null) {
				if(priority == 0) {
					primaryList.add(skillCategory);
				}
				if(priority == 1) {
					secondaryList.add(skillCategory);
				}
				if(priority == 2) {
					tertiaryList.add((skillCategory));
				}
			}
			cursor.moveToNext();
		}
		cursor.close();

		instance.setPrimarySkills(primaryList);
		instance.setSecondarySkills(secondaryList);
		instance.setTertiarySkills(tertiaryList);
	}

	private void setLevels(@NonNull CreatureArchetype instance) {
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		final String selection = ArchetypeLevelsSchema.COLUMN_ARCHETYPE_ID + " = ?";

		Cursor cursor = super.query(ArchetypeLevelsSchema.TABLE_NAME, ArchetypeLevelsSchema.COLUMNS, selection,
									selectionArgs, ArchetypeLevelsSchema.COLUMN_LEVEL);
		SparseArray<CreatureArchetypeLevel> levelSparseArray = new SparseArray<>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CreatureArchetypeLevel level = new CreatureArchetypeLevel();
			level.setCreatureArchetype(instance);
			int levelValue = cursor.getInt(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_LEVEL));
			level.setLevel((short)levelValue);
			level.setAttack(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_ATTACK)));
			level.setAttack2(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_ATTACK2)));
			level.setDefensiveBonus(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_DEF_BONUS)));
			level.setBodyDevelopment(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_BODY_DEV)));
			level.setPrimeSkill(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_PRIME_SKILL)));
			level.setSecondarySkill(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_SECONDARY_SKILL)));
			level.setPowerDevelopment(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_POWER_DEV)));
			level.setSpells(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_SPELLS)));
			level.setTalentDP(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_TALENT_DP)));
			level.setAgility(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_AGILITY)));
			level.setConstitutionStat(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_CONS_STAT)));
			level.setConstitution(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_CONSTITUTION)));
			level.setEmpathy(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_EMPATHY)));
			level.setIntuition(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_INTUITION)));
			level.setMemory(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_MEMORY)));
			level.setPresence(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_PRESENCE)));
			level.setQuickness(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_QUICKNESS)));
			level.setReasoning(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_REASONING)));
			level.setSelfDiscipline(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_SELF_DISC)));
			level.setStrength(cursor.getShort(cursor.getColumnIndexOrThrow(ArchetypeLevelsSchema.COLUMN_STRENGTH)));
			levelSparseArray.put(levelValue, level);
			cursor.moveToNext();
		}
		cursor.close();

		instance.setLevels(levelSparseArray);
	}
}
