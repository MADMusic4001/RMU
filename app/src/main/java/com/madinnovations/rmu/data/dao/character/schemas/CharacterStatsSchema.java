package com.madinnovations.rmu.data.dao.character.schemas;

/**
 * Database schema data for the character_stats table
 */
public interface CharacterStatsSchema {
    public static final String TABLE_NAME = "character_stats";

    public static final String COLUMN_CHARACTER_ID = "characterId";
    public static final String COLUMN_STAT_ID = "statId";
    public static final String COLUMN_CURRENT_VALUE = "currentValue";
    public static final String COLUMN_POTENTIAL_VALUE = "potentialValue";

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_CHARACTER_ID  + " INTEGER NOT NULL, "
            + COLUMN_STAT_ID + " INTEGER NOT NULL, "
            + COLUMN_CURRENT_VALUE  + " INTEGER NOT NULL"
            + COLUMN_POTENTIAL_VALUE + " INTEGER NOT NULL, "
            + "PRIMARY KEY(" + COLUMN_CHARACTER_ID + "," + COLUMN_STAT_ID + ")"
            + ")";

    public static final String[] COLUMNS = new String[] {COLUMN_CHARACTER_ID, COLUMN_STAT_ID, COLUMN_CURRENT_VALUE, COLUMN_POTENTIAL_VALUE};
}
