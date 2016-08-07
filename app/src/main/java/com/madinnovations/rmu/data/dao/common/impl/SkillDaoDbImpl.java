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
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.schemas.SkillSchema;
import com.madinnovations.rmu.data.entities.common.Skill;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Skill} objects in a SQLite database.
 */
@Singleton
public class SkillDaoDbImpl extends BaseDaoDbImpl<Skill> implements SkillDao, SkillSchema {
    private SkillCategoryDao skillCategoryDao;

    /**
     * Creates a new instance of SkillDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public SkillDaoDbImpl(SQLiteOpenHelper helper, SkillCategoryDao skillCategoryDao) {
        super(helper);
        this.skillCategoryDao = skillCategoryDao;
    }

    @Override
    public Skill getById(int id) {
        return super.getById(id);
    }

    @Override
    public boolean save(Skill instance) {
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
    protected int getId(Skill instance) {
        return instance.getId();
    }

    @Override
    protected void setId(Skill instance, int id) {
        instance.setId(id);
    }

    @Override
    protected Skill cursorToEntity(Cursor cursor) {
        Skill instance = null;

        if (cursor != null) {
            instance = new Skill();
            instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
            instance.setCategory(skillCategoryDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID))));
        }
        return instance;
    }

    @Override
    protected ContentValues getContentValues(Skill instance) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_NAME, instance.getName());
        initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
        initialValues.put(COLUMN_CATEGORY_ID, instance.getCategory().getId());
        return initialValues;
    }
}
