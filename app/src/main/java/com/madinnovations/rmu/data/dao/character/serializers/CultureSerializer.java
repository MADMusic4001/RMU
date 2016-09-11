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
package com.madinnovations.rmu.data.dao.character.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSkillRanksSchema;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.common.Skill;

import java.io.IOException;
import java.util.Map;

/**
 * Json serializer and deserializer for the {@link Culture} entities
 */
public class CultureSerializer extends TypeAdapter<Culture> implements CultureSchema {
	@Override
	public void write(JsonWriter out, Culture value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_TRADES_AND_CRAFTS_RANKS).value(value.getTradesAndCraftsRanks());
		if(value.getSkillRanks() != null &&!value.getSkillRanks().isEmpty()) {
			out.name(CultureSkillRanksSchema.TABLE_NAME);
			out.beginArray();
			for(Map.Entry<Skill, Short> entry : value.getSkillRanks().entrySet()) {
				out.beginObject();
				out.name(CultureSkillRanksSchema.COLUMN_SKILL_ID).value(entry.getKey().getId());
				out.name(CultureSkillRanksSchema.COLUMN_SKILL_RANKS).value(entry.getValue());
				out.endObject();
			}
			out.endArray();
		}
		out.endObject();
		out.flush();
	}

	@Override
	public Culture read(JsonReader in) throws IOException {
		Culture culture = new Culture();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					culture.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					culture.setName(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					culture.setDescription(in.nextString());
					break;
				case COLUMN_TRADES_AND_CRAFTS_RANKS:
					culture.setTradesAndCraftsRanks((short)in.nextInt());
					break;
				case CultureSkillRanksSchema.TABLE_NAME:
					readSkillRanks(in, culture);
					break;
			}
		}
		return culture;
	}

	private void readSkillRanks(JsonReader in, Culture culture) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Skill newSkill = null;
			short ranks = 0;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CultureSkillRanksSchema.COLUMN_SKILL_ID:
						newSkill = new Skill(in.nextInt());
						break;
					case CultureSkillRanksSchema.COLUMN_SKILL_RANKS:
						ranks = (short) in.nextInt();
				}
			}
			if (newSkill != null) {
				culture.getSkillRanks().put(newSkill, ranks);
			}
			in.endObject();
		}
		in.endArray();
	}
}
