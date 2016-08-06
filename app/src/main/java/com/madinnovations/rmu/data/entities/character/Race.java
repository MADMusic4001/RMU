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

import com.madinnovations.rmu.data.entities.common.LocomotionType;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Talent;

import java.util.HashMap;
import java.util.Map;

/**
 * Racial attributes
 */
public class Race {
	private int id = -1;
	private String name = null;
	private String description = null;
	private short bonusDevelopmentPoints = 0;
	private short agilityModifier = 0;
	private short constitutionModifier = 0;
	private short empathyModifier = 0;
	private short intuitionModifier = 0;
	private short memoryModifier = 0;
	private short presenceModifier = 0;
	private short quicknessModifier = 0;
	private short reasoningModifier = 0;
	private short selfDisciplineModifier = 0;
	private short strengthModifier = 0;
	private short channelingResistanceModifier = 0;
	private short essenceResistanceModifier = 0;
	private short mentalismResistanceModifier = 0;
	private short physicalResistanceModifier = 0;
	private short enduranceModifier = 0;
	private short baseHits = 0;
	private float recoveryMultiplier = 1.0f;
	private Map<Talent, Short> talentsAndFlawsTiersMap = new HashMap<>();
	private Size size = null;
	private Map<LocomotionType, Short> locomotionTypeRatesMap = new HashMap<>();
	private short strideModifier = 0;
	private short averageHeight = 0;
	private short averageWeight = 0;
	private short poundsPerInch = 0;

	/**
	 * Checks the validity of the Profession instance.
	 *
	 * @return true if the Profession instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty();
	}

	@Override
	public String toString() {
		return "Race{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", bonusDevelopmentPoints=" + bonusDevelopmentPoints +
				", agilityModifier=" + agilityModifier +
				", constitutionModifier=" + constitutionModifier +
				", empathyModifier=" + empathyModifier +
				", intuitionModifier=" + intuitionModifier +
				", memoryModifier=" + memoryModifier +
				", presenceModifier=" + presenceModifier +
				", quicknessModifier=" + quicknessModifier +
				", reasoningModifier=" + reasoningModifier +
				", selfDisciplineModifier=" + selfDisciplineModifier +
				", strengthModifier=" + strengthModifier +
				", channelingResistanceModifier=" + channelingResistanceModifier +
				", essenceResistanceModifier=" + essenceResistanceModifier +
				", mentalismResistanceModifier=" + mentalismResistanceModifier +
				", physicalResistanceModifier=" + physicalResistanceModifier +
				", enduranceModifier=" + enduranceModifier +
				", baseHits=" + baseHits +
				", recoveryMultiplier=" + recoveryMultiplier +
				", talentsAndFlawsTiersMap=" + talentsAndFlawsTiersMap +
				", size=" + size +
				", locomotionTypeRatesMap=" + locomotionTypeRatesMap +
				", strideModifier=" + strideModifier +
				", averageHeight=" + averageHeight +
				", averageWeight=" + averageWeight +
				", poundsPerInch=" + poundsPerInch +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Race race = (Race) o;

		return id == race.id;

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
	public short getBonusDevelopmentPoints() {
		return bonusDevelopmentPoints;
	}
	public void setBonusDevelopmentPoints(short bonusDevelopmentPoints) {
		this.bonusDevelopmentPoints = bonusDevelopmentPoints;
	}
	public short getAgilityModifier() {
		return agilityModifier;
	}
	public void setAgilityModifier(short agilityModifier) {
		this.agilityModifier = agilityModifier;
	}
	public short getConstitutionModifier() {
		return constitutionModifier;
	}
	public void setConstitutionModifier(short constitutionModifier) {
		this.constitutionModifier = constitutionModifier;
	}
	public short getEmpathyModifier() {
		return empathyModifier;
	}
	public void setEmpathyModifier(short empathyModifier) {
		this.empathyModifier = empathyModifier;
	}
	public short getIntuitionModifier() {
		return intuitionModifier;
	}
	public void setIntuitionModifier(short intuitionModifier) {
		this.intuitionModifier = intuitionModifier;
	}
	public short getMemoryModifier() {
		return memoryModifier;
	}
	public void setMemoryModifier(short memoryModifier) {
		this.memoryModifier = memoryModifier;
	}
	public short getPresenceModifier() {
		return presenceModifier;
	}
	public void setPresenceModifier(short presenceModifier) {
		this.presenceModifier = presenceModifier;
	}
	public short getQuicknessModifier() {
		return quicknessModifier;
	}
	public void setQuicknessModifier(short quicknessModifier) {
		this.quicknessModifier = quicknessModifier;
	}
	public short getReasoningModifier() {
		return reasoningModifier;
	}
	public void setReasoningModifier(short reasoningModifier) {
		this.reasoningModifier = reasoningModifier;
	}
	public short getSelfDisciplineModifier() {
		return selfDisciplineModifier;
	}
	public void setSelfDisciplineModifier(short selfDisciplineModifier) {
		this.selfDisciplineModifier = selfDisciplineModifier;
	}
	public short getStrengthModifier() {
		return strengthModifier;
	}
	public void setStrengthModifier(short strengthModifier) {
		this.strengthModifier = strengthModifier;
	}
	public short getChannelingResistanceModifier() {
		return channelingResistanceModifier;
	}
	public void setChannelingResistanceModifier(short channelingResistanceModifier) {
		this.channelingResistanceModifier = channelingResistanceModifier;
	}
	public short getEssenceResistanceModifier() {
		return essenceResistanceModifier;
	}
	public void setEssenceResistanceModifier(short essenceResistanceModifier) {
		this.essenceResistanceModifier = essenceResistanceModifier;
	}
	public short getMentalismResistanceModifier() {
		return mentalismResistanceModifier;
	}
	public void setMentalismResistanceModifier(short mentalismResistanceModifier) {
		this.mentalismResistanceModifier = mentalismResistanceModifier;
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
	public Map<Talent, Short> getTalentsAndFlawsTiersMap() {
		return talentsAndFlawsTiersMap;
	}
	public void setTalentsAndFlawsTiersMap(Map<Talent, Short> talentsAndFlawsTiersMap) {
		this.talentsAndFlawsTiersMap = talentsAndFlawsTiersMap;
	}
	public Size getSize() {
		return size;
	}
	public void setSize(Size size) {
		this.size = size;
	}
	public Map<LocomotionType, Short> getLocomotionTypeRatesMap() {
		return locomotionTypeRatesMap;
	}
	public void setLocomotionTypeRatesMap(Map<LocomotionType, Short> locomotionTypeRatesMap) {
		this.locomotionTypeRatesMap = locomotionTypeRatesMap;
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
}
