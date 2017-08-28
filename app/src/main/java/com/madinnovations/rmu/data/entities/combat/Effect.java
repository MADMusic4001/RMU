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

package com.madinnovations.rmu.data.entities.combat;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

/**
 * Enumeration of effects that can be applied to a target
 */
public enum Effect {
  ATTACK(R.string.enum_effect_attack),
  BLEED(R.string.enum_effect_bleed),
  BREAKAGE(R.string.enum_effect_breakage),
  CRITICAL(R.string.enum_effect_critical),
  DAZE(R.string.enum_effect_daze),
  DEATH(R.string.enum_effect_death),
  DEFENSIVE_BONUS(R.string.enum_effect_defensive_bonus),
  DROP_ITEM(R.string.enum_effect_drop_item),
  FATIGUE(R.string.enum_effect_fatigue),
  GRAPPLE(R.string.enum_effect_grapple),
  HIT_POINTS(R.string.enum_effect_hit_points),
  INITIATIVE_BONUS(R.string.enum_effect_initiative_bonus),
  INJURY(R.string.enum_effect_injury),
  KNOCK_BACK(R.string.enum_effect_knock_back),
  MORALE(R.string.enum_effect_morale),
  MOVE(R.string.enum_effect_move),
  OFFENSIVE_BONUS(R.string.enum_effect_offensive_bonus),
  PRONE(R.string.enum_effect_prone),
  SKILL_BONUS(R.string.enum_effect_skill_bonus),
  STAGGER(R.string.enum_effect_stagger),
  STUN(R.string.enum_effect_stun),
  STUN_NO_PARRY(R.string.enum_effect_stun_no_parry),
  UNCONSCIOUS(R.string.enum_effect_unconscious);

	private @StringRes
	int labelResourceId;

	Effect(int labelResourceId) {
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
