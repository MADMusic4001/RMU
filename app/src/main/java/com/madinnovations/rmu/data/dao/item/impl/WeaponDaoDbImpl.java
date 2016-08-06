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
package com.madinnovations.rmu.data.dao.item.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.item.WeaponDao;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponSchema;
import com.madinnovations.rmu.data.entities.object.Weapon;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Weapon} objects in a SQLite database.
 */
@Singleton
public class WeaponDaoDbImpl extends BaseDaoDbImpl<Weapon> implements WeaponDao, WeaponSchema {
    /**
     * Creates a new instance of WeaponDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public WeaponDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    public Weapon getById(int id) {
        return super.getById(id);
    }

    @Override
    public boolean save(Weapon instance) {
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
    protected int getId(Weapon instance) {
        return instance.getId();
    }

    @Override
    protected void setId(Weapon instance, int id) {
        instance.setId(id);
    }

    @Override
    protected Weapon cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    protected ContentValues getContentValues(Weapon instance) {
        return null;
    }
}
