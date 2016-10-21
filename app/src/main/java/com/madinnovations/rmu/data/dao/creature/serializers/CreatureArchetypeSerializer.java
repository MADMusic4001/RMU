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

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureArchetypeSchema;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;

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
			}
		}
		in.endObject();
		return creatureArchetype;
	}
}
