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
 * Database schema data for the talent_parameters table
 */
public interface TalentParametersSchema {
	String TABLE_NAME = "talent_parameters";

	String COLUMN_TALENT_ID = "talentId";
	String COLUMN_PARAMETER_ID = "parameterId";
	String COLUMN_VALUE = "value";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_TALENT_ID + " INTEGER NOT NULL, "
			+ COLUMN_PARAMETER_ID + " INTEGER NOT NULL, "
			+ COLUMN_VALUE + " TEXT, "
			+ "PRIMARY KEY(" + COLUMN_TALENT_ID + "," + COLUMN_PARAMETER_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_TALENT_ID + ") REFERENCES " + TalentSchema.TABLE_NAME + "(" + TalentSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_PARAMETER_ID + ") REFERENCES " + ParameterSchema.TABLE_NAME + "(" + ParameterSchema.COLUMN_ID + ")"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_TALENT_ID, COLUMN_PARAMETER_ID, COLUMN_VALUE};
}
