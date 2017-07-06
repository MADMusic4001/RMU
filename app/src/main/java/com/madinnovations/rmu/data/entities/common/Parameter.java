/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.entities.common;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.utils.ReactiveUtils;
import com.madinnovations.rmu.data.entities.combat.Resistance;
import com.madinnovations.rmu.data.entities.creature.Extension;
import com.madinnovations.rmu.data.entities.object.Material;
import com.madinnovations.rmu.data.entities.object.PoisonType;
import com.madinnovations.rmu.data.entities.spells.Element;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.view.RMUApp;

import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.ATTACK_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.COMBAT_TRAINING_SKILL_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.CREATURE_ARCHETYPE_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.CREATURE_CATEGORY_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.CREATURE_TYPE_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.CREATURE_VARIETY_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.CRITICAL_TYPE_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.DISEASE_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.MELEE_ATTACK_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.RANGE_ATTACK_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.SKILL_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.SPECIALIZATION_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.SPELL_LIST_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.SPELL_RX_HANDLER;

/**
 * Adjustable Attributes
 */
public enum Parameter {
	ACTION_POINT_COST(R.string.enum_parameter_action_point_cost, null, null),
	ACTION_POINT_DIFFERENCE(R.string.enum_parameter_action_point_difference, null, null),
	ADDITIONAL_ATTACK(R.string.enum_parameter_additional_attack, null, null),
	ADDITIONAL_REALM(R.string.enum_parameter_additional_realm, null, Realm.values()),
	ARMOR_PENALTY(R.string.enum_parameter_armor_penalty, null, null),
	ARMOR_TYPE(R.string.enum_parameter_armor_type, null, null),
	ATTACK(R.string.enum_parameter_attack, ATTACK_RX_HANDLER, null),
	ATTACK_SIZE(R.string.enum_parameter_attack_size, null, Size.values()),
	ATTACK_SIZE_MODIFIER(R.string.enum_parameter_attack_size_modifier, null, null),
	BASE_HITS(R.string.enum_parameter_base_hits, null, null),
	BASE_MOVEMENT_RATE(R.string.enum_parameter_base_movement_rate, null, null),
	BLEED(R.string.enum_parameter_bleed, null, null),
	BONUS(R.string.enum_parameter_bonus, null, null),
	COMBAT_TRAINING_SKILL(R.string.enum_parameter_combat_training_skill, COMBAT_TRAINING_SKILL_RX_HANDLER, null),
	CONDITION(R.string.enum_parameter_state, null, null),
	CONDITIONAL(R.string.enum_parameter_condition, null, ConditionType.values()),
	CONE(R.string.enum_parameter_cone, null, null),
	CONE_PARAMETER_INCREASE(R.string.enum_parameter_cone_parameter_increase, null, null),
	CREATURE_ARCHETYPE(R.string.enum_parameter_creature_archetype, CREATURE_ARCHETYPE_RX_HANDLER, null),
	CREATURE_CATEGORY(R.string.enum_parameter_creature_category, CREATURE_CATEGORY_RX_HANDLER, null),
	CREATURE_TYPE(R.string.enum_parameter_creature_type, CREATURE_TYPE_RX_HANDLER, null),
	CREATURE_VARIETY(R.string.enum_parameter_creature_variety, CREATURE_VARIETY_RX_HANDLER, null),
	CRITICAL_ROLL(R.string.enum_parameter_critical_roll, null, null),
	CRITICAL_SEVERITY(R.string.enum_parameter_critical_severity, null, null),
	CRITICAL_TYPE(R.string.enum_parameter_critical_type, CRITICAL_TYPE_RX_HANDLER, null),
	DEFENSIVE_BONUS(R.string.enum_parameter_defensive_bonus, null, null),
	DEFENSIVE_SIZE(R.string.enum_parameter_defensive_size, null, Size.values()),
	DESTINY_SENSE(R.string.enum_parameter_destiny_sense, null, DestinySense.values()),
	DICE(R.string.enum_parameter_dice, null, Dice.values()),
	DISEASE(R.string.enum_parameter_disease, DISEASE_RX_HANDLER, null),
	DURATION(R.string.enum_parameter_duration, null, null),
	EFFECT(R.string.enum_parameter_effect, null, null),
	ELEMENT(R.string.enum_parameter_element, null, Element.values()),
	ELEMENTAL_RR(R.string.enum_parameter_elemental_rr, null, Resistance.getElementalResistances()),
	ELEMENTAL_DB(R.string.enum_parameter_elemental_db, null, null),
	ELEMENTAL_ENDURANCE(R.string.enum_parameter_elemental_endurance, null, null),
	EMPATHIC_RANGE(R.string.enum_parameter_empathic_range, null, null),
	ENCUMBRANCE_PENALTY(R.string.enum_parameter_encumbrance_penalty, null, null),
	ENDURANCE(R.string.enum_parameter_endurance, null, null),
	ENTER_STATE(R.string.enum_parameter_enter_state, null, StateType.values()),
	EXTENSION(R.string.enum_parameter_extension, null, Extension.values()),
	FATIGUE_PENALTY(R.string.enum_parameter_fatigue_penalty, null, null),
	FEAR_RR(R.string.enum_parameter_fear_rr, null, Resistance.getFearResistances()),
	FOLLOWER_FEAR_RR(R.string.enum_parameter_follower_fear_rr, null, Resistance.getFearResistances()),
	FUMBLE_RANGE(R.string.enum_parameter_fumble_range, null, null),
	HEIGHT(R.string.enum_parameter_height, null, null),
	HIT_POINTS(R.string.enum_parameter_hit_points, null, null),
	HOURS_REST(R.string.enum_parameter_hours_rest, null, null),
	HP_RECOVERY_RATE(R.string.enum_parameter_recovery_rate, null, null),
	HP_RECOVERY_RATE_MULT(R.string.enum_parameter_recovery_rate_mult, null, null),
	INITIATIVE(R.string.enum_parameter_initiative, null, null),
	INJURY_PENALTY(R.string.enum_parameter_injury_penalty, null, null),
	INTERVAL(R.string.enum_parameter_interval, null, null),
	LEVEL(R.string.enum_parameter_level, null, null),
	LIGHTING_PENALTY(R.string.enum_parameter_lighting_penalty, null, null),
	MAGICAL_RR(R.string.enum_parameter_magical_rr, null, Resistance.getMagicalResistances()),
	MANEUVER_ROLL(R.string.enum_parameter_maneuver_roll, null, null),
	MATERIAL(R.string.enum_parameter_material, null, Material.values()),
	MELEE_ATTACK(R.string.enum_parameter_melee_attack, MELEE_ATTACK_RX_HANDLER, null),
	MOVE(R.string.enum_parameter_move, null, null),
	MULTIPLIER(R.string.enum_parameter_multiplier, null, null),
	NUMBER(R.string.enum_parameter_number, null, null),
	OFFENSIVE_BONUS(R.string.enum_parameter_offensive_bonus, null, null),
	OFF_HAND_USAGE_PENALTY(R.string.enum_parameter_off_hand_usage_penalty, null, null),
	PACE(R.string.enum_parameter_pace, null, Pace.values()),
	PACE_LEVEL_INCREASE(R.string.enum_parameter_pace_level_increase, null, null),
	PAIN_PENALTY(R.string.enum_parameter_pain_penalty, null, null),
	PENALTY(R.string.enum_parameter_penalty, null, null),
	PERCENT(R.string.enum_parameter_percent, null, null),
	PHYSICAL_RR(R.string.enum_parameter_physical_rr, null, Resistance.getPhysicalResistances()),
	POISON(R.string.enum_parameter_poison, null, PoisonType.values()),
	POWER_POINTS(R.string.enum_parameter_power_points, null, null),
	RADIUS(R.string.enum_parameter_radius_feet, null, null),
	RANGE(R.string.enum_parameter_range, null, null),
	RANGE_ATTACK(R.string.enum_parameter_range_attack, RANGE_ATTACK_RX_HANDLER, null),
	RANGE_PENALTY(R.string.enum_parameter_range_penalty, null, null),
	RANK_BONUS(R.string.enum_parameter_rank_bonus, null, null),
	RE_ROLL_CRITICAL(R.string.enum_parameter_re_roll_critical, null, null),
	REALM(R.string.enum_parameter_realm, null, Realm.values()),
	RR_MOD(R.string.enum_parameter_rr_mod, null, null),
	SENSE(R.string.enum_parameter_sense, null, Sense.getNoChoiceSenses()),
	SIZE_DIFFERENCE(R.string.enum_parameter_size_difference, null, null),
	SKILL(R.string.enum_parameter_skill, SKILL_RX_HANDLER, null),
	SPECIAL(R.string.enum_parameter_special, null, null),
	SPECIALIZATION(R.string.enum_parameter_specialization, SPECIALIZATION_RX_HANDLER, null),
	SPELL(R.string.enum_parameter_spell, SPELL_RX_HANDLER, null),
	SPELL_LIST(R.string.enum_parameter_spell_list, SPELL_LIST_RX_HANDLER, null),
	SPELL_CASTING(R.string.enum_parameter_spell_casting, null, null),
	SPELL_CONCENTRATION_DURATION(R.string.enum_parameter_spell_concentration_duration, null, null),
	SPELL_DURATION(R.string.enum_parameter_spell_duration, null, null),
	SPELL_FAILURE(R.string.enum_parameter_spell_failure, null, null),
	SPELL_FAILURE_PP_RECOVERY(R.string.enum_parameter_spell_failure_pp_recovery, null, null),
	SPELL_PREP_ROUNDS(R.string.enum_parameter_spell_prep_rounds, null, null),
	SPELL_RADIUS(R.string.enum_parameter_spell_radius, null, null),
	SPELL_RANGE(R.string.enum_parameter_spell_range, null, null),
	SPELL_TARGETS(R.string.enum_parameter_spell_targets, null, null),
	STAT(R.string.enum_parameter_stat, null, Statistic.getAllStats()),
	TERRAIN(R.string.enum_parameter_terrain, null, Terrain.values()),
	TIME_DAYS(R.string.enum_parameter_time_days, null, null),
	TIME_MINUTES(R.string.enum_parameter_time_minutes, null, null),
	TIME_ROUNDS(R.string.enum_parameter_time_rounds, null, null),
	TOUCH_RANGE(R.string.enum_parameter_touch_range, null, null),
	USES(R.string.enum_parameter_uses, null, null),
	VISION_RANGE(R.string.enum_parameter_vision_range, null, null),
	VOLUME(R.string.enum_parameter_volume, null, null);

	private @StringRes int        textResourceId;
	private ReactiveUtils.Handler handler;
	private Enum<?>[]             enumValues;

	Parameter(int textResourceId, ReactiveUtils.Handler handler, Enum<?>[] enumValues) {
		this.textResourceId = textResourceId;
		this.handler = handler;
		this.enumValues = enumValues;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(textResourceId);
	}

	// Getters
	public int getTextResourceId() {
		return textResourceId;
	}
	public ReactiveUtils.Handler getHandler() {
		return handler;
	}
	public Enum<?>[] getEnumValues() {
		return enumValues;
	}
}
