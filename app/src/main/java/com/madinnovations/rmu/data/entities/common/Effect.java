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
 * Adjustable Attributes
 */
public enum Effect {
	ACTION_POINTS("Action Points"),
	ARMOR_PENALTY("Armor Penalty"),
	ARMOR_TYPE("Armor Type"),
	ATTACK_SIZE("Attack Size"),
	BASE_HITS("Base Hit Points"),
	BASE_MOVEMENT_RATE("Base Movement Rate"),
	DEFENSIVE_BONUS("Defensive Bonus"),
	DEFENSIVE_SIZE("Defensive Size"),
	ELEMENTAL_RR("Elemental Resistance Roll"),
	ELEMENTAL_DB("Elemental Defensive Bonus"),
	ELEMENTAL_ENDURANCE("Elemental Endurance"),
	ENCUMBRANCE_PENALTY("Encumbrance Penalty"),
	ENDURANCE("Endurance"),
	FATIGUE_PENALTY("Fatigue Penalty"),
	FEAR_RR("Fear Resistance Roll"),
	FOLLOWER_FEAR_RR("Followers Fear Resistance Roll"),
	FUMBLE_RANGE("Fumble Range"),
	INITIATIVE("Initiative"),
	INJURY_PENALTY("Injury Penalty"),
	LIGHTING_PENALTY("Lighting Penalty (Vision)"),
	MAGICAL_RR("Magical Resistance Roll"),
	MANEUVER_ROLL("Maneuver Roll"),
	OFFENSIVE_BONUS("Offensive Bonus"),
	OFF_HAND_USAGE_PENALTY("Off-hand Usage Penalty"),
	OPPONENT_FUMBLE_RANGE("Opponent Fumble Range"),
	PAIN_PENALTY("Pain Penalty"),
	PHYSICAL_RR("Physical Resistance Roll"),
	RANGE_PENALTY("Range Penalty"),
	RECOVERY_RATE("HP Recovery Rate"),
	RECOVERY_RATE_MULT("HP Recovery Rate Multiplier"),
	SKILL_BONUS("Skill Bonus"),
	SPECIAL("Special"),
	SPELL_DUARATION("Spell Duration"),
	SPELL_FAILURE("Spell Failure"),
	SPELL_RADIUS("Spell Radius"),
	SPELL_RANGE("Spell Range"),
	STAT_BONUS("Stat Bonus"),
	VISION_RANGE("Vision Range");

	private String text;

	Effect(String text) {
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
