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
import com.madinnovations.rmu.data.dao.combat.BodyPartDao;
import com.madinnovations.rmu.data.dao.combat.CriticalResultDao;
import com.madinnovations.rmu.data.dao.combat.CriticalTypeDao;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalResultSchema;
import com.madinnovations.rmu.data.entities.combat.CriticalResult;
import com.madinnovations.rmu.data.entities.combat.CriticalType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link CriticalResult} objects in a SQLite database.
 */
@Singleton
public class CriticalResultDaoDbImpl extends BaseDaoDbImpl<CriticalResult> implements CriticalResultDao, CriticalResultSchema {
    private BodyPartDao bodyPartDao;
    private CriticalTypeDao criticalTypeDao;

    /**
     * Creates a new instance of CriticalResultDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public CriticalResultDaoDbImpl(SQLiteOpenHelper helper, BodyPartDao bodyPartDao, CriticalTypeDao criticalTypeDao) {
        super(helper);
        this.bodyPartDao = bodyPartDao;
        this.criticalTypeDao = criticalTypeDao;
    }

    @Override
    public CriticalResult getById(int id) {
        return super.getById(id);
    }

    @Override
    public boolean save(CriticalResult instance) {
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
    protected int getId(CriticalResult instance) {
        return instance.getId();
    }

    @Override
    protected void setId(CriticalResult instance, int id) {
        instance.setId(id);
    }

    @Override
    protected CriticalResult cursorToEntity(@NonNull Cursor cursor) {
        return cursorToEntity(cursor, criticalTypeDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CRITICAL_TYPE_ID))));
    }

	@Override
	protected ContentValues getContentValues(CriticalResult instance) {
        ContentValues values;

        if(instance.getId() != -1) {
            values = new ContentValues(20);
            values.put(COLUMN_ID, instance.getId());
        }
        else {
            values = new ContentValues(19);
        }
        values.put(COLUMN_SEVERITY_CODE, String.valueOf(instance.getSeverityCode()));
        values.put(COLUMN_RESULT_TEXT, instance.getResultText());
        values.put(COLUMN_MIN_ROLL, instance.getMinRoll());
        values.put(COLUMN_MAX_ROLL, instance.getMaxRoll());
        values.put(COLUMN_BODY_PART_ID, instance.getBodyPart().getId());
        values.put(COLUMN_HITS, instance.getHits());
        values.put(COLUMN_BLEEDING, instance.getBleeding());
        values.put(COLUMN_FATIGUE, instance.getFatigue());
		if(instance.getBreakage() == null) {
			values.putNull(COLUMN_BREAKAGE);
		}
		else {
			values.put(COLUMN_BREAKAGE, instance.getBreakage());
		}
        values.put(COLUMN_INJURY, instance.getInjury());
        values.put(COLUMN_DAZED, instance.getDazed());
        values.put(COLUMN_STUNNED, instance.getStunned());
        values.put(COLUMN_NO_PARRY, instance.getNoParry());
        values.put(COLUMN_STAGGERED, instance.getStaggered());
        values.put(COLUMN_KNOCK_BACK, instance.getKnockBack());
        values.put(COLUMN_PRONE, instance.getProne());
        values.put(COLUMN_GRAPPLED, instance.getGrappled());
		if(instance.getDeath() == null) {
			values.putNull(COLUMN_DEATH);
		}
		else {
			values.put(COLUMN_DEATH, instance.getDeath());
		}
        values.put(COLUMN_CRITICAL_TYPE_ID, instance.getCriticalType().getId());

        return values;
	}

    @Override
    public List<CriticalResult> getCriticalResultsForCriticalType(CriticalType filter) {
        final String selectionArgs[] = { String.valueOf(filter.getId()) };
        final String selection = COLUMN_CRITICAL_TYPE_ID + " = ?";
        List<CriticalResult> list = new ArrayList<>();

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
                    CriticalResult instance = cursorToEntity(cursor, filter);

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
    public Collection<CriticalResult> getCriticalResultTableRows(CriticalType criticalType, char severityCode) {
        final String selectionArgs[] = { String.valueOf(criticalType.getId()), String.valueOf(severityCode) };
        final String selection = COLUMN_CRITICAL_TYPE_ID + " = ? AND " + COLUMN_SEVERITY_CODE + " = ?";
        List<CriticalResult> list = new ArrayList<>();

        SQLiteDatabase db = helper.getReadableDatabase();
        boolean newTransaction = !db.inTransaction();
        if(newTransaction) {
            db.beginTransaction();
        }
        try {
            Cursor cursor = query(getTableName(), getColumns(), selection, selectionArgs, COLUMN_MIN_ROLL);

            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    CriticalResult instance = cursorToEntity(cursor, criticalType, severityCode);

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

    private CriticalResult cursorToEntity(@NonNull Cursor cursor, CriticalType criticalType) {
        return cursorToEntity(cursor, criticalType, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEVERITY_CODE)).charAt(0));
    }

    private CriticalResult cursorToEntity(@NonNull Cursor cursor, CriticalType criticalType, char severityCode) {
        CriticalResult instance = new CriticalResult();

        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        instance.setSeverityCode(severityCode);
        instance.setResultText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESULT_TEXT)));
        instance.setMinRoll(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_MIN_ROLL)));
        instance.setMaxRoll(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_MAX_ROLL)));
        instance.setBodyPart(bodyPartDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BODY_PART_ID))));
        instance.setHits(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_HITS)));
        instance.setBleeding(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BLEEDING)));
        instance.setFatigue(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_FATIGUE)));
        if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_BREAKAGE))) {
            instance.setBreakage(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BREAKAGE)));
        }
        instance.setInjury(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_INJURY)));
        instance.setDazed(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_DAZED)));
        instance.setStunned(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_STUNNED)));
        instance.setNoParry(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_NO_PARRY)));
        instance.setStaggered(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_STAGGERED)));
        instance.setKnockBack(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_KNOCK_BACK)));
        instance.setProne(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_PRONE)));
        instance.setGrappled(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_GRAPPLED)));
		if(!cursor.isNull(cursor.getColumnIndex(COLUMN_DEATH))) {
			instance.setDeath(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_DEATH)));
		}
        instance.setCriticalType(criticalType);

        return instance;
    }
}
