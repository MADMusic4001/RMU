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
package com.madinnovations.rmu.data.dao.spells.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.spells.SpellDao;
import com.madinnovations.rmu.data.dao.spells.SpellListDao;
import com.madinnovations.rmu.data.dao.spells.SpellSubTypeDao;
import com.madinnovations.rmu.data.dao.spells.SpellTypeDao;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellAreaOfEffectParamSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellDurationParamSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellSchema;
import com.madinnovations.rmu.data.entities.spells.AreaOfEffect;
import com.madinnovations.rmu.data.entities.spells.Duration;
import com.madinnovations.rmu.data.entities.spells.Range;
import com.madinnovations.rmu.data.entities.spells.Spell;
import com.madinnovations.rmu.data.entities.spells.SpellList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Spell} objects in a SQLite database.
 */
@Singleton
public class SpellDaoDbImpl extends BaseDaoDbImpl<Spell> implements SpellDao, SpellSchema {
	private SpellListDao spellListDao;
	private SpellTypeDao spellTypeDao;
	private SpellSubTypeDao spellSubTypeDao;

	/**
	 * Creates a new instance of SpellDaoImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 * @param spellListDao  a {@link SpellListDao} instance
	 * @param spellTypeDao  a {@link SpellTypeDao} instance
	 * @param spellSubTypeDao  a {@link SpellSubTypeDao} instance
	 */
	@Inject
	public SpellDaoDbImpl(@NonNull SQLiteOpenHelper helper, @NonNull SpellListDao spellListDao, SpellTypeDao spellTypeDao,
						  SpellSubTypeDao spellSubTypeDao) {
		super(helper);
		this.spellListDao = spellListDao;
		this.spellTypeDao = spellTypeDao;
		this.spellSubTypeDao = spellSubTypeDao;
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
	protected int getId(Spell instance) {
		return instance.getId();
	}

	@Override
	protected void setId(Spell instance, int id) {
		instance.setId(id);
	}

	@Override
	protected Spell cursorToEntity(@NonNull Cursor cursor) {
		return cursorToEntity(cursor, spellListDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SPELL_LIST_ID))));
	}

	@Override
	protected ContentValues getContentValues(Spell instance) {
		ContentValues initialValues = new ContentValues(9);

		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
		initialValues.put(COLUMN_SPELL_LIST_ID, instance.getSpellList().getId());
		initialValues.put(COLUMN_SPELL_TYPE_ID, instance.getSpellType().getId());
		if(instance.getSpellSubType() == null) {
			initialValues.putNull(COLUMN_SPELL_SUB_TYPE_ID);
		}
		else {
			initialValues.put(COLUMN_SPELL_SUB_TYPE_ID, instance.getSpellSubType().getId());
		}
		initialValues.put(COLUMN_AREA_OF_EFFECT, instance.getAreaOfEffect().name());
		initialValues.put(COLUMN_DURATION, instance.getDuration().name());
		initialValues.put(COLUMN_RANGE, instance.getRange().name());
		if(instance.getRangeParam() == null) {
			initialValues.putNull(COLUMN_RANGE_PARAM);
		}
		else {
			initialValues.put(COLUMN_RANGE_PARAM, instance.getRangeParam());
		}

		return initialValues;
	}

	@Override
	protected boolean saveRelationships(SQLiteDatabase db, Spell instance) {
		boolean result = saveAoeParams(db, instance);
		result &= saveDurationParams(db, instance);
		return result;
	}

	@Override
	public List<Spell> getSpellsForList(@NonNull SpellList filter) {
		final String selectionArgs[] = { String.valueOf(filter.getId()) };
		final String selection = COLUMN_SPELL_LIST_ID + " = ?";
		List<Spell> list = new ArrayList<>();

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
					Spell instance = cursorToEntity(cursor, filter);

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

	private boolean saveAoeParams(SQLiteDatabase db, Spell instance) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		final String selection = SpellAreaOfEffectParamSchema.COLUMN_SPELL_ID + " = ?";

		db.delete(SpellAreaOfEffectParamSchema.TABLE_NAME, selection, selectionArgs);

		if(instance.getAreaOfEffectParams() != null) {
			for (int i = 0; i < instance.getAreaOfEffectParams().length; i++) {
				result &= (db.insertWithOnConflict(SpellAreaOfEffectParamSchema.TABLE_NAME, null,
									 getAoeParamContentValues(instance.getId(), i, instance.getAreaOfEffectParams()[i]),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

		return result;
	}

	private ContentValues getAoeParamContentValues(int spellId, int paramIndex, int paramValue) {
		ContentValues values = new ContentValues(3);

		values.put(SpellAreaOfEffectParamSchema.COLUMN_SPELL_ID, spellId);
		values.put(SpellAreaOfEffectParamSchema.COLUMN_PARAM_INDEX, paramIndex);
		values.put(SpellAreaOfEffectParamSchema.COLUMN_PARAM_VALUE, paramValue);

		return values;
	}

	private boolean saveDurationParams(SQLiteDatabase db, Spell instance) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		final String selection = SpellDurationParamSchema.COLUMN_SPELL_ID + " = ?";

		db.delete(SpellDurationParamSchema.TABLE_NAME, selection, selectionArgs);

		if(instance.getDurationParams() != null) {
			for (int i = 0; i < instance.getDurationParams().length; i++) {
				result &= (db.insertWithOnConflict(SpellDurationParamSchema.TABLE_NAME, null,
									 getDurationParamContentValues(instance.getId(), i, instance.getDurationParams()[i]),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

		return result;
	}

	private ContentValues getDurationParamContentValues(int spellId, int paramIndex, int paramValue) {
		ContentValues values = new ContentValues(3);

		values.put(SpellDurationParamSchema.COLUMN_SPELL_ID, spellId);
		values.put(SpellDurationParamSchema.COLUMN_PARAM_INDEX, paramIndex);
		values.put(SpellDurationParamSchema.COLUMN_PARAM_VALUE, paramValue);

		return values;
	}

	private Spell cursorToEntity(@NonNull Cursor cursor, SpellList spellList) {
		Spell instance = new Spell();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		instance.setSpellList(spellList);
		instance.setSpellType(spellTypeDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SPELL_TYPE_ID))));
		instance.setSpellSubType(spellSubTypeDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SPELL_SUB_TYPE_ID))));
		instance.setAreaOfEffect(AreaOfEffect.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AREA_OF_EFFECT))));
		instance.setDuration(Duration.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION))));
		instance.setRange(Range.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RANGE))));
		if(cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_RANGE_PARAM))) {
			instance.setRangeParam(null);
		}
		else {
			instance.setRangeParam(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RANGE_PARAM)));
		}
		instance.setAreaOfEffectParams(getAoeParams(instance.getId()));
		instance.setDurationParams(getDurationParams(instance.getId()));

		return instance;
	}

	private int[] getAoeParams(int spellId) {
		final String selectionArgs[] = { String.valueOf(spellId) };
		final String selection = SpellAreaOfEffectParamSchema.COLUMN_SPELL_ID + " = ?";

		Cursor cursor = super.query(SpellAreaOfEffectParamSchema.TABLE_NAME, SpellAreaOfEffectParamSchema.COLUMNS, selection,
									selectionArgs, SpellAreaOfEffectParamSchema.COLUMN_PARAM_INDEX);
		int[] list = new int[cursor.getCount()];
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int index = cursor.getInt(cursor.getColumnIndexOrThrow(SpellAreaOfEffectParamSchema.COLUMN_PARAM_INDEX));
			int value = cursor.getInt(cursor.getColumnIndexOrThrow(SpellAreaOfEffectParamSchema.COLUMN_PARAM_VALUE));
			list[index] = value;
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}

	private int[] getDurationParams(int spellId) {
		final String selectionArgs[] = { String.valueOf(spellId) };
		final String selection = SpellDurationParamSchema.COLUMN_SPELL_ID + " = ?";

		Cursor cursor = super.query(SpellDurationParamSchema.TABLE_NAME, SpellDurationParamSchema.COLUMNS, selection,
									selectionArgs, SpellDurationParamSchema.COLUMN_PARAM_INDEX);
		int[] list = new int[cursor.getCount()];
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int index = cursor.getInt(cursor.getColumnIndexOrThrow(SpellDurationParamSchema.COLUMN_PARAM_INDEX));
			int value = cursor.getInt(cursor.getColumnIndexOrThrow(SpellDurationParamSchema.COLUMN_PARAM_VALUE));
			list[index] = value;
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}
}
