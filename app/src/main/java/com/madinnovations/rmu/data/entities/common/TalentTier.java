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

/**
 * Talent tier attributes
 */
public class TalentTier {
	private Talent talent = null;
	private short tier = 0;

	/**
	 * Creates a new TalentTier with the given attribute values
	 *
	 * @param talent  a Talent instance to set the talent value for the new TalentTier instance
	 * @param tier  a short to set the tier value for the new TalentTier instance
	 */
	public TalentTier(Talent talent, short tier) {
		this.talent = talent;
		this.tier = tier;
	}

	@Override
	public String toString() {
		return "TalentTier{" +
				"talent=" + talent +
				", tier=" + tier +
				'}';
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
}
