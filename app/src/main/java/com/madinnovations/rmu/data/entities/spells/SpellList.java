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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Spell list attributes
 */
public class SpellList {
	private int           id            = -1;
	private String        name          = null;
	private String        notes         = null;
	private Realm         realm         = null;
	private Realm         realm2        = null;
	private Profession    profession    = null;
	private SpellListType spellListType = null;

	/**
	 * Creates a new SpellList instance
	 */
	public SpellList() {
	}

	/**
	 * Creates a new SpellList instance with the given id
	 *
	 * @param id  the id for the new instance
	 */
	public SpellList(int id) {
		this.id = id;
	}

	/**
	 * Checks the validity of the SpellList instance.
	 *
	 * @return true if the SpellList instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && realm != null && spellListType != null;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", id)
				.append("name", name)
				.append("notes", notes)
				.append("realm", realm)
				.append("realm2", realm2)
				.append("profession", profession)
				.append("spellListType", spellListType)
				.toString();
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
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
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
