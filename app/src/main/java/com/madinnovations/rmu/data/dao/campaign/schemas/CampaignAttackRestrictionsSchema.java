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
 * Database schema data for the campaign_attack_restrictions table
 */
public interface CampaignAttackRestrictionsSchema {
	String TABLE_NAME = "campaign_attack_restrictions";

	String COLUMN_CAMPAIGN_ID = "campaignId";
	String COLUMN_SPECIALIZATION_ID = "specialization_id";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "("
			+ COLUMN_CAMPAIGN_ID + " INTEGER NOT NULL, "
			+ COLUMN_SPECIALIZATION_ID + " INTEGER NOT NULL, "
			+ "PRIMARY KEY (" + COLUMN_CAMPAIGN_ID + "," + COLUMN_SPECIALIZATION_ID + ")"
			+ ")";

	String[] COLUMNS = new String[] { COLUMN_CAMPAIGN_ID, COLUMN_SPECIALIZATION_ID};
}
