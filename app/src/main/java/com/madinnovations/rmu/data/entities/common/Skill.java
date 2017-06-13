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
package com.madinnovations.rmu.data.entities.common;

import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.entities.DatabaseObject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a RoleMaster Skill
 */
@SuppressWarnings("unused")
public class Skill extends DatabaseObject implements Comparable {
	public static final String JSON_NAME = "Skills";
	private String          name                   = null;
	private String          description            = null;
	private SkillCategory   category               = null;
	private boolean         requiresSpecialization = false;
	private boolean         useCategoryStats       = true;
	private boolean         requiresConcentration  = false;
	private boolean         lore                   = false;
	private boolean         creatureOnly           = false;
	private List<Statistic> stats                  = new ArrayList<>();
	private List<Specialization> specializations   = new ArrayList<>();

	/**
	 * Creates a new empty Skill instance.
	 */
	public Skill() {
	}

	/**
	 * Creates a new skill instance with the given id.
	 *
	 * @param id  the id for this new instance
	 */
	public Skill(int id) {
		super(id);
	}

	/**
	 * Creates a new skill instance with the given name.
	 *
	 * @param name  the name for this new instance
	 */
	public Skill(String name) {
		this.name = name;
	}

	/**
	 * Checks the validity of the Skill instance.
	 *
	 * @return true if the Skill instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() && category != null &&
				(useCategoryStats || (stats != null && !stats.isEmpty() && stats.size() == 3));
	}

	@Override
	public int compareTo(@NonNull Object o) {
		int result = 0;
		Skill skill2;

		if(o instanceof Skill) {
			skill2 = (Skill) o;
			if (this.getName() == null) {
				if (skill2.getName() != null) {
					result = 1;
				}
			}
			else if (skill2.getName() == null) {
				result = -1;
			}
			else {
				result = this.getName().compareTo(skill2.getName());
			}
		}
		else {
			result = -1;
		}

		return result;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Generates a String with all of this instances member variable values.
	 *
	 * @return a String with all of this instances member variable values.
	 */
	public String print() {
		return new ToStringBuilder(this,
								   ToStringStyle.MULTI_LINE_STYLE)
				.append("name", name)
				.append("description", description)
				.append("category", category)
				.append("requiresSpecialization", requiresSpecialization)
				.append("useCategoryStats", useCategoryStats)
				.append("requiresConcentration", requiresConcentration)
				.append("lore", lore)
				.append("creatureOnly", creatureOnly)
				.append("stats", stats)
				.append("specializations", specializations)
				.toString();
	}

	/**
	 * Calculates the skill rank bonus
	 *
	 * @param ranks  the number of ranks in the skill
	 * @return  the skill rank bonus.
	 */
	public static short getRankBonus(short ranks) {
		short result = 0;

		if(ranks > 30) {
			result = (short)(ranks - 30);
			ranks = 30;
		}
		if(ranks > 20) {
			result += (short)((ranks - 20) * 2);
			ranks = 20;
		}
		if(ranks > 10) {
			result += (short)((ranks - 10) * 3);
			ranks = 10;
		}
		if(ranks > 0) {
			result += (short)(ranks * 5);
		}

		if(result == 0) {
			result = -25;
		}

		return result;
	}

	// Getters and setters
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
	public SkillCategory getCategory() {
		return category;
	}
	public void setCategory(SkillCategory category) {
		this.category = category;
	}
	public boolean isRequiresSpecialization() {
		return requiresSpecialization;
	}
	public void setRequiresSpecialization(boolean requiresSpecialization) {
		this.requiresSpecialization = requiresSpecialization;
	}
	public boolean isUseCategoryStats() {
		return useCategoryStats;
	}
	public void setUseCategoryStats(boolean useCategoryStats) {
		this.useCategoryStats = useCategoryStats;
	}
	public boolean isRequiresConcentration() {
		return requiresConcentration;
	}
	public void setRequiresConcentration(boolean requiresConcentration) {
		this.requiresConcentration = requiresConcentration;
	}
	public boolean isLore() {
		return lore;
	}
	public void setLore(boolean lore) {
		this.lore = lore;
	}
	public boolean isCreatureOnly() {
		return creatureOnly;
	}
	public void setCreatureOnly(boolean creatureOnly) {
		this.creatureOnly = creatureOnly;
	}
	public List<Statistic> getStats() {
		return stats;
	}
	public void setStats(List<Statistic> stats) {
		this.stats = stats;
	}
	public List<Specialization> getSpecializations() {
		return specializations;
	}
	public void setSpecializations(List<Specialization> specializations) {
		this.specializations = specializations;
	}
}
