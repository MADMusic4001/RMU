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
import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultRowDao;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultSchema;
import com.madinnovations.rmu.data.entities.combat.CriticalType;
import com.madinnovations.rmu.data.entities.combat.DamageResult;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link DamageResult} objects in a SQLite database.
 */
@Singleton
public class DamageResultDaoDbImpl extends BaseDaoDbImpl<DamageResult> implements DamageResultDao, DamageResultSchema {
	private DamageResultRowDao damageResultRowDao;

    /**
     * Creates a new instance of DamageResultDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public DamageResultDaoDbImpl(SQLiteOpenHelper helper, DamageResultRowDao damageResultRowDao) {
        super(helper);
		this.damageResultRowDao = damageResultRowDao;
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
    protected int getId(DamageResult instance) {
        return instance.getId();
    }

    @Override
    protected void setId(DamageResult instance, int id) {
        instance.setId(id);
    }

    @Override
    protected DamageResult cursorToEntity(@NonNull Cursor cursor) {
        DamageResult instance = new DamageResult();

        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setDamageResultRow(damageResultRowDao.getById(cursor.getInt(
				cursor.getColumnIndexOrThrow(COLUMN_DAMAGE_RESULT_ROW_ID))));
		instance.setArmorType(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_ARMOR_TYPE)));
        instance.setHits(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_HITS)));
        if(cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_CRITICAL_SEVERITY)) ||
                cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_CRITICAL_TYPE))) {
            instance.setCriticalSeverity(null);
            instance.setCriticalType(null);
        }
        else {
            instance.setCriticalSeverity(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CRITICAL_SEVERITY)).charAt(0));
            instance.setCriticalType(CriticalType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CRITICAL_TYPE))));
        }

        return instance;
    }

	@Override
	protected ContentValues getContentValues(DamageResult instance) {
        ContentValues values;

        if(instance.getId() != -1) {
			values = new ContentValues(6);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(5);
		}
		values.put(COLUMN_DAMAGE_RESULT_ROW_ID, instance.getDamageResultRow().getId());
		values.put(COLUMN_ARMOR_TYPE, instance.getArmorType());
        values.put(COLUMN_HITS, instance.getHits());
        if(instance.getCriticalSeverity() == null || instance.getCriticalType() == null) {
            values.putNull(COLUMN_CRITICAL_SEVERITY);
            values.putNull(COLUMN_CRITICAL_TYPE);
        }
        else {
            values.put(COLUMN_CRITICAL_SEVERITY, String.valueOf((char)instance.getCriticalSeverity()));
            values.put(COLUMN_CRITICAL_TYPE, instance.getCriticalType().name());
        }

		return values;
	}

	@Override
	public Collection<DamageResult> getDamageResultsForRow(DamageResultRow damageResultRow) {
		List<DamageResult> list = new ArrayList<>();
		String selection = COLUMN_DAMAGE_RESULT_ROW_ID + " = ?";
		String[] selectionArgs = { String.valueOf(damageResultRow.getId())};

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection, selectionArgs, getSortString());

			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					DamageResult instance = cursorToEntity(cursor);
					list.add(instance);
					cursor.moveToNext();
				}
				cursor.close();
			}
			if(newTransaction) {
				db.setTransactionSuccessful();
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
	public Collection<DamageResult> deleteDamageResultsForRow(DamageResultRow damageResultRow) {
		Collection<DamageResult> list;
		String selection = COLUMN_DAMAGE_RESULT_ROW_ID + " = ?";
		String[] selectionArgs = { String.valueOf(damageResultRow.getId())};

		SQLiteDatabase db = helper.getWritableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			list = getDamageResultsForRow(damageResultRow);
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
}
