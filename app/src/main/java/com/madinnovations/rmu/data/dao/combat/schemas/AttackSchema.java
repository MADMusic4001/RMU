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

import com.madinnovations.rmu.data.dao.common.schemas.SpecializationSchema;

/**
 * Database schema data for the attacks table
 */
public interface AttackSchema {
    public static final String TABLE_NAME = "attacks";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DAMAGE_TABLE_ID = "damageTableId";
    public static final String COLUMN_SPECIALIZATION_ID = "specializationId";

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_CODE + " TEXT NOT NULL, "
            + COLUMN_NAME  + " TEXT NOT NULL, "
            + COLUMN_DAMAGE_TABLE_ID + " INTEGER NOT NULL, "
            + COLUMN_SPECIALIZATION_ID + " INTEGER, "
            + "FOREIGN KEY (" + COLUMN_DAMAGE_TABLE_ID + ") REFERENCES " + DamageTableSchema.TABLE_NAME + "(" + DamageTableSchema.COLUMN_ID + "), "
            + "FOREIGN KEY (" + COLUMN_SPECIALIZATION_ID + ") REFERENCES " + SpecializationSchema.TABLE_NAME + "(" + SpecializationSchema.COLUMN_ID + ")"
            + ");";

    public static final String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_CODE, COLUMN_NAME, COLUMN_DAMAGE_TABLE_ID,
            COLUMN_SPECIALIZATION_ID};
}
