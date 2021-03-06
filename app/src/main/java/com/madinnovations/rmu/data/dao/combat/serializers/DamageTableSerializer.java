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
package com.madinnovations.rmu.data.dao.combat.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageTableSchema;
import com.madinnovations.rmu.data.entities.combat.DamageTable;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link DamageTable} entities
 */
public class DamageTableSerializer extends TypeAdapter<DamageTable> implements DamageTableSchema {

	@Override
	public void write(JsonWriter out, DamageTable value) throws IOException {
		out.beginObject()
				.name(COLUMN_ID).value(value.getId())
				.name(COLUMN_NAME).value(value.getName())
				.name(COLUMN_IS_BALL_TABLE).value(value.isBallTable());
		out.endObject();
	}

	@Override
	public DamageTable read(JsonReader in) throws IOException {
		DamageTable damageTable = new DamageTable();

		in.beginObject();
		while(in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					damageTable.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					damageTable.setName(in.nextString());
					break;
				case COLUMN_IS_BALL_TABLE:
					damageTable.setBallTable(in.nextBoolean());
					break;
			}
		}
		in.endObject();

		return damageTable;
	}
}
