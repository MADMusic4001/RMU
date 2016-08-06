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
package com.madinnovations.rmu.data.dao.character.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.CultureDao;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSchema;
import com.madinnovations.rmu.data.entities.character.Culture;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Culture} objects in a SQLite database.
 */
@Singleton
public class CultureDaoDbImpl extends BaseDaoDbImpl<Culture> implements CultureDao, CultureSchema {
    /**
     * Creates a new CultureDaoDbImpl instance
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public CultureDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    public Culture getById(int id) {
        return super.getById(id);
    }

    @Override
    public boolean save(Culture instance) {
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
    protected int getId(Culture instance) {
        return instance.getId();
    }

    @Override
    protected void setId(Culture instance, int id) {
        instance.setId(id);
    }


    @SuppressWarnings("unchecked")
	@Override
    protected Culture cursorToEntity(Cursor cursor) {
        return null;
    }

	@Override
	protected ContentValues getContentValues(Culture instance) {
		return null;
	}
}
