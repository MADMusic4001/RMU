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
import com.madinnovations.rmu.data.dao.combat.BodyPartDao;
import com.madinnovations.rmu.data.dao.combat.CriticalResultDao;
import com.madinnovations.rmu.data.dao.combat.CriticalTypeDao;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalResultSchema;
import com.madinnovations.rmu.data.entities.combat.CriticalResult;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link CriticalResult} objects in a SQLite database.
 */
@Singleton
public class CriticalResultDaoDbImpl extends BaseDaoDbImpl<CriticalResult> implements CriticalResultDao, CriticalResultSchema {
    BodyPartDao bodyPartDao;
    CriticalTypeDao criticalTypeDao;

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
        CriticalResult instance = new CriticalResult();

        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        instance.setSeverityCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEVERITY_CODE)).charAt(0));
        instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
        instance.setMinRoll(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_MIN_ROLL)));
        instance.setMaxRoll(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_MAX_ROLL)));
        instance.setBodyPart(bodyPartDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BODY_PART_ID))));
        instance.setHits(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_HITS)));
        instance.setBleeding(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BLEEDING)));
        instance.setFatigue(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_FATIGUE)));
        instance.setBreakage(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BREAKAGE)));
        instance.setInjury(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_INJURY)));
        instance.setDazed(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_DAZED)));
        instance.setStunned(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_STUNNED)));
        instance.setNoParry(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_NO_PARRY)));
        instance.setStaggered(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STAGGERED)) != 0);
        instance.setKnockBack(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_KNOCK_BACK)));
        instance.setProne(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRONE)) != 0);
        instance.setGrappled(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_GRAPPLED)));
        instance.setCriticalType(criticalTypeDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CRITICAL_TYPE_ID))));

        return instance;
    }

	@Override
	protected ContentValues getContentValues(CriticalResult instance) {
        ContentValues initialValues = new ContentValues(20);

        initialValues.put(COLUMN_SEVERITY_CODE, String.valueOf(instance.getSeverityCode()));
        initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
        initialValues.put(COLUMN_MIN_ROLL, instance.getMinRoll());
        initialValues.put(COLUMN_MAX_ROLL, instance.getMaxRoll());
        initialValues.put(COLUMN_BODY_PART_ID, instance.getBodyPart().getId());
        initialValues.put(COLUMN_HITS, instance.getHits());
        initialValues.put(COLUMN_BLEEDING, instance.getBleeding());
        initialValues.put(COLUMN_FATIGUE, instance.getFatigue());
        initialValues.put(COLUMN_BREAKAGE, instance.getBreakage());
        initialValues.put(COLUMN_INJURY, instance.getInjury());
        initialValues.put(COLUMN_DAZED, instance.getDazed());
        initialValues.put(COLUMN_STUNNED, instance.getStunned());
        initialValues.put(COLUMN_NO_PARRY, instance.getNoParry());
        initialValues.put(COLUMN_STAGGERED, instance.isStaggered());
        initialValues.put(COLUMN_KNOCK_BACK, instance.getKnockBack());
        initialValues.put(COLUMN_PRONE, instance.isProne());
        initialValues.put(COLUMN_GRAPPLED, instance.getGrappled());
        initialValues.put(COLUMN_CRITICAL_TYPE_ID, instance.getCriticalType().getId());

        return initialValues;
	}
}
