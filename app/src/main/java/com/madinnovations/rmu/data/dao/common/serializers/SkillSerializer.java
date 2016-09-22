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
package com.madinnovations.rmu.data.dao.common.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.common.schemas.SkillSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillStatsSchema;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Stat;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link Skill} entities
 */
public class SkillSerializer extends TypeAdapter<Skill> implements SkillSchema {
	@Override
	public void write(JsonWriter out, Skill value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_CATEGORY_ID).value(value.getCategory().getId());
		out.name(COLUMN_REQUIRES_SPECIALIZATION).value(value.isRequiresSpecialization());
		out.name(COLUMN_USE_CATEGORY_STATS).value(value.isUseCategoryStats());
		out.name(COLUMN_REQUIRES_CONCENTRATION).value(value.isRequiresConcentration());
		out.name(COLUMN_IS_LORE).value(value.isLore());

		if(value.getStats() != null && !value.getStats().isEmpty()) {
			out.name(SkillStatsSchema.TABLE_NAME).beginArray();
			for (Stat stat : value.getStats()) {
				out.value(stat.getId());
			}
			out.endArray();
		}

		out.endObject().flush();
	}

	@Override
	public Skill read(JsonReader in) throws IOException {
		Skill skill = new Skill();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					skill.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					skill.setName(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					skill.setDescription(in.nextString());
					break;
				case COLUMN_CATEGORY_ID:
					skill.setCategory(new SkillCategory(in.nextInt()));
					break;
				case COLUMN_REQUIRES_SPECIALIZATION:
					skill.setRequiresSpecialization(in.nextBoolean());
					break;
				case COLUMN_USE_CATEGORY_STATS:
					skill.setUseCategoryStats(in.nextBoolean());
					break;
				case COLUMN_REQUIRES_CONCENTRATION:
					skill.setRequiresConcentration(in.nextBoolean());
					break;
				case COLUMN_IS_LORE:
					skill.setLore(in.nextBoolean());
					break;
				case SkillStatsSchema.TABLE_NAME:
					in.beginArray();
					while (in.hasNext()) {
						skill.getStats().add(new Stat(in.nextInt()));
					}
					in.endArray();
					break;
			}
		}
		in.endObject();

		return skill;
	}
}
