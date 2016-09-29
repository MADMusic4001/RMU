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
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultSchema;
import com.madinnovations.rmu.data.entities.combat.CriticalType;
import com.madinnovations.rmu.data.entities.combat.DamageResult;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link DamageResult} entities
 */
public class  DamageResultSerializer extends TypeAdapter<DamageResult> implements DamageResultSchema {
	@Override
	public void write(JsonWriter out, DamageResult value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_DAMAGE_RESULT_ROW_ID).value(value.getDamageResultRow().getId());
		out.name(COLUMN_ARMOR_TYPE).value(value.getArmorType());
		out.name(COLUMN_HITS).value(value.getHits());
		if(value.getCriticalSeverity() != null && value.getCriticalType() != null) {
			out.name(COLUMN_CRITICAL_SEVERITY).value(String.valueOf(value.getCriticalSeverity()));
			out.name(COLUMN_CRITICAL_TYPE_ID).value(value.getCriticalType().getId());
		}
		out.endObject();
		out.flush();
	}

	@Override
	public DamageResult read(JsonReader in) throws IOException {
		DamageResult damageResult = new DamageResult();
		in.beginObject();
		while(in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					damageResult.setId(in.nextInt());
					break;
				case COLUMN_DAMAGE_RESULT_ROW_ID:
					damageResult.setDamageResultRow(new DamageResultRow(in.nextInt()));
					break;
				case COLUMN_ARMOR_TYPE:
					damageResult.setArmorType((short)in.nextInt());
					break;
				case COLUMN_HITS:
					damageResult.setHits((short) in.nextInt());
					break;
				case COLUMN_CRITICAL_SEVERITY:
					damageResult.setCriticalSeverity(in.nextString().charAt(0));
					break;
				case COLUMN_CRITICAL_TYPE_ID:
					damageResult.setCriticalType(new CriticalType(in.nextInt()));
					break;
			}
		}
		in.endObject();

		return damageResult;
	}
}
