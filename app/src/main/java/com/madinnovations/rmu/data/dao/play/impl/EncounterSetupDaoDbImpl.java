/*
  Copyright (C) 2017 MadInnovations
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.dao.play.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.campaign.CampaignDao;
import com.madinnovations.rmu.data.dao.character.CharacterDao;
import com.madinnovations.rmu.data.dao.creature.CreatureDao;
import com.madinnovations.rmu.data.dao.play.EncounterSetupDao;
import com.madinnovations.rmu.data.dao.play.schemas.EncounterSetupCharacterEncounterInfoSchema;
import com.madinnovations.rmu.data.dao.play.schemas.EncounterSetupCreatureEncounterInfoSchema;
import com.madinnovations.rmu.data.dao.play.schemas.EncounterSetupSchema;
import com.madinnovations.rmu.data.entities.Position;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link EncounterSetup} objects in a SQLite database.
 */
@Singleton
public class EncounterSetupDaoDbImpl extends BaseDaoDbImpl<EncounterSetup> implements EncounterSetupDao, EncounterSetupSchema {
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
	public EncounterSetupDaoDbImpl(SQLiteOpenHelper helper, CharacterDao characterDao, CreatureDao creatureDao,
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
	protected int getId(EncounterSetup instance) {
		return instance.getId();
	}

	@Override
	protected void setId(EncounterSetup instance, int id) {
		instance.setId(id);
	}

	@Override
	public EncounterSetup getMostRecentForCampaign(Campaign campaign) {
		final String selectionArgs[] = { String.valueOf(campaign.getId()) };
		final String selection = COLUMN_CAMPAIGN_ID + " = ?";
		EncounterSetup instance = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection, selectionArgs, COLUMN_ENCOUNTER_START_TIME + " DESC");

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
	public Collection<EncounterSetup> getAllForCampaign(Campaign campaign) {
		final String selectionArgs[] = { String.valueOf(campaign.getId()) };
		final String selection = COLUMN_CAMPAIGN_ID + " = ?";
		Collection<EncounterSetup> collection = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection, selectionArgs, COLUMN_ENCOUNTER_START_TIME + " DESC");

			if (cursor != null) {
				cursor.moveToFirst();
				while(!cursor.isAfterLast()) {
					EncounterSetup instance = cursorToEntity(cursor);
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
	protected EncounterSetup cursorToEntity(@NonNull Cursor cursor) {
		EncounterSetup instance = new EncounterSetup();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setCampaign(campaignDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CAMPAIGN_ID))));
		instance.setCurrentInitiative(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_INITIATIVE)));
		Calendar startTime = Calendar.getInstance();
		startTime.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ENCOUNTER_START_TIME)));
		instance.setEncounterStartTime(startTime);
		instance.setCharacterCombatInfo(getCharacterLocations(instance.getId()));
		instance.setEnemyCombatInfo(getCreatureLocations(instance.getId()));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(EncounterSetup instance) {
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
		values.put(COLUMN_ENCOUNTER_START_TIME, instance.getEncounterStartTime().getTimeInMillis());

		return values;
	}

	@Override
	protected boolean saveRelationships(SQLiteDatabase db, EncounterSetup instance) {
		boolean result;

		result = saveCharacterLocations(db, instance.getId(), instance.getCharacterCombatInfo());

		result &= saveCreatureLocations(db, instance.getId(), instance.getEnemyCombatInfo());
		return result;
	}

	private boolean saveCharacterLocations(SQLiteDatabase db, int combatSetupId, Map<Character, EncounterRoundInfo> characterLocations) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(combatSetupId) };
		final String selection = EncounterSetupCharacterEncounterInfoSchema.COLUMN_ENCOUNTER_SETUP_ID + " = ?";

		db.delete(EncounterSetupCharacterEncounterInfoSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<Character, EncounterRoundInfo> entry : characterLocations.entrySet()) {
			result &= (db.insertWithOnConflict(EncounterSetupCharacterEncounterInfoSchema.TABLE_NAME, null,
											   getCharacterLocationsContentValues(combatSetupId, entry.getKey(), entry.getValue
													   ()), SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		return result;
	}

	private ContentValues getCharacterLocationsContentValues(int combatSetupId, Character character, EncounterRoundInfo encounterRoundInfo) {
		ContentValues values = new ContentValues(6);

		values.put(EncounterSetupCharacterEncounterInfoSchema.COLUMN_ENCOUNTER_SETUP_ID, combatSetupId);
		values.put(EncounterSetupCharacterEncounterInfoSchema.COLUMN_CHARACTER_ID, character.getId());
		values.put(EncounterSetupCharacterEncounterInfoSchema.COLUMN_LOCATION_X, encounterRoundInfo.getPosition().getX());
		values.put(EncounterSetupCharacterEncounterInfoSchema.COLUMN_LOCATION_Y, encounterRoundInfo.getPosition().getY());
		values.put(EncounterSetupCharacterEncounterInfoSchema.COLUMN_BASE_INITIATIVE, encounterRoundInfo.getInitiativeRoll());
		values.put(EncounterSetupCharacterEncounterInfoSchema.COLUMN_ACTION_POINTS_REMAINING, encounterRoundInfo.getActionPointsRemaining());

		return values;
	}

	private boolean saveCreatureLocations(SQLiteDatabase db, int combatSetupId, Map<Creature, EncounterRoundInfo> creatureLocations) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(combatSetupId) };
		final String selection = EncounterSetupCreatureEncounterInfoSchema.COLUMN_ENCOUNTER_SETUP_ID + " = ?";

		db.delete(EncounterSetupCreatureEncounterInfoSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<Creature, EncounterRoundInfo> entry : creatureLocations.entrySet()) {
			result &= (db.insertWithOnConflict(EncounterSetupCreatureEncounterInfoSchema.TABLE_NAME, null,
											   getCharacterLocationsContentValues(combatSetupId, entry.getKey(), entry.getValue
													   ()), SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		return result;
	}

	private ContentValues getCharacterLocationsContentValues(int combatSetupId, Creature creature, EncounterRoundInfo encounterRoundInfo) {
		ContentValues values = new ContentValues(6);

		values.put(EncounterSetupCreatureEncounterInfoSchema.COLUMN_ENCOUNTER_SETUP_ID, combatSetupId);
		values.put(EncounterSetupCreatureEncounterInfoSchema.COLUMN_CREATURE_ID, creature.getId());
		values.put(EncounterSetupCreatureEncounterInfoSchema.COLUMN_LOCATION_X, encounterRoundInfo.getPosition().getX());
		values.put(EncounterSetupCreatureEncounterInfoSchema.COLUMN_LOCATION_Y, encounterRoundInfo.getPosition().getY());
		values.put(EncounterSetupCreatureEncounterInfoSchema.COLUMN_BASE_INITIATIVE, encounterRoundInfo.getInitiativeRoll());
		values.put(EncounterSetupCreatureEncounterInfoSchema.COLUMN_ACTION_POINTS_REMAINING, encounterRoundInfo.getActionPointsRemaining());

		return values;
	}

	private Map<Character, EncounterRoundInfo> getCharacterLocations(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = EncounterSetupCharacterEncounterInfoSchema.COLUMN_ENCOUNTER_SETUP_ID + " = ?";

		Cursor cursor = super.query(EncounterSetupCharacterEncounterInfoSchema.TABLE_NAME, EncounterSetupCharacterEncounterInfoSchema.COLUMNS,
									selection, selectionArgs, EncounterSetupCharacterEncounterInfoSchema.COLUMN_CHARACTER_ID);
		Map<Character, EncounterRoundInfo> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			EncounterRoundInfo encounterRoundInfo = new EncounterRoundInfo();
			encounterRoundInfo.setPosition(new Position());
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(EncounterSetupCharacterEncounterInfoSchema.COLUMN_CHARACTER_ID));
			Character instance = characterDao.getById(mappedId);
			encounterRoundInfo.getPosition().setX(cursor.getFloat(cursor.getColumnIndexOrThrow(
					EncounterSetupCharacterEncounterInfoSchema.COLUMN_LOCATION_X)));
			encounterRoundInfo.getPosition().setY(cursor.getFloat(cursor.getColumnIndexOrThrow(
					EncounterSetupCharacterEncounterInfoSchema.COLUMN_LOCATION_Y)));
			encounterRoundInfo.setInitiativeRoll(cursor.getShort(cursor.getColumnIndexOrThrow(
					EncounterSetupCharacterEncounterInfoSchema.COLUMN_BASE_INITIATIVE)));
			encounterRoundInfo.setActionPointsRemaining(cursor.getShort(cursor.getColumnIndexOrThrow(
					EncounterSetupCharacterEncounterInfoSchema.COLUMN_ACTION_POINTS_REMAINING)));
			if(instance != null) {
				map.put(instance, encounterRoundInfo);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private Map<Creature, EncounterRoundInfo> getCreatureLocations(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = EncounterSetupCreatureEncounterInfoSchema.COLUMN_ENCOUNTER_SETUP_ID + " = ?";

		Cursor cursor = super.query(EncounterSetupCreatureEncounterInfoSchema.TABLE_NAME, EncounterSetupCreatureEncounterInfoSchema.COLUMNS,
									selection, selectionArgs, EncounterSetupCreatureEncounterInfoSchema.COLUMN_CREATURE_ID);
		Map<Creature, EncounterRoundInfo> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			EncounterRoundInfo encounterRoundInfo = new EncounterRoundInfo();
			encounterRoundInfo.setPosition(new Position());
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(EncounterSetupCreatureEncounterInfoSchema.COLUMN_CREATURE_ID));
			Creature instance = creatureDao.getById(mappedId);
			encounterRoundInfo.getPosition().setX(cursor.getFloat(cursor.getColumnIndexOrThrow(
					EncounterSetupCreatureEncounterInfoSchema.COLUMN_LOCATION_X)));
			encounterRoundInfo.getPosition().setY(cursor.getFloat(cursor.getColumnIndexOrThrow(
					EncounterSetupCreatureEncounterInfoSchema.COLUMN_LOCATION_Y)));
			encounterRoundInfo.setInitiativeRoll(cursor.getShort(cursor.getColumnIndexOrThrow(
					EncounterSetupCreatureEncounterInfoSchema.COLUMN_BASE_INITIATIVE)));
			encounterRoundInfo.setActionPointsRemaining(cursor.getShort(cursor.getColumnIndexOrThrow(
					EncounterSetupCreatureEncounterInfoSchema.COLUMN_ACTION_POINTS_REMAINING)));
			if(instance != null) {
				map.put(instance, encounterRoundInfo);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}
}
