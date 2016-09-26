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
package com.madinnovations.rmu.data.entities.spells;

/**
 * Spell attributes
 */
public class Spell {
	public static final String JSON_NAME = "Spells";
	private int id = -1;
	private SpellList spellList = null;
	private String name = null;
	private String description = null;
	private AreaOfEffect areaOfEffect = null;
	private int[] areaOfEffectParams = new int[0];
	private Duration duration = null;
	private int[] durationParams = new int[0];
	private Range range = null;
	private Integer rangeParam = null;
	private SpellType spellType = null;
	private SpellSubType spellSubType = null;

	/**
	 * Checks the validity of the Spell instance.
	 *
	 * @return true if the Spell instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() && spellList != null &&
				spellType != null && areaOfEffect != null && duration != null && range != null;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Spell spell = (Spell) o;

		return id == spell.id;
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
	public SpellList getSpellList() {
		return spellList;
	}
	public void setSpellList(SpellList spellList) {
		this.spellList = spellList;
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
	public AreaOfEffect getAreaOfEffect() {
		return areaOfEffect;
	}
	public void setAreaOfEffect(AreaOfEffect areaOfEffect) {
		this.areaOfEffect = areaOfEffect;
	}
	public int[] getAreaOfEffectParams() {
		return areaOfEffectParams;
	}
	public void setAreaOfEffectParams(int[] areaOfEffectParams) {
		this.areaOfEffectParams = areaOfEffectParams;
	}
	public Duration getDuration() {
		return duration;
	}
	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	public int[] getDurationParams() {
		return durationParams;
	}
	public void setDurationParams(int[] durationParams) {
		this.durationParams = durationParams;
	}
	public Range getRange() {
		return range;
	}
	public void setRange(Range range) {
		this.range = range;
	}
	public Integer getRangeParam() {
		return rangeParam;
	}
	public void setRangeParam(Integer rangeParams) {
		this.rangeParam = rangeParams;
	}
	public SpellType getSpellType() {
		return spellType;
	}
	public void setSpellType(SpellType spellType) {
		this.spellType = spellType;
	}
	public SpellSubType getSpellSubType() {
		return spellSubType;
	}
	public void setSpellSubType(SpellSubType spellSubType) {
		this.spellSubType = spellSubType;
	}
}
