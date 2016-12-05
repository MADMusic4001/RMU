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
package com.madinnovations.rmu.data.dao.creature.schemas;

/**
 * Database schema data for the creature_archetype_levels table
*/
public interface ArchetypeLevelsSchema {
	String TABLE_NAME = "creature_archetype_levels";

	String COLUMN_ARCHETYPE_ID    = "archetypeId";
	String COLUMN_LEVEL           = "level";
	String COLUMN_ATTACK          = "attack";
	String COLUMN_ATTACK2         = "attack2";
	String COLUMN_DEF_BONUS       = "defensiveBonus";
	String COLUMN_BODY_DEV        = "bodyDevelopment";
	String COLUMN_PRIME_SKILL     = "primeSkill";
	String COLUMN_SECONDARY_SKILL = "secondarySkill";
	String COLUMN_POWER_DEV       = "powerDevelopment";
	String COLUMN_SPELLS          = "spells";
	String COLUMN_TALENT_DP       = "talentDP";
	String COLUMN_AGILITY         = "agility";
	String COLUMN_CONS_STAT       = "constitutionStat";
	String COLUMN_CONSTITUTION    = "constitution";
	String COLUMN_EMPATHY         = "empathy";
	String COLUMN_INTUITION       = "intuition";
	String COLUMN_MEMORY          = "memory";
	String COLUMN_PRESENCE        = "presence";
	String COLUMN_QUICKNESS       = "quickness";
	String COLUMN_REASONING       = "reasoning";
	String COLUMN_SELF_DISC       = "selfDiscipline";
	String COLUMN_STRENGTH        = "strength";

	String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ARCHETYPE_ID + " INTEGER NOT NULL, "
			+ COLUMN_LEVEL + " INTEGER NOT NULL, "
			+ COLUMN_ATTACK + " INTEGER NOT NULL, "
			+ COLUMN_ATTACK2 + " INTEGER NOT NULL, "
			+ COLUMN_DEF_BONUS + " INTEGER NOT NULL, "
			+ COLUMN_BODY_DEV + " INTEGER NOT NULL, "
			+ COLUMN_PRIME_SKILL + " INTEGER NOT NULL, "
			+ COLUMN_SECONDARY_SKILL + " INTEGER NOT NULL, "
			+ COLUMN_POWER_DEV + " INTEGER NOT NULL, "
			+ COLUMN_SPELLS + " INTEGER NOT NULL, "
			+ COLUMN_TALENT_DP + " INTEGER NOT NULL, "
			+ COLUMN_AGILITY + " INTEGER NOT NULL, "
			+ COLUMN_CONS_STAT + " INTEGER NOT NULL, "
			+ COLUMN_CONSTITUTION + " INTEGER NOT NULL, "
			+ COLUMN_EMPATHY + " INTEGER NOT NULL, "
			+ COLUMN_INTUITION + " INTEGER NOT NULL, "
			+ COLUMN_MEMORY + " INTEGER NOT NULL, "
			+ COLUMN_PRESENCE + " INTEGER NOT NULL, "
			+ COLUMN_QUICKNESS + " INTEGER NOT NULL, "
			+ COLUMN_REASONING + " INTEGER NOT NULL, "
			+ COLUMN_SELF_DISC + " INTEGER NOT NULL, "
			+ COLUMN_STRENGTH + " INTEGER NOT NULL, "
			+ "PRIMARY KEY(" + COLUMN_ARCHETYPE_ID + "," + COLUMN_LEVEL + ")"
			+ ")";

	String[] COLUMNS = new String[] { COLUMN_ARCHETYPE_ID, COLUMN_LEVEL, COLUMN_ATTACK, COLUMN_ATTACK2, COLUMN_DEF_BONUS,
			COLUMN_BODY_DEV, COLUMN_PRIME_SKILL, COLUMN_SECONDARY_SKILL, COLUMN_POWER_DEV, COLUMN_SPELLS, COLUMN_TALENT_DP,
			COLUMN_AGILITY, COLUMN_CONS_STAT, COLUMN_CONSTITUTION, COLUMN_EMPATHY, COLUMN_INTUITION, COLUMN_MEMORY,
			COLUMN_PRESENCE, COLUMN_QUICKNESS, COLUMN_REASONING, COLUMN_SELF_DISC, COLUMN_STRENGTH};
}
