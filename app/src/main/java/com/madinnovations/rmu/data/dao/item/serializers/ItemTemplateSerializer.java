/*
  Copyright (C) 2017 MadInnovations
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.dao.item.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.item.schemas.ArmorTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.ItemTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.NaturalsTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.SubstanceTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponTemplateSchema;
import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.common.Biome;
import com.madinnovations.rmu.data.entities.common.ManeuverDifficulty;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.item.ArmorTemplate;
import com.madinnovations.rmu.data.entities.item.Cost;
import com.madinnovations.rmu.data.entities.item.Form;
import com.madinnovations.rmu.data.entities.item.HerbTemplate;
import com.madinnovations.rmu.data.entities.item.Item;
import com.madinnovations.rmu.data.entities.item.ItemTemplate;
import com.madinnovations.rmu.data.entities.item.MoneyUnit;
import com.madinnovations.rmu.data.entities.item.NaturalsTemplate;
import com.madinnovations.rmu.data.entities.item.PoisonTemplate;
import com.madinnovations.rmu.data.entities.item.Prep;
import com.madinnovations.rmu.data.entities.item.Slot;
import com.madinnovations.rmu.data.entities.item.SubstanceTemplate;
import com.madinnovations.rmu.data.entities.item.SubstanceType;
import com.madinnovations.rmu.data.entities.item.WeaponTemplate;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link ItemTemplate} entities
 */
public class ItemTemplateSerializer extends TypeAdapter<ItemTemplate> implements ItemTemplateSchema {
	@SuppressWarnings("unused")
	private static final String TAG = "ItemTemplateSerializer";
	private static final String ITEM_TEMPLATE_TYPE = "itemTemplateType";

	@Override
	public void write(JsonWriter out, ItemTemplate value) throws IOException {
		out.beginObject();
		out.name(ITEM_TEMPLATE_TYPE).value(value.getJsonName());
		value.serialize(out);
		out.endObject().flush();
	}

