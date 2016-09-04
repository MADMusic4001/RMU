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

import java.util.List;

/**
 * Skill category attributes
 */
public class SkillCategory {
	private int id = -1;
	private String name = null;
	private String description = null;
	private boolean combat = false;
	private boolean noStats = false;
	private boolean realmStats = false;
	private List<Stat> stats = null;

	/**
	 * Checks the validity of the SkillCategory instance.
	 *
	 * @return true if the SkillCategory instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() &&
				(noStats || (stats != null && stats.size() >= 1 &&
				(realmStats || (stats != null && stats.size() == 3))));
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", id)
				.append("name", name)
				.append("description", description)
				.append("combat", combat)
				.append("noStats", noStats)
				.append("realmStats", realmStats)
				.append("stats", stats)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SkillCategory that = (SkillCategory) o;

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
	public boolean isCombat() {
		return combat;
	}
	public void setCombat(boolean combat) {
		this.combat = combat;
	}
	public boolean isNoStats() {
		return noStats;
	}
	public void setNoStats(boolean noStats) {
		this.noStats = noStats;
	}
	public boolean isRealmStats() {
		return realmStats;
	}
	public void setRealmStats(boolean realmStats) {
		this.realmStats = realmStats;
	}
	public List<Stat> getStats() {
		return stats;
	}
	public void setStats(List<Stat> stats) {
		this.stats = stats;
	}
}
