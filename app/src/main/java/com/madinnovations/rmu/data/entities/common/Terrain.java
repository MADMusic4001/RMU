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
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 12/31/2016.
 */
public enum Terrain {
	EASY((short)20, R.string.enum_terrain_easy, R.string.enum_terrain_easy_description),
	LIGHT((short)20, R.string.enum_terrain_light, R.string.enum_terrain_light_description),
	MEDIUM((short)20, R.string.enum_terrain_medium, R.string.enum_terrain_medium_description),
	HARD((short)20, R.string.enum_terrain_hard, R.string.enum_terrain_hard_description),
	VERY_HARD((short)20, R.string.enum_terrain_very_hard, R.string.enum_terrain_very_hard_description),
	EXTREMELY_HARD((short)20, R.string.enum_terrain_extremely_hard, R.string.enum_terrain_extremely_hard_description),
	SHEER_FOLLY((short)20, R.string.enum_terrain_sheer_folly, R.string.enum_terrain_sheer_folly_description),
	ABSURD((short)20, R.string.enum_terrain_absurd, R.string.enum_terrain_absurd_description),
	NIGH_IMPOSSIBLE((short)20, R.string.enum_terrain_nigh_impossible, R.string.enum_terrain_nigh_impossible_description);

	private short bonus;
	private @StringRes int labelResourceId;
	private @StringRes int descriptionResourceId;

	Terrain(short bonus, int labelResourceId, int descriptionResourceId) {
		this.bonus = bonus;
		this.labelResourceId = labelResourceId;
		this.descriptionResourceId = descriptionResourceId;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(labelResourceId);
	}

	// Getters
	public short getBonus() {
		return bonus;
	}
	public int getLabelResourceId() {
		return labelResourceId;
	}
	public int getDescriptionResourceId() {
		return descriptionResourceId;
	}
}
