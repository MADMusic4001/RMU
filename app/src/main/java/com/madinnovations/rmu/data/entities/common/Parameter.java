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
public enum Parameter {
	ARMOR_PENALTY("Armor Penalty"),
	ARMOR_TYPE("Armor Type"),
	ATTACK("Attack"),
	ATTACK_SIZE("Attack Size"),
	BASE_HITS("Base Hit Points"),
	BASE_MOVEMENT_RATE("Base Movement Rate"),
	BLEED("Bleed"),
	BONUS("Bonus"),
	CONE("Cone"),
	CONE_PARAMETER_INCREASE("Cone Parameter Increase"),
	CRITICAL_SEVERITY("Critical Severity"),
	CRITICAL_TYPE("Critical Type"),
	DEFENSIVE_BONUS("Defensive Bonus"),
	DEFENSIVE_SIZE("Defensive Size"),
	DISEASE("Disease"),
	DURATION("Duration"),
	Effect("Effect"),
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
	INTERVAL("Interval"),
	LIGHTING_PENALTY("Lighting Penalty (Vision)"),
	MAGICAL_RR("Magical Resistance Roll"),
	MANEUVER_ROLL("Maneuver Roll"),
	NUMBER("Number"),
	OFFENSIVE_BONUS("Offensive Bonus"),
	OFF_HAND_USAGE_PENALTY("Off-hand Usage Penalty"),
	PACE_LEVEL_INCREASE("Pace Level Increase"),
	PAIN_PENALTY("Pain Penalty"),
	PENALTY("Penalty"),
	PERCENT("Percent"),
	PHYSICAL_RR("Physical Resistance Roll"),
	POISON("Poison"),
	RADIUS("Radius"),
	RANGE("Range"),
	RANGE_PENALTY("Range Penalty"),
	RANK_BONUS("Rank Bonus"),
	RECOVERY_RATE("HP Recovery Rate"),
	RECOVERY_RATE_MULT("HP Recovery Rate Multiplier"),
	SIZE_DIFFERENCE("Size Difference"),
	SKILL("Skill"),
	SPECIAL("Special"),
	SPECIALIZATION("Specialization"),
	SPELL("Spell"),
	SPELL_DURATION("Spell Duration"),
	SPELL_FAILURE("Spell Failure"),
	SPELL_RADIUS("Spell Radius"),
	SPELL_RANGE("Spell Range"),
	STAT("Stat"),
	TERRAIN("Terrain"),
	USES("Uses"),
	VISION_RANGE("Vision Range");

	private String text;

	Parameter(String text) {
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
