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
 * Representation of a condition on a character or creature.
 */
public class State {
	private StateType  stateType        = null;
	private String     enumName         = null;
	private float      constant         = 0f;
	private float      dividend         = 0f;
	private float[]    divisors         = null;
	private UnitType   qtyUnitType      = null;
	private UnitType[] divisorUnitTypes = null;

	// Getters amd Setters
	public StateType getStateType() {
		return stateType;
	}
	public void setStateType(StateType stateType) {
		this.stateType = stateType;
	}
	public String getEnumName() {
		return enumName;
	}
	public void setEnumName(String enumName) {
		this.enumName = enumName;
	}
	public float getConstant() {
		return constant;
	}
	public void setConstant(float constant) {
		this.constant = constant;
	}
	public float getDividend() {
		return dividend;
	}
	public void setDividend(float dividend) {
		this.dividend = dividend;
	}
	public float[] getDivisors() {
		return divisors;
	}
	public void setDivisors(float[] divisors) {
		this.divisors = divisors;
	}
	public UnitType getQtyUnitType() {
		return qtyUnitType;
	}
	public void setQtyUnitType(UnitType qtyUnitType) {
		this.qtyUnitType = qtyUnitType;
	}
	public UnitType[] getDivisorUnitTypes() {
		return divisorUnitTypes;
	}
	public void setDivisorUnitTypes(UnitType[] divisorUnitTypes) {
		this.divisorUnitTypes = divisorUnitTypes;
	}
}
