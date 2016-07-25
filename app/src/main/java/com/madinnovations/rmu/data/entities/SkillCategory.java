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
package com.madinnovations.rmu.data.entities;

/**
 * Skill category attributes
 */
public class SkillCategory {
	private int id;
	private String name;
	private String description;
	private Stat stat1;
	private Stat stat2;
	private Stat stat3;

	// Getters and setters
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Stat getStat1() {
		return stat1;
	}
	public void setStat1(Stat stat1) {
		this.stat1 = stat1;
	}
	public Stat getStat2() {
		return stat2;
	}
	public void setStat2(Stat stat2) {
		this.stat2 = stat2;
	}
	public Stat getStat3() {
		return stat3;
	}
	public void setStat3(Stat stat3) {
		this.stat3 = stat3;
	}
}
