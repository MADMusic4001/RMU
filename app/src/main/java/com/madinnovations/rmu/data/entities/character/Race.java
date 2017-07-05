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

import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.spells.RealmDBO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Racial attributes
 */
public class Race extends DatabaseObject {
	public static final String    JSON_NAME                  = "Races";
	private String                name                       = null;
	private String                description                = null;
	private short                 bonusDevelopmentPoints     = 0;
	private Map<Statistic, Short> statModifiers              = new HashMap<>();
	private Map<RealmDBO, Short>  realmResistancesModifiers  = new HashMap<>();
	private short                 physicalResistanceModifier = 0;
	private short                 enduranceModifier          = 0;
	private short                 baseHits                   = 0;
	private float                 recoveryMultiplier         = 1.0f;
	private List<TalentInstance>  talentsAndFlawsList        = new ArrayList<>();
	private Size                  size                       = null;
	private short                 strideModifier             = 0;
	private short                 averageHeight              = 0;
	private short                 averageWeight              = 0;
	private short                 poundsPerInch              = 0;
	private List<Culture>         allowedCultures            = new ArrayList<>();

	/**
	 * Default constructor
	 */
	public Race() {
	}

	/**
	 * ID constructor
	 *
	 * @param id  the id of the new instance
	 */
	public Race(int id) {
			super(id);
	}

	/**
	 * Checks the validity of the Profession instance.
	 *
	 * @return true if the Profession instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty()
				&& size != null;
	}

	@Override
	public String toString() {
		return name;
	}

	// Getters and setters
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
	public short getBonusDevelopmentPoints() {
		return bonusDevelopmentPoints;
	}
	public void setBonusDevelopmentPoints(short bonusDevelopmentPoints) {
		this.bonusDevelopmentPoints = bonusDevelopmentPoints;
	}
	public Map<Statistic, Short> getStatModifiers() {
		return statModifiers;
	}
	public void setStatModifiers(Map<Statistic, Short> statModifiers) {
		this.statModifiers = statModifiers;
	}
	public Map<RealmDBO, Short> getRealmResistancesModifiers() {
		return realmResistancesModifiers;
	}
	public void setRealmResistancesModifiers(
			Map<RealmDBO, Short> realmResistancesModifiers) {
		this.realmResistancesModifiers = realmResistancesModifiers;
	}
	public short getPhysicalResistanceModifier() {
		return physicalResistanceModifier;
	}
	public void setPhysicalResistanceModifier(short physicalResistanceModifier) {
		this.physicalResistanceModifier = physicalResistanceModifier;
	}
	public short getEnduranceModifier() {
		return enduranceModifier;
	}
	public void setEnduranceModifier(short enduranceModifier) {
		this.enduranceModifier = enduranceModifier;
	}
	public short getBaseHits() {
		return baseHits;
	}
	public void setBaseHits(short baseHits) {
		this.baseHits = baseHits;
	}
	public float getRecoveryMultiplier() {
		return recoveryMultiplier;
	}
	public void setRecoveryMultiplier(float recoveryMultiplier) {
		this.recoveryMultiplier = recoveryMultiplier;
	}
	public List<TalentInstance> getTalentsAndFlawsList() {
		return talentsAndFlawsList;
	}
	public void setTalentsAndFlawsList(List<TalentInstance> talentsAndFlawsList) {
		this.talentsAndFlawsList = talentsAndFlawsList;
	}
	public Size getSize() {
		return size;
	}
	public void setSize(Size size) {
		this.size = size;
	}
	public short getStrideModifier() {
		return strideModifier;
	}
	public void setStrideModifier(short strideModifier) {
		this.strideModifier = strideModifier;
	}
	public short getAverageHeight() {
		return averageHeight;
	}
	public void setAverageHeight(short averageHeight) {
		this.averageHeight = averageHeight;
	}
	public short getAverageWeight() {
		return averageWeight;
	}
	public void setAverageWeight(short averageWeight) {
		this.averageWeight = averageWeight;
	}
	public short getPoundsPerInch() {
		return poundsPerInch;
	}
	public void setPoundsPerInch(short poundsPerInch) {
		this.poundsPerInch = poundsPerInch;
	}
	public List<Culture> getAllowedCultures() {
		return allowedCultures;
	}
	public void setAllowedCultures(List<Culture> allowedCultures) {
		this.allowedCultures = allowedCultures;
	}
}
