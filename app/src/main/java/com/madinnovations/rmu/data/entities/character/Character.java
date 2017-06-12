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
package com.madinnovations.rmu.data.entities.character;

import android.util.Log;

import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.common.DevelopmentCostGroup;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.State;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.data.entities.spells.SpellList;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Character attributes
 */
@SuppressWarnings("unused")
public class Character extends DatabaseObject implements Serializable {
	private static final long serialVersionUID = 5800697497948561223L;
	private static final String TAG = "Character";
	public static final String         JSON_NAME                       = "Characters";
	public static final short          INITIAL_DP                      = 50;
	private Campaign                   campaign                        = null;
	private short                            currentLevel                    = 0;
	private int                              experiencePoints                = 0;
	private short                            statPurchasePoints              = 0;
	private String                           firstName                       = null;
	private String                           lastName                        = null;
	private String                           knownAs                         = null;
	private String                           description                     = null;
	private String                           hairColor                       = null;
	private String                           hairStyle                       = null;
	private String                           eyeColor                        = null;
	private String                           skinComplexion                  = null;
	private String                           facialFeatures                  = null;
	private String                           identifyingMarks                = null;
	private String                           personality                     = null;
	private String                           mannerisms                      = null;
	private String                           hometown                        = null;
	private String                           familyInfo                      = null;
	private Race                             race                            = null;
	private Culture                          culture                         = null;
	private Profession                       profession                      = null;
	private Realm                            realm                           = null;
	private Realm                            realm2                          = null;
	private Realm                            realm3                          = null;
	private short                            height                          = 70;
	private short                            weight                          = 185;
	private int                              hitPointLoss                    = 0;
	private short                            currentDevelopmentPoints        = INITIAL_DP;
	private short                            fatigue                         = 0;
	private short                            powerPointLoss                  = 0;
	private Map<Skill, DevelopmentCostGroup> skillCosts                      = new HashMap<>();
	private Map<Skill, Short>                skillRanks                      = new HashMap<>();
	private Map<Specialization, Short>       specializationRanks             = new HashMap<>();
	private Map<SpellList, Short>            spellListRanks                  = new HashMap<>();
	private List<TalentInstance>             talentInstances                 = new ArrayList<>();
	private Map<Statistic, Short>            statTemps                       = new HashMap<>();
	private Map<Statistic, Short>            statPotentials                  = new HashMap<>();
	private List<Item>                       items                           = new ArrayList<>();
	private Map<Skill, Short>                currentLevelSkillRanks          = new HashMap<>();
	private Map<Specialization, Short>       currentLevelSpecializationRanks = new HashMap<>();
	private Map<SpellList, Short>            currentLevelSpellListRanks      = new HashMap<>();
	private Map<TalentInstance, Short>       currentLevelTalentTiers         = new HashMap<>();
	private Map<DatabaseObject, Short>       purchasedCultureRanks           = new HashMap<>();
	private int                              statIncreases                   = 0;
	private List<State>                      currentStates                   = new ArrayList<>();
	private List<DatabaseObject>             professionSkills                = new ArrayList<>();
	private List<DatabaseObject>             knacks                          = new ArrayList<>();
	private Item                             mainHandItem                    = null;
	private Item                             offhandItem                     = null;
	private Item                             shirtItem                       = null;
	private Item                             pantsItem                       = null;
	private Item                             headItem                        = null;
	private Item                             chestItem                       = null;
	private Item                             armsItem                        = null;
	private Item                             handsItem                       = null;
	private Item                             legsItem                        = null;
	private Item                             feetItem                        = null;
	private Item                             backItem                        = null;
	private Item                             backpackItem                    = null;

	/**
	 * Creates a new Character instance with default values
	 */
	public Character() {
	}

	/**
	 * Creates a new Character instance with the give id.
	 *
	 * @param id  the id of the new instance
	 */
	public Character(int id) {
		super(id);
	}

