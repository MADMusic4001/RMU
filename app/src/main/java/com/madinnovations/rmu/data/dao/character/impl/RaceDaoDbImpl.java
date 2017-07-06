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
package com.madinnovations.rmu.data.dao.character.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.CultureDao;
import com.madinnovations.rmu.data.dao.character.RaceDao;
import com.madinnovations.rmu.data.dao.character.schemas.RaceCulturesSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceRealmRRModSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceStatModSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceTalentParametersSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceTalentsSchema;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.spells.Realm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Race} objects in a SQLite database.
 */
@Singleton
public class RaceDaoDbImpl extends BaseDaoDbImpl<Race> implements RaceDao, RaceSchema {
	@SuppressWarnings("unused")
	private static final String TAG = "RaceDaoDbImpl";
	private TalentDao talentDao;
	private CultureDao cultureDao;

    /**
     * Creates a new instance of RaceDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
	 * @param talentDao  a {@link TalentDao} instance
	 * @param cultureDao  a {@link CultureDao} instance
     */
    @Inject
    public RaceDaoDbImpl(SQLiteOpenHelper helper, TalentDao talentDao, CultureDao cultureDao) {
        super(helper);
        this.talentDao = talentDao;
		this.cultureDao = cultureDao;
    }

    @Override
    public Race getById(int id) {
        return super.getById(id);
    }

