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
 * Database schema data for the combat_setup_character_locations table
 */
public interface EncounterSetupCharacterEncounterInfoSchema {
	String TABLE_NAME = "encounter_setup_character_encounter_info";

	String COLUMN_ENCOUNTER_SETUP_ID      = "encounterSetupId";
	String COLUMN_CHARACTER_ID            = "characterId";
	String COLUMN_LOCATION_X              = "locationX";
	String COLUMN_LOCATION_Y              = "locationY";
	String COLUMN_DIRECTION               = "direction";
	String COLUMN_BASE_INITIATIVE         = "baseInitiative";
	String COLUMN_ACTION_POINTS_REMAINING = "actionPointsRemaining";
	String COLUMN_INSTANTANEOUS_USED      = "instantaneousUsed";
	String COLUMN_IS_CONCENTRATING        = "isConcentrating";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ENCOUNTER_SETUP_ID + " INTEGER NOT NULL, "
			+ COLUMN_CHARACTER_ID + " INTEGER NOT NULL, "
			+ COLUMN_LOCATION_X + " REAL NOT NULL, "
			+ COLUMN_LOCATION_Y + " REAL NOT NULL, "
			+ COLUMN_DIRECTION + " REAL NOT NULL, "
			+ COLUMN_BASE_INITIATIVE + " INTEGER NOT NULL, "
			+ COLUMN_ACTION_POINTS_REMAINING + " INTEGER NOT NULL, "
			+ COLUMN_INSTANTANEOUS_USED + " INTEGER NOT NULL, "
			+ COLUMN_IS_CONCENTRATING + " INTEGER NOT NULL, "
			+ "PRIMARY KEY(" + COLUMN_ENCOUNTER_SETUP_ID + "," + COLUMN_CHARACTER_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_ENCOUNTER_SETUP_ID + ") "
				+ " REFERENCES " + EncounterSetupSchema.TABLE_NAME + "(" + EncounterSetupSchema.COLUMN_ID + "));";

	String[] COLUMNS = new String[] {COLUMN_ENCOUNTER_SETUP_ID, COLUMN_CHARACTER_ID, COLUMN_LOCATION_X, COLUMN_LOCATION_Y,
			COLUMN_BASE_INITIATIVE, COLUMN_ACTION_POINTS_REMAINING, COLUMN_INSTANTANEOUS_USED, COLUMN_IS_CONCENTRATING};
}
