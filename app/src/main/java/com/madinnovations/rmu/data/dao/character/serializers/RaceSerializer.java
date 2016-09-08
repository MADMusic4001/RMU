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

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.madinnovations.rmu.data.dao.character.schemas.RaceLocomotionSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceTalentsSchema;
import com.madinnovations.rmu.data.dao.common.LocomotionTypeDao;
import com.madinnovations.rmu.data.dao.common.SizeDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.common.LocomotionType;
import com.madinnovations.rmu.data.entities.common.Talent;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Json serializer and deserializer for the {@link Race} entities
 */
public class RaceSerializer implements JsonSerializer<Race>, JsonDeserializer<Race>, RaceSchema {
	LocomotionTypeDao locomotionTypeDao;
	SizeDao           sizeDao;
	TalentDao         talentDao;

	/**
	 * Creates a new RaceSerializer instance.
	 */
	public RaceSerializer(LocomotionTypeDao locomotionTypeDao, SizeDao sizeDao, TalentDao talentDao) {
		this.locomotionTypeDao = locomotionTypeDao;
		this.sizeDao = sizeDao;
		this.talentDao = talentDao;
	}

	@Override
	public JsonElement serialize(Race src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_NAME, src.getName());
		jsonObject.addProperty(COLUMN_DESCRIPTION, src.getDescription());
		jsonObject.addProperty(COLUMN_BONUS_DEVELOPMENT_POINTS, src.getBonusDevelopmentPoints());
		jsonObject.addProperty(COLUMN_AGILITY_MODIFIER, src.getAgilityModifier());
		jsonObject.addProperty(COLUMN_CONSTITUTION_MODIFIER, src.getConstitutionModifier());
		jsonObject.addProperty(COLUMN_EMPATHY_MODIFIER, src.getEmpathyModifier());
		jsonObject.addProperty(COLUMN_INTUITION_MODIFIER, src.getIntuitionModifier());
		jsonObject.addProperty(COLUMN_MEMORY_MODIFIER, src.getMemoryModifier());
		jsonObject.addProperty(COLUMN_PRESENCE_MODIFIER, src.getPresenceModifier());
		jsonObject.addProperty(COLUMN_QUICKNESS_MODIFIER, src.getQuicknessModifier());
		jsonObject.addProperty(COLUMN_REASONING_MODIFIER, src.getReasoningModifier());
		jsonObject.addProperty(COLUMN_SELF_DISCIPLINE_MODIFIER, src.getSelfDisciplineModifier());
		jsonObject.addProperty(COLUMN_STRENGTH_MODIFIER, src.getStrengthModifier());
		jsonObject.addProperty(COLUMN_CHANNELING_RESISTANCE_MODIFIER, src.getChannelingResistanceModifier());
		jsonObject.addProperty(COLUMN_ESSENCE_RESISTANCE_MODIFIER, src.getEssenceResistanceModifier());
		jsonObject.addProperty(COLUMN_MENTALISM_RESISTANCE_MODIFIER, src.getMentalismResistanceModifier());
		jsonObject.addProperty(COLUMN_PHYSICAL_RESISTANCE_MODIFIER, src.getPhysicalResistanceModifier());
		jsonObject.addProperty(COLUMN_ENDURANCE_MODIFIER, src.getEnduranceModifier());
		jsonObject.addProperty(COLUMN_BASE_HITS, src.getBaseHits());
		jsonObject.addProperty(COLUMN_RECOVERY_MULTIPLIER, src.getRecoveryMultiplier());
		jsonObject.addProperty(COLUMN_STRENGTH_MODIFIER, src.getStrideModifier());
		jsonObject.addProperty(COLUMN_AVERAGE_HEIGHT, src.getAverageHeight());
		jsonObject.addProperty(COLUMN_AVERAGE_WEIGHT, src.getAverageWeight());
		jsonObject.addProperty(COLUMN_POUNDS_PER_INCH, src.getPoundsPerInch());
		jsonObject.addProperty(COLUMN_SIZE_ID, src.getSize().getId());

		final JsonArray talentShortMap = new JsonArray();
		for(Map.Entry<Talent, Short> entry : src.getTalentsAndFlawsTiersMap().entrySet()) {
			JsonObject talentTierEntry = new JsonObject();
			talentTierEntry.addProperty(RaceTalentsSchema.COLUMN_TALENT_ID, entry.getKey().getId());
			talentTierEntry.addProperty(RaceTalentsSchema.COLUMN_TIERS, entry.getValue());
			talentShortMap.add(talentTierEntry);
		}
		jsonObject.add(RaceTalentsSchema.TABLE_NAME, talentShortMap);

