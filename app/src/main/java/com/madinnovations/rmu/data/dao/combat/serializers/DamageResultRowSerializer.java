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
import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultRowSchema;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.view.RMUAppException;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Json serializer and deserializer for the {@link DamageResultRow} entities
 */
public class DamageResultRowSerializer extends TypeAdapter<DamageResultRow> implements DamageResultRowSchema {
	DamageResultDao damageResultDao;
	DamageTableDao  damageTableDao;

	/**
	 * Creates a new AttackSerializer instance.
	 */
	@Inject
	public DamageResultRowSerializer(DamageResultDao damageResultDao, DamageTableDao damageTableDao) {
		this.damageResultDao = damageResultDao;
		this.damageTableDao = damageTableDao;
	}

	@Override
	public void write(JsonWriter out, DamageResultRow value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_RANGE_LOW_VALUE).value(value.getRangeLowValue());
		out.name(COLUMN_RANGE_HIGH_VALUE).value(value.getRangeHighValue());
		out.name(COLUMN_DAMAGE_TABLE_ID).value(value.getDamageTable().getId());
		out.name(COLUMN_AT_RESULTS);
		out.beginArray();
		for(int i = 0; i < value.getDamageResults().length; i++ ) {
			if(value.getDamageResults()[i] != null) {
				out.beginObject();
				out.name(COLUMN_AT_RESULT_IDS[i]).value(value.getDamageResults()[i].getId());
				out.endObject();
			}
		}
		out.endArray();
		out.endObject();
		out.flush();
	}

	@Override
	public DamageResultRow read(JsonReader in) throws IOException {
		DamageResultRow damageResultRow = new DamageResultRow();
		in.beginObject();
		if(!in.nextName().equals(COLUMN_ID)) {
			throw new RMUAppException("Missing " + COLUMN_ID + " in json input");
		}
		damageResultRow.setId(in.nextInt());
		if(!in.nextName().equals(COLUMN_RANGE_LOW_VALUE)) {
			throw new RMUAppException("Missing " + COLUMN_RANGE_LOW_VALUE + " in json input");
		}
		damageResultRow.setRangeLowValue((short)in.nextInt());
		if(!in.nextName().equals(COLUMN_RANGE_HIGH_VALUE)) {
			throw new RMUAppException("Missing " + COLUMN_RANGE_HIGH_VALUE + " in json input");
		}
		damageResultRow.setRangeHighValue((short)in.nextInt());
		if(!in.nextName().equals(COLUMN_DAMAGE_TABLE_ID)) {
			throw new RMUAppException("Missing " + COLUMN_DAMAGE_TABLE_ID + " in json input");
		}
		damageResultRow.setDamageTable(damageTableDao.getById(in.nextInt()));

		if(!in.nextName().equals(COLUMN_AT_RESULTS)) {
			throw new RMUAppException("Missing " + COLUMN_AT_RESULTS + " in json input");
		}
		in.beginArray();
		for(int i = 0; i < 10; i++) {
			if(!in.nextName().equals(COLUMN_AT_RESULT_IDS[i])) {
				throw new RMUAppException("Missing " + COLUMN_AT_RESULT_IDS[i] + " in json input");
			}
			damageResultRow.getDamageResults()[i] = damageResultDao.getById(in.nextInt());
		}
		in.endArray();
		in.endObject();

		return damageResultRow;
	}
}
