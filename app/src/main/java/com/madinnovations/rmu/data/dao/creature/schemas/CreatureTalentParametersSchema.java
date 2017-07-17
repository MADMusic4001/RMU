/*
 *
 *   Copyright (C) 2017 MadInnovations
 *   <p/>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   <p/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   <p/>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *
 */
package com.madinnovations.rmu.data.dao.creature.schemas;

import com.madinnovations.rmu.data.dao.character.schemas.CharacterSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterTalentsSchema;

/**
 * Database schema data for the creature_talent_parameters table
 */
public interface CreatureTalentParametersSchema {
    String TABLE_NAME = "creature_talent_parameters";

    String COLUMN_CREATURE_ID        = "creatureId";
    String COLUMN_TALENT_INSTANCE_ID = "talentInstanceId";
    String COLUMN_PARAMETER_NAME     = "parameterName";
    String COLUMN_INT_VALUE          = "intValue";
	String COLUMN_ENUM_NAME          = "enumName";

    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + "("
            + COLUMN_CREATURE_ID + " INTEGER NOT NULL REFERENCES "
                + CreatureSchema.TABLE_NAME + "(" + CreatureSchema.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_TALENT_INSTANCE_ID + " INTEGER NOT NULL REFERENCES "
				+ CharacterTalentsSchema.TABLE_NAME + "(" + CharacterTalentsSchema.COLUMN_ID + "), "
			+ COLUMN_PARAMETER_NAME + " TEXT NOT NULL, "
            + COLUMN_INT_VALUE + " INTEGER, "
			+ COLUMN_ENUM_NAME + " TEXT, "
            + "PRIMARY KEY(" + COLUMN_CREATURE_ID + "," + COLUMN_TALENT_INSTANCE_ID + "," + COLUMN_PARAMETER_NAME + ")"
            + ")";

    String[] COLUMNS = new String[] {COLUMN_CREATURE_ID, COLUMN_TALENT_INSTANCE_ID, COLUMN_PARAMETER_NAME, COLUMN_INT_VALUE,
			COLUMN_ENUM_NAME};
}
