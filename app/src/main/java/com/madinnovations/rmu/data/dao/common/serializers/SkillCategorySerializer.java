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
import com.madinnovations.rmu.data.dao.common.schemas.SkillCategorySchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCategoryStatsSchema;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Statistic;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link SkillCategory} entities
 */
public class SkillCategorySerializer extends TypeAdapter<SkillCategory> implements SkillCategorySchema {
	@Override
	public void write(JsonWriter out, SkillCategory value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_IS_COMBAT).value(value.isCombat());
		out.name(COLUMN_IS_CRAFT_AND_TRADE).value(value.isCraftAndTrade());
		out.name(COLUMN_NO_STATS).value(value.isNoStats());
		out.name(COLUMN_REALM_STATS).value(value.isRealmStats());

		out.name(SkillCategoryStatsSchema.TABLE_NAME).beginArray();
		for(Statistic stat : value.getStats()) {
			out.value(stat.name());
		}
		out.endArray();

		out.endObject().flush();
	}

	@Override
	public SkillCategory read(JsonReader in) throws IOException {
		SkillCategory skillCategory = new SkillCategory();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					skillCategory.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					skillCategory.setName(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					skillCategory.setDescription(in.nextString());
					break;
				case COLUMN_IS_COMBAT:
					skillCategory.setCombat(in.nextBoolean());
					break;
				case COLUMN_IS_CRAFT_AND_TRADE:
					skillCategory.setCraftAndTrade(in.nextBoolean());
					break;
				case COLUMN_NO_STATS:
					skillCategory.setNoStats(in.nextBoolean());
					break;
				case COLUMN_REALM_STATS:
					skillCategory.setRealmStats(in.nextBoolean());
					break;
				case SkillCategoryStatsSchema.TABLE_NAME:
					in.beginArray();
					while (in.hasNext()) {
						skillCategory.getStats().add(Statistic.valueOf(in.nextString()));
					}
					in.endArray();
					break;
			}
		}
		in.endObject();

		return skillCategory;
	}
}
