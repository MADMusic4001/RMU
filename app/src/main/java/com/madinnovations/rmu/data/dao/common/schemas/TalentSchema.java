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
 * Database schema data for the talents table
 */
public interface TalentSchema {
	String TABLE_NAME = "talents";

	String COLUMN_ID               = "id";
	String COLUMN_CATEGORY_ID      = "categoryId";
	String COLUMN_NAME             = "name";
	String COLUMN_DESCRIPTION      = "description";
	String COLUMN_IS_FLAW          = "isFlaw";
	String COLUMN_MIN_TIER         = "minTier";
	String COLUMN_MAX_TIER         = "maxTier";
	String COLUMN_DP_COST          = "dpCost";
	String COLUMN_DP_COST_PER_TIER = "dpCostPerTier";
	String COLUMN_IS_SITUATIONAL   = "isSituational";
	String COLUMN_ACTION           = "action";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_CATEGORY_ID + " INTEGER NOT NULL, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_IS_FLAW + " INTEGER NOT NULL, "
			+ COLUMN_MIN_TIER + " INTEGER NOT NULL, "
			+ COLUMN_MAX_TIER + " INTEGER NOT NULL, "
			+ COLUMN_DP_COST + " INTEGER NOT NULL, "
			+ COLUMN_DP_COST_PER_TIER + " INTEGER NOT NULL, "
			+ COLUMN_IS_SITUATIONAL + " INTEGER NOT NULL, "
			+ COLUMN_ACTION + " TEXT NOT NULL, "
			+ "FOREIGN KEY (" + COLUMN_CATEGORY_ID + ") REFERENCES " + TalentCategorySchema.TABLE_NAME
				+ "(" + TalentCategorySchema.COLUMN_ID + ")"
			+ ")";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_CATEGORY_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_IS_FLAW,
			COLUMN_MIN_TIER, COLUMN_MAX_TIER, COLUMN_DP_COST, COLUMN_DP_COST_PER_TIER, COLUMN_IS_SITUATIONAL,
			COLUMN_ACTION};
}
