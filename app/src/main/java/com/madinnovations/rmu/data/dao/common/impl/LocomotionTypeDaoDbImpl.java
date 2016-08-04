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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.LocomotionTypeDao;
import com.madinnovations.rmu.data.dao.common.schemas.LocomotionTypeSchema;
import com.madinnovations.rmu.data.entities.common.LocomotionType;

import java.util.ArrayList;
import java.util.List;

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
    public LocomotionType getById(int id) {
        final String selectionArgs[] = { String.valueOf(id) };
        final String selection = COLUMN_ID + " = ?";
        LocomotionType instance = new LocomotionType();

        SQLiteDatabase db = helper.getReadableDatabase();
        boolean newTransaction = !db.inTransaction();
        if(newTransaction) {
            db.beginTransaction();
        }
        try {
            Cursor cursor = super.query(TABLE_NAME, COLUMNS, selection,
                    selectionArgs, COLUMN_ID);
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

    @Override
    public List<LocomotionType> getAll() {
        List<LocomotionType> list = new ArrayList<>();

        SQLiteDatabase db = helper.getReadableDatabase();
        boolean newTransaction = !db.inTransaction();
        if(newTransaction) {
            db.beginTransaction();
        }
        try {
            Cursor cursor = super.query(TABLE_NAME, COLUMNS, null,
                    null, COLUMN_ID);

            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    LocomotionType instance = cursorToEntity(cursor);
                    list.add(instance);
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

        return list;
    }

    @Override
    public boolean save(LocomotionType instance) {
        final String selectionArgs[] = { String.valueOf(instance.getId()) };
        final String selection = COLUMN_ID + " = ?";
        ContentValues contentValues = getContentValues(instance);
        boolean result;

        SQLiteDatabase db = helper.getWritableDatabase();
        boolean newTransaction = !db.inTransaction();
        if(newTransaction) {
            db.beginTransaction();
        }
        try {
            if(instance.getId() == -1) {
                instance.setId((int)db.insert(TABLE_NAME, null, contentValues));
                result = (instance.getId() != -1);
            }
            else {
                contentValues.put(COLUMN_ID, instance.getId());
                int count = db.update(TABLE_NAME, contentValues, selection, selectionArgs);
                result = (count == 1);
            }
            if(result && newTransaction) {
                db.setTransactionSuccessful();
            }
        }
        finally {
            if(newTransaction) {
                db.endTransaction();
            }
        }
        return true;
    }

    @Override
    public boolean deleteById(int id) {
        final String selectionArgs[] = { String.valueOf(id) };
        final String selection = COLUMN_ID + " = ?";

        SQLiteDatabase db = helper.getWritableDatabase();
        boolean newTransaction = !db.inTransaction();
        if(newTransaction) {
            db.beginTransaction();
        }
        try {
            db.delete(TABLE_NAME, selection, selectionArgs);
            if(newTransaction) {
                db.setTransactionSuccessful();
            }
        }
        finally {
            if(newTransaction) {
                db.endTransaction();
            }
        }
        return true;
    }

    @Override
    public int deleteAll() {
        int count = 0;

        SQLiteDatabase db = helper.getWritableDatabase();
        boolean newTransaction = !db.inTransaction();
        if(newTransaction) {
            db.beginTransaction();
        }
        try {
            count = db.delete(TABLE_NAME, null, null);
            if(newTransaction) {
                db.setTransactionSuccessful();
            }
        }
        finally {
            if(newTransaction) {
                db.endTransaction();
            }
        }

        return count;
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
        initialValues.put(COLUMN_DEFAULT_RATE, instance.getDefaultRate());
        initialValues.put(COLUMN_NAME, instance.getName());
        initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
        return initialValues;
    }
}
