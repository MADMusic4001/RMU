/**
 * Copyright (C) 2017 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.dao.common.schemas;

/**
 * Database schema data for the talent_parameter_per_unit table
 */
public interface TalentParametersPerUnitSchema {
	String TABLE_NAME = "talent_parameter_per_unit";

	String COLUMN_TALENT_ID           = "talentId";
	String COLUMN_PARAMETER_ROW_INDEX = "parameterRowIndex";
	String COLUMN_PER_UNIT_INDEX      = "perUnitIndex";
	String COLUMN_PER_VALUE           = "perValue";
	String COLUMN_UNIT_TYPE_NAME      = "unitTypeName";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_TALENT_ID + " INTEGER NOT NULL REFERENCES "
				+ TalentSchema.TABLE_NAME + "(" + TalentSchema.COLUMN_ID + "), "
			+ COLUMN_PARAMETER_ROW_INDEX + " INTEGER NOT NULL, "
			+ COLUMN_PER_UNIT_INDEX + " INTEGER NOT NULL, "
			+ COLUMN_PER_VALUE + " INTEGER NOT NULL, "
			+ COLUMN_UNIT_TYPE_NAME + " TEXT NOT NULL, "
			+ "PRIMARY KEY(" + COLUMN_TALENT_ID + "," + COLUMN_PARAMETER_ROW_INDEX + "," + COLUMN_PER_UNIT_INDEX + ")"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_TALENT_ID, COLUMN_PARAMETER_ROW_INDEX, COLUMN_PER_UNIT_INDEX, COLUMN_PER_VALUE,
			COLUMN_UNIT_TYPE_NAME};
}
