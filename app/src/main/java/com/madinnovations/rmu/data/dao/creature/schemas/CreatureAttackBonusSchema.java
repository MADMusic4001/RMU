package com.madinnovations.rmu.data.dao.creature.schemas;

import com.madinnovations.rmu.data.dao.combat.schemas.AttackSchema;

/**
 * Database schema data for the creature_attack_bonus table
 */
public interface CreatureAttackBonusSchema {
	String TABLE_NAME = "creature_attack_bonus";

	String COLUMN_CREATURE_ID = "creatureId";
	String COLUMN_ATTACK_ID = "attackId";
	String COLUMN_BONUS = "bonus";
	String COLUMN_IS_PRIMARY = "isPrimary";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "("
			+ COLUMN_CREATURE_ID + " INTEGER NOT NULL REFERENCES "
			+ CreatureSchema.TABLE_NAME + "(" + CreatureSchema.COLUMN_ID + ") ON DELETE CASCADE, "
			+ COLUMN_ATTACK_ID + " INTEGER NOT NULL REFERENCES "
				+ AttackSchema.TABLE_NAME + "(" + AttackSchema.COLUMN_ID + "), "
			+ COLUMN_BONUS  + " INTEGER NOT NULL, "
			+ COLUMN_IS_PRIMARY + " INTEGER NOT NULL, "
			+ "PRIMARY KEY(" + COLUMN_CREATURE_ID + "," + COLUMN_ATTACK_ID + ")"
			+ ")";

	String[] COLUMNS = new String[] {COLUMN_CREATURE_ID, COLUMN_ATTACK_ID, COLUMN_BONUS, COLUMN_IS_PRIMARY};
}
