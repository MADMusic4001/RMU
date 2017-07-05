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
 * Spell range types
 */
public enum Range {
	DISTANCE ("Distance (feet)", (short)1),
	DISTANCE_PER_LEVEL ("Distance (feet/lvl)", (short)1),
	DISTANCE_MILES_PER_LEVEL ("Distance (miles/lvl)", (short)1),
	DISTANCE_SHORT_RANGE ("Distance [Directed Spell Short Range] (feet)", (short)1),
	DISTANCE_SHORT_RANGE_PER_LEVEL ("Distance [Directed Spell Short Range] (feet/lvl)", (short)1),
	SELF ("Self", (short)0),
	TOUCH ("Touch", (short)0),
	VARIES("Varies", (short)0);

	private String text;
	private short parameterCount;

	Range(String text, short parameterCount) {
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
