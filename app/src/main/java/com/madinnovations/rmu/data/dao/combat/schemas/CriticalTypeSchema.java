package com.madinnovations.rmu.data.dao.combat.schemas;

/**
 * Database schema data for the critical_types table
 */
public interface CriticalTypeSchema {
    public static final String TABLE_NAME = "critical_types";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NAME  + " TEXT NOT NULL"
            + ")";

    public static final String[] COLUMNS = new String[] { COLUMN_ID,
            COLUMN_NAME};
}
