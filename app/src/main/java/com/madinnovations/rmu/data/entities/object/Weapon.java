package com.madinnovations.rmu.data.entities.object;

import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.common.Skill;

/**
 * Weapon attributes
 */
public class Weapon extends Item {
    private Skill combatSkill;
    private DamageTable damageTable;

    @Override
    public String toString() {
        return "Weapon{" +
                "combatSkill=" + combatSkill +
                ", damageTable=" + damageTable +
                '}';
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
