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

import com.madinnovations.rmu.data.entities.combat.CriticalCode;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Stat;

import java.util.Map;

/**
 * Creature attributes
 */
public class Creature {
	private int id;
	private CreatureTemplate template;
	private short level;
	private int maxHits;
	private int currentHits;

	// Getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public CreatureTemplate getTemplate() {
		return template;
	}
	public void setTemplate(CreatureTemplate template) {
		this.template = template;
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
