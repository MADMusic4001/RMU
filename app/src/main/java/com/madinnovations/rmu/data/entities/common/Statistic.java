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

import java.util.Random;

/**
 * Class representing a RoleMaster statistic.
 */
public enum Statistic {
	NON_REALM("NR", "Non-Realm Stat", ""),
	REALM("R", "Realm Stat", ""),
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
	private static final Statistic[] allStats = new Statistic[] {AGILITY, CONSTITUTION, EMPATHY, INTUITION, MEMORY, PRESENCE,
			QUICKNESS, REASONING, SELF_DISCIPLINE, STRENGTH};
	private static final short[] bonusRangeStart = {1, 2, 3, 4, 5, 6, 7, 9, 12, 15, 18, 24, 30, 36, 42, 48, 54, 60, 66, 72, 78,
			84, 87, 90, 93, 95, 96, 97, 98, 99, 100, 101};
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
		for(i = 0; i < bonusRangeStart.length - 1; i++) {
			if(statValue < bonusRangeStart[i]) {
				break;
			}
		}
		return (short)(i - 16);
	}

	/**
	 * Gets the range of stat values that provide the given bonus.
	 *
	 * @param bonus  the bonus amount
	 * @return  a short array of length 2 with the min and max stat values that provide the given bonus.
	 */
	public static short[] getRangeForBonus(short bonus) {
		short[] range = new short[2];
		if(bonus < -15 || bonus > 15) {
			throw new IllegalArgumentException("Bonus must be between -15 and 15 inclusive.");
		}

		range[0] = bonusRangeStart[bonus + 15];
		range[1] = (short)(bonusRangeStart[bonus + 16] - 1);
		return range;
	}

	/**
	 * Generates a stat gain roll.
	 *
	 * @param temp  the current stat temp value
	 * @return  the amount to increase the stat (if not constrained by the potential).
	 */
	public short statGain(short temp) {
		Random random = new Random();
		short result = 0;

		if(temp <= 6) {
			result = (short) random.nextInt(3);
		}
		else if(temp <= 8) {
			result = (short)(random.nextInt(3) + 1);
		}
		else if(temp <= 18) {
			result = (short)(random.nextInt(6) + 1);
		}
		else if(temp <= 81) {
			result = (short)(random.nextInt(10) + 1);
		}
		else if(temp <= 90) {
			result = (short)(random.nextInt(6) + 1);
		}
		else if(temp <= 92) {
			result = (short)(random.nextInt(3) + 1);
		}
		else if(temp <= 99) {
			result = (short)random.nextInt(3);
		}

		return result;
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
	public static Statistic[] getAllStats() {
		return allStats;
	}
}
