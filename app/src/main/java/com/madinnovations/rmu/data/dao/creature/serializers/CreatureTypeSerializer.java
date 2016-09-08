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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.combat.schemas.AttackSchema;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.dao.creature.CreatureCategoryDao;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTypeSchema;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.creature.CreatureType;

import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * Json serializer and deserializer for the {@link CreatureType} entities
 */
public class CreatureTypeSerializer implements JsonSerializer<CreatureType>, JsonDeserializer<CreatureType>, CreatureTypeSchema {
	CreatureCategoryDao creatureCategoryDao;

	/**
	 * Creates a new CreatureTypeSerializer instance.
	 */
	@Inject
	public CreatureTypeSerializer(CreatureCategoryDao creatureCategoryDao) {
		this.creatureCategoryDao = creatureCategoryDao;
	}

	@Override
	public JsonElement serialize(CreatureType src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_NAME, src.getName());
		jsonObject.addProperty(COLUMN_DESCRIPTION, src.getDescription());
		jsonObject.addProperty(COLUMN_CATEGORY_ID, src.getCategory().getId());

		return jsonObject;
	}

	@Override
	public CreatureType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		CreatureType creatureType = new CreatureType();
		JsonObject jsonObject = json.getAsJsonObject();
		creatureType.setId(jsonObject.get(COLUMN_ID).getAsInt());
		creatureType.setName(jsonObject.get(COLUMN_NAME).getAsString());
		creatureType.setDescription(jsonObject.get(COLUMN_DESCRIPTION).getAsString());
		creatureType.setCategory(creatureCategoryDao.getById(jsonObject.get(COLUMN_CATEGORY_ID).getAsInt()));
		return creatureType;
	}
}
