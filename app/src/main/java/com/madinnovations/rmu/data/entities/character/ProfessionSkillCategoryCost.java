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

import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.SkillCost;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class for associating SkillCost to SkillCategory
 */
public class ProfessionSkillCategoryCost implements Comparable {
	private SkillCategory skillCategory;
	private List<SkillCostEntry> skillCosts           = new ArrayList<>();
	private SkillCost            skillCategoryCost    = new SkillCost();
	private boolean              assignable           = false;
	private List<SkillCost>      assignableSkillCosts = new ArrayList<>();

	/**
	 * Creates a new ProfessionSkillCategoryCost instance
	 *
	 * @param skillCategory  a SkillCategory instance
	 * @param skillCosts  a list of Skill costs for the skills in this category
	 * @param skillCategoryCost  the development point costs for this SkillCategory
	 * @param assignable  true if characters can assign each skill in the category a different cost
	 * @param assignableSkillCosts  the list of SkillCosts to be assigned to individual skills when assignable is true
	 */
	public ProfessionSkillCategoryCost(@NonNull SkillCategory skillCategory,
									   @NonNull List<SkillCostEntry> skillCosts,
									   @NonNull SkillCost skillCategoryCost,
									   boolean assignable,
									   @NonNull List<SkillCost> assignableSkillCosts) {
		this.skillCategory = skillCategory;
		this.skillCosts = skillCosts;
		this.skillCategoryCost = skillCategoryCost;
		this.assignable = assignable;
		this.assignableSkillCosts = assignableSkillCosts;
	}

	@Override
	public int compareTo(@NonNull Object o) {
		int result;

		if(!(o instanceof ProfessionSkillCategoryCost)) {
			result = 1;
		}
		else {
			ProfessionSkillCategoryCost o2 = (ProfessionSkillCategoryCost)o;
			result = this.skillCategory.compareTo(o2.skillCategory);
			if(result == 0) {
				if(this.skillCategory == null) {
					if(o2.skillCategory != null) {
						result = -1;
					}
				}
				else if(o2.skillCategory == null) {
					result = 1;
				}
				else {
					result = this.skillCategory.getName().compareTo(o2.skillCategory.getName());
				}
			}
		}

		return result;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this,
								   ToStringStyle.MULTI_LINE_STYLE)
				.append("skillCategory", skillCategory)
				.append("skillCosts", skillCosts)
				.append("skillCategoryCost", skillCategoryCost)
				.append("assignable", assignable)
				.append("assignableSkillCosts", assignableSkillCosts)
				.toString();
	}

	// Getters and setters
	public SkillCategory getSkillCategory() {
		return skillCategory;
	}
	public void setSkillCategory(SkillCategory skillCategory) {
		this.skillCategory = skillCategory;
	}
	public List<SkillCostEntry> getSkillCosts() {
		return skillCosts;
	}
	public SkillCost getSkillCategoryCost() {
		return skillCategoryCost;
	}
	public void setSkillCategoryCost(SkillCost skillCategoryCost) {
		this.skillCategoryCost = skillCategoryCost;
	}
	public boolean isAssignable() {
		return assignable;
	}
	public void setAssignable(boolean assignable) {
		this.assignable = assignable;
	}
	public List<SkillCost> getAssignableSkillCosts() {
		return assignableSkillCosts;
	}
}
