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

import com.madinnovations.rmu.data.dao.common.schemas.SkillCategorySchema;

/**
 * Database schema data for the profession_skill_costs table
 */
public interface ProfessionSkillCostSchema {
	public static final String TABLE_NAME = "profession_skill_costs";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_PROFESSION_ID = "professionId";
	public static final String COLUMN_SKILL_CATEGORY_ID = "skillCategoryId";
	public static final String COLUMN_FIRST_COST = "firstCost";
	public static final String COLUMN_SECOND_COST = "secondCost";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_PROFESSION_ID + " INTEGER NOT NULL, "
			+ COLUMN_SKILL_CATEGORY_ID + " INTEGER NOT NULL, "
			+ COLUMN_FIRST_COST + " INTEGER NOT NULL, "
			+ COLUMN_SECOND_COST + " INTEGER NOT NULL, "
			+ "CONSTRAINT unique_profession_skill_category UNIQUE(" + COLUMN_PROFESSION_ID + "," + COLUMN_SKILL_CATEGORY_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_PROFESSION_ID + ") REFERENCES " + ProfessionSchema.TABLE_NAME + "(" + ProfessionSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_SKILL_CATEGORY_ID + ") REFERENCES " + SkillCategorySchema.TABLE_NAME + "(" + SkillCategorySchema.COLUMN_ID + ")"
			+ ")";

	public static final String[] COLUMNS = new String[] {COLUMN_ID, COLUMN_PROFESSION_ID, COLUMN_SKILL_CATEGORY_ID, COLUMN_FIRST_COST,
			COLUMN_SECOND_COST};
}
