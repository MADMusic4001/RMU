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

import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.creature.Creature;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Maintains combat data (character, npc positions, etc)
 */
public class CombatSetup extends DatabaseObject implements Serializable {
	private static final long serialVersionUID = 5794443312681755710L;
	public static final String         JSON_NAME           = "CombatSetups";
	private Campaign                   campaign            = null;
	private short                      currentInitiative   = 0;
	private Calendar                   combatStartTime     = Calendar.getInstance();
	private Map<Character, CombatInfo> characterCombatInfo = new HashMap<>();
	private Map<Creature, CombatInfo>  creatureCombatInfo  = new HashMap<>();

	/**
	 * Checks the validity of the CombatSetup instance.
	 *
	 * @return true if the CombatSetup instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return campaign != null && !characterCombatInfo.isEmpty() && !creatureCombatInfo.isEmpty();
	}

	// Getters and Setters
	public Campaign getCampaign() {
		return campaign;
	}
	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}
	public short getCurrentInitiative() {
		return currentInitiative;
	}
	public void setCurrentInitiative(short currentInitiative) {
		this.currentInitiative = currentInitiative;
	}
	public Calendar getCombatStartTime() {
		return combatStartTime;
	}
	public void setCombatStartTime(Calendar combatStartTime) {
		this.combatStartTime = combatStartTime;
	}
	public Map<Character, CombatInfo> getCharacterCombatInfo() {
		return characterCombatInfo;
	}
	public void setCharacterCombatInfo(Map<Character, CombatInfo> characterCombatInfo) {
		this.characterCombatInfo = characterCombatInfo;
	}
	public Map<Creature, CombatInfo> getCreatureCombatInfo() {
		return creatureCombatInfo;
	}
	public void setCreatureCombatInfo(Map<Creature, CombatInfo> creatureCombatInfo) {
		this.creatureCombatInfo = creatureCombatInfo;
	}
}
