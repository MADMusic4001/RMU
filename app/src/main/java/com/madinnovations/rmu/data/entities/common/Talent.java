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

import com.madinnovations.rmu.data.entities.combat.Action;

/**
 * Talent data
 */
public class Talent {
	private int               id               = -1;
	private TalentCategory    category         = null;
	private String               name                = null;
	private String               description         = null;
	private boolean              flaw                = false;
	private short                minTier             = 1;
	private short                maxTier             = 1;
	private short                dpCost              = 5;
	private short                dpCostPerTier       = 0;
	private boolean              situational         = false;
	private Action               action              = Action.MELEE_ATTACK;
	private TalentParameterRow[] talentParameterRows = new TalentParameterRow[0];

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
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() && category != null
				&& minTier <= maxTier && action != null;
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
	public short getMinTier() {
		return minTier;
	}
	public void setMinTier(short minTier) {
		this.minTier = minTier;
	}
	public short getMaxTier() {
		return maxTier;
	}
	public void setMaxTier(short maxTier) {
		this.maxTier = maxTier;
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
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public TalentParameterRow[] getTalentParameterRows() {
		return talentParameterRows;
	}
	public void setTalentParameterRows(TalentParameterRow[] talentParameterRows) {
		this.talentParameterRows = talentParameterRows;
	}
}
