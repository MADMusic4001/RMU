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
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillCostsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterStatsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterTalentsSchema;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCost;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.common.Talent;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Json serializer and deserializer for the {@link Character} entities
 */
public class CharacterSerializer implements JsonSerializer<Character>, JsonDeserializer<Character>, CharacterSchema {

	/**
	 * Creates a new CharacterSerializer instance.
	 */
	public CharacterSerializer() {
	}

	@Override
	public JsonElement serialize(Character src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_NAME, src.getName());
		jsonObject.addProperty(COLUMN_DESCRIPTION, src.getDescription());
		jsonObject.addProperty(COLUMN_HAIR_COLOR, src.getHairColor());
		jsonObject.addProperty(COLUMN_HAIR_STYLE, src.getHairStyle());
		jsonObject.addProperty(COLUMN_EYE_COLOR, src.getEyeColor());
		jsonObject.addProperty(COLUMN_SKIN_COMPLEXION, src.getSkinComplexion());
		jsonObject.addProperty(COLUMN_FACIAL_FEATURES, src.getFacialFeatures());
		jsonObject.addProperty(COLUMN_IDENTIFYING_MARKS, src.getIdentifyingMarks());
		jsonObject.addProperty(COLUMN_CLOTHING, src.getClothing());
		jsonObject.addProperty(COLUMN_PERSONALITY, src.getPersonality());
		jsonObject.addProperty(COLUMN_MANNERISMS, src.getMannerisms());
		jsonObject.addProperty(COLUMN_HOMETOWN, src.getHometown());
		jsonObject.addProperty(COLUMN_FAMILY_INFO, src.getFamilyInfo());
		jsonObject.addProperty(COLUMN_RACE_ID, src.getRace().getId());
		jsonObject.addProperty(COLUMN_CULTURE_ID, src.getCulture().getId());
		jsonObject.addProperty(COLUMN_PROFESSION_ID, src.getProfession().getId());
		jsonObject.addProperty(COLUMN_HEIGHT, src.getHeight());
		jsonObject.addProperty(COLUMN_WEIGHT, src.getWeight());
		jsonObject.addProperty(COLUMN_STRIDE, src.getStride());
		jsonObject.addProperty(COLUMN_CURRENT_HITS, src.getCurrentHits());
		jsonObject.addProperty(COLUMN_MAX_HITS, src.getMaxHits());
		jsonObject.addProperty(COLUMN_CURRENT_DEVELOPMENT_POINTS, src.getCurrentDevelopmentPoints());
		final JsonArray skillCostsArray = new JsonArray();
		for(Map.Entry<Skill, SkillCost> entry : src.getSkillCosts().entrySet()) {
			JsonObject skillCostEntry = new JsonObject();
			skillCostEntry.addProperty(COLUMN_ID, entry.getKey().getId());
			skillCostEntry.addProperty(CharacterSkillCostsSchema.COLUMN_FIRST_COST, entry.getValue().getFirstCost());
			skillCostEntry.addProperty(CharacterSkillCostsSchema.COLUMN_ADDITIONAL_COST, entry.getValue().getAdditionalCost());
			skillCostsArray.add(skillCostEntry);
		}
		jsonObject.add(CharacterSkillCostsSchema.TABLE_NAME, skillCostsArray);

		final JsonArray skillRanksArray = new JsonArray();
		for(Map.Entry<Skill, Short> entry : src.getSkillRanks().entrySet()) {
			JsonObject skillRankEntry = new JsonObject();
			skillRankEntry.addProperty(COLUMN_ID, entry.getKey().getId());
			skillRankEntry.addProperty(CharacterSkillRanksSchema.COLUMN_RANKS, entry.getValue());
			skillRanksArray.add(skillRankEntry);
		}
		jsonObject.add(CharacterSkillRanksSchema.TABLE_NAME, skillRanksArray);

		final JsonArray talentTiersArray = new JsonArray();
		for(Map.Entry<Talent, Short> entry : src.getTalentTiers().entrySet()) {
			JsonObject talentTierEntry = new JsonObject();
			talentTierEntry.addProperty(CharacterTalentsSchema.COLUMN_TALENT_ID, entry.getKey().getId());
			talentTierEntry.addProperty(CharacterTalentsSchema.COLUMN_TIERS, entry.getValue());
			talentTiersArray.add(talentTierEntry);
		}
		jsonObject.add(CharacterTalentsSchema.TABLE_NAME, talentTiersArray);

		final JsonArray statTempsArray = new JsonArray();
		for(Map.Entry<Stat, Short> entry : src.getStatTemps().entrySet()) {
			JsonObject statTempEntry = new JsonObject();
			statTempEntry.addProperty(CharacterStatsSchema.COLUMN_STAT_ID, entry.getKey().getId());
			statTempEntry.addProperty(CharacterStatsSchema.COLUMN_CURRENT_VALUE, entry.getValue());
			statTempEntry.addProperty(CharacterStatsSchema.COLUMN_POTENTIAL_VALUE, src.getStatPotentials().get(entry.getKey()));
			statTempsArray.add(statTempEntry);
		}
		jsonObject.add(CharacterStatsSchema.TABLE_NAME, statTempsArray);

