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
	public static final String TABLE_NAME = "creature_varieties";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_TYPE_ID = "typeId";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_TYPICAL_LEVEL = "typicalLevel";
	public static final String COLUMN_LEVEL_SPREAD = "levelSpread";
	public static final String COLUMN_HEIGHT = "height";
	public static final String COLUMN_LENGTH = "length";
	public static final String COLUMN_WEIGHT = "weight";
	public static final String COLUMN_HEALING_RATE = "healingRate";
	public static final String COLUMN_BASE_HITS = "baseHits";
	public static final String COLUMN_BASE_ENDURANCE = "baseEndurance";
	public static final String COLUMN_SIZE_ID = "sizeId";
	public static final String COLUMN_ARMOR_TYPE = "armorType";
	public static final String COLUMN_BASE_MOVEMENT_RATE = "baseMovementRate";
	public static final String COLUMN_BASE_CHANNELING_RR = "baseChannelingRR";
	public static final String COLUMN_BASE_ESSENCE_RR = "baseEssenceRR";
	public static final String COLUMN_BASE_MENTALISM_RR = "baseMentalismRR";
	public static final String COLUMN_BASE_PHYSICAL_RR = "basePhysicalRR";
	public static final String COLUMN_BASE_FEAR_RR = "baseFearRR";
	public static final String COLUMN_REALM1_ID = "realm1Id";
	public static final String COLUMN_REALM2_ID = "realm2Id";
	public static final String COLUMN_BASE_STRIDE = "baseStride";
	public static final String COLUMN_LEFTOVER_DP = "leftoverDP";
	public static final String COLUMN_OUTLOOK_ID = "outlookId";
	public static final String COLUMN_ATTACK_SEQUENCE = "attackSequence";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_TYPE_ID + " INTEGER NOT NULL, "
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
			+ COLUMN_SIZE_ID + " INTEGER NOT NULL, "
			+ COLUMN_ARMOR_TYPE + " INTEGER NOT NULL, "
			+ COLUMN_BASE_MOVEMENT_RATE + " INTEGER NOT NULL, "
			+ COLUMN_BASE_CHANNELING_RR + " INTEGER NOT NULL, "
			+ COLUMN_BASE_ESSENCE_RR + " INTEGER NOT NULL, "
			+ COLUMN_BASE_MENTALISM_RR + " INTEGER NOT NULL, "
			+ COLUMN_BASE_PHYSICAL_RR + " INTEGER NOT NULL, "
			+ COLUMN_BASE_FEAR_RR + " INTEGER NOT NULL, "
			+ COLUMN_REALM1_ID + " INTEGER NOT NULL, "
			+ COLUMN_REALM2_ID + " INTEGER, "
			+ COLUMN_BASE_STRIDE + " INTEGER NOT NULL, "
			+ COLUMN_LEFTOVER_DP + " INTEGER NOT NULL, "
			+ COLUMN_OUTLOOK_ID + " INTEGER NOT NULL, "
			+ COLUMN_ATTACK_SEQUENCE + " TEXT NOT NULL, "
			+ "FOREIGN KEY (" + COLUMN_TYPE_ID + ") REFERENCES " + CreatureTypeSchema.TABLE_NAME + "(" + CreatureTypeSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_SIZE_ID + ") REFERENCES " + SizeSchema.TABLE_NAME + "(" + SizeSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_REALM1_ID + ") REFERENCES " + RealmSchema.TABLE_NAME + "(" + RealmSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_REALM2_ID + ") REFERENCES " + RealmSchema.TABLE_NAME + "(" + RealmSchema.COLUMN_ID + "), "
			+ "FOREIGN KEY (" + COLUMN_OUTLOOK_ID + ") REFERENCES " + OutlookSchema.TABLE_NAME + "(" + OutlookSchema.COLUMN_ID + ")"
			+ ")";

	public static final String DROP_TABLE_V1 = "DROP TABLE " + TABLE_NAME + ";";

	public static final String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_TYPE_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_TYPICAL_LEVEL,
			COLUMN_LEVEL_SPREAD, COLUMN_HEIGHT, COLUMN_LENGTH, COLUMN_WEIGHT, COLUMN_HEALING_RATE, COLUMN_BASE_HITS, COLUMN_BASE_ENDURANCE,
			COLUMN_SIZE_ID, COLUMN_ARMOR_TYPE, COLUMN_BASE_MOVEMENT_RATE, COLUMN_BASE_CHANNELING_RR, COLUMN_BASE_ESSENCE_RR,
			COLUMN_BASE_MENTALISM_RR, COLUMN_BASE_PHYSICAL_RR, COLUMN_BASE_FEAR_RR, COLUMN_REALM1_ID, COLUMN_REALM2_ID, COLUMN_BASE_STRIDE,
			COLUMN_LEFTOVER_DP, COLUMN_OUTLOOK_ID, COLUMN_ATTACK_SEQUENCE};
}
