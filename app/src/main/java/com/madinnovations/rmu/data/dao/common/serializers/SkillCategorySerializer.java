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

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCategorySchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCategoryStatsSchema;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Stat;

import java.lang.reflect.Type;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 9/7/2016.
 */
public class SkillCategorySerializer implements JsonSerializer<SkillCategory>, JsonDeserializer<SkillCategory>,
		SkillCategorySchema {
	@Override
	public SkillCategory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	throws JsonParseException {
		return null;
	}

	@Override
	public JsonElement serialize(SkillCategory src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_NAME, src.getName());
		jsonObject.addProperty(COLUMN_DESCRIPTION, src.getDescription());
		jsonObject.addProperty(COLUMN_IS_COMBAT, src.isCombat());
		jsonObject.addProperty(COLUMN_NO_STATS, src.isNoStats());
		jsonObject.addProperty(COLUMN_REALM_STATS, src.isRealmStats());
		JsonArray stats = new JsonArray();
		for(Stat stat : src.getStats()) {
			JsonObject statValue = new JsonObject();
			statValue.addProperty(SkillCategoryStatsSchema.COLUMN_STAT_ID, stat.getId());
			stats.add(statValue);
		}
		jsonObject.add(SkillCategoryStatsSchema.TABLE_NAME, stats);
		return jsonObject;
	}
}
