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
 * Skill specialization attributes
 */
public class Specialization {
	private int id = -1;
	private String name = null;
	private String description = null;
	private Skill skill = null;
	private boolean useSkillStats = true;
	private List<Stat> stats = null;

	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() && skill != null &&
				(useSkillStats || (stats != null && stats.size() == 3));
	}

	@Override
	public String toString() {
		return "Specialization{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", skill=" + skill +
				", useSkillStats=" + useSkillStats +
				", stats=" + stats +
				'}';
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
	public List<Stat> getStats() {
		return stats;
	}
	public void setStats(List<Stat> stats) {
		this.stats = stats;
	}
}
