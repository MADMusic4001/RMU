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
 * Database schema data for the skill_category_stats table
 */
public interface SkillCategoryStatsSchema {
	String TABLE_NAME = "skill_category_stats";

	String COLUMN_ID                = "id";
	String COLUMN_SKILL_CATEGORY_ID = "skillCategoryId";
	String COLUMN_STAT_NAME         = "statName";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_SKILL_CATEGORY_ID + " INTEGER NOT NULL REFERENCES "
				+ SkillCategorySchema.TABLE_NAME + "(" + SkillCategorySchema.COLUMN_ID + "), "
			+ COLUMN_STAT_NAME + " TEXT NOT NULL"
			+ ")";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_SKILL_CATEGORY_ID, COLUMN_STAT_NAME};
}
