/*
  Copyright (C) 2017 MadInnovations
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.dao.item.schemas;

/**
 * Database schema data for the weapons table
 */
public interface WeaponSchema {
	String TABLE_NAME = "weapons";
	String WEAPON_ID = "weaponId";

	String COLUMN_ITEM_ID    = "itemId";
	String COLUMN_BONUS      = "bonus";
	String COLUMN_TWO_HANDED = "twoHanded";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ITEM_ID + " INTEGER PRIMARY KEY REFERENCES "
				+ ItemSchema.TABLE_NAME + "(" + ItemSchema.COLUMN_ID + "), "
			+ COLUMN_BONUS + " INTEGER NOT NULL, "
			+ COLUMN_TWO_HANDED + " INTEGER NOT NULL"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_ITEM_ID, COLUMN_BONUS, COLUMN_TWO_HANDED};
}
