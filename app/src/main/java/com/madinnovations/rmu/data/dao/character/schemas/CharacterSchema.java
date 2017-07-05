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
package com.madinnovations.rmu.data.dao.character.schemas;

import com.madinnovations.rmu.data.dao.campaign.schemas.CampaignSchema;

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
	String COLUMN_REALM                      = "realm";
	String COLUMN_REALM2                     = "realm2";
	String COLUMN_REALM3                     = "realm3";
	String COLUMN_HEIGHT                     = "height";
	String COLUMN_WEIGHT                     = "weight";
	String COLUMN_CURRENT_HP_LOSS            = "currentHPLoss";
	String COLUMN_CURRENT_DEVELOPMENT_POINTS = "currentDevelopmentPoints";
	String COLUMN_CURRENT_FATIGUE            = "currentFatigue";
	String COLUMN_CURRENT_PP_LOSS            = "currentPPLoss";
	String COLUMN_STAT_INCREASES             = "statIncreases";
	String COLUMN_MAIN_HAND_ITEM_ID          = "mainHandItem";
	String COLUMN_OFFHAND_ITEM_ID            = "offhandItem";
	String COLUMN_SHIRT_ITEM_ID              = "shirtItem";
	String COLUMN_PANTS_ITEM_ID              = "pantsItem";
	String COLUMN_HEAD_ITEM_ID               = "headItem";
	String COLUMN_CHEST_ITEM_ID              = "chestItem";
	String COLUMN_ARMS_ITEM_ID               = "armsItem";
	String COLUMN_LEGS_ITEM_ID               = "legsItem";
	String COLUMN_FEET_ITEM_ID               = "feetItem";
	String COLUMN_BACK_ITEM_ID               = "backItem";
	String COLUMN_BACKPACK_ITEM_ID           = "backpackItem";

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
			+ COLUMN_REALM  + " TEXT NOT NULL, "
			+ COLUMN_REALM2  + " TEXT, "
			+ COLUMN_REALM3  + " TEXT, "
			+ COLUMN_HEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_WEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_CURRENT_HP_LOSS + " INTEGER NOT NULL, "
			+ COLUMN_CURRENT_DEVELOPMENT_POINTS + " INTEGER NOT NULL, "
			+ COLUMN_CURRENT_FATIGUE + " INTEGER NOT NULL, "
			+ COLUMN_CURRENT_PP_LOSS + " INTEGER NOT NULL, "
			+ COLUMN_STAT_INCREASES + " INTEGER NOT NULL, "
			+ COLUMN_MAIN_HAND_ITEM_ID + " INTEGER, "
			+ COLUMN_OFFHAND_ITEM_ID + " INTEGER, "
			+ COLUMN_SHIRT_ITEM_ID + " INTEGER, "
			+ COLUMN_PANTS_ITEM_ID + " INTEGER, "
			+ COLUMN_HEAD_ITEM_ID + " INTEGER, "
			+ COLUMN_CHEST_ITEM_ID + " INTEGER, "
			+ COLUMN_ARMS_ITEM_ID + " INTEGER, "
			+ COLUMN_LEGS_ITEM_ID + " INTEGER, "
			+ COLUMN_FEET_ITEM_ID + " INTEGER, "
			+ COLUMN_BACK_ITEM_ID + " INTEGER, "
			+ COLUMN_BACKPACK_ITEM_ID + " INTEGER"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_ID, COLUMN_CAMPAIGN_ID, COLUMN_CURRENT_LEVEL, COLUMN_EXPERIENCE_POINTS,
			COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_KNOWN_AS, COLUMN_DESCRIPTION, COLUMN_HAIR_COLOR, COLUMN_HAIR_STYLE,
			COLUMN_EYE_COLOR, COLUMN_SKIN_COMPLEXION, COLUMN_FACIAL_FEATURES, COLUMN_IDENTIFYING_MARKS, COLUMN_PERSONALITY,
			COLUMN_MANNERISMS, COLUMN_HOMETOWN, COLUMN_FAMILY_INFO, COLUMN_RACE_ID, COLUMN_CULTURE_ID, COLUMN_PROFESSION_ID,
			COLUMN_REALM, COLUMN_REALM2, COLUMN_REALM3, COLUMN_HEIGHT, COLUMN_WEIGHT, COLUMN_CURRENT_HP_LOSS,
			COLUMN_CURRENT_DEVELOPMENT_POINTS, COLUMN_CURRENT_FATIGUE, COLUMN_CURRENT_PP_LOSS, COLUMN_STAT_INCREASES,
			COLUMN_MAIN_HAND_ITEM_ID, COLUMN_OFFHAND_ITEM_ID, COLUMN_SHIRT_ITEM_ID, COLUMN_PANTS_ITEM_ID, COLUMN_HEAD_ITEM_ID,
			COLUMN_CHEST_ITEM_ID, COLUMN_ARMS_ITEM_ID, COLUMN_LEGS_ITEM_ID, COLUMN_FEET_ITEM_ID, COLUMN_BACK_ITEM_ID,
			COLUMN_BACKPACK_ITEM_ID};
}
