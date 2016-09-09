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
import com.madinnovations.rmu.data.dao.combat.DamageResultRowDao;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageTableSchema;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.data.entities.combat.DamageTable;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Json serializer and deserializer for the {@link DamageTable} entities
 */
public class DamageTableSerializer extends TypeAdapter<DamageTable> implements DamageTableSchema {
	DamageResultRowDao damageResultRowDao;

	/**
	 * Creates a new DamageTableSerializer instance.
	 */
	@Inject
	public DamageTableSerializer(DamageResultRowDao damageResultRowDao) {
		this.damageResultRowDao = damageResultRowDao;
	}

	@Override
	public void write(JsonWriter out, DamageTable value) throws IOException {
		out.beginObject()
				.name(COLUMN_ID).value(value.getId())
				.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_RESULT_ROWS).beginArray();
		for(DamageResultRow resultRow : value.getResultRows()) {
			out.value(resultRow.getId());
		}
		out.endArray();
		out.endObject();
	}

	@Override
	public DamageTable read(JsonReader in) throws IOException {
		DamageTable damageTable = new DamageTable();

		in.beginObject();
		in.nextName();
		damageTable.setId(in.nextInt());
		in.nextName();
		damageTable.setName(in.nextString());

		in.nextName();
		in.beginArray();
		while (in.hasNext()) {
			damageTable.getResultRows().add(damageResultRowDao.getById(in.nextInt()));
		}
		in.endArray();
		in.endObject();

		return damageTable;
	}
}
