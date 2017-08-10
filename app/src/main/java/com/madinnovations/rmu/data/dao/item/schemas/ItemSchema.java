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
package com.madinnovations.rmu.data.dao.item.schemas;

import com.madinnovations.rmu.data.dao.campaign.schemas.CampaignSchema;

/**
 * Database schema data for the items table
 */
public interface ItemSchema {
    String TABLE_NAME = "items";

    String COLUMN_ID = "id";
	String COLUMN_CAMPAIGN_ID = "campaignId";
    String COLUMN_ITEM_TEMPLATE_ID = "itemTemplateId";
    String COLUMN_NAME = "name";
	String COLUMN_HISTORY = "history";
	String COLUMN_SIZE = "size";
	String COLUMN_LEVEL = "level";

    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_CAMPAIGN_ID + " INTEGER NOT NULL REFERENCES "
				+ CampaignSchema.TABLE_NAME + "(" + CampaignSchema.COLUMN_ID + "), "
            + COLUMN_ITEM_TEMPLATE_ID  + " INTEGER NOT NULL REFERENCES "
            	+ ItemTemplateSchema.TABLE_NAME + "(" + ItemTemplateSchema.COLUMN_ID + "), "
			+ COLUMN_NAME + " TEXT, "
			+ COLUMN_HISTORY + " TEXT, "
			+ COLUMN_SIZE + " TEXT NOT NULL, "
			+ COLUMN_LEVEL + " INTEGER NOT NULL"
            + ")";

	String SELECT_FROM = "SELECT"
		+ " ITEM." + COLUMN_ID
		+ ", ITEM." + COLUMN_CAMPAIGN_ID
		+ ", ITEM." + COLUMN_ITEM_TEMPLATE_ID
		+ ", ITEM." + COLUMN_NAME
		+ ", ITEM." + COLUMN_HISTORY
		+ ", ITEM." + COLUMN_SIZE
		+ ", ITEM." + COLUMN_LEVEL
		+ ", WEAPON." + WeaponSchema.COLUMN_ITEM_ID + " AS " + WeaponSchema.WEAPON_ID
		+ ", WEAPON." + WeaponSchema.COLUMN_BONUS
		+ ", WEAPON." + WeaponSchema.COLUMN_TWO_HANDED
		+ " FROM " + TABLE_NAME + " ITEM"
			+ " LEFT OUTER JOIN " + WeaponSchema.TABLE_NAME + " WEAPON"
			+ " ON WEAPON." + WeaponSchema.COLUMN_ITEM_ID+ " = ITEM." + ItemSchema.COLUMN_ID;

	String QUERY_ALL = SELECT_FROM;
	String QUERY_BY_ID = SELECT_FROM + " WHERE ITEM." + COLUMN_ID + " = ?";
	String QUERY_BY_CAMPAIGN = SELECT_FROM + " WHERE ITEM." + COLUMN_CAMPAIGN_ID + " = ?";
	String QUERY_BY_SLOT = SELECT_FROM + " WHERE EXISTS(SELECT NULL FROM " + ItemTemplateSchema.TABLE_NAME + " a WHERE a."
			+ ItemTemplateSchema.COLUMN_ID + " = " + COLUMN_ITEM_TEMPLATE_ID + " AND a."
			+ ItemTemplateSchema.COLUMN_PRIMARY_SLOT + " = ? OR a." + ItemTemplateSchema.COLUMN_SECONDARY_SLOT
			+ " = ? OR a." + ItemTemplateSchema.COLUMN_PRIMARY_SLOT + " = ?)";
	String QUERY_NO_SUBCLASS = SELECT_FROM + " WHERE WEAPON." + WeaponSchema.COLUMN_ITEM_ID + " IS NULL";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_CAMPAIGN_ID, COLUMN_ITEM_TEMPLATE_ID, COLUMN_NAME, COLUMN_HISTORY,
			COLUMN_SIZE, COLUMN_LEVEL};
}
