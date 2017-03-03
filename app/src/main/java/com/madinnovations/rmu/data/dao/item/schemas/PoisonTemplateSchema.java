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
package com.madinnovations.rmu.data.dao.item.schemas;

/**
 * Database schema data for the poison_templates table
 */
public interface PoisonTemplateSchema {
	String TABLE_NAME = "poison_templates";

	String COLUMN_NATURALS_TEMPLATE_ID = "naturalsTemplateId";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_NATURALS_TEMPLATE_ID + " INTEGER NOT NULL PRIMARY KEY REFERENCES "
				+ NaturalsTemplateSchema.TABLE_NAME + "(" + NaturalsTemplateSchema.COLUMN_ITEM_TEMPLATE_ID + ")"
			+ ")";

	String[] COLUMNS = new String[] { COLUMN_NATURALS_TEMPLATE_ID};
}
