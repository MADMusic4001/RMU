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
package com.madinnovations.rmu.data.entities.spells;

/**
 * Spell duration types.
 */
public enum Duration {
	CONCENTRATION ("Concentration", (short)0),
	CONCENTRATION_DAYS ("Concentration (max days)", (short)1),
	CONCENTRATION_HOURS_PER_LEVEL ("Concentration (max hours/lvl)", (short)1),
	CONCENTRATION_MINUTES ("Concentration (max minutes)", (short)1),
	CONCENTRATION_MINUTES_PER_LEVEL ("Concentration (max minutes/lvl)", (short)1),
	CONCENTRATION_PERMANENT ("Concentration Permanent", (short)0),
	CONCENTRATION_ROUNDS ("Concentration (max rounds)", (short)1),
	CONCENTRATION_ROUNDS_PER_LEVEL ("Concentration (max rounds/lvl)", (short)1),
	CONCENTRATION_VARIES ("Concentration Varies", (short)0),
	DAYS ("Days (days)", (short)1),
	DAYS_PER_FAIL ("Days/fail (days, fail amount)", (short)2),
	DAYS_PER_LEVEL ("Days/level (days)", (short)1),
	HOURS ("Hours (hours)", (short)1),
	HOURS_PER_FAIL ("Hours/fail (hours, fail amount)", (short)2),
	HOURS_PER_LEVEL ("Hours/level (hours)", (short)1),
	INSTANTANEOUS ("Instantaneous",  (short)0),
	MINUTES_PER_FAIL ("Minutes/fail (minutes, fail amount)", (short)2),
	MINUTES_PER_LEVEL ("Minutes/level (minutes)", (short)1),
	MONTHS ("Months (months)", (short)1),
	MONTH_PER_FAIL ("Month/fail (months, fail amount)", (short)2),
	MONTH_PER_LEVEL ("Month/level (months)", (short)1),
	ROUNDS ("Rounds (rounds)", (short)1),
	ROUNDS_PER_FAIL ("Rounds/fail (rounds, fail amount)", (short)2),
	ROUNDS_PER_LEVEL ("Rounds/level (rounds)", (short)1),
	WEEKS ("Weeks (weeks)", (short)1),
	WEEKS_PER_FAIL ("Weeks/fail (weeks, fail amount)", (short)2),
	WEEKS_PER_LEVEL ("Weeks/level (weeks)", (short)1),
	VARIES ("Varies", (short)0),
	YEARS ("Years (years)", (short)1),
	YEARS_PER_FAIL ("Years/fail (years, fail amount)", (short)2),
	YEARS_PER_LEVEL ("Years/level (years)", (short)1),
	PERMANENT ("Permanent", (short)0);

	private String text;
	private short parameterCount;

	Duration(String text, short parameterCount) {
		this.text = text;
		this.parameterCount = parameterCount;
	}

	@Override
	public String toString() {
		return text;
	}

	// Getters
	public String getText() {
		return text;
	}
	public short getParameterCount() {
		return parameterCount;
	}

}
