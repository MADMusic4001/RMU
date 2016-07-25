package com.madinnovations.rmu.data.dao.schemas;

/**
 * Database schema data for the character_talents table
 */
public interface CharacterTalentsSchema {
    public static final String TABLE_NAME = "character_talents";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CHARACTER_ID = "characterId";
    public static final String COLUMN_TALENT_ID = "talentId";
    public static final String COLUMN_TIERS = "tiers";

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_CHARACTER_ID  + " INTEGER NOT NULL, "
            + COLUMN_TALENT_ID + " INTEGER NOT NULL, "
            + COLUMN_TIERS  + " INTEGER NOT NULL"
            + ")";

    public static final String[] COLUMNS = new String[] { COLUMN_ID,
            COLUMN_CHARACTER_ID, COLUMN_TALENT_ID, COLUMN_TIERS};
}
