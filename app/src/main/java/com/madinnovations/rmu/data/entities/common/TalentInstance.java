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

import android.util.Log;

import com.madinnovations.rmu.data.entities.DatabaseObject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Talent parameter instance attributes
 */
public class TalentInstance extends DatabaseObject implements Cloneable {
	private Talent                 talent;
	private short                  tiers = 0;
	private Map<Parameter, Object> parameterValues = new HashMap<>();

	@Override
	public TalentInstance clone() {
		TalentInstance talentInstance = null;
		try {
			talentInstance = (TalentInstance)super.clone();
		}
		catch (CloneNotSupportedException ex) {
			Log.e(TAG, "clone: CloneNotSupportedException caught: ", ex);
		}
		return talentInstance;
	}

	/**
	 * Creates a formatted String of the instances field values.
	 *
	 * @return  a formatted String of the instances field values.
	 */
	public String print() {
		return new ToStringBuilder(this,
								   ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("talent", talent)
				.append("tiers", tiers)
				.append("parameterValues", parameterValues)
				.toString();
	}

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
	public Map<Parameter, Object> getParameterValues() {
		return parameterValues;
	}
	public void setParameterValues(Map<Parameter, Object> parameterValues) {
		this.parameterValues = parameterValues;
	}
}
