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

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.Position;
import com.madinnovations.rmu.data.entities.combat.Action;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.AttackResult;
import com.madinnovations.rmu.data.entities.combat.CombatPosition;
import com.madinnovations.rmu.data.entities.combat.OffensiveBonus;
import com.madinnovations.rmu.data.entities.combat.RestrictedQuarters;
import com.madinnovations.rmu.data.entities.common.Being;
import com.madinnovations.rmu.data.entities.common.Pace;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.State;
import com.madinnovations.rmu.data.entities.common.StateType;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.item.Weapon;
import com.madinnovations.rmu.data.entities.item.WeaponTemplate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.madinnovations.rmu.data.entities.Modifiers.DAZED;
import static com.madinnovations.rmu.data.entities.Modifiers.PRONE;
import static com.madinnovations.rmu.data.entities.Modifiers.STAGGERED;

/**
 * Per round encounter information
 */
@SuppressWarnings("unused")
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
	private Attack             selectedAttack        = null;
	private OffensiveBonus     offensiveBonus        = new OffensiveBonus();
	private Being              sensedWeakness        = null;

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
												 Attack attack) {
		short baseOB = getBaseOffensiveBonus(attack);

		Map<Being, Short> bonuses = new HashMap<>(opponentsInfo.size());
		for(Map.Entry<? extends Being, EncounterRoundInfo> entry : opponentsInfo.entrySet()) {
			bonuses.put(entry.getKey(),
						(short)(getOpponentModsToOffensiveBonus(entry.getValue(), weapon) + baseOB));
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
	public short getOffensiveBonus(EncounterRoundInfo opponentInfo, Weapon weapon, Attack attack) {
		short baseOB = getBaseOffensiveBonus(attack);
		baseOB += getOpponentModsToOffensiveBonus(opponentInfo, weapon);

		return baseOB;
	}

	private short getBaseOffensiveBonus(@NonNull Attack attack) {
		short baseOB = -25;

		Short attackBonus;
		if(combatant instanceof Creature) {
			attackBonus = ((Creature) combatant).getCreatureVariety().getPrimaryAttackBonuses().get(attack);
			if (attackBonus == null) {
				attackBonus = ((Creature) combatant).getCreatureVariety().getSecondaryAttackBonuses().get(attack);
			}
		}
		else {
			Short ranks = combatant.getSpecializationRanks().get(attack.getSpecialization());
			attackBonus = Skill.getRankBonus(ranks);
		}
		if(attackBonus != null) {
			baseOB += attackBonus;
		}
		offensiveBonus.getAttackerAdditions().put(R.string.ob_skill_ranks, baseOB);
		for (Map.Entry<StateType, State> entry : combatant.getCurrentStates().entrySet()) {
			switch (entry.getKey()) {
				case STAGGERED:
					offensiveBonus.getAttackerSubtractions().put(R.string.ob_attacker_staggered, (short)STAGGERED);
					baseOB += STAGGERED;
					break;
				case DAZED:
					offensiveBonus.getAttackerSubtractions().put(R.string.ob_attacker_dazed, (short)DAZED);
					baseOB += DAZED;
					break;
				case PRONE:
					offensiveBonus.getAttackerSubtractions().put(R.string.ob_attacker_prone, (short)PRONE);
					baseOB += PRONE;
					break;
			}
		}
		if(getPace() != null) {
			int pacePenalty;
			if(isMovingBackwards()) {
				pacePenalty = getPace().getMoveBackwardsPenalty();
				offensiveBonus.getAttackerSubtractions().put(R.string.ob_attacker_moving_backwards, (short)pacePenalty);
			}
			else {
				pacePenalty = getPace().getPenalty();
				if(pacePenalty != 0) {
					offensiveBonus.getAttackerSubtractions().put(R.string.ob_attacker_moving_pace, (short) pacePenalty);
				}
			}
			baseOB += pacePenalty;
		}

		if(restrictedQuarters != null && restrictedQuarters.getModifier() != 0) {
			short restrictedQuartersModifier = (short)restrictedQuarters.getModifier();
			offensiveBonus.getAttackerSubtractions().put(R.string.ob_attacker_restricted_quarters, restrictedQuartersModifier);
			baseOB += restrictedQuartersModifier;
		}

		return baseOB;
	}

	private short getOpponentModsToOffensiveBonus(EncounterRoundInfo opponentInfo, Weapon weapon) {
		short ob = 0;
		for(Map.Entry<StateType, State> entry : opponentInfo.getCombatant().getCurrentStates().entrySet()) {
			switch (entry.getKey()) {
				case STUNNED:
					offensiveBonus.getDefenderAdditions().put(R.string.ob_defender_stunned, (short)20);
					ob += 20;
					break;
				case SURPRISED:
					offensiveBonus.getDefenderAdditions().put(R.string.ob_defender_surprised, (short)25);
					ob += 25;
					break;
				case FLATFOOTED:
					offensiveBonus.getDefenderAdditions().put(R.string.ob_defender_flatfooted, (short)60);
					ob += 60;
					break;
				case PRONE:
					offensiveBonus.getDefenderAdditions().put(R.string.ob_defender_prone, (short)30);
					ob += 30;
					break;
			}
		}
		if(getPosition() != null && opponentInfo.getPosition() != null) {
			float weaponLength = 0;
			if(weapon != null) {
				weaponLength = ((WeaponTemplate)weapon.getItemTemplate()).getLength();
			}
			CombatPosition defendPosition = getPosition().canAttack(opponentInfo.getPosition(),
																	opponentInfo.getCombatant().getHeight(),
																	combatant.getHeight(), weaponLength);
			switch (defendPosition) {
				case RIGHT_FLANK:
				case LEFT_FLANK:
					offensiveBonus.getDefenderAdditions().put(R.string.ob_attacker_flanking_defender, (short)15);
					ob += 15;
					break;
				case REAR:
					offensiveBonus.getDefenderAdditions().put(R.string.ob_attacker_behind_defender, (short)35);
					ob += 35;
					break;
			}
			CombatPosition attackPosition = getPosition().getAttackPosition(opponentInfo.getPosition());
			switch (attackPosition) {
				case RIGHT_FLANK:
				case LEFT_FLANK:
					offensiveBonus.getDefenderSubtractions().put(R.string.ob_defender_flanking_attacker, (short)-30);
					ob -= 30;
					break;
				case REAR:
					offensiveBonus.getDefenderSubtractions().put(R.string.ob_defender_behind_attacker, (short)-70);
					ob -= 70;
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
	public Attack getSelectedAttack() {
		return selectedAttack;
	}
	public void setSelectedAttack(Attack selectedAttack) {
		this.selectedAttack = selectedAttack;
	}
}
