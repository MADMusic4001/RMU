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

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 9/23/2016.
 */
public enum EnvironmentalCondition {
	DARKNESS("Darkness"),
	FULL_MOON("FULL_MOON"),
	NEW_MOON("NEW_MOON"),
	SUNLIGHT("Sunlight"),
	UNDERWATER("Underwater");

	private String text;

	EnvironmentalCondition(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return getText();
	}

	// Getters
	public String getText() {
		return text;
	}
}
