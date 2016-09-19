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
package com.madinnovations.rmu.data.entities.creature;

import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Stat;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing Creature Archetype attributes.
 */
public class CreatureArchetype {
	private int                 id              = -1;
	private String              name            = null;
	private String              description     = null;
	private boolean             realmStat1      = false;
	private Stat                stat1           = null;
	private boolean             realmStat2      = false;
	private Stat                stat2           = null;
	private List<SkillCategory> primarySkills   = new ArrayList<>();
	private List<SkillCategory> secondarySkills = new ArrayList<>();
	private List<SkillCategory> tertiarySkills  = new ArrayList<>();
	private String              spells          = null;
	private String              roles           = null;

	/**
	 * Checks the validity of the CreatureArchetype instance.
	 *
	 * @return true if the CreatureArchetype instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() && spells != null &&
				roles != null && checkValidCategories();
	}

	private boolean checkValidCategories() {
		boolean isValid = true;
		for(SkillCategory category : primarySkills) {
			if(secondarySkills.contains(category) || tertiarySkills.contains(category)) {
				isValid = false;
				break;
			}
		}
		if(isValid) {
			for(SkillCategory category : secondarySkills) {
				if(tertiarySkills.contains(category)) {
					isValid = false;
					break;
				}
			}
		}
		return isValid;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", id)
				.append("name", name)
				.append("description", description)
				.append("realmStat1", realmStat1)
				.append("stat1", stat1)
				.append("realmStat2", realmStat2)
				.append("stat2", stat2)
				.append("primarySkills", primarySkills)
				.append("secondarySkills", secondarySkills)
				.append("tertiarySkills", tertiarySkills)
				.append("spells", spells)
				.append("roles", roles)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CreatureArchetype that = (CreatureArchetype) o;

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
	public boolean isRealmStat1() {
		return realmStat1;
	}
	public void setRealmStat1(boolean realmStat1) {
		this.realmStat1 = realmStat1;
	}
	public Stat getStat1() {
		return stat1;
	}
	public void setStat1(Stat stat1) {
		this.stat1 = stat1;
	}
	public boolean isRealmStat2() {
		return realmStat2;
	}
	public void setRealmStat2(boolean realmStat2) {
		this.realmStat2 = realmStat2;
	}
	public Stat getStat2() {
		return stat2;
	}
	public void setStat2(Stat stat2) {
		this.stat2 = stat2;
	}
	public List<SkillCategory> getPrimarySkills() {
		return primarySkills;
	}
	public void setPrimarySkills(List<SkillCategory> primarySkills) {
		this.primarySkills = primarySkills;
	}
	public List<SkillCategory> getSecondarySkills() {
		return secondarySkills;
	}
	public void setSecondarySkills(List<SkillCategory> secondarySkills) {
		this.secondarySkills = secondarySkills;
	}
	public List<SkillCategory> getTertiarySkills() {
		return tertiarySkills;
	}
	public void setTertiarySkills(List<SkillCategory> tertiarySkills) {
		this.tertiarySkills = tertiarySkills;
	}
	public String getSpells() {
		return spells;
	}
	public void setSpells(String spells) {
		this.spells = spells;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
}
