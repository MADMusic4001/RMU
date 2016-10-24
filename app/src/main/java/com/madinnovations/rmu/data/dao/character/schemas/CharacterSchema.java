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

import com.madinnovations.rmu.data.dao.campaign.schemas.CampaignSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.RealmSchema;

/**
 * Database schema data for the characters table
 */
public interface CharacterSchema {
	String TABLE_NAME = "characters";

	String COLUMN_ID                         = "id";
	String COLUMN_CAMPAIGN_ID                = "campaignId";
	String COLUMN_CURRENT_LEVEL              = "currentLevel";
	String COLUMN_EXPERIENCE_POINTS          = "experiencePoints";
	String COLUMN_FIRST_NAME                 = "firstName";
	String COLUMN_LAST_NAME                  = "lastName";
	String COLUMN_KNOWN_AS                   = "knownAs";
	String COLUMN_DESCRIPTION                = "description";
	String COLUMN_HAIR_COLOR                 = "hairColor";
	String COLUMN_HAIR_STYLE                 = "hairStyle";
	String COLUMN_EYE_COLOR                  = "eyeColor";
	String COLUMN_SKIN_COMPLEXION            = "skinComplexion";
	String COLUMN_FACIAL_FEATURES            = "facialFeatures";
	String COLUMN_IDENTIFYING_MARKS          = "identifyingMarks";
	String COLUMN_PERSONALITY                = "personality";
	String COLUMN_MANNERISMS                 = "mannerisms";
	String COLUMN_HOMETOWN                   = "hometown";
	String COLUMN_FAMILY_INFO                = "familyInfo";
	String COLUMN_RACE_ID                    = "raceId";
	String COLUMN_CULTURE_ID                 = "cultureId";
	String COLUMN_PROFESSION_ID              = "professionId";
	String COLUMN_REALM_ID                   = "realmId";
	String COLUMN_HEIGHT                     = "height";
	String COLUMN_WEIGHT                     = "weight";
	String COLUMN_CURRENT_HP_LOSS            = "currentHPLoss";
	String COLUMN_CURRENT_DEVELOPMENT_POINTS = "currentDevelopmentPoints";
	String COLUMN_CURRENT_ENDURANCE_LOSS     = "currentEnduranceLoss";
	String COLUMN_CURRENT_PP_LOSS            = "currentPPLoss";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_CAMPAIGN_ID + " INTEGER NOT NULL REFERENCES "
				+ CampaignSchema.TABLE_NAME + "(" + CampaignSchema.COLUMN_ID + "), "
			+ COLUMN_CURRENT_LEVEL + " INTEGER NOT NULL, "
			+ COLUMN_EXPERIENCE_POINTS + " INTEGER NOT NULL, "
			+ COLUMN_FIRST_NAME + " TEXT NOT NULL, "
			+ COLUMN_LAST_NAME + " TEXT NOT NULL, "
			+ COLUMN_KNOWN_AS + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_HAIR_COLOR + " TEXT, "
			+ COLUMN_HAIR_STYLE + " TEXT, "
			+ COLUMN_EYE_COLOR + " TEXT, "
			+ COLUMN_SKIN_COMPLEXION + " TEXT, "
			+ COLUMN_FACIAL_FEATURES + " TEXT, "
			+ COLUMN_IDENTIFYING_MARKS + " TEXT, "
			+ COLUMN_PERSONALITY + " TEXT, "
			+ COLUMN_MANNERISMS + " TEXT, "
			+ COLUMN_HOMETOWN + " TEXT, "
			+ COLUMN_FAMILY_INFO + " TEXT, "
			+ COLUMN_RACE_ID  + " INTEGER NOT NULL REFERENCES "
				+ RaceSchema.TABLE_NAME + "(" + RaceSchema.COLUMN_ID + "), "
			+ COLUMN_CULTURE_ID  + " INTEGER NOT NULL REFERENCES "
				+ CultureSchema.TABLE_NAME + "(" + CultureSchema.COLUMN_ID + "), "
			+ COLUMN_PROFESSION_ID  + " INTEGER NOT NULL REFERENCES "
				+ ProfessionSchema.TABLE_NAME + "(" + ProfessionSchema.COLUMN_ID + "), "
			+ COLUMN_REALM_ID  + " INTEGER REFERENCES "
				+ RealmSchema.TABLE_NAME + "(" + RealmSchema.COLUMN_ID + "), "
			+ COLUMN_HEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_WEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_CURRENT_HP_LOSS + " INTEGER NOT NULL, "
			+ COLUMN_CURRENT_DEVELOPMENT_POINTS + " INTEGER NOT NULL, "
			+ COLUMN_CURRENT_ENDURANCE_LOSS + " INTEGER NOT NULL, "
			+ COLUMN_CURRENT_PP_LOSS + " INTEGER NOT NULL"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_ID, COLUMN_CAMPAIGN_ID, COLUMN_CURRENT_LEVEL, COLUMN_EXPERIENCE_POINTS, COLUMN_FIRST_NAME,
			COLUMN_LAST_NAME, COLUMN_KNOWN_AS, COLUMN_DESCRIPTION, COLUMN_HAIR_COLOR, COLUMN_HAIR_STYLE, COLUMN_EYE_COLOR,
			COLUMN_SKIN_COMPLEXION, COLUMN_FACIAL_FEATURES, COLUMN_IDENTIFYING_MARKS, COLUMN_PERSONALITY, COLUMN_MANNERISMS,
			COLUMN_HOMETOWN, COLUMN_FAMILY_INFO, COLUMN_RACE_ID, COLUMN_CULTURE_ID, COLUMN_PROFESSION_ID, COLUMN_REALM_ID, COLUMN_HEIGHT,
			COLUMN_WEIGHT, COLUMN_CURRENT_HP_LOSS, COLUMN_CURRENT_DEVELOPMENT_POINTS, COLUMN_CURRENT_ENDURANCE_LOSS,
			COLUMN_CURRENT_PP_LOSS};
}
