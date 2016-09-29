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
    String TABLE_NAME = "damage_results";

    String COLUMN_ID = "id";
	String COLUMN_DAMAGE_RESULT_ROW_ID = "damageResultRowId";
	String COLUMN_ARMOR_TYPE = "armorType";
    String COLUMN_HITS = "hits";
    String COLUMN_CRITICAL_SEVERITY = "criticalSeverity";
    String COLUMN_CRITICAL_TYPE_ID = "criticalTypeId";

    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_DAMAGE_RESULT_ROW_ID + " INTEGER NOT NULL, "
			+ COLUMN_ARMOR_TYPE + " INTEGER NOT NULL, "
            + COLUMN_HITS  + " INTEGER NOT NULL, "
            + COLUMN_CRITICAL_SEVERITY  + " TEXT, "
            + COLUMN_CRITICAL_TYPE_ID  + " INTEGER, "
			+ "FOREIGN KEY (" + COLUMN_DAMAGE_RESULT_ROW_ID + ") REFERENCES "
				+ DamageResultRowSchema.TABLE_NAME + "(" + DamageResultRowSchema.COLUMN_ID + "), "
            + "FOREIGN KEY (" + COLUMN_CRITICAL_TYPE_ID + ") REFERENCES "
				+ CriticalTypeSchema.TABLE_NAME + "(" + CriticalTypeSchema.COLUMN_ID + ")"
            + ")";

    String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_HITS, COLUMN_CRITICAL_SEVERITY, COLUMN_CRITICAL_TYPE_ID};
}
