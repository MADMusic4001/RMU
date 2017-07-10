/*
  Copyright (C) 2016 MadInnovations
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.entities.campaign;

import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.common.PowerLevel;
import com.madinnovations.rmu.data.entities.common.Specialization;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Campaign settings
 */
public class Campaign extends DatabaseObject {
	public static final String JSON_NAME = "Campaigns";
	private String name = null;
	private Calendar createDate = Calendar.getInstance();
	private PowerLevel powerLevel = PowerLevel.SUPERIOR;
	private boolean awardDP = false;
	private boolean intenseTrainingAllowed = false;
	private boolean individualStride = false;
	private boolean noProfessions = false;
	private boolean buyStats = false;
	private boolean allowTalentsBeyondFirst = false;
	private boolean openRounds = false;
	private boolean grittyPoisonAndDisease = false;
	private boolean multipleSpellsInARound = false;
	private List<Specialization> restrictedSpecializations = new ArrayList<>();

	/**
	 * Creates a new Campaign instance.
	 */
	public Campaign() {
	}

	/**
	 * Creates a new Campaign instance with the given id.
	 *
	 * @param id  the id of the new instance
	 */
	public Campaign(int id) {
		super(id);
	}

	/**
	 * Checks the validity of the Campaign instance.
	 *
	 * @return true if the Campaign instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && powerLevel != null && createDate != null;
	}

	/**
	 * Generates a formatted String of this instances attributes.
	 *
	 * @return  a String instance.
	 */
	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("name", name)
				.append("createDate", createDate)
				.append("powerLevel", powerLevel)
				.append("awardDP", awardDP)
				.append("intenseTrainingAllowed", intenseTrainingAllowed)
				.append("individualStride", individualStride)
				.append("noProfessions", noProfessions)
				.append("buyStats", buyStats)
				.append("allowTalentsBeyondFirst", allowTalentsBeyondFirst)
				.append("openRounds", openRounds)
				.append("grittyPoisonAndDisease", grittyPoisonAndDisease)
				.append("multipleSpellsInARound", multipleSpellsInARound)
				.append("restrictedSpecializations", restrictedSpecializations)
				.toString();
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
	public Calendar getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Calendar createDate) {
		this.createDate = createDate;
	}
	public PowerLevel getPowerLevel() {
		return powerLevel;
	}
	public void setPowerLevel(PowerLevel powerLevel) {
		this.powerLevel = powerLevel;
	}
	public boolean isAwardDP() {
		return awardDP;
	}
	public void setAwardDP(boolean awardDP) {
		this.awardDP = awardDP;
	}
	public boolean isIntenseTrainingAllowed() {
		return intenseTrainingAllowed;
	}
	public void setIntenseTrainingAllowed(boolean intenseTrainingAllowed) {
		this.intenseTrainingAllowed = intenseTrainingAllowed;
	}
	public boolean isIndividualStride() {
		return individualStride;
	}
	public void setIndividualStride(boolean individualStride) {
		this.individualStride = individualStride;
	}
	public boolean isNoProfessions() {
		return noProfessions;
	}
	public void setNoProfessions(boolean noProfessions) {
		this.noProfessions = noProfessions;
	}
	public boolean isBuyStats() {
		return buyStats;
	}
	public void setBuyStats(boolean buyStats) {
		this.buyStats = buyStats;
	}
	public boolean isAllowTalentsBeyondFirst() {
		return allowTalentsBeyondFirst;
	}
	public void setAllowTalentsBeyondFirst(boolean allowTalentsBeyondFirst) {
		this.allowTalentsBeyondFirst = allowTalentsBeyondFirst;
	}
	public boolean isOpenRounds() {
		return openRounds;
	}
	public void setOpenRounds(boolean openRounds) {
		this.openRounds = openRounds;
	}
	public boolean isGrittyPoisonAndDisease() {
		return grittyPoisonAndDisease;
	}
	public void setGrittyPoisonAndDisease(boolean grittyPoisonAndDisease) {
		this.grittyPoisonAndDisease = grittyPoisonAndDisease;
	}
	public boolean isMultipleSpellsInARound() {
		return multipleSpellsInARound;
	}
	public void setMultipleSpellsInARound(boolean multipleSpellsInARound) {
		this.multipleSpellsInARound = multipleSpellsInARound;
	}
	public List<Specialization> getRestrictedSpecializations() {
		return restrictedSpecializations;
	}
	public void setRestrictedSpecializations(List<Specialization> restrictedSpecializations) {
		this.restrictedSpecializations = restrictedSpecializations;
	}
}
