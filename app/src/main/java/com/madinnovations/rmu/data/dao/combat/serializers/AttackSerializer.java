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
import com.madinnovations.rmu.data.dao.combat.schemas.AttackSchema;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.common.Specialization;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link Attack} entities
 */
public class AttackSerializer extends TypeAdapter<Attack> implements AttackSchema {
	@Override
	public void write(JsonWriter out, Attack value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_CODE).value(value.getCode());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DAMAGE_TABLE_ID).value(value.getDamageTable().getId());
		out.name(COLUMN_SPECIALIZATION_ID).value(value.getSpecialization().getId());
		out.endObject();
	}

	@Override
	public Attack read(JsonReader in) throws IOException {
		Attack attack = new Attack();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					attack.setId(in.nextInt());
					break;
				case COLUMN_CODE:
					attack.setCode(in.nextString());
					break;
				case COLUMN_NAME:
					attack.setName(in.nextString());
					break;
				case COLUMN_DAMAGE_TABLE_ID:
					attack.setDamageTable(new DamageTable(in.nextInt()));
					break;
				case COLUMN_SPECIALIZATION_ID:
					attack.setSpecialization(new Specialization(in.nextInt()));
					break;
			}
		}
		return attack;
	}
}
