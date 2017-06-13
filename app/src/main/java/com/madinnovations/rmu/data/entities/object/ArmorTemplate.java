/*
  Copyright (C) 2016 MadInnovations
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
package com.madinnovations.rmu.data.entities.object;

import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.item.schemas.ArmorTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.ItemTemplateSchema;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;

/**
 * Armor Attributes
 */
public class ArmorTemplate extends ItemTemplate {
	public static final String JSON_NAME = "ArmorTemplate";
	private float              smallCost          = 0.0f;
	private float              mediumCost         = 0.0f;
	private float              bigCost            = 0.0f;
	private float              largeCost          = 0.0f;
	private float              weightPercent      = 0.f;
	private short              armorType          = 0;

	/**
	 * Creates a new ArmorTemplate instance
	 */
	public ArmorTemplate() {
	}

	/**
	 * Creates a new ArmorTemplate instance with the given id
	 *
	 * @param id  the id to use for the new instance
	 */
	public ArmorTemplate(int id) {
		super(id);
	}

	/**
	 * Creates a new ArmorTemplate instance from the given ItemTemplate
	 *
	 * @param other  an ItemTemplate instance
	 */
	public ArmorTemplate(ItemTemplate other) {
		this.setId(other.getId());
		this.setName(other.getName());
		this.setWeight(other.getWeight());
		this.setBaseCost(other.getBaseCost());
		this.setStrength(other.getStrength());
		this.setConstructionTime(other.getConstructionTime());
		this.setManeuverDifficulty(other.getManeuverDifficulty());
		this.setNotes(other.getNotes());
		this.setPrimarySlot(other.getPrimarySlot());
		this.setSecondarySlot(other.getSecondarySlot());
	}

	/**
	 * Debug output of this instance.
	 *
	 * @return a String of this instance's attributes.
	 */
	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("ItemTemplate", super.print())
				.append("smallCost", smallCost)
				.append("mediumCost", mediumCost)
				.append("bigCost", bigCost)
				.append("largeCost", largeCost)
				.append("weightPercent", weightPercent)
				.append("armorType", armorType)
				.toString();
	}

	/**
	 * Writes this instances fields to a JSONWriter
	 *
	 * @param out  a JSONWrite instance to write the fields to
	 * @throws IOException  when an IO error occurs
	 */
	public void serialize(JsonWriter out)
	throws IOException {
		super.serialize(out);
		out.name(ArmorTemplateSchema.COLUMN_SMALL_COST).value(getSmallCost());
		out.name(ArmorTemplateSchema.COLUMN_MEDIUM_COST).value(getMediumCost());
		out.name(ArmorTemplateSchema.COLUMN_BIG_COST).value(getBigCost());
		out.name(ArmorTemplateSchema.COLUMN_LARGE_COST).value(getLargeCost());
		out.name(ArmorTemplateSchema.COLUMN_WEIGHT_PERCENT).value(getWeightPercent());
		out.name(ArmorTemplateSchema.COLUMN_ARMOR_TYPE).value(getArmorType());
	}

	// Getters and setters
	public float getSmallCost() {
		return smallCost;
	}
	public void setSmallCost(float smallCost) {
		this.smallCost = smallCost;
	}
	public float getMediumCost() {
		return mediumCost;
	}
	public void setMediumCost(float mediumCost) {
		this.mediumCost = mediumCost;
	}
	public float getBigCost() {
		return bigCost;
	}
	public void setBigCost(float bigCost) {
		this.bigCost = bigCost;
	}
	public float getLargeCost() {
		return largeCost;
	}
	public void setLargeCost(float largeCost) {
		this.largeCost = largeCost;
	}
	public float getWeightPercent() {
		return weightPercent;
	}
	public void setWeightPercent(float weightPercent) {
		this.weightPercent = weightPercent;
	}
	public short getArmorType() {
		return armorType;
	}
	public void setArmorType(short armorType) {
		this.armorType = armorType;
	}
	@Override
	public String getJsonName() {
		return JSON_NAME;
	}
}
