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
package com.madinnovations.rmu.data.entities.item;

import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.item.schemas.ItemTemplateSchema;
import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.common.ManeuverDifficulty;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 *         Created 10/16/2016.
 */
public class ItemTemplate extends DatabaseObject {
	public static final String             JSON_NAME          = "ItemTemplate";
	private             String             name               = null;
	private             float              weight             = 0.5f;
	private             Cost               baseCost           = null;
	private             short              strength           = 70;
	private             int                constructionTime   = 1;
	private             ManeuverDifficulty maneuverDifficulty = ManeuverDifficulty.MEDIUM;
	private             String             notes              = null;
	private             Slot               primarySlot        = null;
	private             Slot               secondarySlot      = null;

	/**
	 * Creates a new ItemTemplate instance.
	 */
	public ItemTemplate() {
	}

	/**
	 * Creates a new ItemTemplate instance with the given id.
	 *
	 * @param id the id of the new ItemTemplate instance
	 */
	public ItemTemplate(int id) {
		super(id);
	}

// <editor-fold desc="Overridden methods">
	@Override
	public String toString() {
		return name;
	}
// </editor-fold> Overridden methods

// <editor-fold desc="Public methods">
	/**
	 * Debug output of this instance.
	 *
	 * @return a String of this instance's attributes.
	 */
	@SuppressWarnings("unused")
	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("name", name)
				.append("weight", weight)
				.append("baseCost", baseCost)
				.append("strength", strength)
				.append("constructionTime", constructionTime)
				.append("maneuverDifficulty", maneuverDifficulty)
				.append("notes", notes)
				.append("primarySlot", primarySlot)
				.append("secondarySlot", secondarySlot)
				.toString();
	}

	/**
	 * Writes this instances fields to a JSONWriter
	 *
	 * @param out a JSONWrite instance to write the fields to
	 * @throws IOException when an IO error occurs
	 */
	public void serialize(JsonWriter out)
	throws IOException {
		out.name(ItemTemplateSchema.COLUMN_ID).value(getId());
		out.name(ItemTemplateSchema.COLUMN_NAME).value(getName());
		out.name(ItemTemplateSchema.COLUMN_WEIGHT).value(getWeight());
		if(baseCost != null) {
			out.name(ItemTemplateSchema.COLUMN_BASE_COST_VALUE).value(getBaseCost().getValue());
			out.name(ItemTemplateSchema.COLUMN_BASE_COST_UNIT).value(getBaseCost().getUnit().name());
		}
		out.name(ItemTemplateSchema.COLUMN_STRENGTH).value(getStrength());
		out.name(ItemTemplateSchema.COLUMN_CONSTRUCTION_TIME).value(getConstructionTime());
		if (getManeuverDifficulty() != null) {
			out.name(ItemTemplateSchema.COLUMN_MANEUVER_DIFFICULTY).value(getManeuverDifficulty().name());
		}
		if (getNotes() != null) {
			out.name(ItemTemplateSchema.COLUMN_NOTES).value(getNotes());
		}
		if (getPrimarySlot() != null) {
			out.name(ItemTemplateSchema.COLUMN_PRIMARY_SLOT).value(getPrimarySlot().name());
		}
		if (getSecondarySlot() != null) {
			out.name(ItemTemplateSchema.COLUMN_SECONDARY_SLOT).value(getSecondarySlot().name());
		}
	}
// </editor-fold> Public methods

// <editor-fold desc="Getters and setters">
	/**
	 * Checks the validity of the Item instance.
	 *
	 * @return true if the ItemTemplate instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty();
	}

	// Getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public Cost getBaseCost() {
		return baseCost;
	}

	public void setBaseCost(Cost baseCost) {
		this.baseCost = baseCost;
	}

	public short getStrength() {
		return strength;
	}

	public void setStrength(short strength) {
		this.strength = strength;
	}

	public int getConstructionTime() {
		return constructionTime;
	}

	public void setConstructionTime(int constructionTime) {
		this.constructionTime = constructionTime;
	}

	public ManeuverDifficulty getManeuverDifficulty() {
		return maneuverDifficulty;
	}

	public void setManeuverDifficulty(ManeuverDifficulty maneuverDifficulty) {
		this.maneuverDifficulty = maneuverDifficulty;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Slot getPrimarySlot() {
		return primarySlot;
	}

	public void setPrimarySlot(Slot primarySlot) {
		this.primarySlot = primarySlot;
	}

	public Slot getSecondarySlot() {
		return secondarySlot;
	}

	public void setSecondarySlot(Slot secondarySlot) {
		this.secondarySlot = secondarySlot;
	}

	public String getJsonName() {
		return JSON_NAME;
	}
// </editor-fold> Getters and setters
}
