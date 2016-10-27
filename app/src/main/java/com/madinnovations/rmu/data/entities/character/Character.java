/**
 * Copyright (C) 2016 MadInnovations
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.entities.character;

import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCost;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.spells.Realm;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Character attributes
 */
public class Character {
	public static final String JSON_NAME = "Characters";
	public static final short INITIAL_DP = 50;
	private int                        id = -1;
	private Campaign                   campaign = null;
	private short                      currentLevel = 0;
	private int                        experiencePoints = 0;
	private short                      statPurchasePoints = 0;
	private String                     firstName = null;
	private String                     lastName = null;
	private String                     knownAs = null;
	private String                     description = null;
	private String                     hairColor = null;
	private String                     hairStyle = null;
	private String                     eyeColor = null;
	private String                     skinComplexion = null;
	private String                     facialFeatures = null;
	private String                     identifyingMarks = null;
	private String                     personality = null;
	private String                     mannerisms = null;
	private String                     hometown = null;
	private String                     familyInfo = null;
	private Race                       race = null;
	private Culture                    culture = null;
	private Profession                 profession = null;
	private Realm                      realm = null;
	private short                      height = 70;
	private short                      weight = 185;
	private int                        hitPointLoss = 0;
	private short                      currentDevelopmentPoints = INITIAL_DP;
	private short                      enduranceLoss;
	private short                      powerPointLoss;
	private Map<Skill, SkillCost>      skillCosts          = new HashMap<>();
	private Map<Skill, Short>          skillRanks          = new HashMap<>();
	private Map<Specialization, Short> specializationRanks = new HashMap<>();
	private Map<Talent, Short>         talentTiers         = new HashMap<>();
	private Map<Statistic, Short>      statTemps           = new HashMap<>();
	private Map<Statistic, Short>      statPotentials      = new HashMap<>();
	private List<Item>                 items               = new ArrayList<>();
	private Map<Skill, Short>          currentLevelSkillRanks = new HashMap<>();
	private Map<Specialization, Short> currentLevelSpecializationRanks = new HashMap<>();

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
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", id)
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
				.append("height", height)
				.append("weight", weight)
				.append("hitPointLoss", hitPointLoss)
				.append("currentDevelopmentPoints", currentDevelopmentPoints)
				.append("enduranceLoss", enduranceLoss)
				.append("powerPointLoss", powerPointLoss)
				.append("skillCosts", skillCosts)
				.append("skillRanks", skillRanks)
				.append("specializationRanks", specializationRanks)
				.append("talentTiers", talentTiers)
				.append("statTemps", statTemps)
				.append("statPotentials", statPotentials)
				.append("items", items)
				.append("currentLevelSkillRanks", currentLevelSkillRanks)
				.append("currentLevelSpecializationRanks", currentLevelSpecializationRanks)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Character character = (Character) o;

		return id == character.id;
	}

	@Override
	public int hashCode() {
		return id;
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

	// Getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public short getEnduranceLoss() {
		return enduranceLoss;
	}
	public void setEnduranceLoss(short enduranceLoss) {
		this.enduranceLoss = enduranceLoss;
	}
	public short getPowerPointLoss() {
		return powerPointLoss;
	}
	public void setPowerPointLoss(short powerPointLoss) {
		this.powerPointLoss = powerPointLoss;
	}
	public Map<Skill, SkillCost> getSkillCosts() {
		return skillCosts;
	}
	public void setSkillCosts(Map<Skill, SkillCost> skillCosts) {
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
	public Map<Talent, Short> getTalentTiers() {
		return talentTiers;
	}
	public void setTalentTiers(Map<Talent, Short> talentTiers) {
		this.talentTiers = talentTiers;
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
}
