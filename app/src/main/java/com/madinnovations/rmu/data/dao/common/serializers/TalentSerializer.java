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

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.common.schemas.TalentParametersSchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentSchema;
import com.madinnovations.rmu.data.entities.combat.Action;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.data.entities.common.TalentParameterRow;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link Talent} entities
 */
public class TalentSerializer extends TypeAdapter<Talent> implements TalentSchema {
	@Override
	public void write(JsonWriter out, Talent value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_CATEGORY_ID).value(value.getCategory().getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_IS_FLAW).value(value.isFlaw());
		out.name(COLUMN_MIN_TIER).value(value.getMinTier());
		out.name(COLUMN_MAX_TIER).value(value.getMaxTier());
		out.name(COLUMN_DP_COST).value(value.getDpCost());
		out.name(COLUMN_DP_COST_PER_TIER).value(value.getDpCostPerTier());
		out.name(COLUMN_ACTION).value(value.getAction().name());
		out.name(COLUMN_IS_SITUATIONAL).value(value.isSituational());
		out.name("talentEffectRowLength").value(value.getTalentParameterRows().length);
		if(value.getTalentParameterRows().length > 0) {
			out.name(TalentParametersSchema.TABLE_NAME);
			out.beginArray();
			for (TalentParameterRow talentParameterRow : value.getTalentParameterRows()) {
				out.beginObject();
				out.name(TalentParametersSchema.COLUMN_EFFECT).value(talentParameterRow.getParameter().name());
				if(talentParameterRow.getValue() != null) {
					out.name(TalentParametersSchema.COLUMN_VALUE).value(talentParameterRow.getValue());
				}
				if (talentParameterRow.getEnumName() != null) {
					out.name(TalentParametersSchema.COLUMN_ENUM_NAME).value(talentParameterRow.getEnumName());
				}
				out.endObject();
			}
			out.endArray();
		}
		out.endObject();
		out.flush();
	}

	@Override
	public Talent read(JsonReader in) throws IOException {
		int talentEffectRowLength = 0;
		Talent talent = new Talent();
		in.beginObject();
		while (in.hasNext()) {
			Log.d("RMU", "TalentSerializer peek = " + in.peek());
			String name = in.nextName();
			Log.d("RMU", "TalentSerializer name = " + name);
			switch (name) {
				case COLUMN_ID:
					talent.setId(in.nextInt());
					break;
				case COLUMN_CATEGORY_ID:
					talent.setCategory(new TalentCategory(in.nextInt()));
					break;
				case COLUMN_NAME:
					talent.setName(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					talent.setDescription(in.nextString());
					break;
				case COLUMN_IS_FLAW:
					talent.setFlaw(in.nextBoolean());
					break;
				case COLUMN_MIN_TIER:
					talent.setMinTier((short) in.nextInt());
					break;
				case COLUMN_MAX_TIER:
					talent.setMaxTier((short) in.nextInt());
					break;
				case COLUMN_DP_COST:
					talent.setDpCost((short) in.nextInt());
					break;
				case COLUMN_DP_COST_PER_TIER:
					talent.setDpCostPerTier((short) in.nextInt());
					break;
				case COLUMN_IS_SITUATIONAL:
					talent.setSituational(in.nextBoolean());
					break;
				case COLUMN_ACTION:
					talent.setAction(Action.valueOf(in.nextString()));
					break;
				case "talentEffectRowLength":
					talentEffectRowLength = in.nextInt();
					break;
				case TalentParametersSchema.TABLE_NAME:
					readTalentEffectValues(in, talent, talentEffectRowLength);
					break;
			}
		}
		in.endObject();

		return talent;
	}

	private void readTalentEffectValues(JsonReader in, Talent talent, int talentEffectRowLength) throws IOException {
		TalentParameterRow[] talentParameterRows = new TalentParameterRow[talentEffectRowLength];
		int index = 0;
		in.beginArray();
		while(in.hasNext()) {
			TalentParameterRow talentParameterRow = new TalentParameterRow();
			in.beginObject();
			while(in.hasNext()) {
				switch (in.nextName()) {
					case TalentParametersSchema.COLUMN_EFFECT:
						talentParameterRow.setParameter(Parameter.valueOf(in.nextString()));
						break;
					case TalentParametersSchema.COLUMN_VALUE:
						talentParameterRow.setValue(in.nextInt());
						break;
					case TalentParametersSchema.COLUMN_ENUM_NAME:
						talentParameterRow.setEnumName(in.nextString());
						break;
				}
			}
			talentParameterRows[index++] = talentParameterRow;
			in.endObject();
		}
		in.endArray();
		talent.setTalentParameterRows(talentParameterRows);
	}
}
