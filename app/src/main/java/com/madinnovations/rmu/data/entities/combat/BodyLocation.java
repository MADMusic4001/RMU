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
 * Enumeration of all body locations that can be targeted in combat.
 */
public enum BodyLocation {
	ARM(R.string.enum_body_location_arm),
  	CHEST(R.string.enum_body_location_chest),
	GROIN(R.string.enum_body_location_groin),
	HEAD(R.string.enum_body_location_head),
	LEG(R.string.enum_body_location_leg);

	private static final String[] bodyLocationNames;
	private @StringRes int textResourceId;

	static {
		bodyLocationNames = new String[BodyLocation.values().length];
		int i = 0;
		for(BodyLocation bodyLocation : BodyLocation.values()) {
			bodyLocationNames[i] = bodyLocation.toString();
		}
	}

	BodyLocation(int textResourceId) {
		this.textResourceId = textResourceId;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(textResourceId);
	}

	/**
	 * Gets the BodyLocation with the given text value.
	 *
	 * @param textValue  the textValue of the desired BodyLocation instance
	 * @return the BodyLocation instance with the given textValue or null if not found.
	 */
	public static BodyLocation getBodyLocationWithName(String textValue) {
		for(BodyLocation bodyLocation : BodyLocation.values()) {
			if(bodyLocation.toString().equals(textValue)) {
				return bodyLocation;
			}
		}
		return null;
	}

	// Getters
	public static String[] getBodyLocationNames() {
		return bodyLocationNames;
	}
}
