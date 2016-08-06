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
package com.madinnovations.rmu.data.dao.common.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.ParameterDao;
import com.madinnovations.rmu.data.dao.common.schemas.ParameterSchema;
import com.madinnovations.rmu.data.entities.common.Parameter;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Parameter} objects in a SQLite database.
 */
@Singleton
public class ParameterDaoDbImpl extends BaseDaoDbImpl<Parameter> implements ParameterDao, ParameterSchema {
	/**
	 * Creates a new instance of ParameterDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 */
	@Inject
	public ParameterDaoDbImpl(SQLiteOpenHelper helper) {
		super(helper);
	}

	@Override
	public Parameter getById(int id) {
		return super.getById(id);
	}

	@Override
	public boolean save(Parameter instance) {
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
	protected int getId(Parameter instance) {
		return instance.getId();
	}

	@Override
	protected void setId(Parameter instance, int id) {
		instance.setId(id);
	}


	@SuppressWarnings("unchecked")
	@Override
	protected Parameter cursorToEntity(Cursor cursor) {
		Parameter instance = null;

		if (cursor != null) {
			instance = new Parameter();
			instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
			instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
			instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
			instance.setBaseValue(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BASE_VALUE)));
			instance.setValuePerLevelOrTier(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PER_VALUE)));
			instance.setPerLevel(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PER_LEVEL)) != 0);
			instance.setPerTier(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PER_TIER)) != 0);
		}
		return instance;
	}

	@Override
	protected ContentValues getContentValues(Parameter instance) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
		initialValues.put(COLUMN_BASE_VALUE, instance.getBaseValue());
		initialValues.put(COLUMN_PER_VALUE, instance.getValuePerLevelOrTier());
		initialValues.put(COLUMN_PER_LEVEL, instance.isPerLevel());
		initialValues.put(COLUMN_PER_TIER, instance.isPerTier());
		return initialValues;
	}
}
