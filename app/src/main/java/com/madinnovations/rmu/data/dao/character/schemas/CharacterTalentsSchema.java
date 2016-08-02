package com.madinnovations.rmu.data.dao.character.schemas;

import com.madinnovations.rmu.data.dao.common.schemas.TalentSchema;

/**
 * Database schema data for the character_talents table
 */
public interface CharacterTalentsSchema {
    public static final String TABLE_NAME = "character_talents";

    public static final String COLUMN_CHARACTER_ID = "characterId";
    public static final String COLUMN_TALENT_ID = "talentId";
    public static final String COLUMN_TIERS = "tiers";

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_CHARACTER_ID  + " INTEGER NOT NULL, "
            + COLUMN_TALENT_ID + " INTEGER NOT NULL, "
            + COLUMN_TIERS  + " INTEGER NOT NULL, "
            + "PRIMARY KEY(" + COLUMN_CHARACTER_ID + "," + COLUMN_TALENT_ID + "), "
            + "FOREIGN KEY (" + COLUMN_CHARACTER_ID + ") REFERENCES " + CharacterSchema.TABLE_NAME + "(" + CharacterSchema.COLUMN_ID + "), "
            + "FOREIGN KEY (" + COLUMN_TALENT_ID + ") REFERENCES " + TalentSchema.TABLE_NAME + "(" + TalentSchema.COLUMN_ID + ")"
            + ")";

    public static final String[] COLUMNS = new String[] {COLUMN_CHARACTER_ID, COLUMN_TALENT_ID, COLUMN_TIERS};
}
