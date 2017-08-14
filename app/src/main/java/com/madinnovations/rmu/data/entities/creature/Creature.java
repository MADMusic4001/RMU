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

import android.util.Log;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.AttackResult;
import com.madinnovations.rmu.data.entities.combat.CreatureAttack;
import com.madinnovations.rmu.data.entities.common.Being;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillBonus;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.State;
import com.madinnovations.rmu.data.entities.common.StateType;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.item.Weapon;
import com.madinnovations.rmu.data.entities.item.WeaponTemplate;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;
import com.madinnovations.rmu.data.entities.spells.SpellList;
import com.madinnovations.rmu.view.RMUApp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creature attributes
 */
public class Creature extends Being implements Serializable {
	@SuppressWarnings("unused")
	private static final String                     TAG                       = "Creature";
	private static final long                       serialVersionUID          = 2106087301956452748L;
	public static final  String                     JSON_NAME                 = "Creatures";
	private              CreatureVariety            creatureVariety           = null;
	private              CreatureArchetype          archetype                 = null;
	private              short                      numCreatures              = 1;
	private              List<SkillBonus>           primarySkillBonusesList   = new ArrayList<>();
	private              List<SkillBonus>           secondarySkillBonusesList = new ArrayList<>();
	private              Map<Specialization, Short> specializationBonuses     = new HashMap<>();
	private              Map<SpellList, Short>      spellListBonuses          = new HashMap<>();
	private              List<TalentInstance>       talentInstancesList       = new ArrayList<>();
	private              Map<Attack, Short>         primaryAttackBonusesMap   = new HashMap<>();
	private              Map<Attack, Short>         secondaryAttackBonusesMap = new HashMap<>();

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
		return super.isValid() && creatureVariety != null && archetype != null;
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
		for(Map.Entry<StateType, State> entry : currentStates.entrySet()) {
			switch (entry.getKey()) {
				case ENCUMBERED:
					totalPenalty += entry.getValue().getConstant();
					break;
				case FATIGUED:
					totalPenalty += entry.getValue().getConstant();
					break;
				case HASTED:
					totalPenalty += entry.getValue().getConstant();
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
					float hpLossPercent = entry.getValue().getConstant() / maxHits;
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
					totalPenalty += entry.getValue().getConstant();
					break;
			}
		}
		return (short) (totalPenalty / 10);
	}

	@Override
	public short getArmorType() {
		return creatureVariety.getArmorType();
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
		for(Map.Entry<StateType, State> entry : currentStates.entrySet()) {
			switch (entry.getKey()) {
				case ENCUMBERED:
					totalPenalty += entry.getValue().getConstant();
					break;
				case FATIGUED:
					totalPenalty += entry.getValue().getConstant();
					break;
				case HASTED:
					totalPenalty += entry.getValue().getConstant();
					break;
				case HP_LOSS:
					float hpLossPercent = entry.getValue().getConstant()/maxHits;
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
					totalPenalty += entry.getValue().getConstant();
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

	@Override
	public short getHeight() {
		return creatureVariety.getHeight() > creatureVariety.getLength() ? creatureVariety.getHeight() :
			   creatureVariety.getLength();
	}

	/**
	 * Gets the next attack the creature should use based on its attack sequence and the result of any previous attack.
	 *
	 * @param attackResult  the previous attack result
	 * @param opponentInfo  the opponnents infor
	 * @param sameRound  true if this request is to get a next attack in the same round
	 * @return  the next attack to use or null if none found
	 */
	public Attack getNextAttack(AttackResult attackResult, EncounterRoundInfo opponentInfo, boolean sameRound) {
		Attack result = null;
		boolean useNextAttack = false;

		for(CreatureAttack creatureAttack : getCreatureVariety().getAttackList()) {
			Log.d(TAG, "getNextAttack: creatureAttack = " + creatureAttack);
			// Not first attack
			if(attackResult != null) {
				if(useNextAttack) {
					result = creatureAttack.getBaseAttack();
					break;
				}
				else if(attackResult.getAttack().equals(creatureAttack.getBaseAttack()) &&
						creatureAttack.isCriticalFollowUp() &&
						attackResult.getCriticalResult() != null) {
					if(attackResult.getAttack().getCode().equals("gr")) {
						State grappledState = opponentInfo.getCombatant().getCurrentStates().get(StateType.GRAPPLED);
						if(grappledState.getConstant() >= 100 &&
								(!sameRound || creatureAttack.isSameRoundFollowUp())) {
							useNextAttack = true;
						}
					}
					else if(!sameRound || creatureAttack.isSameRoundFollowUp()) {
						useNextAttack = true;
					}
				}
			}
			else if(creatureAttack.getBaseAttack() != null) {
				result = creatureAttack.getBaseAttack();
				break;
			}
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
	public short getNumCreatures() {
		return numCreatures;
	}
	public void setNumCreatures(short numCreatures) {
		this.numCreatures = numCreatures;
	}
	public List<SkillBonus> getPrimarySkillBonusesList() {
		return primarySkillBonusesList;
	}
	public void setPrimarySkillBonusesList(List<SkillBonus> primarySkillBonusesList) {
		this.primarySkillBonusesList = primarySkillBonusesList;
	}
	public List<SkillBonus> getSecondarySkillBonusesList() {
		return secondarySkillBonusesList;
	}
	public void setSecondarySkillBonusesList(List<SkillBonus> secondarySkillBonusesList) {
		this.secondarySkillBonusesList = secondarySkillBonusesList;
	}
	public Map<Specialization, Short> getSpecializationBonuses() {
		return specializationBonuses;
	}
	public void setSpecializationBonuses(Map<Specialization, Short> specializationBonuses) {
		this.specializationBonuses = specializationBonuses;
	}
	public Map<SpellList, Short> getSpellListBonuses() {
		return spellListBonuses;
	}
	public void setSpellListBonuses(Map<SpellList, Short> spellListBonuses) {
		this.spellListBonuses = spellListBonuses;
	}
	public List<TalentInstance> getTalentInstancesList() {
		return talentInstancesList;
	}
	public void setTalentInstancesList(List<TalentInstance> talentInstancesList) {
		this.talentInstancesList = talentInstancesList;
	}
	public Map<Attack, Short> getPrimaryAttackBonusesMap() {
		return primaryAttackBonusesMap;
	}
	public void setPrimaryAttackBonusesMap(Map<Attack, Short> primaryAttackBonusesMap) {
		this.primaryAttackBonusesMap = primaryAttackBonusesMap;
	}
	public Map<Attack, Short> getSecondaryAttackBonusesMap() {
		return secondaryAttackBonusesMap;
	}
	public void setSecondaryAttackBonusesMap(Map<Attack, Short> secondaryAttackBonusesMap) {
		this.secondaryAttackBonusesMap = secondaryAttackBonusesMap;
	}
}
