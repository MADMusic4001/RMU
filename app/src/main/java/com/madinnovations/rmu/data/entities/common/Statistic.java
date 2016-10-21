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
package com.madinnovations.rmu.data.entities.common;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

/**
 * Class representing a RoleMaster statistic.
 */
public enum Statistic {
	NON_REALM("NR", "Non-Realm Stat", ""),
	AGILITY("Ag", "Agility", ""),
	CONSTITUTION("Co", "Constitution", ""),
	EMPATHY("Em", "Empathy", ""),
	INTUITION("In", "Intuition", ""),
	MEMORY("Me", "Memory", ""),
	PRESENCE("Pr", "Presence", ""),
	QUICKNESS("Qu", "Quickness", ""),
	REASONING("Re", "Reasoning", ""),
	SELF_DISCIPLINE("SD", "Self Discipline", ""),
	STRENGTH("St", "Strength", "");
	public static final int NUM_STATS = 10;
	private static final short[] bonusRangeStart = {1, 2, 3, 4, 5, 6, 7, 9, 12, 15, 18, 24, 30, 36, 42, 48, 54, 60, 66, 72, 78,
			84, 87, 90, 93, 95, 96, 97, 98, 99, 100};
	private String abbreviation;
	private String name;
	private String description;

	/**
	 * Creates a new Stat instance.
	 */
	Statistic(String abbreviation, String name, String description) {
		this.abbreviation = abbreviation;
		this.name = name;
		this.description = description;
	}

	/**
	 * Gets the bonus for a stat given its value.
	 *
	 * @param statValue  the value of the stat
	 * @return  the bonus for the given value.
	 */
	public static short getBonus(short statValue) {
		int i;
		for(i = 0; i < bonusRangeStart.length; i++) {
			if(statValue <= bonusRangeStart[i]) {
				break;
			}
		}
		return (short)(i - 15);
	}

	@Override
	public String toString() {
		return String.format(RMUApp.getResourceUtils().getString(R.string.code_name_format_string), abbreviation, name);
	}

	// Getters and setters
	public String getAbbreviation() {
		return abbreviation;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
}
