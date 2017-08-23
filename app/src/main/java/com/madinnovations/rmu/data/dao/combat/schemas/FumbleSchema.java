/*
 *
 *   Copyright (C) 2017 MadInnovations
 *   <p/>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   <p/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   <p/>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *
 */
package com.madinnovations.rmu.data.dao.combat.schemas;

/**
 * Database schema data for the fumbles table
 */
public interface FumbleSchema {
    String TABLE_NAME = "fumbles";

    String COLUMN_ID = "id";
    String COLUMN_MIN_ROLL = "minRoll";
	String COLUMN_MAX_ROLL = "maxRoll";
	String COLUMN_ATTACK_TYPE_NAME = "attackTypeName";
	String COLUMN_DESCRIPTION = "description";

    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_MIN_ROLL + " INTEGER NOT NULL, "
			+ COLUMN_MAX_ROLL + " INTEGER NOT NULL, "
            + COLUMN_ATTACK_TYPE_NAME + " TEXT NOT NULL, "
            + COLUMN_DESCRIPTION + " TEXT NOT NULL"
            + ");";

    String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_MIN_ROLL, COLUMN_MAX_ROLL, COLUMN_ATTACK_TYPE_NAME, COLUMN_DESCRIPTION};
}
