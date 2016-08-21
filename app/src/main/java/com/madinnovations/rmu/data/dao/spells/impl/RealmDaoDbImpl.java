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
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.dao.spells.schemas.RealmSchema;
import com.madinnovations.rmu.data.entities.spells.Realm;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Realm} objects in a SQLite database.
 */
@Singleton
public class RealmDaoDbImpl extends BaseDaoDbImpl<Realm> implements RealmDao, RealmSchema {
	private StatDao statDao;

	/**
	 * Creates a new instance of RealmDaoImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 * @param statDao  a {@link StatDao} instance
	 */
	@Inject
	public RealmDaoDbImpl(@NonNull SQLiteOpenHelper helper, @NonNull StatDao statDao) {
		super(helper);
		this.statDao = statDao;
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
	protected int getId(Realm instance) {
		return instance.getId();
	}

	@Override
	protected void setId(Realm instance, int id) {
		instance.setId(id);
	}

	@Override
	protected Realm cursorToEntity(@NonNull Cursor cursor) {
		Realm instance = new Realm();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		instance.setStat(statDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STAT_ID))));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(Realm instance) {
		ContentValues initialValues = new ContentValues(3);

		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
		initialValues.put(COLUMN_STAT_ID, instance.getStat().getId());

		return initialValues;
	}
}
