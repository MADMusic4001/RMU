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

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

/**
 * Enumeration of all positions of a combatant to it's opponent.
 */
public enum CombatPosition {
	OUT_OF_RANGE(R.string.enum_combat_position_out_of_range),
	FRONT(R.string.enum_combat_position_front),
	LEFT_FLANK(R.string.enum_combat_position_left_flank),
	RIGHT_FLANK(R.string.enum_combat_position_right_flank),
	REAR(R.string.enum_combat_position_rear);

	private static final String[] combatPositionNames;
	private @StringRes int      textResourceId;

	static {
		combatPositionNames = new String[CombatPosition.values().length];
		int i = 0;
		for(CombatPosition combatPosition: CombatPosition.values()) {
			combatPositionNames[i] = combatPosition.toString();
		}
	}

	CombatPosition(int textResourceId) {
		this.textResourceId = textResourceId;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(textResourceId);
	}

	/**
	 * Gets the CombatPosition with the given text value.
	 *
	 * @param textValue  the textValue of the desired CombatPosition instance
	 * @return the CombatPosition instance with the given textValue or null if not found.
	 */
	public static CombatPosition getCombatPositionWithName(String textValue) {
		for(CombatPosition combatPosition : CombatPosition.values()) {
			if(combatPosition.toString().equals(textValue)) {
				return combatPosition;
			}
		}
		return null;
	}

	// Getters
	public static String[] getBodyLocationNames() {
		return combatPositionNames;
	}
}