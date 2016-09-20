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

import com.madinnovations.rmu.data.dao.common.schemas.TalentSchema;

/**
 * Database schema data for the creature_type_talents table
 */
public interface CreatureTypeTalentsSchema {
	String TABLE_NAME = "creature_type_talents";

	String COLUMN_ID = "id";
	String COLUMN_CREATURE_TYPE_ID = "creatureTypeId";
	String COLUMN_TALENT_ID = "talentId";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_CREATURE_TYPE_ID + " INTEGER NOT NULL, "
			+ COLUMN_TALENT_ID + " INTEGER NOT NULL, "
			+ "FOREIGN KEY (" + COLUMN_CREATURE_TYPE_ID + ") REFERENCES " + CreatureTypeSchema.TABLE_NAME
				+ "(" + CreatureTypeSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_TALENT_ID + ") REFERENCES " + TalentSchema.TABLE_NAME + "(" + TalentSchema.COLUMN_ID + ")"
			+ ")";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_CREATURE_TYPE_ID, COLUMN_TALENT_ID};
}
