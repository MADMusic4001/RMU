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
package com.madinnovations.rmu.data.entities.spells;

import com.madinnovations.rmu.data.entities.DatabaseObject;

/**
 * Spell type attributes
 */
public class SpellType extends DatabaseObject {
	public static final String JSON_NAME = "SpellTypes";
	private String name = null;
	private Character code = null;
	private String description = null;

	/**
	 * Creates a new SpellType instance with default values.
	 */
	public SpellType() {
		super();
	}

	/**
	 * Creates a new SpellType instance with the given id.
	 *
	 * @param id  the id to use for the new instance.
	 */
	public SpellType(int id) {
		super(id);
	}

	/**
	 * Checks the validity of the SpellType instance.
	 *
	 * @return true if the SpellType instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && code != null && description != null && !description.isEmpty();
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
	public Character getCode() {
		return code;
	}
	public void setCode(Character code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
