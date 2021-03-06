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
import com.madinnovations.rmu.data.dao.spells.SpellSubTypeDao;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellSubTypeSchema;
import com.madinnovations.rmu.data.entities.spells.SpellSubType;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link SpellSubType} objects in a SQLite database.
 */
@Singleton
public class SpellSubTypeDaoDbImpl extends BaseDaoDbImpl<SpellSubType> implements SpellSubTypeDao, SpellSubTypeSchema {
    private LruCache<Integer, SpellSubType> bodyPartsCache = new LruCache<>(CacheConfig.BODY_PART_CACHE_SIZE);

    /**
     * Creates a new instance of SpellSubTypeDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public SpellSubTypeDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    public SpellSubType getById(int id) {
        return super.getById(id);
    }

    @Override
    public boolean save(SpellSubType instance) {
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
    protected int getId(SpellSubType instance) {
        return instance.getId();
    }

    @Override
    protected void setId(SpellSubType instance, int id) {
        instance.setId(id);
    }

    @Override
    protected LruCache<Integer, SpellSubType> getCache() {
        return bodyPartsCache;
    }

    @Override
    protected SpellSubType cursorToEntity(@NonNull Cursor cursor) {
        SpellSubType instance = new SpellSubType();

        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        instance.setCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CODE)).charAt(0));
        instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));

        return instance;
    }

	@Override
	protected ContentValues getContentValues(SpellSubType instance) {
        ContentValues values;

        if(instance.getId() != -1) {
            values = new ContentValues(4);
            values.put(COLUMN_ID, instance.getId());
        }
        else {
            values = new ContentValues(3);
        }
		values.put(COLUMN_NAME, instance.getName());
        values.put(COLUMN_CODE, instance.getCode().toString());
		values.put(COLUMN_DESCRIPTION, instance.getDescription());

		return values;
	}
}
