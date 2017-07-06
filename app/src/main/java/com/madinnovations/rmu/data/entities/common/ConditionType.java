/*
  Copyright (C) 2017 MadInnovations
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.entities.common;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.utils.ReactiveUtils;
import com.madinnovations.rmu.data.entities.combat.CriticalSeverity;
import com.madinnovations.rmu.view.RMUApp;

import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.CREATURE_CATEGORY_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.CREATURE_TYPE_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.CREATURE_VARIETY_RX_HANDLER;
import static com.madinnovations.rmu.controller.utils.ReactiveUtils.Handler.RACE_RX_HANDLER;

/**
 * Conditional options
 */
public enum ConditionType {
	ALL_OB_TO_PARRY(R.string.enum_condition_type_all_ob_to_parry, null, null),
	CRITICAL_ACHIEVED(R.string.enum_condition_type_critical_achieved, null, null),
	CRITICAL_SEVERITY_ACHIEVED(R.string.enum_condition_type_critical_severity_achieved, null, CriticalSeverity.values()),
	PARRY_EFFECTIVE(R.string.enum_condition_type_parry_effective, null, null),
	HIT_RACE(R.string.enum_condition_type_hit_race, RACE_RX_HANDLER, null),
	HIT_CREATURE_CATEGORY(R.string.enum_condition_type_hit_creature_category, CREATURE_CATEGORY_RX_HANDLER, null),
	HIT_CREATURE_TYPE(R.string.enum_condition_type_hit_creature_type, CREATURE_TYPE_RX_HANDLER, null),
	HIT_CREATURE_VARIETY(R.string.enum_condition_type_hit_creature_variety, CREATURE_VARIETY_RX_HANDLER, null),
	HIT_SIZE(R.string.enum_condition_type_hit_size, null, Size.values()),
	IN_DARKNESS(R.string.enum_condition_type_in_darkness, null, null),
	IN_SUNLIGHT(R.string.enum_condition_type_in_sunlight, null, null),
	UNDERWATER(R.string.enum_condition_type_underwater, null, null),
	IN_FIRE(R.string.enum_condition_type_in_fire, null, null),
	OPPONENT_FUMBLE(R.string.enum_condition_type_fumbled, null, null),
	SUCCESSFUL_PERCEPTION(R.string.enum_condition_type_successful_perception, null, null);

	private @StringRes int        nameResourceId;
	private ReactiveUtils.Handler handler;
	private Enum<?>[]             enumValues;

	ConditionType(int nameResourceId, ReactiveUtils.Handler handler, Enum<?>[] enumValues) {
		this.nameResourceId = nameResourceId;
		this.handler = handler;
		this.enumValues = enumValues;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(nameResourceId);
	}

	// Getters
	public int getNameResourceId() {
		return nameResourceId;
	}
	public ReactiveUtils.Handler getHandler() {
		return handler;
	}
	public Enum<?>[] getEnumValues() {
		return enumValues;
	}
}
