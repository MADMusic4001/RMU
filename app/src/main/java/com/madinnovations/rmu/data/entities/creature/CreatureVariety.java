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

import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SizeRxHandler;
import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.CreatureAttack;
import com.madinnovations.rmu.data.entities.combat.CriticalCode;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.SkillBonus;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.spells.RealmDBO;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Subscriber;

/**
 * Class containing Creature Variety attributes.
 */
public class CreatureVariety extends DatabaseObject {
	public static final  String                    JSON_NAME            = "CreatureVarieties";
	private static final String                    TAG                  = "CreatureVariety";
	private static final String BONUS_REG_EX               = "(-?\\d+)";
	private static final String SIZE_REG_EX                = "(Mi\\(1\\)|D\\(2\\)|T\\(3\\)|S\\(4\\)|M\\(5\\)|B\\(6\\)|L\\(7\\)|H\\(8\\)|"
		+ "G\\(9\\)|E\\(10\\)|I\\(11\\)|O\\(12\\)|V\\(13\\))";
	private static final String ATTACK_ABBR_REG_EX         = "([a-z]{2,})";
	private static final String ELEMENTAL_TYPE_REG_EX      = "(\\[[A-Z][a-z]+\\])";
	private static final String COOP_INDICATOR_REG_EX      = "(Coop)";
	private static final String COOP_COUNT_REG_EX          = "(\\d+)";
	private static final String TRIGGER_ON_CRITICAL_REG_EX = "(>{0,2})";
	private static final String KATA_COUNT_REG_EX          = "([0-9]*)";
	private static final String KATA_INDICATOR_REG_EX      = "(WF)?";
	private static final String ATTACK_SEPARATOR_REG_EX    = "\\s*;*";
	private AttackRxHandler           attackRxHandler = null;
	private SizeRxHandler             sizeRxHandler = null;
	private CreatureType              type                 = null;
	private String                    name                 = null;
	private String                    description          = null;
	private short                     typicalLevel         = 1;
	private char                      levelSpread          = 'A';
	private Map<Statistic, Short>     racialStatBonuses    = new HashMap<>();
	private short                     height               = 60;
	private short                     length               = 60;
	private short                     weight               = 200;
	private short                     healingRate          = 1;
	private short                     baseHits             = 25;
	private short                     baseEndurance        = 0;
	private Size                      size                 = null;
	private              short                     armorType            = 1;
	private              List<CriticalCode>        criticalCodes        = new ArrayList<>();
	private              short                     baseMovementRate     = 15;
	private              short                     baseChannellingRR    = 0;
	private              short                     baseEssenceRR        = 0;
	private              short                     baseMentalismRR      = 0;
	private              short                     basePhysicalRR       = 0;
	private              short                     baseFearRR           = 0;
	private              RealmDBO                  realmDBO1            = null;
	private              RealmDBO                  realmDBO2            = null;
	private              short                     baseStride           = 0;
	private              short                     leftoverDP           = 200;
	private              Outlook                   outlook              = null;
	private              List<TalentInstance>      talentInstancesList  = new ArrayList<>();
	private              Map<Attack, Short>        attackBonusesMap     = new HashMap<>();
	private              Size                      criticalSizeModifier = null;
	private              String                    attackSequence       = null;
	private              List<SkillBonus>          skillBonusesList     = new ArrayList<>();
	private              ArrayList<CreatureAttack> attackList           = null;

