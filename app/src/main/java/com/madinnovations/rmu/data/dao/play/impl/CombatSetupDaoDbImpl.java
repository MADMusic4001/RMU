/**
 * Copyright (C) 2017 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.dao.play.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.campaign.CampaignDao;
import com.madinnovations.rmu.data.dao.character.CharacterDao;
import com.madinnovations.rmu.data.dao.creature.CreatureDao;
import com.madinnovations.rmu.data.dao.play.CombatSetupDao;
import com.madinnovations.rmu.data.dao.play.schemas.CombatSetupCharacterCombatInfoSchema;
import com.madinnovations.rmu.data.dao.play.schemas.CombatSetupCreatureCombatInfoSchema;
import com.madinnovations.rmu.data.dao.play.schemas.CombatSetupSchema;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.CombatInfo;
import com.madinnovations.rmu.data.entities.play.CombatSetup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link CombatSetup} objects in a SQLite database.
 */
@Singleton
public class CombatSetupDaoDbImpl extends BaseDaoDbImpl<CombatSetup> implements CombatSetupDao, CombatSetupSchema {
	private CharacterDao characterDao;
	private CreatureDao creatureDao;
	private CampaignDao campaignDao;

	/**
	 * Creates a new CombatSetupDaoDbImpl instance
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 * @param characterDao  a {@link CharacterDao} instance
	 * @param creatureDao  a {@link CreatureDao} instance
	 * @param campaignDao  a {@link CampaignDao} instance
	 */
	@Inject
	public CombatSetupDaoDbImpl(SQLiteOpenHelper helper, CharacterDao characterDao, CreatureDao creatureDao,
								CampaignDao campaignDao) {
		super(helper);
		this.characterDao = characterDao;
		this.creatureDao =  creatureDao;
		this.campaignDao = campaignDao;
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
	protected int getId(CombatSetup instance) {
		return instance.getId();
	}

	@Override
	protected void setId(CombatSetup instance, int id) {
		instance.setId(id);
	}

	@Override
	public CombatSetup getMostRecentForCampaign(Campaign campaign) {
		final String selectionArgs[] = { String.valueOf(campaign.getId()) };
		final String selection = COLUMN_CAMPAIGN_ID + " = ?";
		CombatSetup instance = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection, selectionArgs, COLUMN_COMBAT_START_TIME + " DESC");

			if (cursor != null) {
				if(cursor.moveToFirst()) {
					instance = cursorToEntity(cursor);
				}
				cursor.close();
			}
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}

