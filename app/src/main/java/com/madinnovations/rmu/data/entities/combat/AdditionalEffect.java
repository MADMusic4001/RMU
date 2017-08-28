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
package com.madinnovations.rmu.data.entities.combat;

import com.madinnovations.rmu.data.entities.DatabaseObject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * AdditionalCriticalResult attribute info
 */
public class AdditionalEffect extends DatabaseObject implements Serializable {
	private static final long serialVersionUID = -4717734800503159418L;
	private CriticalResult criticalResult = null;
	private Fumble fumble = null;
	private TargetType targetType = TargetType.OPPONENT;
	private Effect effect = Effect.SKILL_BONUS;
	private Object value1 = null;
	private Object value2 = null;
	private Object value3 = null;
	private Object value4 = null;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}

		AdditionalEffect that = (AdditionalEffect) o;

		if (getTargetType() != that.getTargetType()) {
			return false;
		}
		if (getEffect() != that.getEffect()) {
			return false;
		}
		if (getValue1() != null ? !getValue1().equals(that.getValue1()) : that.getValue1() != null) {
			return false;
		}
		if (getValue2() != null ? !getValue2().equals(that.getValue2()) : that.getValue2() != null) {
			return false;
		}
		//noinspection SimplifiableIfStatement
		if (getValue3() != null ? !getValue3().equals(that.getValue3()) : that.getValue3() != null) {
			return false;
		}
		return getValue4() != null ? getValue4().equals(that.getValue4()) : that.getValue4() == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + getTargetType().hashCode();
		result = 31 * result + getEffect().hashCode();
		result = 31 * result + (getValue1() != null ? getValue1().hashCode() : 0);
		result = 31 * result + (getValue2() != null ? getValue2().hashCode() : 0);
		result = 31 * result + (getValue3() != null ? getValue3().hashCode() : 0);
		result = 31 * result + (getValue4() != null ? getValue4().hashCode() : 0);
		return result;
	}

	// Getters and setters
	/**
	 * Checks the validity of the AdditionalEffect instance.
	 *
	 * @return true if the AdditionalEffect instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return (criticalResult != null || fumble != null) && (criticalResult == null || fumble == null) && targetType != null &&
				effect != null && value1 != null;
	}

	/**
	 * Generates a string containing the field values for this instance.
	 *
	 * @return a string containing the field values for this instance.
	 */
	public String print() {
		return new ToStringBuilder(this,
								   ToStringStyle.MULTI_LINE_STYLE)
				.append("criticalResult", criticalResult)
				.append("fumble", fumble)
				.append("targetType", targetType)
				.append("effect", effect)
				.append("value1", value1)
				.append("value2", value2)
				.append("value3", value3)
				.append("value4", value4)
				.toString();
	}

	public CriticalResult getCriticalResult() {
		return criticalResult;
	}
	public void setCriticalResult(CriticalResult criticalResult) {
		this.criticalResult = criticalResult;
	}
	public Fumble getFumble() {
		return fumble;
	}
	public void setFumble(Fumble fumble) {
		this.fumble = fumble;
	}
	public TargetType getTargetType() {
		return targetType;
	}
	public void setTargetType(TargetType targetType) {
		this.targetType = targetType;
	}
	public Effect getEffect() {
		return effect;
	}
	public void setEffect(Effect effect) {
		this.effect = effect;
	}
	public Object getValue1() {
		return value1;
	}
	public void setValue1(Object value1) {
		this.value1 = value1;
	}
	public Object getValue2() {
		return value2;
	}
	public void setValue2(Object value2) {
		this.value2 = value2;
	}
	public Object getValue3() {
		return value3;
	}
	public void setValue3(Object value3) {
		this.value3 = value3;
	}
	public Object getValue4() {
		return value4;
	}
	public void setValue4(Object value4) {
		this.value4 = value4;
	}
}
