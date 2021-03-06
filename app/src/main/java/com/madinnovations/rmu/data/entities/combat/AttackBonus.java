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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Attack bonus attributes
 */
public class AttackBonus {
	private Attack attack = null;
	private short bonus = 0;
	private boolean primary = true;

	/**
	 * Creates a new AttackBonus with the given attribute values
	 *
	 * @param attack  a Attack instance to set the attack value for the new AttackBonus instance
	 * @param bonus  a short to set the bonus value for the new AttackBonus instance
	 * @param primary  true if this is a primary attack, otherwise false
	 */
	public AttackBonus(Attack attack, short bonus, boolean primary) {
		this.attack = attack;
		this.bonus = bonus;
		this.primary = primary;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("attack", attack)
				.append("bonus", bonus)
				.append("primary", primary)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AttackBonus talentBonus = (AttackBonus) o;

		return attack != null ? attack.equals(talentBonus.attack) : talentBonus.attack == null;
	}

	@Override
	public int hashCode() {
		return attack != null ? attack.hashCode() : 0;
	}

	// Getters and setters
	public Attack getAttack() {
		return attack;
	}
	public void setAttack(Attack talent) {
		this.attack = talent;
	}
	public short getBonus() {
		return bonus;
	}
	public void setBonus(short bonus) {
		this.bonus = bonus;
	}
	public boolean isPrimary() {
		return primary;
	}
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
}
