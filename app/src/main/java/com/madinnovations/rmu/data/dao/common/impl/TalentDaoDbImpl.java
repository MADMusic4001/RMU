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
import com.madinnovations.rmu.data.dao.common.TalentCategoryDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.common.schemas.TalentParametersSchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentSchema;
import com.madinnovations.rmu.data.entities.combat.Action;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentParameterRow;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Talent} objects in a SQLite database.
 */
@Singleton
public class TalentDaoDbImpl extends BaseDaoDbImpl<Talent> implements TalentDao, TalentSchema {
    private TalentCategoryDao talentCategoryDao;

    /**
     * Creates a new instance of TalentDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public TalentDaoDbImpl(SQLiteOpenHelper helper, TalentCategoryDao talentCategoryDao) {
        super(helper);
        this.talentCategoryDao = talentCategoryDao;
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
        return saveTalentParameters(db, instance);
    }

    @Override
    protected Talent cursorToEntity(@NonNull Cursor cursor) {
        Talent instance = new Talent();

        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        instance.setCategory(talentCategoryDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID))));
        instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
        instance.setFlaw(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FLAW)) != 0);
        instance.setMinTier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_MIN_TIER)));
        instance.setMaxTier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_MAX_TIER)));
        instance.setDpCost(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_DP_COST)));
        instance.setDpCostPerTier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_DP_COST_PER_TIER)));
        instance.setSituational(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_SITUATIONAL)) != 0);
        instance.setAction(Action.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTION))));
		instance.setTalentParameterRows(getTalentEffectRows(instance.getId()));

        return instance;
    }

    @Override
    protected ContentValues getContentValues(Talent instance) {
        ContentValues initialValues = new ContentValues(10);

        initialValues.put(COLUMN_CATEGORY_ID, instance.getCategory().getId());
        initialValues.put(COLUMN_NAME, instance.getName());
        initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
        initialValues.put(COLUMN_IS_FLAW, instance.isFlaw());
        initialValues.put(COLUMN_MIN_TIER, instance.getMinTier());
        initialValues.put(COLUMN_MAX_TIER, instance.getMaxTier());
        initialValues.put(COLUMN_DP_COST, instance.getDpCost());
        initialValues.put(COLUMN_DP_COST_PER_TIER, instance.getDpCostPerTier());
        initialValues.put(COLUMN_IS_SITUATIONAL, instance.isSituational());
        initialValues.put(COLUMN_ACTION, instance.getAction().name());

        return initialValues;
    }

    private boolean saveTalentParameters(SQLiteDatabase db, Talent instance) {
		boolean result;
		final String selectionArgs[] = { String.valueOf(	instance.getId()) };
		final String selection = TalentParametersSchema.COLUMN_TALENT_ID + " = ?";
		ContentValues contentValues = new ContentValues(5);

		// Delete all current effects for this talent and then recreate them
		result = (db.delete(TalentParametersSchema.TABLE_NAME, selection, selectionArgs) != -1);

		contentValues.put(TalentParametersSchema.COLUMN_TALENT_ID, instance.getId());
		for(int i = 0; i < instance.getTalentParameterRows().length; i++) {
			TalentParameterRow talentParameterRow = instance.getTalentParameterRows()[i];
			contentValues.put(TalentParametersSchema.COLUMN_INDEX, i);
			contentValues.put(TalentParametersSchema.COLUMN_EFFECT, talentParameterRow.getParameter().name());
			if(talentParameterRow.getValue() == null) {
				contentValues.putNull(TalentParametersSchema.COLUMN_VALUE);
			}
			else {
				contentValues.put(TalentParametersSchema.COLUMN_VALUE, talentParameterRow.getValue());
			}
			if(talentParameterRow.getEnumName() != null) {
				contentValues.put(TalentParametersSchema.COLUMN_ENUM_NAME, talentParameterRow.getEnumName());
			}
			else {
				contentValues.putNull(TalentParametersSchema.COLUMN_ENUM_NAME);
			}
			result &= (db.insertWithOnConflict(TalentParametersSchema.TABLE_NAME, null, contentValues,
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		return result;
	}

	private TalentParameterRow[] getTalentEffectRows(int talentId) {
		final String selectionArgs[] = { String.valueOf(talentId) };
		final String selection = TalentParametersSchema.COLUMN_TALENT_ID + " = ?";

		Cursor cursor = super.query(TalentParametersSchema.TABLE_NAME, TalentParametersSchema.COLUMNS, selection,
									selectionArgs, TalentParametersSchema.COLUMN_INDEX);
		TalentParameterRow[] rows = new TalentParameterRow[cursor.getCount()];
		TalentParameterRow row = new TalentParameterRow();
		int index;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			index = cursor.getInt(cursor.getColumnIndexOrThrow(TalentParametersSchema.COLUMN_INDEX));
			Parameter parameter = Parameter.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(TalentParametersSchema.COLUMN_EFFECT)));
			row.setParameter(parameter);
			if(cursor.isNull(cursor.getColumnIndexOrThrow(TalentParametersSchema.COLUMN_VALUE))) {
				row.setValue(null);
			}
			else {
				row.setValue(cursor.getInt(cursor.getColumnIndexOrThrow(TalentParametersSchema.COLUMN_VALUE)));
			}
			if(cursor.isNull(cursor.getColumnIndexOrThrow(TalentParametersSchema.COLUMN_ENUM_NAME))) {
				row.setEnumName(null);
			}
			else {
				row.setEnumName(cursor.getString(cursor.getColumnIndexOrThrow(TalentParametersSchema.COLUMN_ENUM_NAME)));
			}
			rows[index] = row;
			cursor.moveToNext();
		}
		cursor.close();

		return rows;
	}
}
