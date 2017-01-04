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
package com.madinnovations.rmu.data.entities.common;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;

/**
 * Unit types
 */
public enum UnitType {
	BONUS(R.string.enum_unit_bonus),
	DAYS(R.string.enum_unit_days),
	FAILURE(R.string.enum_unit_failure),
	HOURS(R.string.enum_unit_hours),
	LEVELS(R.string.enum_unit_levels),
	MINUTES(R.string.enum_unit_minutes),
	MONTHS(R.string.enum_unit_months),
	ROUNDS(R.string.enum_unit_rounds),
	WEEKS(R.string.enum_unit_weeks),
	YEARS(R.string.enum_unit_years);

	private @StringRes int labelResourceId;

	UnitType(int labelResourceId) {
		this.labelResourceId = labelResourceId;
	}

	// Getter
	public int getLabelResourceId() {
		return labelResourceId;
	}
}
