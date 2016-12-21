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

import com.madinnovations.rmu.controller.utils.ReactiveUtils;

import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.*;

/**
 * Adjustable Attributes
 */
public enum Parameter {
	ARMOR_PENALTY("Armor Penalty", null, null),
	ARMOR_TYPE("Armor Type", null, null),
	ATTACK("Attack", null, null),
	ATTACK_SIZE("Attack Size", null, null),
	BASE_HITS("Base Hit Points", null, null),
	BASE_MOVEMENT_RATE("Base Movement Rate", null, null),
	BLEED("Bleed", null, null),
	BONUS("Bonus", null, null),
	CONDITION("Condition", null, null),
	CONE("Cone", null, null),
	CONE_PARAMETER_INCREASE("Cone Parameter Increase", null, null),
	CREATURE_ARCHETYPE("Creature Archetype", CREATURE_ARCHETYPE_RX_HANDLER, null),
	CREATURE_CATEGORY("Creature Category", CREATURE_CATEGORY_RX_HANDLER, null),
	CREATURE_TYPE("Creature Type", CREATURE_TYPE_RX_HANDLER, null),
	CREATURE_VARIETY("Creature Variety", CREATURE_VARIETY_RX_HANDLER, null),
	CRITICAL_ROLL("Critical Roll", null, null),
	CRITICAL_SEVERITY("Critical Severity", null, null),
	CRITICAL_TYPE("Critical Type", null, null),
	DEFENSIVE_BONUS("Defensive Bonus", null, null),
	DEFENSIVE_SIZE("Defensive Size", null, null),
	DISEASE("Disease", null, null),
	DURATION("Duration", null, null),
	EFFECT("Effect", null, null),
	ELEMENTAL_RR("Elemental Resistance Roll", null, null),
	ELEMENTAL_DB("Elemental Defensive Bonus", null, null),
	ELEMENTAL_ENDURANCE("Elemental Endurance", null, null),
	EMPATHIC_RANGE("Empathic Communication Range", null, null),
	ENCUMBRANCE_PENALTY("Encumbrance Penalty", null, null),
	ENDURANCE("Endurance", null, null),
	FATIGUE_PENALTY("Fatigue Penalty", null, null),
	FEAR_RR("Fear Resistance Roll", null, null),
	FOLLOWER_FEAR_RR("Followers Fear Resistance Roll", null, null),
	FUMBLE_RANGE("Fumble Range", null, null),
	HOURS_REST("Hours Rest for PP Recovery", null, null),
	INITIATIVE("Initiative", null, null),
	INJURY_PENALTY("Injury Penalty", null, null),
	INTERVAL("Interval", null, null),
	LIGHTING_PENALTY("Lighting Penalty (Vision)", null, null),
	MAGICAL_RR("Magical Resistance Roll", null, null),
	MANEUVER_ROLL("Maneuver Roll", null, null),
	NUMBER("Number", null, null),
	OFFENSIVE_BONUS("Offensive Bonus", null, null),
	OFF_HAND_USAGE_PENALTY("Off-hand Usage Penalty", null, null),
	PACE_LEVEL_INCREASE("Pace Level Increase", null, null),
	PAIN_PENALTY("Pain Penalty", null, null),
	PENALTY("Penalty", null, null),
	PERCENT("Percent", null, null),
	PHYSICAL_RR("Physical Resistance Roll", null, null),
	POISON("Poison", null, null),
	RADIUS("Radius", null, null),
	RANGE("Range", null, null),
	RANGE_PENALTY("Range Penalty", null, null),
	RANK_BONUS("Rank Bonus", null, null),
	RECOVERY_RATE("HP Recovery Rate", null, null),
	RECOVERY_RATE_MULT("HP Recovery Rate Multiplier", null, null),
	SENSE("Sense", null, null),
	SIZE_DIFFERENCE("Size Difference", null, null),
	SKILL("Skill", null, null),
	SPECIAL("Special", null, null),
	SPECIALIZATION("Specialization", null, null),
	SPELL("Spell", null, null),
	SPELL_LIST("Spell List", null, null),
	SPELL_CASTING("Spell Casting", null, null),
	SPELL_CONCENTRATION_DURATION("Spell Concentration Duration", null, null),
	SPELL_DURATION("Spell Duration", null, null),
	SPELL_FAILURE("Spell Failure", null, null),
	SPELL_PREP_ROUNDS("Spell Preparation Rounds", null, null),
	SPELL_RADIUS("Spell Radius", null, null),
	SPELL_RANGE("Spell Range", null, null),
	STAT("Stat", null, null),
	TERRAIN("Terrain", null, null),
	TOUCH_RANGE("Touch Range", null, null),
	USES("Uses", null, null),
	VISION_RANGE("Vision Range", null, null);

	private String                    text;
	private ReactiveUtils.Handler     handler;
	private Enum<?>                   enumType;

	Parameter(String text, ReactiveUtils.Handler handler, Enum<?> enumType) {
		this.text = text;
		this.handler = handler;
		this.enumType = enumType;
	}

	@Override
	public String toString() {
		return getText();
	}

	// Getters
	public String getText() {
		return text;
	}
	public ReactiveUtils.Handler getHandler() {
		return handler;
	}
	public Enum<?> getEnumType() {
		return enumType;
	}
}
