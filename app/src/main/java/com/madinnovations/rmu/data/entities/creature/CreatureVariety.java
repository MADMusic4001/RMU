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
package com.madinnovations.rmu.data.entities.creature;

import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.CriticalCode;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.SkillBonus;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.spells.Realm;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class containing Creature Variety attributes.
 */
public class CreatureVariety extends DatabaseObject {
	public static final String    JSON_NAME            = "CreatureVarieties";
	private CreatureType          type                 = null;
	private String                name                 = null;
	private String                description          = null;
	private short                 typicalLevel         = 1;
	private char                  levelSpread          = 'A';
	private Map<Statistic, Short> racialStatBonuses    = new HashMap<>();
	private short                 height               = 60;
	private short                 length               = 60;
	private short                 weight               = 200;
	private short                 healingRate          = 1;
	private short                 baseHits             = 25;
	private short                 baseEndurance        = 0;
	private Size                  size                 = null;
	private short                 armorType            = 1;
	private List<CriticalCode>    criticalCodes        = new ArrayList<>();
	private short                 baseMovementRate     = 15;
	private short                 baseChannellingRR    = 0;
	private short                 baseEssenceRR        = 0;
	private short                 baseMentalismRR      = 0;
	private short                 basePhysicalRR       = 0;
	private short                 baseFearRR           = 0;
	private Realm                 realm1               = null;
	private Realm                realm2                = null;
	private short                baseStride            = 0;
	private short                leftoverDP            = 200;
	private Outlook              outlook               = null;
	private List<TalentInstance> talentInstancesList   = new ArrayList<>();
	private Map<Attack, Short>   attackBonusesMap      = new HashMap<>();
	private Size                 criticalSizeModifier  = null;
	private String               attackSequence        = null;
	private List<SkillBonus>     skillBonusesList      = new ArrayList<>();

