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
package com.madinnovations.rmu.data.entities.combat;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Critical code attributes
 */
public class CriticalCode {
	public static final String JSON_NAME = "CriticalCodes";
	private int id = -1;
 	private String code = null;
	private String description = null;

	/**
	 * Creates a new CriticalCode instance
	 */
	public CriticalCode() {
	}

	/**
	 * Creates a new CriticalCode instance with the given id
	 *
	 * @param id  the id for the new instance
 	 */
	public CriticalCode(int id) {
		this.id = id;
	}

	/**
	 * Checks the validity of the CriticalCode instance.
	 *
	 * @return true if the CriticalCode instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return code != null && !code.isEmpty() && description != null && !description.isEmpty();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", id)
				.append("code", code)
				.append("description", description)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CriticalCode that = (CriticalCode) o;

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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
