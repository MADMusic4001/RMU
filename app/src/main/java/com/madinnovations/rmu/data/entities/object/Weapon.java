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
public class Weapon extends Item {
    private Skill combatSkill;
    private DamageTable damageTable;

    /**
     * Checks the validity of the Weapon instance.
     *
     * @return true if the Weapon instance is valid, otherwise false.
     */
    public boolean isValid() {
        return combatSkill != null && damageTable != null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("combatSkill", combatSkill)
                .append("damageTable", damageTable)
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
}
