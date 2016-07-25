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
package com.madinnovations.rmu.data.dao.schemas;

/**
 * Database schema data for the races table
 */
public interface RaceSchema {
	public static final String TABLE_NAME = "races";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_BONUS_DEVELOPMENT_POINTS = "bonusDevelopmentPoints";
	public static final String COLUMN_AGILITY_MODIFIER = "agilityModifier";
	public static final String COLUMN_CONSTITUTION_MODIFIER = "constitutionModifier";
	public static final String COLUMN_EMPATHY_MODIFIER = "empathyModifier";
	public static final String COLUMN_INTUITION_MODIFIER = "intuitionModifier";
	public static final String COLUMN_MEMORY_MODIFIER = "memoryModifier";
	public static final String COLUMN_PRESENCE_MODIFIER = "presenceModifier";
	public static final String COLUMN_QUICKNESS_MODIFIER = "quicknessModifier";
	public static final String COLUMN_REASONING_MODIFIER = "reasoningModifier";
	public static final String COLUMN_SELF_DISCIPLINE_MODIFIER = "selfDisciplineModifier";
	public static final String COLUMN_STRENGTH_MODIFIER = "strengthModifier";
	public static final String COLUMN_CHANNELING_RESISTANCE_MODIFIER = "channelingResistanceModifier";
	public static final String COLUMN_ESSENCE_RESISTANCE_MODIFIER = "essenceResistanceModifier";
	public static final String COLUMN_MENTALISM_RESISTANCE_MODIFIER = "mentalismResistanceModifier";
	public static final String COLUMN_PHYSICAL_RESISTANCE_MODIFIER = "physicalResistanceModifier";
	public static final String COLUMN_ENDURANCE_MODIFIER = "enduranceModifier";
	public static final String COLUMN_BASE_HITS = "baseHits";
	public static final String COLUMN_RECOVERY_MULTIPLIER = "recoveryMultiplier";
	public static final String COLUMN_SIZE_CATEGORY = "sizeCategory";
	public static final String COLUMN_STRIDE_MODIFIER = "strideModifier";
	public static final String COLUMN_AVERAGE_HEIGHT = "averageHeight";
	public static final String COLUMN_AVERAGE_WEIGHT = "averageWeight";
	public static final String COLUMN_POUNDS_PER_INCH = "poundsPerInch";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_BONUS_DEVELOPMENT_POINTS + " INTEGER NOT NULL, "
			+ COLUMN_AGILITY_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_CONSTITUTION_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_EMPATHY_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_INTUITION_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_MEMORY_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_PRESENCE_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_QUICKNESS_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_REASONING_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_SELF_DISCIPLINE_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_STRENGTH_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_CHANNELING_RESISTANCE_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_ESSENCE_RESISTANCE_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_MENTALISM_RESISTANCE_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_PHYSICAL_RESISTANCE_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_ENDURANCE_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_BASE_HITS + " INTEGER NOT NULL, "
			+ COLUMN_RECOVERY_MULTIPLIER + " REAL NOT NULL, "
			+ COLUMN_SIZE_CATEGORY + " INTEGER NOT NULL, "
			+ COLUMN_STRIDE_MODIFIER + " INTEGER NOT NULL, "
			+ COLUMN_AVERAGE_HEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_AVERAGE_WEIGHT + " INTEGER NOT NULL, "
			+ COLUMN_POUNDS_PER_INCH + " INTEGER NOT NULL"
			+ ")";

	public static final String[] COLUMNS = new String[] { COLUMN_ID,
			COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_BONUS_DEVELOPMENT_POINTS,
			COLUMN_AGILITY_MODIFIER, COLUMN_CONSTITUTION_MODIFIER, COLUMN_EMPATHY_MODIFIER,
			COLUMN_INTUITION_MODIFIER, COLUMN_MEMORY_MODIFIER, COLUMN_PRESENCE_MODIFIER,
			COLUMN_QUICKNESS_MODIFIER, COLUMN_REASONING_MODIFIER, COLUMN_SELF_DISCIPLINE_MODIFIER,
			COLUMN_STRENGTH_MODIFIER, COLUMN_CHANNELING_RESISTANCE_MODIFIER,
			COLUMN_ESSENCE_RESISTANCE_MODIFIER, COLUMN_MENTALISM_RESISTANCE_MODIFIER,
			COLUMN_PHYSICAL_RESISTANCE_MODIFIER, COLUMN_ENDURANCE_MODIFIER,
			COLUMN_BASE_HITS, COLUMN_RECOVERY_MULTIPLIER, COLUMN_SIZE_CATEGORY,
			COLUMN_STRIDE_MODIFIER, COLUMN_AVERAGE_HEIGHT, COLUMN_AVERAGE_WEIGHT,
			COLUMN_POUNDS_PER_INCH};
}
