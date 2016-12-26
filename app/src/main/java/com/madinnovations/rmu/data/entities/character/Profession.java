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

import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.common.DevelopmentCostGroup;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
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
public class Profession extends DatabaseObject {
	public static final String JSON_NAME = "Professions";
	private String                                         name                        = null;
	private String                                         description                 = null;
	private Realm                                          realm1                      = null;
	private Realm                                          realm2                      = null;
	private Map<SkillCategory, DevelopmentCostGroup>       skillCategoryCosts          = new LinkedHashMap<>();
	private Map<Skill, DevelopmentCostGroup>               skillCosts                  = new LinkedHashMap<>();
	private Map<SkillCategory, List<DevelopmentCostGroup>> assignableSkillCostsMap     = new LinkedHashMap<>();
	private List<SkillCategory>                            professionalSkillCategories = new ArrayList<>();

	/**
	 * Default constructor
	 */
	public Profession() {
	}

	/**
	 * Copy constructor.
	 *
	 * @param other  a {@link Profession} to copy
	 */
	public Profession(Profession other) {
		this.name = other.name;
		this.description = other.description;
		this.realm1 = other.realm1;
		this.realm2 = other.realm2;
		this.skillCategoryCosts = other.skillCategoryCosts;
		this.skillCosts = other.skillCosts;
		this.assignableSkillCostsMap = other.assignableSkillCostsMap;
		this.professionalSkillCategories = other.professionalSkillCategories;
	}

	/**
	 * ID constructor
	 *
	 * @param id  the id of the Profession instance
	 */
	public Profession(int id) {
		super(id);
	}

	/**
	 * Checks the validity of the Profession instance.
	 *
	 * @return true if the Profession instance is valid, otherwise false.
	 */
	public boolean isValid() {
		boolean isValid = (name != null && !name.isEmpty() && description != null && !description.isEmpty());
		if(isValid) {
			for (Map.Entry<SkillCategory, DevelopmentCostGroup> entry : skillCategoryCosts.entrySet()) {
				DevelopmentCostGroup skillCost = entry.getValue();
				boolean allSkillsHaveCosts = true;
				if(skillCost == null) {
					for (Map.Entry<Skill, DevelopmentCostGroup> skillEntry : skillCosts.entrySet()) {
						if(entry.getKey().equals(skillEntry.getKey().getCategory())) {
							allSkillsHaveCosts &= skillEntry.getValue() != null;
						}
					}
				}
				isValid &= (skillCost != null || allSkillsHaveCosts || assignableSkillCostsMap.containsKey(entry.getKey()));
			}
		}
		if(isValid) {
			for (Map.Entry<SkillCategory, List<DevelopmentCostGroup>> entry : assignableSkillCostsMap.entrySet()) {
				for(DevelopmentCostGroup skillCost : entry.getValue()) {
					isValid &= (skillCost != null);
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
				.append("id", getId())
				.append("name", name)
				.append("description", description)
				.append("realm1", realm1)
				.append("realm2", realm2)
				.append("assignableSkillCostsMap", assignableSkillCostsMap)
				.append("skillCategoryCosts", skillCategoryCosts)
				.append("skillCosts", skillCosts)
				.append("professionalSkillCategories", professionalSkillCategories)
				.toString();
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
	public Map<SkillCategory, DevelopmentCostGroup> getSkillCategoryCosts() {
		return skillCategoryCosts;
	}
	public void setSkillCategoryCosts(Map<SkillCategory, DevelopmentCostGroup> skillCategoryCosts) {
		this.skillCategoryCosts = skillCategoryCosts;
	}
	public Map<Skill, DevelopmentCostGroup> getSkillCosts() {
		return skillCosts;
	}
	public void setSkillCosts(Map<Skill, DevelopmentCostGroup> skillCosts) {
		this.skillCosts = skillCosts;
	}
	public Map<SkillCategory, List<DevelopmentCostGroup>> getAssignableSkillCostsMap() {
		return assignableSkillCostsMap;
	}
	public void setAssignableSkillCostsMap(Map<SkillCategory, List<DevelopmentCostGroup>> assignableSkillCostsMap) {
		this.assignableSkillCostsMap = assignableSkillCostsMap;
	}
	public List<SkillCategory> getProfessionalSkillCategories() {
		return professionalSkillCategories;
	}
}
