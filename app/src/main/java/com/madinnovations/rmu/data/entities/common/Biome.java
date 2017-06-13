/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.entities.common;

import com.madinnovations.rmu.data.entities.DatabaseObject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Representation of a biome (habitat)
 */
public class Biome extends DatabaseObject {
	public static final String JSON_NAME = "Biomes";
	private char code;
	private String description;
	private String humidity;
	private String flora;
	private String fauna;

	/**
	 * Creates a new empty Biome instance.
	 */
	public Biome() {
	}

	/**
	 * Creates a new Biome instance with the given id.
	 *
	 * @param id  the id for this new instance
	 */
	public Biome(int id) {
		super(id);
	}

	/**
	 * Checks the validity of the Biome instance.
	 *
	 * @return true if the Biome instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return description != null && flora != null && fauna != null;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("code", code)
				.append("description", description)
				.append("humidity", humidity)
				.append("flora", flora)
				.append("fauna", fauna)
				.toString();
	}

	// Getters and setters
	public char getCode() {
		return code;
	}
	public void setCode(char code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getFlora() {
		return flora;
	}
	public void setFlora(String flora) {
		this.flora = flora;
	}
	public String getFauna() {
		return fauna;
	}
	public void setFauna(String fauna) {
		this.fauna = fauna;
	}
}
