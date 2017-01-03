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
package com.madinnovations.rmu.data.entities.play;

import android.graphics.PointF;

import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.creature.Creature;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Maintains combat data (character, npc positions, etc)
 */
public class CombatSetup extends DatabaseObject{
	private Calendar               combatStartTime    = Calendar.getInstance();
	private Map<Character, PointF> characterLocations = new HashMap<>();
	private Map<Creature, PointF>  creatureLocations  = new HashMap<>();

	// Getters and Setters
	public Calendar getCombatStartTime() {
		return combatStartTime;
	}
	public void setCombatStartTime(Calendar combatStartTime) {
		this.combatStartTime = combatStartTime;
	}
	public Map<Character, PointF> getCharacterLocations() {
		return characterLocations;
	}
	public void setCharacterLocations(
			Map<Character, PointF> characterLocations) {
		this.characterLocations = characterLocations;
	}
	public Map<Creature, PointF> getOpponentLocation() {
		return creatureLocations;
	}
	public void setCreatureLocations(
			Map<Creature, PointF> creatureLocations) {
		this.creatureLocations = creatureLocations;
	}
}
