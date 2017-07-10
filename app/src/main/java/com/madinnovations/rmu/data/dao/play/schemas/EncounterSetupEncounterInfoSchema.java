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
package com.madinnovations.rmu.data.dao.play.schemas;

/**
 * Database schema data for the encounter_setup_encounter_info table
 */
public interface EncounterSetupEncounterInfoSchema {
	String TABLE_NAME = "encounter_setup_encounter_info";

	String COLUMN_ENCOUNTER_SETUP_ID      = "encounterSetupId";
	String COLUMN_BEING_ID                = "beingId";
	String COLUMN_IS_CHARACTER            = "isCharacter";
	String COLUMN_LOCATION_X              = "locationX";
	String COLUMN_LOCATION_Y              = "locationY";
	String COLUMN_DIRECTION               = "direction";
	String COLUMN_SELECTED_OPPONENT_ID    = "selectedOpponentId";
	String COLUMN_OPPONENT_IS_CHARACTER   = "opponentIsCharacter";
	String COLUMN_PARRY                   = "parry";
	String COLUMN_INITIATIVE_ROLL         = "initiativeRoll";
	String COLUMN_BASE_INITIATIVE         = "baseInitiative";
	String COLUMN_ACTION_POINTS_REMAINING = "actionPointsRemaining";
	String COLUMN_ACTION_POINTS_SPENT     = "actionPointsSpent";
	String COLUMN_INSTANTANEOUS_USED      = "instantaneousUsed";
	String COLUMN_IS_CONCENTRATING        = "isConcentrating";
	String COLUMN_PACE                    = "pace";
	String COLUMN_IS_MOVING_BACKWARDS     = "isMovingBackwards";
	String COLUMN_RESTRICTED_QUARTERS     = "restrictedQuarters";
	String COLUMN_ACTION_IN_PROGRESS      = "actionInProgress";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ENCOUNTER_SETUP_ID + " INTEGER NOT NULL, "
			+ COLUMN_BEING_ID + " INTEGER NOT NULL, "
			+ COLUMN_IS_CHARACTER + " INTEGER NOT NULL, "
			+ COLUMN_PARRY + " INTEGER NOT NULL, "
			+ COLUMN_INITIATIVE_ROLL + " INTEGER NOT NULL, "
			+ COLUMN_LOCATION_X + " REAL NOT NULL, "
			+ COLUMN_LOCATION_Y + " REAL NOT NULL, "
			+ COLUMN_DIRECTION + " REAL NOT NULL, "
			+ COLUMN_SELECTED_OPPONENT_ID + " INTEGER, "
			+ COLUMN_OPPONENT_IS_CHARACTER + " INTEGER, "
			+ COLUMN_BASE_INITIATIVE + " INTEGER NOT NULL, "
			+ COLUMN_ACTION_POINTS_REMAINING + " INTEGER NOT NULL, "
			+ COLUMN_ACTION_POINTS_SPENT + " INTEGER NOT NULL, "
			+ COLUMN_INSTANTANEOUS_USED + " INTEGER NOT NULL, "
			+ COLUMN_IS_CONCENTRATING + " INTEGER NOT NULL, "
			+ COLUMN_PACE + " TEXT, "
			+ COLUMN_IS_MOVING_BACKWARDS + " INTEGER NOT NULL, "
			+ COLUMN_RESTRICTED_QUARTERS + " TEXT, "
			+ COLUMN_ACTION_IN_PROGRESS + " TEXT, "
			+ "PRIMARY KEY(" + COLUMN_ENCOUNTER_SETUP_ID + "," + COLUMN_BEING_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_ENCOUNTER_SETUP_ID + ") "
				+ " REFERENCES " + EncounterSetupSchema.TABLE_NAME + "(" + EncounterSetupSchema.COLUMN_ID + "));";

	String[] COLUMNS = new String[] {COLUMN_ENCOUNTER_SETUP_ID, COLUMN_BEING_ID, COLUMN_IS_CHARACTER, COLUMN_LOCATION_X,
			COLUMN_LOCATION_Y, COLUMN_DIRECTION, COLUMN_SELECTED_OPPONENT_ID, COLUMN_OPPONENT_IS_CHARACTER, COLUMN_PARRY,
			COLUMN_INITIATIVE_ROLL, COLUMN_BASE_INITIATIVE, COLUMN_ACTION_POINTS_REMAINING, COLUMN_ACTION_POINTS_SPENT,
			COLUMN_INSTANTANEOUS_USED, COLUMN_IS_CONCENTRATING, COLUMN_PACE, COLUMN_IS_MOVING_BACKWARDS,
			COLUMN_RESTRICTED_QUARTERS, COLUMN_ACTION_IN_PROGRESS};
}
