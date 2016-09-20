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
package com.madinnovations.rmu.data.dao.common.schemas;

/**
 * Database schema data for the talent_resistances table
 */
public interface TalentResistancesSchema {
	String TABLE_NAME = "talent_resistances";

	String COLUMN_TALENT_ID = "talentId";
	String COLUMN_RESISTANCE = "resistance";
	String COLUMN_VALUE = "value";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_TALENT_ID + " INTEGER NOT NULL, "
			+ COLUMN_RESISTANCE + " TEXT NOT NULL, "
			+ COLUMN_VALUE + " INTEGER NOT NULL, "
			+ "PRIMARY KEY(" + COLUMN_TALENT_ID + "," + COLUMN_RESISTANCE + "), "
			+ "FOREIGN KEY (" + COLUMN_TALENT_ID + ") REFERENCES " + TalentSchema.TABLE_NAME + "(" + TalentSchema.COLUMN_ID + ")"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_TALENT_ID, COLUMN_RESISTANCE, COLUMN_VALUE};
}