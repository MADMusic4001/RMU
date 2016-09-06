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

import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.SkillCost;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class for associating SkillCost to Skill
 */
public class ProfessionSkillCost {
	private Skill                     skill;
	private SkillCost                 skillCost = null;

	public ProfessionSkillCost(@NonNull Skill skill, SkillCost skillCost) {
		this.skill = skill;
		this.skillCost = skillCost;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("skill", skill)
				.append("skillCost", skillCost)
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

		ProfessionSkillCost that = (ProfessionSkillCost) o;

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
	public SkillCost getSkillCost() {
		return skillCost;
	}
	public void setSkillCost(SkillCost skillCost) {
		this.skillCost = skillCost;
	}
}
