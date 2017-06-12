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
package com.madinnovations.rmu.data.dao.item.schemas;

/**
 * Database schema data for the item_templates table
 */
public interface ItemTemplateSchema {
	String TABLE_NAME = "item_templates";

	String COLUMN_ID                  = "id";
	String COLUMN_NAME                = "name";
	String COLUMN_WEIGHT              = "weight";
	String COLUMN_BASE_COST           = "baseCost";
	String COLUMN_STRENGTH            = "strength";
	String COLUMN_CONSTRUCTION_TIME   = "constructionTime";
	String COLUMN_MANEUVER_DIFFICULTY = "maneuverDifficulty";
	String COLUMN_NOTES               = "notes";
	String COLUMN_PRIMARY_SLOT        = "primarySlot";
	String COLUMN_SECONDARY_SLOT      = "secondarySlot";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_NAME  + " TEXT NOT NULL, "
			+ COLUMN_WEIGHT + " REAL NOT NULL, "
			+ COLUMN_BASE_COST + " REAL NOT NULL, "
			+ COLUMN_STRENGTH + " INTEGER NOT NULL, "
			+ COLUMN_CONSTRUCTION_TIME + " REAL NOT NULL, "
			+ COLUMN_MANEUVER_DIFFICULTY + " TEXT, "
			+ COLUMN_NOTES + " TEXT, "
			+ COLUMN_PRIMARY_SLOT + " TEXT, "
			+ COLUMN_SECONDARY_SLOT + " TEXT"
			+ ")";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_WEIGHT, COLUMN_BASE_COST, COLUMN_STRENGTH,
			COLUMN_CONSTRUCTION_TIME, COLUMN_MANEUVER_DIFFICULTY, COLUMN_NOTES, COLUMN_PRIMARY_SLOT, COLUMN_SECONDARY_SLOT};
}
