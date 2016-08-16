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
import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultRowDao;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultRowSchema;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.data.entities.combat.DamageTable;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link DamageResultRow} objects in a SQLite database.
 */
@Singleton
public class DamageResultRowDaoDbImpl extends BaseDaoDbImpl<DamageResultRow> implements DamageResultRowDao, DamageResultRowSchema {
    DamageTableDao damageTableDao;
    DamageResultDao damageResultDao;

    /**
     * Creates a new instance of DamageResultRowDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public DamageResultRowDaoDbImpl(SQLiteOpenHelper helper, DamageTableDao damageTableDao, DamageResultDao damageResultDao) {
        super(helper);
        this.damageTableDao = damageTableDao;
        this.damageResultDao = damageResultDao;
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
    protected int getId(DamageResultRow instance) {
        return instance.getId();
    }

    @Override
    protected void setId(DamageResultRow instance, int id) {
        instance.setId(id);
    }

    @Override
    protected DamageResultRow cursorToEntity(@NonNull Cursor cursor) {
        return cursorToEntity(cursor, damageTableDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DAMAGE_TABLE_ID))));
    }

	@Override
	protected ContentValues getContentValues(DamageResultRow instance) {
        ContentValues values = new ContentValues(14);

        values.put(COLUMN_DAMAGE_TABLE_ID, instance.getDamageTable().getId());
        values.put(COLUMN_RANGE_LOW_VALUE, instance.getRangeLowValue());
        values.put(COLUMN_RANGE_HIGH_VALUE, instance.getRangeHighValue());
        for(int i = 0; i < 10; i++) {
            values.put(COLUMN_AT_RESULT_IDS[i], instance.getDamageResults()[i] != null ? instance.getDamageResults()[i].getId() : null);
        }

		return values;
	}

    @Override
    public Collection<DamageResultRow> getDamageResultRowsForDamageTable(DamageTable filter) {
        final String selectionArgs[] = { String.valueOf(filter.getId()) };
        final String selection = COLUMN_DAMAGE_TABLE_ID + " = ?";
        Collection<DamageResultRow> list = new ArrayList<>();

        SQLiteDatabase db = helper.getReadableDatabase();
        boolean newTransaction = !db.inTransaction();
        if(newTransaction) {
            db.beginTransaction();
        }
        try {
            Cursor cursor = query(getTableName(), getColumns(), selection, selectionArgs, getIdColumnName());

            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    DamageResultRow instance = cursorToEntity(cursor, filter);

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

    private DamageResultRow cursorToEntity(@NonNull Cursor cursor, DamageTable damageTable) {
        DamageResultRow instance = new DamageResultRow();

        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        instance.setDamageTable(damageTable);
        instance.setRangeLowValue(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_RANGE_LOW_VALUE)));
        instance.setRangeHighValue(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_RANGE_HIGH_VALUE)));
        for(int i =  0; i < 10; i ++) {
            if(cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_AT_RESULT_IDS[i]))) {
                instance.getDamageResults()[i] = null;
            }
            else {
                instance.getDamageResults()[i] = damageResultDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AT_RESULT_IDS[i])));
            }
        }

        return instance;
    }
}
