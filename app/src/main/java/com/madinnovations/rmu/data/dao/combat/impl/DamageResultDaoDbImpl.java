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
    /**
     * Creates a new instance of DamageResultDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public DamageResultDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    public DamageResult getById(int id) {
        return super.getById(id);
    }

    @Override
    public boolean save(DamageResult instance) {
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
    protected int getId(DamageResult instance) {
        return instance.getId();
    }

    @Override
    protected void setId(DamageResult instance, int id) {
        instance.setId(id);
    }

    @Override
    protected DamageResult cursorToEntity(@NonNull Cursor cursor) {
        return null;
    }

	@Override
	protected ContentValues getContentValues(DamageResult instance) {
		return null;
	}
}
