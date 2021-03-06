/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.dao.combat.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
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
    private DamageTableDao damageTableDao;

    /**
     * Creates a new instance of DamageResultRowDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public DamageResultRowDaoDbImpl(SQLiteOpenHelper helper, DamageTableDao damageTableDao) {
        super(helper);
        this.damageTableDao = damageTableDao;
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
        ContentValues values;

        if(instance.getId() != -1) {
            values = new ContentValues(4);
            values.put(COLUMN_ID, instance.getId());
        }
        else {
            values = new ContentValues(3);
        }
        values.put(COLUMN_DAMAGE_TABLE_ID, instance.getDamageTable().getId());
        values.put(COLUMN_RANGE_LOW_VALUE, instance.getRangeLowValue());
        values.put(COLUMN_RANGE_HIGH_VALUE, instance.getRangeHighValue());

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
            Cursor cursor = query(getTableName(), getColumns(), selection, selectionArgs, COLUMN_RANGE_HIGH_VALUE + " DESC");

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

	@Override
	public Collection<DamageResultRow> deleteDamageResultRowsForDamageTable(DamageTable filter) {
		Collection<DamageResultRow> list;
		String selection = COLUMN_DAMAGE_TABLE_ID + " = ?";
		String[] selectionArgs = { String.valueOf(filter.getId())};

		SQLiteDatabase db = helper.getWritableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			list = getDamageResultRowsForDamageTable(filter);
			boolean successful = (db.delete(TABLE_NAME, selection, selectionArgs) == list.size());
			if(newTransaction && successful) {
				db.setTransactionSuccessful();
			} else if(!successful) {
				list = new ArrayList<>();
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

        return instance;
    }
}
