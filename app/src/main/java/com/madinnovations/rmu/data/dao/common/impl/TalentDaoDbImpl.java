/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
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
import com.madinnovations.rmu.data.dao.common.schemas.TalentParametersPerUnitSchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentParametersSchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentSchema;
import com.madinnovations.rmu.data.entities.combat.Action;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.data.entities.common.TalentParameterRow;
import com.madinnovations.rmu.data.entities.common.UnitType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
		return cursorToEntity(cursor, talentCategoryDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID))));
    }

    @Override
    protected ContentValues getContentValues(Talent instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(12);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(11);
		}
        values.put(COLUMN_CATEGORY_ID, instance.getCategory().getId());
        values.put(COLUMN_NAME, instance.getName());
        values.put(COLUMN_DESCRIPTION, instance.getDescription());
        values.put(COLUMN_IS_FLAW, instance.isFlaw());
        values.put(COLUMN_MIN_TIER, instance.getMinTier());
        values.put(COLUMN_MAX_TIER, instance.getMaxTier());
        values.put(COLUMN_DP_COST, instance.getDpCost());
        values.put(COLUMN_DP_COST_PER_TIER, instance.getDpCostPerTier());
        values.put(COLUMN_IS_SITUATIONAL, instance.isSituational());
        values.put(COLUMN_ACTION, instance.getAction().name());
		values.put(COLUMN_IS_CREATURE_ONLY, instance.isCreatureOnly());

        return values;
    }

	@Override
	public Collection<Talent> getTalentsForTalentCategory(@NonNull TalentCategory filter) {
		final String selectionArgs[] = { String.valueOf(filter.getId()) };
		final String selection = COLUMN_CATEGORY_ID + " = ?";
		Collection<Talent> list = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection, selectionArgs, getIdColumnName());

			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Talent instance = cursorToEntity(cursor, filter);

					list.add(instance);
					cursor.moveToNext();
				}
				cursor.close();
			}
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}

		return list;
	}

	private Talent cursorToEntity(@NonNull Cursor cursor, TalentCategory category) {
		Talent instance = new Talent();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setCategory(category);
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		instance.setFlaw(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FLAW)) != 0);
		instance.setMinTier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_MIN_TIER)));
		instance.setMaxTier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_MAX_TIER)));
		instance.setDpCost(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_DP_COST)));
		instance.setDpCostPerTier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_DP_COST_PER_TIER)));
		instance.setSituational(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_SITUATIONAL)) != 0);
		instance.setAction(Action.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTION))));
		instance.setTalentParameterRows(getTalentParameterRows(instance.getId()));
		for(int i = 0; i < instance.getTalentParameterRows().length; i++) {
			getTalentParameterPerUnitValues(instance.getId(), i, instance.getTalentParameterRows()[i]);
		}
		instance.setCreatureOnly(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_CREATURE_ONLY)) != 0);

		return instance;
	}

	@Override
	protected boolean deleteRelationships(SQLiteDatabase db, int id) {
		boolean result;
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = TalentParametersSchema.COLUMN_TALENT_ID + " = ?";

		// Delete all current effects for this talent and then recreate them
		result = (db.delete(TalentParametersSchema.TABLE_NAME, selection, selectionArgs) != -1);
		return result;
	}

	private boolean saveTalentParameters(SQLiteDatabase db, Talent instance) {
		boolean result;
		final String selectionArgs[] = { String.valueOf(	instance.getId()) };
		final String selection = TalentParametersSchema.COLUMN_TALENT_ID + " = ?";

		// Delete all parameter per unit values
		result = (db.delete(TalentParametersPerUnitSchema.TABLE_NAME, selection, selectionArgs) != -1);

		// Delete all current parameters for this talent and then recreate them
		result &= (db.delete(TalentParametersSchema.TABLE_NAME, selection, selectionArgs) != -1);
		ContentValues contentValues = new ContentValues(9);

		// Save new parameter rows
		for(int i = 0; i < instance.getTalentParameterRows().length; i++) {
			TalentParameterRow talentParameterRow = instance.getTalentParameterRows()[i];
			setTalentParameterContentValues(contentValues, instance.getId(), i, talentParameterRow);
			result &= (db.insertWithOnConflict(TalentParametersSchema.TABLE_NAME, null, contentValues,
											   SQLiteDatabase.CONFLICT_NONE) != -1);
			for(int j = 0; j < talentParameterRow.getPerValues().length; j++) {
				ContentValues perUnitContentValues = setTalentParameterPerUnitContentValues(instance.getId(), i, j,
																							talentParameterRow);
				result &= (db.insertWithOnConflict(TalentParametersPerUnitSchema.TABLE_NAME, null, perUnitContentValues,
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

		return result;
	}

	private void setTalentParameterContentValues(ContentValues contentValues, int talentId, int rowIndex,
												 TalentParameterRow talentParameterRow) {
		contentValues.put(TalentParametersSchema.COLUMN_TALENT_ID, talentId);
		contentValues.put(TalentParametersSchema.COLUMN_ROW_INDEX, rowIndex);
		contentValues.put(TalentParametersSchema.COLUMN_PARAMETER_NAME, talentParameterRow.getParameter().name());
		if(talentParameterRow.getInitialValue() == null) {
			contentValues.putNull(TalentParametersSchema.COLUMN_INITIAL_VALUE);
		}
		else {
			contentValues.put(TalentParametersSchema.COLUMN_INITIAL_VALUE, talentParameterRow.getInitialValue());
		}
		if(talentParameterRow.getEnumName() != null) {
			contentValues.put(TalentParametersSchema.COLUMN_ENUM_NAME, talentParameterRow.getEnumName());
		}
		else {
			contentValues.putNull(TalentParametersSchema.COLUMN_ENUM_NAME);
		}
	}

	private TalentParameterRow[] getTalentParameterRows(int talentId) {
		final String selectionArgs[] = { String.valueOf(talentId) };
		final String selection = TalentParametersSchema.COLUMN_TALENT_ID + " = ?";

		Cursor cursor = super.query(TalentParametersSchema.TABLE_NAME, TalentParametersSchema.COLUMNS, selection,
									selectionArgs, TalentParametersSchema.COLUMN_ROW_INDEX);
		TalentParameterRow[] rows = new TalentParameterRow[cursor.getCount()];
		int index;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TalentParameterRow row = new TalentParameterRow();
			index = cursor.getInt(cursor.getColumnIndexOrThrow(TalentParametersSchema.COLUMN_ROW_INDEX));
			Parameter parameter = Parameter.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(
					TalentParametersSchema.COLUMN_PARAMETER_NAME)));
			row.setParameter(parameter);
			if(cursor.isNull(cursor.getColumnIndexOrThrow(TalentParametersSchema.COLUMN_INITIAL_VALUE))) {
				row.setInitialValue(null);
			}
			else {
				row.setInitialValue(cursor.getInt(cursor.getColumnIndexOrThrow(TalentParametersSchema.COLUMN_INITIAL_VALUE)));
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

	private ContentValues setTalentParameterPerUnitContentValues(int talentId, int rowIndex, int unitIndex,
												 TalentParameterRow talentParameterRow) {
		ContentValues contentValues = new ContentValues(5);

		contentValues.put(TalentParametersPerUnitSchema.COLUMN_TALENT_ID, talentId);
		contentValues.put(TalentParametersPerUnitSchema.COLUMN_PARAMETER_ROW_INDEX, rowIndex);
		contentValues.put(TalentParametersPerUnitSchema.COLUMN_PER_UNIT_INDEX, unitIndex);
		contentValues.put(TalentParametersPerUnitSchema.COLUMN_PER_VALUE,
						  talentParameterRow.getPerValues()[unitIndex].doubleValue());
		contentValues.put(TalentParametersPerUnitSchema.COLUMN_UNIT_TYPE_NAME,
						  talentParameterRow.getUnitTypes()[unitIndex].name());

		return contentValues;
	}

	private boolean getTalentParameterPerUnitValues(int talentId, int parameterRowIndex, TalentParameterRow talentParameterRow) {
		final String selectionArgs[] = { String.valueOf(talentId), String.valueOf(parameterRowIndex) };
		final String selection = TalentParametersPerUnitSchema.COLUMN_TALENT_ID + " = ? AND "
				+ TalentParametersPerUnitSchema.COLUMN_PARAMETER_ROW_INDEX + " = ?";

		Cursor cursor = super.query(TalentParametersPerUnitSchema.TABLE_NAME, TalentParametersPerUnitSchema.COLUMNS, selection,
									selectionArgs, TalentParametersPerUnitSchema.COLUMN_PER_UNIT_INDEX);
		List<BigDecimal> valuesList = new ArrayList<>(cursor.getCount());
		List<UnitType> unitTypesList = new ArrayList<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			valuesList.add(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(
					TalentParametersPerUnitSchema.COLUMN_PER_VALUE))));
			unitTypesList.add(UnitType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(
					TalentParametersPerUnitSchema.COLUMN_UNIT_TYPE_NAME))));
			cursor.moveToNext();
		}
		cursor.close();

		BigDecimal[] perValues = new BigDecimal[valuesList.size()];
		valuesList.toArray(perValues);
		talentParameterRow.setPerValues(perValues);
		UnitType[] unitTypes = new UnitType[unitTypesList.size()];
		unitTypesList.toArray(unitTypes);
		talentParameterRow.setUnitTypes(unitTypes);

		return true;
	}
}