	@Override
	public ItemTemplate read(JsonReader in) throws IOException {
		ItemTemplate itemTemplate = new ItemTemplate();
		boolean armorTemplate = false;
		boolean herbTemplate = false;
		boolean poisonTemplate = false;
		boolean substanceTemplate = false;
		boolean weaponTemplate = false;
		Cost baseCost = new Cost();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case ITEM_TEMPLATE_TYPE:
					switch (in.nextString()) {
						case Item.JSON_NAME:
							break;
						case ArmorTemplate.JSON_NAME:
							armorTemplate = true;
							itemTemplate = new ArmorTemplate(itemTemplate);
							break;
						case HerbTemplate.JSON_NAME:
							herbTemplate = true;
							itemTemplate = new HerbTemplate(itemTemplate);
							break;
						case PoisonTemplate.JSON_NAME:
							poisonTemplate = true;
							itemTemplate = new PoisonTemplate(itemTemplate);
							break;
						case SubstanceTemplate.JSON_NAME:
							substanceTemplate = true;
							itemTemplate = new SubstanceTemplate(itemTemplate);
							break;
						case WeaponTemplate.JSON_NAME:
							itemTemplate = new WeaponTemplate(itemTemplate);
							weaponTemplate = true;
							break;
					}
					break;
				case COLUMN_ID:
					itemTemplate.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					itemTemplate.setName(in.nextString());
					break;
				case COLUMN_WEIGHT:
					itemTemplate.setWeight((float)in.nextDouble());
					break;
				case COLUMN_BASE_COST_VALUE:
					baseCost.setValue((short)in.nextInt());
					break;
				case COLUMN_BASE_COST_UNIT:
					baseCost.setUnit(MoneyUnit.valueOf(in.nextString()));
					break;
				case COLUMN_STRENGTH:
					itemTemplate.setStrength((short)in.nextInt());
					break;
				case COLUMN_CONSTRUCTION_TIME:
					itemTemplate.setConstructionTime(in.nextInt());
					break;
				case COLUMN_MANEUVER_DIFFICULTY:
					itemTemplate.setManeuverDifficulty(ManeuverDifficulty.valueOf(in.nextString()));
					break;
				case COLUMN_NOTES:
					itemTemplate.setNotes(in.nextString());
					break;
				case COLUMN_PRIMARY_SLOT:
					itemTemplate.setPrimarySlot(Slot.valueOf(in.nextString()));
					break;
				case COLUMN_SECONDARY_SLOT:
					itemTemplate.setSecondarySlot(Slot.valueOf(in.nextString()));
					break;
				case ArmorTemplateSchema.COLUMN_SMALL_COST:
					if(armorTemplate) {
						((ArmorTemplate)itemTemplate).setSmallCost((float)in.nextDouble());
					}
					break;
				case ArmorTemplateSchema.COLUMN_MEDIUM_COST:
					if(armorTemplate) {
						((ArmorTemplate)itemTemplate).setMediumCost((float)in.nextDouble());
					}
					break;
				case ArmorTemplateSchema.COLUMN_BIG_COST:
					if(armorTemplate) {
						((ArmorTemplate)itemTemplate).setBigCost((float)in.nextDouble());
					}
					break;
				case ArmorTemplateSchema.COLUMN_LARGE_COST:
					if(armorTemplate) {
						((ArmorTemplate)itemTemplate).setLargeCost((float)in.nextDouble());
					}
					break;
				case ArmorTemplateSchema.COLUMN_WEIGHT_PERCENT:
					if(armorTemplate) {
						((ArmorTemplate)itemTemplate).setWeightPercent((float)in.nextDouble());
					}
					break;
				case ArmorTemplateSchema.COLUMN_ARMOR_TYPE:
					if(armorTemplate) {
						((ArmorTemplate)itemTemplate).setArmorType((short)in.nextInt());
					}
					break;
				case NaturalsTemplateSchema.COLUMN_BIOME_ID:
					if(herbTemplate || poisonTemplate) {
						((NaturalsTemplate)itemTemplate).setBiome(new Biome(in.nextInt()));
					}
					break;
				case NaturalsTemplateSchema.COLUMN_FORM_NAME:
					if(herbTemplate || poisonTemplate) {
						((NaturalsTemplate)itemTemplate).setForm(Form.valueOf(in.nextString()));
					}
					break;
				case NaturalsTemplateSchema.COLUMN_PREP_NAME:
					if(herbTemplate || poisonTemplate) {
						((NaturalsTemplate)itemTemplate).setPrep(Prep.valueOf(in.nextString()));
					}
					break;
				case NaturalsTemplateSchema.COLUMN_SEASON:
					if(herbTemplate || poisonTemplate) {
						((NaturalsTemplate)itemTemplate).setSeason(in.nextString());
					}
					break;
				case NaturalsTemplateSchema.COLUMN_EFFECTS:
					if(herbTemplate || poisonTemplate) {
						((NaturalsTemplate)itemTemplate).setEffects(in.nextString());
					}
					break;
				case SubstanceTemplateSchema.COLUMN_SUBSTANCE_TYPE_NAME:
					if(substanceTemplate) {
						((SubstanceTemplate)itemTemplate).setSubstanceType(SubstanceType.valueOf(in.nextString()));
					}
					break;
				case SubstanceTemplateSchema.COLUMN_HARDNESS:
					if(substanceTemplate) {
						((SubstanceTemplate)itemTemplate).setHardness((float)in.nextDouble());
					}
					break;
				case SubstanceTemplateSchema.COLUMN_DESCRIPTION:
					if(substanceTemplate) {
						((SubstanceTemplate)itemTemplate).setDescription(in.nextString());
					}
					break;
				case WeaponTemplateSchema.COLUMN_SPECIALIZATION_ID:
					if(weaponTemplate) {
						int id = in.nextInt();
						((WeaponTemplate)itemTemplate).setCombatSpecialization(new Specialization(id));
					}
					break;
				case WeaponTemplateSchema.COLUMN_DAMAGE_TABLE_ID:
					if(weaponTemplate) {
						int id = in.nextInt();
						((WeaponTemplate)itemTemplate).setDamageTable(new DamageTable(id));
					}
					break;
				case WeaponTemplateSchema.COLUMN_BRACEABLE:
					if(weaponTemplate) {
						((WeaponTemplate)itemTemplate).setBraceable(in.nextBoolean());
					}
					break;
				case WeaponTemplateSchema.COLUMN_FUMBLE:
					if(weaponTemplate) {
						((WeaponTemplate)itemTemplate).setFumble((short)in.nextInt());
					}
					break;
				case WeaponTemplateSchema.COLUMN_LENGTH:
					if(weaponTemplate) {
						((WeaponTemplate)itemTemplate).setLength((float)in.nextDouble());
					}
					break;
				case WeaponTemplateSchema.COLUMN_SIZE_ADJUSTMENT:
					if(weaponTemplate) {
						((WeaponTemplate)itemTemplate).setSizeAdjustment((short)in.nextInt());
					}
					break;
			}
			itemTemplate.setBaseCost(baseCost);
		}
		in.endObject();
		return itemTemplate;
	}
}
