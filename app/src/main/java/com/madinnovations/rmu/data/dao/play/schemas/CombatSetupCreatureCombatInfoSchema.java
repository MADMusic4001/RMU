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
package com.madinnovations.rmu.data.dao.play.schemas;

/**
 * Database schema data for the combat_setup_creature_locations table
 */
public interface CombatSetupCreatureCombatInfoSchema {
	String TABLE_NAME = "combat_setup_creature_combat_info";

	String COLUMN_COMBAT_SETUP_ID         = "combatSetupId";
	String COLUMN_CREATURE_ID             = "creatureId";
	String COLUMN_LOCATION_X              = "locationX";
	String COLUMN_LOCATION_Y              = "locationY";
	String COLUMN_BASE_INITIATIVE         = "baseInitiative";
	String COLUMN_ACTION_POINTS_REMAINING = "actionPointsRemaining";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_COMBAT_SETUP_ID + " INTEGER NOT NULL, "
			+ COLUMN_CREATURE_ID + " INTEGER NOT NULL, "
			+ COLUMN_LOCATION_X + " INTEGER NOT NULL, "
			+ COLUMN_LOCATION_Y + " INTEGER NOT NULL, "
			+ COLUMN_BASE_INITIATIVE + " INTEGER NOT NULL, "
			+ COLUMN_ACTION_POINTS_REMAINING + " INTEGER NOT NULL, "
			+ "PRIMARY KEY(" + COLUMN_COMBAT_SETUP_ID + "," + COLUMN_CREATURE_ID + ")"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_COMBAT_SETUP_ID, COLUMN_CREATURE_ID, COLUMN_LOCATION_X, COLUMN_LOCATION_Y,
			COLUMN_BASE_INITIATIVE, COLUMN_ACTION_POINTS_REMAINING};
}
