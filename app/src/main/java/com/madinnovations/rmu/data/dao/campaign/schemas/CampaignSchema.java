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
package com.madinnovations.rmu.data.dao.campaign.schemas;

/**
 * Database schema data for the campaigns table
 */
public interface CampaignSchema {
	String TABLE_NAME = "campaigns";

	String COLUMN_ID = "id";
	String COLUMN_NAME = "name";
	String COLUMN_CREATE_DATE = "createDate";
	String COLUMN_POWER_LEVEL = "powerLevel";
	String COLUMN_AWARD_DP = "awardDP";
	String COLUMN_INTENSE_TRAINING_ALLOWED = "intenseTrainingAllowed";
	String COLUMN_INDIVIDUAL_STRIDE = "individualStride";
	String COLUMN_NO_PROFESSIONS = "noProfessions";
	String COLUMN_BUY_STATS = "buyStats";
	String COLUMN_ALLOW_TALENTS = "allowTalentsBeyondFirst";
	String COLUMN_OPEN_ROUNDS = "openRounds";
	String COLUMN_GRITTY_POISON_AND_DISEASE = "grittyPoisonAndDisease";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "("
			+ COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_CREATE_DATE + " INTEGER NOT NULL, "
			+ COLUMN_POWER_LEVEL + " TEXT NOT NULL, "
			+ COLUMN_AWARD_DP + " INTEGER NOT NULL, "
			+ COLUMN_INTENSE_TRAINING_ALLOWED + " INTEGER NOT NULL, "
			+ COLUMN_INDIVIDUAL_STRIDE + " INTEGER NOT NULL, "
			+ COLUMN_NO_PROFESSIONS + " INTEGER NOT NULL, "
			+ COLUMN_BUY_STATS + " INTEGER NOT NULL, "
			+ COLUMN_ALLOW_TALENTS + " INTEGER NOT NULL, "
			+ COLUMN_OPEN_ROUNDS + " INTEGER NOT NULL, "
			+ COLUMN_GRITTY_POISON_AND_DISEASE + " INTEGER NOT NULL"
			+ ")";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_CREATE_DATE, COLUMN_POWER_LEVEL, COLUMN_AWARD_DP,
			COLUMN_INTENSE_TRAINING_ALLOWED, COLUMN_INDIVIDUAL_STRIDE, COLUMN_NO_PROFESSIONS, COLUMN_BUY_STATS,
			COLUMN_ALLOW_TALENTS, COLUMN_OPEN_ROUNDS, COLUMN_GRITTY_POISON_AND_DISEASE};
}
