/**
 * Copyright (C) 2016 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.entities.common;

import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.entities.spells.SpellList;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Used by SkillRanksAdapter
 */
public class SkillRanks implements Comparable {
	private Skill          skill = null;
	private Specialization specialization = null;
	private SpellList      spellList = null;

	@Override
	public String toString() {
		String result = null;

		if(skill != null) {
			result = skill.getName();
		}
		else if (specialization != null){
			result = specialization.getName();
		}
		else if (spellList != null) {
			result = spellList.toString();
		}

		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SkillRanks that = (SkillRanks) o;

		if (getSkill() != null ? !getSkill().equals(that.getSkill()) : that.getSkill() != null) {
			return false;
		}
		if (getSpecialization() != null ? !getSpecialization().equals(that.getSpecialization())
										: that.getSpecialization() != null) {
			return false;
		}
		return spellList != null ? spellList.equals(that.spellList) : that.spellList == null;
	}

	@Override
	public int hashCode() {
		int result = getSkill() != null ? getSkill().hashCode() : 0;
		result = 31 * result + (getSpecialization() != null ? getSpecialization().hashCode() : 0);
		result = 31 * result + (spellList != null ? spellList.hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(@NonNull Object o) {
		int result = 0;
		SkillRanks otherSkillRanks;
		String thisName = null;
		String otherName = null;

		if(skill != null) {
			thisName = skill.getName();
		}
		else if(specialization != null) {
			thisName = specialization.getName();
		}
		else if(spellList != null) {
			thisName = spellList.getName();
		}

		if(o instanceof SkillRanks) {
			otherSkillRanks = (SkillRanks) o;

			if(otherSkillRanks.getSkill() != null) {
				otherName = otherSkillRanks.getSkill().getName();
			}
			else if(otherSkillRanks.getSpecialization() != null) {
				otherName = otherSkillRanks.getSpecialization().getName();
			}
			else if(otherSkillRanks.getSpellList() != null) {
				otherName = otherSkillRanks.getSpellList().getName();
			}

			if (thisName == null) {
				if (otherName != null) {
					result = 1;
				}
			}
			else if (otherName == null) {
				result = -1;
			}
			else {
				result = thisName.compareTo(otherName);
			}
		}
		else {
			result = -1;
		}

		return result;
	}

	@SuppressWarnings("unused")
	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("skill", skill)
				.append("specialization", specialization)
				.toString();
	}

	// Getters and setters
	public Skill getSkill() {
		return skill;
	}
	public void setSkill(Skill skill) {
		this.skill = skill;
	}
	public Specialization getSpecialization() {
		return specialization;
	}
	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}
	public SpellList getSpellList() {
		return spellList;
	}
	public void setSpellList(SpellList spellList) {
		this.spellList = spellList;
	}
}
