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

import com.madinnovations.rmu.data.dao.campaign.schemas.CampaignSchema;

/**
 * Database schema data for the creatures table
 */
public interface CreatureSchema {
	String TABLE_NAME = "creatures";

	String COLUMN_ID = "id";
	String COLUMN_CAMPAIGN_ID = "campaignId";
	String COLUMN_CREATURE_VARIETY_ID = "creatureVarietyId";
	String COLUMN_LEVEL = "level";
	String COLUMN_MAX_HITS = "maxHits";
	String COLUMN_CURRENT_HITS = "currentHits";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_CAMPAIGN_ID + " INTEGER NOT NULL REFERENCES "
				+ CampaignSchema.TABLE_NAME + "(" + CampaignSchema.COLUMN_ID + "), "
			+ COLUMN_CREATURE_VARIETY_ID + " INTEGER NOT NULL REFERENCES "
				+ CreatureVarietySchema.TABLE_NAME + "(" + CreatureVarietySchema.COLUMN_ID + "), "
			+ COLUMN_LEVEL + " INTEGER NOT NULL, "
			+ COLUMN_MAX_HITS + " INTEGER NOT NULL, "
			+ COLUMN_CURRENT_HITS + " INTEGER NOT NULL"
			+ ")";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_CAMPAIGN_ID, COLUMN_CREATURE_VARIETY_ID, COLUMN_LEVEL, COLUMN_MAX_HITS,
			COLUMN_CURRENT_HITS};
}
