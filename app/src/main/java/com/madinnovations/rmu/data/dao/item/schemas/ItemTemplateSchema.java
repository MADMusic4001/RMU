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

/**
 * Database schema data for the item_templates table
 */
public interface ItemTemplateSchema {
	String TABLE_NAME = "item_templates";

	String COLUMN_ID                  = "id";
	String COLUMN_NAME                = "name";
	String COLUMN_WEIGHT              = "weight";
	String COLUMN_BASE_COST_VALUE     = "baseCostValue";
	String COLUMN_BASE_COST_UNIT      = "baseCostUnit";
	String COLUMN_STRENGTH            = "strength";
	String COLUMN_CONSTRUCTION_TIME   = "constructionTime";
	String COLUMN_MANEUVER_DIFFICULTY = "maneuverDifficulty";
	String COLUMN_NOTES               = "notes";
	String COLUMN_PRIMARY_SLOT        = "primarySlot";
	String COLUMN_SECONDARY_SLOT      = "secondarySlot";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_NAME  + " TEXT NOT NULL, "
			+ COLUMN_WEIGHT + " REAL NOT NULL, "
			+ COLUMN_BASE_COST_VALUE + " INTEGER NOT NULL, "
			+ COLUMN_BASE_COST_UNIT + " TEXT NOT NULL, "
			+ COLUMN_STRENGTH + " INTEGER NOT NULL, "
			+ COLUMN_CONSTRUCTION_TIME + " INTEGER NOT NULL, "
			+ COLUMN_MANEUVER_DIFFICULTY + " TEXT, "
			+ COLUMN_NOTES + " TEXT, "
			+ COLUMN_PRIMARY_SLOT + " TEXT, "
			+ COLUMN_SECONDARY_SLOT + " TEXT"
			+ ")";

