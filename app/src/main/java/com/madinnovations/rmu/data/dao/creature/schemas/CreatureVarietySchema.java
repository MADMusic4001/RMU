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

import com.madinnovations.rmu.data.dao.common.schemas.SizeSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.RealmSchema;

/**
 * Database schema data for the creature_varieties table
 */
public interface CreatureVarietySchema {
	String TABLE_NAME = "creature_varieties";

	String COLUMN_ID = "id";
	String COLUMN_TYPE_ID = "typeId";
	String COLUMN_NAME = "name";
	String COLUMN_DESCRIPTION = "description";
	String COLUMN_TYPICAL_LEVEL = "typicalLevel";
	String COLUMN_LEVEL_SPREAD = "levelSpread";
	String COLUMN_HEIGHT = "height";
	String COLUMN_LENGTH = "length";
	String COLUMN_WEIGHT = "weight";
	String COLUMN_HEALING_RATE = "healingRate";
	String COLUMN_BASE_HITS = "baseHits";
	String COLUMN_BASE_ENDURANCE = "baseEndurance";
	String COLUMN_SIZE_ID = "sizeId";
	String COLUMN_ARMOR_TYPE = "armorType";
	String COLUMN_BASE_MOVEMENT_RATE = "baseMovementRate";
	String COLUMN_BASE_CHANNELING_RR = "baseChannelingRR";
	String COLUMN_BASE_ESSENCE_RR = "baseEssenceRR";
	String COLUMN_BASE_MENTALISM_RR = "baseMentalismRR";
	String COLUMN_BASE_PHYSICAL_RR = "basePhysicalRR";
	String COLUMN_BASE_FEAR_RR = "baseFearRR";
	String COLUMN_REALM1_ID = "realm1Id";
	String COLUMN_REALM2_ID = "realm2Id";
	String COLUMN_BASE_STRIDE = "baseStride";
	String COLUMN_LEFTOVER_DP = "leftoverDP";
	String COLUMN_OUTLOOK_ID = "outlookId";
	String COLUMN_CRITICAL_SIZE_MODIFIER_ID = "criticalSizeModifierId";
	String COLUMN_ATTACK_SEQUENCE = "attackSequence";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_TYPE_ID + " INTEGER NOT NULL REFERENCES "
				+ CreatureTypeSchema.TABLE_NAME + "(" + CreatureTypeSchema.COLUMN_ID + "), "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_TYPICAL_LEVEL + " INTEGER NOT NULL, "
			+ COLUMN_LEVEL_SPREAD + " INTEGER NOT NULL, "
			+ COLUMN_HEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_LENGTH + " INTEGER NOT NULL, "
			+ COLUMN_WEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_HEALING_RATE + " INTEGER NOT NULL, "
			+ COLUMN_BASE_HITS + " INTEGER NOT NULL, "
			+ COLUMN_BASE_ENDURANCE + " INTEGER NOT NULL, "
			+ COLUMN_SIZE_ID + " INTEGER NOT NULL REFERENCES "
				+ SizeSchema.TABLE_NAME + "(" + SizeSchema.COLUMN_ID + "), "
			+ COLUMN_ARMOR_TYPE + " INTEGER NOT NULL, "
			+ COLUMN_BASE_MOVEMENT_RATE + " INTEGER NOT NULL, "
			+ COLUMN_BASE_CHANNELING_RR + " INTEGER NOT NULL, "
			+ COLUMN_BASE_ESSENCE_RR + " INTEGER NOT NULL, "
			+ COLUMN_BASE_MENTALISM_RR + " INTEGER NOT NULL, "
			+ COLUMN_BASE_PHYSICAL_RR + " INTEGER NOT NULL, "
			+ COLUMN_BASE_FEAR_RR + " INTEGER NOT NULL, "
			+ COLUMN_REALM1_ID + " INTEGER NOT NULL REFERENCES "
				+ RealmSchema.TABLE_NAME + "(" + RealmSchema.COLUMN_ID + "), "
			+ COLUMN_REALM2_ID + " INTEGER REFERENCES "
				+ RealmSchema.TABLE_NAME + "(" + RealmSchema.COLUMN_ID + "), "
			+ COLUMN_BASE_STRIDE + " INTEGER NOT NULL, "
			+ COLUMN_LEFTOVER_DP + " INTEGER NOT NULL, "
			+ COLUMN_OUTLOOK_ID + " INTEGER NOT NULL REFERENCES "
				+ OutlookSchema.TABLE_NAME + "(" + OutlookSchema.COLUMN_ID + "), "
			+ COLUMN_CRITICAL_SIZE_MODIFIER_ID + " INTEGER REFERENCES "
				+ SizeSchema.TABLE_NAME + "(" + SizeSchema.COLUMN_ID + "), "
			+ COLUMN_ATTACK_SEQUENCE + " TEXT NOT NULL"
			+ ");";

	String DROP_TABLE_V1 = "DROP TABLE " + TABLE_NAME + ";";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_TYPE_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_TYPICAL_LEVEL,
			COLUMN_LEVEL_SPREAD, COLUMN_HEIGHT, COLUMN_LENGTH, COLUMN_WEIGHT, COLUMN_HEALING_RATE, COLUMN_BASE_HITS,
			COLUMN_BASE_ENDURANCE, COLUMN_SIZE_ID, COLUMN_ARMOR_TYPE, COLUMN_BASE_MOVEMENT_RATE, COLUMN_BASE_CHANNELING_RR,
			COLUMN_BASE_ESSENCE_RR, COLUMN_BASE_MENTALISM_RR, COLUMN_BASE_PHYSICAL_RR, COLUMN_BASE_FEAR_RR, COLUMN_REALM1_ID,
			COLUMN_REALM2_ID, COLUMN_BASE_STRIDE, COLUMN_LEFTOVER_DP, COLUMN_OUTLOOK_ID, COLUMN_CRITICAL_SIZE_MODIFIER_ID,
			COLUMN_ATTACK_SEQUENCE};
}
