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
package com.madinnovations.rmu.data.entities.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Talent data
 */
public class Talent {
	private int id = -1;
	private TalentCategory category = null;
	private String name = null;
	private String description = null;
	private boolean flaw = false;
	private Skill affectedSkill = null;
	private short tier = 1;
	private short maxTiers = 1;
	private short dpCost = 15;
	private short dpCostPerTier = 0;
	private short bonusPerTier = 5;
	private boolean situational = false;
	private short actionPoints = 0;
	private List<ParameterValue> parameterValues = new ArrayList<>();

	/**
	 * Checks the validity of the Talent instance.
	 *
	 * @return true if the Talent instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() && category != null;
	}

	@Override
	public String toString() {
		return "Talent{" +
				"id=" + id +
				", category=" + category +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", flaw=" + flaw +
				", affectedSkill=" + affectedSkill +
				", tier=" + tier +
				", maxTiers=" + maxTiers +
				", dpCost=" + dpCost +
				", dpCostPerTier=" + dpCostPerTier +
				", bonusPerTier=" + bonusPerTier +
				", situational=" + situational +
				", actionPoints=" + actionPoints +
				", parameters=" + parameterValues +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Talent talent = (Talent) o;

		return id == talent.id;

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
	public TalentCategory getCategory() {
		return category;
	}
	public void setCategory(TalentCategory category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isFlaw() {
		return flaw;
	}
	public void setFlaw(boolean flaw) {
		this.flaw = flaw;
	}
	public Skill getAffectedSkill() {
		return affectedSkill;
	}
	public void setAffectedSkill(Skill affectedSkill) {
		this.affectedSkill = affectedSkill;
	}
	public short getTier() {
		return tier;
	}
	public void setTier(short tier) {
		this.tier = tier;
	}
	public short getMaxTiers() {
		return maxTiers;
	}
	public void setMaxTiers(short maxTiers) {
		this.maxTiers = maxTiers;
	}
	public short getDpCost() {
		return dpCost;
	}
	public void setDpCost(short dpCost) {
		this.dpCost = dpCost;
	}
	public short getDpCostPerTier() {
		return dpCostPerTier;
	}
	public void setDpCostPerTier(short dpCostPerTier) {
		this.dpCostPerTier = dpCostPerTier;
	}
	public short getBonusPerTier() {
		return bonusPerTier;
	}
	public void setBonusPerTier(short bonusPerTier) {
		this.bonusPerTier = bonusPerTier;
	}
	public boolean isSituational() {
		return situational;
	}
	public void setSituational(boolean situational) {
		this.situational = situational;
	}
	public short getActionPoints() {
		return actionPoints;
	}
	public void setActionPoints(short actionPoints) {
		this.actionPoints = actionPoints;
	}
	public List<ParameterValue> getParameterValues() {
		return parameterValues;
	}
	public void setParameterValues(List<ParameterValue> parameterValues) {
		this.parameterValues = parameterValues;
	}
}
