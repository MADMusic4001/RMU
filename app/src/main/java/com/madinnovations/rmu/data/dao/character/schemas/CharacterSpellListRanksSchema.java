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

import com.madinnovations.rmu.data.dao.common.schemas.SpecializationSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellListSchema;

/**
 * Database schema data for the character_spell_list_ranks table
 */
public interface CharacterSpellListRanksSchema {
    String TABLE_NAME = "character_spell_list_ranks";

    String COLUMN_CHARACTER_ID = "characterId";
    String COLUMN_SPELL_LIST_ID = "spellListId";
    String COLUMN_RANKS = "ranks";

    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + "("
            + COLUMN_CHARACTER_ID  + " INTEGER NOT NULL REFERENCES "
                + CharacterSchema.TABLE_NAME + "(" + CharacterSchema.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_SPELL_LIST_ID + " INTEGER NOT NULL REFERENCES "
				+ SpellListSchema.TABLE_NAME + "(" + SpellListSchema.COLUMN_ID + ") ON DELETE CASCADE, "
            + COLUMN_RANKS  + " INTEGER NOT NULL, "
            + "PRIMARY KEY(" + COLUMN_CHARACTER_ID + "," + COLUMN_SPELL_LIST_ID + ")"
            + ")";

    String[] COLUMNS = new String[] {COLUMN_CHARACTER_ID, COLUMN_SPELL_LIST_ID, COLUMN_RANKS};
}
