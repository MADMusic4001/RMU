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

import com.madinnovations.rmu.data.dao.spells.schemas.RealmSchema;

/**
 * Database schema data for the professions table
 */
public interface ProfessionSchema {
	String TABLE_NAME = "professions";

	String COLUMN_ID = "id";
	String COLUMN_NAME = "name";
	String COLUMN_DESCRIPTION = "description";
	String COLUMN_REALM1_ID = "realm1Id";
	String COLUMN_REALM2_ID = "realm2Id";
	String COLUMN_COST_ARRAY = "costArray";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_REALM1_ID + " INTEGER, "
			+ COLUMN_REALM2_ID + " INTEGER, "
			+ "FOREIGN KEY fk_profession_realm1 (" + COLUMN_REALM1_ID + ") REFERENCES "
				+ RealmSchema.TABLE_NAME + "(" + RealmSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY fk_profession_realm2 (" + COLUMN_REALM2_ID + ") REFERENCES "
				+ RealmSchema.TABLE_NAME + "(" + RealmSchema.COLUMN_ID +	")"
			+ ");";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_REALM1_ID, COLUMN_REALM2_ID};
}
