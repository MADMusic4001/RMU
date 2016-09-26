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
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.TalentCategoryDao;
import com.madinnovations.rmu.data.dao.common.schemas.TalentCategorySchema;
import com.madinnovations.rmu.data.entities.common.TalentCategory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link TalentCategory} objects in a SQLite database.
 */
@Singleton
public class TalentCategoryDaoDbImpl extends BaseDaoDbImpl<TalentCategory> implements TalentCategoryDao, TalentCategorySchema {
	/**
	 * Creates a new instance of TalentCategoryDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 */
	@Inject
	public TalentCategoryDaoDbImpl(SQLiteOpenHelper helper) {
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
	protected int getId(TalentCategory instance) {
		return instance.getId();
	}

	@Override
	protected void setId(TalentCategory instance, int id) {
		instance.setId(id);
	}

	@Override
	protected TalentCategory cursorToEntity(@NonNull Cursor cursor) {
		TalentCategory instance = new TalentCategory();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(TalentCategory instance) {
		ContentValues initialValues = new ContentValues(2);

		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());

		return initialValues;
	}
}
