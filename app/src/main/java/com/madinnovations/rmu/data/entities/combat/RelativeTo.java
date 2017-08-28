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
 * Enumeration of objects that a direction can be relative to
 */
@SuppressWarnings("unused")
public enum RelativeTo {
	COMBATANT_FACING(R.string.enum_relative_to_combatant),
	FOE_FACING(R.string.enum_relative_to_foe),
	NORTH(R.string.enum_relative_to_north),
	RANDOM(R.string.enum_relative_to_random),
	CHOICE(R.string.enum_relative_to_choice);

	private @StringRes int nameResourceId;

	RelativeTo(int nameResourceId) {
		this.nameResourceId = nameResourceId;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(nameResourceId);
	}

	// Getter
	public int getNameResourceId() {
		return nameResourceId;
	}
}
