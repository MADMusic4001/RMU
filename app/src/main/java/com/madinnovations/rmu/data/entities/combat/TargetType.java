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
 * Enumeration of the possible targets for an effect
 */
public enum TargetType {
	COMBATANT(R.string.enum_target_combatant),
	ALL_ALLIES(R.string.enum_target_all_allies),
	FRIENDS_IN_RANGE(R.string.enum_target_allies_in_range),
	MOUNT(R.string.enum_target_mount),
	OPPONENT(R.string.enum_target_foe),
	ALL_OPPONENTS(R.string.enum_target_all_foes),
	OPPONENTS_IN_RANGE(R.string.enum_target_foes_in_range);

	private @StringRes
	int labelResourceId;

	TargetType(int labelResourceId) {
		this.labelResourceId = labelResourceId;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(labelResourceId);
	}

	// Getter
	public int getLabelResourceId() {
		return labelResourceId;
	}
}
