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
package com.madinnovations.rmu.data.dao.common.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.SkillCostDao;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCostSchema;
import com.madinnovations.rmu.data.entities.common.SkillCost;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link SkillCost} objects in a SQLite database.
 */
@Singleton
public class SkillCostDaoDbImpl extends BaseDaoDbImpl<SkillCost> implements SkillCostDao, SkillCostSchema {
    /**
     * Creates a new instance of SkillCostDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public SkillCostDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    public SkillCost getById(int id) {
        return super.getById(id);
    }

    @Override
    public boolean save(SkillCost instance) {
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
    protected int getId(SkillCost instance) {
        return instance.getId();
    }

    @Override
    protected void setId(SkillCost instance, int id) {
        instance.setId(id);
    }


    @Override
    protected SkillCost cursorToEntity(Cursor cursor) {
        SkillCost instance = null;

        if (cursor != null) {
            instance = new SkillCost();
            instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            instance.setFirstCost(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_SKILL_COST)));
            instance.setAdditionalCost(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_ADDITIONAL_COST)));
        }
        return instance;
    }

    @Override
    protected ContentValues getContentValues(SkillCost instance) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_SKILL_COST, instance.getFirstCost());
        initialValues.put(COLUMN_ADDITIONAL_COST, instance.getAdditionalCost());
        return initialValues;
    }
}
