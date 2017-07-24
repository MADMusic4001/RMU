/*
 *
 *   Copyright (C) 2017 MadInnovations
 *   <p/>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   <p/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   <p/>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  
 *
 */

package com.madinnovations.rmu.data.entities.common;

import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.data.entities.spells.SpellList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class with attributes common to player characters and non-player characters / creatures.
 */
@SuppressWarnings("WeakerAccess")
public abstract class Being extends DatabaseObject {
	public static final short                      INITIAL_DP               = 50;
	protected           Campaign                   campaign                 = null;
	protected           short                      currentLevel             = 0;
	protected           int                        maxHits                  = 0;
	protected           int                        currentHits              = 0;
	protected           Realm                      realm                    = null;
	protected           Realm                      realm2                   = null;
	protected           Realm                      realm3                   = null;
	protected           short                      height                   = 70;
	protected           short                      weight                   = 185;
	protected           int                        hitPointLoss             = 0;
	protected           short                      fatigue                  = 0;
	protected           short                      powerPointLoss           = 0;
	protected           short                      baseMovementRate         = 20;
	protected           short                      currentDevelopmentPoints = INITIAL_DP;
	protected           List<State>                currentStates            = new ArrayList<>();
	protected           Map<Skill, Short>          skillRanks               = new HashMap<>();
	protected           Map<Specialization, Short> specializationRanks      = new HashMap<>();
	protected           Map<SpellList, Short>      spellListRanks           = new HashMap<>();
	protected           List<TalentInstance>       talentInstances          = new ArrayList<>();
	protected           Item                       mainHandItem             = null;
	protected           Item                       offhandItem              = null;
	protected           Item                       shirtItem                = null;
	protected           Item                       pantsItem                = null;
	protected           Item                       headItem                 = null;
	protected           Item                       chestItem                = null;
	protected           Item                       armsItem                 = null;
	protected           Item                       handsItem                = null;
	protected           Item                       legsItem                 = null;
	protected           Item                       feetItem                 = null;
	protected           Item                       backItem                 = null;
	protected           Item                       backpackItem             = null;

	/**
	 * Creates a new Being instance
	 */
	public Being() {
	}

	/**
	 * Creates a new Being instance
	 *
	 * @param id the database ID of the new instance
	 */
	public Being(int id) {
		super(id);
	}

// <editor-fold desc="Public methods">

	/**
	 * Gets the stat bonus for the given statistic
	 *
	 * @param statistic the Statistic whose bonus is needed
	 * @return the total bonus for the given Statistic.
	 */
	public abstract short getTotalStatBonus(Statistic statistic);
// </editor-fold> Public methods

// <editor-fold desc="Getters and setters">

	/**
	 * Checks the validity of the Creature instance.
	 *
	 * @return true if the Creature instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return campaign != null;
	}

	/**
	 * Gets the current primary weapon in use by this character or null if no weapon is equipped.
	 *
	 * @return a Weapon instance or null if none are equipped.
	 */
	public Weapon getWeapon() {
		Weapon result = null;

		if (mainHandItem != null && mainHandItem instanceof Weapon) {
			result = (Weapon) mainHandItem;
		}
		else if (offhandItem != null && offhandItem instanceof Weapon) {
			result = (Weapon) offhandItem;
		}

		return result;
	}

	/**
	 * Gets the length of the currently equipped weapon or 0 if no weapon is equipped.
	 *
	 * @return the length of the equipped weapon or 0 if no weapon is equipped.
	 */
	public float getWeaponLength() {
		float result = 0.0f;

		Weapon weapon = getWeapon();
		if (weapon != null) {
			result = ((WeaponTemplate) weapon.getItemTemplate()).getLength();
		}

		return result;
	}

	/**
	 * Gets the total modifications that need to be applied to the initiative roll for this Being.
	 *
	 * @return the total initiative roll modifications
	 */
	public abstract short getInitiativeModifications();

	/**
	 * Gets the armor type for this being
	 *
	 * @return  the being's armor type.
	 */
	public abstract short getArmorType();

	// Getters and setters
	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public short getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(short currentLevel) {
		this.currentLevel = currentLevel;
	}

	public int getMaxHits() {
		return maxHits;
	}

	public void setMaxHits(int maxHits) {
		this.maxHits = maxHits;
	}

	public int getCurrentHits() {
		return currentHits;
	}

