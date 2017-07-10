/*
 *
 *   Copyright (C) 2017 MadInnovations
 *   <p/>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   <p/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   <p/>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *
 */

package com.madinnovations.rmu.data.entities.object;

import com.madinnovations.rmu.data.entities.object.MoneyUnit;

/**
 * Encapsulates a costs value and units
 */
public class Cost {
	private short     value;
	private MoneyUnit unit;

	/**
	 * Creates a new Cost instance
	 */
	public Cost() {
	}

	/**
	 * Creates a new Cost instance with the given value and unit
	 *
	 * @param value  the value to use for the new Cost instance
	 * @param unit  the MonetaryUnit to use for the new Cost instance
	 */
	public Cost(short value, MoneyUnit unit) {
		this.value = value;
		this.unit = unit;
	}

	// Getters and setters
	public short getValue() {
		return value;
	}
	public void setValue(short value) {
		this.value = value;
	}
	public MoneyUnit getUnit() {
		return unit;
	}
	public void setUnit(MoneyUnit unit) {
		this.unit = unit;
	}
}
