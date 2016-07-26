package com.madinnovations.rmu.data.dao.item.schemas;

/**
 * Database schema data for the weapons table
 */
public interface WeaponSchema {
    public static final String TABLE_NAME = "weapons";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SKILL_ID = "skillId";
    public static final String COLUMN_DAMAGE_TABLE_ID = "damageTableId";

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_SKILL_ID + " INTEGER NOT NULL, "
            + COLUMN_DAMAGE_TABLE_ID + " INTEGER NOT NULL"
            + ")";

    public static final String[] COLUMNS = new String[]{COLUMN_ID,
            COLUMN_SKILL_ID, COLUMN_DAMAGE_TABLE_ID};
}