	public void setCurrentHits(int currentHits) {
		this.currentHits = currentHits;
	}

	public Realm getRealm() {
		return realm;
	}

	public void setRealm(Realm realm) {
		this.realm = realm;
	}

	public Realm getRealm2() {
		return realm2;
	}

	public void setRealm2(Realm realm2) {
		this.realm2 = realm2;
	}

	public Realm getRealm3() {
		return realm3;
	}

	public void setRealm3(Realm realm3) {
		this.realm3 = realm3;
	}

	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public short getWeight() {
		return weight;
	}

	public void setWeight(short weight) {
		this.weight = weight;
	}

	public int getHitPointLoss() {
		return hitPointLoss;
	}

	public void setHitPointLoss(int hitPointLoss) {
		this.hitPointLoss = hitPointLoss;
	}

	public short getFatigue() {
		return fatigue;
	}

	public void setFatigue(short fatigue) {
		this.fatigue = fatigue;
	}

	public short getPowerPointLoss() {
		return powerPointLoss;
	}

	public void setPowerPointLoss(short powerPointLoss) {
		this.powerPointLoss = powerPointLoss;
	}

	public short getBaseMovementRate() {
		return baseMovementRate;
	}

	public void setBaseMovementRate(short baseMovementRate) {
		this.baseMovementRate = baseMovementRate;
	}

	public short getCurrentDevelopmentPoints() {
		return currentDevelopmentPoints;
	}

	public void setCurrentDevelopmentPoints(short currentDevelopmentPoints) {
		this.currentDevelopmentPoints = currentDevelopmentPoints;
	}

	public List<State> getCurrentStates() {
		return currentStates;
	}

	public Map<Skill, Short> getSkillRanks() {
		return skillRanks;
	}

	public void setSkillRanks(Map<Skill, Short> skillRanks) {
		this.skillRanks = skillRanks;
	}

	public Map<Specialization, Short> getSpecializationRanks() {
		return specializationRanks;
	}

	public void setSpecializationRanks(
			Map<Specialization, Short> specializationRanks) {
		this.specializationRanks = specializationRanks;
	}

	public Map<SpellList, Short> getSpellListRanks() {
		return spellListRanks;
	}

	public void setSpellListRanks(Map<SpellList, Short> spellListRanks) {
		this.spellListRanks = spellListRanks;
	}

	public List<TalentInstance> getTalentInstances() {
		return talentInstances;
	}

	public void setTalentInstances(List<TalentInstance> talentInstances) {
		this.talentInstances = talentInstances;
	}

	public Item getMainHandItem() {
		return mainHandItem;
	}

	public void setMainHandItem(Item mainHandItem) {
		this.mainHandItem = mainHandItem;
	}

	public Item getOffhandItem() {
		return offhandItem;
	}

	public void setOffhandItem(Item offhandItem) {
		this.offhandItem = offhandItem;
	}

	public Item getShirtItem() {
		return shirtItem;
	}

	public void setShirtItem(Item shirtItem) {
		this.shirtItem = shirtItem;
	}

	public Item getPantsItem() {
		return pantsItem;
	}

	public void setPantsItem(Item pantsItem) {
		this.pantsItem = pantsItem;
	}

	public Item getHeadItem() {
		return headItem;
	}

	public void setHeadItem(Item headItem) {
		this.headItem = headItem;
	}

	public Item getChestItem() {
		return chestItem;
	}

	public void setChestItem(Item chestItem) {
		this.chestItem = chestItem;
	}

	public Item getArmsItem() {
		return armsItem;
	}

	public void setArmsItem(Item armsItem) {
		this.armsItem = armsItem;
	}

	public Item getHandsItem() {
		return handsItem;
	}

	public void setHandsItem(Item handsItem) {
		this.handsItem = handsItem;
	}

	public Item getLegsItem() {
		return legsItem;
	}

	public void setLegsItem(Item legsItem) {
		this.legsItem = legsItem;
	}

	public Item getFeetItem() {
		return feetItem;
	}

	public void setFeetItem(Item feetItem) {
		this.feetItem = feetItem;
	}

	public Item getBackItem() {
		return backItem;
	}

	public void setBackItem(Item backItem) {
		this.backItem = backItem;
	}

	public Item getBackpackItem() {
		return backpackItem;
	}

	public void setBackpackItem(Item backpackItem) {
		this.backpackItem = backpackItem;
	}
// </editor-fold> Getters and setters
}
