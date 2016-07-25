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
package com.madinnovations.rmu.data.entities;

import java.util.Map;

/**
 * Creature attributes
 */
public class Creature {
	private int id;
	private String name;
	private String description;
	private CreatureArchetype archetype;
	private short level;
	private char levelSpread;
	private CreatureCategory category;
	private CreatureType type;
	private Map<Stat, Short> stats;
	private short height;
	private short length;
	private short weight;
	private float healingRate;
	private short baseHits;
	private short baseEndurance;
	private Size size;
	private short armorType;
	private CriticalCode criticalCode;
	private int baseMovementRate;
	private int defensiveBonus;
	private int endurance;
	private int offensiveBonus;

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
	public CreatureArchetype getArchetype() {
		return archetype;
	}
	public void setArchetype(CreatureArchetype archetype) {
		this.archetype = archetype;
	}
	public short getLevel() {
		return level;
	}
	public void setLevel(short level) {
		this.level = level;
	}
	public char getLevelSpread() {
		return levelSpread;
	}
	public void setLevelSpread(char levelSpread) {
		this.levelSpread = levelSpread;
	}
	public CreatureCategory getCategory() {
		return category;
	}
	public void setCategory(CreatureCategory category) {
		this.category = category;
	}
	public CreatureType getType() {
		return type;
	}
	public void setType(CreatureType type) {
		this.type = type;
	}
	public Map<Stat, Short> getStats() {
		return stats;
	}
	public void setStats(Map<Stat, Short> stats) {
		this.stats = stats;
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
	public float getHealingRate() {
		return healingRate;
	}
	public void setHealingRate(float healingRate) {
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
	public CriticalCode getCriticalCode() {
		return criticalCode;
	}
	public void setCriticalCode(CriticalCode criticalCode) {
		this.criticalCode = criticalCode;
	}
	public int getBaseMovementRate() {
		return baseMovementRate;
	}
	public void setBaseMovementRate(int baseMovementRate) {
		this.baseMovementRate = baseMovementRate;
	}
	public int getDefensiveBonus() {
		return defensiveBonus;
	}
	public void setDefensiveBonus(int defensiveBonus) {
		this.defensiveBonus = defensiveBonus;
	}
	public int getEndurance() {
		return endurance;
	}
	public void setEndurance(int endurance) {
		this.endurance = endurance;
	}
	public int getOffensiveBonus() {
		return offensiveBonus;
	}
	public void setOffensiveBonus(int offensiveBonus) {
		this.offensiveBonus = offensiveBonus;
	}
}
