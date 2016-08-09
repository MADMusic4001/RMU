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
import com.madinnovations.rmu.data.dao.common.LocomotionTypeDao;
import com.madinnovations.rmu.data.dao.common.schemas.LocomotionTypeSchema;
import com.madinnovations.rmu.data.entities.common.LocomotionType;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link LocomotionType} objects in a SQLite database.
 */
@Singleton
public class LocomotionTypeDaoDbImpl extends BaseDaoDbImpl<LocomotionType> implements LocomotionTypeDao, LocomotionTypeSchema {
    /**
     * Creates a new instance of LocomotionTypeDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public LocomotionTypeDaoDbImpl(SQLiteOpenHelper helper) {
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
    protected int getId(LocomotionType instance) {
        return instance.getId();
    }

    @Override
    protected void setId(LocomotionType instance, int id) {
        instance.setId(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected LocomotionType cursorToEntity(Cursor cursor) {
        LocomotionType instance = null;

        if (cursor != null) {
            instance = new LocomotionType();
            instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
            instance.setDefaultRate(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_DEFAULT_RATE)));
        }
        return instance;
    }

    @Override
    protected ContentValues getContentValues(LocomotionType instance) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_NAME, instance.getName());
        initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
        initialValues.put(COLUMN_DEFAULT_RATE, instance.getDefaultRate());
        return initialValues;
    }
}