	/**
	 * Checks the validity of the Character instance.
	 *
	 * @return true if the Character instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return campaign != null && firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty()
				&& description != null && !description.isEmpty() && race != null && profession != null && culture != null
				&& (profession.getRealm1() != null || realm != null);
	}

	/**
	 * Gets the full name of the character formatted to make sense.
	 *
	 * @return  the full name of the character
	 */
	public String getFullName() {
		StringBuilder builder = new StringBuilder();
		builder.append(getFirstName()).append(" ");
		if(!getFirstName().equals(getKnownAs()) && !getKnownAs().isEmpty()) {
			builder.append("\"").append(getKnownAs()).append("\" ");
		}
		builder.append(getLastName());
		return builder.toString();
	}

	/**
	 * Generates initial stats for this character. If the Campaign for this character is not null and returns true for
	 * isBuyStats() then the temps are set to 53 (0 bonus) and the potentials are set to 100. Otherwise the temps and
	 * potentials are randomly generated using the method specified in the section 3.5 of A&CL.
	 */
	public void generateStats() {
		if(getCampaign() != null && getCampaign().isBuyStats()) {
			getStatTemps().put(Statistic.AGILITY, (short)53);
			getStatPotentials().put(Statistic.AGILITY, (short)100);
			getStatTemps().put(Statistic.CONSTITUTION, (short)53);
			getStatPotentials().put(Statistic.CONSTITUTION, (short)100);
			getStatTemps().put(Statistic.EMPATHY, (short)53);
			getStatPotentials().put(Statistic.EMPATHY, (short)100);
			getStatTemps().put(Statistic.INTUITION, (short)53);
			getStatPotentials().put(Statistic.INTUITION, (short)100);
			getStatTemps().put(Statistic.MEMORY, (short)53);
			getStatPotentials().put(Statistic.MEMORY, (short)100);
			getStatTemps().put(Statistic.PRESENCE, (short)53);
			getStatPotentials().put(Statistic.PRESENCE, (short)100);
			getStatTemps().put(Statistic.QUICKNESS, (short)53);
			getStatPotentials().put(Statistic.QUICKNESS, (short)100);
			getStatTemps().put(Statistic.REASONING, (short)53);
			getStatPotentials().put(Statistic.REASONING, (short)100);
			getStatTemps().put(Statistic.SELF_DISCIPLINE, (short)53);
			getStatPotentials().put(Statistic.SELF_DISCIPLINE, (short)100);
			getStatTemps().put(Statistic.STRENGTH, (short)53);
			getStatPotentials().put(Statistic.STRENGTH, (short)100);
		}
		else {
			generateRandomStat(Statistic.AGILITY);
			generateRandomStat(Statistic.CONSTITUTION);
			generateRandomStat(Statistic.EMPATHY);
			generateRandomStat(Statistic.INTUITION);
			generateRandomStat(Statistic.MEMORY);
			generateRandomStat(Statistic.PRESENCE);
			generateRandomStat(Statistic.QUICKNESS);
			generateRandomStat(Statistic.REASONING);
			generateRandomStat(Statistic.SELF_DISCIPLINE);
			generateRandomStat(Statistic.STRENGTH);
		}
	}

