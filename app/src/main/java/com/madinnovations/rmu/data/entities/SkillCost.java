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
package com.madinnovations.rmu.data.entities;

/**
 * Cost of improving a skill
 */
public class SkillCost {
	private short initialCost;
	private short additionalCost;

	// Getters and setters
	public short getInitialCost() {
		return initialCost;
	}
	public void setInitialCost(short initialCost) {
		this.initialCost = initialCost;
	}
	public short getAdditionalCost() {
		return additionalCost;
	}
	public void setAdditionalCost(short additionalCost) {
		this.additionalCost = additionalCost;
	}
}
