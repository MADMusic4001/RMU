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

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillCostsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterStatsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterTalentsSchema;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.creature.schemas.ArchetypeSkillsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureArchetypeSchema;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.SkillCost;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Json serializer and deserializer for the {@link CreatureArchetype} entities
 */
public class CreatureArchetypeSerializer implements JsonSerializer<CreatureArchetype>, JsonDeserializer<CreatureArchetype>,
		CreatureArchetypeSchema {
	SkillCategoryDao skillCategoryDao;
	StatDao statDao;

	/**
	 * Creates a new CharacterSerializer instance.
	 */
	public CreatureArchetypeSerializer(SkillCategoryDao skillCategoryDao, StatDao statDao) {
		this.skillCategoryDao = skillCategoryDao;
		this.statDao = statDao;
	}

	@Override
	public JsonElement serialize(CreatureArchetype src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_NAME, src.getName());
		jsonObject.addProperty(COLUMN_DESCRIPTION, src.getDescription());
		if(src.getStat1() != null) {
			jsonObject.addProperty(COLUMN_STAT1_ID, src.getStat1().getId());
		}
		if(src.getStat2() != null) {
			jsonObject.addProperty(COLUMN_STAT2_ID, src.getStat2().getId());
		}
		jsonObject.addProperty(COLUMN_SPELLS, src.getSpells());
		jsonObject.addProperty(COLUMN_ROLES, src.getRoles());

		final JsonArray primarySkillsArray = new JsonArray();
		for(SkillCategory skillCategory : src.getPrimarySkills()) {
			primarySkillsArray.add(skillCategory.getId());
		}
		jsonObject.add("primary_skills", primarySkillsArray);

		final JsonArray secondarySkillsArray = new JsonArray();
		for(SkillCategory skillCategory : src.getSecondarySkills()) {
			secondarySkillsArray.add(skillCategory.getId());
		}
		jsonObject.add("secondary_skills", secondarySkillsArray);

		final JsonArray tertiarySkillsArray = new JsonArray();
		for(SkillCategory skillCategory : src.getTertiarySkills()) {
			tertiarySkillsArray.add(skillCategory.getId());
		}
		jsonObject.add("tertiary_skills", tertiarySkillsArray);

		return jsonObject;
	}

	@Override
	public CreatureArchetype deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		CreatureArchetype creatureArchetype = new CreatureArchetype();
		JsonObject jsonObject = json.getAsJsonObject();
		creatureArchetype.setId(jsonObject.get(COLUMN_ID).getAsInt());
		creatureArchetype.setName(jsonObject.get(COLUMN_NAME).getAsString());
		creatureArchetype.setDescription(jsonObject.get(COLUMN_DESCRIPTION).getAsString());
		JsonElement stat1 = jsonObject.get(COLUMN_STAT1_ID);
		if(stat1 != null) {
			creatureArchetype.setStat1(statDao.getById(stat1.getAsInt()));
		}
		JsonElement stat2 = jsonObject.get(COLUMN_STAT2_ID);
		if(stat2 != null) {
			creatureArchetype.setStat2(statDao.getById(stat2.getAsInt()));
		}

		JsonArray skillsArray = jsonObject.getAsJsonArray("primary_skills");
		for(JsonElement element : skillsArray) {
			creatureArchetype.getPrimarySkills().add(skillCategoryDao.getById(element.getAsInt()));
		}

		skillsArray = jsonObject.getAsJsonArray("secondary_skills");
		for(JsonElement element : skillsArray) {
			creatureArchetype.getSecondarySkills().add(skillCategoryDao.getById(element.getAsInt()));
		}

		skillsArray = jsonObject.getAsJsonArray("tertiary_skills");
		for(JsonElement element : skillsArray) {
			creatureArchetype.getTertiarySkills().add(skillCategoryDao.getById(element.getAsInt()));
		}

		return creatureArchetype;
	}
}
