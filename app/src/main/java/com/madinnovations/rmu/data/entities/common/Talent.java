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

import java.util.List;

/**
 * Talent data
 */
public class Talent {
	private int id = -1;
	private TalentCategory category;
	private String name;
	private String description;
	private Skill affectedSkill;
	private short initialCost;
	private short costPerTier;
	private short bonusPerTier;
	private boolean situational;
	private short actionPoints;
	private List<Parameter> parameters;

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
	public Skill getAffectedSkill() {
		return affectedSkill;
	}
	public void setAffectedSkill(Skill affectedSkill) {
		this.affectedSkill = affectedSkill;
	}
	public short getInitialCost() {
		return initialCost;
	}
	public void setInitialCost(short initialCost) {
		this.initialCost = initialCost;
	}
	public short getCostPerTier() {
		return costPerTier;
	}
	public void setCostPerTier(short costPerTier) {
		this.costPerTier = costPerTier;
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
	public List<Parameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
}
