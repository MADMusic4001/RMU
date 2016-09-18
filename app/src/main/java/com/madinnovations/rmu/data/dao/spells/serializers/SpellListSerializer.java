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

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellListSchema;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.data.entities.spells.SpellList;
import com.madinnovations.rmu.data.entities.spells.SpellListType;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link SpellList} entities
 */
public class SpellListSerializer extends TypeAdapter<SpellList> implements SpellListSchema {
	@Override
	public void write(JsonWriter out, SpellList value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_NOTES).value(value.getNotes());
		out.name(COLUMN_REALM_ID).value(value.getRealm().getId());
		if(value.getRealm2() != null) {
			out.name(COLUMN_REALM2_ID).value(value.getRealm2().getId());
		}
		if(value.getProfession() != null) {
			out.name(COLUMN_PROFESSION_ID).value(value.getProfession().getId());
		}
		out.name(COLUMN_SPELL_LIST_TYPE_ID).value(value.getSpellListType().getId());

		out.endObject().flush();
	}

	@Override
	public SpellList read(JsonReader in) throws IOException {
		SpellList spellList = new SpellList();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					spellList.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					spellList.setName(in.nextString());
					break;
				case COLUMN_NOTES:
					spellList.setNotes(in.nextString());
					break;
				case COLUMN_REALM_ID:
					spellList.setRealm(new Realm(in.nextInt()));
					break;
				case COLUMN_REALM2_ID:
					spellList.setRealm2(new Realm(in.nextInt()));
					break;
				case COLUMN_PROFESSION_ID:
					spellList.setProfession(new Profession(in.nextInt()));
					break;
				case COLUMN_SPELL_LIST_TYPE_ID:
					spellList.setSpellListType(new SpellListType(in.nextInt()));
					break;
			}
		}
		in.endObject();

		return spellList;
	}
}
