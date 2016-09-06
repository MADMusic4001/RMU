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
 * Cost of improving a skill
 */
public class SkillCost {
	private Short firstCost = null;
	private Short additionalCost = null;

	/**
	 * Checks the validity of the SkillCost instance.
	 *
	 * @return true if the SkillCost instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return true;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("firstCost", firstCost)
				.append("additionalCost", additionalCost)
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

		SkillCost skillCost = (SkillCost) o;

		return getFirstCost() != null ? getFirstCost().equals(skillCost.getFirstCost())
									  : skillCost.getFirstCost() == null && (getAdditionalCost() != null
																			 ? getAdditionalCost().equals(
											  skillCost.getAdditionalCost()) : skillCost.getAdditionalCost() == null);
	}

	@Override
	public int hashCode() {
		int result = getFirstCost() != null ? getFirstCost().hashCode() : 0;
		result = 31 * result + (getAdditionalCost() != null ? getAdditionalCost().hashCode() : 0);
		return result;
	}

	// Getters and setters
	public Short getFirstCost() {
		return firstCost;
	}
	public void setFirstCost(Short firstCost) {
		this.firstCost = firstCost;
	}
	public Short getAdditionalCost() {
		return additionalCost;
	}
	public void setAdditionalCost(Short additionalCost) {
		this.additionalCost = additionalCost;
	}
}
