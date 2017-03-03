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
package com.madinnovations.rmu.data.entities.combat;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 9/21/2016.
 */
public enum Action {
	AUTO("Auto", (short)0, (short)0, false),
	DROP_ITEM("Drop Item", (short)0, (short)0, false),
	INSTANTANEOUS_SPELL("Instantaneous Spell", (short)0, (short)0, false),
	USE_SHIELD_IN_MELEE("Use Shield in Melee", (short)0, (short)0, false),
	MOVE_FOURTH_MOVEMENT_RATE("Move 1/4 of Movement Rate", (short)1, (short)1, false),
	SHIFT_ITEM_TO_OTHER_HAND("Shift Item to Other Hand", (short)1, (short)1, false),
	DRAW_WEAPON("Draw Weapon/Ammo/Item", (short)1, (short)1, false),
	STAND_FROM_PRONE("Prone <-> Stand", (short)1, (short)1, false),
	PERCEPTION("Perception", (short)1, (short)2, false),
	RANGED_ATTACK("Ranged Attack", (short)1, (short)3, false),
	EAT_DRINK("Eat or Drink (herb/potion)", (short)2, (short)2, false),
	CAST_SPELL("Cast Spell", (short)2, (short)4, false),
	MELEE_ATTACK("Melee Attack", (short)2, (short)4, false),
	GET_ITEM_FROM_GROUND("Get Item from Ground", (short)3, (short)3, false),
	DODGE("Dodge", (short)4, (short)4, false),
	MOUNT_DISMOUNT("Mount/Dismount", (short)4, (short)4, false),
	STRING_BOW("String Bow", (short)6, (short)6, false),
	LOAD_LIGHT_XBOW("Load Light Crossbow", (short)8, (short)8, false),
	LOAD_HEAVY_XBOW("Load Heavy Crossbow", (short)16, (short)16, false),
	PICK_LOCK_DISARM_TRAP("Pick Lock / Disarm Trap", (short)20, (short)20, false),
	PARTIAL_DODGE("Partial Dodge", (short)-1, (short)-1, true),
	HOLD_POSITION("Hold Position (Swim/Climb)", (short)-1, (short)-1, true),
	CONCENTRATION("Concentration", (short)-1, (short)-1, true),
	SPELL_PREPARATION("Spell Preparation", (short)-1, (short)-1, true),
	MOVEMENT("Movement", (short)-1, (short)-1, false);

	private String text;
	private short minActionPoints;
	private short maxActionPoints;
	private boolean concentration;

	Action(String text, short minActionPoints, short maxActionPoints, boolean concentration) {
		this.text = text;
		this.minActionPoints = minActionPoints;
		this.maxActionPoints = maxActionPoints;
		this.concentration = concentration;
	}

	@Override
	public String toString() {
		return text;
	}

	// Getters
	public String getText() {
		return text;
	}
	public short getMinActionPoints() {
		return minActionPoints;
	}
	public short getMaxActionPoints() {
		return maxActionPoints;
	}
	public boolean isConcentration() {
		return concentration;
	}
}
