/**
 * Copyright (C) 2016 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.entities.common;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

/**
 * Senses
 */
public enum Sense {
	ALL(R.string.enum_all),
	CHOICE(R.string.enum_choice),
	HEARING(R.string.enum_hearing),
	SMELL(R.string.enum_smell),
	TASTE(R.string.enum_taste),
	TOUCH(R.string.enum_touch),
	VISION(R.string.enum_vision);
	public static final int NUM_SENSES = 5;
	private static final Sense[] noChoiceSenses = new Sense[] {ALL, HEARING, SMELL, TASTE, TOUCH, VISION};
	private static final Sense[] allSenses = new Sense[] {HEARING, SMELL, TASTE, TOUCH, VISION};

	private @StringRes int stringId;

	Sense(@StringRes int stringId) {
		this.stringId = stringId;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(stringId);
	}

	// Getters
	public @StringRes int getStringId() {
		return stringId;
	}
	public static Sense[] getNoChoiceSenses() {
		return noChoiceSenses;
	}
	public static Sense[] getAllSenses() {
		return allSenses;
	}
}
