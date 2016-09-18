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
package com.madinnovations.rmu.data.dao.spells.schemas;

/**
 * Database schema data for the spell_aoe_params table
 */
public interface SpellAreaOfEffectParamSchema {
    String TABLE_NAME = "spell_aoe_params";

    String COLUMN_ID = "id";
    String COLUMN_SPELL_ID = "spellId";
    String COLUMN_PARAM_INDEX = "paramIndex";
    String COLUMN_PARAM_VALUE = "paramValue";

    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_SPELL_ID + " INTEGER NOT NULL, "
            + COLUMN_PARAM_INDEX + " INTEGER NOT NULL, "
            + COLUMN_PARAM_VALUE + " INTEGER NOT NULL, "
			+ "FOREIGN KEY (" + COLUMN_SPELL_ID + ") REFERENCES " + SpellSchema.TABLE_NAME + "(" + SpellSchema.COLUMN_ID + ")"
            + ")";

    String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_SPELL_ID, COLUMN_PARAM_INDEX, COLUMN_PARAM_VALUE};
}
