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
package com.madinnovations.rmu.data.entities.character;

import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;

import java.util.List;
import java.util.Map;

/**
 * Culture attributes
 */
public class Culture {
	private int id = -1;
	private String name;
	private String description;
	private Map<SkillCategory, Short> skillRanks;
	private List<Skill> whiteList;
	private List<Skill> blackList;

	@Override
	public String toString() {
		return "Culture{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", skillRanks=" + skillRanks +
				", whiteList=" + whiteList +
				", blackList=" + blackList +
				'}';
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
	public Map<SkillCategory, Short> getSkillRanks() {
		return skillRanks;
	}
	public void setSkillRanks(Map<SkillCategory, Short> skillRanks) {
		this.skillRanks = skillRanks;
	}
	public List<Skill> getWhiteList() {
		return whiteList;
	}
	public void setWhiteList(List<Skill> whiteList) {
		this.whiteList = whiteList;
	}
	public List<Skill> getBlackList() {
		return blackList;
	}
	public void setBlackList(List<Skill> blackList) {
		this.blackList = blackList;
	}
}
