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

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.creature.CreatureArchetypeDao;
import com.madinnovations.rmu.data.dao.creature.schemas.ArchetypeSkillsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureArchetypeSchema;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link CreatureArchetype} objects in a SQLite database.
 */
@Singleton
public class CreatureArchetypeDaoDbImpl extends BaseDaoDbImpl<CreatureArchetype>
		implements CreatureArchetypeDao, CreatureArchetypeSchema {
	private StatDao statDao;

	/**
	 * Creates a new instance of CreatureArchetypeDaoImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 */
	@Inject
	public CreatureArchetypeDaoDbImpl(SQLiteOpenHelper helper, StatDao statDao) {
		super(helper);
		this.statDao = statDao;
	}

	@Override
	public CreatureArchetype getById(int id) {
		return super.getById(id);
	}

	@Override
	public boolean save(CreatureArchetype instance) {
		return super.save(instance);
	}

	@Override
	public boolean deleteById(int id) {
		return super.deleteById(id);
	}

	@Override
	public int deleteAll() {
		return super.deleteAll();
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
	protected CreatureArchetype cursorToEntity(Cursor cursor) {
		CreatureArchetype instance = new CreatureArchetype();

		if (cursor != null) {
			instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
			instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
			instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
			instance.setStat1(statDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STAT1_ID))));
			instance.setStat2(statDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STAT2_ID))));
			instance.setSpells(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SPELLS)));
			instance.setRoles(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLES)));
		}
		return instance;
	}

	@Override
	protected ContentValues getContentValues(CreatureArchetype instance) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
		initialValues.put(COLUMN_STAT1_ID, instance.getStat1().getId());
		initialValues.put(COLUMN_STAT2_ID, instance.getStat2().getId());
		initialValues.put(COLUMN_SPELLS, instance.getSpells());
		initialValues.put(COLUMN_ROLES, instance.getRoles());
		return initialValues;
	}

	@Override
	protected boolean saveRelationships(SQLiteDatabase db, CreatureArchetype instance) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		final String selection = ArchetypeSkillsSchema.COLUMN_ARCHETYPE_ID + " = ?";

		db.delete(ArchetypeSkillsSchema.TABLE_NAME, selection, selectionArgs);

		for(SkillCategory skillCategory : instance.getPrimarySkills()) {
			result &= (db.insert(ArchetypeSkillsSchema.TABLE_NAME, null, getArchetypeSkillCategorysValues(instance.getId(),
					skillCategory.getId(), 0)) != -1);
		}

		for(SkillCategory skillCategory : instance.getSecondarySkills()) {
			result &= (db.insert(ArchetypeSkillsSchema.TABLE_NAME, null, getArchetypeSkillCategorysValues(instance.getId(),
					skillCategory.getId(), 1)) != -1);
		}

		for(SkillCategory skillCategory : instance.getTertiarySkills()) {
			result &= (db.insert(ArchetypeSkillsSchema.TABLE_NAME, null, getArchetypeSkillCategorysValues(instance.getId(),
					skillCategory.getId(), 2)) != -1);
		}

		return result;
	}

	private ContentValues getArchetypeSkillCategorysValues(int archetypeId, int skillCategoryId, int priority) {
		ContentValues values = new ContentValues();

		values.put(ArchetypeSkillsSchema.COLUMN_ARCHETYPE_ID, archetypeId);
		values.put(ArchetypeSkillsSchema.COLUMN_SKILL_ID, skillCategoryId);
		values.put(ArchetypeSkillsSchema.COLUMN_PRIORITY, priority);

		return values;
	}
}
