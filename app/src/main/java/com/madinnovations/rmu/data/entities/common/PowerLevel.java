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

/**
 * How powerful characters are in the game relative to average NPCs
 */
public enum 	PowerLevel {
	AVERAGE("Average", (short)0, (short)0, (short)0, (short)5, (short)0),
	SUPERIOR("Superior", (short)11, (short)10, (short)5, (short)4, (short)10),
	HEROIC("Heroic", (short)21, (short)20, (short)10, (short)3, (short)20),
	LEGENDARY("Legendary", (short)31, (short)30, (short)15, (short)2, (short)30),
	EPIC("Epic", (short)41, (short)40, (short)20, (short)1, (short)40);

	private String name;
	private short rerollUnder;
	private short professionalKnacks;
	private short maxKnackBonus;
	private short ppRecoveryHours;
	private short statPoints;

	PowerLevel(String name, short rerollUnder, short professionalKnacks, short maxKnackBonus, short ppRecoveryHours,
			   short statPoints) {
		this.name = name;
		this.rerollUnder = rerollUnder;
		this.professionalKnacks = professionalKnacks;
		this.maxKnackBonus = maxKnackBonus;
		this.ppRecoveryHours = ppRecoveryHours;
		this.statPoints = statPoints;
	}

	@Override
	public String toString() {
		return getName();
	}

	// Getters
	public String getName() {
		return name;
	}
	public short getRerollUnder() {
		return rerollUnder;
	}
	public short getProfessionalKnacks() {
		return professionalKnacks;
	}
	public short getMaxKnackBonus() {
		return maxKnackBonus;
	}
	public short getPpRecoveryHours() {
		return ppRecoveryHours;
	}
	public short getStatPoints() {
		return statPoints;
	}
}
