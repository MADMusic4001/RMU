/**
 * Copyright (C) 2016 MadInnovations
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.entities.combat;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

/**
 * Magic/Physical element attribute
 */
public enum Resistance {
	ALL(R.string.enum_resistance_all, true, true, true, true),
	CHANNELING(R.string.enum_resistance_channeling, false, false, true, false),
	COLD(R.string.enum_resistance_cold_ice, true, false, false, false),
	DISEASE(R.string.enum_resistance_disease, false, false, false, true),
	ELECTRICITY(R.string.enum_resistance_electricity_light, true, false, false, false),
	ESSENCE(R.string.enum_resistance_essence, false, false, true, false),
	FEAR(R.string.enum_resistance_fear, false, true, false, false),
	HEAT(R.string.enum_resistance_heat_fire, true, false, false, false),
	MENTALISM(R.string.enum_resistance_mentalism, false, false, true, false),
	POISON(R.string.enum_resistance_poison, false, false, false, true);

	private @StringRes int textResourceId;
	private boolean        elemental;
	private boolean        fear;
	private boolean        magical;
	private boolean        physical;

	Resistance(int textResourceId, boolean elemental, boolean fear, boolean magical, boolean physical) {
		this.textResourceId = textResourceId;
		this.elemental = elemental;
		this.fear = fear;
		this.magical = magical;
		this.physical = physical;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(textResourceId);
	}

	/**
	 * Gets an array containing only elemental Resistance instances.
	 *
	 * @return  an array of Resistance instances with elemental flag = true.
	 */
	public static Resistance[] getElementalResistances() {
		int count = 0;
		Resistance[] values = Resistance.values();
		for(Resistance resistance : values) {
			count += (resistance.elemental) ? 1 : 0;
		}
		Resistance[] result = new Resistance[count];
		int index = 0;
		for (Resistance value : values) {
			if (value.elemental) {
				result[index++] = value;
			}
		}
		return result;
	}

	/**
	 * Gets an array containing only fear Resistance instances.
	 *
	 * @return  an array of Resistance instances with fear flag = true.
	 */
	public static Resistance[] getFearResistances() {
		int count = 0;
		Resistance[] values = Resistance.values();
		for(Resistance resistance : values) {
			count += (resistance.fear) ? 1 : 0;
		}
		Resistance[] result = new Resistance[count];
		int index = 0;
		for (Resistance value : values) {
			if (value.fear) {
				result[index++] = value;
			}
		}
		return result;
	}

	/**
	 * Gets an array containing only magical Resistance instances.
	 *
	 * @return  an array of Resistance instances with magical flag = true.
	 */
	public static Resistance[] getMagicalResistances() {
		int count = 0;
		Resistance[] values = Resistance.values();
		for(Resistance resistance : values) {
			count += (resistance.magical) ? 1 : 0;
		}
		Resistance[] result = new Resistance[count];
		int index = 0;
		for (Resistance value : values) {
			if (value.magical) {
				result[index++] = value;
			}
		}
		return result;
	}

	/**
	 * Gets an array containing only physical Resistance instances.
	 *
	 * @return  an array of Resistance instances with physical flag = true.
	 */
	public static Resistance[] getPhysicalResistances() {
		int count = 0;
		Resistance[] values = Resistance.values();
		for(Resistance resistance : values) {
			count += (resistance.physical) ? 1 : 0;
		}
		Resistance[] result = new Resistance[count];
		int index = 0;
		for (Resistance value : values) {
			if (value.physical) {
				result[index++] = value;
			}
		}
		return result;
	}

	// Getters
	public int getTextResourceId() {
		return textResourceId;
	}
	public boolean isElemental() {
		return elemental;
	}
	public boolean isFear() {
		return fear;
	}
	public boolean isMagical() {
		return magical;
	}
	public boolean isPhysical() {
		return physical;
	}
}
