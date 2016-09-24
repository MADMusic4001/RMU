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
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 9/23/2016.
 */
public enum BonusType {
	CRITICAL_ROLL("Critical Roll"),
	DEFENSIVE_BONUS("DB"),
	ENDURANCE_ROLL("Endurance Roll"),
	INITIATIVE("Initiative"),
	MANEUVER_ROLL("Maneuver Roll"),
	OFFENSIVE_BONUS("OB"),
	RANGE_PENALTY("Range Penalty"),
	RESISTANCE_ROLL("Resistance Roll"),
	SKILL_BONUS("Skill Bonus"),
	SPELL_CASTING_ROLL("SCR"),
	STAT_BONUS("Stat Bonus");

	private String text;

	BonusType(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return getText();
	}

	// Getters
	public String getText() {
		return text;
	}
}
