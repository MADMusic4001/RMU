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
import com.madinnovations.rmu.data.dao.character.schemas.RaceLocomotionSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceTalentsSchema;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.common.LocomotionType;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Talent;

import java.io.IOException;
import java.util.Map;

/**
 * Json serializer and deserializer for the {@link Race} entities
 */
public class RaceSerializer extends TypeAdapter<Race> implements RaceSchema {
	@Override
	public void write(JsonWriter out, Race value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_BONUS_DEVELOPMENT_POINTS).value(value.getBonusDevelopmentPoints());
		out.name(COLUMN_AGILITY_MODIFIER).value(value.getAgilityModifier());
		out.name(COLUMN_CONSTITUTION_MODIFIER).value(value.getConstitutionModifier());
		out.name(COLUMN_EMPATHY_MODIFIER).value(value.getEmpathyModifier());
		out.name(COLUMN_INTUITION_MODIFIER).value(value.getIntuitionModifier());
		out.name(COLUMN_MEMORY_MODIFIER).value(value.getMemoryModifier());
		out.name(COLUMN_PRESENCE_MODIFIER).value(value.getPresenceModifier());
		out.name(COLUMN_QUICKNESS_MODIFIER).value(value.getQuicknessModifier());
		out.name(COLUMN_REASONING_MODIFIER).value(value.getReasoningModifier());
		out.name(COLUMN_SELF_DISCIPLINE_MODIFIER).value(value.getSelfDisciplineModifier());
		out.name(COLUMN_STRENGTH_MODIFIER).value(value.getStrengthModifier());
		out.name(COLUMN_CHANNELING_RESISTANCE_MODIFIER).value(value.getChannelingResistanceModifier());
		out.name(COLUMN_ESSENCE_RESISTANCE_MODIFIER).value(value.getEssenceResistanceModifier());
		out.name(COLUMN_MENTALISM_RESISTANCE_MODIFIER).value(value.getMentalismResistanceModifier());
		out.name(COLUMN_PHYSICAL_RESISTANCE_MODIFIER).value(value.getPhysicalResistanceModifier());
		out.name(COLUMN_ENDURANCE_MODIFIER).value(value.getEnduranceModifier());
		out.name(COLUMN_BASE_HITS).value(value.getBaseHits());
		out.name(COLUMN_RECOVERY_MULTIPLIER).value(value.getRecoveryMultiplier());
		out.name(COLUMN_STRENGTH_MODIFIER).value(value.getStrideModifier());
		out.name(COLUMN_AVERAGE_HEIGHT).value(value.getAverageHeight());
		out.name(COLUMN_AVERAGE_WEIGHT).value(value.getAverageWeight());
		out.name(COLUMN_POUNDS_PER_INCH).value(value.getPoundsPerInch());
		out.name(COLUMN_SIZE_ID).value(value.getSize().getId());

		out.name(RaceTalentsSchema.TABLE_NAME).beginArray();
		for(Map.Entry<Talent, Short> entry : value.getTalentsAndFlawsTiersMap().entrySet()) {
			out.beginObject();
			out.name(RaceTalentsSchema.COLUMN_TALENT_ID).value(entry.getKey().getId());
			out.name(RaceTalentsSchema.COLUMN_TIERS).value(entry.getValue());
			out.endObject();
		}
		out.endArray();

		out.name(RaceLocomotionSchema.TABLE_NAME).beginArray();
		for(Map.Entry<LocomotionType, Short> entry : value.getLocomotionTypeRatesMap().entrySet()) {
			out.beginObject();
			out.name(RaceLocomotionSchema.COLUMN_LOCOMOTION_TYPE_ID).value(entry.getKey().getId());
			out.name(RaceLocomotionSchema.COLUMN_RATE).value(entry.getValue());
			out.endObject();
		}
		out.endArray();

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
				case COLUMN_AGILITY_MODIFIER:
					race.setAgilityModifier((short)in.nextInt());
					break;
				case COLUMN_CONSTITUTION_MODIFIER:
					race.setConstitutionModifier((short)in.nextInt());
					break;
				case COLUMN_EMPATHY_MODIFIER:
					race.setEmpathyModifier((short)in.nextInt());
					break;
				case COLUMN_INTUITION_MODIFIER:
					race.setIntuitionModifier((short)in.nextInt());
					break;
				case COLUMN_MEMORY_MODIFIER:
					race.setMemoryModifier((short)in.nextInt());
					break;
				case COLUMN_PRESENCE_MODIFIER:
					race.setPresenceModifier((short)in.nextInt());
					break;
				case COLUMN_QUICKNESS_MODIFIER:
					race.setQuicknessModifier((short)in.nextInt());
					break;
				case COLUMN_REASONING_MODIFIER:
					race.setReasoningModifier((short)in.nextInt());
					break;
				case COLUMN_SELF_DISCIPLINE_MODIFIER:
					race.setSelfDisciplineModifier((short)in.nextInt());
					break;
				case COLUMN_STRENGTH_MODIFIER:
					race.setStrengthModifier((short)in.nextInt());
					break;
				case COLUMN_CHANNELING_RESISTANCE_MODIFIER:
					race.setChannelingResistanceModifier((short)in.nextInt());
					break;
				case COLUMN_ESSENCE_RESISTANCE_MODIFIER:
					race.setEssenceResistanceModifier((short)in.nextInt());
					break;
				case COLUMN_MENTALISM_RESISTANCE_MODIFIER:
					race.setMentalismResistanceModifier((short)in.nextInt());
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
				case RaceTalentsSchema.TABLE_NAME:
					readTalentsAndFlawsTiers(in, race);
					break;
				case RaceLocomotionSchema.TABLE_NAME:
					readLocomotions(in, race);
					break;

			}
		}
		in.endObject();
		return race;
	}

	private void readTalentsAndFlawsTiers(JsonReader in, Race race) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Talent newTalent = null;
			Short tiers = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case RaceTalentsSchema.COLUMN_TALENT_ID:
						newTalent = new Talent(in.nextInt());
						tiers = (short)in.nextInt();
						race.getTalentsAndFlawsTiersMap().put(newTalent, tiers);
				}
			}
			if(newTalent != null) {
				race.getTalentsAndFlawsTiersMap().put(newTalent, tiers);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readLocomotions(JsonReader in, Race race) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			LocomotionType newLocomotionType = null;
			Short rate = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case RaceLocomotionSchema.COLUMN_LOCOMOTION_TYPE_ID:
						newLocomotionType = new LocomotionType(in.nextInt());
						break;
					case RaceLocomotionSchema.COLUMN_RATE:
						rate = (short)in.nextInt();
						break;
				}
			}
			if(newLocomotionType != null) {
				race.getLocomotionTypeRatesMap().put(newLocomotionType, rate);
			}
		}
	}
}
