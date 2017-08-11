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
import com.madinnovations.rmu.data.dao.play.schemas.EncounterSetupEncounterInfoSchema;
import com.madinnovations.rmu.data.dao.play.schemas.EncounterSetupSchema;
import com.madinnovations.rmu.data.entities.Position;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.combat.Action;
import com.madinnovations.rmu.data.entities.combat.RestrictedQuarters;
import com.madinnovations.rmu.data.entities.common.Pace;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
		setEncounterRoundInfo(instance);

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

		result = saveEncounterRoundInfo(db, instance.getId(), instance.getCharacterCombatInfo(), instance.getEnemyCombatInfo());

		return result;
	}

	private boolean saveEncounterRoundInfo(SQLiteDatabase db, int combatSetupId,
										   Map<Character, EncounterRoundInfo> characterInfo,
										   Map<Creature, EncounterRoundInfo> creatureInfo) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(combatSetupId) };
		final String selection = EncounterSetupEncounterInfoSchema.COLUMN_ENCOUNTER_SETUP_ID + " = ?";

		db.delete(EncounterSetupEncounterInfoSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<Character, EncounterRoundInfo> entry : characterInfo.entrySet()) {
			result &= (db.insertWithOnConflict(EncounterSetupEncounterInfoSchema.TABLE_NAME, null,
											   getCharacterInfoContentValues(combatSetupId, entry.getValue()),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}

		for(Map.Entry<Creature, EncounterRoundInfo> entry : creatureInfo.entrySet()) {
			result &= (db.insertWithOnConflict(EncounterSetupEncounterInfoSchema.TABLE_NAME, null,
											   getCreatureInfoContentValues(combatSetupId, entry.getValue()),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}

		return result;
	}

	private ContentValues getCharacterInfoContentValues(int combatSetupId, EncounterRoundInfo encounterRoundInfo) {
		ContentValues values = new ContentValues(19);

		values.put(EncounterSetupEncounterInfoSchema.COLUMN_ENCOUNTER_SETUP_ID, combatSetupId);
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_BEING_ID, encounterRoundInfo.getCombatant().getId());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_IS_CHARACTER, true);
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_PARRY, encounterRoundInfo.getParry());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_INITIATIVE_ROLL, encounterRoundInfo.getInitiativeRoll());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_LOCATION_X, encounterRoundInfo.getPosition().getX());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_LOCATION_Y, encounterRoundInfo.getPosition().getY());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_DIRECTION, encounterRoundInfo.getPosition().getDirection());
		if(encounterRoundInfo.getSelectedOpponent() != null) {
			values.put(EncounterSetupEncounterInfoSchema.COLUMN_SELECTED_OPPONENT_ID,
					   encounterRoundInfo.getSelectedOpponent().getId());
			values.put(EncounterSetupEncounterInfoSchema.COLUMN_OPPONENT_IS_CHARACTER,
					   encounterRoundInfo.getSelectedOpponent() instanceof Character);
		}
		else {
			values.putNull(EncounterSetupEncounterInfoSchema.COLUMN_SELECTED_OPPONENT_ID);
			values.putNull(EncounterSetupEncounterInfoSchema.COLUMN_OPPONENT_IS_CHARACTER);
		}
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_BASE_INITIATIVE, encounterRoundInfo.getInitiativeRoll());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_ACTION_POINTS_REMAINING,
				   encounterRoundInfo.getActionPointsRemaining());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_ACTION_POINTS_SPENT, encounterRoundInfo.getActionPointsSpent());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_INSTANTANEOUS_USED, encounterRoundInfo.isInstantaneousUsed());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_IS_CONCENTRATING, encounterRoundInfo.isConcentrating());
		if(encounterRoundInfo.getPace() != null) {
			values.put(EncounterSetupEncounterInfoSchema.COLUMN_PACE, encounterRoundInfo.getPace().name());
		}
		else {
			values.putNull(EncounterSetupEncounterInfoSchema.COLUMN_PACE);
		}
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_IS_MOVING_BACKWARDS, encounterRoundInfo.isMovingBackwards());
		if(encounterRoundInfo.getRestrictedQuarters() != null) {
			values.put(EncounterSetupEncounterInfoSchema.COLUMN_RESTRICTED_QUARTERS,
					   encounterRoundInfo.getRestrictedQuarters().name());
		}
		else {
			values.putNull(EncounterSetupEncounterInfoSchema.COLUMN_RESTRICTED_QUARTERS);
		}
		if(encounterRoundInfo.getActionInProgress() != null) {
			values.put(EncounterSetupEncounterInfoSchema.COLUMN_ACTION_IN_PROGRESS,
					   encounterRoundInfo.getActionInProgress().name());
		}
		else {
			values.putNull(EncounterSetupEncounterInfoSchema.COLUMN_ACTION_IN_PROGRESS);
		}

		return values;
	}

	private ContentValues getCreatureInfoContentValues(int combatSetupId, EncounterRoundInfo encounterRoundInfo) {
		ContentValues values = new ContentValues(19);

		values.put(EncounterSetupEncounterInfoSchema.COLUMN_ENCOUNTER_SETUP_ID, combatSetupId);
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_BEING_ID, encounterRoundInfo.getCombatant().getId());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_IS_CHARACTER, false);
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_PARRY, encounterRoundInfo.getParry());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_INITIATIVE_ROLL, encounterRoundInfo.getInitiativeRoll());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_LOCATION_X, encounterRoundInfo.getPosition().getX());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_LOCATION_Y, encounterRoundInfo.getPosition().getY());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_DIRECTION, encounterRoundInfo.getPosition().getDirection());
		if(encounterRoundInfo.getSelectedOpponent() != null) {
			values.put(EncounterSetupEncounterInfoSchema.COLUMN_SELECTED_OPPONENT_ID,
					   encounterRoundInfo.getSelectedOpponent().getId());
			values.put(EncounterSetupEncounterInfoSchema.COLUMN_OPPONENT_IS_CHARACTER,
					   encounterRoundInfo.getSelectedOpponent() instanceof Character);
		}
		else {
			values.putNull(EncounterSetupEncounterInfoSchema.COLUMN_SELECTED_OPPONENT_ID);
			values.putNull(EncounterSetupEncounterInfoSchema.COLUMN_OPPONENT_IS_CHARACTER);
		}
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_BASE_INITIATIVE, encounterRoundInfo.getInitiativeRoll());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_ACTION_POINTS_REMAINING,
				   encounterRoundInfo.getActionPointsRemaining());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_ACTION_POINTS_SPENT, encounterRoundInfo.getActionPointsSpent());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_INSTANTANEOUS_USED, encounterRoundInfo.isInstantaneousUsed());
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_IS_CONCENTRATING, encounterRoundInfo.isConcentrating());
		if(encounterRoundInfo.getPace() != null) {
			values.put(EncounterSetupEncounterInfoSchema.COLUMN_PACE, encounterRoundInfo.getPace().name());
		}
		else {
			values.putNull(EncounterSetupEncounterInfoSchema.COLUMN_PACE);
		}
		values.put(EncounterSetupEncounterInfoSchema.COLUMN_IS_MOVING_BACKWARDS, encounterRoundInfo.isMovingBackwards());
		if(encounterRoundInfo.getRestrictedQuarters() != null) {
			values.put(EncounterSetupEncounterInfoSchema.COLUMN_RESTRICTED_QUARTERS,
					   encounterRoundInfo.getRestrictedQuarters().name());
		}
		else {
			values.putNull(EncounterSetupEncounterInfoSchema.COLUMN_RESTRICTED_QUARTERS);
		}
		if(encounterRoundInfo.getActionInProgress() != null) {
			values.put(EncounterSetupEncounterInfoSchema.COLUMN_ACTION_IN_PROGRESS,
					   encounterRoundInfo.getActionInProgress().name());
		}
		else {
			values.putNull(EncounterSetupEncounterInfoSchema.COLUMN_ACTION_IN_PROGRESS);
		}

		return values;
	}

	private void setEncounterRoundInfo(EncounterSetup encounterSetup) {
		final String selectionArgs[] = { String.valueOf(encounterSetup.getId()) };
		final String selection = EncounterSetupEncounterInfoSchema.COLUMN_ENCOUNTER_SETUP_ID + " = ?";

		Cursor cursor = super.query(EncounterSetupEncounterInfoSchema.TABLE_NAME, EncounterSetupEncounterInfoSchema.COLUMNS,
									selection, selectionArgs, EncounterSetupEncounterInfoSchema.COLUMN_BEING_ID);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			EncounterRoundInfo encounterRoundInfo = new EncounterRoundInfo();
			encounterRoundInfo.setPosition(new Position());
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(EncounterSetupEncounterInfoSchema.COLUMN_BEING_ID));
			boolean isCharacter = cursor.getInt(cursor.getColumnIndexOrThrow(
					EncounterSetupEncounterInfoSchema.COLUMN_IS_CHARACTER)) != 0;
			if(isCharacter) {
				Character character = characterDao.getById(mappedId);
				encounterRoundInfo.setCombatant(character);
				encounterSetup.getCharacterCombatInfo().put(character, encounterRoundInfo);
			}
			else {
				Creature creature = creatureDao.getById(mappedId);
				if(creature != null) {
					encounterRoundInfo.setCombatant(creature);
					encounterSetup.getEnemyCombatInfo().put(creature, encounterRoundInfo);
				}
			}
			encounterRoundInfo.getPosition().setX(cursor.getFloat(cursor.getColumnIndexOrThrow(
					EncounterSetupEncounterInfoSchema.COLUMN_LOCATION_X)));
			encounterRoundInfo.getPosition().setY(cursor.getFloat(cursor.getColumnIndexOrThrow(
					EncounterSetupEncounterInfoSchema.COLUMN_LOCATION_Y)));
			encounterRoundInfo.getPosition().setDirection(cursor.getFloat(cursor.getColumnIndexOrThrow(
					EncounterSetupEncounterInfoSchema.COLUMN_DIRECTION)));
			if(!cursor.isNull(cursor.getColumnIndexOrThrow(EncounterSetupEncounterInfoSchema.COLUMN_SELECTED_OPPONENT_ID))) {
				mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(
						EncounterSetupEncounterInfoSchema.COLUMN_SELECTED_OPPONENT_ID));
				isCharacter = cursor.getInt(cursor.getColumnIndexOrThrow(
						EncounterSetupEncounterInfoSchema.COLUMN_OPPONENT_IS_CHARACTER)) != 0;
				if (isCharacter) {
					encounterRoundInfo.setSelectedOpponent(characterDao.getById(mappedId));
				}
				else {
					encounterRoundInfo.setSelectedOpponent(creatureDao.getById(mappedId));
				}
			}
			encounterRoundInfo.setParry(cursor.getShort(cursor.getColumnIndexOrThrow(
					EncounterSetupEncounterInfoSchema.COLUMN_PARRY)));
			encounterRoundInfo.setInitiativeRoll(cursor.getShort(cursor.getColumnIndexOrThrow(
					EncounterSetupEncounterInfoSchema.COLUMN_BASE_INITIATIVE)));
			encounterRoundInfo.setBaseInitiative(cursor.getShort(cursor.getColumnIndexOrThrow(
					EncounterSetupEncounterInfoSchema.COLUMN_BASE_INITIATIVE)));
			encounterRoundInfo.setActionPointsRemaining(cursor.getShort(cursor.getColumnIndexOrThrow(
					EncounterSetupEncounterInfoSchema.COLUMN_ACTION_POINTS_REMAINING)));
			encounterRoundInfo.setActionPointsSpent(cursor.getShort(cursor.getColumnIndexOrThrow(
					EncounterSetupEncounterInfoSchema.COLUMN_ACTION_POINTS_SPENT)));
			encounterRoundInfo.setInstantaneousUsed(cursor.getInt(cursor.getColumnIndexOrThrow(
					EncounterSetupEncounterInfoSchema.COLUMN_INSTANTANEOUS_USED)) != 0);
			encounterRoundInfo.setConcentrating(cursor.getInt(cursor.getColumnIndexOrThrow(
					EncounterSetupEncounterInfoSchema.COLUMN_IS_CONCENTRATING)) != 0);
			if(!cursor.isNull(cursor.getColumnIndexOrThrow(EncounterSetupEncounterInfoSchema.COLUMN_PACE))) {
				encounterRoundInfo.setPace(Pace.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(
						EncounterSetupEncounterInfoSchema.COLUMN_PACE))));
			}
			encounterRoundInfo.setMovingBackwards(cursor.getInt(cursor.getColumnIndexOrThrow(
					EncounterSetupEncounterInfoSchema.COLUMN_IS_MOVING_BACKWARDS)) != 0);
			if(!cursor.isNull(cursor.getColumnIndexOrThrow(EncounterSetupEncounterInfoSchema.COLUMN_RESTRICTED_QUARTERS))) {
				encounterRoundInfo.setRestrictedQuarters(RestrictedQuarters.valueOf(cursor.getString(
						cursor.getColumnIndexOrThrow(EncounterSetupEncounterInfoSchema.COLUMN_RESTRICTED_QUARTERS))));
			}
			if(!cursor.isNull(cursor.getColumnIndexOrThrow(EncounterSetupEncounterInfoSchema.COLUMN_ACTION_IN_PROGRESS))) {
				encounterRoundInfo.setActionInProgress(Action.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(
						EncounterSetupEncounterInfoSchema.COLUMN_ACTION_IN_PROGRESS))));
			}
			cursor.moveToNext();
		}
		cursor.close();
	}
}
