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

import com.madinnovations.rmu.data.dao.common.schemas.TalentSchema;

/**
 * Database schema data for the race_talents table
 */
public interface RaceTalentsSchema {
	String TABLE_NAME = "race_talents";

	String COLUMN_RACE_ID = "raceId";
	String COLUMN_TALENT_ID = "talentId";
	String COLUMN_TIERS = "tiers";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "("
			+ COLUMN_RACE_ID + " INTEGER NOT NULL REFERENCES "
				+ RaceSchema.TABLE_NAME + "(" + RaceSchema.COLUMN_ID + "), "
			+ COLUMN_TALENT_ID + " INTEGER NOT NULL REFERENCES "
				+ TalentSchema.TABLE_NAME + "(" + TalentSchema.COLUMN_ID + "), "
			+ COLUMN_TIERS + " INTEGER NOT NULL, "
			+ "PRIMARY KEY(" + COLUMN_RACE_ID + "," + COLUMN_TALENT_ID + ")"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_RACE_ID, COLUMN_TALENT_ID, COLUMN_TIERS};
}
