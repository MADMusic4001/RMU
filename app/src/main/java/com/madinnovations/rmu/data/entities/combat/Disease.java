/**
 * Copyright (C) 2017 MadInnovations
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
package com.madinnovations.rmu.data.entities.combat;

import com.madinnovations.rmu.data.entities.DatabaseObject;

/**
 * Disease attributes
 */
public class Disease extends DatabaseObject {
	public static final String JSON_NAME = "Diseases";
	private String   name;
	private Severity severity;
	private Short    minDurationDays;
	private Short    maxDurationDays;
	private String   effects;

	/**
	 * Creates a new Disease instance.
	 */
	public Disease() {
	}

	/**
	 * Creates a new Disease instance with the given id.
	 *
	 * @param id  the id of the new instance
	 */
	public Disease(int id) {
		super(id);
	}

	/**
	 * Checks the validity of the Disease instance.
	 *
	 * @return true if the Disease instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && severity != null && effects != null && !effects.isEmpty();
	}

	// Getters and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Severity getSeverity() {
		return severity;
	}
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	public Short getMinDurationDays() {
		return minDurationDays;
	}
	public void setMinDurationDays(Short minDurationDays) {
		this.minDurationDays = minDurationDays;
	}
	public Short getMaxDurationDays() {
		return maxDurationDays;
	}
	public void setMaxDurationDays(Short maxDurationDays) {
		this.maxDurationDays = maxDurationDays;
	}
	public String getEffects() {
		return effects;
	}
	public void setEffects(String effects) {
		this.effects = effects;
	}
}
