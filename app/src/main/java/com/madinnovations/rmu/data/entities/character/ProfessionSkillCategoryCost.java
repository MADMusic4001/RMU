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

import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.SkillCost;

/**
 * Container class for associating SkillCost to SkillCategory
 */
public class ProfessionSkillCategoryCost {
	private int id = -1;
	private Profession profession = null;
	private SkillCategory skillCategory = null;
	private SkillCost skillCost = null;

	public ProfessionSkillCategoryCost() {
	}

	public ProfessionSkillCategoryCost(Profession profession, SkillCategory skillCategory, SkillCost skillCost) {
		this.profession = profession;
		this.skillCategory = skillCategory;
		this.skillCost = skillCost;
	}

	/**
	 * Checks the validity of the ProfessionSkillCategoryCost instance.
	 *
	 * @return true if the ProfessionSkillCategoryCost instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return profession != null && skillCategory != null && skillCost != null &&
				skillCost.getFirstCost() <= skillCost.getAdditionalCost();
	}

	@Override
	public String toString() {
		return "ProfessionSkillCategoryCost{" +
				"id=" + id +
				", profession=" + profession +
				", skillCategory=" + skillCategory +
				", skillCost=" + skillCost +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProfessionSkillCategoryCost that = (ProfessionSkillCategoryCost) o;

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
	public Profession getProfession() {
		return profession;
	}
	public void setProfession(Profession profession) {
		this.profession = profession;
	}
	public SkillCategory getSkillCategory() {
		return skillCategory;
	}
	public void setSkillCategory(SkillCategory skillCategory) {
		this.skillCategory = skillCategory;
	}
	public SkillCost getSkillCost() {
		return skillCost;
	}
	public void setSkillCost(SkillCost skillCost) {
		this.skillCost = skillCost;
	}
}
