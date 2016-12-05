/**
 * Copyright (C) 2016 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.entities.creature;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Class containing Creature Archetype level attributes.
 */
public class CreatureArchetypeLevel {
	private CreatureArchetype creatureArchetype;
	private short level = 1;
	private short attack = 0;
	private short attack2 = 0;
	private short defensiveBonus = 0;
	private short bodyDevelopment = 0;
	private short primeSkill = 0;
	private short secondarySkill = 0;
	private short powerDevelopment = 0;
	private short spells = 0;
	private short talentDP = 0;
	private short agility = 0;
	private short constitutionStat = 50;
	private short constitution = 0;
	private short empathy = 0;
	private short intuition = 0;
	private short memory = 0;
	private short presence = 0;
	private short reasoning = 0;
	private short quickness = 0;
	private short selfDiscipline = 0;
	private short strength = 0;

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("creatureArchetype", creatureArchetype)
				.append("level", level)
				.append("attack", attack)
				.append("attack2", attack2)
				.append("defensiveBonus", defensiveBonus)
				.append("bodyDevelopment", bodyDevelopment)
				.append("primeSkill", primeSkill)
				.append("secondarySkill", secondarySkill)
				.append("powerDevelopment", powerDevelopment)
				.append("spells", spells)
				.append("talentDP", talentDP)
				.append("agility", agility)
				.append("constitutionStat", constitutionStat)
				.append("constitution", constitution)
				.append("empathy", empathy)
				.append("intuition", intuition)
				.append("memory", memory)
				.append("presence", presence)
				.append("reasoning", reasoning)
				.append("quickness", quickness)
				.append("selfDiscipline", selfDiscipline)
				.append("strength", strength)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		CreatureArchetypeLevel level1 = (CreatureArchetypeLevel) o;

		if (getLevel() != level1.getLevel()) {
			return false;
		}
		return getCreatureArchetype().equals(level1.getCreatureArchetype());

	}

	@Override
	public int hashCode() {
		int result = getCreatureArchetype().hashCode();
		result = 31 * result + (int) getLevel();
		return result;
	}

	// Getters and setters
	public CreatureArchetype getCreatureArchetype() {
		return creatureArchetype;
	}
	public void setCreatureArchetype(CreatureArchetype creatureArchetype) {
		this.creatureArchetype = creatureArchetype;
	}
	public short getLevel() {
		return level;
	}
	public void setLevel(short level) {
		this.level = level;
	}
	public short getAttack() {
		return attack;
	}
	public void setAttack(short attack) {
		this.attack = attack;
	}
	public short getAttack2() {
		return attack2;
	}
	public void setAttack2(short attack2) {
		this.attack2 = attack2;
	}
	public short getDefensiveBonus() {
		return defensiveBonus;
	}
	public void setDefensiveBonus(short defensiveBonus) {
		this.defensiveBonus = defensiveBonus;
	}
	public short getBodyDevelopment() {
		return bodyDevelopment;
	}
	public void setBodyDevelopment(short bodyDevelopment) {
		this.bodyDevelopment = bodyDevelopment;
	}
	public short getPrimeSkill() {
		return primeSkill;
	}
	public void setPrimeSkill(short primeSkill) {
		this.primeSkill = primeSkill;
	}
	public short getSecondarySkill() {
		return secondarySkill;
	}
	public void setSecondarySkill(short secondarySkill) {
		this.secondarySkill = secondarySkill;
	}
	public short getPowerDevelopment() {
		return powerDevelopment;
	}
	public void setPowerDevelopment(short powerDevelopment) {
		this.powerDevelopment = powerDevelopment;
	}
	public short getSpells() {
		return spells;
	}
	public void setSpells(short spells) {
		this.spells = spells;
	}
	public short getTalentDP() {
		return talentDP;
	}
	public void setTalentDP(short talentDP) {
		this.talentDP = talentDP;
	}
	public short getAgility() {
		return agility;
	}
	public void setAgility(short agility) {
		this.agility = agility;
	}
	public short getConstitutionStat() {
		return constitutionStat;
	}
	public void setConstitutionStat(short constitutionStat) {
		this.constitutionStat = constitutionStat;
	}
	public short getConstitution() {
		return constitution;
	}
	public void setConstitution(short constitution) {
		this.constitution = constitution;
	}
	public short getEmpathy() {
		return empathy;
	}
	public void setEmpathy(short empathy) {
		this.empathy = empathy;
	}
	public short getIntuition() {
		return intuition;
	}
	public void setIntuition(short intuition) {
		this.intuition = intuition;
	}
	public short getMemory() {
		return memory;
	}
	public void setMemory(short memory) {
		this.memory = memory;
	}
	public short getPresence() {
		return presence;
	}
	public void setPresence(short presence) {
		this.presence = presence;
	}
	public short getReasoning() {
		return reasoning;
	}
	public void setReasoning(short reasoning) {
		this.reasoning = reasoning;
	}
	public short getQuickness() {
		return quickness;
	}
	public void setQuickness(short quickness) {
		this.quickness = quickness;
	}
	public short getSelfDiscipline() {
		return selfDiscipline;
	}
	public void setSelfDiscipline(short selfDiscipline) {
		this.selfDiscipline = selfDiscipline;
	}
	public short getStrength() {
		return strength;
	}
	public void setStrength(short strength) {
		this.strength = strength;
	}
}
