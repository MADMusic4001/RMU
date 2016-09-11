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
import com.madinnovations.rmu.data.dao.spells.schemas.SpellSchema;
import com.madinnovations.rmu.data.entities.spells.Spell;
import com.madinnovations.rmu.data.entities.spells.SpellList;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link Spell} entities
 */
public class SpellSerializer extends TypeAdapter<Spell> implements SpellSchema {
	@Override
	public void write(JsonWriter out, Spell value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_SPELL_LIST_ID).value(value.getSpellList().getId());

		out.endObject().flush();
	}

	@Override
	public Spell read(JsonReader in) throws IOException {
		Spell spell = new Spell();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					spell.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					spell.setName(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					spell.setDescription(in.nextString());
					break;
				case COLUMN_SPELL_LIST_ID:
					spell.setSpellList(new SpellList(in.nextInt()));
					break;
			}
		}
		in.endObject();

		return spell;
	}
}
