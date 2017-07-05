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
package com.madinnovations.rmu.data.entities.common;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * Talent Effect information
 */
public class TalentParameterRow {
	private Parameter    parameter    = Parameter.BONUS;
	private Integer      initialValue = null;
	private String       enumName     = null;
	private BigDecimal[] perValues    = new BigDecimal[0];
	private UnitType[]   unitTypes    = new UnitType[0];

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

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("parameter", parameter)
				.append("enumName", enumName)
				.append("initialValue", initialValue)
				.append("perValues", perValues)
				.append("unitTypes", unitTypes)
				.toString();
	}

	// Getters and setters
	public Parameter getParameter() {
		return parameter;
	}
	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
		this.initialValue = null;
	}
	public Integer getInitialValue() {
		return initialValue;
	}
	public void setInitialValue(Integer initialValue) {
		this.initialValue = initialValue;
	}
	public String getEnumName() {
		return enumName;
	}
	public void setEnumName(String enumName) {
		this.enumName = enumName;
	}
	public BigDecimal[] getPerValues() {
		return perValues;
	}
	public void setPerValues(BigDecimal[] perValues) {
		this.perValues = perValues;
	}
	public UnitType[] getUnitTypes() {
		return unitTypes;
	}
	public void setUnitTypes(UnitType[] unitTypes) {
		this.unitTypes = unitTypes;
	}
}