		return jsonObject;
	}

	@Override
	public Character deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Character character = new Character();
		JsonObject jsonObject = json.getAsJsonObject();
		character.setId(jsonObject.get(COLUMN_ID).getAsInt());
		character.setName(jsonObject.get(COLUMN_NAME).getAsString());
		character.setDescription(jsonObject.get(COLUMN_DESCRIPTION).getAsString());
		character.setHairColor(jsonObject.get(COLUMN_HAIR_COLOR).getAsString());
		character.setHairStyle(jsonObject.get(COLUMN_HAIR_STYLE).getAsString());
		character.setEyeColor(jsonObject.get(COLUMN_EYE_COLOR).getAsString());
		character.setSkinComplexion(jsonObject.get(COLUMN_SKIN_COMPLEXION).getAsString());
		character.setFacialFeatures(jsonObject.get(COLUMN_FACIAL_FEATURES).getAsString());
		character.setIdentifyingMarks(jsonObject.get(COLUMN_IDENTIFYING_MARKS).getAsString());
		character.setClothing(jsonObject.get(COLUMN_CLOTHING).getAsString());
		character.setPersonality(jsonObject.get(COLUMN_PERSONALITY).getAsString());
		character.setMannerisms(jsonObject.get(COLUMN_MANNERISMS).getAsString());
		character.setHometown(jsonObject.get(COLUMN_HOMETOWN).getAsString());
		character.setFamilyInfo(jsonObject.get(COLUMN_FAMILY_INFO).getAsString());
		character.setRace(new Race(jsonObject.get(COLUMN_RACE_ID).getAsInt()));
		character.setCulture(new Culture(jsonObject.get(COLUMN_CULTURE_ID).getAsInt()));
		character.setProfession(new Profession(jsonObject.get(COLUMN_PROFESSION_ID).getAsInt()));
		character.setHeight(jsonObject.get(COLUMN_HEIGHT).getAsShort());
		character.setWeight(jsonObject.get(COLUMN_WEIGHT).getAsShort());
		character.setStride(jsonObject.get(COLUMN_STRIDE).getAsShort());
		character.setCurrentHits(jsonObject.get(COLUMN_CURRENT_HITS).getAsShort());
		character.setMaxHits(jsonObject.get(COLUMN_MAX_HITS).getAsShort());
		character.setCurrentDevelopmentPoints(jsonObject.get(COLUMN_CURRENT_DEVELOPMENT_POINTS).getAsShort());

		JsonArray skillCosts = jsonObject.getAsJsonArray(CharacterSkillCostsSchema.TABLE_NAME);
		Map<Skill, SkillCost> skillCostsMap = new HashMap<>(skillCosts.size());
		for(int i = 0; i < skillCosts.size(); i ++ ) {
			final JsonObject skillCostObject = skillCosts.get(i).getAsJsonObject();
			Skill newSkill = new Skill(skillCostObject.get(COLUMN_ID).getAsInt());
			SkillCost skillCost = new SkillCost(skillCostObject.get(CharacterSkillCostsSchema.COLUMN_FIRST_COST).getAsShort(),
												skillCostObject.get(CharacterSkillCostsSchema.COLUMN_ADDITIONAL_COST)
														.getAsShort());
			skillCostsMap.put(newSkill, skillCost);
		}
		character.setSkillCosts(skillCostsMap);

		JsonArray skillRanks = jsonObject.getAsJsonArray(CharacterSkillRanksSchema.TABLE_NAME);
		Map<Skill, Short> skillRanksMap = new HashMap<>(skillRanks.size());
		for(int i = 0; i < skillRanks.size(); i ++ ) {
			final JsonObject skillRankObject = skillRanks.get(i).getAsJsonObject();
			Skill newSkill = new Skill(skillRankObject.get(CharacterSkillRanksSchema.COLUMN_SKILL_ID).getAsInt());
			Short ranks = skillRankObject.get(CharacterSkillRanksSchema.COLUMN_RANKS).getAsShort();
			skillRanksMap.put(newSkill, ranks);
		}
		character.setSkillRanks(skillRanksMap);

		JsonArray talentTiers = jsonObject.getAsJsonArray(CharacterTalentsSchema.TABLE_NAME);
		Map<Talent, Short> talentTiersMap = new HashMap<>(talentTiers.size());
		for(int i = 0; i < talentTiers.size(); i ++ ) {
			final JsonObject talentTierObject = talentTiers.get(i).getAsJsonObject();
			Talent newTalent = new Talent(talentTierObject.get(CharacterTalentsSchema.COLUMN_TALENT_ID).getAsInt());
			Short tiers = talentTierObject.get(CharacterTalentsSchema.COLUMN_TIERS).getAsShort();
			talentTiersMap.put(newTalent, tiers);
		}
		character.setTalentTiers(talentTiersMap);

		JsonArray statValues = jsonObject.getAsJsonArray(CharacterStatsSchema.TABLE_NAME);
		Map<Stat, Short> tempValuesMap = new HashMap<>(statValues.size());
		Map<Stat, Short> potentialValuesMap = new HashMap<>(statValues.size());
		for(int i = 0; i < statValues.size(); i ++ ) {
			final JsonObject statValueObject = statValues.get(i).getAsJsonObject();
			Stat newStat = new Stat(statValueObject.get(CharacterStatsSchema.COLUMN_STAT_ID).getAsInt());
			Short temp = statValueObject.get(CharacterStatsSchema.COLUMN_CURRENT_VALUE).getAsShort();
			Short potential = statValueObject.get(CharacterStatsSchema.COLUMN_POTENTIAL_VALUE).getAsShort();
			tempValuesMap.put(newStat, temp);
			potentialValuesMap.put(newStat, potential);
		}
		character.setStatTemps(tempValuesMap);
		character.setStatPotentials(potentialValuesMap);

		return character;
	}
}
