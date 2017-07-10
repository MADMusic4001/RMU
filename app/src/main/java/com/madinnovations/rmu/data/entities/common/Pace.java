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

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Pace values
 */
public enum Pace {
	CREEP(R.string.enum_pace_creep, 0.5f, (short)0, (short)-5, (short)-150),
	WALK(R.string.enum_pace_walk, 1.0f, (short)-10, (short)-20, (short)-100),
	BRISK(R.string.enum_pace_brisk, 1.5f, (short)-20, (short)-40, (short)-70),
	JOG(R.string.enum_pace_jog, 2.0f, (short)-30, null, (short)-50),
	RUN(R.string.enum_pace_run, 3.0f, (short)-50, null, (short)-30),
	SPRINT(R.string.enum_pace_sprint, 4.0f, (short)-70, null, (short)-20),
	DASH(R.string.enum_pace_dash, 5.0f, (short)-100, null, (short)-10);

	@StringRes int nameId;
	float baseMovementRateMultiplier;
	short penalty;
	Short moveBackwardsPenalty;
	short encumbranceLimit;

	Pace(int nameId, float baseMovementRateMultiplier, short penalty, Short moveBackwardsPenalty, short encumbranceLimit) {
		this.nameId = nameId;
		this.baseMovementRateMultiplier = baseMovementRateMultiplier;
		this.penalty = penalty;
		this.moveBackwardsPenalty = moveBackwardsPenalty;
		this.encumbranceLimit = encumbranceLimit;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(nameId);
	}

	/**
	 * Returns a string containing the field names and values for this instance.
	 *
	 * @return a string containing the field names and values for this instance.
	 */
	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("nameId", nameId)
				.append("baseMovementRateMultiplier", baseMovementRateMultiplier)
				.append("penalty", penalty)
				.append("moveBackwardsPenalty", moveBackwardsPenalty)
				.append("encumbranceLimit", encumbranceLimit)
				.toString();
	}

	// Getters
	public int getNameId() {
		return nameId;
	}
	public float getBaseMovementRateMultiplier() {
		return baseMovementRateMultiplier;
	}
	public short getPenalty() {
		return penalty;
	}
	public Short getMoveBackwardsPenalty() {
		return moveBackwardsPenalty;
	}
	public short getEncumbranceLimit() {
		return encumbranceLimit;
	}
}
