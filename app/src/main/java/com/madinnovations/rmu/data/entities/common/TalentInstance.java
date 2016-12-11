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

import java.util.HashMap;
import java.util.Map;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 12/8/2016.
 */
public class TalentInstance {
	private Talent                  talent;
	private short                   tiers = 0;
	private Map<Parameter, Integer> parameterValues = new HashMap<>();

	// Getters and setters
	public Talent getTalent() {
		return talent;
	}
	public void setTalent(Talent talent) {
		this.talent = talent;
	}
	public short getTiers() {
		return tiers;
	}
	public void setTiers(short tiers) {
		this.tiers = tiers;
	}
	public Map<Parameter, Integer> getParameterValues() {
		return parameterValues;
	}
	public void setParameterValues(Map<Parameter, Integer> parameterValues) {
		this.parameterValues = parameterValues;
	}
}
