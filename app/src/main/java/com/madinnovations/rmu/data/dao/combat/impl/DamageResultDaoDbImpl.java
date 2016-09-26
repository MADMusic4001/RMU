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
import com.madinnovations.rmu.data.dao.combat.CriticalTypeDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultSchema;
import com.madinnovations.rmu.data.entities.combat.DamageResult;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link DamageResult} objects in a SQLite database.
 */
@Singleton
public class DamageResultDaoDbImpl extends BaseDaoDbImpl<DamageResult> implements DamageResultDao, DamageResultSchema {
    CriticalTypeDao criticalTypeDao;

    /**
     * Creates a new instance of DamageResultDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public DamageResultDaoDbImpl(SQLiteOpenHelper helper, CriticalTypeDao criticalTypeDao) {
        super(helper);
        this.criticalTypeDao = criticalTypeDao;
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
        instance.setHits(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_HITS)));
        if(cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_CRITICAL_SEVERITY)) ||
                cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_CRITICAL_TYPE_ID))) {
            instance.setCriticalSeverity(null);
            instance.setCriticalType(null);
        }
        else {
            instance.setCriticalSeverity(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CRITICAL_SEVERITY)).charAt(0));
            instance.setCriticalType(criticalTypeDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CRITICAL_TYPE_ID))));
        }

        return instance;
    }

	@Override
	protected ContentValues getContentValues(DamageResult instance) {
        ContentValues values = new ContentValues(4);

        values.put(COLUMN_HITS, instance.getHits());
        if(instance.getCriticalSeverity() == null || instance.getCriticalType() == null) {
            values.putNull(COLUMN_CRITICAL_SEVERITY);
            values.putNull(COLUMN_CRITICAL_TYPE_ID);
        }
        else {
            values.put(COLUMN_CRITICAL_SEVERITY, String.valueOf((char)instance.getCriticalSeverity()));
            values.put(COLUMN_CRITICAL_TYPE_ID, instance.getCriticalType().getId());
        }

		return values;
	}
}
