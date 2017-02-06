/**
 * Copyright (C) 2017 MadInnovations
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

import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.creature.Creature;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 2/5/2017.
 */
public class InitiativeListItem implements Comparable<InitiativeListItem> {
	private Character character;
	private Creature  creature;
	private String    name;
	private short     initiativeRoll;
	private short     quicknessBonus;
	private short     otherPenalties;
	private short     totalInitiative;

	@Override
	public int compareTo(InitiativeListItem o) {
		if(o == null) {
			return 11;
		}
		return o.totalInitiative - totalInitiative;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public short getInitiativeRoll() {
		return initiativeRoll;
	}
	public void setInitiativeRoll(short initiativeRoll) {
		this.initiativeRoll = initiativeRoll;
	}
	public short getQuicknessBonus() {
		return quicknessBonus;
	}
	public void setQuicknessBonus(short quicknessBonus) {
		this.quicknessBonus = quicknessBonus;
	}
	public short getOtherPenalties() {
		return otherPenalties;
	}
	public void setOtherPenalties(short otherPenalties) {
		this.otherPenalties = otherPenalties;
	}
	public short getTotalInitiative() {
		return totalInitiative;
	}
	public void setTotalInitiative(short totalInitiative) {
		this.totalInitiative = totalInitiative;
	}
}
