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

import com.madinnovations.rmu.data.entities.Position;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.combat.Action;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.AttackResult;
import com.madinnovations.rmu.data.entities.combat.CombatPosition;
import com.madinnovations.rmu.data.entities.combat.RestrictedQuarters;
import com.madinnovations.rmu.data.entities.common.Being;
import com.madinnovations.rmu.data.entities.common.Pace;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.State;
import com.madinnovations.rmu.data.entities.common.StateType;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.madinnovations.rmu.data.entities.Modifiers.DAZED;
import static com.madinnovations.rmu.data.entities.Modifiers.OFF_HAND_PENALTY;
import static com.madinnovations.rmu.data.entities.Modifiers.PRONE;
import static com.madinnovations.rmu.data.entities.Modifiers.STAGGERED;

/**
 * Per round encounter information
 */
public class EncounterRoundInfo implements Serializable, Comparable<EncounterRoundInfo> {
	private static final long   serialVersionUID = 7312314437576720214L;
	@SuppressWarnings("unused")
	private static final String TAG              = "EncounterRoundInfo";
	private Being              combatant;
	private Position           position;
	private short              initiativeRoll;
	private short              baseInitiative;
	private Being              selectedOpponent      = null;
	private short              parry                 = 0;
	private short              actionPointsRemaining = 4;
	private short              actionPointsSpent     = 0;
	private boolean            instantaneousUsed     = false;
	private boolean            concentrating         = false;
	private Pace               pace                  = null;
	private boolean            movingBackwards       = false;
	private RestrictedQuarters restrictedQuarters    = RestrictedQuarters.NORMAL;
	private Action             actionInProgress      = null;
	private AttackResult       attackResult          = null;
	private short              movementRemaining     = 4;
	private Object             selectedAttack        = null;

	/**
	 * Creates a new EncounterRoundInfo instance
	 */
	public EncounterRoundInfo() {
	}

	/**
	 * Creates a new EncounterRoundInfo instance
	 *
	 * @param combatant  the combatant for the new instance
	 * @param position  the position of the combatant
	 * @param initiativeRoll  the initiative dice/random roll
	 * @param baseInitiative  the initiative for the combatant's stats and condition
	 */
	public EncounterRoundInfo(Being combatant, Position position, short initiativeRoll, short baseInitiative) {
		this.combatant = combatant;
		this.position = position;
		this.initiativeRoll = initiativeRoll;
		this.baseInitiative = baseInitiative;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("combatant", combatant)
				.append("position", position)
				.append("initiativeRoll", initiativeRoll)
				.append("baseInitiative", baseInitiative)
				.append("selectedOpponent", selectedOpponent)
				.append("parry", parry)
				.append("actionPointsRemaining", actionPointsRemaining)
				.append("actionPointsSpent", actionPointsSpent)
				.append("instantaneousUsed", instantaneousUsed)
				.append("concentrating", concentrating)
				.append("pace", pace)
				.append("movingBackwards", movingBackwards)
				.append("restrictedQuarters", restrictedQuarters)
				.append("actionInProgress", actionInProgress)
				.append("attackResult", attackResult)
				.append("movementRemaining", movementRemaining)
				.append("selectedAttack", selectedAttack)
				.toString();
	}

	@Override
	public int compareTo(@NonNull EncounterRoundInfo o) {
		return o.getBaseInitiative() - getBaseInitiative();
	}

	public Map<Being, Short> getOffensiveBonuses(Map<? extends Being, EncounterRoundInfo> opponentsInfo, Weapon weapon,
												 Attack attack, Specialization specialization) {
		short baseOB = getBaseOffensiveBonus(weapon, attack, specialization);

		Map<Being, Short> bonuses = new HashMap<>(opponentsInfo.size());
		for(Map.Entry<? extends Being, EncounterRoundInfo> entry : opponentsInfo.entrySet()) {
			bonuses.put(entry.getKey(),
						(short)(getOpponentModsToOffensiveBonus(entry.getValue()) + baseOB));
		}

		return bonuses;
	}

	/**
	 * Calculates the offensive bonus for this combatant versus the opponentInfo combatant using the given weapon or attack.
	 *
	 * @param opponentInfo  the opponents EncounterRoundInfo instance
	 * @param weapon  the weapon to be used. Can be null if an Attack is used instead
	 * @param attack  the attack to be used. Can be null if a Weapon is used instead
	 * @return  the offensive bonus
	 */
	public short getOffensiveBonus(EncounterRoundInfo opponentInfo, Weapon weapon, Attack attack, Specialization specialization) {
		short baseOB = getBaseOffensiveBonus(weapon, attack, specialization);
		baseOB += getOpponentModsToOffensiveBonus(opponentInfo);

		return baseOB;
	}

