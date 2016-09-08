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
package com.madinnovations.rmu.data.dao.common.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.schemas.SpecializationSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SpecializationStatsSchema;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Stat;

import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * Json serializer and deserializer for the {@link Specialization} entities
 */
public class SpecializationSerializer implements JsonSerializer<Specialization>, JsonDeserializer<Specialization>,
		SpecializationSchema {
	SkillDao skillDao;
	StatDao statDao;

	/**
	 * Creates a new SpecializationSerializer instance.
	 *
	 * @param skillDao  a {@link SkillDao} instance
	 * @param statDao  a {@link StatDao} instance
	 */
	@Inject
	public SpecializationSerializer(SkillDao skillDao, StatDao statDao) {
		this.skillDao = skillDao;
		this.statDao = statDao;
	}

	@Override
	public JsonElement serialize(Specialization src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_NAME, src.getName());
		jsonObject.addProperty(COLUMN_DESCRIPTION, src.getDescription());
		jsonObject.addProperty(COLUMN_SKILL_STATS, src.isUseSkillStats());
		jsonObject.addProperty(COLUMN_SKILL_ID, src.getSkill().getId());

		JsonArray stats = new JsonArray();
		for(Stat stat : src.getStats()) {
			stats.add(stat.getId());
		}
		jsonObject.add(SpecializationStatsSchema.TABLE_NAME, stats);
		return jsonObject;
	}

	@Override
	public Specialization deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	throws JsonParseException {
		Specialization specialization = new Specialization();
		JsonObject jsonObject = json.getAsJsonObject();

		specialization.setId(jsonObject.get(COLUMN_ID).getAsInt());
		specialization.setName(jsonObject.get(COLUMN_NAME).getAsString());
		specialization.setDescription(jsonObject.get(COLUMN_DESCRIPTION).getAsString());
		specialization.setUseSkillStats(jsonObject.get(COLUMN_SKILL_STATS).getAsBoolean());
		specialization.setSkill(skillDao.getById(jsonObject.get(COLUMN_SKILL_ID).getAsInt()));
		JsonArray jsonArray = jsonObject.getAsJsonArray(SpecializationStatsSchema.TABLE_NAME);
		for(JsonElement jsonElement : jsonArray) {
			specialization.getStats().add(statDao.getById(jsonElement.getAsInt()));
		}

		return specialization;
	}
}
