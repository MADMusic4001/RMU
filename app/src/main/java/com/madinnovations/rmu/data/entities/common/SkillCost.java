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
	private short firstCost = 0;
	private short additionalCost = 0;

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

		return firstCost == skillCost.firstCost && additionalCost == skillCost.additionalCost;
	}

	@Override
	public int hashCode() {
		int result = firstCost;
		result = 31 * result + additionalCost;
		return result;
	}

	// Getters and setters
	public short getFirstCost() {
		return firstCost;
	}
	public void setFirstCost(short firstCost) {
		this.firstCost = firstCost;
	}
	public short getAdditionalCost() {
		return additionalCost;
	}
	public void setAdditionalCost(short additionalCost) {
		this.additionalCost = additionalCost;
	}
}
