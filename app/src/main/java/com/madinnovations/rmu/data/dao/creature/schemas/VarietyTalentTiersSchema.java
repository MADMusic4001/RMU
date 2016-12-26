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
package com.madinnovations.rmu.data.dao.creature.schemas;

import com.madinnovations.rmu.data.dao.common.schemas.TalentSchema;

/**
 * Database schema data for the variety_talents table
 */
public interface VarietyTalentTiersSchema {
	String TABLE_NAME = "variety_talents";

	String COLUMN_ID = "id";
	String COLUMN_VARIETY_ID = "varietyId";
	String COLUMN_TALENT_ID = "talentId";
	String COLUMN_TIERS = "tiers";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_VARIETY_ID  + " INTEGER NOT NULL REFERENCES "
				+ CreatureVarietySchema.TABLE_NAME + "(" + CreatureVarietySchema.COLUMN_ID + "), "
			+ COLUMN_TALENT_ID + " INTEGER NOT NULL REFERENCES "
				+ TalentSchema.TABLE_NAME + "(" + TalentSchema.COLUMN_ID + "), "
			+ COLUMN_TIERS + " INTEGER NOT NULL"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_ID, COLUMN_VARIETY_ID, COLUMN_TALENT_ID, COLUMN_TIERS};
}
