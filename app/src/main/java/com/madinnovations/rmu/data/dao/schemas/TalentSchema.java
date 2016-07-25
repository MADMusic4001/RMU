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
 * Database schema data for the talents table
 */
public interface TalentSchema {
	public static final String TABLE_NAME = "talents";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_CATEGORY_ID = "categoryId";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_AFFECTED_SKILL_ID = "affectedSkillId";
	public static final String COLUMN_INITIAL_COST = "initialCost";
	public static final String COLUMN_COST_PER_TIER = "costPerTier";
	public static final String COLUMN_BONUS_PER_TIER = "bonusPerTier";
	public static final String COLUMN_SITUATIONAL = "situational";
	public static final String COLUMN_ACTION_POINTS = "actionPoints";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_CATEGORY_ID + " INTEGER NOT NULL, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_AFFECTED_SKILL_ID + " INTEGER NOT NULL, "
			+ COLUMN_INITIAL_COST + " INTEGER NOT NULL, "
			+ COLUMN_COST_PER_TIER + " INTEGER NOT NULL, "
			+ COLUMN_BONUS_PER_TIER + " INTEGER NOT NULL, "
			+ COLUMN_SITUATIONAL + " INTEGER NOT NULL, "
			+ COLUMN_ACTION_POINTS + " INTEGER NOT NULL"
			+ ")";

	public static final String[] COLUMNS = new String[] { COLUMN_ID,
			COLUMN_CATEGORY_ID, COLUMN_NAME, COLUMN_DESCRIPTION,
			COLUMN_AFFECTED_SKILL_ID, COLUMN_INITIAL_COST, COLUMN_COST_PER_TIER,
			COLUMN_BONUS_PER_TIER, COLUMN_SITUATIONAL, COLUMN_ACTION_POINTS};
}
