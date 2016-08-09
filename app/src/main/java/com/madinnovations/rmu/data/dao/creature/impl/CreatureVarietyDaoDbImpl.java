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
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.creature.CreatureTypeDao;
import com.madinnovations.rmu.data.dao.creature.CreatureVarietyDao;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureVarietySchema;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link CreatureVariety} objects in a SQLite database.
 */
@Singleton
public class CreatureVarietyDaoDbImpl extends BaseDaoDbImpl<CreatureVariety> implements CreatureVarietyDao, CreatureVarietySchema {
	private CreatureTypeDao creatureTypeDao;

	/**
	 * Creates a new instance of CreatureVarietyDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 */
	@Inject
	public CreatureVarietyDaoDbImpl(SQLiteOpenHelper helper, CreatureTypeDao creatureTypeDao) {
		super(helper);
		this.creatureTypeDao = creatureTypeDao;
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
	protected int getId(CreatureVariety instance) {
		return instance.getId();
	}

	@Override
	protected void setId(CreatureVariety instance, int id) {
		instance.setId(id);
	}

	@Override
	protected CreatureVariety cursorToEntity(Cursor cursor) {
		CreatureVariety instance = new CreatureVariety();

		if (cursor != null) {
			instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
			instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
			instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
			instance.setType(creatureTypeDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TYPE_ID))));
		}
		return instance;
	}

	@Override
	protected ContentValues getContentValues(CreatureVariety instance) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
		initialValues.put(COLUMN_TYPE_ID, instance.getType().getId());
		return initialValues;
	}
}
