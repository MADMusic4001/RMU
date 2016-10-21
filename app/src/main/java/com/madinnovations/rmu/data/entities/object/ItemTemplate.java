/**
 * Copyright (C) 2016 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.entities.object;

import com.madinnovations.rmu.data.entities.common.ManeuverDifficulty;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 10/16/2016.
 */
public class ItemTemplate {
	public static final String JSON_NAME = "ItemTemplates";
	private int id = -1;
	private String name = null;
	private float weight = 0.5f;
	private float baseCost = 0.0f;
	private short strength = 0;
	private float constructionTime = 0.0f;
	private ManeuverDifficulty maneuverDifficulty = null;
	private String notes = null;

	/**
	 * Checks the validity of the Item instance.
	 *
	 * @return true if the ItemTemplate instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty();
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Debug output of this instance.
	 *
	 * @return a String of this instance's attributes.
	 */
	public String debugToString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", id)
				.append("name", name)
				.append("weight", weight)
				.append("baseCost", baseCost)
				.append("strength", strength)
				.append("constructionTime", constructionTime)
				.append("maneuverDifficulty", maneuverDifficulty)
				.append("notes", notes)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ItemTemplate itemTemplate = (ItemTemplate) o;

		return id == itemTemplate.id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	// Getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public float getBaseCost() {
		return baseCost;
	}
	public void setBaseCost(float baseCost) {
		this.baseCost = baseCost;
	}
	public short getStrength() {
		return strength;
	}
	public void setStrength(short strength) {
		this.strength = strength;
	}
	public float getConstructionTime() {
		return constructionTime;
	}
	public void setConstructionTime(float constructionTime) {
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
}