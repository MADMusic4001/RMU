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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCategoryStatsSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SpecializationSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SpecializationStatsSchema;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Stat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Specialization} objects in a SQLite database.
 */
@Singleton
public class SpecializationDaoDbImpl extends BaseDaoDbImpl<Specialization> implements SpecializationDao, SpecializationSchema {
    private SkillDao skillDao;
    private StatDao statDao;

    /**
     * Creates a new instance of SpecializationDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public SpecializationDaoDbImpl(SQLiteOpenHelper helper, SkillDao skillDao, StatDao statDao) {
        super(helper);
        this.skillDao = skillDao;
        this.statDao = statDao;
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
    protected int getId(Specialization instance) {
        return instance.getId();
    }

    @Override
    protected void setId(Specialization instance, int id) {
        instance.setId(id);
    }

    @Override
    protected Specialization cursorToEntity(@NonNull Cursor cursor) {
        Specialization instance = null;

        instance = new Specialization();
        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
        instance.setSkill(skillDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SKILL_ID))));
        instance.setSkillStats(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SKILL_STATS)) != 0);
        if(!instance.isSkillStats()) {
            instance.setStats(getStats(instance.getId()));
        }

        return instance;
    }

    private List<Stat> getStats(int specializationId) {
        final String selectionArgs[] = { String.valueOf(specializationId) };
        final String selection = SpecializationStatsSchema.COLUMN_SPECIALIZATION_ID + " = ?";

        Cursor cursor = super.query(SpecializationStatsSchema.TABLE_NAME, SpecializationStatsSchema.COLUMNS, selection,
                selectionArgs, SpecializationStatsSchema.COLUMN_STAT_ID);
        List<Stat> list = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(SkillCategoryStatsSchema.COLUMN_STAT_ID));
            Stat instance = statDao.getById(id);
            if(instance != null) {
                list.add(instance);
            }
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    @Override
    protected ContentValues getContentValues(Specialization instance) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_NAME, instance.getName());
        initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
        initialValues.put(COLUMN_SKILL_ID, instance.getSkill().getId());
        initialValues.put(COLUMN_SKILL_STATS, instance.isSkillStats());
        return initialValues;
    }

    @Override
    protected boolean saveRelationships(SQLiteDatabase db, Specialization instance) {
        boolean result = true;
        final String selectionArgs[] = { String.valueOf(instance.getId()) };
        final String selection = SpecializationStatsSchema.COLUMN_SPECIALIZATION_ID + " = ?";

        db.delete(SpecializationStatsSchema.TABLE_NAME, selection, selectionArgs);

        for(Stat stat : instance.getStats()) {
            result &= (db.insert(SpecializationStatsSchema.TABLE_NAME, null, getSpecializationStat(instance.getId(), stat.getId())) != -1);
        }
        return result;
    }

    private ContentValues getSpecializationStat(int specializationId, int statId) {
        ContentValues values = new ContentValues(2);

        values.put(SpecializationStatsSchema.COLUMN_SPECIALIZATION_ID, specializationId);
        values.put(SpecializationStatsSchema.COLUMN_STAT_ID, statId);

        return values;
    }
}
