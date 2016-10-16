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
 * Class representing a RoleMaster stat.
 */
public class Stat {
	private static final short[] bonusRangeStart = {1, 2, 3, 4, 5, 6, 7, 9, 12, 15, 18, 24, 30, 36, 42, 48, 54, 60, 66, 72, 78,
			84, 87, 90, 93, 95, 96, 97, 98, 99, 100};
	public static final String JSON_NAME = "Stats";
	private int id = -1;
	private String abbreviation;
	private String name;
	private String description;

	/**
	 * Creates a new Stat instance.
	 */
	public Stat() {
	}

	/**
	 * Creates a new Stat instance with the given id.
	 *
	 * @param id  the id of the given stat instance
	 */
	public Stat(int id) {
		this.id = id;
	}

	/**
	 * Checks the validity of the Stat instance.
	 *
	 * @return true if the Stat instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return abbreviation != null && !abbreviation.isEmpty() && name != null && !name.isEmpty() && description != null &&
				!description.isEmpty();
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Stat stat = (Stat) o;

		return id == stat.id;
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
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
