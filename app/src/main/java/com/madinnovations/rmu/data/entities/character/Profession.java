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
import com.madinnovations.rmu.data.entities.common.SkillCost;
import com.madinnovations.rmu.data.entities.spells.Realm;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Profession attributes
 */
public class Profession {
	public static final String JSON_NAME = "Professions";
	private int                           id                          = -1;
	private String                        name                        = null;
	private String                        description                 = null;
	private Realm                         realm1                      = null;
	private Realm                         realm2                      = null;
	private Map<SkillCategory, SkillCost> skillCategoryCosts          = new LinkedHashMap<>();
	private Map<Skill, SkillCost>         skillCosts                  = new LinkedHashMap<>();
	private Map<SkillCategory, List<SkillCost>> assignableSkillCosts  = new LinkedHashMap<>();
	private List<SkillCategory>           professionalSkillCategories = new ArrayList<>();

	/**
	 * Default constructor
	 */
	public Profession() {
	}

	/**
	 * ID constructor
	 *
	 * @param id  the id of the Profession instance
	 */
	public Profession(int id) {
		this.id = id;
	}

	/**
	 * Checks the validity of the Profession instance.
	 *
	 * @return true if the Profession instance is valid, otherwise false.
	 */
	public boolean isValid() {
		boolean isValid = (name != null && !name.isEmpty() && description != null && !description.isEmpty());
		if(isValid) {
			for (Map.Entry<SkillCategory, SkillCost> entry : skillCategoryCosts.entrySet()) {
				SkillCost skillCost = entry.getValue();
				isValid &= (skillCost == null || assignableSkillCosts.containsKey(entry.getKey()) ||
						(skillCost.getFirstCost() != null && skillCost.getAdditionalCost() != null &&
							skillCost.getFirstCost() < skillCost.getAdditionalCost()));
			}
		}
		if(isValid) {
			for (Map.Entry<Skill, SkillCost> entry : skillCosts.entrySet()) {
				SkillCost skillCost = entry.getValue();
				isValid &= (skillCost == null ||
						(skillCategoryCosts.containsKey(entry.getKey().getCategory()) &&
								skillCategoryCosts.get(entry.getKey().getCategory()).getFirstCost() != null &&
								skillCategoryCosts.get(entry.getKey().getCategory()).getAdditionalCost() != null) ||
						(skillCost.getFirstCost() != null && skillCost.getAdditionalCost() != null &&
							skillCost.getFirstCost() < skillCost.getAdditionalCost()));
			}
		}
		if(isValid) {
			for (Map.Entry<SkillCategory, List<SkillCost>> entry : assignableSkillCosts.entrySet()) {
				for(SkillCost skillCost : entry.getValue()) {
					isValid &= (skillCost == null ||
							(skillCost.getFirstCost() != null && skillCost.getAdditionalCost() != null &&
								skillCost.getFirstCost() < skillCost.getAdditionalCost()));
				}
			}
		}
		return isValid;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Generates a debug output string for this instance.
	 *
	 * @return  a string with the instance's values.
	 */
	@SuppressWarnings({"unused", "WeakerAccess"})
	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", id)
				.append("name", name)
				.append("description", description)
				.append("realm1", realm1)
				.append("realm2", realm2)
				.append("assignableSkillCosts", assignableSkillCosts)
				.append("skillCategoryCosts", skillCategoryCosts)
				.append("skillCosts", skillCosts)
				.append("professionalSkillCategories", professionalSkillCategories)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Profession that = (Profession) o;

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
	public Realm getRealm1() {
		return realm1;
	}
	public void setRealm1(Realm realm1) {
		this.realm1 = realm1;
	}
	public Realm getRealm2() {
		return realm2;
	}
	public void setRealm2(Realm realm2) {
		this.realm2 = realm2;
	}
	public Map<SkillCategory, SkillCost> getSkillCategoryCosts() {
		return skillCategoryCosts;
	}
	public void setSkillCategoryCosts(Map<SkillCategory, SkillCost> skillCategoryCosts) {
		this.skillCategoryCosts = skillCategoryCosts;
	}
	public Map<Skill, SkillCost> getSkillCosts() {
		return skillCosts;
	}
	public void setSkillCosts(Map<Skill, SkillCost> skillCosts) {
		this.skillCosts = skillCosts;
	}
	public Map<SkillCategory, List<SkillCost>> getAssignableSkillCosts() {
		return assignableSkillCosts;
	}
	public void setAssignableSkillCosts(Map<SkillCategory, List<SkillCost>> assignableSkillCosts) {
		this.assignableSkillCosts = assignableSkillCosts;
	}
	public List<SkillCategory> getProfessionalSkillCategories() {
		return professionalSkillCategories;
	}
}
