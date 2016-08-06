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
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCategorySchema;
import com.madinnovations.rmu.data.entities.common.SkillCategory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link SkillCategory} objects in a SQLite database.
 */
@Singleton
public class SkillCategoryDaoDbImpl extends BaseDaoDbImpl<SkillCategory> implements SkillCategoryDao, SkillCategorySchema {
    private StatDao statDao;

    /**
     * Creates a new instance of SkillCategoryDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public SkillCategoryDaoDbImpl(SQLiteOpenHelper helper, StatDao statDao) {
        super(helper);
        this.statDao = statDao;
    }

    @Override
    public SkillCategory getById(int id) {
        return super.getById(id);
    }

    @Override
    public boolean save(SkillCategory instance) {
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
    protected int getId(SkillCategory instance) {
        return instance.getId();
    }

    @Override
    protected void setId(SkillCategory instance, int id) {
        instance.setId(id);
    }

    @Override
    protected SkillCategory cursorToEntity(Cursor cursor) {
        SkillCategory instance = null;

        if (cursor != null) {
            instance = new SkillCategory();
            instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
            instance.setStat1(statDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STAT1_ID))));
            instance.setStat2(statDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STAT2_ID))));
            instance.setStat3(statDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STAT3_ID))));
        }
        return instance;
    }

    @Override
    protected ContentValues getContentValues(SkillCategory instance) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_NAME, instance.getName());
        initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
        initialValues.put(COLUMN_STAT1_ID, instance.getStat1().getId());
        initialValues.put(COLUMN_STAT1_ID, instance.getStat1().getId());
        initialValues.put(COLUMN_STAT2_ID, instance.getStat2().getId());
        initialValues.put(COLUMN_STAT3_ID, instance.getStat3().getId());
        return initialValues;
    }
}
