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

package com.madinnovations.rmu.data.entities.item;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

/**
 * Enumeration of all of the available poison types.
 */
public enum PoisonType {
	CIRCULATORY(R.string.enum_poison_circulatory),
	MUSCLE(R.string.enum_poison_muscle),
	NERVE(R.string.enum_poison_nerve),
	RESPIRATORY(R.string.enum_poison_respiratory);

	private @StringRes
	int textResourceId;

	/**
	 * Creates a new Material instance
	 *
	 * @param textResourceId  the string resource ID for the material's name
	 */
	PoisonType(int textResourceId) {
		this.textResourceId = textResourceId;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(textResourceId);
	}

	/**
	 * Gets the string resource ID for the name of this instance
	 *
	 * @return  the string resource ID for the name of this instance
	 */
	public int getTextResourceId() {
		return textResourceId;
	}
}
