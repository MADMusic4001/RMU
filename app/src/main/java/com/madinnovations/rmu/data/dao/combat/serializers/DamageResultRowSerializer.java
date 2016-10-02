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
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultRowSchema;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.data.entities.combat.DamageTable;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link DamageResultRow} entities
 */
public class DamageResultRowSerializer extends TypeAdapter<DamageResultRow> implements DamageResultRowSchema {
	@Override
	public void write(JsonWriter out, DamageResultRow value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_RANGE_LOW_VALUE).value(value.getRangeLowValue());
		out.name(COLUMN_RANGE_HIGH_VALUE).value(value.getRangeHighValue());
		out.name(COLUMN_DAMAGE_TABLE_ID).value(value.getDamageTable().getId());
		out.endObject();
		out.flush();
	}

	@Override
	public DamageResultRow read(JsonReader in) throws IOException {
		DamageResultRow damageResultRow = new DamageResultRow();
		in.beginObject();
		while(in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					damageResultRow.setId(in.nextInt());
					break;
				case COLUMN_RANGE_LOW_VALUE:
					damageResultRow.setRangeLowValue((short) in.nextInt());
					break;
				case COLUMN_RANGE_HIGH_VALUE:
					damageResultRow.setRangeHighValue((short) in.nextInt());
					break;
				case COLUMN_DAMAGE_TABLE_ID:
					damageResultRow.setDamageTable(new DamageTable(in.nextInt()));
					break;
			}
		}
		in.endObject();

		return damageResultRow;
	}
}
