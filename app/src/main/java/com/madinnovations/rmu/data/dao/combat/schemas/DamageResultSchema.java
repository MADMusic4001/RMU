package com.madinnovations.rmu.data.dao.combat.schemas;

/**
 * Database schema data for the damage_results table
 */
public interface DamageResultSchema {
    public static final String TABLE_NAME = "damage_results";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DAMAGE_TABLE_ID = "damageTableId";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_MIN_ROLL = "minRoll";
    public static final String COLUMN_MAX_ROLL = "maxRoll";
    public static final String COLUMN_HITS = "hits";
    public static final String COLUMN_CRITICAL_SEVERITY = "criticalSeverity";
    public static final String COLUMN_CRITICAL_TYPE_ID = "criticalTypeId";

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_DAMAGE_TABLE_ID  + " INTEGER NOT NULL, "
            + COLUMN_NAME  + " TEXT NOT NULL, "
            + COLUMN_DESCRIPTION + " TEXT NOT NULL, "
            + COLUMN_MIN_ROLL  + " INTEGER NOT NULL, "
            + COLUMN_MAX_ROLL  + " INTEGER NOT NULL, "
            + COLUMN_HITS  + " INTEGER NOT NULL, "
            + COLUMN_CRITICAL_SEVERITY  + " TEXT NOT NULL, "
            + COLUMN_CRITICAL_TYPE_ID  + " INTEGER NOT NULL"
            + ")";

    public static final String[] COLUMNS = new String[] { COLUMN_ID,
            COLUMN_DAMAGE_TABLE_ID, COLUMN_NAME, COLUMN_DESCRIPTION,
            COLUMN_MIN_ROLL, COLUMN_MAX_ROLL, COLUMN_HITS,
            COLUMN_CRITICAL_SEVERITY, COLUMN_CRITICAL_TYPE_ID};
}
