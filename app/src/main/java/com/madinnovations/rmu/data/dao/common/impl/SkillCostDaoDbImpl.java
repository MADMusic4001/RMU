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
import com.madinnovations.rmu.data.dao.common.SkillCostDao;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCostSchema;
import com.madinnovations.rmu.data.entities.common.SkillCost;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link SkillCost} objects in a SQLite database.
 */
@Singleton
public class SkillCostDaoDbImpl extends BaseDaoDbImpl<SkillCost> implements SkillCostDao, SkillCostSchema {
    /**
     * Creates a new instance of SkillCostDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public SkillCostDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    public SkillCost getById(int id) {
        final String selectionArgs[] = { String.valueOf(id) };
        final String selection = COLUMN_ID + " = ?";
        SkillCost instance = new SkillCost();

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
    public List<SkillCost> getAll() {
        List<SkillCost> list = new ArrayList<>();

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
                    SkillCost instance = cursorToEntity(cursor);
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
    public boolean save(SkillCost instance) {
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

    @Override
    protected SkillCost cursorToEntity(Cursor cursor) {
        SkillCost instance = null;

        if (cursor != null) {
            instance = new SkillCost();
            instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            instance.setInitialCost(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_SKILL_COST)));
            instance.setAdditionalCost(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_ADDITIONAL_COST)));
        }
        return instance;
    }

    @Override
    protected ContentValues getContentValues(SkillCost instance) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_SKILL_COST, instance.getInitialCost());
        initialValues.put(COLUMN_ADDITIONAL_COST, instance.getAdditionalCost());
        return initialValues;
    }
}
