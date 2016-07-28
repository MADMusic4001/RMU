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
import com.madinnovations.rmu.data.entities.common.Talent;

import java.util.List;
import java.util.Map;

/**
 * Racial attributes
 */
public class Race {
	private int id = -1;
	private String name;
	private String description;
	private short bonusDevelopmentPoints;
	private short agilityModifier;
	private short constitutionModifier;
	private short empathyModifier;
	private short intuitionModifier;
	private short memoryModifier;
	private short presenceModifier;
	private short quicknessModifier;
	private short reasoningModifier;
	private short selfDisciplineModifier;
	private short strengthModifier;
	private short channelingResistanceModifier;
	private short essenceResistanceModifier;
	private short mentalismResistanceModifier;
	private short physicalResistanceModifier;
	private short enduranceModifier;
	private short baseHits;
	private float recoveryMultiplier;
	private Map<Talent, Short> talentsAndFlaws;
	private short sizeCategory;
	private List<LocomotionType> locomotionTypes;
	private short strideModifier;
	private short averageHeight;
	private short averageWeight;
	private short poundsPerInch;

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
	public Map<Talent, Short> getTalentsAndFlaws() {
		return talentsAndFlaws;
	}
	public void setTalentsAndFlaws(Map<Talent, Short> talentsAndFlaws) {
		this.talentsAndFlaws = talentsAndFlaws;
	}
	public short getSizeCategory() {
		return sizeCategory;
	}
	public void setSizeCategory(short sizeCategory) {
		this.sizeCategory = sizeCategory;
	}
	public List<LocomotionType> getLocomotionTypes() {
		return locomotionTypes;
	}
	public void setLocomotionTypes(List<LocomotionType> locomotionTypes) {
		this.locomotionTypes = locomotionTypes;
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
