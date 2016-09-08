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
import com.madinnovations.rmu.data.entities.combat.Attack;

import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * Json serializer and deserializer for the {@link Attack} entities
 */
public class AttackSerializer implements JsonSerializer<Attack>, JsonDeserializer<Attack>, AttackSchema {
	DamageTableDao damageTableDao;
	SpecializationDao specializationDao;

	/**
	 * Creates a new AttackSerializer instance.
	 */
	@Inject
	public AttackSerializer(DamageTableDao damageTableDao, SpecializationDao specializationDao) {
		this.damageTableDao = damageTableDao;
		this.specializationDao = specializationDao;
	}

	@Override
	public JsonElement serialize(Attack src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_CODE, src.getCode());
		jsonObject.addProperty(COLUMN_NAME, src.getName());
		jsonObject.addProperty(COLUMN_DAMAGE_TABLE_ID, src.getDamageTable().getId());
		jsonObject.addProperty(COLUMN_SPECIALIZATION_ID, src.getSpecialization().getId());

		return jsonObject;
	}

	@Override
	public Attack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Attack attack = new Attack();
		JsonObject jsonObject = json.getAsJsonObject();
		attack.setId(jsonObject.get(COLUMN_ID).getAsInt());
		attack.setCode(jsonObject.get(COLUMN_CODE).getAsString());
		attack.setName(jsonObject.get(COLUMN_NAME).getAsString());
		attack.setDamageTable(damageTableDao.getById(jsonObject.get(COLUMN_DAMAGE_TABLE_ID).getAsInt()));
		attack.setSpecialization(specializationDao.getById(jsonObject.get(COLUMN_SPECIALIZATION_ID).getAsInt()));
		return attack;
	}
}
