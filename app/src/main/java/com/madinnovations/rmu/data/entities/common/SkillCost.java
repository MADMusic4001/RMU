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

/**
 * Cost of improving a skill
 */
public class SkillCost {
	private int id = -1;
	private int initialCost;
	private int additionalCost;

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
		return "SkillCost{" +
				"id=" + id +
				", initialCost=" + initialCost +
				", additionalCost=" + additionalCost +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SkillCost skillCost = (SkillCost) o;

		return id == skillCost.id;

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
	public int getInitialCost() {
		return initialCost;
	}
	public void setInitialCost(int initialCost) {
		this.initialCost = initialCost;
	}
	public int getAdditionalCost() {
		return additionalCost;
	}
	public void setAdditionalCost(int additionalCost) {
		this.additionalCost = additionalCost;
	}
}
