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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Skill specialization attributes
 */
public class Specialization {
	public static final String JSON_NAME = "Specializations";
	private int id = -1;
	private String name = null;
	private String description = null;
	private Skill skill = null;
	private boolean useSkillStats = true;
	private boolean creatureOnly = false;
	private List<Statistic> stats = new ArrayList<>();

	/**
	 * Creates a new Specialization instance
	 */
	public Specialization() {
	}

	/**
	 * Creates a new Specialization instance with the given id
	 *
	 * @param id  the id for the new instance
	 */
	public Specialization(int id) {
		this.id = id;
	}

	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() && skill != null &&
				(useSkillStats || (stats != null && stats.size() == 3));
	}

	/**
	 * Outputs a formatted string listing all of the instances attributes.
	 *
	 * @return  a formatted String with the instances attributes.
	 */
	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", id)
				.append("name", name)
				.append("description", description)
				.append("skill", skill)
				.append("useSkillStats", useSkillStats)
				.append("creatureOnly", creatureOnly)
				.append("stats", stats)
				.toString();
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Specialization that = (Specialization) o;

		return id == that.id;
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
	public Skill getSkill() {
		return skill;
	}
	public void setSkill(Skill skill) {
		this.skill = skill;
	}
	public boolean isUseSkillStats() {
		return useSkillStats;
	}
	public void setUseSkillStats(boolean useSkillStats) {
		this.useSkillStats = useSkillStats;
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
}
