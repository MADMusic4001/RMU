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

import com.madinnovations.rmu.data.dao.common.schemas.LocomotionTypeSchema;

/**
 * Database schema data for the race_movements table
 */
public interface RaceLocomotionSchema {
	public static final String TABLE_NAME = "race_locomotion_types";

	public static final String COLUMN_LOCOMOTION_TYPE_ID = "locomotionTypeId";
	public static final String COLUMN_RACE_ID = "raceId";
	public static final String COLUMN_RATE = "rate";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_LOCOMOTION_TYPE_ID + " INTEGER NOT NULL, "
			+ COLUMN_RACE_ID + " INTEGER NOT NULL, "
			+ COLUMN_RATE + " INTEGER NOT NULL, "
			+ "PRIMARY KEY (" + COLUMN_LOCOMOTION_TYPE_ID + "," + COLUMN_RACE_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_LOCOMOTION_TYPE_ID + ") REFERENCES " + LocomotionTypeSchema.TABLE_NAME + "(" + LocomotionTypeSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_RACE_ID + ") REFERENCES " + RaceSchema.TABLE_NAME + "(" + RaceSchema.COLUMN_ID + ")"
			+ ")";

	public static final String[] COLUMNS = new String[] {COLUMN_LOCOMOTION_TYPE_ID, COLUMN_RACE_ID, COLUMN_RATE};
}