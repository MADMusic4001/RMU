/*
  Copyright (C) 2016 MadInnovations
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
public class EncounterSetup extends DatabaseObject implements Serializable {
	private static final long               serialVersionUID    = 5794443312681755710L;
	public static final String              JSON_NAME           = "EncounterSetups";
	private Campaign                        campaign            = null;
	private short                           currentInitiative   = 0;
	private Calendar                        encounterStartTime  = Calendar.getInstance();
	private Map<Character, CombatRoundInfo> characterCombatInfo = new HashMap<>();
	private Map<Creature, CombatRoundInfo>  enemyCombatInfo     = new HashMap<>();

	/**
	 * Checks the validity of the CombatSetup instance.
	 *
	 * @return true if the CombatSetup instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return campaign != null && !characterCombatInfo.isEmpty() && !enemyCombatInfo.isEmpty();
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
	public Calendar getEncounterStartTime() {
		return encounterStartTime;
	}
	public void setEncounterStartTime(Calendar encounterStartTime) {
		this.encounterStartTime = encounterStartTime;
	}
	public Map<Character, CombatRoundInfo> getCharacterCombatInfo() {
		return characterCombatInfo;
	}
	public void setCharacterCombatInfo(Map<Character, CombatRoundInfo> characterCombatInfo) {
		this.characterCombatInfo = characterCombatInfo;
	}
	public Map<Creature, CombatRoundInfo> getEnemyCombatInfo() {
		return enemyCombatInfo;
	}
	public void setEnemyCombatInfo(Map<Creature, CombatRoundInfo> enemyCombatInfo) {
		this.enemyCombatInfo = enemyCombatInfo;
	}
}
