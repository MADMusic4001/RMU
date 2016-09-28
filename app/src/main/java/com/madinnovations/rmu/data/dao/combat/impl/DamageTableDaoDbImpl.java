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
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageTableSchema;
import com.madinnovations.rmu.data.entities.combat.DamageTable;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link DamageTable} objects in a SQLite database.
 */
@Singleton
public class DamageTableDaoDbImpl extends BaseDaoDbImpl<DamageTable> implements DamageTableDao, DamageTableSchema {
    /**
     * Creates a new instance of DamageTableDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public DamageTableDaoDbImpl(SQLiteOpenHelper helper) {
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
    protected int getId(DamageTable instance) {
        return instance.getId();
    }

    @Override
    protected void setId(DamageTable instance, int id) {
        instance.setId(id);
    }

    @Override
    protected String getSortString() {
        return COLUMN_NAME;
    }

    @Override
    protected DamageTable cursorToEntity(@NonNull Cursor cursor) {
        DamageTable instance = new DamageTable();

        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        instance.setName((cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))));
        instance.setBallTable(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_BALL_TABLE)) != 0);

        return instance;
    }

	@Override
	protected ContentValues getContentValues(DamageTable instance) {
        ContentValues values;

        if(instance.getId() != -1) {
            values = new ContentValues(3);
            values.put(COLUMN_ID, instance.getId());
        }
        else {
            values = new ContentValues(2);
        }
        values.put(COLUMN_NAME, instance.getName());
		values.put(COLUMN_IS_BALL_TABLE, instance.isBallTable());

		return values;
	}
}
