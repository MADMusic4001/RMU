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
import com.madinnovations.rmu.data.dao.spells.schemas.RealmSchema;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.spells.RealmDBO;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link RealmDBO} entities
 */
public class RealmSerializer extends TypeAdapter<RealmDBO> implements RealmSchema {
	@Override
	public void write(JsonWriter out, RealmDBO value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_STAT_NAME).value(value.getStat().name());
		out.endObject().flush();
	}

	@Override
	public RealmDBO read(JsonReader in) throws IOException {
		RealmDBO realmDBO = new RealmDBO();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					realmDBO.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					realmDBO.setName(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					realmDBO.setDescription(in.nextString());
					break;
				case COLUMN_STAT_NAME:
				case "statId":
					realmDBO.setStat(Statistic.valueOf(in.nextString()));
					break;
			}
		}
		in.endObject();
		return realmDBO;
	}
}
