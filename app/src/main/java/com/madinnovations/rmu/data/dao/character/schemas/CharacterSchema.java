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
package com.madinnovations.rmu.data.dao.character.schemas;

import com.madinnovations.rmu.data.dao.spells.schemas.RealmSchema;

/**
 * Database schema data for the characters table
 */
public interface CharacterSchema {
	String TABLE_NAME = "characters";

	String COLUMN_ID = "id";
	String COLUMN_NAME = "name";
	String COLUMN_DESCRIPTION = "description";
	String COLUMN_HAIR_COLOR = "hairColor";
	String COLUMN_HAIR_STYLE = "hairStyle";
	String COLUMN_EYE_COLOR = "eyeColor";
	String COLUMN_SKIN_COMPLEXION = "skinComplexion";
	String COLUMN_FACIAL_FEATURES = "facialFeatures";
	String COLUMN_IDENTIFYING_MARKS = "identifyingMarks";
	String COLUMN_CLOTHING = "clothing";
	String COLUMN_PERSONALITY = "personality";
	String COLUMN_MANNERISMS = "mannerisms";
	String COLUMN_HOMETOWN = "hometown";
	String COLUMN_FAMILY_INFO = "familyInfo";
	String COLUMN_RACE_ID = "raceId";
	String COLUMN_CULTURE_ID = "cultureId";
	String COLUMN_PROFESSION_ID = "professionId";
	String COLUMN_REALM_ID = "realmId";
	String COLUMN_HEIGHT = "height";
	String COLUMN_WEIGHT = "weight";
	String COLUMN_STRIDE = "stride";
	String COLUMN_CURRENT_HITS = "currentHits";
	String COLUMN_MAX_HITS = "maxHits";
	String COLUMN_CURRENT_DEVELOPMENT_POINTS = "currentDevelopmentPoints";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_NAME  + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_HAIR_COLOR + " TEXT, "
			+ COLUMN_HAIR_STYLE + " TEXT, "
			+ COLUMN_EYE_COLOR + " TEXT, "
			+ COLUMN_SKIN_COMPLEXION + " TEXT, "
			+ COLUMN_FACIAL_FEATURES + " TEXT, "
			+ COLUMN_IDENTIFYING_MARKS + " TEXT, "
			+ COLUMN_CLOTHING + " TEXT, "
			+ COLUMN_PERSONALITY + " TEXT, "
			+ COLUMN_MANNERISMS + " TEXT, "
			+ COLUMN_HOMETOWN + " TEXT, "
			+ COLUMN_FAMILY_INFO + " TEXT, "
			+ COLUMN_RACE_ID  + " INTEGER NOT NULL, "
			+ COLUMN_CULTURE_ID  + " INTEGER NOT NULL, "
			+ COLUMN_PROFESSION_ID  + " INTEGER NOT NULL, "
			+ COLUMN_REALM_ID  + " INTEGER, "
			+ COLUMN_HEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_WEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_STRIDE + " INTEGER NOT NULL, "
			+ COLUMN_CURRENT_HITS + " INTEGER NOT NULL, "
			+ COLUMN_MAX_HITS + " INTEGER NOT NULL, "
			+ COLUMN_CURRENT_DEVELOPMENT_POINTS + " INTEGER NOT NULL, "
			+ "FOREIGN KEY (" + COLUMN_RACE_ID + ") REFERENCES " + RaceSchema.TABLE_NAME + "(" + RaceSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_CULTURE_ID + ") REFERENCES " + CultureSchema.TABLE_NAME + "(" + CultureSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_PROFESSION_ID + ") REFERENCES " + ProfessionSchema.TABLE_NAME + "(" + ProfessionSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_REALM_ID + ") REFERENCES " + RealmSchema.TABLE_NAME + "(" + RealmSchema.COLUMN_ID + ")"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_HAIR_COLOR, COLUMN_HAIR_STYLE,
			COLUMN_EYE_COLOR, COLUMN_SKIN_COMPLEXION, COLUMN_FACIAL_FEATURES, COLUMN_IDENTIFYING_MARKS, COLUMN_CLOTHING,
			COLUMN_PERSONALITY, COLUMN_MANNERISMS, COLUMN_HOMETOWN, COLUMN_FAMILY_INFO, COLUMN_RACE_ID, COLUMN_CULTURE_ID,
			COLUMN_PROFESSION_ID, COLUMN_REALM_ID, COLUMN_HEIGHT, COLUMN_WEIGHT, COLUMN_STRIDE, COLUMN_CURRENT_HITS,
			COLUMN_MAX_HITS, COLUMN_CURRENT_DEVELOPMENT_POINTS};
}
