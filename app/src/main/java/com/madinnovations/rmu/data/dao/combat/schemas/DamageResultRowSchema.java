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
 * Database schema data for the damage_result_rows table
 */
public interface DamageResultRowSchema {
    public static final String TABLE_NAME = "damage_result_rows";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DAMAGE_TABLE_ID = "damageTableId";
    public static final String COLUMN_RANGE_LOW_VALUE = "rangeLowValue";
    public static final String COLUMN_RANGE_HIGH_VALUE = "rangeHighValue";
    public static final String[] COLUMN_AT_RESULT_IDS = {"at1ResultId", "at2ResultId", "at3ResultId", "at4ResultId", "at5ResultId",
            "at6ResultId", "at7ResultId", "at8ResultId", "at9ResultId", "at10ResultId"};

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_DAMAGE_TABLE_ID  + " INTEGER NOT NULL, "
            + COLUMN_RANGE_LOW_VALUE  + " INTEGER NOT NULL, "
            + COLUMN_RANGE_HIGH_VALUE  + " INTEGER NOT NULL, "
            + COLUMN_AT_RESULT_IDS[0]  + " INTEGER, "
            + COLUMN_AT_RESULT_IDS[1]  + " INTEGER, "
            + COLUMN_AT_RESULT_IDS[2]  + " INTEGER, "
            + COLUMN_AT_RESULT_IDS[3]  + " INTEGER, "
            + COLUMN_AT_RESULT_IDS[4]  + " INTEGER, "
            + COLUMN_AT_RESULT_IDS[5]  + " INTEGER, "
            + COLUMN_AT_RESULT_IDS[6]  + " INTEGER, "
            + COLUMN_AT_RESULT_IDS[7]  + " INTEGER, "
            + COLUMN_AT_RESULT_IDS[8]  + " INTEGER, "
            + COLUMN_AT_RESULT_IDS[9]  + " INTEGER, "
            + "FOREIGN KEY (" + COLUMN_DAMAGE_TABLE_ID + ") REFERENCES " + DamageTableSchema.TABLE_NAME + "(" + DamageTableSchema.COLUMN_ID + "), "
            + "FOREIGN KEY (" + COLUMN_AT_RESULT_IDS[0] + ") REFERENCES " + DamageResultSchema.TABLE_NAME + "(" + DamageResultSchema.COLUMN_ID + "), "
            + "FOREIGN KEY (" + COLUMN_AT_RESULT_IDS[1] + ") REFERENCES " + DamageResultSchema.TABLE_NAME + "(" + DamageResultSchema.COLUMN_ID + "), "
            + "FOREIGN KEY (" + COLUMN_AT_RESULT_IDS[2] + ") REFERENCES " + DamageResultSchema.TABLE_NAME + "(" + DamageResultSchema.COLUMN_ID + "), "
            + "FOREIGN KEY (" + COLUMN_AT_RESULT_IDS[3] + ") REFERENCES " + DamageResultSchema.TABLE_NAME + "(" + DamageResultSchema.COLUMN_ID + "), "
            + "FOREIGN KEY (" + COLUMN_AT_RESULT_IDS[4] + ") REFERENCES " + DamageResultSchema.TABLE_NAME + "(" + DamageResultSchema.COLUMN_ID + "), "
            + "FOREIGN KEY (" + COLUMN_AT_RESULT_IDS[5] + ") REFERENCES " + DamageResultSchema.TABLE_NAME + "(" + DamageResultSchema.COLUMN_ID + "), "
            + "FOREIGN KEY (" + COLUMN_AT_RESULT_IDS[6] + ") REFERENCES " + DamageResultSchema.TABLE_NAME + "(" + DamageResultSchema.COLUMN_ID + "), "
            + "FOREIGN KEY (" + COLUMN_AT_RESULT_IDS[7] + ") REFERENCES " + DamageResultSchema.TABLE_NAME + "(" + DamageResultSchema.COLUMN_ID + "), "
            + "FOREIGN KEY (" + COLUMN_AT_RESULT_IDS[8] + ") REFERENCES " + DamageResultSchema.TABLE_NAME + "(" + DamageResultSchema.COLUMN_ID + "), "
            + "FOREIGN KEY (" + COLUMN_AT_RESULT_IDS[9] + ") REFERENCES " + DamageResultSchema.TABLE_NAME + "(" + DamageResultSchema.COLUMN_ID + ")"
            + ")";

    public static final String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_DAMAGE_TABLE_ID, COLUMN_RANGE_LOW_VALUE,
            COLUMN_RANGE_HIGH_VALUE, COLUMN_AT_RESULT_IDS[0], COLUMN_AT_RESULT_IDS[1], COLUMN_AT_RESULT_IDS[2], COLUMN_AT_RESULT_IDS[3],
            COLUMN_AT_RESULT_IDS[4], COLUMN_AT_RESULT_IDS[5], COLUMN_AT_RESULT_IDS[6], COLUMN_AT_RESULT_IDS[7], COLUMN_AT_RESULT_IDS[8],
            COLUMN_AT_RESULT_IDS[9]};
}
