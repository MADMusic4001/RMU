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

/**
 * Database schema data for the creature_archetypes table
*/
public interface CreatureArchetypeSchema {
	String TABLE_NAME = "creature_archetypes";

	String COLUMN_ID             = "id";
	String COLUMN_NAME           = "name";
	String COLUMN_DESCRIPTION    = "description";
	String COLUMN_STAT1_IS_REALM = "stat1IsRealm";
	String COLUMN_STAT1_NAME     = "stat1Name";
	String COLUMN_STAT2_IS_REALM = "stat2IsRealm";
	String COLUMN_STAT2_NAME     = "stat2Name";
	String COLUMN_SPELLS         = "spells";
	String COLUMN_ROLES          = "roles";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_STAT1_IS_REALM + " INTEGER NOT NULL, "
			+ COLUMN_STAT1_NAME + " TEXT, "
			+ COLUMN_STAT2_IS_REALM + " INTEGER NOT NULL, "
			+ COLUMN_STAT2_NAME + " TEXT, "
			+ COLUMN_SPELLS + " TEXT NOT NULL,"
			+ COLUMN_ROLES + " TEXT NOT NULL"
			+ ")";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_STAT1_IS_REALM, COLUMN_STAT1_NAME,
			COLUMN_STAT2_IS_REALM, COLUMN_STAT2_NAME, COLUMN_SPELLS, COLUMN_ROLES};
}
