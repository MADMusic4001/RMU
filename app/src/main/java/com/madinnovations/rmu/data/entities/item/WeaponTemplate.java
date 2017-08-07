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
package com.madinnovations.rmu.data.entities.item;

import android.util.Log;

import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponTemplateSchema;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.common.Specialization;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;

/**
 * Weapon attributes
 */
public class WeaponTemplate extends ItemTemplate {
	private static final String TAG = "WeaponTemplate";
	public static final String         JSON_NAME   = "WeaponTemplate";
    private Specialization combatSpecialization = null;
	private Attack         attack;
    private DamageTable    damageTable = null;
    private boolean        braceable   = false;
	private short	       fumble = 3;
	private float          length = 4.0f;
	private Short          sizeAdjustment = null;

	/**
	 * Creates a new WeaponTemplate instance
	 */
	public WeaponTemplate() {
	}

	/**
	 * Creates a new WeaponTemplate instance with the given id
	 *
	 * @param id  the id to use for the new instance
	 */
	public WeaponTemplate(int id) {
		super(id);
	}

	/**
	 * Creates a new WeaponTemplate instance from the given ItemTemplate
	 *
	 * @param other  an ItemTemplate instance
	 */
	public WeaponTemplate(ItemTemplate other) {
		this.setId(other.getId());
		this.setName(other.getName());
		this.setWeight(other.getWeight());
		this.setBaseCost(other.getBaseCost());
		this.setStrength(other.getStrength());
		this.setConstructionTime(other.getConstructionTime());
		this.setManeuverDifficulty(other.getManeuverDifficulty());
		this.setNotes(other.getNotes());
		this.setPrimarySlot(other.getPrimarySlot());
		this.setSecondarySlot(other.getSecondarySlot());
	}

	/**
     * Checks the validity of the Weapon instance.
     *
     * @return true if the Weapon instance is valid, otherwise false.
     */
    public boolean isValid() {
        return combatSpecialization != null && damageTable != null && super.isValid();
    }

	/**
	 * Debug output of this instance.
	 *
	 * @return a String of this instance's attributes.
	 */
	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("ItemTemplate", super.print())
				.append("combatSpecialization", combatSpecialization)
				.append("damageTable", damageTable)
				.append("braceable", braceable)
				.append("fumble", fumble)
				.append("length", length)
				.append("attack", attack)
				.toString();
	}

	/**
	 * Writes this instances fields to a JSONWriter
	 *
	 * @param out  a JSONWrite instance to write the fields to
	 * @throws IOException  when an IO error occurs
	 */
	public void serialize(JsonWriter out)
	throws IOException {
		super.serialize(out);
		out.name(WeaponTemplateSchema.COLUMN_SPECIALIZATION_ID).value(getCombatSpecialization().getId());
		out.name(WeaponTemplateSchema.COLUMN_DAMAGE_TABLE_ID).value(getDamageTable().getId());
		out.name(WeaponTemplateSchema.COLUMN_BRACEABLE).value(isBraceable());
		out.name(WeaponTemplateSchema.COLUMN_FUMBLE).value(fumble);
		out.name(WeaponTemplateSchema.COLUMN_LENGTH).value(length);
		if(sizeAdjustment != null) {
			out.name(WeaponTemplateSchema.COLUMN_SIZE_ADJUSTMENT).value(sizeAdjustment);
		}
		if(attack == null) {
			Log.d(TAG, "serialize: this = " + print());
		}
		else {
			out.name(WeaponTemplateSchema.COLUMN_ATTACK_ID).value(attack.getId());
		}
	}

	// Getters and setters
	public Specialization getCombatSpecialization() {
		return combatSpecialization;
	}
	public void setCombatSpecialization(Specialization combatSpecialization) {
		this.combatSpecialization = combatSpecialization;
	}
	public Attack getAttack() {
		return attack;
	}
	public void setAttack(Attack attack) {
		this.attack = attack;
	}
	public DamageTable getDamageTable() {
        return damageTable;
    }
    public void setDamageTable(DamageTable damageTable) {
        this.damageTable = damageTable;
    }
	public boolean isBraceable() {
		return braceable;
	}
	public void setBraceable(boolean braceable) {
		this.braceable = braceable;
	}
	public short getFumble() {
		return fumble;
	}
	public void setFumble(short fumble) {
		this.fumble = fumble;
	}
	public float getLength() {
		return length;
	}
	public void setLength(float length) {
		this.length = length;
	}
	public Short getSizeAdjustment() {
		return sizeAdjustment;
	}
	public void setSizeAdjustment(Short sizeAdjustment) {
		this.sizeAdjustment = sizeAdjustment;
	}
	@Override
	public String getJsonName() {
		return JSON_NAME;
	}
}
