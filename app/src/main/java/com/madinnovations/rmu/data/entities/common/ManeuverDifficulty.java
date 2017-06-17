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

/**
 * Maneuver difficulties
 */
public enum ManeuverDifficulty {
	CASUAL("C", "Casual", (short)70),
	SIMPLE("S", "Simple", (short)50),
	ROUTINE("R", "Routine", (short)30),
	EASY("E", "Easy", (short)20),
	LIGHT("L", "Light", (short)10),
	MEDIUM("M", "Medium", (short)0),
	HARD("H", "Hard", (short)-10),
	VERY_HARD("VH", "Very Hard", (short)-20),
	EXTREMELY_HARD("XH", "Extremely Hard", (short)-30),
	SHEER_FOLLY("SF", "Sheer Folly", (short)-50),
	ABSURD("A", "Absurd", (short)-70),
	NIGH_IMPOSSIBLE("NI", "Nigh Impossible", (short)-100);

	private String abbreviation;
	private String text;
	private short bonus;

	ManeuverDifficulty(String abbreviation, String text, short bonus) {
		this.abbreviation = abbreviation;
		this.text = text;
		this.bonus = bonus;
	}

	@Override
	public String toString() {
		return getText();
	}

	// Getters
	public String getText() {
		return text;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public short getBonus() {
		return bonus;
	}
}
