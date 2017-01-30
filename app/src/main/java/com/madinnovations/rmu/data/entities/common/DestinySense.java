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
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 1/29/2017.
 */
public enum DestinySense {
	DIRECTION(R.string.enum_destiny_sense_direction),
	DIRECTION_AND_DISTANCE(R.string.enum_destiny_sense_direction_and_distance);

	private @StringRes int nameResourceId;

	DestinySense(int nameResourceId) {
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
