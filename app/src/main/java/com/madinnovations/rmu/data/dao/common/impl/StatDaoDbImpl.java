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
import android.util.LruCache;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.CacheConfig;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.schemas.StatSchema;
import com.madinnovations.rmu.data.entities.common.Stat;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Stat} objects in a SQLite database.
 */
@Singleton
public class StatDaoDbImpl extends BaseDaoDbImpl<Stat> implements StatDao, StatSchema {
	private LruCache<Integer, Stat> statsCache = new LruCache<>(CacheConfig.STAT_CACHE_SIZE);

    /**
     * Creates a new instance of StatDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public StatDaoDbImpl(SQLiteOpenHelper helper) {
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
	protected int getId(Stat instance) {
		return instance.getId();
	}

	@Override
	protected void setId(Stat instance, int id) {
		instance.setId(id);
	}

	@Override
	protected LruCache<Integer, Stat> getCache() {
		return statsCache;
	}

	@Override
    protected Stat cursorToEntity(@NonNull Cursor cursor) {
		Stat instance = new Stat();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setAbbreviation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ABBREVIATION)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(Stat instance) {
		ContentValues initialValues = new ContentValues(4);

		initialValues.put(COLUMN_ABBREVIATION, instance.getAbbreviation());
		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());

		return initialValues;
	}
}
