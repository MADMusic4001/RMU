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

import android.util.Log;
import android.util.SparseArray;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultRowSchema;
import com.madinnovations.rmu.data.entities.combat.DamageResult;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.data.entities.combat.DamageTable;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link DamageResultRow} entities
 */
public class DamageResultRowSerializer extends TypeAdapter<DamageResultRow> implements DamageResultRowSchema {
	private SparseArray<DamageResult> damageResults;

	public void setDamageResults(SparseArray<DamageResult> damageResults) {
		this.damageResults = damageResults;
	}

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
				case "atResults":
					in.beginArray();
					int resultCount = 0;
					while (in.hasNext()) {
						in.beginObject();
						short armorType = 0;
						int resultId = -1;
						while (in.hasNext()) {
							switch (in.nextName()) {
								case "index":
									armorType = (short)(in.nextInt() + 1);
									break;
								case "atResultId":
									resultId = in.nextInt();
									break;
							}
							DamageResult damageResult = damageResults.get(resultId);
							if(damageResult != null && armorType != 0) {
								resultCount++;
								damageResult.setArmorType(armorType);
								damageResult.setDamageResultRow(damageResultRow);
								damageResultRow.getResults().put(armorType, damageResult);
							}
							else if (resultId != -1){
								Log.d("RMU", "resultId " +  resultId + ". armorType = " + armorType);
							}
						}
						in.endObject();
					}
					in.endArray();
					break;
			}
		}
		in.endObject();

		return damageResultRow;
	}
}
