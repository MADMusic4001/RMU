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
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.ParameterValue;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;

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
		out.name(COLUMN_IS_FLAW).value(value.getName());
		if(value.getAffectedSkill() != null) {
			out.name(COLUMN_AFFECTED_SKILL_ID).value(value.getAffectedSkill().getId());
		}
		out.name(COLUMN_TIER).value(value.getTier());
		out.name(COLUMN_MAX_TIERS).value(value.getMaxTiers());
		out.name(COLUMN_DP_COST).value(value.getDpCost());
		out.name(COLUMN_DP_COST_PER_TIER).value(value.getDpCostPerTier());
		out.name(COLUMN_BONUS_PER_TIER).value(value.getBonusPerTier());
		out.name(COLUMN_IS_SITUATIONAL).value(value.isSituational());
		out.name(COLUMN_ACTION_POINTS).value(value.getActionPoints());
		out.name(TalentParametersSchema.TABLE_NAME);
		out.beginArray();
		for(ParameterValue parameterValue : value.getParameterValues()) {
			out.beginObject();
			out.name(TalentParametersSchema.COLUMN_PARAMETER_ID).value(parameterValue.getParameter().getId());
			out.name(TalentParametersSchema.COLUMN_VALUE).value(parameterValue.getValue());
			out.endObject();
		}
		out.endArray();
		out.endObject();
		out.flush();
	}

	@Override
	public Talent read(JsonReader in) throws IOException {
		Talent talent = new Talent();
		in.beginObject();
		while (in.hasNext()) {
			Log.d("RMU", "TalentSerializer peek = " + in.peek());
			switch (in.nextName()) {
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
				case COLUMN_AFFECTED_SKILL_ID:
					talent.setAffectedSkill(new Skill(in.nextInt()));
					break;
				case COLUMN_TIER:
					talent.setTier((short) in.nextInt());
					break;
				case COLUMN_MAX_TIERS:
					talent.setMaxTiers((short) in.nextInt());
					break;
				case COLUMN_DP_COST:
					talent.setDpCost((short) in.nextInt());
					break;
				case COLUMN_DP_COST_PER_TIER:
					talent.setDpCostPerTier((short) in.nextInt());
					break;
				case COLUMN_BONUS_PER_TIER:
					talent.setBonusPerTier((short) in.nextInt());
					break;
				case COLUMN_IS_SITUATIONAL:
					talent.setSituational(in.nextBoolean());
					break;
				case COLUMN_ACTION_POINTS:
					talent.setActionPoints((short) in.nextInt());
					break;
				case TalentParametersSchema.TABLE_NAME:
					readParameterValues(in, talent);
					break;
			}
		}
		in.endObject();

		return talent;
	}

	private void readParameterValues(JsonReader in, Talent talent) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			Parameter newParameter = null;
			String newValue = null;
			in.beginObject();
			while(in.hasNext()) {
				switch (in.nextName()) {
					case TalentParametersSchema.COLUMN_PARAMETER_ID:
						newParameter = new Parameter(in.nextInt());
						break;
					case TalentParametersSchema.COLUMN_VALUE:
						newValue = in.nextString();
				}
			}
			ParameterValue parameterValue = new ParameterValue(newParameter, newValue);
			talent.getParameterValues().add(parameterValue);
			in.endObject();
		}
		in.endArray();
	}
}
