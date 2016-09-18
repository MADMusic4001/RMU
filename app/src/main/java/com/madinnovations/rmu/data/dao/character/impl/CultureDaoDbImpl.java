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
import com.madinnovations.rmu.data.dao.character.schemas.CultureSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSkillRanksSchema;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.common.Skill;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Culture} objects in a SQLite database.
 */
@Singleton
public class CultureDaoDbImpl extends BaseDaoDbImpl<Culture> implements CultureDao, CultureSchema {
    private SkillDao skillDao;

    /**
     * Creates a new CultureDaoDbImpl instance
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public CultureDaoDbImpl(SQLiteOpenHelper helper, SkillDao skillDao) {
        super(helper);
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
        instance.setTradesAndCraftsRanks(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_TRADES_AND_CRAFTS_RANKS)));
        instance.setSkillRanks(getSkillRanks(instance.getId()));

        return instance;
    }

    @Override
	protected ContentValues getContentValues(Culture instance) {
        ContentValues initialValues = new ContentValues(3);

        initialValues.put(COLUMN_NAME, instance.getName());
        initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
		initialValues.put(COLUMN_TRADES_AND_CRAFTS_RANKS, instance.getTradesAndCraftsRanks());

        return initialValues;
	}

    @Override
    protected boolean saveRelationships(SQLiteDatabase db, Culture instance) {
        return saveCultureSkillRanks(db, instance.getId(), instance.getSkillRanks());
    }

    private boolean saveCultureSkillRanks(SQLiteDatabase db, int cultureId, Map<Skill, Short> rankMap) {
        boolean result = true;
        final String selectionArgs[] = { String.valueOf(cultureId) };
        final String selection = CultureSkillRanksSchema.COLUMN_CULTURE_ID + " = ?";

        db.delete(CultureSkillRanksSchema.TABLE_NAME, selection, selectionArgs);

        for(Map.Entry<Skill, Short> entry : rankMap.entrySet()) {
            result &= (db.insertWithOnConflict(CultureSkillRanksSchema.TABLE_NAME, null, getCultureSkillRanksValues(cultureId,
                    entry.getKey(), entry.getValue()), SQLiteDatabase.CONFLICT_NONE) != -1);
        }
        return result;
    }

    private ContentValues getCultureSkillRanksValues(int cultureId, Skill skill, Short ranks) {
        ContentValues values = new ContentValues(3);

        values.put(CultureSkillRanksSchema.COLUMN_CULTURE_ID, cultureId);
        values.put(CultureSkillRanksSchema.COLUMN_SKILL_ID, skill.getId());
        values.put(CultureSkillRanksSchema.COLUMN_SKILL_RANKS, ranks);

        return values;
    }

    private Map<Skill, Short> getSkillRanks(int id) {
        final String selectionArgs[] = { String.valueOf(id) };
        final String selection = CultureSkillRanksSchema.COLUMN_CULTURE_ID + " = ?";

        Cursor cursor = super.query(CultureSkillRanksSchema.TABLE_NAME, CultureSkillRanksSchema.COLUMNS, selection,
                selectionArgs, CultureSkillRanksSchema.COLUMN_SKILL_ID);
        Map<Skill, Short> map = new HashMap<>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CultureSkillRanksSchema.COLUMN_SKILL_ID));
            Skill instance = skillDao.getById(mappedId);
            if(instance != null) {
                map.put(instance, cursor.getShort(cursor.getColumnIndexOrThrow(CultureSkillRanksSchema.COLUMN_SKILL_RANKS)));
            }
            cursor.moveToNext();
        }
        cursor.close();

        return map;
    }
}
