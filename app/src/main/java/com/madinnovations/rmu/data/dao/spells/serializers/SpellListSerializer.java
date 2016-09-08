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
import com.madinnovations.rmu.data.dao.character.ProfessionDao;
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.dao.spells.SpellListTypeDao;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellListSchema;
import com.madinnovations.rmu.data.entities.spells.SpellList;

import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * Json serializer and deserializer for the {@link SpellList} entities
 */
public class SpellListSerializer implements JsonSerializer<SpellList>, JsonDeserializer<SpellList>, SpellListSchema {
	ProfessionDao    professionDao;
	RealmDao         realmDao;
	SpellListTypeDao spellListTypeDao;

	/**
	 * Creates a new SpellListSerializer instance.
	 */
	@Inject
	public SpellListSerializer(ProfessionDao professionDao, RealmDao realmDao, SpellListTypeDao spellListTypeDao) {
		this.professionDao = professionDao;
		this.realmDao = realmDao;
		this.spellListTypeDao = spellListTypeDao;
	}

	@Override
	public JsonElement serialize(SpellList src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_NAME, src.getName());
		jsonObject.addProperty(COLUMN_DESCRIPTION, src.getDescription());
		jsonObject.addProperty(COLUMN_REALM_ID, src.getRealm().getId());
		if(src.getRealm2() != null) {
			jsonObject.addProperty(COLUMN_REALM2_ID, src.getRealm2().getId());
		}
		if(src.getProfession() != null) {
			jsonObject.addProperty(COLUMN_PROFESSION_ID, src.getProfession().getId());
		}
		jsonObject.addProperty(COLUMN_SPELL_LIST_TYPE_ID, src.getSpellListType().getId());

		return jsonObject;
	}

	@Override
	public SpellList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		SpellList spellList = new SpellList();
		JsonObject jsonObject = json.getAsJsonObject();

		spellList.setId(jsonObject.get(COLUMN_ID).getAsInt());
		spellList.setName(jsonObject.get(COLUMN_NAME).getAsString());
		spellList.setDescription(jsonObject.get(COLUMN_DESCRIPTION).getAsString());
		spellList.setRealm(realmDao.getById(jsonObject.get(COLUMN_REALM_ID).getAsInt()));
		JsonElement element = jsonObject.get(COLUMN_REALM2_ID);
		if(element != null) {
			spellList.setRealm2(realmDao.getById(element.getAsInt()));
		}
		element = jsonObject.get(COLUMN_PROFESSION_ID);
		if(element != null) {
			spellList.setProfession(professionDao.getById(element.getAsInt()));
		}
		spellList.setSpellListType(spellListTypeDao.getById(jsonObject.get(COLUMN_SPELL_LIST_TYPE_ID).getAsInt()));

		return spellList;
	}
}
