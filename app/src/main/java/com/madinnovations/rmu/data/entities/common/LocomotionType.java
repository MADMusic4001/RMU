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
 * Representation of a method of locomotion for creatures and characters.
 */
public class LocomotionType {
	private int id = -1;
	private String name = null;
	private String description = null;
	private int defaultRate = 20;

	/**
	 * Creates a new LocomotionType instance
	 */
	public LocomotionType() {
	}

	/**
	 * Creates a new LocomotionType instance with the given id
	 *
	 * @param id  the id for the new instance
	 */
	public LocomotionType(int id) {
		this.id = id;
	}

	/**
	 * Checks the validity of the LocomotionType instance.
	 *
	 * @return true if the LocomotionType instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", id)
				.append("name", name)
				.append("description", description)
				.append("defaultRate", defaultRate)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LocomotionType that = (LocomotionType) o;

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
	public int getDefaultRate() {
		return defaultRate;
	}
	public void setDefaultRate(int defaultRate) {
		this.defaultRate = defaultRate;
	}
}
