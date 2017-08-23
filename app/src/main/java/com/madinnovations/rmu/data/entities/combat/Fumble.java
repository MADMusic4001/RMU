/*
 *
 *   Copyright (C) 2017 MadInnovations
 *   <p/>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   <p/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   <p/>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *
 */
package com.madinnovations.rmu.data.entities.combat;

import com.madinnovations.rmu.data.entities.DatabaseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Fumble attributes
 */
public class Fumble extends DatabaseObject {
	public static final String JSON_NAME = "Fumbles";
	private int minRoll;
	private int maxRoll;
	private String attackTypeName;
	private String description;
	private List<AdditionalEffect> additionalEffects = new ArrayList<>();

	/**
	 * Creates a new Fumble instance with all values set to defaults
	 */
	public Fumble() {
	}

	/**
	 * Creates a new Fumble instance with the given id and the rest of the fields set to defaults.
	 */
	public Fumble(int id) {
		super(id);
	}

	// Getters and setters
	public int getMinRoll() {
		return minRoll;
	}
	public void setMinRoll(int minRoll) {
		this.minRoll = minRoll;
	}
	public int getMaxRoll() {
		return maxRoll;
	}
	public void setMaxRoll(int maxRoll) {
		this.maxRoll = maxRoll;
	}
	public String getAttackTypeName() {
		return attackTypeName;
	}
	public void setAttackTypeName(String attackTypeName) {
		this.attackTypeName = attackTypeName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<AdditionalEffect> getAdditionalEffects() {
		return additionalEffects;
	}
}
