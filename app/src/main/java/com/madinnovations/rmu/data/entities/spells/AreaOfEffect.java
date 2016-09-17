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
package com.madinnovations.rmu.data.entities.spells;

/**
 * Spell area of effect types.
 */
public enum AreaOfEffect {
	ANIMAL ("Animal (quantity)", (short)1),
	ANIMAL_PER_LEVEL ("Animal/lvl (quantity, levels)", (short)2),
	ARMOR ("Armor (quantity)", (short)1 ),
	ATTACK ("Attack (quantity)", (short)1),
	ATTACK_PER_LEVEL ("Attack/lvl (quantity, levels)", (short)2),
	ATTACK_PER_ROUND ("Attack/rnd (quantity, rounds)", (short)2),
	BEAM ("Beam (length)", (short)1),
	BEING ("Being (quantity)", (short)1),
	BODY_OF_WATER ("Body of Water (quantity)", (short)1),
	CABIN ("Cabin (quantity)", (short)1),
	CASTER ("Caster", (short)0),
	CASTER_PLUS_DEMON ("Caster + Demon", (short)0),
	CASTER_PLUS_TARGET ("Caster + Target (number of targets)", (short)1),
	CHANNEL ("Channel (quantity)", (short)1),
	CONDITION_PER_LEVEL ("Condition (quantity, levels", (short)2),
	CONE_DIAMETER ("Cone (inner diameter, outer diameter, length)", (short)3),
	CONE_RADIUS ("Cone (inner radius, outer radius, length)", (short)3),
	CORPSE ("Corpse (quantity)", (short)1),
	CREATURE ("Creature (quantity)", (short)1),
	CUBE ("Cube (width, height, depth)", (short)3),
	CUBE_PER_LEVEL_DEPTH ("Cube (width, height, depth/lvl)", (short)3),
	CUBIC_FOOT_PER_LEVEL ("Cu'/lvl (cubic feet, levels)", (short)2),
	CURSE ("Curse (quantity)", (short)1),
	CYLINDER ("Cylinder (diameter, length", (short)2),
	CYLINDER_PER_LEVEL ("Cylinder (diameter, length/lvl", (short)2),
	DEITY ("Deity (quantity)", (short)1),
	DEMON ("Demon (quantity)", (short)1),
	DOOR ("Door (quantity)", (short)1),
	EARTH_CU_FT ("Earth (cubic feet)", (short)2),
	ELEMENTAL ("Elemental (quantity)", (short)1),
	EMBLEM ("Emblem (quantity)", (short)1),
	ENTITY ("Entity (quantity)", (short)1),
	FAMILIAR ("Familiar (quantity)", (short)1),
	FOOT ("Foot (length)", (short)1),
	FOOT_PER_LEVEL ("Foot/lvl (length, levels)", (short)2),
	FUNGUS ("Fungus (quantity)", (short)1),
	GATEWAY ("Gateway (quantity)", (short)1),
	HERB ("Herb (quantity)", (short)1),
	HERB_PER_LEVEL ("Herb/lvl (quantity, levels)", (short)2),
	ILLUSION ("Illusion (quantity)", (short)1),
	INANIMATE_SOLID ("Inanimate Solid (cubic feet)", (short)1),
	INORGANIC_OBJECT ("Inorganic Object (cubic feet)", (short)1),
	INSTRUMENT ("Instrument (quantity)", (short)1),
	ITEM ("Item (quantity)", (short)1),
	LIMB ("Limb (quantity)", (short)1),
	LOCATION ("Location (quantity)", (short)1),
	LOCK ("Lock (quantity)", (short)1),
	MACHINE ("Machine (quantity)", (short)1),
	MECHANISM ("Mechanism (quantity)", (short)1),
	METAL ("Metal (max diameter)", (short)1),
	METAL_CU_FT ("Metal (cubic feet)", (short)1),
	MILE ("Mile (distance)", (short)1),
	MILE_PER_LEVEL ("Mile/lvl (distance, levels)", (short)2),
	MISSILE ("Missile (quantity)", (short)1),
	MISSILE_PER_LEVEL ("Missile/lvl (quantity, levels)", (short)2),
	MOUNT ("Mount (quantity)", (short)1),
	MOUNT_PER_CASTER ("Mount/caster (quantity)", (short)1),
	OBJECT ("Object (quantity)", (short)1),
	OBJECT_PER_LEVEL ("Object/lvl (quantity, levels)", (short)2),
	OBJECT_PER_ROUND ("Object/rnd (quantity, rounds)", (short)2),
	PARRY_PER_ROUND ("Parry/rnd (quantity, rounds)", (short)2),
	PLANE ("Plane (width, height)", (short)2),
	PLANE_SQ_FT ("Plane (square feet", (short)1),
	PLANT ("Plant (quantity)", (short)1),
	PLANT_PER_LEVEL ("Plant/lvl (quantity, levels)", (short)2),
	POOL ("Pool (quantity)", (short)1),
	POINT ("Point (quantity)", (short)1),
	POISON ("Poison (quantity)", (short)1),
	POSSESSOR ("Possessor (quantity)", (short)1),
	POUND ("Pound (weight)", (short)1),
	POUND_PER_LEVEL ("Pound/lvl (weight, levels)", (short)2),
	RUNE ("Rune (quantity)", (short)1),
	SEED ("Seed (quantity)", (short)1),
	SHADOW_PER_LEVEL ("Shadow/lvl (quantity, levels)", (short)2),
	SHIELD ("Shield (quantity)", (short)1),
	SHIELD_PER_LEVEL ("Shield/lvl (quantity, levels)", (short)2),
	SPECIAL ("Special", (short)0),
	SPECIES ("Species (quantity)", (short)1),
	SPELL ("Spell (quantity)", (short)1),
	SPELL_AREA_EFFECT ("Spell [Area Effect] (quantity)", (short)1),
	SPELL_DIRECTED ("Spell [Directed] (quantity)", (short)1),
	SPELL_PER_TARGET ("Spell/target (quantity, targets)", (short)2),
	STONE ("Stone (quantity)", (short)1),
	STONE_CU_FT ("Stone (cubic feet)", (short)2),
	STONE_CU_FT_PER_LEVEL ("Stone (cubic feet, levels)", (short)2),
	SYMBOL ("Symbol (quantity)", (short)1),
	TARGET ("Target (quantity)", (short)1),
	TARGET_PER_LEVEL ("Target/lvl (quantity, levels)", (short)2),
	TARGET_PER_ROUND ("Target/rnd (quantity, rounds)", (short)2),
	TARGET_PLUS_RADIUS ("Target + Radius (quantity, radius)", (short)2),
	TARGETS_UNLIMITED ("Targets (Unlimited)", (short)0),
	TENT ("Tent (quantity)", (short)1),
	TEXT ("Text (quantity)", (short)1),
	TOWER ("Tower (quantity)", (short)1),
	TRACKS ("Tracks (quantity)", (short)1),
	TRANSFERENCE ("Transference (quantity)", (short)1),
	TRAP ("Trap (quantity)", (short)1),
	TREE ("Tree (quantity)", (short)1),
	UNDEAD ("Undead (quantity)", (short)1),
	VARIES ("Varies", (short)0),
	WALL_WEDGE ("Wall [Wedge] (width, height, base depth, top depth)", (short)4),
	WATER_CORRIDOR ("Water corridor (diameter, length, max depth)", (short)3),
	WATER_CORRIDOR_PER_LEVEL ("Water corridor (diameter, length/lvl, max depth)", (short)3),
	WATER_CU_FT ("Water (cubic feet)", (short)1),
	WATER_FL_OZ ("Water (fluid ounces)", (short)1),
	WATER_GALLON ("Water (gallons)", (short)1),
	WAVE_PER_LEVEL ("Wave (height/lvl, width/lvl)", (short)2),
	WEAPON ("Weapon (quantity)", (short)1),
	WOOD ("Wood (max diameter)", (short)1),
	WOOD_OBJECT ("Wood Object (quantity)", (short)1),
	NA ("NA", (short)0);

	private String text;
	private short parameterCount;

	AreaOfEffect(String text, short parameterCount) {
		this.text = text;
		this.parameterCount = parameterCount;
	}

	// Getters
	public String getText() {
		return text;
	}
	public short getParameterCount() {
		return parameterCount;
	}
}
