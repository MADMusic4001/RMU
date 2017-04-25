/*
  Copyright (C) 2017 MadInnovations
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.entities.play;

import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.creature.Creature;

/**
 * Class used by the ListView adapter in the Initiative dialog UI to encapsulate data for each row.
 */
public class InitiativeListItem implements Comparable<InitiativeListItem> {
	private Character character;
	private Creature  creature;
	private CombatRoundInfo combatRoundInfo;

	@Override
	public int compareTo(@NonNull InitiativeListItem o) {
		return o.combatRoundInfo.getBaseInitiative() - combatRoundInfo.getBaseInitiative();
	}

	// Getters and setters
	public Character getCharacter() {
		return character;
	}
	public void setCharacter(Character character) {
		this.character = character;
	}
	public Creature getCreature() {
		return creature;
	}
	public void setCreature(Creature creature) {
		this.creature = creature;
	}
	public CombatRoundInfo getCombatRoundInfo() {
		return combatRoundInfo;
	}
	public void setCombatRoundInfo(CombatRoundInfo combatRoundInfo) {
		this.combatRoundInfo = combatRoundInfo;
	}
}
