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
 * Database schema data for the talent_parameter table
 */
public interface TalentParametersSchema {
	String TABLE_NAME = "talent_parameter";

	String COLUMN_TALENT_ID      = "talentId";
	String COLUMN_ROW_INDEX      = "rowIndex";
	String COLUMN_PARAMETER_NAME = "parameterName";
	String COLUMN_ENUM_NAME      = "enumName";
	String COLUMN_INITIAL_VALUE  = "initialValue";
	String COLUMN_VALUE_PER      = "valuePerTier";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_TALENT_ID + " INTEGER NOT NULL, "
			+ COLUMN_ROW_INDEX + " INTEGER NOT NULL, "
			+ COLUMN_PARAMETER_NAME + " TEXT NOT NULL, "
			+ COLUMN_ENUM_NAME + " TEXT, "
			+ COLUMN_INITIAL_VALUE + " INTEGER, "
			+ COLUMN_VALUE_PER + " INTEGER, "
			+ "PRIMARY KEY(" + COLUMN_TALENT_ID + "," + COLUMN_ROW_INDEX + "), "
			+ "FOREIGN KEY (" + COLUMN_TALENT_ID + ") REFERENCES " + TalentSchema.TABLE_NAME + "(" + TalentSchema.COLUMN_ID + ")"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_TALENT_ID, COLUMN_ROW_INDEX, COLUMN_PARAMETER_NAME, COLUMN_ENUM_NAME, COLUMN_INITIAL_VALUE,
			COLUMN_VALUE_PER};
}
