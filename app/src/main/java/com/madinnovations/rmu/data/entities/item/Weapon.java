/*
  Copyright (C) 2017 MadInnovations
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.entities.item;

import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponSchema;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;

/**
 * Weapon attributes
 */
public class Weapon extends Item {
	public static final String JSON_NAME = "Weapon";
	private short bonus = 0;
	private boolean twoHanded = false;

	/**
	 * Creates a new Weapon instance
	 */
	public Weapon() {
	}

	/**
	 * Creates a new Weapon instance with the give id
	 *
	 * @param id  the id for the new Weapon instance
	 */
	public Weapon(int id) {
		super(id);
	}

	/**
	 * Creates a new Weapon instance from the given Item
	 *
	 * @param other  an Item instance
	 */
	public Weapon(Item other) {
		this.setId(other.getId());
		this.setCampaign(other.getCampaign());
		this.setItemTemplate(other.getItemTemplate());
		this.setName(other.getName());
		this.setHistory(other.getHistory());
		this.setSize(other.getSize());
	}

	/**
	 * Checks the validity of the Weapon instance.
	 *
	 * @return true if the Weapon instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return super.isValid();
	}

	@Override
	public String print() {
		return new ToStringBuilder(this,
								   ToStringStyle.MULTI_LINE_STYLE)
				.append("item", super.print())
				.append("bonus", bonus)
				.append("twoHanded", twoHanded)
				.toString();
	}

	@Override
	public void serialize(JsonWriter out) throws IOException {
		super.serialize(out);
		out.name(WeaponSchema.COLUMN_BONUS).value(getBonus());
		out.name(WeaponSchema.COLUMN_TWO_HANDED).value(isTwoHanded());
	}

	// Getters and setters
	public short getBonus() {
		return bonus;
	}
	public void setBonus(short bonus) {
		this.bonus = bonus;
	}
	public boolean isTwoHanded() {
		return twoHanded;
	}
	public void setTwoHanded(boolean twoHanded) {
		this.twoHanded = twoHanded;
	}
	public String getJsonName() {
		return JSON_NAME;
	}
}