	String QUERY_ALL = "SELECT"
			+ " ITEM." + COLUMN_ID
			+ ", ITEM." + COLUMN_NAME
			+ ", ITEM." + COLUMN_WEIGHT
			+ ", ITEM." + COLUMN_BASE_COST_VALUE
			+ ", ITEM." + COLUMN_BASE_COST_UNIT
			+ ", ITEM." + COLUMN_STRENGTH
			+ ", ITEM." + COLUMN_CONSTRUCTION_TIME
			+ ", ITEM." + COLUMN_MANEUVER_DIFFICULTY
			+ ", ITEM." + COLUMN_NOTES
			+ ", ITEM." + COLUMN_PRIMARY_SLOT
			+ ", ITEM." + COLUMN_SECONDARY_SLOT
			+ ", ARMOR." + ArmorTemplateSchema.COLUMN_ID + " AS " + ArmorTemplateSchema.ARMOR_ID
			+ ", ARMOR." + ArmorTemplateSchema.COLUMN_SMALL_COST
			+ ", ARMOR." + ArmorTemplateSchema.COLUMN_MEDIUM_COST
			+ ", ARMOR." + ArmorTemplateSchema.COLUMN_BIG_COST
			+ ", ARMOR." + ArmorTemplateSchema.COLUMN_LARGE_COST
			+ ", ARMOR." + ArmorTemplateSchema.COLUMN_WEIGHT_PERCENT
			+ ", ARMOR." + ArmorTemplateSchema.COLUMN_ARMOR_TYPE
			+ ", HERBS." + HerbTemplateSchema.COLUMN_NATURALS_TEMPLATE_ID + " AS " + HerbTemplateSchema.HERB_ID
			+ ", NATURALS." + NaturalsTemplateSchema.COLUMN_ITEM_TEMPLATE_ID + " AS " + NaturalsTemplateSchema.NATURAL_ID
			+ ", NATURALS." + NaturalsTemplateSchema.COLUMN_BIOME_ID
			+ ", NATURALS." + NaturalsTemplateSchema.COLUMN_FORM_NAME
			+ ", NATURALS." + NaturalsTemplateSchema.COLUMN_PREP_NAME
			+ ", NATURALS." + NaturalsTemplateSchema.COLUMN_SEASON
			+ ", NATURALS." + NaturalsTemplateSchema.COLUMN_EFFECTS
			+ ", POISONS." + PoisonTemplateSchema.COLUMN_NATURALS_TEMPLATE_ID + " AS " + PoisonTemplateSchema.POISON_ID
			+ ", SUBSTANCES." + SubstanceTemplateSchema.COLUMN_ID + " AS " + SubstanceTemplateSchema.SUBSTANCE_ID
			+ ", SUBSTANCES." + SubstanceTemplateSchema.COLUMN_SUBSTANCE_TYPE_NAME
			+ ", SUBSTANCES." + SubstanceTemplateSchema.COLUMN_HARDNESS
			+ ", SUBSTANCES." + SubstanceTemplateSchema.COLUMN_DESCRIPTION
			+ ", WEAPON." + WeaponTemplateSchema.COLUMN_ID + " AS " + WeaponTemplateSchema.WEAPON_ID
			+ ", WEAPON." + WeaponTemplateSchema.COLUMN_SPECIALIZATION_ID
			+ ", WEAPON." + WeaponTemplateSchema.COLUMN_DAMAGE_TABLE_ID
			+ ", WEAPON." + WeaponTemplateSchema.COLUMN_BRACEABLE
			+ ", WEAPON." + WeaponTemplateSchema.COLUMN_FUMBLE
			+ ", WEAPON." + WeaponTemplateSchema.COLUMN_LENGTH
			+ ", WEAPON." + WeaponTemplateSchema.COLUMN_SIZE_ADJUSTMENT
			+ " FROM " + TABLE_NAME + " ITEM"
				+ " LEFT OUTER JOIN " + ArmorTemplateSchema.TABLE_NAME + " ARMOR"
					+ " ON ARMOR." + ArmorTemplateSchema.COLUMN_ID + " = ITEM." + ItemTemplateSchema.COLUMN_ID
				+ " LEFT OUTER JOIN " + HerbTemplateSchema.TABLE_NAME + " HERBS"
					+ " ON HERBS. " + HerbTemplateSchema.COLUMN_NATURALS_TEMPLATE_ID + " = NATURALS." + NaturalsTemplateSchema.COLUMN_ITEM_TEMPLATE_ID
				+ " LEFT OUTER JOIN " + NaturalsTemplateSchema.TABLE_NAME + " NATURALS"
					+ " ON NATURALS." + NaturalsTemplateSchema.COLUMN_ITEM_TEMPLATE_ID + " = ITEM." +ItemTemplateSchema.COLUMN_ID
				+ " LEFT OUTER JOIN " + PoisonTemplateSchema.TABLE_NAME + " POISONS"
					+ " ON POISONS. " + PoisonTemplateSchema.COLUMN_NATURALS_TEMPLATE_ID + " = NATURALS." + NaturalsTemplateSchema.COLUMN_ITEM_TEMPLATE_ID
				+ " LEFT OUTER JOIN " + SubstanceTemplateSchema.TABLE_NAME + " SUBSTANCES"
					+ " ON SUBSTANCES." + SubstanceTemplateSchema.COLUMN_ID + " = ITEM." + ItemTemplateSchema.COLUMN_ID
				+ " LEFT OUTER JOIN " + WeaponTemplateSchema.TABLE_NAME + " WEAPON"
					+ " ON WEAPON." + WeaponTemplateSchema.COLUMN_ID + " = ITEM." + ItemTemplateSchema.COLUMN_ID;

	String QUERY_BY_ID = QUERY_ALL + " WHERE ITEM." + ItemTemplateSchema.COLUMN_ID + " = ?";

	String QUERY_NO_SUBCLASS = QUERY_ALL + " WHERE ARMOR." + ArmorTemplateSchema.COLUMN_ID + " IS NULL AND"
			+ " HERBS." + HerbTemplateSchema.COLUMN_NATURALS_TEMPLATE_ID + " IS NULL AND"
			+ " NATURALS." + NaturalsTemplateSchema.COLUMN_ITEM_TEMPLATE_ID + " IS NULL AND"
			+ " POISONS." + PoisonTemplateSchema.COLUMN_NATURALS_TEMPLATE_ID + " IS NULL AND"
			+ " SUBSTANCES." + SubstanceTemplateSchema.COLUMN_ID + " IS NULL AND"
			+ " WEAPON." + WeaponTemplateSchema.COLUMN_ID + " IS NULL";

	String QUERY_FOR_SLOT = QUERY_ALL + " WHERE " + COLUMN_PRIMARY_SLOT + " = ? OR " + COLUMN_SECONDARY_SLOT + " = ? OR "
			+ COLUMN_PRIMARY_SLOT + " = ?";

	String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_WEIGHT, COLUMN_BASE_COST_VALUE, COLUMN_BASE_COST_UNIT,
			COLUMN_STRENGTH, COLUMN_CONSTRUCTION_TIME, COLUMN_MANEUVER_DIFFICULTY, COLUMN_NOTES, COLUMN_PRIMARY_SLOT,
			COLUMN_SECONDARY_SLOT};
}