	/**
	 * Checks the validity of the CreatureVariety instance.
	 *
	 * @return true if the CreatureVariety instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() && type != null && size != null &&
				attackSequence != null && realmDBO1 != null && outlook != null;
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

	public void parseAttackSequence() {
		if(attackList == null) {
			String patternString = BONUS_REG_EX
					+ SIZE_REG_EX
					+ ATTACK_ABBR_REG_EX
					+ "("
					+ ELEMENTAL_TYPE_REG_EX
					+ "\\(?"
					+ COOP_INDICATOR_REG_EX
					+ COOP_COUNT_REG_EX
					+ "\\)?"
					+ "|"
					+ TRIGGER_ON_CRITICAL_REG_EX
					+ "|"
					+ "\\(?"
					+ KATA_COUNT_REG_EX
					+ KATA_INDICATOR_REG_EX
					+ "\\)?"
					+ ")"
					+ ATTACK_SEPARATOR_REG_EX;
			attackList = new ArrayList<>();
			Pattern pattern = Pattern.compile(patternString);
			final Matcher matcher = pattern.matcher(attackSequence);
			while (matcher.find()) {
				final CreatureAttack creatureAttack = new CreatureAttack();
				for (int i = 0; i < matcher.groupCount(); i++) {
					switch (i) {
						case 1:
							creatureAttack.setOffensiveBonus(Short.valueOf(matcher.group(1)));
							break;
						case 2:
							String romanNumeralCode = null;
							switch (matcher.group(2)) {
								case "Mi(1)":
									romanNumeralCode = "I";
									break;
								case "D(2)":
									romanNumeralCode ="II";
									break;
								case "T(3)":
									romanNumeralCode = "III";
									break;
								case "S(4)":
									romanNumeralCode = "IV";
									break;
								case "M(5)":
									romanNumeralCode = "V";
									break;
								case "B(6)":
									romanNumeralCode = "VI";
									break;
								case "L(7)":
									romanNumeralCode = "VII";
									break;
								case "H(8)":
									romanNumeralCode = "VIII";
									break;
								case "G(9)":
									romanNumeralCode = "IX";
									break;
								case "E(10)":
									romanNumeralCode = "X";
									break;
								case "I(11)":
									romanNumeralCode = "XI";
									break;
								case "O(12)":
									romanNumeralCode = "XII";
									break;
								case "V(13)":
									romanNumeralCode = "XIII";
									break;
							}
							if(romanNumeralCode != null) {
								sizeRxHandler.getByCode(romanNumeralCode)
										.subscribe(new Subscriber<Size>() {
											@Override
											public void onCompleted() {}
											@Override
											public void onError(Throwable e) {
												Log.e(TAG, "onError: Exception when loading Size " + matcher.group(2), e);
											}
											@Override
											public void onNext(Size size) {
												creatureAttack.setSize(size);
											}
										});
							}
							break;
						case 3:
							if("all".equals(matcher.group(3)) && "WF".equals(matcher.group(5))) {
								creatureAttack.setKataCount(Short.valueOf(matcher.group(4)));
							}
							else {
								String tempString = matcher.group(3);
								if(tempString.endsWith("Coop")) {
									tempString = tempString.substring(0, tempString.lastIndexOf("Coop"));
								}
								final String attackString = tempString;
								attackRxHandler.getByCode(attackString)
										.subscribe(new Subscriber<Attack>() {
											@Override
											public void onCompleted() {}
											@Override
											public void onError(Throwable e) {
												Log.e(TAG, "Exception caught getting Attack " + attackString, e);
											}
											@Override
											public void onNext(Attack attack) {
												creatureAttack.setBaseAttack(attack);
											}
										});
							}
							break;
						case 4:
							if(">".equals(matcher.group(4))) {
								creatureAttack.setCriticalFollowUp(true);
								creatureAttack.setSameRoundFollowUp(true);
							}
							if(">>".equals(matcher.group(4))) {
								creatureAttack.setCriticalFollowUp(true);
							}
							if("Coop".equals(matcher.group(4))) {
								try {
									creatureAttack.setCoopNumber(Short.valueOf(matcher.group(5)));
								}
								catch(NumberFormatException e) {
									Log.e(TAG, "Exception caught parsing coopNumber " + matcher.group(5), e);
								}
							}
							if("WF".equals(matcher.group(5))) {
								try {
									creatureAttack.setKataCount(Short.valueOf(matcher.group(4)));
								}
								catch(NumberFormatException e) {
									Log.e(TAG, "Exception parsing kata count " + matcher.group(4), e);
								}
							}
							break;
					}
				}
				attackList.add(creatureAttack);
			}
		}
	}

	/**
	 * Gets the next attack for the creature to use
	 * @param lastAttack  the last attack the creature used or null if this is the first attack or when the attack sequence
	 *                       needs to be restarted
	 * @return  the next attack to use or null if no attacks are found.
	 */
	public CreatureAttack getNextAttack(CreatureAttack lastAttack, short numCreatures) {
		CreatureAttack result = null;
		int index = -1;
		int nextIndex = -1;
		parseAttackSequence();
		if(numCreatures > 1) {
			for (CreatureAttack creatureAttack : attackList) {
				nextIndex++;
				if(creatureAttack.getCoopNumber() > 1 && numCreatures >= creatureAttack.getCoopNumber()) {
					index = nextIndex;
				}
			}
		}
		else {
			result = getNextSoloAttack(lastAttack);
		}
		if(attackList != null && attackList.size() > 0 && index >= 0) {
			if(index == attackList.size()) {
				index = 0;
			}
			result = attackList.get(index);
		}
		return result;
	}

	private CreatureAttack getNextSoloAttack(CreatureAttack lastAttack) {
		CreatureAttack result = null;
		int index = -1;
		int nextIndex = 0;
		parseAttackSequence();

		if (lastAttack != null && attackList != null) {
			for (CreatureAttack creatureAttack : attackList) {
				nextIndex++;
				if(attackList != null && attackList.size() > nextIndex && attackList.get(nextIndex).getCoopNumber() > 1) {
					index = 0;
					break;
				}
				if (creatureAttack.equals(lastAttack)) {
					index = nextIndex;
					break;
				}
			}
		}
		else if (lastAttack == null) {
			index = 0;
		}
		if(attackList != null && !attackList.isEmpty()) {
			if (index == attackList.size()) {
				index = 0;
			}
			if(index >= 0 && attackList.size() > index) {
				result = attackList.get(index);
			}
		}
		return result;
	}

	/**
	 * Gets the offensive bonus for the given attack for the creature.
	 *
	 * @param attack  the attack for which to get the bonus
	 * @return  the attack bonus
	 */
	public short getOffensiveBonus(Attack attack) {
		short result = -25;

		Short bonus = attackBonusesMap.get(attack);
		if(bonus != null) {
			result = bonus;
		}

		return result;
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
				.append("realm1", realmDBO1)
				.append("realm2", realmDBO2)
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
	public void setAttackRxHandler(AttackRxHandler attackRxHandler) {
		this.attackRxHandler = attackRxHandler;
	}
	public void setSizeRxHandler(SizeRxHandler sizeRxHandler) {
		this.sizeRxHandler = sizeRxHandler;
	}
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
	public RealmDBO getRealmDBO1() {
		return realmDBO1;
	}
	public void setRealmDBO1(RealmDBO realmDBO1) {
		this.realmDBO1 = realmDBO1;
	}
	public RealmDBO getRealmDBO2() {
		return realmDBO2;
	}
	public void setRealmDBO2(RealmDBO realmDBO2) {
		this.realmDBO2 = realmDBO2;
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
	public void setTalentInstancesList(List<TalentInstance> talentInstancesList) {
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
	public ArrayList<CreatureAttack> getAttackList() {
		return attackList;
	}
}
