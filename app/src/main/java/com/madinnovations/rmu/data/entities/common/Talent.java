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

import com.madinnovations.rmu.data.entities.combat.Resistance;

/**
 * Talent data
 */
public class Talent {
	private int              id                  = -1;
	private TalentCategory   category            = null;
	private String           name                = null;
	private String           description         = null;
	private boolean          flaw                = false;
	private Effect[]         effects             = new Effect[0];
	private short[]          effectValues        = new short[0];
	private Skill[]          affectedSkills      = new Skill[0];
	private short[]          skillBonuses        = new short[0];
	private Resistance[]     affectedResistances = new Resistance[0];
	private short[]          resistBonuses       = new short[0];
	private Stat[]           affectedStats       = new Stat[0];
	private short[]          statBonuses         = new short[0];
	private short            tier                = 1;
	private short            maxTiers            = 1;
	private short            dpCost              = 5;
	private short            dpCostPerTier       = 0;
	private boolean          situational         = false;
	private short            actionPoints        = 0;
	private ParameterValue[] parameterValues     = new ParameterValue[0];

	/**
	 * Default no-arg constructor
	 */
	public Talent() {
	}

	/**
	 * Constructor for setting just the id (PK)
	 *
	 * @param id  the ID of the talent
	 */
	public Talent(int id) {
		this.id = id;
	}

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
		return name;
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
	public Effect[] getEffects() {
		return effects;
	}
	public void setEffects(Effect[] effects) {
		this.effects = effects;
	}
	public short[] getEffectValues() {
		return effectValues;
	}
	public void setEffectValues(short[] effectValues) {
		this.effectValues = effectValues;
	}
	public Skill[] getAffectedSkills() {
		return affectedSkills;
	}
	public void setAffectedSkills(Skill[] affectedSkills) {
		this.affectedSkills = affectedSkills;
	}
	public short[] getSkillBonuses() {
		return skillBonuses;
	}
	public void setSkillBonuses(short[] skillBonuses) {
		this.skillBonuses = skillBonuses;
	}
	public Resistance[] getAffectedResistances() {
		return affectedResistances;
	}
	public void setAffectedResistances(Resistance[] affectedResistances) {
		this.affectedResistances = affectedResistances;
	}
	public short[] getResistBonuses() {
		return resistBonuses;
	}
	public void setResistBonuses(short[] resistBonuses) {
		this.resistBonuses = resistBonuses;
	}
	public Stat[] getAffectedStats() {
		return affectedStats;
	}
	public void setAffectedStats(Stat[] affectedStats) {
		this.affectedStats = affectedStats;
	}
	public short[] getStatBonuses() {
		return statBonuses;
	}
	public void setStatBonuses(short[] statBonuses) {
		this.statBonuses = statBonuses;
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
	public ParameterValue[] getParameterValues() {
		return parameterValues;
	}
	public void setParameterValues(ParameterValue[] parameterValues) {
		this.parameterValues = parameterValues;
	}
}
