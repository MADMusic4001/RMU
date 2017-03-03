/**
 * Copyright (C) 2017 MadInnovations
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
package com.madinnovations.rmu.data.entities.object;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

/**
 * Available equipment slots
 */
public enum Slot {
	ANY(R.string.enum_slot_any),
	MAIN_HAND(R.string.enum_slot_main_hand),
	OFF_HAND(R.string.enum_slot_off_hand),
	HEAD(R.string.enum_slot_head),
	CHEST(R.string.enum_slot_chest),
	ARMS(R.string.enum_slot_arms),
	HANDS(R.string.enum_slot_hands),
	LEGS(R.string.enum_slot_legs),
	FEET(R.string.enum_slot_feet),
	BACK(R.string.enum_slot_back),
	BACKPACK(R.string.enum_slot_backpack),
	SHIRT(R.string.enum_slot_shirt),
	PANTS(R.string.enum_slot_pants),
	NONE(R.string.enum_slot_none);

	private static final Slot[] anyOrAll = {ANY, MAIN_HAND, OFF_HAND, HEAD, CHEST, ARMS, HANDS, LEGS, FEET, BACK, BACKPACK,
			SHIRT, PANTS};
	private static final Slot[] anyOrNone = {NONE, MAIN_HAND, OFF_HAND, HEAD, CHEST, ARMS, HANDS, LEGS, FEET, BACK, BACKPACK,
			SHIRT, PANTS};

	private @StringRes int stringId;

	Slot(int stringId) {
		this.stringId = stringId;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(stringId);
	}

	// Getters
	public int getStringId() {
		return stringId;
	}
	public static Slot[] getAnyOrAll() {
		return anyOrAll;
	}
	public static Slot[] getAnyOrNone() {
		return anyOrNone;
	}
}
