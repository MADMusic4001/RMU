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
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.creature.CreatureCategoryDao;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureCategorySchema;
import com.madinnovations.rmu.data.entities.creature.CreatureCategory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link CreatureCategory} objects in a SQLite database.
 */
@Singleton
public class CreatureCategoryDaoDbImpl extends BaseDaoDbImpl<CreatureCategory>
		implements CreatureCategoryDao, CreatureCategorySchema {
	/**
	 * Creates a new instance of CreatureCategoryDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 */
	@Inject
	public CreatureCategoryDaoDbImpl(SQLiteOpenHelper helper) {
		super(helper);
	}

	@Override
	public CreatureCategory getById(int id) {
		return super.getById(id);
	}

	@Override
	public boolean save(CreatureCategory instance) {
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
	protected int getId(CreatureCategory instance) {
		return instance.getId();
	}

	@Override
	protected void setId(CreatureCategory instance, int id) {
		instance.setId(id);
	}

	@Override
	protected CreatureCategory cursorToEntity(@NonNull Cursor cursor) {
		CreatureCategory instance = new CreatureCategory();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(CreatureCategory instance) {
		ContentValues initialValues = new ContentValues(3);

		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());

		return initialValues;
	}
}
