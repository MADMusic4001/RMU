package com.madinnovations.rmu.data.dao.combat.schemas;

/**
 * Database schema data for the body_parts table
 */
public interface CriticalResultSchema {
    public static final String TABLE_NAME = "critical_results";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SEVERITY_CODE = "severityCode";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_MIN_ROLL = "minRoll";
    public static final String COLUMN_MAX_ROLL = "maxRoll";
    public static final String COLUMN_BODY_PART_ID = "bodyPartId";
    public static final String COLUMN_HITS = "hits";
    public static final String COLUMN_BLEEDING = "bleeding";
    public static final String COLUMN_FATIGUE = "fatigue";
    public static final String COLUMN_BREAKAGE = "breakage";
    public static final String COLUMN_INJURY = "injury";
    public static final String COLUMN_DAZED = "dazed";
    public static final String COLUMN_STUNNED = "stunned";
    public static final String COLUMN_NO_PARRY = "noParry";
    public static final String COLUMN_STAGGERED = "staggered";
    public static final String COLUMN_KNOCK_BACK = "knockBack";
    public static final String COLUMN_PRONE = "prone";
    public static final String COLUMN_GRAPPLED = "grappled";

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_SEVERITY_CODE + " TEXT NOT NULL, "
            + COLUMN_DESCRIPTION  + " TEXT NOT NULL, "
            + COLUMN_MIN_ROLL + " INTEGER NOT NULL, "
            + COLUMN_MAX_ROLL + " INTEGER NOT NULL, "
            + COLUMN_BODY_PART_ID + " INTEGER NOT NULL, "
            + COLUMN_HITS + " INTEGER NOT NULL, "
            + COLUMN_BLEEDING + " INTEGER NOT NULL, "
            + COLUMN_FATIGUE + " INTEGER NOT NULL, "
            + COLUMN_BREAKAGE + " INTEGER NOT NULL, "
            + COLUMN_INJURY + " INTEGER NOT NULL, "
            + COLUMN_DAZED + " INTEGER NOT NULL, "
            + COLUMN_STUNNED + " INTEGER NOT NULL, "
            + COLUMN_NO_PARRY + " INTEGER NOT NULL, "
            + COLUMN_STAGGERED + " INTEGER NOT NULL, "
            + COLUMN_KNOCK_BACK + " INTEGER NOT NULL, "
            + COLUMN_PRONE + " INTEGER NOT NULL, "
            + COLUMN_GRAPPLED + " INTEGER NOT NULL, "
            + "FOREIGN KEY (" + COLUMN_BODY_PART_ID + ") REFERENCES " + BodyPartSchema.TABLE_NAME + "(" + BodyPartSchema.COLUMN_ID + ")"
            + ")";

    public static final String[] COLUMNS = new String[] { COLUMN_ID,
            COLUMN_SEVERITY_CODE, COLUMN_DESCRIPTION, COLUMN_MIN_ROLL,
            COLUMN_MAX_ROLL, COLUMN_BODY_PART_ID, COLUMN_HITS,
            COLUMN_BLEEDING, COLUMN_FATIGUE, COLUMN_BREAKAGE,
            COLUMN_INJURY, COLUMN_DAZED, COLUMN_STUNNED,
            COLUMN_NO_PARRY, COLUMN_STAGGERED, COLUMN_KNOCK_BACK,
            COLUMN_PRONE, COLUMN_GRAPPLED};
}
