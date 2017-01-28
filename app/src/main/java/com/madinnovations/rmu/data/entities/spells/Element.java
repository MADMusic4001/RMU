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
package com.madinnovations.rmu.data.entities.spells;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * The element types in Rolemaster
 */
public enum Element {
	EARTH(R.string.enum_element_earth),
	FIRE(R.string.enum_element_fire),
	ICE(R.string.enum_element_ice),
	LIGHT(R.string.enum_element_light),
	WATER(R.string.enum_element_water),
	WIND(R.string.enum_element_wind);

	private @StringRes int nameResourceId;

	Element(int nameResourceId) {
		this.nameResourceId = nameResourceId;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(nameResourceId);
	}

	/**
	 * Gets the name string resource id
	 *
	 * @return  this instances name resource id
	 */
	public int getNameResourceId() {
		return nameResourceId;
	}
}
