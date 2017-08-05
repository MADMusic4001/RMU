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

import com.madinnovations.rmu.data.dao.common.schemas.SpecializationSchema;

/**
 * Database schema data for the creature_specialization_bonus table
 */
public interface CreatureSpecializationBonusSchema {
    String TABLE_NAME = "creature_specialization_bonus";

    String COLUMN_CREATURE_ID       = "creatureId";
    String COLUMN_SPECIALIZATION_ID = "specializationId";
    String COLUMN_BONUS             = "bonus";

    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + "("
            + COLUMN_CREATURE_ID + " INTEGER NOT NULL REFERENCES "
                + CreatureSchema.TABLE_NAME + "(" + CreatureSchema.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_SPECIALIZATION_ID + " INTEGER NOT NULL REFERENCES "
				+ SpecializationSchema.TABLE_NAME + "(" + SpecializationSchema.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_BONUS + " INTEGER NOT NULL, "
            + "PRIMARY KEY(" + COLUMN_CREATURE_ID + "," + COLUMN_SPECIALIZATION_ID + ")"
            + ")";

    String[] COLUMNS = new String[] {COLUMN_CREATURE_ID, COLUMN_SPECIALIZATION_ID, COLUMN_BONUS};
}
