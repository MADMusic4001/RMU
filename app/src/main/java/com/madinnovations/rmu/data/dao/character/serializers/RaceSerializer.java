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
package com.madinnovations.rmu.data.dao.character.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.character.schemas.RaceCulturesSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceRealmRRModSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceStatModSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceTalentParametersSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceTalentsSchema;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.spells.RealmDBO;

import java.io.IOException;
import java.util.Map;

/**
 * Json serializer and deserializer for the {@link Race} entities
 */
public class RaceSerializer extends TypeAdapter<Race> implements RaceSchema {
	@SuppressWarnings("unused")
	private static final String TAG = "RaceSerializer";

	@Override
	public void write(JsonWriter out, Race value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_BONUS_DEVELOPMENT_POINTS).value(value.getBonusDevelopmentPoints());
		out.name(COLUMN_PHYSICAL_RESISTANCE_MODIFIER).value(value.getPhysicalResistanceModifier());
		out.name(COLUMN_ENDURANCE_MODIFIER).value(value.getEnduranceModifier());
		out.name(COLUMN_BASE_HITS).value(value.getBaseHits());
		out.name(COLUMN_RECOVERY_MULTIPLIER).value(value.getRecoveryMultiplier());
		out.name(COLUMN_AVERAGE_HEIGHT).value(value.getAverageHeight());
		out.name(COLUMN_AVERAGE_WEIGHT).value(value.getAverageWeight());
		out.name(COLUMN_POUNDS_PER_INCH).value(value.getPoundsPerInch());
		out.name(COLUMN_SIZE_ID).value(value.getSize().getId());

		out.name(RaceRealmRRModSchema.TABLE_NAME).beginArray();
		for(Map.Entry<RealmDBO, Short> entry : value.getRealmResistancesModifiers().entrySet()) {
			out.beginObject();
			out.name(RaceRealmRRModSchema.COLUMN_REALM_ID).value(entry.getKey().getId());
			out.name(RaceRealmRRModSchema.COLUMN_MODIFIER).value(entry.getValue());
			out.endObject();
		}
		out.endArray();

		out.name(RaceStatModSchema.TABLE_NAME).beginArray();
		for(Map.Entry<Statistic, Short> entry : value.getStatModifiers().entrySet()) {
			out.beginObject();
			out.name(RaceStatModSchema.COLUMN_STAT_NAME).value(entry.getKey().name());
			out.name(RaceStatModSchema.COLUMN_MODIFIER).value(entry.getValue());
			out.endObject();
		}
		out.endArray();

		out.name(RaceTalentsSchema.TABLE_NAME).beginArray();
		for(TalentInstance talentInstance : value.getTalentsAndFlawsList()) {
			out.beginObject();
			out.name(RaceTalentsSchema.COLUMN_ID).value(talentInstance.getId());
			out.name(RaceTalentsSchema.COLUMN_TALENT_ID).value(talentInstance.getTalent().getId());
			out.name(RaceTalentsSchema.COLUMN_TIERS).value(talentInstance.getTiers());
			if(!talentInstance.getParameterValues().isEmpty()) {
				out.name(RaceTalentParametersSchema.TABLE_NAME).beginArray();
				for(Map.Entry<Parameter, Object> paramEntry : talentInstance.getParameterValues().entrySet()) {
					out.beginObject();
					out.name(RaceTalentParametersSchema.COLUMN_PARAMETER_NAME).value(paramEntry.getKey().name());
					if(paramEntry.getValue() != null) {
						if(paramEntry.getValue() instanceof Integer) {
							out.name(RaceTalentParametersSchema.COLUMN_INT_VALUE).value((Integer) paramEntry.getValue());
						}
						else {
							out.name(RaceTalentParametersSchema.COLUMN_ENUM_NAME).value((String) paramEntry.getValue());
						}
					}
					out.endObject();
				}
				out.endArray();
			}
			out.endObject();
		}
		out.endArray();