	/**
	 * Checks the validity of the CreatureVariety instance.
	 *
	 * @return true if the CreatureVariety instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() && type != null && size != null &&
				attackSequence != null && realm1 != null && outlook != null;
	}

	/**
	 * Initializes the racial stat bonuses for this creature variety.
	 *
	 * @param stats  the stats to initialize
	 */
	public void initRacialStatBonusList(List<Statistic> stats) {
		for(Statistic stat : stats) {
			racialStatBonuses.put(stat, (short)0);
		}
	}

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Generates a debug string containing the member variables for this instance.
	 *
	 * @return a debug string containing the member variables for this instance.
	 */
	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("type", type)
				.append("name", name)
				.append("description", description)
				.append("typicalLevel", typicalLevel)
				.append("levelSpread", levelSpread)
				.append("racialStatBonuses", racialStatBonuses)
				.append("height", height)
				.append("length", length)
				.append("weight", weight)
				.append("healingRate", healingRate)
				.append("baseHits", baseHits)
				.append("baseEndurance", baseEndurance)
				.append("size", size)
				.append("armorType", armorType)
				.append("criticalCodes", criticalCodes)
				.append("baseMovementRate", baseMovementRate)
				.append("baseChannellingRR", baseChannellingRR)
				.append("baseEssenceRR", baseEssenceRR)
				.append("baseMentalismRR", baseMentalismRR)
				.append("basePhysicalRR", basePhysicalRR)
				.append("baseFearRR", baseFearRR)
				.append("realm1", realm1)
				.append("realm2", realm2)
				.append("baseStride", baseStride)
				.append("leftoverDP", leftoverDP)
				.append("outlook", outlook)
				.append("talentInstancesList", talentInstancesList)
				.append("attackBonusesMap", attackBonusesMap)
				.append("criticalSizeModifier", criticalSizeModifier)
				.append("attackSequence", attackSequence)
				.append("skillBonusesList", skillBonusesList)
				.toString();
	}

	// Getters and setters
	public CreatureType getType() {
		return type;
	}
	public void setType(CreatureType type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public short getTypicalLevel() {
		return typicalLevel;
	}
	public void setTypicalLevel(short typicalLevel) {
		this.typicalLevel = typicalLevel;
	}
	public char getLevelSpread() {
		return levelSpread;
	}
	public void setLevelSpread(char levelSpread) {
		this.levelSpread = levelSpread;
	}
	public Map<Statistic, Short> getRacialStatBonuses() {
		return racialStatBonuses;
	}
	public void setRacialStatBonuses(Map<Statistic, Short> racialStatBonuses) {
		this.racialStatBonuses = racialStatBonuses;
	}
	public short getHeight() {
		return height;
	}
	public void setHeight(short height) {
		this.height = height;
	}
	public short getLength() {
		return length;
	}
	public void setLength(short length) {
		this.length = length;
	}
	public short getWeight() {
		return weight;
	}
	public void setWeight(short weight) {
		this.weight = weight;
	}
	public short getHealingRate() {
		return healingRate;
	}
	public void setHealingRate(short healingRate) {
		this.healingRate = healingRate;
	}
	public short getBaseHits() {
		return baseHits;
	}
	public void setBaseHits(short baseHits) {
		this.baseHits = baseHits;
	}
	public short getBaseEndurance() {
		return baseEndurance;
	}
	public void setBaseEndurance(short baseEndurance) {
		this.baseEndurance = baseEndurance;
	}
	public Size getSize() {
		return size;
	}
	public void setSize(Size size) {
		this.size = size;
	}
	public short getArmorType() {
		return armorType;
	}
	public void setArmorType(short armorType) {
		this.armorType = armorType;
	}
	public List<CriticalCode> getCriticalCodes() {
		return criticalCodes;
	}
	public void setCriticalCodes(List<CriticalCode> criticalCodes) {
		this.criticalCodes = criticalCodes;
	}
	public short getBaseMovementRate() {
		return baseMovementRate;
	}
	public void setBaseMovementRate(short baseMovementRate) {
		this.baseMovementRate = baseMovementRate;
	}
	public short getBaseChannellingRR() {
		return baseChannellingRR;
	}
	public void setBaseChannellingRR(short baseChannellingRR) {
		this.baseChannellingRR = baseChannellingRR;
	}
	public short getBaseEssenceRR() {
		return baseEssenceRR;
	}
	public void setBaseEssenceRR(short baseEssenceRR) {
		this.baseEssenceRR = baseEssenceRR;
	}
	public short getBaseMentalismRR() {
		return baseMentalismRR;
	}
	public void setBaseMentalismRR(short baseMentalismRR) {
		this.baseMentalismRR = baseMentalismRR;
	}
	public short getBasePhysicalRR() {
		return basePhysicalRR;
	}
	public void setBasePhysicalRR(short basePhysicalRR) {
		this.basePhysicalRR = basePhysicalRR;
	}
	public short getBaseFearRR() {
		return baseFearRR;
	}
	public void setBaseFearRR(short baseFearRR) {
		this.baseFearRR = baseFearRR;
	}
	public Realm getRealm1() {
		return realm1;
	}
	public void setRealm1(Realm realm1) {
		this.realm1 = realm1;
	}
	public Realm getRealm2() {
		return realm2;
	}
	public void setRealm2(Realm realm2) {
		this.realm2 = realm2;
	}
	public short getBaseStride() {
		return baseStride;
	}
	public void setBaseStride(short baseStride) {
		this.baseStride = baseStride;
	}
	public short getLeftoverDP() {
		return leftoverDP;
	}
	public void setLeftoverDP(short leftoverDP) {
		this.leftoverDP = leftoverDP;
	}
	public Outlook getOutlook() {
		return outlook;
	}
	public void setOutlook(Outlook outlook) {
		this.outlook = outlook;
	}
	public List<TalentInstance> getTalentInstancesList() {
		return talentInstancesList;
	}
	public void setTalentInstancesList(List<TalentInstance> talentsMap) {
		this.talentInstancesList = talentInstancesList;
	}
	public Map<Attack, Short> getAttackBonusesMap() {
		return attackBonusesMap;
	}
	public void setAttackBonusesMap(Map<Attack, Short> attackBonusesMap) {
		this.attackBonusesMap = attackBonusesMap;
	}
	public Size getCriticalSizeModifier() {
		return criticalSizeModifier;
	}
	public void setCriticalSizeModifier(Size criticalSizeModifier) {
		this.criticalSizeModifier = criticalSizeModifier;
	}
	public String getAttackSequence() {
		return attackSequence;
	}
	public void setAttackSequence(String attackSequence) {
		this.attackSequence = attackSequence;
	}
	public List<SkillBonus> getSkillBonusesList() {
		return skillBonusesList;
	}
	public void setSkillBonusesList(List<SkillBonus> skillBonusesList) {
		this.skillBonusesList = skillBonusesList;
	}
}
