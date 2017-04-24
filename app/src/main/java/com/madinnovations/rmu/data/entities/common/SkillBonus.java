/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.entities.common;

import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.entities.spells.SpellList;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Skill bonus attributes
 */
public class SkillBonus implements Comparable {
	public static final String         JSON_NAME      = "SkillBonuses";
	private             Skill          skill          = null;
	private             Specialization specialization = null;
	private             SpellList      spellList      = null;
	private             short          bonus          = 0;

	/**
	 * Creates a new SkillBonus instance
	 */
	public SkillBonus() {
	}

	/**
	 * Creates a new SkillBonus with the given attribute values
	 *
	 * @param skill  a Skill instance to set the skill value for the new SkillBonus instance
	 * @param specialization  a Specialization instance to set the specialization value for the new SkillBonus instance
	 * @param spellList  a SpellList instance to set the spellList value for the new SkillBonus instance
	 * @param bonus  a short to set the bonus value for the new SkillBonus instance
	 */
	public SkillBonus(Skill skill, Specialization specialization, SpellList spellList, short bonus) {
		this.skill = skill;
		this.specialization = specialization;
		this.spellList = spellList;
		this.bonus = bonus;
	}

	/**
	 * Creates a new SkillBonus instance by copying the member variables from another instance
	 *
	 * @param other  a SkillBonus instance to copy
	 */
	public SkillBonus(SkillBonus other) {
		this.skill = other.skill;
		this.specialization = other.specialization;
		this.spellList = other.spellList;
		this.bonus = other.bonus;
	}

	@Override
	public String toString() {
		String result = null;
		if(skill != null) {
			result = skill.getName();
		}
		else if(specialization != null) {
			result = specialization.getName();
		}
		else if(spellList != null) {
			result = spellList.getName();
		}

		return result;
	}

	/**
	 * Creates a debug string containing the values of the member variables for this instance.
	 *
	 * @return  a debug string.
	 */
	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("skill", skill)
				.append("specialization", specialization)
				.append("spellList", spellList)
				.append("bonus", bonus)
				.toString();
	}

	@SuppressWarnings("SimplifiableIfStatement")
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SkillBonus that = (SkillBonus) o;

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

	// Getters and setters
	public Skill getSkill() {
		return skill;
	}
	public void setSkill(Skill talent) {
		this.skill = talent;
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
	public short getBonus() {
		return bonus;
	}
	public void setBonus(short bonus) {
		this.bonus = bonus;
	}
}
