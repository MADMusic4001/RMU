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

package com.madinnovations.rmu.data.entities.common;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

/**
 * Enumeration of all dice sizes.
 */
public enum Dice {
	D2(R.string.enum_dice_d2, (short)2),
	D4(R.string.enum_dice_d4, (short)4),
	D5(R.string.enum_dice_d5, (short)5),
	D6(R.string.enum_dice_d6, (short)6),
	D8(R.string.enum_dice_d8, (short)8),
	D10(R.string.enum_dice_d10, (short)10),
	D12(R.string.enum_dice_d12, (short)12),
	D20(R.string.enum_dice_d20, (short)20);

	private @StringRes int textResourceId;
	private short sides;

	/**
	 * Creates a new Material instance
	 *
	 * @param textResourceId  the string resource ID for the material's name
	 */
	Dice(int textResourceId, short sides) {
		this.textResourceId = textResourceId;
		this.sides = sides;
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

	public short getSides() {
		return sides;
	}
}
