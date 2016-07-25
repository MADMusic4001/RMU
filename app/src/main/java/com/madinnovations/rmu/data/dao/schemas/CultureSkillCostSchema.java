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
 * Database schema data for the culture_skill_costs table
 */
public interface CultureSkillCostSchema {
	public static final String TABLE_NAME = "culture_skill_costs";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_PROFESSION_ID = "professionId";
	public static final String COLUMN_SKILL_CATEGORY_ID = "skillCategoryId";
	public static final String COLUMN_SKILL_COST = "skillCost";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_PROFESSION_ID + " INTEGER NOT NULL, "
			+ COLUMN_SKILL_CATEGORY_ID + " INTEGER NOT NULL, "
			+ COLUMN_SKILL_COST + " TEXT NOT NULL"
			+ ")";

	public static final String[] COLUMNS = new String[] { COLUMN_ID,
			COLUMN_PROFESSION_ID, COLUMN_SKILL_CATEGORY_ID, COLUMN_SKILL_COST};
}
