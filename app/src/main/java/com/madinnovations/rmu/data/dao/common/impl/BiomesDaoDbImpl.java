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
import com.madinnovations.rmu.data.dao.common.BiomeDao;
import com.madinnovations.rmu.data.dao.common.schemas.BiomeSchema;
import com.madinnovations.rmu.data.entities.common.Biome;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Biome} objects in a SQLite database.
 */
@Singleton
public class BiomesDaoDbImpl extends BaseDaoDbImpl<Biome> implements BiomeDao, BiomeSchema {
    /**
     * Creates a new instance of BiomeDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public BiomesDaoDbImpl(SQLiteOpenHelper helper) {
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
    protected int getId(Biome instance) {
        return instance.getId();
    }

    @Override
    protected void setId(Biome instance, int id) {
        instance.setId(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Biome cursorToEntity(Cursor cursor) {
        Biome instance = null;

        if (cursor != null) {
            instance = new Biome();
            instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            instance.setCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CODE)).charAt(0));
            instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
            instance.setHumidity(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HUMIDITY)));
            instance.setFlora(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FLORA)));
            instance.setFauna(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FAUNA)));
        }
        return instance;
    }

    @Override
    protected ContentValues getContentValues(Biome instance) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_CODE, String.valueOf(instance.getCode()));
        initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
        initialValues.put(COLUMN_HUMIDITY, instance.getHumidity());
        initialValues.put(COLUMN_FLORA, instance.getFlora());
        initialValues.put(COLUMN_FAUNA, instance.getFauna());
        return initialValues;
    }
}
