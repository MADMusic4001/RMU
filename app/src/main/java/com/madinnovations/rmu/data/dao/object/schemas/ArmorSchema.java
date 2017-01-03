/**
 * Copyright (C) 2016 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.dao.object.schemas;

/**
 * Database schema data for the armor table
 */
public interface ArmorSchema {
	String TABLE_NAME = "armor";

	String COLUMN_ID = "id";
	String COLUMN_SMALL_COST = "smallCost";
	String COLUMN_MEDIUM_COST = "mediumCost";
	String COLUMN_BIG_COST = "bigCost";
	String COLUMN_LARGE_COST = "largeCost";
	String COLUMN_WEIGHT_PERCENT = "weightPercent";
	String COLUMN_ARMOR_TYPE = "armorType";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_SMALL_COST + " REAL NOT NULL, "
			+ COLUMN_MEDIUM_COST + " REAL NOT NULL, "
			+ COLUMN_BIG_COST+ " REAL NOT NULL, "
			+ COLUMN_LARGE_COST + " REAL NOT NULL, "
			+ COLUMN_WEIGHT_PERCENT + " REAL NOT NULL, "
			+ COLUMN_ARMOR_TYPE+ " INTEGER NOT NULL"
			+ ")";

	String[] COLUMNS = new String[]{COLUMN_ID, COLUMN_SMALL_COST, COLUMN_MEDIUM_COST, COLUMN_BIG_COST, COLUMN_LARGE_COST,
			COLUMN_WEIGHT_PERCENT, COLUMN_ARMOR_TYPE};
}
