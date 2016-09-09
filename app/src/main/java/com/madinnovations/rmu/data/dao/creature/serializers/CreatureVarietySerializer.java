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
package com.madinnovations.rmu.data.dao.creature.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.madinnovations.rmu.data.dao.combat.AttackDao;
import com.madinnovations.rmu.data.dao.combat.CriticalCodeDao;
import com.madinnovations.rmu.data.dao.common.SizeDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.creature.CreatureTypeDao;
import com.madinnovations.rmu.data.dao.creature.OutlookDao;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureVarietySchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyAttacksSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyCriticalCodesSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietySkillsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyStatsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyTalentTiersSchema;
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.CriticalCode;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Json serializer and deserializer for the {@link CreatureVariety} entities
 */
public class CreatureVarietySerializer implements JsonSerializer<CreatureVariety>, JsonDeserializer<CreatureVariety>,
		CreatureVarietySchema {
	AttackDao       attackDao;
	CreatureTypeDao creatureTypeDao;
	CriticalCodeDao criticalCodeDao;
	OutlookDao      outlookDao;
	RealmDao        realmDao;
	SizeDao         sizeDao;
	SkillDao        skillDao;
	StatDao         statDao;
	TalentDao       talentDao;

	/**
	 * Creates a new CharacterSerializer instance.
	 */
	@Inject
	public CreatureVarietySerializer(AttackDao attackDao, CreatureTypeDao creatureTypeDao, CriticalCodeDao criticalCodeDao,
									 OutlookDao outlookDao, RealmDao realmDao, SizeDao sizeDao, SkillDao skillDao,
									 StatDao statDao, TalentDao talentDao) {
		this.attackDao = attackDao;
		this.creatureTypeDao = creatureTypeDao;
		this.criticalCodeDao = criticalCodeDao;
		this.outlookDao = outlookDao;
		this.realmDao = realmDao;
		this.sizeDao = sizeDao;
		this.skillDao = skillDao;
		this.statDao = statDao;
		this.talentDao = talentDao;
	}

	@Override
	public JsonElement serialize(CreatureVariety src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_NAME, src.getName());
		jsonObject.addProperty(COLUMN_DESCRIPTION, src.getDescription());
		jsonObject.addProperty(COLUMN_TYPICAL_LEVEL, src.getTypicalLevel());
		jsonObject.addProperty(COLUMN_LEVEL_SPREAD, src.getLevelSpread());
		jsonObject.addProperty(COLUMN_HEIGHT, src.getHeight());
		jsonObject.addProperty(COLUMN_LENGTH, src.getLength());
		jsonObject.addProperty(COLUMN_WEIGHT, src.getWeight());
		jsonObject.addProperty(COLUMN_HEALING_RATE, src.getHealingRate());
		jsonObject.addProperty(COLUMN_BASE_HITS, src.getBaseHits());
		jsonObject.addProperty(COLUMN_BASE_ENDURANCE, src.getBaseEndurance());
		jsonObject.addProperty(COLUMN_ARMOR_TYPE, src.getArmorType());
		jsonObject.addProperty(COLUMN_BASE_MOVEMENT_RATE, src.getBaseMovementRate());
		jsonObject.addProperty(COLUMN_BASE_CHANNELING_RR, src.getBaseChannellingRR());
		jsonObject.addProperty(COLUMN_BASE_ESSENCE_RR, src.getBaseEssenceRR());
		jsonObject.addProperty(COLUMN_BASE_MENTALISM_RR, src.getBaseMentalismRR());
		jsonObject.addProperty(COLUMN_BASE_PHYSICAL_RR, src.getBasePhysicalRR());
		jsonObject.addProperty(COLUMN_BASE_FEAR_RR, src.getBaseFearRR());
		jsonObject.addProperty(COLUMN_BASE_STRIDE, src.getBaseStride());
		jsonObject.addProperty(COLUMN_LEFTOVER_DP, src.getLeftoverDP());
		jsonObject.addProperty(COLUMN_ATTACK_SEQUENCE, src.getAttackSequence());
		jsonObject.addProperty(COLUMN_TYPE_ID, src.getType().getId());
		jsonObject.addProperty(COLUMN_SIZE_ID, src.getSize().getId());
		jsonObject.addProperty(COLUMN_REALM1_ID, src.getRealm1().getId());
		jsonObject.addProperty(COLUMN_REALM2_ID, src.getRealm2().getId());
		jsonObject.addProperty(COLUMN_OUTLOOK_ID, src.getOutlook().getId());

		JsonArray jsonArray = new JsonArray();
		for(Map.Entry<Stat, Short> entry : src.getRacialStatBonuses().entrySet()) {
			JsonObject arrayEntry = new JsonObject();
			arrayEntry.addProperty(VarietyStatsSchema.COLUMN_STAT_ID, entry.getKey().getId());
			arrayEntry.addProperty(VarietyStatsSchema.COLUMN_BONUS, entry.getValue());
			jsonArray.add(arrayEntry);
		}
		jsonObject.add(VarietyStatsSchema.TABLE_NAME, jsonArray);

		jsonArray = new JsonArray();
		for(CriticalCode criticalCode : src.getCriticalCodes()) {
			jsonArray.add(criticalCode.getId());
		}
		jsonObject.add(VarietyCriticalCodesSchema.TABLE_NAME, jsonArray);

		jsonArray = new JsonArray();
		for(Map.Entry<Talent, Short> entry : src.getTalentTiersMap().entrySet()) {
			JsonObject arrayEntry = new JsonObject();
			arrayEntry.addProperty(VarietyTalentTiersSchema.COLUMN_TALENT_ID, entry.getKey().getId());
			arrayEntry.addProperty(VarietyTalentTiersSchema.COLUMN_TIERS, entry.getValue());
			jsonArray.add(arrayEntry);
		}
		jsonObject.add(VarietyTalentTiersSchema.TABLE_NAME, jsonArray);

		jsonArray = new JsonArray();
		for(Map.Entry<Attack, Short> entry : src.getAttackBonusesMap().entrySet()) {
			JsonObject arrayEntry = new JsonObject();
			arrayEntry.addProperty(VarietyAttacksSchema.COLUMN_ATTACK_ID, entry.getKey().getId());
			arrayEntry.addProperty(VarietyAttacksSchema.COLUMN_ATTACK_BONUS, entry.getValue());
			jsonArray.add(arrayEntry);
		}
		jsonObject.add(VarietyAttacksSchema.TABLE_NAME, jsonArray);

		jsonArray = new JsonArray();
		for(Map.Entry<Skill, Short> entry : src.getSkillBonusesMap().entrySet()) {
			JsonObject arrayEntry = new JsonObject();
			arrayEntry.addProperty(VarietySkillsSchema.COLUMN_SKILL_ID, entry.getKey().getId());
			arrayEntry.addProperty(VarietySkillsSchema.COLUMN_SKILL_BONUS, entry.getValue());
			jsonArray.add(arrayEntry);
		}
		jsonObject.add(VarietySkillsSchema.TABLE_NAME, jsonArray);

		return jsonObject;
	}

	@Override
	public CreatureVariety deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	throws JsonParseException {
		CreatureVariety creatureVariety = new CreatureVariety();
		JsonObject jsonObject = json.getAsJsonObject();
		creatureVariety.setId(jsonObject.get(COLUMN_ID).getAsInt());
		creatureVariety.setName(jsonObject.get(COLUMN_NAME).getAsString());
		creatureVariety.setDescription(jsonObject.get(COLUMN_DESCRIPTION).getAsString());
		creatureVariety.setTypicalLevel(jsonObject.get(COLUMN_TYPICAL_LEVEL).getAsShort());
		creatureVariety.setLevelSpread(jsonObject.get(COLUMN_LEVEL_SPREAD).getAsCharacter());
		creatureVariety.setHeight(jsonObject.get(COLUMN_HEIGHT).getAsShort());
		creatureVariety.setLength(jsonObject.get(COLUMN_LENGTH).getAsShort());
		creatureVariety.setWeight(jsonObject.get(COLUMN_WEIGHT).getAsShort());
		creatureVariety.setHealingRate(jsonObject.get(COLUMN_HEALING_RATE).getAsShort());
		creatureVariety.setBaseHits(jsonObject.get(COLUMN_BASE_HITS).getAsShort());
		creatureVariety.setBaseEndurance(jsonObject.get(COLUMN_BASE_ENDURANCE).getAsShort());
		creatureVariety.setArmorType(jsonObject.get(COLUMN_ARMOR_TYPE).getAsShort());
		creatureVariety.setBaseMovementRate(jsonObject.get(COLUMN_BASE_MOVEMENT_RATE).getAsShort());
		creatureVariety.setBaseChannellingRR(jsonObject.get(COLUMN_BASE_CHANNELING_RR).getAsShort());
		creatureVariety.setBaseEssenceRR(jsonObject.get(COLUMN_BASE_ESSENCE_RR).getAsShort());
		creatureVariety.setBaseMentalismRR(jsonObject.get(COLUMN_BASE_MENTALISM_RR).getAsShort());
		creatureVariety.setBasePhysicalRR(jsonObject.get(COLUMN_BASE_PHYSICAL_RR).getAsShort());
		creatureVariety.setBaseFearRR(jsonObject.get(COLUMN_BASE_FEAR_RR).getAsShort());
		creatureVariety.setBaseStride(jsonObject.get(COLUMN_BASE_STRIDE).getAsShort());
		creatureVariety.setLeftoverDP(jsonObject.get(COLUMN_LEFTOVER_DP).getAsShort());
		creatureVariety.setAttackSequence(jsonObject.get(COLUMN_ATTACK_SEQUENCE).getAsString());
		creatureVariety.setType(creatureTypeDao.getById(jsonObject.get(COLUMN_TYPE_ID).getAsInt()));
		creatureVariety.setSize(sizeDao.getById(jsonObject.get(COLUMN_SIZE_ID).getAsInt()));
		creatureVariety.setRealm1(realmDao.getById(jsonObject.get(COLUMN_REALM1_ID).getAsInt()));
		creatureVariety.setRealm2(realmDao.getById(jsonObject.get(COLUMN_REALM2_ID).getAsInt()));
		creatureVariety.setOutlook(outlookDao.getById(jsonObject.get(COLUMN_OUTLOOK_ID).getAsInt()));

		JsonArray jsonArray = jsonObject.getAsJsonArray(VarietyStatsSchema.TABLE_NAME);
		Map<Stat, Short> statsMap = new HashMap<>(jsonArray.size());
		for(JsonElement element : jsonArray) {
			final JsonObject arrayEntry = element.getAsJsonObject();
			Stat newStat = statDao.getById(arrayEntry.get(VarietyStatsSchema.COLUMN_STAT_ID).getAsInt());
			statsMap.put(newStat, arrayEntry.get(VarietyStatsSchema.COLUMN_BONUS).getAsShort());
		}
		creatureVariety.setRacialStatBonuses(statsMap);

		jsonArray = jsonObject.getAsJsonArray(VarietyCriticalCodesSchema.TABLE_NAME);
		List<CriticalCode> criticalCodeList = new ArrayList<>(jsonArray.size());
		for(JsonElement element : jsonArray) {
			criticalCodeList.add(criticalCodeDao.getById(element.getAsInt()));
		}
		creatureVariety.setCriticalCodes(criticalCodeList);

		jsonArray = jsonObject.getAsJsonArray(VarietyTalentTiersSchema.TABLE_NAME);
		Map<Talent, Short> talentsMap = new HashMap<>(jsonArray.size());
		for(JsonElement element : jsonArray) {
			final JsonObject arrayEntry = element.getAsJsonObject();
			Talent newTalent = talentDao.getById(arrayEntry.get(VarietyTalentTiersSchema.COLUMN_TALENT_ID).getAsInt());
			talentsMap.put(newTalent, arrayEntry.get(VarietyTalentTiersSchema.COLUMN_TIERS).getAsShort());
		}
		creatureVariety.setTalentTiersMap(talentsMap);

		jsonArray = jsonObject.getAsJsonArray(VarietyAttacksSchema.TABLE_NAME);
		Map<Attack, Short> attacksMap = new HashMap<>(jsonArray.size());
		for(JsonElement element : jsonArray) {
			final JsonObject arrayEntry = element.getAsJsonObject();
			Attack newAttack = attackDao.getById(arrayEntry.get(VarietyAttacksSchema.COLUMN_ATTACK_ID).getAsInt());
			attacksMap.put(newAttack, arrayEntry.get(VarietyAttacksSchema.COLUMN_ATTACK_BONUS).getAsShort());
		}
		creatureVariety.setAttackBonusesMap(attacksMap);

		jsonArray = jsonObject.getAsJsonArray(VarietySkillsSchema.TABLE_NAME);
		Map<Skill, Short> skillsMap = new HashMap<>(jsonArray.size());
		for(JsonElement element : jsonArray) {
			final JsonObject arrayEntry = element.getAsJsonObject();
			Skill newSkill = skillDao.getById(arrayEntry.get(VarietySkillsSchema.COLUMN_SKILL_ID).getAsInt());
			skillsMap.put(newSkill, arrayEntry.get(VarietySkillsSchema.COLUMN_SKILL_BONUS).getAsShort());
		}
		creatureVariety.setSkillBonusesMap(skillsMap);

		return creatureVariety;
	}
}
