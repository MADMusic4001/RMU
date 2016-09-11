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

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTypeSchema;
import com.madinnovations.rmu.data.entities.creature.CreatureCategory;
import com.madinnovations.rmu.data.entities.creature.CreatureType;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link CreatureType} entities
 */
public class CreatureTypeSerializer extends TypeAdapter<CreatureType> implements CreatureTypeSchema {
	@Override
	public void write(JsonWriter out, CreatureType value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_CATEGORY_ID).value(value.getCategory().getId());
		out.endObject();
		out.flush();
	}

	@Override
	public CreatureType read(JsonReader in) throws IOException {
		CreatureType creatureType = new CreatureType();
		in.beginObject();
		while (in.hasNext()) {
			switch(in.nextName()) {
				case COLUMN_ID:
					creatureType.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					creatureType.setName(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					creatureType.setDescription(in.nextString());
					break;
				case COLUMN_CATEGORY_ID:
					creatureType.setCategory(new CreatureCategory(in.nextInt()));
					break;
			}
		}
		in.endObject();
		return creatureType;
	}
}
