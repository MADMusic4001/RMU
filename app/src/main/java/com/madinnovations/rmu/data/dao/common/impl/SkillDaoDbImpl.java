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
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.schemas.SkillSchema;
import com.madinnovations.rmu.data.entities.common.Skill;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Skill} objects in a SQLite database.
 */
@Singleton
public class SkillDaoDbImpl extends BaseDaoDbImpl<Skill> implements SkillDao, SkillSchema {
    /**
     * Creates a new instance of SkillDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public SkillDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    public Skill getById(int id) {
        return null;
    }

    @Override
    public List<Skill> getAll() {
        return null;
    }

    @Override
    public boolean save(Skill instance) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    @Override
    protected Skill cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    protected ContentValues getContentValues(Skill instance) {
        return null;
    }
}
