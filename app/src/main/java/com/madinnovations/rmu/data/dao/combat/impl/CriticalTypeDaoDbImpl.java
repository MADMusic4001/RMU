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
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.CriticalTypeDao;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalTypeSchema;
import com.madinnovations.rmu.data.entities.combat.CriticalType;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link CriticalType} objects in a SQLite database.
 */
@Singleton
public class CriticalTypeDaoDbImpl extends BaseDaoDbImpl<CriticalType> implements CriticalTypeDao, CriticalTypeSchema {
    /**
     * Creates a new instance of CriticalTypeDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public CriticalTypeDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    public CriticalType getById(int id) {
        return super.getById(id);
    }

    @Override
    public boolean save(CriticalType instance) {
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
    protected int getId(CriticalType instance) {
        return instance.getId();
    }

    @Override
    protected void setId(CriticalType instance, int id) {
        instance.setId(id);
    }

    @Override
    protected CriticalType cursorToEntity(@NonNull Cursor cursor) {
        CriticalType instance = new CriticalType();

        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));

        return instance;
    }

	@Override
	protected ContentValues getContentValues(CriticalType instance) {
        ContentValues values = new ContentValues(3);

        values.put(COLUMN_NAME, instance.getName());
        values.put(COLUMN_DESCRIPTION, instance.getDescription());

		return null;
	}
}
