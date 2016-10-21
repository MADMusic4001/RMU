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

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.creature.CreatureArchetypeDao;
import com.madinnovations.rmu.data.dao.creature.schemas.ArchetypeSkillsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureArchetypeSchema;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;

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
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		final String selection = ArchetypeSkillsSchema.COLUMN_ARCHETYPE_ID + " = ?";

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

		return result;
	}

	private ContentValues getArchetypeSkillCategoryValues(int archetypeId, int skillCategoryId, int priority) {
		ContentValues values = new ContentValues(3);

		values.put(ArchetypeSkillsSchema.COLUMN_ARCHETYPE_ID, archetypeId);
		values.put(ArchetypeSkillsSchema.COLUMN_SKILL_ID, skillCategoryId);
		values.put(ArchetypeSkillsSchema.COLUMN_PRIORITY, priority);

		return values;
	}

	private void setSkillCategories(@NonNull  CreatureArchetype instance) {
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
}
