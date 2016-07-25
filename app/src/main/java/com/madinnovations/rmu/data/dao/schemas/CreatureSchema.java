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
 * Database schema data for the creatures table
 */
public interface CreatureSchema {
	public static final String TABLE_NAME = "creatures";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_ARCHETYPE_ID = "archetypeId";
	public static final String COLUMN_LEVEL = "level";
	public static final String COLUMN_LEVEL_SPREAD = "levelSpread";
	public static final String COLUMN_CATEGORY_ID = "categoryId";
	public static final String COLUMN_TYPE_ID = "typeId";
	public static final String COLUMN_HEIGHT = "height";
	public static final String COLUMN_LENGTH = "length";
	public static final String COLUMN_WEIGHT = "weight";
	public static final String COLUMN_HEALING_RATE = "healingRate";
	public static final String COLUMN_BASE_HITS = "baseHits";
	public static final String COLUMN_BASE_ENDURANCE = "baseEndurance";
	public static final String COLUMN_SIZE_ID = "sizeId";
	public static final String COLUMN_ARMOR_TYPE = "armorType";
	public static final String COLUMN_CRITICAL_CODE_ID = "criticalCodeId";
	public static final String COLUMN_BASE_MOVEMENT_RATE = "baseMovementRate";
	public static final String COLUMN_DEFENSIVE_BONUS = "defensiveBonus";
	public static final String COLUMN_ENDURANCE = "endurance";
	public static final String COLUMN_OFFENSIVE_BONUS = "offensiveBonus";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_ARCHETYPE_ID + " INTEGER NOT NULL, "
			+ COLUMN_LEVEL + " INTEGER NOT NULL, "
			+ COLUMN_LEVEL_SPREAD + " TEXT NOT NULL, "
			+ COLUMN_CATEGORY_ID + " INTEGER NOT NULL, "
			+ COLUMN_TYPE_ID + " INTEGER NOT NULL, "
			+ COLUMN_HEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_LENGTH + " INTEGER NOT NULL, "
			+ COLUMN_WEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_HEALING_RATE + " REAL NOT NULL, "
			+ COLUMN_BASE_HITS + " INTEGER NOT NULL, "
			+ COLUMN_BASE_ENDURANCE + " INTEGER NOT NULL, "
			+ COLUMN_SIZE_ID + " INTEGER NOT NULL, "
			+ COLUMN_ARMOR_TYPE + " INTEGER NOT NULL, "
			+ COLUMN_CRITICAL_CODE_ID + " INTEGER NOT NULL, "
			+ COLUMN_BASE_MOVEMENT_RATE + " INTEGER NOT NULL, "
			+ COLUMN_DEFENSIVE_BONUS + " INTEGER NOT NULL, "
			+ COLUMN_ENDURANCE + " INTEGER NOT NULL, "
			+ COLUMN_OFFENSIVE_BONUS + " INTEGER NOT NULL"
			+ ")";

	public static final String[] COLUMNS = new String[] { COLUMN_ID,
			COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_ARCHETYPE_ID,
			COLUMN_LEVEL, COLUMN_LEVEL_SPREAD, COLUMN_CATEGORY_ID,
			COLUMN_TYPE_ID, COLUMN_HEIGHT, COLUMN_LENGTH,
			COLUMN_WEIGHT, COLUMN_HEALING_RATE, COLUMN_BASE_HITS,
			COLUMN_BASE_ENDURANCE, COLUMN_SIZE_ID, COLUMN_ARMOR_TYPE,
			COLUMN_CRITICAL_CODE_ID, COLUMN_BASE_MOVEMENT_RATE, COLUMN_DEFENSIVE_BONUS,
			COLUMN_ENDURANCE, COLUMN_OFFENSIVE_BONUS};
}
