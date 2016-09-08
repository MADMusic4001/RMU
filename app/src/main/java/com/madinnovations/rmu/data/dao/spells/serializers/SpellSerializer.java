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
import com.madinnovations.rmu.data.dao.spells.SpellListDao;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellSchema;
import com.madinnovations.rmu.data.entities.spells.Spell;

import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * Json serializer and deserializer for the {@link Spell} entities
 */
public class SpellSerializer implements JsonSerializer<Spell>, JsonDeserializer<Spell>, SpellSchema {
	SpellListDao spellListDao;

	/**
	 * Creates a new SpellSerializer instance.
	 */
	@Inject
	public SpellSerializer(SpellListDao spellListDao) {
		this.spellListDao = spellListDao;
	}

	@Override
	public JsonElement serialize(Spell src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_NAME, src.getName());
		jsonObject.addProperty(COLUMN_DESCRIPTION, src.getDescription());
		jsonObject.addProperty(COLUMN_SPELL_LIST_ID, src.getSpellList().getId());

		return jsonObject;
	}

	@Override
	public Spell deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Spell spell = new Spell();
		JsonObject jsonObject = json.getAsJsonObject();

		spell.setId(jsonObject.get(COLUMN_ID).getAsInt());
		spell.setName(jsonObject.get(COLUMN_NAME).getAsString());
		spell.setDescription(jsonObject.get(COLUMN_DESCRIPTION).getAsString());
		spell.setSpellList(spellListDao.getById(jsonObject.get(COLUMN_SPELL_LIST_ID).getAsInt()));

		return spell;
	}
}