		if(value.getAllowedCultures() != null && !value.getAllowedCultures().isEmpty()) {
			out.name(RaceCulturesSchema.TABLE_NAME).beginArray();
			for(Culture culture : value.getAllowedCultures()) {
				out.value(culture.getId());
			}
			out.endArray();
		}
		out.endObject().flush();
	}

	@Override
	public Race read(JsonReader in) throws IOException {
		Race race = new Race();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					race.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					race.setName(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					race.setDescription(in.nextString());
					break;
				case COLUMN_BONUS_DEVELOPMENT_POINTS:
					race.setBonusDevelopmentPoints((short)in.nextInt());
					break;
				case COLUMN_PHYSICAL_RESISTANCE_MODIFIER:
					race.setPhysicalResistanceModifier((short)in.nextInt());
					break;
				case COLUMN_ENDURANCE_MODIFIER:
					race.setEnduranceModifier((short)in.nextInt());
					break;
				case COLUMN_BASE_HITS:
					race.setBaseHits((short)in.nextInt());
					break;
				case COLUMN_RECOVERY_MULTIPLIER:
					race.setRecoveryMultiplier((float)in.nextDouble());
					break;
				case COLUMN_STRIDE_MODIFIER:
					race.setStrideModifier((short)in.nextInt());
					break;
				case COLUMN_AVERAGE_HEIGHT:
					race.setAverageHeight((short)in.nextInt());
					break;
				case COLUMN_AVERAGE_WEIGHT:
					race.setAverageWeight((short)in.nextInt());
					break;
				case COLUMN_POUNDS_PER_INCH:
					race.setPoundsPerInch((short)in.nextInt());
					break;
				case COLUMN_SIZE_ID:
					race.setSize(new Size(in.nextInt()));
					break;
				case RaceRealmRRModSchema.TABLE_NAME:
					readRaceRealmRRMods(in, race);
					break;
				case RaceStatModSchema.TABLE_NAME:
					readRaceStatMods(in, race);
					break;
				case RaceTalentsSchema.TABLE_NAME:
					readTalentsAndFlawsTiers(in, race);
					break;
				case RaceCulturesSchema.TABLE_NAME:
					in.beginArray();
					while(in.hasNext()) {
						race.getAllowedCultures().add(new Culture(in.nextInt()));
					}
					in.endArray();
					break;
			}
		}
		in.endObject();
		return race;
	}

	private void readRaceRealmRRMods(JsonReader in, Race race) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			RealmDBO newRealmDBO = null;
			Short mods = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case RaceRealmRRModSchema.COLUMN_REALM_ID:
						newRealmDBO = new RealmDBO(in.nextInt());
						break;
					case RaceRealmRRModSchema.COLUMN_MODIFIER:
						mods = (short)in.nextInt();
						break;
				}
			}
			in.endObject();
			if(newRealmDBO != null) {
				race.getRealmResistancesModifiers().put(newRealmDBO, mods);
			}
		}
		in.endArray();
	}

	private void readRaceStatMods(JsonReader in, Race race) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Statistic newStat = null;
			Short mods = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case RaceStatModSchema.COLUMN_STAT_NAME:
						newStat = Statistic.valueOf(in.nextString());
						break;
					case RaceStatModSchema.COLUMN_MODIFIER:
						mods = (short)in.nextInt();
						break;
				}
			}
			in.endObject();
			if(newStat != null) {
				race.getStatModifiers().put(newStat, mods);
			}
		}
		in.endArray();
	}

	private void readTalentsAndFlawsTiers(JsonReader in, Race race) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			in.beginObject();
			TalentInstance talentInstance = new TalentInstance();
			Talent newTalent = null;
			short tiers = 0;
			while (in.hasNext()) {
				switch (in.nextName()) {
					case RaceTalentsSchema.COLUMN_ID:
						talentInstance.setId(in.nextInt());
						break;
					case RaceTalentsSchema.COLUMN_TALENT_ID:
						newTalent = new Talent(in.nextInt());
						break;
					case RaceTalentsSchema.COLUMN_TIERS:
						tiers = (short) in.nextInt();
						break;
					case RaceTalentParametersSchema.TABLE_NAME:
						readTalentParameterValues(in, talentInstance);
						break;
					}
			}
			talentInstance.setTalent(newTalent);
			talentInstance.setTiers(tiers);
			race.getTalentsAndFlawsList().add(talentInstance);
			in.endObject();
		}
		in.endArray();
	}

	private void readTalentParameterValues(JsonReader in, TalentInstance talentInstance) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			in.beginObject();
			Parameter parameter = null;
			Object value = null;
			while (in.hasNext()) {
				switch (in.nextName()) {
					case RaceTalentParametersSchema.COLUMN_PARAMETER_NAME:
						parameter = Parameter.valueOf(in.nextString());
						break;
					case RaceTalentParametersSchema.COLUMN_INT_VALUE:
						value = in.nextInt();
						break;
					case RaceTalentParametersSchema.COLUMN_ENUM_NAME:
						value = in.nextString();
						break;
				}
			}
			if(parameter != null) {
				talentInstance.getParameterValues().put(parameter, value);
			}
			in.endObject();
		}
		in.endArray();
	}
}
