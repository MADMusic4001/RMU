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

import com.madinnovations.rmu.data.dao.common.schemas.SkillSchema;

/**
 * Database schema data for the creature_skill_bonus table
 */
public interface CreatureSkillBonusSchema {
    String TABLE_NAME = "creature_skill_bonus";

    String COLUMN_CREATURE_ID = "creatureId";
    String COLUMN_SKILL_ID = "skillId";
    String COLUMN_BONUS = "bonus";
    String COLUMN_IS_PRIMARY = "isPrimary";

    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + "("
            + COLUMN_CREATURE_ID + " INTEGER NOT NULL REFERENCES "
                + CreatureSchema.TABLE_NAME + "(" + CreatureSchema.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_SKILL_ID + " INTEGER NOT NULL REFERENCES "
				+ SkillSchema.TABLE_NAME + "(" + SkillSchema.COLUMN_ID + "), "
            + COLUMN_BONUS  + " INTEGER NOT NULL, "
            + COLUMN_IS_PRIMARY + " INTEGER NOT NULL, "
            + "PRIMARY KEY(" + COLUMN_CREATURE_ID + "," + COLUMN_SKILL_ID + ")"
            + ")";

    String[] COLUMNS = new String[] {COLUMN_CREATURE_ID, COLUMN_SKILL_ID, COLUMN_BONUS, COLUMN_IS_PRIMARY};
}
