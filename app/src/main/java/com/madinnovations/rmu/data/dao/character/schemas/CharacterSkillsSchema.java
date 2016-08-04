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

import com.madinnovations.rmu.data.dao.common.schemas.SkillSchema;

/**
 * Database schema data for the character_skills table
 */
public interface CharacterSkillsSchema {
    public static final String TABLE_NAME = "character_skills";

    public static final String COLUMN_CHARACTER_ID = "characterId";
    public static final String COLUMN_SKILL_ID = "skillId";
    public static final String COLUMN_RANKS = "ranks";

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_CHARACTER_ID  + " INTEGER NOT NULL, "
            + COLUMN_SKILL_ID + " INTEGER NOT NULL, "
            + COLUMN_RANKS  + " INTEGER NOT NULL, "
            + "PRIMARY KEY(" + COLUMN_CHARACTER_ID + "," + COLUMN_SKILL_ID + "), "
            + "FOREIGN KEY (" + COLUMN_CHARACTER_ID + ") REFERENCES " + CharacterSchema.TABLE_NAME + "(" + CharacterSchema.COLUMN_ID + "), "
            + "FOREIGN KEY (" + COLUMN_SKILL_ID + ") REFERENCES " + SkillSchema.TABLE_NAME + "(" + SkillSchema.COLUMN_ID + ")"
            + ")";

    public static final String[] COLUMNS = new String[] {COLUMN_CHARACTER_ID, COLUMN_SKILL_ID, COLUMN_RANKS};
}
