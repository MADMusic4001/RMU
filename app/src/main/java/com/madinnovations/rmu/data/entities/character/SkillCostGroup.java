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
import com.madinnovations.rmu.data.entities.common.Skill;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Container class for associating DevelopmentCostGroup to Skill
 */
public class SkillCostGroup implements Comparable {
	private Skill                skill;
	private DevelopmentCostGroup costGroup = null;

	/**
	 * Creates a new SkillCostEntry instance will the given values.
	 *
	 * @param skill  a {@link Skill} instance
	 * @param costGroup  a {@link DevelopmentCostGroup} instance
	 */
	public SkillCostGroup(@NonNull Skill skill, DevelopmentCostGroup costGroup) {
		this.skill = skill;
		this.costGroup = costGroup;
	}

	@Override
	public int compareTo(@NonNull Object o) {
		int result;

		if(!(o instanceof SkillCostGroup)) {
			result = 1;
		}
		else {
			SkillCostGroup o2 = (SkillCostGroup)o;
			if(this.getSkill() != null) {
				result = this.getSkill().getName().compareTo(o2.getSkill().getName());
			}
			else if (o2.getSkill() != null) {
				result = 1;
			}
			else {
				if(this.getCostGroup() != null) {
					result = this.getCostGroup().compareTo(o2.getCostGroup());
				}
				else if(o2.getCostGroup() != null) {
					result = -1;
				}
				else {
					result = 0;
				}
			}
		}

		return result;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("skill", skill)
				.append("costGroup", costGroup)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SkillCostGroup that = (SkillCostGroup) o;

		return getSkill().equals(that.getSkill());
	}

	@Override
	public int hashCode() {
		return getSkill().hashCode();
	}

	// Getters and setters
	public Skill getSkill() {
		return skill;
	}
	public void setSkill(Skill skill) {
		this.skill = skill;
	}
	public DevelopmentCostGroup getCostGroup() {
		return costGroup;
	}
	public void setCostGroup(DevelopmentCostGroup costGroup) {
		this.costGroup = costGroup;
	}
}