	private short getBaseOffensiveBonus(Weapon weapon, Attack attack, Specialization specialization) {
		short baseOB = -25;

		if(weapon != null) {
			if (weapon == combatant.getMainHandItem()) {
				baseOB = 0;
			}
			else if (weapon == combatant.getOffhandItem()) {
				baseOB = OFF_HAND_PENALTY;
			}
			Short ranks = combatant.getSpecializationRanks().get(((WeaponTemplate)weapon.getItemTemplate())
																		 .getCombatSpecialization());
			if(ranks == null) {
				baseOB -= 25;
			}
			else {
				baseOB += Skill.getRankBonus(ranks);
			}
		}
		if(attack != null && combatant instanceof Creature) {
			if(baseOB == -25) {
				baseOB = 0;
			}
			Short attackBonus = ((Creature)combatant).getCreatureVariety().getPrimaryAttackBonuses().get(attack);
			if(attackBonus == null) {
				attackBonus = ((Creature)combatant).getCreatureVariety().getSecondaryAttackBonuses().get(attack);
			}
			if(attackBonus != null) {
				baseOB += attackBonus;
			}
		}
		if(specialization != null && combatant instanceof Character) {
			Short ranks = combatant.getSpecializationRanks().get(specialization);
			if(ranks != null) {
				baseOB = Skill.getRankBonus(ranks);
			}
		}
		for (Map.Entry<StateType, State> entry : combatant.getCurrentStates().entrySet()) {
			switch (entry.getKey()) {
				case STAGGERED:
					baseOB += STAGGERED;
					break;
				case DAZED:
					baseOB += DAZED;
					break;
				case PRONE:
					baseOB += PRONE;
					break;
			}
		}
		if(getPace() != null) {
			baseOB -= isMovingBackwards() ? getPace().getMoveBackwardsPenalty() : getPace().getPenalty();
		}

		baseOB += restrictedQuarters.getModifier();

		return baseOB;
	}

	private short getOpponentModsToOffensiveBonus(EncounterRoundInfo opponentInfo) {
		short ob = 0;
		for(Map.Entry<StateType, State> entry : opponentInfo.getCombatant().getCurrentStates().entrySet()) {
			switch (entry.getKey()) {
				case STUNNED:
					ob += 20;
					break;
				case SURPRISED:
					ob += 25;
					break;
				case FLATFOOTED:
					ob += 60;
					break;
				case PRONE:
					ob += 30;
					break;
			}
		}
		if(getPosition() != null && opponentInfo.getPosition() != null) {
			CombatPosition defendPosition = getPosition().canAttack(opponentInfo.getPosition(),
																	opponentInfo.getCombatant().getHeight(),
																	combatant.getHeight(), combatant.getWeaponLength());
			switch (defendPosition) {
				case RIGHT_FLANK:
				case LEFT_FLANK:
					ob += 15;
					break;
				case REAR:
					ob += 35;
					break;
			}
			CombatPosition attackPosition = getPosition().getAttackPosition(opponentInfo.getPosition());
			switch (attackPosition) {
				case RIGHT_FLANK:
				case LEFT_FLANK:
					ob -= 30;
					break;
				case REAR:
					ob -= 30;
					break;
			}
		}

		return ob;
	}

	/**
	 * Calculates the defensive bonus for the combatant.
	 *
	 * @param opponentInfo  the opponent's EncounterRoundInfo
	 * @return the defensive bonus for the combatant.
	 */
	public short getDefensiveBonus(EncounterRoundInfo opponentInfo) {
		return 0;
	}

	// Getters and setters
	public Being getCombatant() {
		return combatant;
	}
	public void setCombatant(Being combatant) {
		this.combatant = combatant;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public short getInitiativeRoll() {
		return initiativeRoll;
	}
	public void setInitiativeRoll(short initiativeRoll) {
		this.initiativeRoll = initiativeRoll;
	}
	public short getBaseInitiative() {
		return baseInitiative;
	}
	public void setBaseInitiative(short baseInitiative) {
		this.baseInitiative = baseInitiative;
	}
	public Being getSelectedOpponent() {
		return selectedOpponent;
	}
	public void setSelectedOpponent(Being selectedOpponent) {
		this.selectedOpponent = selectedOpponent;
	}
	public short getParry() {
		return parry;
	}
	public void setParry(short parry) {
		this.parry = parry;
	}
	public short getActionPointsRemaining() {
		return actionPointsRemaining;
	}
	public void setActionPointsRemaining(short actionPointsRemaining) {
		this.actionPointsRemaining = actionPointsRemaining;
	}
	public short getActionPointsSpent() {
		return actionPointsSpent;
	}
	public void setActionPointsSpent(short actionPointsSpent) {
		this.actionPointsSpent = actionPointsSpent;
	}
	public boolean isInstantaneousUsed() {
		return instantaneousUsed;
	}
	public void setInstantaneousUsed(boolean instantaneousUsed) {
		this.instantaneousUsed = instantaneousUsed;
	}
	public boolean isConcentrating() {
		return concentrating;
	}
	public void setConcentrating(boolean concentrating) {
		this.concentrating = concentrating;
	}
	public Pace getPace() {
		return pace;
	}
	public void setPace(Pace pace) {
		this.pace = pace;
	}
	public boolean isMovingBackwards() {
		return movingBackwards;
	}
	public void setMovingBackwards(boolean movingBackwards) {
		this.movingBackwards = movingBackwards;
	}
	public RestrictedQuarters getRestrictedQuarters() {
		return restrictedQuarters;
	}
	public void setRestrictedQuarters(RestrictedQuarters restrictedQuarters) {
		this.restrictedQuarters = restrictedQuarters;
	}
	public Action getActionInProgress() {
		return actionInProgress;
	}
	public void setActionInProgress(Action actionInProgress) {
		this.actionInProgress = actionInProgress;
	}
	public AttackResult getAttackResult() {
		return attackResult;
	}
	public void setAttackResult(AttackResult attackResult) {
		this.attackResult = attackResult;
	}
	public short getMovementRemaining() {
		return movementRemaining;
	}
	public void setMovementRemaining(short movementRemaining) {
		this.movementRemaining = movementRemaining;
	}
	public Object getSelectedAttack() {
		return selectedAttack;
	}
	public void setSelectedAttack(Object selectedAttack) {
		this.selectedAttack = selectedAttack;
	}
}
