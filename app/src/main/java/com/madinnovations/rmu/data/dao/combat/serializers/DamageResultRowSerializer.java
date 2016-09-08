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
import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultRowSchema;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;

import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * Json serializer and deserializer for the {@link DamageResultRow} entities
 */
public class DamageResultRowSerializer implements JsonSerializer<DamageResultRow>, JsonDeserializer<DamageResultRow>,
		DamageResultRowSchema {
	DamageResultDao damageResultDao;
	DamageTableDao  damageTableDao;

	/**
	 * Creates a new AttackSerializer instance.
	 */
	@Inject
	public DamageResultRowSerializer(DamageResultDao damageResultDao, DamageTableDao damageTableDao) {
		this.damageResultDao = damageResultDao;
		this.damageTableDao = damageTableDao;
	}

	@Override
	public JsonElement serialize(DamageResultRow src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_RANGE_LOW_VALUE, src.getRangeLowValue());
		jsonObject.addProperty(COLUMN_RANGE_HIGH_VALUE, src.getRangeHighValue());
		jsonObject.addProperty(COLUMN_DAMAGE_TABLE_ID, src.getDamageTable().getId());

		final JsonArray skillRanksArray = new JsonArray();
		for(int i = 0; i < src.getDamageResults().length; i++) {
			JsonObject atResult = new JsonObject();
			atResult.addProperty(COLUMN_AT_RESULT_IDS[i], src.getDamageResults()[i].getId());
			skillRanksArray.add(atResult);
		}
		jsonObject.add(COLUMN_AT_RESULTS, skillRanksArray);

		return jsonObject;
	}

	@Override
	public DamageResultRow deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		DamageResultRow damageResultRow = new DamageResultRow();
		JsonObject jsonObject = json.getAsJsonObject();
		damageResultRow.setId(jsonObject.get(COLUMN_ID).getAsInt());
		damageResultRow.setRangeLowValue(jsonObject.get(COLUMN_RANGE_LOW_VALUE).getAsShort());
		damageResultRow.setRangeHighValue(jsonObject.get(COLUMN_RANGE_HIGH_VALUE).getAsShort());
		damageResultRow.setDamageTable(damageTableDao.getById(jsonObject.get(COLUMN_DAMAGE_TABLE_ID).getAsInt()));
		JsonArray atResults = jsonObject.getAsJsonArray(COLUMN_AT_RESULTS);
		for(int i = 0; i < atResults.size(); i++) {
			JsonObject atResult = atResults.get(i).getAsJsonObject();
			damageResultRow.getDamageResults()[i] = damageResultDao.getById(atResult.get(COLUMN_AT_RESULT_IDS[i]).getAsInt());
		}

		return damageResultRow;
	}
}
