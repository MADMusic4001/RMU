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
package com.madinnovations.rmu.data.entities.character;

import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.common.Skill;

import java.util.HashMap;
import java.util.Map;

/**
 * Culture attributes
 */
public class Culture extends DatabaseObject {
	public static final String JSON_NAME            = "Cultures";
	private String             name                 = null;
	private String             description          = null;
	private short              tradesAndCraftsRanks = 0;
	private short              otherLoreRanks       = 0;
	private Map<Skill, Short>  skillRanks           = new HashMap<>();

	/**
	 * Default constructor
	 */
	public Culture() {
	}

	/**
	 * Create a new Culture instance wit the given ID
	 *
	 * @param id  the id of the new instance
	 */
	public Culture(int id) {
		super(id);
	}

	/**
	 * Checks the validity of the Culture instance.
	 *
	 * @return true if the Culture instance is valid, otherwise false.
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
	public short getTradesAndCraftsRanks() {
		return tradesAndCraftsRanks;
	}
	public void setTradesAndCraftsRanks(short tradesAndCraftsRanks) {
		this.tradesAndCraftsRanks = tradesAndCraftsRanks;
	}
	public short getOtherLoreRanks() {
		return otherLoreRanks;
	}
	public void setOtherLoreRanks(short otherLoreRanks) {
		this.otherLoreRanks = otherLoreRanks;
	}
	public Map<Skill, Short> getSkillRanks() {
		return skillRanks;
	}
	public void setSkillRanks(Map<Skill, Short> skillRanks) {
		this.skillRanks = skillRanks;
	}
}
