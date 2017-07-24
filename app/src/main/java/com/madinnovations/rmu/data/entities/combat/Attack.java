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
package com.madinnovations.rmu.data.entities.combat;

import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.common.Specialization;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Attack attributes
 */
public class Attack extends DatabaseObject {
	public static final String JSON_NAME = "Attacks";
	private String code = null;
	private String name = null;
	private DamageTable damageTable = null;
	private Specialization specialization = null;
	private short fumble = 1;

	/**
	 * Create a new Attack instance
	 */
	public Attack() {
	}

	/**
	 * Create a new Attack instance with the given id
	 *
	 * @param id  the id for the new instance
	 */
	public Attack(int id) {
		super(id);
	}

	/**
	 * Checks the validity of the Attack instance.
	 *
	 * @return true if the Attack instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return code != null && name != null && damageTable != null;
	}

	@Override
	public String toString() {
		return String.format("%1$s - %2$s", code, name);
	}

	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("code", code)
				.append("name", name)
				.append("damageTable", damageTable)
				.append("specialization", specialization)
				.append("fumble", fumble)
				.toString();
	}

	// Getters and setters
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DamageTable getDamageTable() {
		return damageTable;
	}
	public void setDamageTable(DamageTable damageTable) {
		this.damageTable = damageTable;
	}
	public Specialization getSpecialization() {
		return specialization;
	}
	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}
	public short getFumble() {
		return fumble;
	}
	public void setFumble(short fumble) {
		this.fumble = fumble;
	}
}
