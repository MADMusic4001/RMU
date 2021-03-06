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
package com.madinnovations.rmu.data.dao.common.schemas;

/**
 * Database schema data for the skill_categories table
 */
public interface SkillCategorySchema {
	String TABLE_NAME = "skill_categories";

	String COLUMN_ID = "id";
	String COLUMN_NAME = "name";
	String COLUMN_DESCRIPTION = "description";
	String COLUMN_IS_COMBAT = "isCombat";
	String COLUMN_IS_CRAFT_AND_TRADE = "isCraftAndTrade";
	String COLUMN_NO_STATS = "noStats";
	String COLUMN_REALM_STATS = "realmStats";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_IS_COMBAT + " INTEGER NOT NULL, "
			+ COLUMN_IS_CRAFT_AND_TRADE + " INTEGER NOT NULL, "
			+ COLUMN_NO_STATS + " INTEGER NOT NULL, "
			+ COLUMN_REALM_STATS + " INTEGER NOT NULL"
			+ ")";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_IS_COMBAT, COLUMN_IS_CRAFT_AND_TRADE,
			COLUMN_NO_STATS, COLUMN_REALM_STATS};
}
