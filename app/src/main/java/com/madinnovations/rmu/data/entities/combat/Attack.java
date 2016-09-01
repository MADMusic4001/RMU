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
package com.madinnovations.rmu.data.entities.combat;

import com.madinnovations.rmu.data.entities.common.Specialization;

/**
 * Attack attributes
 */
public class Attack {
	private int id = -1;
	private String code = null;
	private String name = null;
	private DamageTable damageTable = null;
	private Specialization specialization = null;

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
		return "Attack{" +
				"id=" + id +
				", code='" + code + '\'' +
				", name='" + name + '\'' +
				", damageTable=" + damageTable +
				", specialization=" + specialization +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Attack attack = (Attack) o;

		return id == attack.id;
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
}
