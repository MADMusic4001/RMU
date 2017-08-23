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
 * Database schema data for the additionalEffects table
 */
public interface AdditionalEffectSchema {
	String TABLE_NAME = "additionalEffects";

	String COLUMN_ID = "id";
	String COLUMN_CRITICAL_RESULT_ID = "criticalResultId";
	String COLUMN_FUMBLE_ID = "fumbleId";
	String COLUMN_TARGET_TYPE_NAME = "targetTypeName";
	String COLUMN_EFFECT_NAME = "effectName";
	String COLUMN_VALUE1 = "value1";
	String COLUMN_VALUE2 = "value2";
	String COLUMN_VALUE3 = "value3";
	String COLUMN_VALUE4 = "value4";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_CRITICAL_RESULT_ID + " INTEGER REFERENCES " + CriticalResultSchema.TABLE_NAME
				+ "(" + CriticalResultSchema.COLUMN_ID + "), "
			+ COLUMN_FUMBLE_ID + " INTEGER REFERENCES " + FumbleSchema.TABLE_NAME
				+ "(" + FumbleSchema.COLUMN_ID + "), "
			+ COLUMN_TARGET_TYPE_NAME + " TEXT NOT NULL, "
			+ COLUMN_EFFECT_NAME  + " TEXT NOT NULL, "
			+ COLUMN_VALUE1 + " TEXT NOT NULL, "
			+ COLUMN_VALUE2 + " TEXT, "
			+ COLUMN_VALUE3 + " TEXT, "
			+ COLUMN_VALUE4 + " TEXT"
			+ ");";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_CRITICAL_RESULT_ID, COLUMN_FUMBLE_ID, COLUMN_TARGET_TYPE_NAME,
			COLUMN_EFFECT_NAME, COLUMN_VALUE1, COLUMN_VALUE2, COLUMN_VALUE3, COLUMN_VALUE4};
}
