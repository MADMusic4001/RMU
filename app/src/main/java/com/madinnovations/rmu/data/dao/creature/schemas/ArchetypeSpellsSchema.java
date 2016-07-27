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

/**
 * Database schema data for the archetype_spells table
 */
public interface ArchetypeSpellsSchema {
	public static final String TABLE_NAME = "archetype_spells";

	public static final String COLUMN_ARCHETYPE_ID = "archetypeId";
	public static final String COLUMN_SPELL_ID = "spellId";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ARCHETYPE_ID + " INTEGER NOT NULL, "
			+ COLUMN_SPELL_ID + " INTEGER NOT NULL, "
			+ "PRIMARY KEY(" + COLUMN_ARCHETYPE_ID + "," + COLUMN_SPELL_ID + ")"
			+ ")";

	public static final String[] COLUMNS = new String[] {COLUMN_ARCHETYPE_ID, COLUMN_SPELL_ID};
}
