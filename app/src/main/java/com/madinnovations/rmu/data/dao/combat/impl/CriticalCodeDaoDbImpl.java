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
package com.madinnovations.rmu.data.dao.combat.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.CriticalCodeDao;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalCodeSchema;
import com.madinnovations.rmu.data.entities.combat.CriticalCode;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link CriticalCode} objects in a SQLite database.
 */
@Singleton
public class CriticalCodeDaoDbImpl extends BaseDaoDbImpl<CriticalCode> implements CriticalCodeDao, CriticalCodeSchema {
	/**
	 * Creates a new instance of CriticalCodeDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 */
	@Inject
	public CriticalCodeDaoDbImpl(SQLiteOpenHelper helper) {
		super(helper);
	}

	@Override
	public CriticalCode getById(int id) {
		return super.getById(id);
	}

	@Override
	public boolean save(CriticalCode instance) {
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
	protected int getId(CriticalCode instance) {
		return instance.getId();
	}

	@Override
	protected void setId(CriticalCode instance, int id) {
		instance.setId(id);
	}


	@Override
	protected CriticalCode cursorToEntity(Cursor cursor) {
		CriticalCode instance = new CriticalCode();

		if (cursor != null) {
			instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
			instance.setCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CODE)));
			instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		}
		return instance;
	}

	@Override
	protected ContentValues getContentValues(CriticalCode instance) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COLUMN_CODE, instance.getCode());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
		return initialValues;
	}
}
