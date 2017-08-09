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

import com.madinnovations.rmu.data.dao.common.schemas.BiomeSchema;

/**
 * Database schema data for the naturals_templates table
 */
public interface NaturalsTemplateSchema {
	String TABLE_NAME = "naturals_templates";
	String NATURAL_ID = "naturalId";

	String COLUMN_ITEM_TEMPLATE_ID = "itemTemplateId";
	String COLUMN_BIOME_ID         = "biomeId";
	String COLUMN_FORM_NAME        = "formName";
	String COLUMN_PREP_NAME        = "prepName";
	String COLUMN_SEASON           = "season";
	String COLUMN_EFFECTS          = "effects";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ITEM_TEMPLATE_ID + " INTEGER NOT NULL PRIMARY KEY REFERENCES "
				+ ItemTemplateSchema.TABLE_NAME + "(" + ItemTemplateSchema.COLUMN_ID + "), "
			+ COLUMN_BIOME_ID + " INTEGER NOT NULL REFERENCES "
				+ BiomeSchema.TABLE_NAME + "(" + BiomeSchema.COLUMN_ID + "), "
			+ COLUMN_FORM_NAME + " TEXT NOT NULL, "
			+ COLUMN_PREP_NAME + " TEXT NOT NULL, "
			+ COLUMN_SEASON + " TEXT, "
			+ COLUMN_EFFECTS + " TEXT NOT NULL"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_ITEM_TEMPLATE_ID, COLUMN_BIOME_ID, COLUMN_FORM_NAME, COLUMN_PREP_NAME,
			COLUMN_SEASON, COLUMN_EFFECTS};
}
