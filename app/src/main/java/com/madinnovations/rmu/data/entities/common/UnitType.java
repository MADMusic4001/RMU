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
import com.madinnovations.rmu.view.RMUApp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Unit types
 */
public enum UnitType {
	ALWAYS(R.string.enum_unit_always),
	BONUS(R.string.enum_unit_bonus),
	DAY(R.string.enum_unit_day),
	FAILURE(R.string.enum_unit_failure),
	HOUR(R.string.enum_unit_hour),
	LEVEL(R.string.enum_unit_level),
	MINUTE(R.string.enum_unit_minute),
	MONTH(R.string.enum_unit_month),
	ROUND(R.string.enum_unit_round),
	TIER(R.string.enum_unit_tier),
	UNIT(R.string.enum_unit_unit),
	VISIBLE_BLEEDING(R.string.enum_unit_visible_bleeding),
	WEEK(R.string.enum_unit_week),
	YEAR(R.string.enum_unit_year),
	PCT_PER_BONUS(R.string.enum_unit_pct_bonus),
	PCT_PER_DAY(R.string.enum_unit_pct_day),
	PCT_PER_FAILURE(R.string.enum_unit_pct_failure),
	PCT_PER_HOUR(R.string.enum_unit_pct_hour),
	PCT_PER_LEVEL(R.string.enum_unit_pct_level),
	PCT_PER_MINUTE(R.string.enum_unit_pct_minute),
	PCT_PER_MONTH(R.string.enum_unit_pct_month),
	PCT_PER_ROUND(R.string.enum_unit_pct_round),
	PCT_PER_TIER(R.string.enum_unit_pct_tier),
	PCT_PER_UNIT(R.string.enum_unit_pct_unit),
	PCT_PER_WEEK(R.string.enum_unit_pct_week),
	PCT_PER_YEAR(R.string.enum_unit_pct_year),
	BONUS_INTERVAL(R.string.enum_unit_bonus_interval),
	DAY_INTERVAL(R.string.enum_unit_day_interval),
	FAILURE_INTERVAL(R.string.enum_unit_failure_interval),
	HOUR_INTERVAL(R.string.enum_unit_hour_interval),
	LEVEL_INTERVAL(R.string.enum_unit_level_interval),
	MINUTE_INTERVAL(R.string.enum_unit_minute_interval),
	MONTH_INTERVAL(R.string.enum_unit_month_interval),
	ROUND_INTERVAL(R.string.enum_unit_round_interval),
	TIER_INTERVAL(R.string.enum_unit_tier_interval),
	UNIT_INTERVAL(R.string.enum_unit_unit_interval),
	WEEK_INTERVAL(R.string.enum_unit_week_interval),
	YEAR_INTERVAL(R.string.enum_unit_year_interval);

	private @StringRes int labelResourceId;

	UnitType(int labelResourceId) {
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
