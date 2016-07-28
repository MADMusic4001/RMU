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
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 7/27/2016.
 */
public interface SkillCostSchema {
	public static final String TABLE_NAME = "skill_costs";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_SKILL_COST = "initialCost";
	public static final String COLUMN_ADDITIONAL_COST = "additionalCost";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_SKILL_COST + " INTEGER NOT NULL, "
			+ COLUMN_ADDITIONAL_COST + " INTEGER NOT NULL"
			+ ")";

	public static final String[] COLUMNS = new String[] { COLUMN_ID,
			COLUMN_SKILL_COST, COLUMN_ADDITIONAL_COST};
}
