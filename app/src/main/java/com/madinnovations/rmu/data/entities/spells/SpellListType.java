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
package com.madinnovations.rmu.data.entities.spells;

import com.madinnovations.rmu.data.entities.DatabaseObject;

/**
 * Spell list type attributes
 */
public class SpellListType extends DatabaseObject {
	public static final String JSON_NAME = "SpellListTypes";
	private String name = null;
	private String description = null;

	/**
	 * Creates a new SpellListType instance
	 */
	public SpellListType() {
	}

	/**
	 * Creates a new SpellListType instance with the given id
	 *
	 * @param id  the id for the new instance
	 */
	public SpellListType(int id) {
		super(id);
	}

	/**
	 * Checks the validity of the SpellListType instance.
	 *
	 * @return true if the SpellListType instance is valid, otherwise false.
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
