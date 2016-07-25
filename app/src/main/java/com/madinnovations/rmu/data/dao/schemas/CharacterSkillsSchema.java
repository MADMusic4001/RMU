package com.madinnovations.rmu.data.dao.schemas;

/**
 * Database schema data for the character_skills table
 */
public interface CharacterSkillsSchema {
    public static final String TABLE_NAME = "character_skills";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CHARACTER_ID = "characterId";
    public static final String COLUMN_SKILL_ID = "skillId";
    public static final String COLUMN_RANKS = "ranks";

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_CHARACTER_ID  + " INTEGER NOT NULL, "
            + COLUMN_SKILL_ID + " INTEGER NOT NULL, "
            + COLUMN_RANKS  + " INTEGER NOT NULL"
            + ")";

    public static final String[] COLUMNS = new String[] { COLUMN_ID,
            COLUMN_CHARACTER_ID, COLUMN_SKILL_ID, COLUMN_RANKS};
}
