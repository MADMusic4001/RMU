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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Creature attributes
 */
public class Creature extends DatabaseObject {
	public static final String JSON_NAME = "Creatures";
	private CreatureVariety creatureVariety = null;
	private short level = 0;
	private int maxHits = 0;
	private int currentHits = 0;

	/**
	 * Checks the validity of the Creature instance.
	 *
	 * @return true if the Creature instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return creatureVariety != null;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("creatureVariety", creatureVariety)
				.append("level", level)
				.append("maxHits", maxHits)
				.append("currentHits", currentHits)
				.toString();
	}

	// Getters and setters
	public CreatureVariety getCreatureVariety() {
		return creatureVariety;
	}
	public void setCreatureVariety(CreatureVariety creatureVariety) {
		this.creatureVariety = creatureVariety;
	}
	public short getLevel() {
		return level;
	}
	public void setLevel(short level) {
		this.level = level;
	}
	public int getMaxHits() {
		return maxHits;
	}
	public void setMaxHits(int maxHits) {
		this.maxHits = maxHits;
	}
	public int getCurrentHits() {
		return currentHits;
	}
	public void setCurrentHits(int currentHits) {
		this.currentHits = currentHits;
	}
}
