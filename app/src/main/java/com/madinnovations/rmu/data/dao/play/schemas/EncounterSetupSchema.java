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
package com.madinnovations.rmu.data.dao.play.schemas;

import com.madinnovations.rmu.data.dao.campaign.schemas.CampaignSchema;

/**
 * Database schema data for the combat_setups table
 */
public interface EncounterSetupSchema {
	String TABLE_NAME = "encounter_setups";

	String COLUMN_ID                   = "id";
	String COLUMN_CAMPAIGN_ID          = "campaignId";
	String COLUMN_CURRENT_INITIATIVE   = "currentInitiative";
	String COLUMN_ENCOUNTER_START_TIME = "encounterStartTime";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_CAMPAIGN_ID + " INTEGER NOT NULL REFERENCES "
				+ CampaignSchema.TABLE_NAME + "(" + CampaignSchema.COLUMN_ID + "), "
			+ COLUMN_CURRENT_INITIATIVE + " INTEGER NOT NULL, "
			+ COLUMN_ENCOUNTER_START_TIME + " INTEGER NOT NULL"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_ID, COLUMN_CAMPAIGN_ID, COLUMN_CURRENT_INITIATIVE, COLUMN_ENCOUNTER_START_TIME};
}
