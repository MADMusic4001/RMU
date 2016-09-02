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
 * Database schema data for the skill_stats table
 */
public interface SkillStatsSchema {
	String TABLE_NAME = "skill_stats";

	String COLUMN_ID = "id";
	String COLUMN_SKILL_ID = "skillId";
	String COLUMN_STAT_ID = "statId";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_SKILL_ID + " INTEGER NOT NULL, "
			+ COLUMN_STAT_ID + " INTEGER NOT NULL, "
			+ "FOREIGN KEY (" + COLUMN_SKILL_ID + ") REFERENCES " + SkillSchema.TABLE_NAME + "(" + SkillSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_STAT_ID + ") REFERENCES " + StatSchema.TABLE_NAME + "(" + StatSchema.COLUMN_ID + ")"
			+ ")";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_SKILL_ID, COLUMN_STAT_ID};
}
