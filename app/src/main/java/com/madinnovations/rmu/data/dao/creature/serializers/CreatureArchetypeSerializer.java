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
package com.madinnovations.rmu.data.dao.creature.serializers;

import android.util.SparseArray;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.creature.schemas.ArchetypeLevelsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureArchetypeSchema;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetypeLevel;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link CreatureArchetype} entities
 */
public class CreatureArchetypeSerializer extends TypeAdapter<CreatureArchetype> implements CreatureArchetypeSchema {
	private static final String PRIMARY_SKILLS = "primary_skills";
	private static final String SECONDARY_SKILLS = "secondary_skills";
	private static final String TERTIARY_SKILLS = "tertiary_skills";

	@Override
	public void write(JsonWriter out, CreatureArchetype value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_STAT1_IS_REALM).value(value.isRealmStat1());
		if(value.getStat1() != null) {
			out.name(COLUMN_STAT1_NAME).value(value.getStat1().name());
		}
		out.name(COLUMN_STAT2_IS_REALM).value(value.isRealmStat2());
		if(value.getStat2() != null) {
			out.name(COLUMN_STAT2_NAME).value(value.getStat2().name());
		}
		out.name(COLUMN_SPELLS).value(value.getSpells());
		out.name(COLUMN_ROLES).value(value.getRoles());

		out.name(PRIMARY_SKILLS).beginArray();
		for(SkillCategory skillCategory : value.getPrimarySkills()) {
			out.value(skillCategory.getId());
		}
		out.endArray();

		out.name(SECONDARY_SKILLS).beginArray();
		for(SkillCategory skillCategory : value.getSecondarySkills()) {
			out.value(skillCategory.getId());
		}
		out.endArray();

		out.name(TERTIARY_SKILLS).beginArray();
		for(SkillCategory skillCategory : value.getTertiarySkills()) {
			out.value(skillCategory.getId());
		}
		out.endArray();

		out.name(ArchetypeLevelsSchema.TABLE_NAME).beginArray();
		for(int i = 0; i < value.getLevels().size(); i++) {
			CreatureArchetypeLevel level = value.getLevels().valueAt(i);
			out.beginObject();
			out.name(ArchetypeLevelsSchema.COLUMN_LEVEL).value(level.getLevel());
			out.name(ArchetypeLevelsSchema.COLUMN_ATTACK).value(level.getAttack());
			out.name(ArchetypeLevelsSchema.COLUMN_ATTACK2).value(level.getAttack2());
			out.name(ArchetypeLevelsSchema.COLUMN_DEF_BONUS).value(level.getDefensiveBonus());
			out.name(ArchetypeLevelsSchema.COLUMN_BODY_DEV).value(level.getBodyDevelopment());
			out.name(ArchetypeLevelsSchema.COLUMN_PRIME_SKILL).value(level.getPrimeSkill());
			out.name(ArchetypeLevelsSchema.COLUMN_SECONDARY_SKILL).value(level.getSecondarySkill());
			out.name(ArchetypeLevelsSchema.COLUMN_POWER_DEV).value(level.getPowerDevelopment());
			out.name(ArchetypeLevelsSchema.COLUMN_SPELLS).value(level.getSpells());
			out.name(ArchetypeLevelsSchema.COLUMN_TALENT_DP).value(level.getTalentDP());
			out.name(ArchetypeLevelsSchema.COLUMN_AGILITY).value(level.getAgility());
			out.name(ArchetypeLevelsSchema.COLUMN_CONS_STAT).value(level.getConstitutionStat());
			out.name(ArchetypeLevelsSchema.COLUMN_CONSTITUTION).value(level.getConstitution());
			out.name(ArchetypeLevelsSchema.COLUMN_EMPATHY).value(level.getEmpathy());
			out.name(ArchetypeLevelsSchema.COLUMN_INTUITION).value(level.getIntuition());
			out.name(ArchetypeLevelsSchema.COLUMN_MEMORY).value(level.getMemory());
			out.name(ArchetypeLevelsSchema.COLUMN_PRESENCE).value(level.getPresence());
			out.name(ArchetypeLevelsSchema.COLUMN_QUICKNESS).value(level.getQuickness());
			out.name(ArchetypeLevelsSchema.COLUMN_REASONING).value(level.getReasoning());
			out.name(ArchetypeLevelsSchema.COLUMN_SELF_DISC).value(level.getSelfDiscipline());
			out.name(ArchetypeLevelsSchema.COLUMN_STRENGTH).value(level.getStrength());
			out.endObject();
		}
		out.endArray();

