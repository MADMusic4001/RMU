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

package com.madinnovations.rmu.data.entities.spells;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.view.RMUApp;

/**
 * Enumeration of all magical realms.
 */
public enum Realm {
	CHANNELING(R.string.enum_realm_channeling, Statistic.INTUITION),
	ESSENCE(R.string.enum_realm_essence, Statistic.EMPATHY),
	MENTALISM(R.string.enum_realm_mentalism, Statistic.PRESENCE);

	private @StringRes int textResourceId;
	private Statistic statistic;

	/**
	 * Creates a new Material instance
	 *
	 * @param textResourceId  the string resource ID for the material's name
	 * @param statistic  the statistic associated with this realm of magic
	 */
	Realm(int textResourceId, Statistic statistic) {
		this.textResourceId = textResourceId;
		this.statistic = statistic;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(textResourceId);
	}

	/**
	 * Gets the Realm with the given text value.
	 *
	 * @param textValue  the textValue of the desired Realm instance
	 * @return the Realm instance with the given textValue or null if not found.
	 */
	public static Realm getRealmWithName(String textValue) {
		for(Realm realm : Realm.values()) {
			if(realm.toString().equals(textValue)) {
				return realm;
			}
		}
		return null;
	}

	// Getters
	public int getTextResourceId() {
		return textResourceId;
	}
	public Statistic getStatistic() {
		return statistic;
	}
}
