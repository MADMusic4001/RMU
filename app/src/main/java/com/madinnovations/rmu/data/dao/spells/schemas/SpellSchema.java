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
package com.madinnovations.rmu.data.dao.spells.schemas;

/**
 * Database schema data for the spells table
 */
public interface SpellSchema {
	String TABLE_NAME = "spells";

	String COLUMN_ID = "id";
	String COLUMN_SPELL_LIST_ID = "spellListId";
	String COLUMN_NAME = "name";
	String COLUMN_DESCRIPTION = "description";
	String COLUMN_SPELL_TYPE_ID = "spellTypeId";
	String COLUMN_SPELL_SUB_TYPE_ID = "spellSubTypeId";
	String COLUMN_AREA_OF_EFFECT = "areaOfEffect";
	String COLUMN_DURATION = "duration";
	String COLUMN_RANGE = "range";
	String COLUMN_RANGE_PARAM = "rangeParam";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_SPELL_LIST_ID + " INTEGER NOT NULL, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_SPELL_TYPE_ID + " INTEGER NOT NULL, "
			+ COLUMN_SPELL_SUB_TYPE_ID + " INTEGER, "
			+ COLUMN_AREA_OF_EFFECT + " TEXT NOT NULL, "
			+ COLUMN_DURATION + " TEXT NOT NULL, "
			+ COLUMN_RANGE + " TEXT NOT NULL, "
			+ COLUMN_RANGE_PARAM + " INTEGER, "
			+ "FOREIGN KEY (" + COLUMN_SPELL_LIST_ID + ") REFERENCES " + SpellListSchema.TABLE_NAME
			+ "(" + SpellListSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_SPELL_TYPE_ID + ") REFERENCES " + SpellTypeSchema.TABLE_NAME
			+ "(" + SpellTypeSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_SPELL_SUB_TYPE_ID + ") REFERENCES " + SpellSubTypeSchema.TABLE_NAME
			+ "(" + SpellSubTypeSchema.COLUMN_ID + ")"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_ID, COLUMN_SPELL_LIST_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_SPELL_TYPE_ID,
			COLUMN_SPELL_SUB_TYPE_ID, COLUMN_AREA_OF_EFFECT, COLUMN_DURATION, COLUMN_RANGE, COLUMN_RANGE_PARAM};
}
