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

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.view.RMUApp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Spell list attributes
 */
public class SpellList extends DatabaseObject {
	public static final String        JSON_NAME     = "SpellLists";
	private             String        name          = null;
	private             String        notes         = null;
	private             RealmDBO      realmDBO      = null;
	private             RealmDBO      realmDBO2     = null;
	private             Profession    profession    = null;
	private             SpellListType spellListType = null;
	private             Skill         skill         = null;

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
		super(id);
	}

	/**
	 * Checks the validity of the SpellList instance.
	 *
	 * @return true if the SpellList instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && realmDBO != null && spellListType != null;
	}

	/**
	 * Creates a String containing the contents of this instance for debugging.
	 *
	 * @return  a debug String containing the contents of this instance.
	 */
	public String print() {
		return new ToStringBuilder(this,
								   ToStringStyle.MULTI_LINE_STYLE)
				.append("name", name)
				.append("notes", notes)
				.append("realm", realmDBO)
				.append("realm2", realmDBO2)
				.append("professionId", profession != null ? profession.getId() : null)
				.append("spellListType", spellListType)
				.append("skill", skill)
				.toString();
	}

	@Override
	public String toString() {
		String formatString = RMUApp.getResourceUtils().getString(R.string.spell_list_format_string);
		String result;

		if(profession != null) {
			if(spellListType != null) {
				result = String.format(formatString, name, spellListType, profession.getName());
			}
			else {
				result = String.format(formatString, name, profession.getName(), "");
			}
		}
		else if (realmDBO != null) {
			if(spellListType != null) {
				result = String.format(formatString, name, getSpellListType(), realmDBO.getName());
			}
			else {
				result = String.format(formatString, name, realmDBO.getName(), "");
			}
		}
		else {
			result = name;
		}

		return result;
	}

	// Getters and setters
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
	public RealmDBO getRealmDBO() {
		return realmDBO;
	}
	public void setRealmDBO(RealmDBO realmDBO) {
		this.realmDBO = realmDBO;
	}
	public RealmDBO getRealmDBO2() {
		return realmDBO2;
	}
	public void setRealmDBO2(RealmDBO realmDBO2) {
		this.realmDBO2 = realmDBO2;
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
	public Skill getSkill() {
		return skill;
	}
	public void setSkill(Skill skill) {
		this.skill = skill;
	}
}
