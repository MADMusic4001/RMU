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

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a RoleMaster Skill
 */
public class Skill implements Comparable {
	public static final String JSON_NAME = "Skills";
	private int             id                     = -1;
	private String          name                   = null;
	private String          description            = null;
	private SkillCategory   category               = null;
	private boolean         requiresSpecialization = false;
	private boolean         useCategoryStats       = true;
	private boolean         requiresConcentration  = false;
	private boolean         lore                   = false;  // Used to allow skill ranks > 2x level with Intense Training
	                                                       // optional rule
	private List<Statistic> stats                  = new ArrayList<>();

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
		this.id = id;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Skill skill = (Skill) o;

		return id == skill.id;
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
	public List<Statistic> getStats() {
		return stats;
	}
	public void setStats(List<Statistic> stats) {
		this.stats = stats;
	}
}
