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
package com.madinnovations.rmu.data.entities.common;

/**
 * Representation of a biome (habitat)
 */
public class Biome {
	private int id;
	private char code;
	private String description;
	private String humidity;
	private String flora;
	private String fauna;

	public boolean isValid() {
		return description != null && flora != null && fauna != null;
	}

	@Override
	public String toString() {
		return "Biome{" +
				"id=" + id +
				", code=" + code +
				", description='" + description + '\'' +
				", humidity='" + humidity + '\'' +
				", flora='" + flora + '\'' +
				", fauna='" + fauna + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Biome biome = (Biome) o;

		return id == biome.id;

	}

	@Override
	public int hashCode() {
		return id;
	}

	// Getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
