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
 * Enumeration of all monetary unit values.
 */
public enum MoneyUnit {
	TRADE_BEADS(R.string.enum_money_unit_trade_bead, "tb", 1),
	COPPER_COIN(R.string.enum_money_unit_copper_coin, "cc", 36),
	BRONZE_COIN(R.string.enum_money_unit_bronze_coin, "bc", 1296),
	SILVER_COIN(R.string.enum_money_unit_silver_coin, "sc", 7776),
	LESSER_GOLD(R.string.enum_money_unit_lesser_gold, "lg", 279936),
	GREATER_GOLD(R.string.enum_money_unit_greater_gold, "gg", 1679616);

	private @StringRes int nameId;
	private String abbreviation;
	private int factorFromBase;

	MoneyUnit(int nameId, String abbreviation, int factorFromBase) {
		this.nameId = nameId;
		this.abbreviation = abbreviation;
		this.factorFromBase = factorFromBase;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(nameId);
	}

	// Getters
	public String getAbbreviation() {
		return abbreviation;
	}
	public int getFactorFromBase() {
		return factorFromBase;
	}
}
