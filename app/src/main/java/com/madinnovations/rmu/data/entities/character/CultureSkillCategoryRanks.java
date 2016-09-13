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

import com.madinnovations.rmu.data.entities.common.SkillCategory;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class for grouping culture Skill ranks to SkillCategory
 */
public class CultureSkillCategoryRanks {
	private SkillCategory skillCategory;
	private List<CultureSkillRank> skillRanksList = new ArrayList<>();

	/**
	 * Creates a new CultureSkillCategoryRanks instance
	 *
	 * @param skillCategory  the {@link SkillCategory} for this instance
	 */
	public CultureSkillCategoryRanks(SkillCategory skillCategory) {
		this.skillCategory = skillCategory;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this,
								   ToStringStyle.MULTI_LINE_STYLE)
				.append("skillCategory", skillCategory)
				.append("skillRanksList", skillRanksList)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		CultureSkillCategoryRanks that = (CultureSkillCategoryRanks) o;

		return getSkillCategory().equals(that.getSkillCategory());
	}

	@Override
	public int hashCode() {
		return getSkillCategory().hashCode();
	}

	// Getters and setters
	public SkillCategory getSkillCategory() {
		return skillCategory;
	}
	public void setSkillCategory(SkillCategory skillCategory) {
		this.skillCategory = skillCategory;
	}
	public List<CultureSkillRank> getSkillRanksList() {
		return skillRanksList;
	}
	public void setSkillRanksList(List<CultureSkillRank> skillRanksList) {
		this.skillRanksList = skillRanksList;
	}
}
