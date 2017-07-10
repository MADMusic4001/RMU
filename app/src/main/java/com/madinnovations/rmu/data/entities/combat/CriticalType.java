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
 * Enumeration of all types of criticals that can be dealt.
 */
public enum CriticalType {
	ACID(R.string.enum_critical_type_acid, 'A'),
	GRAPPLE(R.string.enum_critical_type_grapple, 'G'),
	HEAT(R.string.enum_critical_type_heat, 'H'),
	IMPACT(R.string.enum_critical_type_impact, 'I'),
	CRUSH(R.string.enum_critical_type_crush, 'K'),
	ELECTRICITY(R.string.enum_critical_type_electricity, 'L'),
	COLD(R.string.enum_critical_type_cold, 'O'),
	PUNCTURE(R.string.enum_critical_type_puncture, 'P'),
	SLASH(R.string.enum_critical_type_slash, 'S'),
	STRIKE(R.string.enum_critical_type_strike, 'T'),
	UNBALANCE(R.string.enum_critical_type_unbalance, 'U');

	private static String[] criticalTypeNames;

	private @StringRes int  textResourceId;
	private            char code;

	static {
		criticalTypeNames = new String[CriticalType.values().length];
		int i = 0;
		for(CriticalType criticalType : CriticalType.values()) {
			criticalTypeNames[i++] = criticalType.toString();
		}
	}
	CriticalType(int textResourceId, char code) {
		this.textResourceId = textResourceId;
		this.code = code;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(textResourceId);
	}

	/**
	 * Gets the CriticalType with the given text value.
	 *
	 * @param textValue  the textValue of the desired CriticalType instance
	 * @return the CriticalType instance with the given textValue or null if not found.
	 */
	public static CriticalType getCriticalTypeWithName(String textValue) {
		for(CriticalType criticalType : CriticalType.values()) {
			if(criticalType.toString().equals(textValue)) {
				return criticalType;
			}
		}
		return null;
	}

	/**
	 * Gets the CriticalType instance with the given code.
	 *
	 * @param code  the code for which the CriticalType must match
	 * @return the CriticalType instance whose code matches the given code
	 */
	public static CriticalType getTypeForCode(char code) {
		CriticalType result = null;
		for(CriticalType criticalType : CriticalType.values()) {
			if(criticalType.getCode() == code) {
				result = criticalType;
				break;
			}
		}
		return result;
	}

	// Getters
	public static String[] getCriticalTypeNames() {
		return criticalTypeNames;
	}
	public char getCode() {
		return code;
	}
}
