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
package com.madinnovations.rmu.data.dao.spells.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.spells.SpellListTypeDao;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellListTypeSchema;
import com.madinnovations.rmu.data.entities.spells.SpellListType;

/**
 * Methods for managing {@link SpellListType} objects in a SQLite database.
 */
public class SpellListTypeDaoDbImpl extends BaseDaoDbImpl<SpellListType> implements SpellListTypeDao, SpellListTypeSchema {

	/**
	 * Creates a new instance of SpellListTypeDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 */
	public SpellListTypeDaoDbImpl(@NonNull SQLiteOpenHelper helper) {
		super(helper);
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
	protected int getId(SpellListType instance) {
		return instance.getId();
	}

	@Override
	protected void setId(SpellListType instance, int id) {
		instance.setId(id);
	}

	@Override
	protected SpellListType cursorToEntity(@NonNull Cursor cursor) {
		SpellListType instance = new SpellListType();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(SpellListType instance) {
		ContentValues values = new ContentValues(2);

		values.put(COLUMN_NAME, instance.getName());
		values.put(COLUMN_DESCRIPTION, instance.getDescription());

		return values;
	}
}
