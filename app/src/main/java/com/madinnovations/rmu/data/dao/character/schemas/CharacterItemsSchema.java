/**
 * Copyright (C) 2016 MadInnovations
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
package com.madinnovations.rmu.data.dao.character.schemas;

import com.madinnovations.rmu.data.dao.item.schemas.ItemSchema;

/**
 * Database schema data for the character_items table
 */
public interface CharacterItemsSchema {
	String TABLE_NAME = "character_items";

	String COLUMN_CHARACTER_ID = "characterId";
	String COLUMN_ITEM_ID = "itemId";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "("
			+ COLUMN_CHARACTER_ID  + " INTEGER NOT NULL REFERENCES "
			+ CharacterSchema.TABLE_NAME + "(" + CharacterSchema.COLUMN_ID + "), "
			+ COLUMN_ITEM_ID + " INTEGER NOT NULL REFERENCES "
			+ ItemSchema.TABLE_NAME + "(" + ItemSchema.COLUMN_ID + "), "
			+ "PRIMARY KEY(" + COLUMN_CHARACTER_ID + "," + COLUMN_ITEM_ID + ")"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_CHARACTER_ID, COLUMN_ITEM_ID};
}
