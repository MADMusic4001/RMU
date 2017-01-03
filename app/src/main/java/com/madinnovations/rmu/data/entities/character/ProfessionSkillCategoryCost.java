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

import com.madinnovations.rmu.data.entities.common.DevelopmentCostGroup;
import com.madinnovations.rmu.data.entities.common.SkillCategory;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class for associating DevelopmentCostGroup to SkillCategory
 */
public class ProfessionSkillCategoryCost implements Comparable {
	private SkillCategory skillCategory;
	private List<SkillCostGroup>       skillCostGroups      = new ArrayList<>();
	private DevelopmentCostGroup       costGroup            = null;
	private boolean                    assignable           = false;
	private List<DevelopmentCostGroup> assignableCostGroups = new ArrayList<>();
	private boolean                    professionCat        = false;

	/**
	 * Creates a new ProfessionSkillCategoryCost instance
	 *
	 * @param skillCategory  a SkillCategory instance
	 * @param skillCostGroups  a list of Skill costs for the skills in this category
	 * @param costGroup  the development point costs for this SkillCategory
	 * @param assignable  true if characters can assign each skill in the category a different cost
	 * @param assignableCostGroups  the list of SkillCosts to be assigned to individual skills when assignable is true
	 * @param professionCat  true if this category has skills that can be chosen to receive a profession bonus
	 */
	public ProfessionSkillCategoryCost(@NonNull SkillCategory skillCategory,
									   @NonNull List<SkillCostGroup> skillCostGroups,
									   @NonNull DevelopmentCostGroup costGroup,
									   boolean assignable,
									   @NonNull List<DevelopmentCostGroup> assignableCostGroups,
									   boolean professionCat) {
		this.skillCategory = skillCategory;
		this.skillCostGroups = skillCostGroups;
		this.costGroup = costGroup;
		this.assignable = assignable;
		this.assignableCostGroups = assignableCostGroups;
		this.professionCat = professionCat;
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
				.append("skillCostGroups", skillCostGroups)
				.append("costGroup", costGroup)
				.append("assignable", assignable)
				.append("assignableCostGroups", assignableCostGroups)
				.append("professionCat", professionCat)
				.toString();
	}

	// Getters and setters
	public SkillCategory getSkillCategory() {
		return skillCategory;
	}
	public void setSkillCategory(SkillCategory skillCategory) {
		this.skillCategory = skillCategory;
	}
	public List<SkillCostGroup> getSkillCostGroups() {
		return skillCostGroups;
	}
	public DevelopmentCostGroup getCostGroup() {
		return costGroup;
	}
	public void setCostGroup(DevelopmentCostGroup costGroup) {
		this.costGroup = costGroup;
	}
	public boolean isAssignable() {
		return assignable;
	}
	public void setAssignable(boolean assignable) {
		this.assignable = assignable;
	}
	public List<DevelopmentCostGroup> getAssignableCostGroups() {
		return assignableCostGroups;
	}
	public void setAssignableCostGroups(
			List<DevelopmentCostGroup> assignableCostGroups) {
		this.assignableCostGroups = assignableCostGroups;
	}
	public boolean isProfessionCat() {
		return professionCat;
	}
	public void setProfessionCat(boolean professionCat) {
		this.professionCat = professionCat;
	}
}
