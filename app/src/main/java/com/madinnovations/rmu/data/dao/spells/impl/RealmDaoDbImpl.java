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
import android.util.LruCache;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.CacheConfig;
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.dao.spells.schemas.RealmSchema;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.spells.RealmDBO;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link RealmDBO} objects in a SQLite database.
 */
@Singleton
public class RealmDaoDbImpl extends BaseDaoDbImpl<RealmDBO> implements RealmDao, RealmSchema {
	private LruCache<Integer, RealmDBO> realmsCache = new LruCache<>(CacheConfig.REALM_CACHE_SIZE);

	/**
	 * Creates a new instance of RealmDaoImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 */
	@Inject
	public RealmDaoDbImpl(@NonNull SQLiteOpenHelper helper) {
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
	protected int getId(RealmDBO instance) {
		return instance.getId();
	}

	@Override
	protected void setId(RealmDBO instance, int id) {
		instance.setId(id);
	}

	@Override
	protected LruCache<Integer, RealmDBO> getCache() {
		return realmsCache;
	}

	@Override
	protected RealmDBO cursorToEntity(@NonNull Cursor cursor) {
		RealmDBO instance = new RealmDBO();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		instance.setStat(Statistic.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STAT_NAME))));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(RealmDBO instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(4);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(3);
		}
		values.put(COLUMN_NAME, instance.getName());
		values.put(COLUMN_DESCRIPTION, instance.getDescription());
		values.put(COLUMN_STAT_NAME, instance.getStat().name());

		return values;
	}
}
