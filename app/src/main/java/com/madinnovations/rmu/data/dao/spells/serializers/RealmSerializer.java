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
package com.madinnovations.rmu.data.dao.spells.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.spells.schemas.RealmSchema;
import com.madinnovations.rmu.data.entities.spells.Realm;

import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * Json serializer and deserializer for the {@link Realm} entities
 */
public class RealmSerializer implements JsonSerializer<Realm>, JsonDeserializer<Realm>, RealmSchema {
	StatDao           statDao;

	/**
	 * Creates a new RealmSerializer instance.
	 *
	 * @param statDao  a {@link StatDao} instance
	 */
	@Inject
	public RealmSerializer(StatDao statDao) {
		this.statDao = statDao;
	}

	@Override
	public JsonElement serialize(Realm src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_NAME, src.getName());
		jsonObject.addProperty(COLUMN_DESCRIPTION, src.getDescription());
		jsonObject.addProperty(COLUMN_STAT_ID, src.getStat().getId());

		return jsonObject;
	}

	@Override
	public Realm deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Realm realm = new Realm();
		JsonObject jsonObject = json.getAsJsonObject();
		realm.setId(jsonObject.get(COLUMN_ID).getAsInt());
		realm.setName(jsonObject.get(COLUMN_NAME).getAsString());
		realm.setDescription(jsonObject.get(COLUMN_DESCRIPTION).getAsString());
		realm.setStat(statDao.getById(jsonObject.get(COLUMN_STAT_ID).getAsInt()));
		return realm;
	}
}
