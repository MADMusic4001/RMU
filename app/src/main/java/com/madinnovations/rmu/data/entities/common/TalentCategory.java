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

import com.madinnovations.rmu.data.entities.DatabaseObject;

/**
 * Talent category attributes
 */
public class TalentCategory extends DatabaseObject {
	public static final String JSON_NAME = "TalentCategories";
	private String name        = null;
	private String description = null;

	/**
	 * Creates a new TalentCategory instance
	 */
	public TalentCategory() {
	}

	/**
	 * Creates a new TalentCategory instance with the given id
	 *
	 * @param id  the id of the new instance
	 */
	public TalentCategory(int id) {
		super(id);
	}

	/**
	 * Checks the validity of the TalentCategory instance.
	 *
	 * @return true if the TalentCategory instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty();
	}

	@Override
	public String toString() {
		return name;
	}

	// Getters and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