    @Override
    public boolean save(Race instance) {
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
    protected int getId(Race instance) {
        return instance.getId();
    }

    @Override
    protected void setId(Race instance, int id) {
        instance.setId(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Race cursorToEntity(@NonNull Cursor cursor) {
        Race instance = new Race();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		instance.setBonusDevelopmentPoints(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BONUS_DEVELOPMENT_POINTS)));
		instance.setStatModifiers(getStatModifiers(instance.getId()));
		instance.setRealmResistancesModifiers(getRealmResistanceModifiers(instance.getId()));
		instance.setPhysicalResistanceModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_PHYSICAL_RESISTANCE_MODIFIER)));
		instance.setEnduranceModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_ENDURANCE_MODIFIER)));
		instance.setBaseHits(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BASE_HITS)));
		instance.setRecoveryMultiplier(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RECOVERY_MULTIPLIER)));
		instance.setStrideModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_STRIDE_MODIFIER)));
		instance.setAverageHeight(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_AVERAGE_HEIGHT)));
		instance.setAverageWeight(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_AVERAGE_WEIGHT)));
		instance.setPoundsPerInch(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_POUNDS_PER_INCH)));
		instance.setSize(Size.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE))));
		instance.setRealmResistancesModifiers(getRealmResistanceModifiers(instance.getId()));
		instance.setStatModifiers(getStatModifiers(instance.getId()));
		instance.setTalentsAndFlawsList(getTalentsAndFlaws(instance.getId()));
		instance.setAllowedCultures(getAllowedCultures(instance.getId()));

        return instance;
    }

	@Override
	protected ContentValues getContentValues(Race instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(13);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(12);
		}
		values.put(COLUMN_NAME, instance.getName());
		values.put(COLUMN_DESCRIPTION, instance.getDescription());
		values.put(COLUMN_BONUS_DEVELOPMENT_POINTS, instance.getBonusDevelopmentPoints());
		values.put(COLUMN_PHYSICAL_RESISTANCE_MODIFIER, instance.getPhysicalResistanceModifier());
		values.put(COLUMN_ENDURANCE_MODIFIER, instance.getEnduranceModifier());
		values.put(COLUMN_BASE_HITS, instance.getBaseHits());
		values.put(COLUMN_RECOVERY_MULTIPLIER, instance.getRecoveryMultiplier());
		values.put(COLUMN_SIZE, instance.getSize().name());
		values.put(COLUMN_STRIDE_MODIFIER, instance.getStrideModifier());
		values.put(COLUMN_AVERAGE_HEIGHT, instance.getAverageHeight());
		values.put(COLUMN_AVERAGE_WEIGHT, instance.getAverageWeight());
		values.put(COLUMN_POUNDS_PER_INCH, instance.getPoundsPerInch());
		return values;
	}

	@Override
	protected boolean saveRelationships(SQLiteDatabase db, Race instance) {
		boolean result = saveTalentsAndFlaws(db, instance.getId(), instance.getTalentsAndFlawsList());
		result &= saveSatMods(db, instance.getId(), instance.getStatModifiers());
		result &= saveRealmRRMods(db, instance.getId(), instance.getRealmResistancesModifiers());
		result &= saveAllowedCultures(db, instance.getId(), instance.getAllowedCultures());
		return result;
	}

	@Override
	protected boolean deleteRelationships(SQLiteDatabase db, int id) {
		String selectionArgs[] = { String.valueOf(id) };

		String selection = RaceTalentsSchema.COLUMN_RACE_ID + " = ?";
		db.delete(RaceTalentsSchema.TABLE_NAME, selection, selectionArgs);

		selection = RaceStatModSchema.COLUMN_RACE_ID + " = ?";
		db.delete(RaceStatModSchema.TABLE_NAME, selection, selectionArgs);

		selection = RaceRealmRRModSchema.COLUMN_RACE_ID + " = ?";
		db.delete(RaceRealmRRModSchema.TABLE_NAME, selection, selectionArgs);

		selection = RaceCulturesSchema.COLUMN_RACE_ID + " = ?";
		db.delete(RaceCulturesSchema.TABLE_NAME, selection, selectionArgs);

		return true;
	}

	private List<TalentInstance> getTalentsAndFlaws(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = RaceTalentsSchema.COLUMN_RACE_ID + " = ?";

		Cursor cursor = super.query(RaceTalentsSchema.TABLE_NAME, RaceTalentsSchema.COLUMNS, selection,
									selectionArgs, RaceTalentsSchema.COLUMN_TALENT_ID);
		List<TalentInstance> list = new ArrayList<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(RaceTalentsSchema.COLUMN_ID));
			TalentInstance instance = new TalentInstance();
			instance.setId(mappedId);
			Talent talent = talentDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(RaceTalentsSchema.COLUMN_TALENT_ID)));
			instance.setTalent(talent);
			instance.setTiers(cursor.getShort(cursor.getColumnIndexOrThrow(RaceTalentsSchema.COLUMN_TIERS)));
			list.add(instance);
			instance.setParameterValues(getTalentParameters(id, mappedId));
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}

	private boolean saveTalentsAndFlaws(SQLiteDatabase db, int raceId, List<TalentInstance> talentInstancesList) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(raceId) };
		final String selection = RaceTalentsSchema.COLUMN_RACE_ID + " = ?";

		db.delete(RaceTalentParametersSchema.TABLE_NAME, selection, selectionArgs);
		db.delete(RaceTalentsSchema.TABLE_NAME, selection, selectionArgs);

		for(TalentInstance talentInstance : talentInstancesList) {
			int newId = (int)db.insertWithOnConflict(RaceTalentsSchema.TABLE_NAME, null, getTalentTiersValues(
					raceId, talentInstance.getId(), talentInstance.getTalent().getId(), talentInstance.getTiers()),
													 SQLiteDatabase.CONFLICT_NONE);
			talentInstance.setId(newId);
			result &= (newId != -1);
			result &= saveTalentParameterValues(db, raceId, talentInstance.getId(), talentInstance.getParameterValues());
		}
		return result;
	}

	private ContentValues getTalentTiersValues(int raceId, int talentInstanceId, int talentId, Short tiers) {
		ContentValues values;
		if(talentInstanceId != -1) {
			values = new ContentValues(4);
			values.put(RaceTalentsSchema.COLUMN_ID, talentInstanceId);
		}
		else {
			values = new ContentValues(3);
		}
		values.put(RaceTalentsSchema.COLUMN_RACE_ID, raceId);
		values.put(RaceTalentsSchema.COLUMN_TALENT_ID, talentId);
		values.put(RaceTalentsSchema.COLUMN_TIERS, tiers);
		return values;
	}

	private Map<Parameter, Object> getTalentParameters(int raceId, int talentInstanceId) {
		final String selectionArgs[] = { String.valueOf(raceId), String.valueOf(talentInstanceId) };
		final String selection = RaceTalentParametersSchema.COLUMN_RACE_ID +
				" = ? AND " +
				RaceTalentParametersSchema.COLUMN_TALENT_INSTANCE_ID +
				" = ?";

		Cursor cursor = super.query(RaceTalentParametersSchema.TABLE_NAME, RaceTalentParametersSchema.COLUMNS, selection,
									selectionArgs, RaceTalentParametersSchema.COLUMN_PARAMETER_NAME);
		Map<Parameter, Object> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String parameterName = cursor.getString(cursor.getColumnIndexOrThrow(
					RaceTalentParametersSchema.COLUMN_PARAMETER_NAME));
			Parameter instance = Parameter.valueOf(parameterName);
			Object value = null;
			if(!cursor.isNull(cursor.getColumnIndexOrThrow(RaceTalentParametersSchema.COLUMN_INT_VALUE))) {
				value = cursor.getInt(cursor.getColumnIndexOrThrow(RaceTalentParametersSchema.COLUMN_INT_VALUE));
			}
			else if(!cursor.isNull(cursor.getColumnIndexOrThrow(RaceTalentParametersSchema.COLUMN_ENUM_NAME))) {
				value = cursor.getString(cursor.getColumnIndexOrThrow(RaceTalentParametersSchema.COLUMN_ENUM_NAME));
			}
			map.put(instance, value);
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private boolean saveTalentParameterValues(SQLiteDatabase db, int raceId, int talentInstanceId,
											  Map<Parameter, Object> parameterValuesMap) {
		boolean result = true;

		for(Map.Entry<Parameter, Object> entry : parameterValuesMap.entrySet()) {
			result &= (db.insertWithOnConflict(RaceTalentParametersSchema.TABLE_NAME, null,
											   getParameterValuesValues(raceId, talentInstanceId,
																		entry.getKey(), entry.getValue()),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		return result;
	}

	private ContentValues getParameterValuesValues(int raceId, int talentId, Parameter parameter, Object value) {
		ContentValues values = new ContentValues(5);
		values.put(RaceTalentParametersSchema.COLUMN_RACE_ID, raceId);
		values.put(RaceTalentParametersSchema.COLUMN_TALENT_INSTANCE_ID, talentId);
		values.put(RaceTalentParametersSchema.COLUMN_PARAMETER_NAME, parameter.name());
		if(value == null) {
			values.putNull(RaceTalentParametersSchema.COLUMN_INT_VALUE);
			values.putNull(RaceTalentParametersSchema.COLUMN_ENUM_NAME);
		}
		else if(value instanceof Integer) {
			values.put(RaceTalentParametersSchema.COLUMN_INT_VALUE, (Integer)value);
			values.putNull(RaceTalentParametersSchema.COLUMN_ENUM_NAME);
		}
		else {
			values.putNull(RaceTalentParametersSchema.COLUMN_INT_VALUE);
			values.put(RaceTalentParametersSchema.COLUMN_ENUM_NAME, (String)value);
		}
		return values;
	}

	private Map<Statistic, Short> getStatModifiers(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = RaceStatModSchema.COLUMN_RACE_ID + " = ?";

		Cursor cursor = super.query(RaceStatModSchema.TABLE_NAME, RaceStatModSchema.COLUMNS, selection,
									selectionArgs, RaceStatModSchema.COLUMN_STAT_NAME);
		Map<Statistic, Short> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Statistic statistic = Statistic.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(
					RaceStatModSchema.COLUMN_STAT_NAME)));
			map.put(statistic, cursor.getShort(cursor.getColumnIndexOrThrow(RaceStatModSchema.COLUMN_MODIFIER)));
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private boolean saveSatMods(SQLiteDatabase db, int raceId, Map<Statistic, Short> statMods) {
		boolean result = true;
		final String selectionArgs[] = {String.valueOf(raceId) };
		final String selection = RaceStatModSchema.COLUMN_RACE_ID + " = ?";

		db.delete(RaceStatModSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<Statistic, Short> entry : statMods.entrySet()) {
			result &= (db.insertWithOnConflict(RaceStatModSchema.TABLE_NAME, null, getRaceStatModValues(raceId, entry),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		return result;
	}

	private ContentValues getRaceStatModValues(int raceId, Map.Entry<Statistic, Short> statModEntry) {
		ContentValues values = new ContentValues(3);
		values.put(RaceStatModSchema.COLUMN_RACE_ID, raceId);
		values.put(RaceStatModSchema.COLUMN_STAT_NAME, statModEntry.getKey().name());
		values.put(RaceStatModSchema.COLUMN_MODIFIER, statModEntry.getValue());
		return values;
	}

	private Map<Realm, Short> getRealmResistanceModifiers(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = RaceRealmRRModSchema.COLUMN_RACE_ID + " = ?";

		Cursor cursor = super.query(RaceRealmRRModSchema.TABLE_NAME, RaceRealmRRModSchema.COLUMNS, selection,
									selectionArgs, RaceRealmRRModSchema.COLUMN_REALM);
		Map<Realm, Short> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Realm instance = Realm.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(RaceRealmRRModSchema.COLUMN_REALM)));
			map.put(instance, cursor.getShort(cursor.getColumnIndexOrThrow(RaceRealmRRModSchema.COLUMN_MODIFIER)));
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private boolean saveRealmRRMods(SQLiteDatabase db, int raceId, Map<Realm, Short> realmRRMods) {
		boolean result = true;
		final String selectionArgs[] = {String.valueOf(raceId) };
		final String selection = RaceRealmRRModSchema.COLUMN_RACE_ID + " = ?";

		db.delete(RaceRealmRRModSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<Realm, Short> entry : realmRRMods.entrySet()) {
			result &= (db.insertWithOnConflict(RaceRealmRRModSchema.TABLE_NAME, null, getRaceRealmRRModValues(raceId, entry),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		return result;
	}

	private ContentValues getRaceRealmRRModValues(int raceId, Map.Entry<Realm, Short> realmModEntry) {
		ContentValues values = new ContentValues(3);
		values.put(RaceRealmRRModSchema.COLUMN_RACE_ID, raceId);
		values.put(RaceRealmRRModSchema.COLUMN_REALM, realmModEntry.getKey().name());
		values.put(RaceRealmRRModSchema.COLUMN_MODIFIER, realmModEntry.getValue());
		return values;
	}

	private List<Culture> getAllowedCultures(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = RaceTalentsSchema.COLUMN_RACE_ID + " = ?";

		Cursor cursor = super.query(RaceCulturesSchema.TABLE_NAME, RaceCulturesSchema.COLUMNS, selection,
									selectionArgs, RaceCulturesSchema.COLUMN_CULTURE_ID);
		List<Culture> list = new ArrayList<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(RaceCulturesSchema.COLUMN_CULTURE_ID));
			Culture instance = cultureDao.getById(mappedId);
			if(instance != null) {
				list.add(instance);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}

	private boolean saveAllowedCultures(SQLiteDatabase db, int raceId, List<Culture> allowedCultures) {
		boolean result = true;
		final String selectionArgs[] = {String.valueOf(raceId) };
		final String selection = RaceCulturesSchema.COLUMN_RACE_ID + " = ?";

		db.delete(RaceCulturesSchema.TABLE_NAME, selection, selectionArgs);

		for(Culture culture : allowedCultures) {
			result &= (db.insertWithOnConflict(RaceCulturesSchema.TABLE_NAME, null, getAllowedCultureValues(raceId, culture),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		return result;
	}

	private ContentValues getAllowedCultureValues(int raceId, Culture culture) {
		ContentValues values = new ContentValues(2);
		values.put(RaceCulturesSchema.COLUMN_RACE_ID, raceId);
		values.put(RaceCulturesSchema.COLUMN_CULTURE_ID, culture.getId());
		return values;
	}
}
