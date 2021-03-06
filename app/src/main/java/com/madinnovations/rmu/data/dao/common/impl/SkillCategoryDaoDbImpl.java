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
import android.util.LruCache;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.CacheConfig;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCategorySchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCategoryStatsSchema;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Statistic;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link SkillCategory} objects in a SQLite database.
 */
@Singleton
public class SkillCategoryDaoDbImpl extends BaseDaoDbImpl<SkillCategory> implements SkillCategoryDao, SkillCategorySchema {
    private LruCache<Integer, SkillCategory> skillCategories = new LruCache<>(CacheConfig.SKILL_CATEGORY_CACHE_SIZE);

    /**
     * Creates a new instance of SkillCategoryDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public SkillCategoryDaoDbImpl(SQLiteOpenHelper helper) {
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
    protected String getSortString() {
        return COLUMN_NAME;
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
    protected LruCache<Integer, SkillCategory> getCache() {
        return skillCategories;
    }

    @Override
    public List<SkillCategory> getCombatCategories() {
        final String selectionArgs[] = { "1" };
        final String selection = COLUMN_IS_COMBAT + " = ?";
        SkillCategory instance;
        List<SkillCategory> skillCategories = null;

        SQLiteDatabase db = helper.getReadableDatabase();
        boolean newTransaction = !db.inTransaction();
        if(newTransaction) {
            db.beginTransaction();
        }
        try {
            Cursor cursor = query(getTableName(), getColumns(), selection,
                    selectionArgs, getIdColumnName());
            if (cursor != null) {
                skillCategories = new ArrayList<>(cursor.getCount());
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    instance = cursorToEntity(cursor);
                    cursor.moveToNext();
                    skillCategories.add(instance);
                }
                cursor.close();
            }
        }
        finally {
            if(newTransaction) {
                db.endTransaction();
            }
        }

        return skillCategories;
    }

    @Override
    protected SkillCategory cursorToEntity(@NonNull Cursor cursor) {
        SkillCategory instance;

        instance = new SkillCategory();
        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
        instance.setCombat(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMBAT)) != 0);
		instance.setCraftAndTrade(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_CRAFT_AND_TRADE)) != 0);
        instance.setNoStats(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NO_STATS)) != 0);
        instance.setRealmStats(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REALM_STATS)) != 0);
        if(!instance.isNoStats()) {
            instance.setStats(getStats(instance.getId()));
        }

        return instance;
    }

    @Override
    protected ContentValues getContentValues(SkillCategory instance) {
        ContentValues values;

        if(instance.getId() != -1) {
            values = new ContentValues(7);
            values.put(COLUMN_ID, instance.getId());
        }
        else {
            values = new ContentValues(6);
        }
        values.put(COLUMN_NAME, instance.getName());
        values.put(COLUMN_DESCRIPTION, instance.getDescription());
        values.put(COLUMN_IS_COMBAT, instance.isCombat());
		values.put(COLUMN_IS_CRAFT_AND_TRADE, instance.isCraftAndTrade());
        values.put(COLUMN_NO_STATS, instance.isNoStats());
        values.put(COLUMN_REALM_STATS, instance.isRealmStats());

        return values;
    }

    @Override
    protected boolean saveRelationships(SQLiteDatabase db, SkillCategory instance) {
        boolean result = true;
        final String selectionArgs[] = { String.valueOf(instance.getId()) };
        final String selection = SkillCategoryStatsSchema.COLUMN_SKILL_CATEGORY_ID + " = ?";

        db.delete(SkillCategoryStatsSchema.TABLE_NAME, selection, selectionArgs);

        if(instance.getStats() != null) {
            for (Statistic stat : instance.getStats()) {
                result &= (db.insertWithOnConflict(SkillCategoryStatsSchema.TABLE_NAME, null,
												   getSkillCategoryStat(instance.getId(), stat),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
            }
        }
        return result;
    }

    @Override
    protected boolean deleteRelationships(SQLiteDatabase db, int id) {
        final String selectionArgs[] = { String.valueOf(id) };
        final String selection = SkillCategoryStatsSchema.COLUMN_SKILL_CATEGORY_ID + " = ?";

        return db.delete(SkillCategoryStatsSchema.TABLE_NAME, selection, selectionArgs) >= 0;
    }

    private ContentValues getSkillCategoryStat(int skillCategoryId, Statistic stat) {
        ContentValues values = new ContentValues(2);

        values.put(SkillCategoryStatsSchema.COLUMN_SKILL_CATEGORY_ID, skillCategoryId);
        values.put(SkillCategoryStatsSchema.COLUMN_STAT_NAME, stat.name());

        return values;
    }

    private List<Statistic> getStats(int skillCategoryId) {
        final String selectionArgs[] = { String.valueOf(skillCategoryId) };
        final String selection = SkillCategoryStatsSchema.COLUMN_SKILL_CATEGORY_ID + " = ?";

        Cursor cursor = super.query(SkillCategoryStatsSchema.TABLE_NAME, SkillCategoryStatsSchema.COLUMNS, selection,
                selectionArgs, SkillCategoryStatsSchema.COLUMN_STAT_NAME);
        List<Statistic> list = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
			list.add(Statistic.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SkillCategoryStatsSchema.COLUMN_STAT_NAME))));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }
}
