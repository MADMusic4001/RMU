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
package com.madinnovations.rmu.data.dao.character.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSkillRanksSchema;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.common.Skill;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Json serializer and deserializer for the {@link Culture} entities
 */
public class CultureSerializer implements JsonSerializer<Culture>, JsonDeserializer<Culture>, CultureSchema {
	SkillDao skillDao;

	/**
	 * Creates a new CultureSerializer instance.
	 */
	public CultureSerializer(SkillDao skillDao) {
		this.skillDao = skillDao;
	}

	@Override
	public JsonElement serialize(Culture src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_NAME, src.getName());
		jsonObject.addProperty(COLUMN_DESCRIPTION, src.getDescription());
		jsonObject.addProperty(COLUMN_TRADES_AND_CRAFTS_RANKS, src.getTradesAndCraftsRanks());

		final JsonArray skillRanksArray = new JsonArray();
		for(Map.Entry<Skill, Short> entry : src.getSkillRanks().entrySet()) {
			JsonObject skillRankEntry = new JsonObject();
			skillRankEntry.addProperty(CultureSkillRanksSchema.COLUMN_SKILL_ID, entry.getKey().getId());
			skillRankEntry.addProperty(CultureSkillRanksSchema.COLUMN_SKILL_RANKS, entry.getValue());
			skillRanksArray.add(skillRankEntry);
		}
		jsonObject.add(CultureSkillRanksSchema.TABLE_NAME, skillRanksArray);

		return jsonObject;
	}

	@Override
	public Culture deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Culture culture = new Culture();
		JsonObject jsonObject = json.getAsJsonObject();
		culture.setId(jsonObject.get(COLUMN_ID).getAsInt());
		culture.setName(jsonObject.get(COLUMN_NAME).getAsString());
		culture.setDescription(jsonObject.get(COLUMN_DESCRIPTION).getAsString());
		culture.setTradesAndCraftsRanks(jsonObject.get(COLUMN_TRADES_AND_CRAFTS_RANKS).getAsShort());

		JsonArray skillRanks = jsonObject.getAsJsonArray(CultureSkillRanksSchema.TABLE_NAME);
		Map<Skill, Short> skillRanksMap = new HashMap<>(skillRanks.size());
		for(JsonElement skillRankElement : skillRanks) {
			final JsonObject skillRankObject = skillRankElement.getAsJsonObject();
			Skill newSkill = new Skill(skillRankObject.get(CultureSkillRanksSchema.COLUMN_SKILL_ID).getAsInt());
			Short ranks = skillRankObject.get(CultureSkillRanksSchema.COLUMN_SKILL_RANKS).getAsShort();
			skillRanksMap.put(newSkill, ranks);
		}
		culture.setSkillRanks(skillRanksMap);

		return culture;
	}
}
