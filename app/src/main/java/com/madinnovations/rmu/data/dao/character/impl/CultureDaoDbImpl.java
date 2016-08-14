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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.CultureDao;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSkillBlackListSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSkillRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSkillWhiteListSchema;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Culture} objects in a SQLite database.
 */
@Singleton
public class CultureDaoDbImpl extends BaseDaoDbImpl<Culture> implements CultureDao, CultureSchema {
    private SkillCategoryDao skillCategoryDao;
    private SkillDao skillDao;

    /**
     * Creates a new CultureDaoDbImpl instance
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public CultureDaoDbImpl(SQLiteOpenHelper helper, SkillCategoryDao skillCategoryDao, SkillDao skillDao) {
        super(helper);
        this.skillCategoryDao = skillCategoryDao;
        this.skillDao = skillDao;
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
    protected Culture cursorToEntity(@NonNull Cursor cursor) {
        Culture instance = new Culture();

        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
        instance.setSkillCategoryRanks(getSkillCategoryRanks(instance.getId()));
        instance.setBlackList(getList(instance.getId(), CultureSkillBlackListSchema.TABLE_NAME,
                CultureSkillBlackListSchema.COLUMN_CULTURE_ID, CultureSkillBlackListSchema.COLUMN_SKILL_ID,
                CultureSkillBlackListSchema.COLUMNS));
        instance.setWhiteList(getList(instance.getId(), CultureSkillWhiteListSchema.TABLE_NAME,
                CultureSkillWhiteListSchema.COLUMN_CULTURE_ID, CultureSkillWhiteListSchema.COLUMN_SKILL_ID,
                CultureSkillWhiteListSchema.COLUMNS));

        return instance;
    }

    @Override
	protected ContentValues getContentValues(Culture instance) {
        ContentValues initialValues = new ContentValues(3);

        initialValues.put(COLUMN_NAME, instance.getName());
        initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());

        return initialValues;
	}

    @Override
    protected boolean saveRelationships(SQLiteDatabase db, Culture instance) {
        boolean result = saveCultureSkillRanks(db, instance.getId(), instance.getSkillCategoryRanks());
        result &= saveList(db, instance.getId(), instance.getBlackList(), CultureSkillBlackListSchema.TABLE_NAME,
                CultureSkillBlackListSchema.COLUMN_CULTURE_ID, CultureSkillBlackListSchema.COLUMN_SKILL_ID);
        result &= saveList(db, instance.getId(), instance.getWhiteList(), CultureSkillWhiteListSchema.TABLE_NAME,
                CultureSkillWhiteListSchema.COLUMN_CULTURE_ID, CultureSkillWhiteListSchema.COLUMN_SKILL_ID);
        return result;
    }

    private boolean saveCultureSkillRanks(SQLiteDatabase db, int cultureId, Map<SkillCategory, Short> rankMap) {
        boolean result = true;
        final String selectionArgs[] = { String.valueOf(cultureId) };
        final String selection = CultureSkillRanksSchema.COLUMN_CULTURE_ID + " = ?";

        db.delete(CultureSkillRanksSchema.TABLE_NAME, selection, selectionArgs);

        for(Map.Entry<SkillCategory, Short> entry : rankMap.entrySet()) {
            result &= (db.insert(CultureSkillRanksSchema.TABLE_NAME, null, getCultureSkillRanksValues(cultureId,
                    entry.getKey(), entry.getValue())) != -1);
        }
        return result;
    }

    private ContentValues getCultureSkillRanksValues(int cultureId, SkillCategory skillCategory, Short ranks) {
        ContentValues values = new ContentValues(4);

        values.put(CultureSkillRanksSchema.COLUMN_CULTURE_ID, cultureId);
        values.put(CultureSkillRanksSchema.COLUMN_SKILL_CATEGORY_ID, skillCategory.getId());
        values.put(CultureSkillRanksSchema.COLUMN_SKILL_RANKS, ranks);

        return values;
    }

    private boolean saveList(SQLiteDatabase db, int cultureId, List<Skill> skillList, String tableName, String cultureIdColumnName,
                             String skillIdColumnName) {
        boolean result = true;
        final String selectionArgs[] = { String.valueOf(cultureId) };
        final String selection = cultureIdColumnName + " = ?";

        db.delete(tableName, selection, selectionArgs);

        for(Skill skill : skillList) {
            result &= (db.insert(tableName, null, getListContentValues(cultureId, skill, cultureIdColumnName, skillIdColumnName)) != -1);
        }
        return result;
    }

    private ContentValues getListContentValues(int cultureId, Skill skill, String cultureIdColumnName, String skillIdColumnName) {
        ContentValues values = new ContentValues();
        values.put(cultureIdColumnName, cultureId);
        values.put(skillIdColumnName, skill.getId());
        return values;
    }

    private Map<SkillCategory, Short> getSkillCategoryRanks(int id) {
        final String selectionArgs[] = { String.valueOf(id) };
        final String selection = CultureSkillRanksSchema.COLUMN_CULTURE_ID + " = ?";

        Cursor cursor = super.query(CultureSkillRanksSchema.TABLE_NAME, CultureSkillRanksSchema.COLUMNS, selection,
                selectionArgs, CultureSkillRanksSchema.COLUMN_SKILL_CATEGORY_ID);
        Map<SkillCategory, Short> map = new HashMap<>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CultureSkillRanksSchema.COLUMN_SKILL_CATEGORY_ID));
            SkillCategory instance = skillCategoryDao.getById(mappedId);
            if(instance != null) {
                map.put(instance, cursor.getShort(cursor.getColumnIndexOrThrow(CharacterSkillsSchema.COLUMN_RANKS)));
            }
            cursor.moveToNext();
        }
        cursor.close();

        return map;
    }

    private List<Skill> getList(int id, String tableName, String cultureIdColumnName, String skillIdColumnName, String[] columns) {
        final String selectionArgs[] = {String.valueOf(id)};
        final String selection = cultureIdColumnName + "=?";

        Cursor cursor = super.query(tableName, columns, selection, selectionArgs,
                skillIdColumnName);
        List<Skill> list = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int skillId = cursor.getInt(cursor.getColumnIndexOrThrow(skillIdColumnName));
            Skill instance = skillDao.getById(skillId);
            if(instance != null) {
                list.add(instance);
            }
        }
        return list;
    }
}
