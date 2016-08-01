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
 * Database schema data for the characters table
 */
public interface CharacterSchema {
	public static final String TABLE_NAME = "characters";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_RACE_ID = "raceId";
	public static final String COLUMN_HEIGHT = "height";
	public static final String COLUMN_WEIGHT = "weight";
	public static final String COLUMN_STRIDE = "stride";
	public static final String COLUMN_CURRENT_HITS = "currentHits";
	public static final String COLUMN_MAX_HITS = "maxHits";
	public static final String COLUMN_CURRENT_DEVELOPMENT_POINTS = "currentDevelopmentPoints";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_NAME  + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_RACE_ID  + " INTEGER NOT NULL, "
			+ COLUMN_HEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_WEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_STRIDE + " INTEGER NOT NULL, "
			+ COLUMN_CURRENT_HITS + " INTEGER NOT NULL, "
			+ COLUMN_MAX_HITS + " INTEGER NOT NULL, "
			+ COLUMN_CURRENT_DEVELOPMENT_POINTS + " INTEGER NOT NULL, "
			+ "FOREIGN KEY (" + COLUMN_RACE_ID + ") REFERENCES " + RaceSchema.TABLE_NAME + "(" + RaceSchema.COLUMN_ID + ")"
			+ ")";

	public static final String[] COLUMNS = new String[] { COLUMN_ID,
			COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_RACE_ID,
			COLUMN_HEIGHT, COLUMN_WEIGHT, COLUMN_STRIDE,
			COLUMN_CURRENT_HITS, COLUMN_MAX_HITS, COLUMN_CURRENT_DEVELOPMENT_POINTS};
}
