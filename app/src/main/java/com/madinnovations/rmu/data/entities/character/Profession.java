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

import java.util.List;

/**
 * Profession attributes
 */
public class Profession {
	private int id = -1;
	private String name;
	private String description;
	private List<ProfessionSkillCategoryCost> professionSkillCategoryCosts;

	/**
	 * Checks the validity of the Profession instance.
	 *
	 * @return true if the Profession instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty();
	}

	@Override
	public String toString() {
		return "Profession{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", skillCategoryCosts=" + professionSkillCategoryCosts +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Profession that = (Profession) o;

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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<ProfessionSkillCategoryCost> getProfessionSkillCategoryCosts() {
		return professionSkillCategoryCosts;
	}
	public void setProfessionSkillCategoryCosts(List<ProfessionSkillCategoryCost> professionSkillCategoryCosts) {
		this.professionSkillCategoryCosts = professionSkillCategoryCosts;
	}
}
