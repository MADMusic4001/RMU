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
package com.madinnovations.rmu.data.dao.schemas;

/**
 * Database schema data for the race_talents table
 */
public interface RaceTalentsSchema {
	public static final String TABLE_NAME = "race_talents";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_RACE_ID = "raceId";
	public static final String COLUMN_TALENT_ID = "talentId";
	public static final String COLUMN_TIERS = "tiers";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_RACE_ID + " INTEGER NOT NULL, "
			+ COLUMN_TALENT_ID + " INTEGER NOT NULL, "
			+ COLUMN_TIERS + " INTEGER NOT NULL"
			+ ")";

	public static final String[] COLUMNS = new String[] { COLUMN_ID,
			COLUMN_RACE_ID, COLUMN_TALENT_ID, COLUMN_TIERS};
}
