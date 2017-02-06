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
 * Representation of a state type
 */
public enum StateType {
	ACTION_POINT_LOSS(R.string.enum_state_type_action_point_loss),
	BLEEDING(R.string.enum_state_type_bleeding),
	CONCENTRATING(R.string.enum_state_type_concentrating),
	DAZED(R.string.enum_state_type_dazed),
	DEATH_AT(R.string.enum_state_type_death_at),
	DUPLICATED(R.string.enum_state_type_duplicated),
	DYING(R.string.enum_state_type_dying),
	ENCUMBERED(R.string.enum_state_type_encumbered),
	FATIGUED(R.string.enum_state_type_fatigued),
	FLATFOOTED(R.string.enum_state_type_flatfooted),
	FRENZIED(R.string.enum_state_type_frenzied),
	GRAPPLED(R.string.enum_state_type_grappled),
	HASTED(R.string.enum_state_type_hasted),
	HP_LOSS(R.string.enum_state_type_hp_loss),
	INJURED(R.string.enum_state_type_injured),
	KNOCKED_BACK(R.string.enum_state_type_knocked_back),
	MORALE_BONUS(R.string.enum_state_type_morale_bonus),
	NO_PARRY(R.string.enum_state_type_unable_to_parry),
	PARALYZED(R.string.enum_state_type_paralyzed),
	PP_LOSS(R.string.enum_state_type_pp_loss),
	PREPARING_SPELL(R.string.enum_state_type_preparing_spell),
	PRONE(R.string.enum_state_type_prone),
	SHAPE_CHANGED(R.string.enum_state_type_shape_changed),
	STAT_DRAINED(R.string.enum_state_type_stat_drained),
	STUNNED(R.string.enum_state_type_stunned),
	STAGGERED(R.string.enum_state_type_staggered),
	SURPRISED(R.string.enum_state_type_surprised),
	UNCONSCIOUS(R.string.enum_state_type_unconscious),
	USED_INSTANTANEOUS(R.string.enum_state_type_used_instantaneous),
	WOUNDED(R.string.enum_state_type_wounded);

	private @StringRes int nameResourceId;

	StateType(int nameResourceId) {
		this.nameResourceId = nameResourceId;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(nameResourceId);
	}

	// Getters
	public int getNameResourceId() {
		return nameResourceId;
	}
}
