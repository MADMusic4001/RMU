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
import com.madinnovations.rmu.data.dao.common.ParameterDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.TalentCategoryDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.common.schemas.TalentParametersSchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentSchema;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.ParameterValue;
import com.madinnovations.rmu.data.entities.common.Talent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Talent} objects in a SQLite database.
 */
@Singleton
public class TalentDaoDbImpl extends BaseDaoDbImpl<Talent> implements TalentDao, TalentSchema {
    private TalentCategoryDao talentCategoryDao;
    private SkillDao skillDao;
    private ParameterDao parameterDao;

    /**
     * Creates a new instance of TalentDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public TalentDaoDbImpl(SQLiteOpenHelper helper, TalentCategoryDao talentCategoryDao, SkillDao skillDao, ParameterDao parameterDao) {
        super(helper);
        this.talentCategoryDao = talentCategoryDao;
        this.skillDao = skillDao;
        this.parameterDao = parameterDao;
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
    protected int getId(Talent instance) {
        return instance.getId();
    }

    @Override
    protected void setId(Talent instance, int id) {
        instance.setId(id);
    }

    @Override
    protected boolean saveRelationships(SQLiteDatabase db, Talent instance) {
        return saveParameters(db, instance);
    }

    @Override
    protected Talent cursorToEntity(@NonNull Cursor cursor) {
        Talent instance = new Talent();

        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        instance.setCategory(talentCategoryDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID))));
        instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
        instance.setFlaw(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FLAW)) != 0);
//        if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_AFFECTED_SKILL_ID))) {
//            instance.setAffectedSkill(skillDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AFFECTED_SKILL_ID))));
//        }
        instance.setTier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_TIER)));
        instance.setMaxTiers(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_MAX_TIERS)));
        instance.setDpCost(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_DP_COST)));
        instance.setDpCostPerTier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_DP_COST_PER_TIER)));
//        instance.setBonusPerTier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BONUS_PER_TIER)));
        instance.setSituational(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_SITUATIONAL)) != 0);
        instance.setActionPoints(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_ACTION_POINTS)));
//        instance.setParameterValues(getParameters(instance.getId()));

        return instance;
    }

    @Override
    protected ContentValues getContentValues(Talent instance) {
        ContentValues initialValues = new ContentValues(12);

        initialValues.put(COLUMN_CATEGORY_ID, instance.getCategory().getId());
        initialValues.put(COLUMN_NAME, instance.getName());
        initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
        initialValues.put(COLUMN_IS_FLAW, instance.isFlaw());
//        initialValues.put(COLUMN_AFFECTED_SKILL_ID, instance.getAffectedSkill() != null ?
//                instance.getAffectedSkill().getId() : null);
        initialValues.put(COLUMN_TIER, instance.getTier());
        initialValues.put(COLUMN_MAX_TIERS, instance.getMaxTiers());
        initialValues.put(COLUMN_DP_COST, instance.getDpCost());
        initialValues.put(COLUMN_DP_COST_PER_TIER, instance.getDpCostPerTier());
//        initialValues.put(COLUMN_BONUS_PER_TIER, instance.getBonusPerTier());
        initialValues.put(COLUMN_IS_SITUATIONAL, instance.isSituational());
        initialValues.put(COLUMN_ACTION_POINTS, instance.getActionPoints());

        return initialValues;
    }

    private List<ParameterValue> getParameters(int talentId) {
        final String selectionArgs[] = { String.valueOf(talentId) };
        final String selection = TalentParametersSchema.COLUMN_TALENT_ID + " = ?";

        Cursor cursor = super.query(TalentParametersSchema.TABLE_NAME, TalentParametersSchema.COLUMNS, selection,
                selectionArgs, TalentParametersSchema.COLUMN_PARAMETER_ID);
        List<ParameterValue> list = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(TalentParametersSchema.COLUMN_PARAMETER_ID));
            Parameter instance = parameterDao.getById(id);
            String value = null;
            if(!cursor.isNull(cursor.getColumnIndexOrThrow(TalentParametersSchema.COLUMN_VALUE))) {
                value = cursor.getString(cursor.getColumnIndexOrThrow(TalentParametersSchema.COLUMN_VALUE));
            }
            if(instance != null) {
                list.add(new ParameterValue(instance, value));
            }
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    private boolean saveParameters(SQLiteDatabase db, Talent instance) {
        boolean result;
        final String selectionArgs[] = { String.valueOf(instance.getId()) };
        final String selection = TalentParametersSchema.COLUMN_TALENT_ID + " = ?";
        ContentValues contentValues = new ContentValues();

        // Delete all current parameters for this talent and then recreate them
        result = (db.delete(TalentParametersSchema.TABLE_NAME, selection, selectionArgs) != -1);

        contentValues.put(TalentParametersSchema.COLUMN_TALENT_ID, instance.getId());
        for(ParameterValue parameterValue : instance.getParameterValues()) {
            contentValues.put(TalentParametersSchema.COLUMN_PARAMETER_ID, parameterValue.getParameter().getId());
            contentValues.put(TalentParametersSchema.COLUMN_VALUE, parameterValue.getValue());
            result &= (db.insertWithOnConflict(TalentParametersSchema.TABLE_NAME, null, contentValues,
                                               SQLiteDatabase.CONFLICT_NONE) != -1);
        }
        return result;
    }
}