		return instance;
	}

	@Override
	public Collection<CombatSetup> getAllForCampaign(Campaign campaign) {
		final String selectionArgs[] = { String.valueOf(campaign.getId()) };
		final String selection = COLUMN_CAMPAIGN_ID + " = ?";
		Collection<CombatSetup> collection = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection, selectionArgs, COLUMN_COMBAT_START_TIME + " DESC");

			if (cursor != null) {
				cursor.moveToFirst();
				while(!cursor.isAfterLast()) {
					CombatSetup instance = cursorToEntity(cursor);
					collection.add(instance);
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

		return collection;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected CombatSetup cursorToEntity(@NonNull Cursor cursor) {
		CombatSetup instance = new CombatSetup();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setCampaign(campaignDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CAMPAIGN_ID))));
		instance.setCurrentInitiative(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_INITIATIVE)));
		Calendar startTime = Calendar.getInstance();
		startTime.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_COMBAT_START_TIME)));
		instance.setCombatStartTime(startTime);
		instance.setCharacterCombatInfo(getCharacterLocations(instance.getId()));
		instance.setCreatureCombatInfo(getCreatureLocations(instance.getId()));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(CombatSetup instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(4);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(3);
		}
		values.put(COLUMN_CAMPAIGN_ID, instance.getCampaign().getId());
		values.put(COLUMN_CURRENT_INITIATIVE, instance.getCurrentInitiative());
		values.put(COLUMN_COMBAT_START_TIME, instance.getCombatStartTime().getTimeInMillis());

		return values;
	}

	@Override
	protected boolean saveRelationships(SQLiteDatabase db, CombatSetup instance) {
		boolean result;

		result = saveCharacterLocations(db, instance.getId(), instance.getCharacterCombatInfo());

		result &= saveCreatureLocations(db, instance.getId(), instance.getCreatureCombatInfo());
		return result;
	}

	private boolean saveCharacterLocations(SQLiteDatabase db, int combatSetupId, Map<Character, CombatInfo> characterLocations) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(combatSetupId) };
		final String selection = CombatSetupCharacterCombatInfoSchema.COLUMN_COMBAT_SETUP_ID + " = ?";

		db.delete(CombatSetupCharacterCombatInfoSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<Character, CombatInfo> entry : characterLocations.entrySet()) {
			result &= (db.insertWithOnConflict(CombatSetupCharacterCombatInfoSchema.TABLE_NAME, null,
											   getCharacterLocationsContentValues(combatSetupId, entry.getKey(), entry.getValue
													   ()), SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		return result;
	}

	private ContentValues getCharacterLocationsContentValues(int combatSetupId, Character character, CombatInfo combatInfo) {
		ContentValues values = new ContentValues(6);

		values.put(CombatSetupCharacterCombatInfoSchema.COLUMN_COMBAT_SETUP_ID, combatSetupId);
		values.put(CombatSetupCharacterCombatInfoSchema.COLUMN_CHARACTER_ID, character.getId());
		values.put(CombatSetupCharacterCombatInfoSchema.COLUMN_LOCATION_X, combatInfo.getHexCoordinate().x);
		values.put(CombatSetupCharacterCombatInfoSchema.COLUMN_LOCATION_Y, combatInfo.getHexCoordinate().y);
		values.put(CombatSetupCharacterCombatInfoSchema.COLUMN_BASE_INITIATIVE, combatInfo.getInitiativeRoll());
		values.put(CombatSetupCharacterCombatInfoSchema.COLUMN_ACTION_POINTS_REMAINING, combatInfo.getActionPointsRemaining());

		return values;
	}

	private boolean saveCreatureLocations(SQLiteDatabase db, int combatSetupId, Map<Creature, CombatInfo> creatureLocations) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(combatSetupId) };
		final String selection = CombatSetupCreatureCombatInfoSchema.COLUMN_COMBAT_SETUP_ID + " = ?";

		db.delete(CombatSetupCreatureCombatInfoSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<Creature, CombatInfo> entry : creatureLocations.entrySet()) {
			result &= (db.insertWithOnConflict(CombatSetupCreatureCombatInfoSchema.TABLE_NAME, null,
											   getCharacterLocationsContentValues(combatSetupId, entry.getKey(), entry.getValue
													   ()), SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		return result;
	}

	private ContentValues getCharacterLocationsContentValues(int combatSetupId, Creature creature, CombatInfo combatInfo) {
		ContentValues values = new ContentValues(6);

		values.put(CombatSetupCreatureCombatInfoSchema.COLUMN_COMBAT_SETUP_ID, combatSetupId);
		values.put(CombatSetupCreatureCombatInfoSchema.COLUMN_CREATURE_ID, creature.getId());
		values.put(CombatSetupCreatureCombatInfoSchema.COLUMN_LOCATION_X, combatInfo.getHexCoordinate().x);
		values.put(CombatSetupCreatureCombatInfoSchema.COLUMN_LOCATION_Y, combatInfo.getHexCoordinate().y);
		values.put(CombatSetupCreatureCombatInfoSchema.COLUMN_BASE_INITIATIVE, combatInfo.getInitiativeRoll());
		values.put(CombatSetupCreatureCombatInfoSchema.COLUMN_ACTION_POINTS_REMAINING, combatInfo.getActionPointsRemaining());

		return values;
	}

	private Map<Character, CombatInfo> getCharacterLocations(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CombatSetupCharacterCombatInfoSchema.COLUMN_COMBAT_SETUP_ID + " = ?";

		Cursor cursor = super.query(CombatSetupCharacterCombatInfoSchema.TABLE_NAME, CombatSetupCharacterCombatInfoSchema.COLUMNS,
									selection, selectionArgs, CombatSetupCharacterCombatInfoSchema.COLUMN_CHARACTER_ID);
		Map<Character, CombatInfo> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CombatInfo combatInfo = new CombatInfo();
			combatInfo.setHexCoordinate(new Point());
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CombatSetupCharacterCombatInfoSchema.COLUMN_CHARACTER_ID));
			Character instance = characterDao.getById(mappedId);
			combatInfo.getHexCoordinate().x = cursor.getInt(cursor.getColumnIndexOrThrow(
					CombatSetupCharacterCombatInfoSchema.COLUMN_LOCATION_X));
			combatInfo.getHexCoordinate().y = cursor.getInt(cursor.getColumnIndexOrThrow(
					CombatSetupCharacterCombatInfoSchema.COLUMN_LOCATION_Y));
			combatInfo.setInitiativeRoll(cursor.getShort(cursor.getColumnIndexOrThrow(
					CombatSetupCharacterCombatInfoSchema.COLUMN_BASE_INITIATIVE)));
			combatInfo.setActionPointsRemaining(cursor.getShort(cursor.getColumnIndexOrThrow(
					CombatSetupCharacterCombatInfoSchema.COLUMN_ACTION_POINTS_REMAINING)));
			if(instance != null) {
				map.put(instance, combatInfo);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private Map<Creature, CombatInfo> getCreatureLocations(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CombatSetupCreatureCombatInfoSchema.COLUMN_COMBAT_SETUP_ID + " = ?";

		Cursor cursor = super.query(CombatSetupCreatureCombatInfoSchema.TABLE_NAME, CombatSetupCreatureCombatInfoSchema.COLUMNS,
									selection, selectionArgs, CombatSetupCreatureCombatInfoSchema.COLUMN_CREATURE_ID);
		Map<Creature, CombatInfo> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CombatInfo combatInfo = new CombatInfo();
			combatInfo.setHexCoordinate(new Point());
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CombatSetupCreatureCombatInfoSchema.COLUMN_CREATURE_ID));
			Creature instance = creatureDao.getById(mappedId);
			combatInfo.getHexCoordinate().x = cursor.getInt(cursor.getColumnIndexOrThrow(
					CombatSetupCreatureCombatInfoSchema.COLUMN_LOCATION_X));
			combatInfo.getHexCoordinate().y = cursor.getInt(cursor.getColumnIndexOrThrow(
					CombatSetupCreatureCombatInfoSchema.COLUMN_LOCATION_Y));
			combatInfo.setInitiativeRoll(cursor.getShort(cursor.getColumnIndexOrThrow(
					CombatSetupCreatureCombatInfoSchema.COLUMN_BASE_INITIATIVE)));
			combatInfo.setActionPointsRemaining(cursor.getShort(cursor.getColumnIndexOrThrow(
					CombatSetupCreatureCombatInfoSchema.COLUMN_ACTION_POINTS_REMAINING)));
			if(instance != null) {
				map.put(instance, combatInfo);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}
}
