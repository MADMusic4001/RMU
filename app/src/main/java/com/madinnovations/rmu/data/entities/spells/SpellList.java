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

import com.madinnovations.rmu.data.entities.character.Profession;

/**
 * Spell list attributes
 */
public class SpellList {
	private int id = -1;
	private String name = null;
	private String description = null;
	private Realm realm = null;
	private Realm realm2 = null;
	private Profession profession = null;
	private SpellListType spellListType = null;

	/**
	 * Checks the validity of the SpellList instance.
	 *
	 * @return true if the SpellList instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() && realm != null && spellListType != null;
	}

	@Override
	public String toString() {
		return "SpellList{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", realm=" + realm +
				", realm2=" + realm2 +
				", profession=" + profession +
				", spellListType=" + spellListType +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SpellList spellList = (SpellList) o;

		return id == spellList.id;
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
	public Realm getRealm() {
		return realm;
	}
	public void setRealm(Realm realm) {
		this.realm = realm;
	}
	public Realm getRealm2() {
		return realm2;
	}
	public void setRealm2(Realm realm2) {
		this.realm2 = realm2;
	}
	public Profession getProfession() {
		return profession;
	}
	public void setProfession(Profession profession) {
		this.profession = profession;
	}
	public SpellListType getSpellListType() {
		return spellListType;
	}
	public void setSpellListType(SpellListType spellListType) {
		this.spellListType = spellListType;
	}
}
