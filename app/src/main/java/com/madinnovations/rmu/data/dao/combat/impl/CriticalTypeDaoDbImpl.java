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
import android.database.sqlite.SQLiteDatabase;
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
        instance.setCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CODE)).charAt(0));
        instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));

        return instance;
    }

	@Override
	protected ContentValues getContentValues(CriticalType instance) {
        ContentValues values = new ContentValues(3);

        values.put(COLUMN_CODE, String.valueOf(instance.getCode()));
        values.put(COLUMN_NAME, instance.getName());

		return values;
	}

    @Override
    public CriticalType getByCode(char code) {
        final String selectionArgs[] = { String.valueOf(code) };
        final String selection = COLUMN_CODE + " = ?";
        CriticalType instance = null;

        SQLiteDatabase db = helper.getReadableDatabase();
        boolean newTransaction = !db.inTransaction();
        if(newTransaction) {
            db.beginTransaction();
        }
        try {
            Cursor cursor = query(getTableName(), getColumns(), selection,
                    selectionArgs, getIdColumnName());
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    instance = cursorToEntity(cursor);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        finally {
            if(newTransaction) {
                db.endTransaction();
            }
        }

        return instance;
    }
}
