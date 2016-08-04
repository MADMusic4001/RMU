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
            + COLUMN_CRITICAL_TYPE_ID  + " INTEGER NOT NULL, "
            + "FOREIGN KEY (" + COLUMN_DAMAGE_TABLE_ID + ") REFERENCES " + DamageTableSchema.TABLE_NAME + "(" + DamageTableSchema.COLUMN_ID + "), "
            + "FOREIGN KEY (" + COLUMN_CRITICAL_TYPE_ID + ") REFERENCES " + CriticalTypeSchema.TABLE_NAME + "(" + CriticalTypeSchema.COLUMN_ID + ")"
            + ")";

    public static final String[] COLUMNS = new String[] { COLUMN_ID,
            COLUMN_DAMAGE_TABLE_ID, COLUMN_NAME, COLUMN_DESCRIPTION,
            COLUMN_MIN_ROLL, COLUMN_MAX_ROLL, COLUMN_HITS,
            COLUMN_CRITICAL_SEVERITY, COLUMN_CRITICAL_TYPE_ID};
}
