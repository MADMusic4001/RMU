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
package com.madinnovations.rmu.data.entities.object;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Substance attributes
 */
public class Substance extends ItemTemplate {
	private SubstanceType substanceType;
	private float hardness;
	private String description;

	/**
	 * Creates a new Substance instance.
	 */
	public Substance() {
	}

	/**
	 * Creates a new Substance instance.
	 *
	 * @param id  the id of the new Substance instance
	 */
	public Substance(int id) {
		super(id);
	}

	/**
	 * Checks the validity of the Substance instance.
	 *
	 * @return true if the Substance instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return substanceType != null && description != null && !description.isEmpty();
	}

	@Override
	public String toString() {
		return String.format(RMUApp.getResourceUtils().getString(R.string.substance_display_format_string), getName(), hardness);
	}

	/**
	 * Creates a debug string containing all member variables for this instance.
	 *
	 * @return  a debug string
	 */
	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("name", getName())
				.append("substanceType", substanceType)
				.append("hardness", hardness)
				.append("description", description)
				.toString();
	}

	// Getters and setters
	public SubstanceType getSubstanceType() {
		return substanceType;
	}
	public void setSubstanceType(SubstanceType substanceType) {
		this.substanceType = substanceType;
	}
	public float getHardness() {
		return hardness;
	}
	public void setHardness(float hardness) {
		this.hardness = hardness;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
