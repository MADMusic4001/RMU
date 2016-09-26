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
package com.madinnovations.rmu.data.entities.common;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Skill bonus attributes
 */
public class SkillBonus {
	public static final String JSON_NAME = "SkillBonuses";
	private Skill skill = null;
	private short bonus = 0;

	/**
	 * Creates a new SkillBonus with the given attribute values
	 *
	 * @param skill  a Skill instance to set the skill value for the new SkillBonus instance
	 * @param bonus  a short to set the bonus value for the new SkillBonus instance
	 */
	public SkillBonus(Skill skill, short bonus) {
		this.skill = skill;
		this.bonus = bonus;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("skill", skill)
				.append("bonus", bonus)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SkillBonus talentBonus = (SkillBonus) o;

		return skill != null ? skill.equals(talentBonus.skill) : talentBonus.skill == null;
	}

	@Override
	public int hashCode() {
		return skill != null ? skill.hashCode() : 0;
	}

	// Getters and setters
	public Skill getSkill() {
		return skill;
	}
	public void setSkill(Skill talent) {
		this.skill = talent;
	}
	public short getBonus() {
		return bonus;
	}
	public void setBonus(short bonus) {
		this.bonus = bonus;
	}
}
