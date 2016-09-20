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

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

/**
 * Creature size class
 */
public class Size {
	private int id = -1;
	private String  code;
	private String  name;
	private String  examples;
	private Integer minWeight;
	private Integer maxWeight;
	private Integer minHeight;
	private Integer maxHeight;

	/**
	 * Creates a new Size instance
	 */
	public Size() {}

	/**
	 * Creates a new Size instance with the given id
	 *
	 * @param id  the id for the new instance
	 */
	public Size(int id) {
		this.id = id;
	}

	/**
	 * Checks the validity of the Size instance.
	 *
	 * @return true if the Size instance is valid, otherwise false.
	 */
	public boolean isValid() {
		boolean isValid = code != null && !code.isEmpty() && name != null && !name.isEmpty() && examples != null && !examples.isEmpty();
		isValid &= minHeight == null || maxHeight == null || minHeight <= maxHeight;
		isValid &= minWeight == null || maxWeight == null || minWeight <= maxWeight;
		return isValid;
	}

	@Override
	public String toString() {
		return String.format(RMUApp.getResourceUtils().getString(R.string.code_name_format_string), code, name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Size size = (Size) o;

		return id == size.id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	// Getters and setters
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExamples() {
		return examples;
	}
	public void setExamples(String examples) {
		this.examples = examples;
	}
	public Integer getMinWeight() {
		return minWeight;
	}
	public void setMinWeight(Integer minWeight) {
		this.minWeight = minWeight;
	}
	public Integer getMaxWeight() {
		return maxWeight;
	}
	public void setMaxWeight(Integer maxWeight) {
		this.maxWeight = maxWeight;
	}
	public Integer getMinHeight() {
		return minHeight;
	}
	public void setMinHeight(Integer minHeight) {
		this.minHeight = minHeight;
	}
	public Integer getMaxHeight() {
		return maxHeight;
	}
	public void setMaxHeight(Integer maxHeight) {
		this.maxHeight = maxHeight;
	}
}