	@Override
	public String toString() {
		return knownAs;
	}

	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("campaign", campaign)
				.append("currentLevel", currentLevel)
				.append("experiencePoints", experiencePoints)
				.append("statPurchasePoints", statPurchasePoints)
				.append("firstName", firstName)
				.append("lastName", lastName)
				.append("knownAs", knownAs)
				.append("description", description)
				.append("hairColor", hairColor)
				.append("hairStyle", hairStyle)
				.append("eyeColor", eyeColor)
				.append("skinComplexion", skinComplexion)
				.append("facialFeatures", facialFeatures)
				.append("identifyingMarks", identifyingMarks)
				.append("personality", personality)
				.append("mannerisms", mannerisms)
				.append("hometown", hometown)
				.append("familyInfo", familyInfo)
				.append("race", race)
				.append("culture", culture)
				.append("profession", profession)
				.append("realm", realm)
				.append("realm2", realm2)
				.append("realm3", realm3)
				.append("height", height)
				.append("weight", weight)
				.append("hitPointLoss", hitPointLoss)
				.append("currentDevelopmentPoints", currentDevelopmentPoints)
				.append("fatigue", fatigue)
				.append("powerPointLoss", powerPointLoss)
				.append("skillCosts", skillCosts)
				.append("skillRanks", skillRanks)
				.append("specializationRanks", specializationRanks)
				.append("spellListRanks", spellListRanks)
				.append("talentInstances", talentInstances)
				.append("statTemps", statTemps)
				.append("statPotentials", statPotentials)
				.append("items", items)
				.append("currentLevelSkillRanks", currentLevelSkillRanks)
				.append("currentLevelSpecializationRanks", currentLevelSpecializationRanks)
				.append("currentLevelSpellListRanks", currentLevelSpellListRanks)
				.append("currentLevelTalentTiers", currentLevelTalentTiers)
				.append("purchasedCultureRanks", purchasedCultureRanks)
				.append("statIncreases", statIncreases)
				.append("professionSkills", professionSkills)
				.append("knacks", knacks)
				.append("mainHandItem", mainHandItem)
				.append("offhandItem", offhandItem)
				.append("shirtItem", shirtItem)
				.append("pantsItem", pantsItem)
				.append("headItem", headItem)
				.append("chestItem", chestItem)
				.append("armsItem", armsItem)
				.append("legsItem", legsItem)
				.append("feetItem", feetItem)
				.append("backItem", backItem)
				.toString();
	}

	/**
	 * Adds a Statistic to the statistics that have been increased this level.
	 *
	 * @param statistic  the Statistic that is being increased
	 */
	public void addStatIncrease(Statistic statistic) {
		statIncreases |= (1 << statistic.ordinal());
	}

	/**
	 * Checks if a Statistic has been increase this level.
	 *
	 * @param statistic  the Statistic to check
	 * @return true if the Statistic has been increased this level, otherwise false.
	 */
	public boolean isStatIncreased(Statistic statistic) {
		return (statIncreases & (1 << statistic.ordinal())) != 0;
	}

	/**
	 * Sets the value of statIncreases to a specific value. Typically this should only be used when the value is read from
	 * persistent storage.
	 *
	 * @param statIncreases the value to set
	 */
	public void setStatIncreases(int statIncreases) {
		this.statIncreases = statIncreases;
	}

	/**
	 * Clears the value of statIncreases to indicate that no stats have been increased this level.
	 */
	public void clearStatIncreases() {
		this.statIncreases = 0;
	}

	/**
	 * Returns the count of how many Statistics have been increased this level.
	 *
	 * @return  the count of how many Statistics have been increased this level.
	 */
	public int statsIncreasedCount() {
		int result = 0;

		for(int i = 1; i < Integer.MAX_VALUE; i <<= 1) {
			if((statIncreases & i) != 0) {
				result++;
			}
		}

		return result;
	}

	/**
	 * Calculates the total bonus for the given {@link Statistic}.
	 *
	 * @param statistic  a Statistic instance
	 * @return  the calculated bonus including the current stat value, race bonus, and any talent bonuses.
	 */
	public short getTotalStatBonus(Statistic statistic) {
		// TODO: Add talent stat bonuses to total if they exist.
		short bonus = 0;
		Short tempShort = getStatTemps().get(statistic);
		if(tempShort != null) {
			bonus = Statistic.getBonus(tempShort);
		}
		if(getRace() != null) {
			tempShort = getRace().getStatModifiers().get(statistic);
			if (tempShort != null) {
				bonus += tempShort;
			}
		}

		return bonus;
	}

	/**
	 * Calculates the initiative penalty
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
					int maxHits = race.getBaseHits()
							+ (Statistic.getBonus(getStatTemps().get(Statistic.CONSTITUTION))*2)
							+ Statistic.getBonus(getStatTemps().get(Statistic.SELF_DISCIPLINE))
							+ getSkillRanks().get(new Skill("Body Development"));
					for(TalentInstance talentInstance : getTalentInstances()) {
						if(talentInstance.getTalent().getName().equals("Tough")) {
							maxHits += talentInstance.getTiers() * 5;
						}
						if(talentInstance.getTalent().getName().equals("Fragile")) {
							maxHits -= talentInstance.getTiers() * 5;
						}
					}
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

	public TalentInstance findTalentInstanceForTalent(Talent talent) {
		TalentInstance result = null;

		for(TalentInstance talentInstance : getTalentInstances()) {
			if(talentInstance.getTalent().equals(talent)) {
				result = talentInstance;
				break;
			}
		}
		return result;
	}

	public TalentInstance findTalentInstanceById(int id) {
		TalentInstance result = null;

		for(TalentInstance talentInstance : getTalentInstances()) {
			if(talentInstance.getId() == id) {
				result = talentInstance;
				break;
			}
		}

		return result;
	}

	private void generateRandomStat(Statistic statistic) {
		short roll;
		short rolls[] = new short[2];
		Random random = new Random();

		for(int i = 0; i < 3;) {
			roll = (short)(random.nextInt(99) + 1);
			short reRollUnder = 11;
			if(getCampaign() != null && getCampaign().getPowerLevel() != null) {
				reRollUnder = getCampaign().getPowerLevel().getRerollUnder();
			}
			if(roll >= reRollUnder) {
				switch (i) {
					case 2:
						if (roll > rolls[1]) {
							rolls[0] = rolls[1];
							rolls[1] = roll;
						} else if (roll > rolls[0]) {
							rolls[0] = roll;
						}
						break;
					case 1:
						if(roll >= rolls[0]) {
							rolls[1] = roll;
						}
						else {
							rolls[1] = rolls[0];
							rolls[0] = roll;
						}
						break;
					case 0:
						rolls[0] = roll;
						break;
				}
				i++;
			}
		}
		getStatTemps().put(statistic, rolls[0]);
		getStatPotentials().put(statistic, rolls[1]);
	}

	/**
	 * Gets the offensive bonus for the currently equipped weapon
	 *
	 * @return the character's offensive bonus.
	 */
	public short getOffensiveBonus() {
		short result = -25;

		Log.d(TAG, "getOffensiveBonus: mainHandItem = " + mainHandItem.print());
		if(mainHandItem != null && mainHandItem instanceof Weapon) {
			Log.d(TAG, "getOffensiveBonus: weaponTemplate = " + mainHandItem.getItemTemplate().print());
			WeaponTemplate weaponTemplate = (WeaponTemplate)mainHandItem.getItemTemplate();
			Skill skill = weaponTemplate.getCombatSkill();
			Log.d(TAG, "getOffensiveBonus: skill = " + skill.print());
			result = skill.getRankBonus(getSkillRanks().get(skill));
		}

		return result;
	}

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
	public int getExperiencePoints() {
		return experiencePoints;
	}
	public void setExperiencePoints(int experiencePoints) {
		this.experiencePoints = experiencePoints;
	}
	public short getStatPurchasePoints() {
		return statPurchasePoints;
	}
	public void setStatPurchasePoints(short statPurchasePoints) {
		this.statPurchasePoints = statPurchasePoints;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getKnownAs() {
		return knownAs;
	}
	public void setKnownAs(String knownAs) {
		this.knownAs = knownAs;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHairColor() {
		return hairColor;
	}
	public void setHairColor(String hairColor) {
		this.hairColor = hairColor;
	}
	public String getHairStyle() {
		return hairStyle;
	}
	public void setHairStyle(String hairStyle) {
		this.hairStyle = hairStyle;
	}
	public String getEyeColor() {
		return eyeColor;
	}
	public void setEyeColor(String eyeColor) {
		this.eyeColor = eyeColor;
	}
	public String getSkinComplexion() {
		return skinComplexion;
	}
	public void setSkinComplexion(String skinComplexion) {
		this.skinComplexion = skinComplexion;
	}
	public String getFacialFeatures() {
		return facialFeatures;
	}
	public void setFacialFeatures(String facialFeatures) {
		this.facialFeatures = facialFeatures;
	}
	public String getIdentifyingMarks() {
		return identifyingMarks;
	}
	public void setIdentifyingMarks(String identifyingMarks) {
		this.identifyingMarks = identifyingMarks;
	}
	public String getPersonality() {
		return personality;
	}
	public void setPersonality(String personality) {
		this.personality = personality;
	}
	public String getMannerisms() {
		return mannerisms;
	}
	public void setMannerisms(String mannerisms) {
		this.mannerisms = mannerisms;
	}
	public String getHometown() {
		return hometown;
	}
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}
	public String getFamilyInfo() {
		return familyInfo;
	}
	public void setFamilyInfo(String familyInfo) {
		this.familyInfo = familyInfo;
	}
	public Race getRace() {
		return race;
	}
	public void setRace(Race race) {
		this.race = race;
	}
	public Culture getCulture() {
		return culture;
	}
	public void setCulture(Culture culture) {
		this.culture = culture;
	}
	public Profession getProfession() {
		return profession;
	}
	public void setProfession(Profession profession) {
		this.profession = profession;
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
	public short getCurrentDevelopmentPoints() {
		return currentDevelopmentPoints;
	}
	public void setCurrentDevelopmentPoints(short currentDevelopmentPoints) {
		this.currentDevelopmentPoints = currentDevelopmentPoints;
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
	public Map<Skill, DevelopmentCostGroup> getSkillCosts() {
		return skillCosts;
	}
	public void setSkillCosts(Map<Skill, DevelopmentCostGroup> skillCosts) {
		this.skillCosts = skillCosts;
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
	public Map<Statistic, Short> getStatTemps() {
		return statTemps;
	}
	public void setStatTemps(Map<Statistic, Short> statTemps) {
		this.statTemps = statTemps;
	}
	public Map<Statistic, Short> getStatPotentials() {
		return statPotentials;
	}
	public void setStatPotentials(Map<Statistic, Short> statPotentials) {
		this.statPotentials = statPotentials;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public Map<Skill, Short> getCurrentLevelSkillRanks() {
		return currentLevelSkillRanks;
	}
	public void setCurrentLevelSkillRanks(Map<Skill, Short> currentLevelSkillRanks) {
		this.currentLevelSkillRanks = currentLevelSkillRanks;
	}
	public Map<Specialization, Short> getCurrentLevelSpecializationRanks() {
		return currentLevelSpecializationRanks;
	}
	public void setCurrentLevelSpecializationRanks(Map<Specialization, Short> currentLevelSpecializationRanks) {
		this.currentLevelSpecializationRanks = currentLevelSpecializationRanks;
	}
	public Map<SpellList, Short> getCurrentLevelSpellListRanks() {
		return currentLevelSpellListRanks;
	}
	public void setCurrentLevelSpellListRanks(
			Map<SpellList, Short> currentLevelSpellListRanks) {
		this.currentLevelSpellListRanks = currentLevelSpellListRanks;
	}
	public Map<TalentInstance, Short> getCurrentLevelTalentTiers() {
		return currentLevelTalentTiers;
	}
	public void setCurrentLevelTalentTiers(
			Map<TalentInstance, Short> currentLevelTalentTiers) {
		this.currentLevelTalentTiers = currentLevelTalentTiers;
	}
	public Map<DatabaseObject, Short> getPurchasedCultureRanks() {
		return purchasedCultureRanks;
	}
	public void setPurchasedCultureRanks(Map<DatabaseObject, Short> purchasedCultureRanks) {
		this.purchasedCultureRanks = purchasedCultureRanks;
	}
	public int getStatIncreases() {
		return statIncreases;
	}
	public List<DatabaseObject> getProfessionSkills() {
		return professionSkills;
	}
	public void setProfessionSkills(List<DatabaseObject> professionSkills) {
		this.professionSkills = professionSkills;
	}
	public List<DatabaseObject> getKnacks() {
		return knacks;
	}
	public void setKnacks(List<DatabaseObject> knacks) {
		this.knacks = knacks;
	}
	public List<State> getCurrentStates() {
		return currentStates;
	}
	public void setCurrentStates(List<State> currentStates) {
		this.currentStates = currentStates;
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
}
