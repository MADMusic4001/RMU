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
 * Enumeration of all time units
 */
public enum TimeUnit {
	DAYS(R.string.enum_time_unit_days),
	HOURS(R.string.enum_time_unit_hours),
	MINUTES(R.string.enum_time_unit_minutes),
	MONTHS(R.string.enum_time_unit_months),
	ROUNDS(R.string.enum_time_unit_rounds),
	WEEKS(R.string.enum_time_unit_weeks),
	YEARS(R.string.enum_time_unit_years);

	private @StringRes
	int labelResourceId;

	TimeUnit(int labelResourceId) {
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
