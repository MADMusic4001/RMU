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

import java.io.Serializable;

/**
 * AdditionalCriticalResult attribute info
 */
public class AdditionalEffect extends DatabaseObject implements Serializable {
	private static final long serialVersionUID = -4717734800503159418L;
	private TargetType targetType = TargetType.OPPONENT;
	private Effect effect = Effect.SKILL_BONUS;
	private Object value1 = null;
	private Object value2 = null;
	private Object value3 = null;
	private Object value4 = null;

	// Getters and setters
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
