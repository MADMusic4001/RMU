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
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.common.Talent;

import java.util.Map;

/**
 * Character attributes
 */
public class Character {
	private int id = -1;
	private String name;
	private String description;
	private Race race;
	private Profession profession;
	private short height;
	private short weight;
	private short stride;
	private short currentHits;
	private short maxHits;
	private short currentDevelopmentPoints;
	private Map<Skill, Short> skillRanks;
	private Map<Talent, Short> talentTiers;
	private Map<Stat, Short> statTemps;
	private Map<Stat, Short> statPotentials;

	/**
	 * Checks the validity of the Character instance.
	 *
	 * @return true if the Character instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() && race != null && profession != null;
	}

	@Override
	public String toString() {
		return "Character{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", race=" + race +
				", profession=" + profession +
				", height=" + height +
				", weight=" + weight +
				", stride=" + stride +
				", currentHits=" + currentHits +
				", maxHits=" + maxHits +
				", currentDevelopmentPoints=" + currentDevelopmentPoints +
				", skillRanks=" + skillRanks +
				", talentTiers=" + talentTiers +
				", statTemps=" + statTemps +
				", statPotentials=" + statPotentials +
				'}';
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
	public Race getRace() {
		return race;
	}
	public void setRace(Race race) {
		this.race = race;
	}
	public Profession getProfession() {
		return profession;
	}
	public void setProfession(Profession profession) {
		this.profession = profession;
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
	public short getStride() {
		return stride;
	}
	public void setStride(short stride) {
		this.stride = stride;
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
