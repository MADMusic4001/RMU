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
import com.madinnovations.rmu.data.dao.combat.DiseaseDao;
import com.madinnovations.rmu.data.dao.combat.schemas.DiseaseSchema;
import com.madinnovations.rmu.data.entities.combat.Disease;
import com.madinnovations.rmu.data.entities.combat.Severity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Disease} objects in a SQLite database.
 */
@Singleton
public class DiseaseDaoDbImpl extends BaseDaoDbImpl<Disease> implements DiseaseDao, DiseaseSchema {
    /**
     * Creates a new instance of DiseaseDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public DiseaseDaoDbImpl(SQLiteOpenHelper helper) {
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
    protected int getId(Disease instance) {
        return instance.getId();
    }

    @Override
    protected void setId(Disease instance, int id) {
        instance.setId(id);
    }

    @Override
    protected Disease cursorToEntity(@NonNull Cursor cursor) {
        Disease instance = new Disease();

        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        instance.setSeverity(Severity.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEVERITY_NAME))));
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_MIN_DURATION))) {
			instance.setMinDurationDays(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_MIN_DURATION)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_MAX_DURATION))) {
			instance.setMaxDurationDays(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_MAX_DURATION)));
		}
		instance.setEffects(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EFFECTS)));

        return instance;
    }

	@Override
	protected ContentValues getContentValues(Disease instance) {
        ContentValues values;

        if(instance.getId() != -1) {
            values = new ContentValues(6);
            values.put(COLUMN_ID, instance.getId());
        }
        else {
            values = new ContentValues(5);
        }
        values.put(COLUMN_NAME, instance.getName());
		values.put(COLUMN_SEVERITY_NAME, instance.getSeverity().name());
		if(instance.getMinDurationDays() != null) {
			values.put(COLUMN_MIN_DURATION, instance.getMinDurationDays());
		}
		else {
			values.putNull(COLUMN_MIN_DURATION);
		}
		if(instance.getMaxDurationDays() != null) {
			values.put(COLUMN_MAX_DURATION, instance.getMaxDurationDays());
		}
		else {
			values.putNull(COLUMN_MAX_DURATION);
		}
		values.put(COLUMN_EFFECTS, instance.getEffects());

		return values;
	}
}
