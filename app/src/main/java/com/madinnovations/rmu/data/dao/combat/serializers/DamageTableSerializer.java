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
package com.madinnovations.rmu.data.dao.combat.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.madinnovations.rmu.data.dao.combat.DamageResultRowDao;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageTableSchema;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.data.entities.combat.DamageTable;

import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * Json serializer and deserializer for the {@link DamageTable} entities
 */
public class DamageTableSerializer implements JsonSerializer<DamageTable>, JsonDeserializer<DamageTable>, DamageTableSchema {
	DamageResultRowDao damageResultRowDao;

	/**
	 * Creates a new DamageTableSerializer instance.
	 */
	@Inject
	public DamageTableSerializer(DamageResultRowDao damageResultRowDao) {
		this.damageResultRowDao = damageResultRowDao;
	}

	@Override
	public JsonElement serialize(DamageTable src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_NAME, src.getName());

		JsonArray resultRows = new JsonArray();
		for(DamageResultRow resultRow : src.getResultRows()) {
			resultRows.add(resultRow.getId());
		}
		jsonObject.add(COLUMN_RESULT_ROWS, resultRows);

		return jsonObject;
	}

	@Override
	public DamageTable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		DamageTable damageTable = new DamageTable();
		JsonObject jsonObject = json.getAsJsonObject();
		damageTable.setId(jsonObject.get(COLUMN_ID).getAsInt());
		damageTable.setName(jsonObject.get(COLUMN_NAME).getAsString());

		JsonArray jsonArray = jsonObject.getAsJsonArray(COLUMN_RESULT_ROWS);
		for(JsonElement element : jsonArray) {
			damageTable.getResultRows().add(damageResultRowDao.getById(element.getAsInt()));
		}

		return damageTable;
	}
}
