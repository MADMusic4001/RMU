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
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.spells.Realm;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link Realm} entities
 */
public class RealmSerializer extends TypeAdapter<Realm> implements RealmSchema {
	@Override
	public void write(JsonWriter out, Realm value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_STAT_ID).value(value.getStat().getId());
		out.endObject().flush();
	}

	@Override
	public Realm read(JsonReader in) throws IOException {
		Realm realm = new Realm();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					realm.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					realm.setName(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					realm.setDescription(in.nextString());
					break;
				case COLUMN_STAT_ID:
					realm.setStat(new Stat(in.nextInt()));
					break;
			}
		}
		return realm;
	}
}