		final JsonArray locomotionTypeShortMap = new JsonArray();
		for(Map.Entry<LocomotionType, Short> entry : src.getLocomotionTypeRatesMap().entrySet()) {
			JsonObject locomotionTypeRateEntry = new JsonObject();
			locomotionTypeRateEntry.addProperty(RaceLocomotionSchema.COLUMN_LOCOMOTION_TYPE_ID, entry.getKey().getId());
			locomotionTypeRateEntry.addProperty(RaceLocomotionSchema.COLUMN_RATE, entry.getValue());
			talentShortMap.add(locomotionTypeRateEntry);
		}
		jsonObject.add(RaceLocomotionSchema.TABLE_NAME, locomotionTypeShortMap);

		return jsonObject;
	}

	@Override
	public Race deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Race race = new Race();
		JsonObject jsonObject = json.getAsJsonObject();
		race.setId(jsonObject.get(COLUMN_ID).getAsInt());
		race.setName(jsonObject.get(COLUMN_NAME).getAsString());
		race.setDescription(jsonObject.get(COLUMN_DESCRIPTION).getAsString());
		race.setBonusDevelopmentPoints(jsonObject.get(COLUMN_BONUS_DEVELOPMENT_POINTS).getAsShort());
		race.setAgilityModifier(jsonObject.get(COLUMN_AGILITY_MODIFIER).getAsShort());
		race.setConstitutionModifier(jsonObject.get(COLUMN_CONSTITUTION_MODIFIER).getAsShort());
		race.setEmpathyModifier(jsonObject.get(COLUMN_EMPATHY_MODIFIER).getAsShort());
		race.setIntuitionModifier(jsonObject.get(COLUMN_INTUITION_MODIFIER).getAsShort());
		race.setMemoryModifier(jsonObject.get(COLUMN_MEMORY_MODIFIER).getAsShort());
		race.setPresenceModifier(jsonObject.get(COLUMN_PRESENCE_MODIFIER).getAsShort());
		race.setQuicknessModifier(jsonObject.get(COLUMN_QUICKNESS_MODIFIER).getAsShort());
		race.setReasoningModifier(jsonObject.get(COLUMN_REASONING_MODIFIER).getAsShort());
		race.setSelfDisciplineModifier(jsonObject.get(COLUMN_SELF_DISCIPLINE_MODIFIER).getAsShort());
		race.setStrengthModifier(jsonObject.get(COLUMN_STRENGTH_MODIFIER).getAsShort());
		race.setChannelingResistanceModifier(jsonObject.get(COLUMN_CHANNELING_RESISTANCE_MODIFIER).getAsShort());
		race.setEssenceResistanceModifier(jsonObject.get(COLUMN_ESSENCE_RESISTANCE_MODIFIER).getAsShort());
		race.setMentalismResistanceModifier(jsonObject.get(COLUMN_MENTALISM_RESISTANCE_MODIFIER).getAsShort());
		race.setPhysicalResistanceModifier(jsonObject.get(COLUMN_PHYSICAL_RESISTANCE_MODIFIER).getAsShort());
		race.setEnduranceModifier(jsonObject.get(COLUMN_MENTALISM_RESISTANCE_MODIFIER).getAsShort());
		race.setBaseHits(jsonObject.get(COLUMN_BASE_HITS).getAsShort());
		race.setRecoveryMultiplier(jsonObject.get(COLUMN_RECOVERY_MULTIPLIER).getAsFloat());
		race.setStrideModifier(jsonObject.get(COLUMN_STRIDE_MODIFIER).getAsShort());
		race.setAverageHeight(jsonObject.get(COLUMN_AVERAGE_HEIGHT).getAsShort());
		race.setAverageWeight(jsonObject.get(COLUMN_AVERAGE_WEIGHT).getAsShort());
		race.setPoundsPerInch(jsonObject.get(COLUMN_POUNDS_PER_INCH).getAsShort());
		race.setSize(sizeDao.getById(jsonObject.get(COLUMN_SIZE_ID).getAsInt()));

		JsonArray talentTiers = jsonObject.getAsJsonArray(RaceTalentsSchema.TABLE_NAME);
		for(JsonElement talentTierElement : talentTiers) {
			final JsonObject talentTierObject = talentTierElement.getAsJsonObject();
			Talent newTalent = talentDao.getById(talentTierObject.get(RaceTalentsSchema.COLUMN_TALENT_ID).getAsInt());
			Short tiers = talentTierObject.get(RaceTalentsSchema.COLUMN_TIERS).getAsShort();
			race.getTalentsAndFlawsTiersMap().put(newTalent, tiers);
		}

		JsonArray locomotionMap = jsonObject.getAsJsonArray(RaceLocomotionSchema.TABLE_NAME);
		for(JsonElement locomotionElement : locomotionMap) {
			final JsonObject locomotionObject = locomotionElement.getAsJsonObject();
			LocomotionType newLocomotionType = locomotionTypeDao.getById(
					locomotionObject.get(RaceLocomotionSchema.COLUMN_LOCOMOTION_TYPE_ID).getAsInt());
			Short rate = locomotionObject.get(RaceLocomotionSchema.COLUMN_RATE).getAsShort();
			race.getLocomotionTypeRatesMap().put(newLocomotionType, rate);
		}

		return race;
	}
}
