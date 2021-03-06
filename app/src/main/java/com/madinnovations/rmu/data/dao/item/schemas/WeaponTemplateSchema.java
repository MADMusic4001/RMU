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

import com.madinnovations.rmu.data.dao.combat.schemas.DamageTableSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SpecializationSchema;

/**
 * Database schema data for the weapons table
 */
public interface WeaponTemplateSchema {
    String TABLE_NAME = "weapon_templates";
	String WEAPON_ID = "weaponId";

	String COLUMN_ITEM_TEMPLATE_ID = "itemTemplateId";
    String COLUMN_SPECIALIZATION_ID = "specializationId";
    String COLUMN_DAMAGE_TABLE_ID = "damageTableId";
	String COLUMN_BRACEABLE = "braceable";
	String COLUMN_FUMBLE = "fumble";
	String COLUMN_LENGTH = "length";
	String COLUMN_SIZE_ADJUSTMENT = "sizeAdjustment";
	String COLUMN_ATTACK_ID = "attackId";

    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ITEM_TEMPLATE_ID + " INTEGER PRIMARY KEY REFERENCES "
                + ItemTemplateSchema.TABLE_NAME + "(" + ItemTemplateSchema.COLUMN_ID + "), "
            + COLUMN_SPECIALIZATION_ID + " INTEGER NOT NULL REFERENCES "
				+ SpecializationSchema.TABLE_NAME + "(" + SpecializationSchema.COLUMN_ID + "), "
            + COLUMN_DAMAGE_TABLE_ID + " INTEGER NOT NULL REFERENCES "
				+ DamageTableSchema.TABLE_NAME + "(" + DamageTableSchema.COLUMN_ID + "), "
			+ COLUMN_BRACEABLE + " INTEGER NOT NULL, "
			+ COLUMN_FUMBLE + " INTEGER NOT NULL, "
			+ COLUMN_LENGTH + " REAL NOT NULL, "
			+ COLUMN_SIZE_ADJUSTMENT + " INTEGER DEFAULT NULL, "
			+ COLUMN_ATTACK_ID + " INTEGER NOT NULL"
			+ ")";

    String[] COLUMNS = new String[]{COLUMN_ITEM_TEMPLATE_ID, COLUMN_SPECIALIZATION_ID, COLUMN_DAMAGE_TABLE_ID, COLUMN_BRACEABLE,
			COLUMN_FUMBLE, COLUMN_LENGTH, COLUMN_SIZE_ADJUSTMENT, COLUMN_ATTACK_ID};
}
