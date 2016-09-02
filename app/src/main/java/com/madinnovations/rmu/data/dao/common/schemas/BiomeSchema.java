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
 * Database schema data for the biomes table
 */
public interface BiomeSchema {
	String TABLE_NAME = "biomes";

	String COLUMN_ID = "id";
	String COLUMN_CODE = "code";
	String COLUMN_DESCRIPTION = "description";
	String COLUMN_HUMIDITY = "humidity";
	String COLUMN_FLORA = "flora";
	String COLUMN_FAUNA = "fauna";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_CODE + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_HUMIDITY + " TEXT NOT NULL, "
			+ COLUMN_FLORA + " TEXT NOT NULL, "
			+ COLUMN_FAUNA + " TEXT NOT NULL"
			+ ")";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_CODE, COLUMN_DESCRIPTION, COLUMN_HUMIDITY, COLUMN_FLORA,
			COLUMN_FAUNA};
}
