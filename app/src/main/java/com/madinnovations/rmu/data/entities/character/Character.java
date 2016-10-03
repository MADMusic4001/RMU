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

import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCost;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.spells.Realm;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;

/**
 * Character attributes
 */
public class Character {
	public static final String JSON_NAME = "Characters";
	private int                   id = -1;
	private short                 currentLevel = 0;
	private String                firstName = null;
	private String                lastName = null;
	private String                knownAs = null;
	private String                description = null;
	private String                hairColor = null;
	private String                hairStyle = null;
	private String                eyeColor = null;
	private String                skinComplexion = null;
	private String                facialFeatures = null;
	private String                identifyingMarks = null;
	private String                personality = null;
	private String                mannerisms = null;
	private String                hometown = null;
	private String                familyInfo = null;
	private Race                  race = null;
	private Culture               culture = null;
	private Profession            profession = null;
	private Realm                 realm = null;
	private short                 height = 70;
	private short                 weight = 185;
	private short                 currentHits = 25;
	private short                 maxHits = 25;
	private short                 currentDevelopmentPoints = 50;
	private Map<Skill, SkillCost> skillCosts = new HashMap<>();
	private Map<Skill, Short>     skillRanks = new HashMap<>();
	private Map<Talent, Short>    talentTiers = new HashMap<>();
	private Map<Stat, Short>      statTemps = new HashMap<>();
	private Map<Stat, Short>      statPotentials = new HashMap<>();

	/**
	 * Checks the validity of the Character instance.
	 *
	 * @return true if the Character instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty() && description != null
				&& !description.isEmpty() && race != null && profession != null && culture != null
				&& (profession.getRealm1() != null || realm != null);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", id)
				.append("currentLevel", currentLevel)
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
				.append("currentHits", currentHits)
				.append("maxHits", maxHits)
				.append("currentDevelopmentPoints", currentDevelopmentPoints)
				.append("skillCosts", skillCosts)
				.append("skillRanks", skillRanks)
				.append("talentTiers", talentTiers)
				.append("statTemps", statTemps)
				.append("statPotentials", statPotentials)
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

	// Getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public short getCurrentLevel() {
		return currentLevel;
	}
	public void setCurrentLevel(short currentLevel) {
		this.currentLevel = currentLevel;
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
	public short getCurrentHits() {
		return currentHits;
	}
	public void setCurrentHits(short currentHits) {
		this.currentHits = currentHits;
	}
	public short getMaxHits() {
		return maxHits;
	}
	public void setMaxHits(short maxHits) {
		this.maxHits = maxHits;
	}
	public short getCurrentDevelopmentPoints() {
		return currentDevelopmentPoints;
	}
	public void setCurrentDevelopmentPoints(short currentDevelopmentPoints) {
		this.currentDevelopmentPoints = currentDevelopmentPoints;
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
	public Map<Talent, Short> getTalentTiers() {
		return talentTiers;
	}
	public void setTalentTiers(Map<Talent, Short> talentTiers) {
		this.talentTiers = talentTiers;
	}
	public Map<Stat, Short> getStatTemps() {
		return statTemps;
	}
	public void setStatTemps(Map<Stat, Short> statTemps) {
		this.statTemps = statTemps;
	}
	public Map<Stat, Short> getStatPotentials() {
		return statPotentials;
	}
	public void setStatPotentials(Map<Stat, Short> statPotentials) {
		this.statPotentials = statPotentials;
	}
}
