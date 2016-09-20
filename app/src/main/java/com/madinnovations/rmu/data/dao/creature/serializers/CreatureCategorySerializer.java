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
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureCategorySchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureCategoryTalentsSchema;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.creature.CreatureCategory;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link CreatureCategory} entities
 */
public class CreatureCategorySerializer extends TypeAdapter<CreatureCategory> implements CreatureCategorySchema {
	@Override
	public void write(JsonWriter out, CreatureCategory value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());

		out.name(CreatureCategoryTalentsSchema.TABLE_NAME).beginArray();
		for(Talent talent : value.getTalents()) {
			out.value(talent.getId());
		}
		out.endArray();

		out.endObject().flush();
	}

	@Override
	public CreatureCategory read(JsonReader in) throws IOException {
		CreatureCategory creatureCategory = new CreatureCategory();
		in.beginObject();
		while (in.hasNext()) {
			switch(in.nextName()) {
				case COLUMN_ID:
					creatureCategory.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					creatureCategory.setName(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					creatureCategory.setDescription(in.nextString());
					break;
				case CreatureCategoryTalentsSchema.TABLE_NAME:
					in.beginArray();
					while (in.hasNext()) {
						creatureCategory.getTalents().add(new Talent(in.nextInt()));
					}
					in.endArray();
					break;
			}
		}
		in.endObject();
		return creatureCategory;
	}
}
