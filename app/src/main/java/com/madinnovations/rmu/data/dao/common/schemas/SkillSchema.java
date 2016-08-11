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
 * Database schema data for the skills table
 */
public interface SkillSchema {
	public static final String TABLE_NAME = "skills";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_CATEGORY_ID = "categoryId";
	public static final String COLUMN_REQUIRES_SPECIALIZATION = "requiresSpecialization";
	public static final String COLUMN_CATEGORY_STATS = "categoryStats";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_CATEGORY_ID + " INTEGER NOT NULL, "
			+ COLUMN_REQUIRES_SPECIALIZATION + " INTEGER NOT NULL, "
			+ COLUMN_CATEGORY_STATS + " INTEGER NOT NULL, "
			+ "FOREIGN KEY (" + COLUMN_CATEGORY_ID + ") REFERENCES " + SkillCategorySchema.TABLE_NAME + "(" + SkillCategorySchema.COLUMN_ID + ")"
			+ ")";

	public static final String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_CATEGORY_ID,
			COLUMN_REQUIRES_SPECIALIZATION, COLUMN_CATEGORY_STATS};
}
