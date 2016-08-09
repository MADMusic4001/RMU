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

import com.madinnovations.rmu.data.entities.combat.CriticalCode;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.common.Talent;

import java.util.Map;

/**
 * Class containing Creature Variety attributes.
 */
public class CreatureVariety {
	private int id = -1;
	private CreatureType type;
	private String name;
	private String description;
	private short typicalLevel;
	private char levelSpread;
	private Map<Stat, Short> racialStatBonuses;
	private short             height;
	private short             length;
	private short             weight;
	private float             healingRate;
	private short             baseHits;
	private short             baseEndurance;
	private Size size;
	private short             armorType;
	private CriticalCode criticalCode;
	private short             baseMovementRate;
	private short baseChannellingRR;
	private short baseEssenceRR;
	private short baseMentalismRR;
	private short basePhysicalRR;
	private short baseFearRR;
	private short baseStride;
	private short leftoverDP;
	private Outlook outlook;
	private Map<Talent, Short> talentTiersMap;


	/**
	 * Checks the validity of the CreatureVariety instance.
	 *
	 * @return true if the CreatureVariety instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() && type != null;
	}

	@Override
	public String toString() {
		return "CreatureVariety{" +
				"id=" + id +
				", type=" + type +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CreatureVariety that = (CreatureVariety) o;

		return id == that.id;

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
}
