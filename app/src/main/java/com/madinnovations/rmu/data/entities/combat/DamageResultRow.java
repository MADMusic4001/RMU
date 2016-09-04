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
package com.madinnovations.rmu.data.entities.combat;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;

/**
 * Daamage result row attributes
 */
public class DamageResultRow {
	public int id = -1;
	public DamageTable damageTable = null;
	public short rangeLowValue = 65;
	public short rangeHighValue = 67;
	public DamageResult[] damageResults = new DamageResult[10];

	/**
	 * Checks the validity of the DamageResultRow instance.
	 *
	 * @return true if the DamageResultRow instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return damageTable != null;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", id)
				.append("damageTable", damageTable)
				.append("rangeLowValue", rangeLowValue)
				.append("rangeHighValue", rangeHighValue)
				.append("damageResults", damageResults)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DamageResultRow that = (DamageResultRow) o;

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
	public DamageTable getDamageTable() {
		return damageTable;
	}
	public void setDamageTable(DamageTable damageTable) {
		this.damageTable = damageTable;
	}
	public short getRangeLowValue() {
		return rangeLowValue;
	}
	public void setRangeLowValue(short rangeLowValue) {
		this.rangeLowValue = rangeLowValue;
	}
	public short getRangeHighValue() {
		return rangeHighValue;
	}
	public void setRangeHighValue(short rangeHighValue) {
		this.rangeHighValue = rangeHighValue;
	}
	public DamageResult[] getDamageResults() {
		return damageResults;
	}
}
