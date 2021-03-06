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
package com.madinnovations.rmu.data.dao.character.schemas;

/**
 * Database schema data for the race_stat_mods table
 */
public interface RaceStatModSchema {
	String TABLE_NAME = "race_stat_mods";

	String COLUMN_RACE_ID   = "raceId";
	String COLUMN_STAT_NAME = "statName";
	String COLUMN_MODIFIER  = "modifier";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "("
			+ COLUMN_RACE_ID + " INTEGER NOT NULL REFERENCES "
				+ RaceSchema.TABLE_NAME + "(" + RaceSchema.COLUMN_ID + "), "
			+ COLUMN_STAT_NAME + " TEXT NOT NULL, "
			+ COLUMN_MODIFIER + " INTEGER NOT NULL, "
			+ "PRIMARY KEY (" + COLUMN_RACE_ID + "," + COLUMN_STAT_NAME + ")"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_RACE_ID, COLUMN_STAT_NAME, COLUMN_MODIFIER};
}
