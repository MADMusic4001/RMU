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
package com.madinnovations.rmu.data.entities.creature;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.combat.CreatureAttack;
import com.madinnovations.rmu.data.entities.common.Being;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.State;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;
import com.madinnovations.rmu.view.RMUApp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Creature attributes
 */
public class Creature extends Being implements Serializable {
	private static final long              serialVersionUID = 2106087301956452748L;
	public static final  String            JSON_NAME        = "Creatures";
	private              CreatureVariety   creatureVariety  = null;
	private              CreatureArchetype archetype        = null;
	private              CreatureAttack    lastAttack       = null;
	private              short             numCreatures     = 1;

	/**
	 * Creates a new Creature instance with default values
	 */
	public Creature() {
	}

	/**
	 * Creates a new Creature instance with default values
	 *
	 * @param id  the database id of the new creature
	 */
	public Creature(int id) {
		super(id);
	}

	/**
	 * Checks the validity of the Creature instance.
	 *
	 * @return true if the Creature instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return creatureVariety != null;
	}

	@Override
	public String toString() {
		return String.format(RMUApp.getResourceUtils().getString(R.string.creature_name_format_string), currentLevel,
							 creatureVariety.getName());
	}

	@Override
	public short getTotalStatBonus(Statistic statistic) {
		return getCreatureVariety().getRacialStatBonuses().get(statistic);
	}

	@Override
	public short getInitiativeModifications() {
		int totalPenalty = 0;
		for (State state : currentStates) {
			switch (state.getStateType()) {
				case ENCUMBERED:
					totalPenalty += state.getConstant();
					break;
				case FATIGUED:
					totalPenalty += state.getConstant();
					break;
				case HASTED:
					totalPenalty += state.getConstant();
					break;
				case HP_LOSS:
					int maxHits = getCreatureVariety().getBaseHits()
							+ getCreatureVariety().getRacialStatBonuses().get(Statistic.CONSTITUTION) * 2
							+ getCreatureVariety().getRacialStatBonuses().get(Statistic.SELF_DISCIPLINE)
							+ getCreatureVariety().getSkillBonus(new Skill("BodyDevelopment"));
					for (TalentInstance talentInstance : getCreatureVariety().getTalentInstancesList()) {
						if (talentInstance.getTalent().getName().equals("Tough")) {
							maxHits += talentInstance.getTiers() * 5;
						}
						if (talentInstance.getTalent().getName().equals("Fragile")) {
							maxHits -= talentInstance.getTiers() * 5;
						}
					}
					float hpLossPercent = state.getConstant() / maxHits;
					if (hpLossPercent >= 0.76) {
						totalPenalty += -30;
					}
					else if (hpLossPercent >= 0.51) {
						totalPenalty += -20;
					}
					else if (hpLossPercent >= 0.26) {
						totalPenalty += -10;
					}
					break;
				case INJURED:
					totalPenalty += state.getConstant();
					break;
			}
		}
		return (short) (totalPenalty / 10);
	}

	/**
	 * Generates a debug String containing all of the member variables for this instance.
	 *
	 * @return a debug String containing all of the member variables for this instance.
	 */
	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("campaign", campaign)
				.append("creatureVariety", creatureVariety)
				.append("currentLevel", currentLevel)
				.append("maxHits", maxHits)
				.append("currentHits", currentHits)
				.append("archetype", archetype)
				.append("fatigue", fatigue)
				.append("powerPointLoss", powerPointLoss)
				.append("currentStates", currentStates)
				.toString();
	}

	/**
	 * Calculates the initiative penalty for this creature
	 *
	 * @return the initiative penalty
	 */
	public short getInitiativePenalty() {
		int totalPenalty = 0;
		for(State state : currentStates) {
			switch (state.getStateType()) {
				case ENCUMBERED:
					totalPenalty += state.getConstant();
					break;
				case FATIGUED:
					totalPenalty += state.getConstant();
					break;
				case HASTED:
					totalPenalty += state.getConstant();
					break;
				case HP_LOSS:
					float hpLossPercent = state.getConstant()/maxHits;
					if(hpLossPercent >= 0.76) {
						totalPenalty += -30;
					}
					else if(hpLossPercent >= 0.51) {
						totalPenalty += -20;
					}
					else if(hpLossPercent >= 0.26) {
						totalPenalty += -10;
					}
					break;
				case INJURED:
					totalPenalty += state.getConstant();
					break;
			}
		}
		return (short)(totalPenalty/10);
	}

	/**
	 * Gets the current primary weapon in use by this character or null if no weapon is equipped.
	 *
	 * @return a Weapon instance or null if none are equipped.
	 */
	public Weapon getWeapon() {
		Weapon result = null;

		if(mainHandItem != null && mainHandItem instanceof Weapon) {
			result = (Weapon)mainHandItem;
		}
		else if(offhandItem != null && offhandItem instanceof Weapon) {
			result = (Weapon)offhandItem;
		}

		return result;
	}

	/**
	 * Gets the length of the currently equipped weapon or 0 if no weapon is equipped.
	 *
	 * @return  the length of the equipped weapon or 0 if no weapon is equipped.
	 */
	public float getWeaponLength() {
		float result = 0.0f;

		Weapon weapon = getWeapon();
		if(weapon != null) {
			result = ((WeaponTemplate)weapon.getItemTemplate()).getLength();
		}

		return result;
	}

	// Getters and setters
	public CreatureVariety getCreatureVariety() {
		return creatureVariety;
	}
	public void setCreatureVariety(CreatureVariety creatureVariety) {
		this.creatureVariety = creatureVariety;
	}
	public CreatureArchetype getArchetype() {
		return archetype;
	}
	public void setArchetype(CreatureArchetype archetype) {
		this.archetype = archetype;
	}
	public CreatureAttack getLastAttack() {
		return lastAttack;
	}
	public void setLastAttack(CreatureAttack lastAttack) {
		this.lastAttack = lastAttack;
	}
	public short getNumCreatures() {
		return numCreatures;
	}
	public void setNumCreatures(short numCreatures) {
		this.numCreatures = numCreatures;
	}
}
