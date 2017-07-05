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
package com.madinnovations.rmu.data.entities.spells;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

/**
 * Spell area of effect types.
 */
public enum AreaOfEffect {
	ANIMAL (R.string.enum_aoe_animal, (short)1),
	ANIMAL_PER_LEVEL (R.string.enum_aoe_animal_per_level, (short)2),
	ARMOR (R.string.enum_aoe_armor, (short)1 ),
	ATTACK (R.string.enum_aoe_attack, (short)1),
	ATTACK_PER_LEVEL (R.string.enum_aoe_attack_per_level, (short)2),
	ATTACK_PER_ROUND (R.string.enum_aoe_attack_per_round, (short)2),
	ATTACKS_OR_MISSILES(R.string.enum_aoe_attacks_or_missiles, (short)1),
	ATTACKS_OR_MISSILES_PER_LEVEL(R.string.enum_aoe_attacks_or_missiles_per_level, (short)2),
	BEAM (R.string.enum_aoe_beam, (short)1),
	BEING (R.string.enum_aoe_being, (short)1),
	BODY_OF_WATER (R.string.enum_aoe_body_of_water, (short)1),
	CABIN (R.string.enum_aoe_cabin, (short)1),
	CASTER (R.string.enum_aoe_caster, (short)0),
	CASTER_PLUS_DEMON (R.string.enum_aoe_caster_plus_demon, (short)0),
	CASTER_PLUS_TARGET (R.string.enum_aoe_caster_plus_target, (short)1),
	CHANNEL (R.string.enum_aoe_channel, (short)1),
	CONDITION_PER_LEVEL (R.string.enum_aoe_condition, (short)2),
	CONE_DIAMETER (R.string.enum_aoe_cone_diameter, (short)3),
	CONE_RADIUS (R.string.enum_aoe_cone_radius, (short)3),
	CORPSE (R.string.enum_aoe_corpse, (short)1),
	CREATURE (R.string.enum_aoe_creature, (short)1),
	CUBE (R.string.enum_aoe_cube, (short)3),
	CUBE_PER_LEVEL_DEPTH (R.string.enum_aoe_cube_per_level_depth, (short)3),
	CUBIC_FOOT_PER_LEVEL (R.string.enum_aoe_cubic_feet_per_level, (short)2),
	CURSE (R.string.enum_aoe_curse, (short)1),
	CYLINDER (R.string.enum_aoe_cylinder, (short)2),
	CYLINDER_PER_LEVEL (R.string.enum_aoe_cylinder_per_level, (short)2),
	DEITY (R.string.enum_aoe_deity, (short)1),
	DEMON (R.string.enum_aoe_demon, (short)1),
	DOOR (R.string.enum_aoe_door, (short)1),
	EARTH_CU_FT (R.string.enum_aoe_earth_cubic_feet, (short)2),
	ELEMENTAL (R.string.enum_aoe_elemental, (short)1),
	EMBLEM (R.string.enum_aoe_emblem, (short)1),
	ENTITY (R.string.enum_aoe_entity, (short)1),
	FAMILIAR (R.string.enum_aoe_familiar, (short)1),
	FOOT (R.string.enum_aoe_foot, (short)1),
	FOOT_PER_LEVEL (R.string.enum_aoe_foot_per_level, (short)2),
	FUNGUS (R.string.enum_aoe_fungus, (short)1),
	GARMENT(R.string.enum_aoe_garment, (short)1),
	GATEWAY (R.string.enum_aoe_gateway, (short)1),
	HERB (R.string.enum_aoe_herb, (short)1),
	HERB_PER_LEVEL (R.string.enum_aoe_herb_per_level, (short)2),
	ILLUSION (R.string.enum_aoe_illusion, (short)1),
	INANIMATE_SOLID (R.string.enum_aoe_inanimate_solid, (short)1),
	INORGANIC_OBJECT (R.string.enum_aoe_inorganic_object, (short)1),
	INSTRUMENT (R.string.enum_aoe_instrument, (short)1),
	ITEM (R.string.enum_aoe_item, (short)1),
	LIMB (R.string.enum_aoe_limb, (short)1),
	LOCATION (R.string.enum_aoe_location, (short)1),
	LOCK (R.string.enum_aoe_lock, (short)1),
	MACHINE (R.string.enum_aoe_machine, (short)1),
	MECHANISM (R.string.enum_aoe_mechanism, (short)1),
	METAL (R.string.enum_aoe_metal, (short)1),
	METAL_CU_FT (R.string.enum_aoe_metal_cu_ft, (short)1),
	MILE (R.string.enum_aoe_mile, (short)1),
	MILE_PER_LEVEL (R.string.enum_aoe_miles_per_level, (short)2),
	MISSILE (R.string.enum_aoe_missile, (short)1),
	MISSILE_PER_LEVEL (R.string.enum_aoe_missiles_per_level, (short)2),
	MOUNT (R.string.enum_aoe_mount, (short)1),
	MOUNT_PER_CASTER (R.string.enum_aoe_mount_per_caster, (short)1),
	OBJECT (R.string.enum_aoe_object, (short)1),
	OBJECT_PER_LEVEL (R.string.enum_aoe_objects_per_level, (short)2),
	OBJECT_PER_ROUND (R.string.enum_aoe_objects_Per_round, (short)2),
	PARRY_PER_ROUND (R.string.enum_aoe_parry_per_round, (short)2),
	PLANE (R.string.enum_aoe_plane, (short)2),
	PLANE_SQ_FT (R.string.enum_aoe_plane_sq_ft, (short)1),
	PLANT (R.string.enum_aoe_plant, (short)1),
	PLANT_PER_LEVEL (R.string.enum_aoe_plants_per_level, (short)2),
	POOL (R.string.enum_aoe_pool, (short)1),
	POINT (R.string.enum_aoe_point, (short)1),
	POISON (R.string.enum_aoe_poison, (short)1),
	POSSESSOR (R.string.enum_aoe_possessor, (short)1),
	POUND (R.string.enum_aoe_pound, (short)1),
	POUND_PER_LEVEL (R.string.enum_aoe_pounds_per_level, (short)2),
	RADIUS_FEET(R.string.enum_aoe_radius_feet, (short)1),
	RADIUS_FEET_PER_LEVEL(R.string.enum_aoe_radius_feet_per_level, (short)1),
	RUNE (R.string.enum_aoe_rune, (short)1),
	SEED (R.string.enum_aoe_seed, (short)1),
	SHADOW_PER_LEVEL (R.string.enum_aoe_shadow_per_level, (short)2),
	SHIELD (R.string.enum_aoe_shield, (short)1),
	SHIELD_PER_LEVEL (R.string.enum_aoe_shields_per_level, (short)2),
	SPECIAL (R.string.enum_aoe_special, (short)0),
	SPECIES (R.string.enum_aoe_species, (short)1),
	SPELL (R.string.enum_aoe_spell, (short)1),
	SPELL_AREA_EFFECT (R.string.enum_aoe_spell_area_effect, (short)1),
	SPELL_DIRECTED (R.string.enum_aoe_spell_directed, (short)1),
	SPELL_PER_TARGET (R.string.enum_aoe_spell_per_target, (short)2),
	STONE (R.string.enum_aoe_stone, (short)1),
	STONE_CU_FT (R.string.enum_aoe_stone_cubic_feet, (short)2),
	STONE_CU_FT_PER_LEVEL (R.string.enum_aoe_stone_cubic_feet_per_level, (short)2),
	SYMBOL (R.string.enum_aoe_symbol, (short)1),
	TARGET (R.string.enum_aoe_target, (short)1),
	TARGET_PER_LEVEL (R.string.enum_aoe_targets_per_level, (short)2),
	TARGET_PER_ROUND (R.string.enum_aoe_targets_per_round, (short)2),
	TARGET_PLUS_RADIUS (R.string.enum_aoe_target_plus_radius, (short)2),
	TARGETS_UNLIMITED (R.string.enum_aoe_targets_unlimited, (short)0),
	TENT (R.string.enum_aoe_tent, (short)1),
	TEXT (R.string.enum_aoe_text, (short)1),
	TOWER (R.string.enum_aoe_tower, (short)1),
	TRACKS (R.string.enum_aoe_tracks, (short)1),
	TRANSFERENCE (R.string.enum_aoe_transference, (short)1),
	TRAP (R.string.enum_aoe_trap, (short)1),
	TREE (R.string.enum_aoe_tree, (short)1),
	UNDEAD (R.string.enum_aoe_undead, (short)1),
	VARIES (R.string.enum_aoe_varies, (short)0),
	WALL_WEDGE (R.string.enum_aoe_wall_wedge, (short)4),
	WATER_CORRIDOR (R.string.enum_aoe_water_corridor, (short)3),
	WATER_CORRIDOR_PER_LEVEL (R.string.enum_aoe_water_corridor_per_level, (short)3),
	WATER_CU_FT (R.string.enum_aoe_water_cubic_feet, (short)1),
	WATER_FL_OZ (R.string.enum_aoe_water_fluid_ounces, (short)1),
	WATER_GALLON (R.string.enum_aoe_water_gallons, (short)1),
	WAVE_PER_LEVEL (R.string.enum_aoe_wave_per_level, (short)2),
	WEAPON (R.string.enum_aoe_weapon, (short)1),
	WOOD (R.string.enum_aoe_wood, (short)1),
	WOOD_OBJECT (R.string.enum_aoe_wood_object, (short)1),
	NA (R.string.enum_aoe_na, (short)0);

	private @StringRes int textResourceId;
	private short parameterCount;

	AreaOfEffect(@StringRes int textResourceId, short parameterCount) {
		this.textResourceId = textResourceId;
		this.parameterCount = parameterCount;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(textResourceId);
	}

	// Getters
	public int getTextResourceId() {
		return textResourceId;
	}
	public short getParameterCount() {
		return parameterCount;
	}
}
