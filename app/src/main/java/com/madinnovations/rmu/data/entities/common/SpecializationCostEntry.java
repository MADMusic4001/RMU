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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Container class for associating SkillCost to Specialization
 */
public class SpecializationCostEntry {
	private Specialization            specialization;
	private SkillCost                 skillCost = null;

	public SpecializationCostEntry(@NonNull Specialization specialization, SkillCost skillCost) {
		this.specialization = specialization;
		this.skillCost = skillCost;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("specialization", specialization)
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

		SpecializationCostEntry that = (SpecializationCostEntry) o;

		return getSpecialization().equals(that.getSpecialization());
	}

	@Override
	public int hashCode() {
		return getSpecialization().hashCode();
	}

	// Getters and setters
	public Specialization getSpecialization() {
		return specialization;
	}
	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}
	public SkillCost getSkillCost() {
		return skillCost;
	}
	public void setSkillCost(SkillCost skillCost) {
		this.skillCost = skillCost;
	}
}
