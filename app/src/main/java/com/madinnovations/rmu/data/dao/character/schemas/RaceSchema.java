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
 * Database schema data for the races table
 */
public interface RaceSchema {
	String TABLE_NAME = "races";

	String COLUMN_ID = "id";
	String COLUMN_NAME = "name";
	String COLUMN_DESCRIPTION = "description";
	String COLUMN_BONUS_DEVELOPMENT_POINTS = "bonusDevelopmentPoints";
	String COLUMN_PHYSICAL_RESISTANCE_MODIFIER = "physicalResistanceModifier";
	String COLUMN_ENDURANCE_MODIFIER = "enduranceModifier";
	String COLUMN_BASE_HITS = "baseHits";
	String COLUMN_RECOVERY_MULTIPLIER = "recoveryMultiplier";
	String COLUMN_SIZE_ID = "sizeId";
	String COLUMN_STRIDE_MODIFIER = "strideModifier";
	String COLUMN_AVERAGE_HEIGHT = "averageHeight";
	String COLUMN_AVERAGE_WEIGHT = "averageWeight";
	String COLUMN_POUNDS_PER_INCH = "poundsPerInch";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_BONUS_DEVELOPMENT_POINTS + " INTEGER NOT NULL, "
			+ COLUMN_PHYSICAL_RESISTANCE_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_ENDURANCE_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_BASE_HITS + " INTEGER NOT NULL, "
			+ COLUMN_RECOVERY_MULTIPLIER + " REAL NOT NULL, "
			+ COLUMN_SIZE_ID + " INTEGER NOT NULL, "
			+ COLUMN_STRIDE_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_AVERAGE_HEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_AVERAGE_WEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_POUNDS_PER_INCH + " INTEGER NOT NULL"
			+ ");";

	String[] COLUMNS = new String[] { COLUMN_ID,
			COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_BONUS_DEVELOPMENT_POINTS,
			COLUMN_PHYSICAL_RESISTANCE_MODIFIER, COLUMN_ENDURANCE_MODIFIER,
			COLUMN_BASE_HITS, COLUMN_RECOVERY_MULTIPLIER, COLUMN_SIZE_ID,
			COLUMN_STRIDE_MODIFIER, COLUMN_AVERAGE_HEIGHT, COLUMN_AVERAGE_WEIGHT,
			COLUMN_POUNDS_PER_INCH};
}
