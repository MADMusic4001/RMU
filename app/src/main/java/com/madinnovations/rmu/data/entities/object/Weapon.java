package com.madinnovations.rmu.data.entities.object;

import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.common.Skill;

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
