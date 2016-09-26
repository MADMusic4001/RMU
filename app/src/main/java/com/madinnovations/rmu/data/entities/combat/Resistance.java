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

/**
 * Magic/Physical element attribute
 */
public enum Resistance {
	CHANNELING("Channeling", false, false, true, false),
	COLD("Cold/Ice", true, false, false, false),
	DISEASE("Disease", false, false, false, true),
	ELECTRICITY("Electricity/Light", true, false, false, false),
	ESSENCE("Essence", false, false, true, false),
	FEAR("Fear", false, true, false, false),
	HEAT("Heat/Fire", true, false, false, false),
	MENTALISM("Mentalism", false, false, true, false),
	POISON("Poison", false, false, false, true);

	private String text;
	private boolean elemental;
	private boolean fear;
	private boolean magical;
	private boolean physical;

	Resistance(String text, boolean elemental, boolean fear, boolean magical, boolean physical) {
		this.text = text;
		this.elemental = elemental;
		this.fear = fear;
		this.magical = magical;
		this.physical = physical;
	}

	@Override
	public String toString() {
		return text;
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
	public String getText() {
		return text;
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
