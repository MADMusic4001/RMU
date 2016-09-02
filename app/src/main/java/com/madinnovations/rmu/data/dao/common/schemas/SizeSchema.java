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
 * Database schema data for the sizes table
 */
public interface SizeSchema {
	String TABLE_NAME = "sizes";

	String COLUMN_ID = "id";
	String COLUMN_CODE = "code";
	String COLUMN_NAME = "name";
	String COLUMN_EXAMPLES = "examples";
	String COLUMN_MIN_WEIGHT = "minWeight";
	String COLUMN_MAX_WEIGHT = "maxWeight";
	String COLUMN_MIN_HEIGHT = "minHeight";
	String COLUMN_MAX_HEIGHT = "maxHeight";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_CODE + " TEXT NOT NULL, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_EXAMPLES + " TEXT NOT NULL, "
			+ COLUMN_MIN_WEIGHT + " INTEGER, "
			+ COLUMN_MAX_WEIGHT + " INTEGER, "
			+ COLUMN_MIN_HEIGHT + " INTEGER, "
			+ COLUMN_MAX_HEIGHT + " INTEGER"
			+ ")";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_CODE, COLUMN_NAME, COLUMN_EXAMPLES, COLUMN_MIN_WEIGHT,
			COLUMN_MAX_WEIGHT, COLUMN_MIN_HEIGHT, COLUMN_MAX_HEIGHT};
}
