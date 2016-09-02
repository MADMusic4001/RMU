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
 * Database schema data for the parameters table
 */
public interface ParameterSchema {
	String TABLE_NAME = "parameters";

	String COLUMN_ID = "id";
	String COLUMN_NAME = "name";
	String COLUMN_DESCRIPTION = "description";
	String COLUMN_BASE_VALUE = "baseValue";
	String COLUMN_PER_VALUE = "perValue";
	String COLUMN_PER_LEVEL = "perLevel";
	String COLUMN_PER_TIER = "perTier";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_BASE_VALUE + " TEXT, "
			+ COLUMN_PER_VALUE + " TEXT, "
			+ COLUMN_PER_LEVEL + " INTEGER NOT NULL, "
			+ COLUMN_PER_TIER + " INTEGER NOT NULL"
			+ ")";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_BASE_VALUE, COLUMN_PER_VALUE,
			COLUMN_PER_LEVEL, COLUMN_PER_TIER};
}
