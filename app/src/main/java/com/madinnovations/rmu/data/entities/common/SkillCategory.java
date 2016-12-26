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

import com.madinnovations.rmu.data.entities.DatabaseObject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Skill category attributes
 */
public class SkillCategory extends DatabaseObject implements Comparable {
	public static final String JSON_NAME = "SkillCategories";
	private String name = null;
	private String description = null;
	private boolean combat = false;
	private boolean craftAndTrade = false;
	private boolean noStats = false;
	private boolean realmStats = false;
	private List<Statistic> stats = new ArrayList<>();

	/**
	 * Creates a new SkillCategory instance
	 */
	public SkillCategory() {
	}

	/**
	 * Creates a new SkillCategory instance with the given id
	 *
	 * @param id  the id of the new instance
	 */
	public SkillCategory(int id) {
		super(id);
	}

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

	/**
	 * Builds a formatted string containing this instances attributes.
	 *
	 * @return  a formatted string containing this instances attributes.
	 */
	@SuppressWarnings("unused")
	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("name", name)
				.append("description", description)
				.append("combat", combat)
				.append("craftAndTrade", craftAndTrade)
				.append("noStats", noStats)
				.append("realmStats", realmStats)
				.append("stats", stats)
				.toString();
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(@NonNull Object o) {
		int result;

		if(!(o instanceof SkillCategory)) {
			result = 1;
		}
		else {
			SkillCategory o2 = (SkillCategory)o;
			result = this.name.compareTo(o2.name);
			if(result == 0) {
				if(this.name == null) {
					if(o2.name != null) {
						result = -1;
					}
				}
				else if(o2.name == null) {
					result = 1;
				}
				else {
					result = this.name.compareTo(o2.name);
				}
			}
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
	public boolean isCombat() {
		return combat;
	}
	public void setCombat(boolean combat) {
		this.combat = combat;
	}
	public boolean isCraftAndTrade() {
		return craftAndTrade;
	}
	public void setCraftAndTrade(boolean craftAndTrade) {
		this.craftAndTrade = craftAndTrade;
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
	public List<Statistic> getStats() {
		return stats;
	}
	public void setStats(List<Statistic> stats) {
		this.stats = stats;
	}
}
