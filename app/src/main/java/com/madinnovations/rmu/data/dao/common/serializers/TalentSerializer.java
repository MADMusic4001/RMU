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

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.common.schemas.TalentParametersPerUnitSchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentParametersSchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentSchema;
import com.madinnovations.rmu.data.entities.combat.Action;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.data.entities.common.TalentParameterRow;
import com.madinnovations.rmu.data.entities.common.UnitType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		out.name(COLUMN_IS_CREATURE_ONLY).value(value.isCreatureOnly());
		out.name("talentEffectRowLength").value(value.getTalentParameterRows().length);
		if(value.getTalentParameterRows().length > 0) {
			out.name(TalentParametersSchema.TABLE_NAME);
			out.beginArray();
			for (TalentParameterRow talentParameterRow : value.getTalentParameterRows()) {
				out.beginObject();
				out.name(TalentParametersSchema.COLUMN_PARAMETER_NAME).value(talentParameterRow.getParameter().name());
				if(talentParameterRow.getInitialValue() != null) {
					out.name(TalentParametersSchema.COLUMN_INITIAL_VALUE).value(talentParameterRow.getInitialValue());
				}
				if (talentParameterRow.getEnumName() != null) {
					out.name(TalentParametersSchema.COLUMN_ENUM_NAME).value(talentParameterRow.getEnumName());
				}
				if(talentParameterRow.getPerValues() != null && talentParameterRow.getPerValues().length > 0) {
					out.name(TalentParametersPerUnitSchema.TABLE_NAME).beginArray();
					writeParameterPerUnitValues(out, talentParameterRow);
					out.endArray();
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
			String name = in.nextName();
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
				case COLUMN_IS_CREATURE_ONLY:
					talent.setCreatureOnly(in.nextBoolean());
					break;
				case COLUMN_ACTION:
					talent.setAction(Action.valueOf(in.nextString()));
					break;
				case "talentEffectRowLength":
					talentEffectRowLength = in.nextInt();
					break;
				case TalentParametersSchema.TABLE_NAME:
					readTalentParameterValues(in, talent, talentEffectRowLength);
					break;
			}
		}
		in.endObject();

		return talent;
	}

	private void readTalentParameterValues(JsonReader in, Talent talent, int talentEffectRowLength) throws IOException {
		TalentParameterRow[] talentParameterRows = new TalentParameterRow[talentEffectRowLength];
		int index = 0;
		in.beginArray();
		while(in.hasNext()) {
			TalentParameterRow talentParameterRow = new TalentParameterRow();
			Integer perValue = null;
			UnitType unitType = null;
			in.beginObject();
			while(in.hasNext()) {
				switch (in.nextName()) {
					case TalentParametersSchema.COLUMN_PARAMETER_NAME:
					case "effect":
						talentParameterRow.setParameter(Parameter.valueOf(in.nextString()));
						break;
					case TalentParametersSchema.COLUMN_INITIAL_VALUE:
						talentParameterRow.setInitialValue(in.nextInt());
						break;
					case "valuePerTier":
						perValue = in.nextInt();
						break;
					case TalentParametersSchema.COLUMN_ENUM_NAME:
						talentParameterRow.setEnumName(in.nextString());
						break;
					case "perLevel":
						if(in.nextBoolean()) {
							unitType = UnitType.LEVEL;
						}
						break;
					case "perRound":
						if(in.nextBoolean()) {
							unitType = UnitType.ROUND;
						}
						break;
					case "perTier":
						if(in.nextBoolean()) {
							unitType = UnitType.TIER;
						}
						break;
					case TalentParametersPerUnitSchema.TABLE_NAME:
						readParameterPerUnitValues(in, talentParameterRow);
						break;
				}
			}
			talentParameterRows[index++] = talentParameterRow;
			if(perValue != null && unitType != null) {
				talentParameterRow.setPerValues(new Integer[1]);
				talentParameterRow.getPerValues()[0] = perValue;
				talentParameterRow.setUnitTypes(new UnitType[1]);
				talentParameterRow.getUnitTypes()[0] = unitType;
			}
			in.endObject();
		}
		in.endArray();
		talent.setTalentParameterRows(talentParameterRows);
	}

	private void writeParameterPerUnitValues(JsonWriter out, TalentParameterRow row) throws IOException {
		for(int i = 0; i < row.getPerValues().length; i++) {
			out.beginObject();
			out.name(TalentParametersPerUnitSchema.COLUMN_PER_VALUE).value(row.getPerValues()[i]);
			out.name(TalentParametersPerUnitSchema.COLUMN_UNIT_TYPE_NAME).value(row.getUnitTypes()[i].name());
			out.endObject();
		}
	}

	private void readParameterPerUnitValues(JsonReader in, TalentParameterRow row) throws IOException {
		List<Integer> perValues = new ArrayList<>();
		List<UnitType> unitTypes = new ArrayList<>();
		in.beginArray();
		while(in.hasNext()) {
			Integer perValue = null;
			UnitType unitType = null;
			in.beginObject();
			while(in.hasNext()) {
				switch (in.nextName()) {
					case TalentParametersPerUnitSchema.COLUMN_PER_VALUE:
						perValue = in.nextInt();
						break;
					case TalentParametersPerUnitSchema.COLUMN_UNIT_TYPE_NAME:
						unitType = UnitType.valueOf(in.nextString());
						break;
				}
			}
			in.endObject();
			if(perValue != null && unitType != null) {
				perValues.add(perValue);
				unitTypes.add(unitType);
			}
			Integer[] perValuesArray = new Integer[perValues.size()];
			perValues.toArray(perValuesArray);
			row.setPerValues(perValuesArray);
			UnitType[] unitTypesArray = new UnitType[unitTypes.size()];
			unitTypes.toArray(unitTypesArray);
			row.setUnitTypes(unitTypesArray);
		}
		in.endArray();
	}
}
