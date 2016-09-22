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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Magic/Physical element attribute
 */
public enum Resistance {
	CHANNELING("Channeling", false, true),
	COLD("Cold/Ice", true, false),
	DISEASE("Disease", false, false),
	ELECTRICITY("Electricity/Light", true, false),
	ESSENCE("Essence", false, true),
	FEAR("Fea", false, false),
	HEAT("Heat/Fire", true, false),
	MENTALISM("Mentalism", false, true),
	POISON("Poison", false, false);

	private String text;
	private boolean elemental;
	private boolean magical;

	Resistance(String text, boolean elemental, boolean magical) {
		this.text = text;
		this.elemental = elemental;
		this.magical = magical;
	}

	@Override
	public String toString() {
		return text;
	}

	// Getters
	public String getText() {
		return text;
	}
	public boolean isElemental() {
		return elemental;
	}
	public boolean isMagical() {
		return magical;
	}
}
