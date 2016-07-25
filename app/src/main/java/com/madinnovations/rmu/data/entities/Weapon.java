package com.madinnovations.rmu.data.entities;

/**
 * Weapon attributes
 */
public class Weapon extends Item {
    private Skill combatSkill;
    private DamageTable damageTable;

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
