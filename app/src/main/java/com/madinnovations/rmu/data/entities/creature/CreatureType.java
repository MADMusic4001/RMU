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
package com.madinnovations.rmu.data.entities.creature;

import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.common.Talent;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing Creature Type attributes.
 */
public class CreatureType extends DatabaseObject {
	public static final String JSON_NAME = "CreatureTypes";
	private CreatureCategory category    = null;
	private String           name        = null;
	private String           description = null;
	private List<Talent>     talents     = new ArrayList<>();

	/**
	 * Creates a new CreatureType instance
	 */
	public CreatureType() {
	}

	/**
	 * Creates a new CreatureType instance with the given id
	 *
	 * @param id  the id for the new instance
	 */
	public CreatureType(int id) {
		super(id);
	}

	/**
	 * Checks the validity of the CreatureType instance.
	 *
	 * @return true if the CreatureType instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() && category != null;
	}

	@Override
	public String toString() {
		return name;
	}

	// Getters and setters
	public CreatureCategory getCategory() {
		return category;
	}
	public void setCategory(CreatureCategory category) {
		this.category = category;
	}
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
	public List<Talent> getTalents() {
		return talents;
	}
	public void setTalents(List<Talent> talents) {
		this.talents = talents;
	}
}
