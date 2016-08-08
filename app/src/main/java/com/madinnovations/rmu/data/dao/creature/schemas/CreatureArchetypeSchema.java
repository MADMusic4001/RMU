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
package com.madinnovations.rmu.data.dao.creature.schemas;

import com.madinnovations.rmu.data.dao.common.schemas.StatSchema;

/**
 * Database schema data for the creature_archetypes table
*/
public interface CreatureArchetypeSchema {
	public static final String TABLE_NAME = "creature_archetypes";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_STAT1_ID = "stat1Id";
	public static final String COLUMN_STAT2_ID = "stat2Id";
	public static final String COLUMN_SPELLS = "spells";
	public static final String COLUMN_ROLES = "roles";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_STAT1_ID + " INTEGER NOT NULL, "
			+ COLUMN_STAT2_ID + " INTEGER NOT NULL, "
			+ COLUMN_SPELLS + " TEXT NOT NULL,"
			+ COLUMN_ROLES + " TEXT NOT NULL, "
			+ "FOREIGN KEY (" + COLUMN_STAT1_ID + ") REFERENCES " + StatSchema.TABLE_NAME + "(" + StatSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_STAT2_ID + ") REFERENCES " + StatSchema.TABLE_NAME + "(" + StatSchema.COLUMN_ID + ")"
			+ ")";

	public static final String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_STAT1_ID, COLUMN_STAT2_ID,
			COLUMN_SPELLS, COLUMN_ROLES};
}
