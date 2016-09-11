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
import com.madinnovations.rmu.data.dao.common.schemas.SpecializationSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SpecializationStatsSchema;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Stat;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link Specialization} entities
 */
public class SpecializationSerializer extends TypeAdapter<Specialization> implements SpecializationSchema {
	@Override
	public void write(JsonWriter out, Specialization value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_SKILL_STATS).value(value.isUseSkillStats());
		out.name(COLUMN_SKILL_ID).value(value.getSkill().getId());

		out.name(SpecializationStatsSchema.TABLE_NAME).beginArray();
		for(Stat stat : value.getStats()) {
			out.value(stat.getId());
		}
		out.endArray();

		out.endObject().flush();
	}

	@Override
	public Specialization read(JsonReader in) throws IOException {
		Specialization specialization = new Specialization();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					specialization.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					specialization.setName(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					specialization.setDescription(in.nextString());
					break;
				case COLUMN_SKILL_STATS:
					specialization.setUseSkillStats(in.nextBoolean());
					break;
				case COLUMN_SKILL_ID:
					specialization.setSkill(new Skill(in.nextInt()));
					break;
				case SpecializationStatsSchema.TABLE_NAME:
					in.beginArray();
					while (in.hasNext()) {
						specialization.getStats().add(new Stat(in.nextInt()));
					}
					in.endArray();
					break;
			}
		}
		in.endObject();

		return specialization;
	}
}
