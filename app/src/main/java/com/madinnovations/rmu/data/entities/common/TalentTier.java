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
package com.madinnovations.rmu.data.entities.common;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Talent tier attributes
 */
public class TalentTier {
	private Talent talent = null;
	private short  tier = 0;
	private short  startingTiers = 0;
	private short  endingTiers = 0;

	/**
	 * Creates a new TalentTier instance
	 */
	public TalentTier() {
	}

	/**
	 * Creates a new TalentTier instance for the given Talent and number of tiers
	 *
	 * @param talent  a Talent instance
	 * @param tier  the number of tiers already purchased
	 */
	public TalentTier(Talent talent, short tier) {
		this.talent = talent;
		this.tier = tier;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("talent", talent)
				.append("tier", tier)
				.append("startingTiers", startingTiers)
				.append("endingTiers", endingTiers)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TalentTier talentTier = (TalentTier) o;

		return talent != null ? talent.equals(talentTier.talent) : talentTier.talent == null;
	}

	@Override
	public int hashCode() {
		return talent != null ? talent.hashCode() : 0;
	}

	// Getters and setters
	public Talent getTalent() {
		return talent;
	}
	public void setTalent(Talent talent) {
		this.talent = talent;
	}
	public short getTier() {
		return tier;
	}
	public void setTier(short tier) {
		this.tier = tier;
	}
	public short getStartingTiers() {
		return startingTiers;
	}
	public void setStartingTiers(short startingTiers) {
		this.startingTiers = startingTiers;
	}
	public short getEndingTiers() {
		return endingTiers;
	}
	public void setEndingTiers(short endingTiers) {
		this.endingTiers = endingTiers;
	}
}
