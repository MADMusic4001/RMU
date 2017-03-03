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
package com.madinnovations.rmu.data.entities.object;

import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.common.Skill;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Weapon attributes
 */
public class WeaponTemplate extends ItemTemplate {
	public static final String JSON_NAME = "WeaponTemplate";
    private Skill combatSkill = null;
    private DamageTable damageTable = null;
    private boolean braceable = false;

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
        return combatSkill != null && damageTable != null;
    }

	/**
	 * Debug output of this instance.
	 *
	 * @return a String of this instance's attributes.
	 */
	public String debugToString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("combatSkill", combatSkill)
				.append("damageTable", damageTable)
				.append("braceable", braceable)
				.toString();
	}

	// Getters and setters
    public Skill getCombatSkill() {
        return combatSkill;
    }
    public void setCombatSkill(Skill combatSkill) {
        this.combatSkill = combatSkill;
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
}
