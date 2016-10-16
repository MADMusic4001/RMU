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
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.item.WeaponTemplateDao;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponTemplateSchema;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link WeaponTemplate} objects in a SQLite database.
 */
@Singleton
public class WeaponTemplateDaoDbImpl extends BaseDaoDbImpl<WeaponTemplate> implements WeaponTemplateDao, WeaponTemplateSchema {
    /**
     * Creates a new instance of WeaponDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public WeaponTemplateDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    public WeaponTemplate getById(int id) {
        return super.getById(id);
    }

    @Override
    public boolean save(WeaponTemplate instance) {
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
    protected int getId(WeaponTemplate instance) {
        return instance.getId();
    }

    @Override
    protected void setId(WeaponTemplate instance, int id) {
        instance.setId(id);
    }

    @Override
    protected WeaponTemplate cursorToEntity(@NonNull Cursor cursor) {
        return null;
    }

    @Override
    protected ContentValues getContentValues(WeaponTemplate instance) {
        return null;
    }
}
