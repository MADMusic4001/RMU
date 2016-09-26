/**
 * Copyright (C) 2016 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.entities.common;

/**
 * Talent Effect information
 */
public class TalentParameterRow {
	private Parameter      parameter              = Parameter.BONUS;
	private boolean        perTier                = false;
	private boolean        perLevel               = false;
	private boolean        perRound               = false;
	private String         enumName               = null;
	private Integer        value                  = 0;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TalentParameterRow that = (TalentParameterRow) o;

		return getParameter() == that.getParameter();
	}

	@Override
	public int hashCode() {
		return getParameter() != null ? getParameter().hashCode() : 0;
	}

	// Getters and setters
	public Parameter getParameter() {
		return parameter;
	}
	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
	public boolean isPerTier() {
		return perTier;
	}
	public void setPerTier(boolean perTier) {
		this.perTier = perTier;
	}
	public boolean isPerLevel() {
		return perLevel;
	}
	public void setPerLevel(boolean perLevel) {
		this.perLevel = perLevel;
	}
	public boolean isPerRound() {
		return perRound;
	}
	public void setPerRound(boolean perRound) {
		this.perRound = perRound;
	}
	public String getEnumName() {
		return enumName;
	}
	public void setEnumName(String enumName) {
		this.enumName = enumName;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
}
