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
import com.madinnovations.rmu.data.dao.character.RaceDao;
import com.madinnovations.rmu.data.dao.character.schemas.RaceLocomotionSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceRealmRRModSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceStatModSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceTalentsSchema;
import com.madinnovations.rmu.data.dao.common.LocomotionTypeDao;
import com.madinnovations.rmu.data.dao.common.SizeDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.common.LocomotionType;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.spells.Realm;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Race} objects in a SQLite database.
 */
@Singleton
public class RaceDaoDbImpl extends BaseDaoDbImpl<Race> implements RaceDao, RaceSchema {
    TalentDao talentDao;
    LocomotionTypeDao locomotionTypeDao;
	RealmDao realmDao;
	SizeDao sizeDao;
	StatDao statDao;

    /**
     * Creates a new instance of RaceDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public RaceDaoDbImpl(SQLiteOpenHelper helper, TalentDao talentDao, LocomotionTypeDao locomotionTypeDao, RealmDao realmDao,
						 SizeDao sizeDao, StatDao statDao) {
        super(helper);
        this.talentDao = talentDao;
        this.locomotionTypeDao = locomotionTypeDao;
		this.realmDao = realmDao;
		this.sizeDao = sizeDao;
		this.statDao = statDao;
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
		instance.setSize(sizeDao.getById(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_SIZE_ID))));
		instance.setTalentsAndFlawsTiersMap(getTalentsAndFlaws(instance.getId()));
		instance.setLocomotionTypeRatesMap(getLocomotionTypeRates(instance.getId()));

        return instance;
    }

	@Override
	protected ContentValues getContentValues(Race instance) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, instance.getName());
		values.put(COLUMN_DESCRIPTION, instance.getDescription());
		values.put(COLUMN_BONUS_DEVELOPMENT_POINTS, instance.getBonusDevelopmentPoints());
		values.put(COLUMN_PHYSICAL_RESISTANCE_MODIFIER, instance.getPhysicalResistanceModifier());
		values.put(COLUMN_ENDURANCE_MODIFIER, instance.getEnduranceModifier());
		values.put(COLUMN_BASE_HITS, instance.getBaseHits());
		values.put(COLUMN_RECOVERY_MULTIPLIER, instance.getRecoveryMultiplier());
		values.put(COLUMN_SIZE_ID, instance.getSize().getId());
		values.put(COLUMN_STRIDE_MODIFIER, instance.getStrideModifier());
		values.put(COLUMN_AVERAGE_HEIGHT, instance.getAverageHeight());
		values.put(COLUMN_AVERAGE_WEIGHT, instance.getAverageWeight());
		values.put(COLUMN_POUNDS_PER_INCH, instance.getPoundsPerInch());
		instance.setTalentsAndFlawsTiersMap(getTalentsAndFlaws(instance.getId()));
		instance.setLocomotionTypeRatesMap(getLocomotionTypeRates(instance.getId()));
		return values;
	}

	@Override
	protected boolean saveRelationships(SQLiteDatabase db, Race instance) {
		boolean result = saveTalentsAndFlaws(db, instance.getId(), instance.getTalentsAndFlawsTiersMap());
		result &= saveLocomotionTypes(db, instance.getId(), instance.getLocomotionTypeRatesMap());
		result &= saveSatMods(db, instance.getId(), instance.getStatModifiers());
		result &= saveRealmRRMods(db, instance.getId(), instance.getRealmResistancesModifiers());
		return result;
	}

	private boolean saveTalentsAndFlaws(SQLiteDatabase db, int raceId, Map<Talent, Short> talentTiersMap) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(raceId) };
		final String selection = RaceTalentsSchema.COLUMN_RACE_ID + " = ?";

		db.delete(RaceTalentsSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<Talent, Short> entry : talentTiersMap.entrySet()) {
			result &= (db.insert(RaceTalentsSchema.TABLE_NAME, null, getTalentTiersValues(raceId,
					entry.getKey(), entry.getValue())) != -1);
		}
		return result;
	}

	private ContentValues getTalentTiersValues(int raceId, Talent talent, Short tiers) {
		ContentValues values = new ContentValues();
		values.put(RaceTalentsSchema.COLUMN_RACE_ID, raceId);
		values.put(RaceTalentsSchema.COLUMN_TALENT_ID, talent.getId());
		values.put(RaceTalentsSchema.COLUMN_TIERS, tiers);
		return values;
	}

	private boolean saveLocomotionTypes(SQLiteDatabase db, int raceId, Map<LocomotionType, Short> locomotionTypeRatesMap) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(raceId) };
		final String selection = RaceLocomotionSchema.COLUMN_RACE_ID + " = ?";

		db.delete(RaceLocomotionSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<LocomotionType, Short> entry : locomotionTypeRatesMap.entrySet()) {
			result &= (db.insert(RaceLocomotionSchema.TABLE_NAME, null, getLocomotionTypeValues(raceId, entry)) != -1);
		}
		return result;
	}

	private boolean saveSatMods(SQLiteDatabase db, int raceId, Map<Stat, Short> statMods) {
		boolean result = true;
		final String selectionArgs[] = {String.valueOf(raceId) };
		final String selection = RaceStatModSchema.COLUMN_RACE_ID + " = ?";

		db.delete(RaceStatModSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<Stat, Short> entry : statMods.entrySet()) {
			result &= (db.insert(RaceStatModSchema.TABLE_NAME, null, getRaceStatModValues(raceId, entry)) != -1);
		}
		return result;
	}

	private boolean saveRealmRRMods(SQLiteDatabase db, int raceId, Map<Realm, Short> realmRRMods) {
		boolean result = true;
		final String selectionArgs[] = {String.valueOf(raceId) };
		final String selection = RaceRealmRRModSchema.COLUMN_RACE_ID + " = ?";

		db.delete(RaceRealmRRModSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<Realm, Short> entry : realmRRMods.entrySet()) {
			result &= (db.insert(RaceRealmRRModSchema.TABLE_NAME, null, getRaceRealmRRModValues(raceId, entry)) != -1);
		}
		return result;
	}

	private ContentValues getLocomotionTypeValues(int raceId, Map.Entry<LocomotionType, Short> locomotionTypeRateEntry) {
		ContentValues values = new ContentValues();
		values.put(RaceLocomotionSchema.COLUMN_RACE_ID, raceId);
		values.put(RaceLocomotionSchema.COLUMN_LOCOMOTION_TYPE_ID, locomotionTypeRateEntry.getKey().getId());
		values.put(RaceLocomotionSchema.COLUMN_RATE, locomotionTypeRateEntry.getValue());
		return values;
	}

	private Map<Talent, Short> getTalentsAndFlaws(int id) {
        final String selectionArgs[] = { String.valueOf(id) };
        final String selection = RaceTalentsSchema.COLUMN_RACE_ID + " = ?";

        Cursor cursor = super.query(RaceTalentsSchema.TABLE_NAME, RaceTalentsSchema.COLUMNS, selection,
                selectionArgs, RaceTalentsSchema.COLUMN_TALENT_ID);
        Map<Talent, Short> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(RaceTalentsSchema.COLUMN_TALENT_ID));
			Talent instance = talentDao.getById(mappedId);
			if(instance != null) {
				map.put(instance, cursor.getShort(cursor.getColumnIndexOrThrow(RaceTalentsSchema.COLUMN_TIERS)));
			}
			cursor.moveToNext();
		}
		cursor.close();

        return map;
    }

    private Map<LocomotionType, Short> getLocomotionTypeRates(int id) {
        final String selectionArgs[] = { String.valueOf(id) };
        final String selection = RaceLocomotionSchema.COLUMN_RACE_ID + " = ?";

        Cursor cursor = super.query(RaceLocomotionSchema.TABLE_NAME, RaceLocomotionSchema.COLUMNS, selection,
                selectionArgs, RaceLocomotionSchema.COLUMN_LOCOMOTION_TYPE_ID);
        Map<LocomotionType, Short> locomotionTypeRatesMap = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int locomotionTypeId = cursor.getInt(cursor.getColumnIndexOrThrow(RaceLocomotionSchema.COLUMN_LOCOMOTION_TYPE_ID));
			LocomotionType locomotionType = locomotionTypeDao.getById(locomotionTypeId);
			if(locomotionType != null) {
				locomotionTypeRatesMap.put(locomotionType, cursor.getShort(cursor.getColumnIndexOrThrow(RaceLocomotionSchema.COLUMN_RATE)));
			}
			cursor.moveToNext();
		}
		cursor.close();

        return locomotionTypeRatesMap;
    }

	private Map<Stat, Short> getStatModifiers(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = RaceStatModSchema.COLUMN_RACE_ID + " = ?";

		Cursor cursor = super.query(RaceStatModSchema.TABLE_NAME, RaceStatModSchema.COLUMNS, selection,
									selectionArgs, RaceStatModSchema.COLUMN_STAT_ID);
		Map<Stat, Short> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(RaceStatModSchema.COLUMN_STAT_ID));
			Stat instance = statDao.getById(mappedId);
			if(instance != null) {
				map.put(instance, cursor.getShort(cursor.getColumnIndexOrThrow(RaceStatModSchema.COLUMN_MODIFIER)));
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private Map<Realm, Short> getRealmResistanceModifiers(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = RaceRealmRRModSchema.COLUMN_RACE_ID + " = ?";

		Cursor cursor = super.query(RaceRealmRRModSchema.TABLE_NAME, RaceRealmRRModSchema.COLUMNS, selection,
									selectionArgs, RaceRealmRRModSchema.COLUMN_REALM_ID);
		Map<Realm, Short> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(RaceRealmRRModSchema.COLUMN_REALM_ID));
			Realm instance = realmDao.getById(mappedId);
			if(instance != null) {
				map.put(instance, cursor.getShort(cursor.getColumnIndexOrThrow(RaceRealmRRModSchema.COLUMN_MODIFIER)));
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private ContentValues getRaceStatModValues(int raceId, Map.Entry<Stat, Short> statModEntry) {
		ContentValues values = new ContentValues();
		values.put(RaceStatModSchema.COLUMN_RACE_ID, raceId);
		values.put(RaceStatModSchema.COLUMN_STAT_ID, statModEntry.getKey().getId());
		values.put(RaceStatModSchema.COLUMN_MODIFIER, statModEntry.getValue());
		return values;
	}

	private ContentValues getRaceRealmRRModValues(int raceId, Map.Entry<Realm, Short> realmModEntry) {
		ContentValues values = new ContentValues();
		values.put(RaceRealmRRModSchema.COLUMN_RACE_ID, raceId);
		values.put(RaceRealmRRModSchema.COLUMN_REALM_ID, realmModEntry.getKey().getId());
		values.put(RaceRealmRRModSchema.COLUMN_MODIFIER, realmModEntry.getValue());
		return values;
	}
}
