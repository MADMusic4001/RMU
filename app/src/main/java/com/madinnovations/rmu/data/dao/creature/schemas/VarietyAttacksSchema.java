/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.dao.creature.schemas;

import com.madinnovations.rmu.data.dao.combat.schemas.AttackSchema;

/**
 * Database schema data for the variety_stats table
 */
public interface VarietyAttacksSchema {
	String TABLE_NAME = "variety_attacks";

	String COLUMN_VARIETY_ID = "varietyId";
	String COLUMN_ATTACK_ID = "attackId";
	String COLUMN_ATTACK_BONUS = "attackBonus";
	String COLUMN_IS_PRIMARY = "isPrimary";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_VARIETY_ID + " INTEGER NOT NULL REFERENCES "
				+ CreatureVarietySchema.TABLE_NAME + "(" + CreatureVarietySchema.COLUMN_ID + "), "
			+ COLUMN_ATTACK_ID + " INTEGER NOT NULL REFERENCES "
				+ AttackSchema.TABLE_NAME + "(" + AttackSchema.COLUMN_ID + "), "
			+ COLUMN_ATTACK_BONUS + " INTEGER NOT NULL, "
			+ COLUMN_IS_PRIMARY + " INTEGER NOT NUlL, "
			+ "PRIMARY KEY(" + COLUMN_VARIETY_ID + "," + COLUMN_ATTACK_ID + ")"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_VARIETY_ID, COLUMN_ATTACK_ID, COLUMN_ATTACK_BONUS, COLUMN_IS_PRIMARY};
}