		out.endObject();
		out.flush();
	}

	@Override
	public CreatureArchetype read(JsonReader in) throws IOException {
		CreatureArchetype creatureArchetype = new CreatureArchetype();
		in.beginObject();
		while(in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					creatureArchetype.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					creatureArchetype.setName(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					creatureArchetype.setDescription(in.nextString());
					break;
				case COLUMN_STAT1_IS_REALM:
					creatureArchetype.setRealmStat1(in.nextBoolean());
					break;
				case COLUMN_STAT1_NAME:
				case "stat1Id":
					creatureArchetype.setStat1(Statistic.valueOf(in.nextString()));
					break;
				case COLUMN_STAT2_IS_REALM:
					creatureArchetype.setRealmStat2(in.nextBoolean());
					break;
				case COLUMN_STAT2_NAME:
				case "stat2Id":
					creatureArchetype.setStat2(Statistic.valueOf(in.nextString()));
					break;
				case COLUMN_SPELLS:
					creatureArchetype.setSpells(in.nextString());
					break;
				case COLUMN_ROLES:
					creatureArchetype.setRoles(in.nextString());
					break;
				case PRIMARY_SKILLS:
					in.beginArray();
					while(in.hasNext()) {
						creatureArchetype.getPrimarySkills().add(new SkillCategory(in.nextInt()));
					}
					in.endArray();
					break;
				case SECONDARY_SKILLS:
					in.beginArray();
					while(in.hasNext()) {
						creatureArchetype.getSecondarySkills().add(new SkillCategory(in.nextInt()));
					}
					in.endArray();
					break;
				case TERTIARY_SKILLS:
					in.beginArray();
					while(in.hasNext()) {
						creatureArchetype.getTertiarySkills().add(new SkillCategory(in.nextInt()));
					}
					in.endArray();
					break;
				case ArchetypeLevelsSchema.TABLE_NAME:
					readLevels(in, creatureArchetype);
					break;
			}
		}
		in.endObject();
		return creatureArchetype;
	}

	private void readLevels(JsonReader in, CreatureArchetype creatureArchetype) throws IOException {
		in.beginArray();
		SparseArray<CreatureArchetypeLevel> levelSparseArray = new SparseArray<>();
		while (in.hasNext()) {
			CreatureArchetypeLevel level = new CreatureArchetypeLevel();
			level.setCreatureArchetype(creatureArchetype);
			int levelValue = 0;
			in.beginObject();
			while(in.hasNext()) {
				switch (in.nextName()) {
					case ArchetypeLevelsSchema.COLUMN_LEVEL:
						levelValue = in.nextInt();
						level.setLevel((short)levelValue);
						break;
					case ArchetypeLevelsSchema.COLUMN_ATTACK:
						level.setAttack((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_ATTACK2:
						level.setAttack2((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_DEF_BONUS:
						level.setDefensiveBonus((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_BODY_DEV:
						level.setBodyDevelopment((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_PRIME_SKILL:
						level.setPrimeSkill((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_SECONDARY_SKILL:
						level.setSecondarySkill((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_POWER_DEV:
						level.setPowerDevelopment((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_SPELLS:
						level.setSpells((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_TALENT_DP:
						level.setTalentDP((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_AGILITY:
						level.setAgility((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_CONS_STAT:
						level.setConstitutionStat((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_CONSTITUTION:
						level.setConstitution((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_EMPATHY:
						level.setEmpathy((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_INTUITION:
						level.setIntuition((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_MEMORY:
						level.setMemory((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_PRESENCE:
						level.setPresence((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_QUICKNESS:
						level.setQuickness((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_REASONING:
						level.setReasoning((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_SELF_DISC:
						level.setSelfDiscipline((short)in.nextInt());
						break;
					case ArchetypeLevelsSchema.COLUMN_STRENGTH:
						level.setStrength((short)in.nextInt());
						break;
				}
			}
			levelSparseArray.put(levelValue, level);
			in.endObject();
		}
		in.endArray();
		creatureArchetype.setLevels(levelSparseArray);
	}
}
