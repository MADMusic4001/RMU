/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.dao.character.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterCurrentLevelSkillRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterCurrentLevelSpecializationRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterCurrentLevelSpellListRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterKnacksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterProfessionSkillsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterPurchasedCultureRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillCostsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSpecializationRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSpellListRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterStatsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterTalentParametersSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterTalentsSchema;
import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.common.DevelopmentCostGroup;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.data.entities.spells.SpellList;

import java.io.IOException;
import java.util.Map;

/**
 * Json serializer and deserializer for the {@link Character} entities
 */
public class CharacterSerializer extends TypeAdapter<Character> implements CharacterSchema {
	@SuppressWarnings("unused")
	private static final String TAG = "CharacterSerializer";

	@Override
	public void write(JsonWriter out, Character value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_CAMPAIGN_ID).value(value.getCampaign().getId());
		out.name(COLUMN_CURRENT_LEVEL).value(value.getCurrentLevel());
		out.name(COLUMN_EXPERIENCE_POINTS).value(value.getExperiencePoints());
		out.name(COLUMN_FIRST_NAME).value(value.getFirstName());
		out.name(COLUMN_LAST_NAME).value(value.getLastName());
		out.name(COLUMN_KNOWN_AS).value(value.getKnownAs());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_HAIR_COLOR).value(value.getHairColor());
		out.name(COLUMN_HAIR_STYLE).value(value.getHairStyle());
		out.name(COLUMN_EYE_COLOR).value(value.getEyeColor());
		out.name(COLUMN_SKIN_COMPLEXION).value(value.getSkinComplexion());
		out.name(COLUMN_FACIAL_FEATURES).value(value.getFacialFeatures());
		out.name(COLUMN_IDENTIFYING_MARKS).value(value.getIdentifyingMarks());
		out.name(COLUMN_PERSONALITY).value(value.getPersonality());
		out.name(COLUMN_MANNERISMS).value(value.getMannerisms());
		out.name(COLUMN_HOMETOWN).value(value.getHometown());
		out.name(COLUMN_FAMILY_INFO).value(value.getFamilyInfo());
		out.name(COLUMN_RACE_ID).value(value.getRace().getId());
		out.name(COLUMN_CULTURE_ID).value(value.getCulture().getId());
		out.name(COLUMN_PROFESSION_ID).value(value.getProfession().getId());
		out.name(COLUMN_REALM).value(value.getRealm().name());
		if(value.getRealm2() != null) {
			out.name(COLUMN_REALM2).value(value.getRealm2().name());
		}
		if(value.getRealm3() != null) {
			out.name(COLUMN_REALM3).value(value.getRealm3().name());
		}
		out.name(COLUMN_HEIGHT).value(value.getHeight());
		out.name(COLUMN_WEIGHT).value(value.getWeight());
		out.name(COLUMN_CURRENT_HP_LOSS).value(value.getHitPointLoss());
		out.name(COLUMN_CURRENT_DEVELOPMENT_POINTS).value(value.getCurrentDevelopmentPoints());
		out.name(COLUMN_CURRENT_FATIGUE).value(value.getFatigue());
		out.name(COLUMN_CURRENT_PP_LOSS).value(value.getPowerPointLoss());
		out.name(COLUMN_BASE_MOVEMENT_RATE).value(value.getBaseMovementRate());
		out.name(COLUMN_STAT_INCREASES).value(value.getStatIncreases());
		if(value.getMainHandItem() != null) {
			out.name(COLUMN_MAIN_HAND_ITEM_ID).value(value.getMainHandItem().getId());
		}
		if(value.getOffhandItem() != null) {
			out.name(COLUMN_OFFHAND_ITEM_ID).value(value.getOffhandItem().getId());
		}
		if(value.getShirtItem() != null) {
			out.name(COLUMN_SHIRT_ITEM_ID).value(value.getShirtItem().getId());
		}
		if(value.getPantsItem() != null) {
			out.name(COLUMN_PANTS_ITEM_ID).value(value.getPantsItem().getId());
		}
		if(value.getHeadItem() != null) {
			out.name(COLUMN_HEAD_ITEM_ID).value(value.getHeadItem().getId());
		}
		if(value.getChestItem() != null) {
			out.name(COLUMN_CHEST_ITEM_ID).value(value.getChestItem().getId());
		}
		if(value.getArmsItem() != null) {
			out.name(COLUMN_ARMS_ITEM_ID).value(value.getArmsItem().getId());
		}
		if(value.getLegsItem() != null) {
			out.name(COLUMN_LEGS_ITEM_ID).value(value.getLegsItem().getId());
		}
		if(value.getFeetItem() != null) {
			out.name(COLUMN_FEET_ITEM_ID).value(value.getFeetItem().getId());
		}
		if(value.getBackItem() != null) {
			out.name(COLUMN_BACK_ITEM_ID).value(value.getBackItem().getId());
		}
		if(value.getBackpackItem() != null) {
			out.name(COLUMN_BACKPACK_ITEM_ID).value(value.getBackpackItem().getId());
		}

		out.name(CharacterSkillCostsSchema.TABLE_NAME).beginArray();
		for (Map.Entry<Skill, DevelopmentCostGroup> entry : value.getSkillCosts().entrySet()) {
			out.beginObject();
			out.name(CharacterSkillCostsSchema.COLUMN_SKILL_ID).value(entry.getKey().getId());
			if (entry.getValue() != null && !DevelopmentCostGroup.NONE.equals(entry.getValue())) {
				out.name(CharacterSkillCostsSchema.COLUMN_COST_GROUP_NAME).value(entry.getValue().name());
			}
			out.endObject();
		}
		out.endArray();

		out.name(CharacterSkillRanksSchema.TABLE_NAME).beginArray();
		for (Map.Entry<Skill, Short> entry : value.getSkillRanks().entrySet()) {
			out.beginObject();
			out.name(CharacterSkillRanksSchema.COLUMN_SKILL_ID).value(entry.getKey().getId());
			out.name(CharacterSkillRanksSchema.COLUMN_RANKS).value(entry.getValue());
			out.endObject();
		}
		out.endArray();

		out.name(CharacterSpecializationRanksSchema.TABLE_NAME).beginArray();
		for (Map.Entry<Specialization, Short> entry : value.getSpecializationRanks().entrySet()) {
			out.beginObject();
			out.name(CharacterSpecializationRanksSchema.COLUMN_SPECIALIZATION_ID).value(entry.getKey().getId());
			out.name(CharacterSpecializationRanksSchema.COLUMN_RANKS).value(entry.getValue());
			out.endObject();
		}
		out.endArray();

		out.name(CharacterSpellListRanksSchema.TABLE_NAME).beginArray();
		for (Map.Entry<SpellList, Short> entry : value.getSpellListRanks().entrySet()) {
			out.beginObject();
			out.name(CharacterSpellListRanksSchema.COLUMN_SPELL_LIST_ID).value(entry.getKey().getId());
			out.name(CharacterSpellListRanksSchema.COLUMN_RANKS).value(entry.getValue());
			out.endObject();
		}
		out.endArray();

		out.name(CharacterTalentsSchema.TABLE_NAME).beginArray();
		for (TalentInstance talentInstance : value.getTalentInstances()) {
			out.beginObject();
			out.name(CharacterTalentsSchema.COLUMN_ID).value(talentInstance.getId());
			out.name(CharacterTalentsSchema.COLUMN_TALENT_ID).value(talentInstance.getTalent().getId());
			out.name(CharacterTalentsSchema.COLUMN_TIERS).value(talentInstance.getTiers());
			if (!talentInstance.getParameterValues().isEmpty()) {
				out.name(CharacterTalentParametersSchema.TABLE_NAME).beginArray();
				for (Map.Entry<Parameter, Object> paramEntry : talentInstance.getParameterValues().entrySet()) {
					out.beginObject();
					out.name(CharacterTalentParametersSchema.COLUMN_PARAMETER_NAME).value(paramEntry.getKey().name());
					if (paramEntry.getValue() != null) {
						if (paramEntry.getValue() instanceof Integer) {
							out.name(CharacterTalentParametersSchema.COLUMN_INT_VALUE).value((Integer) paramEntry.getValue());
						}
						else {
							out.name(CharacterTalentParametersSchema.COLUMN_ENUM_NAME).value((String) paramEntry.getValue());
						}
					}
					out.endObject();
				}
				out.endArray();
			}
			out.endObject();
		}
		out.endArray();

		out.name(CharacterStatsSchema.TABLE_NAME).beginArray();
		for (Map.Entry<Statistic, Short> entry : value.getStatTemps().entrySet()) {
			out.beginObject();
			out.name(CharacterStatsSchema.COLUMN_STAT_NAME).value(entry.getKey().name());
			out.name(CharacterStatsSchema.COLUMN_CURRENT_VALUE).value(entry.getValue());
			out.name(CharacterStatsSchema.COLUMN_POTENTIAL_VALUE).value(value.getStatPotentials().get(entry.getKey()));
			out.endObject();
		}
		out.endArray();

		out.name(CharacterCurrentLevelSkillRanksSchema.TABLE_NAME).beginArray();
		for (Map.Entry<Skill, Short> entry : value.getCurrentLevelSkillRanks().entrySet()) {
			out.beginObject();
			out.name(CharacterCurrentLevelSkillRanksSchema.COLUMN_SKILL_ID).value(entry.getKey().getId());
			out.name(CharacterCurrentLevelSkillRanksSchema.COLUMN_RANKS).value(entry.getValue());
			out.endObject();
		}
		out.endArray();

		out.name(CharacterCurrentLevelSpecializationRanksSchema.TABLE_NAME).beginArray();
		for (Map.Entry<Specialization, Short> entry : value.getCurrentLevelSpecializationRanks().entrySet()) {
			out.beginObject();
			out.name(CharacterCurrentLevelSpecializationRanksSchema.COLUMN_SPECIALIZATION_ID).value(entry.getKey().getId());
			out.name(CharacterCurrentLevelSpecializationRanksSchema.COLUMN_RANKS).value(entry.getValue());
			out.endObject();
		}
		out.endArray();

		out.name(CharacterCurrentLevelSpellListRanksSchema.TABLE_NAME).beginArray();
		for (Map.Entry<SpellList, Short> entry : value.getCurrentLevelSpellListRanks().entrySet()) {
			out.beginObject();
			out.name(CharacterCurrentLevelSpellListRanksSchema.COLUMN_SPELL_LIST_ID).value(entry.getKey().getId());
			out.name(CharacterCurrentLevelSpellListRanksSchema.COLUMN_RANKS).value(entry.getValue());
			out.endObject();
		}
		out.endArray();

		out.name(CharacterPurchasedCultureRanksSchema.TABLE_NAME).beginArray();
		for (Map.Entry<DatabaseObject, Short> entry : value.getPurchasedCultureRanks().entrySet()) {
			out.beginObject();
			if (entry.getKey() instanceof Skill) {
				out.name(CharacterPurchasedCultureRanksSchema.COLUMN_SKILL_ID).value(entry.getKey().getId());
			}
			else {
				out.name(CharacterPurchasedCultureRanksSchema.COLUMN_SPECIALIZATION_ID).value(entry.getKey().getId());
			}
			out.name(CharacterPurchasedCultureRanksSchema.COLUMN_RANKS).value(entry.getValue());
			out.endObject();
		}
		out.endArray();

		out.name(CharacterProfessionSkillsSchema.TABLE_NAME).beginArray();
		for (DatabaseObject databaseObject : value.getProfessionSkills()) {
			out.beginObject();
			if (databaseObject instanceof Skill) {
				out.name(CharacterProfessionSkillsSchema.COLUMN_SKILL_ID).value(databaseObject.getId());
			}
			else if(databaseObject instanceof Specialization){
				out.name(CharacterProfessionSkillsSchema.COLUMN_SPECIALIZATION_ID).value(databaseObject.getId());
			}
			else if(databaseObject instanceof SpellList){
				out.name(CharacterProfessionSkillsSchema.COLUMN_SPELL_LIST_ID).value(databaseObject.getId());
			}
			out.endObject();
		}
		out.endArray();

		out.name(CharacterKnacksSchema.TABLE_NAME).beginArray();
		for (DatabaseObject databaseObject : value.getKnacks()) {
			out.beginObject();
			if (databaseObject instanceof Skill) {
				out.name(CharacterKnacksSchema.COLUMN_SKILL_ID).value(databaseObject.getId());
			}
			else if(databaseObject instanceof Specialization) {
				out.name(CharacterKnacksSchema.COLUMN_SPECIALIZATION_ID).value(databaseObject.getId());
			}
			else if(databaseObject instanceof SpellList) {
				out.name(CharacterKnacksSchema.COLUMN_SPELL_LIST_ID).value(databaseObject.getId());
			}
			out.endObject();
		}
		out.endArray();

		out.endObject();
		out.flush();
	}

	@Override
	public Character read(JsonReader in) throws IOException {
		Character character = new Character();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					character.setId(in.nextInt());
					break;
				case COLUMN_CAMPAIGN_ID:
					character.setCampaign(new Campaign(in.nextInt()));
					break;
				case COLUMN_CURRENT_LEVEL:
					character.setCurrentLevel((short) in.nextInt());
					break;
				case COLUMN_EXPERIENCE_POINTS:
					character.setExperiencePoints(in.nextInt());
					break;
				case COLUMN_FIRST_NAME:
					character.setFirstName(in.nextString());
					break;
				case COLUMN_LAST_NAME:
					character.setLastName(in.nextString());
					break;
				case COLUMN_KNOWN_AS:
					character.setKnownAs(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					character.setDescription(in.nextString());
					break;
				case COLUMN_HAIR_COLOR:
					character.setHairColor(in.nextString());
					break;
				case COLUMN_HAIR_STYLE:
					character.setHairStyle(in.nextString());
					break;
				case COLUMN_EYE_COLOR:
					character.setEyeColor(in.nextString());
					break;
				case COLUMN_SKIN_COMPLEXION:
					character.setSkinComplexion(in.nextString());
					break;
				case COLUMN_FACIAL_FEATURES:
					character.setFacialFeatures(in.nextString());
					break;
				case COLUMN_IDENTIFYING_MARKS:
					character.setIdentifyingMarks(in.nextString());
					break;
				case COLUMN_PERSONALITY:
					character.setPersonality(in.nextString());
					break;
				case COLUMN_MANNERISMS:
					character.setMannerisms(in.nextString());
					break;
				case COLUMN_HOMETOWN:
					character.setHometown(in.nextString());
					break;
				case COLUMN_FAMILY_INFO:
					character.setFamilyInfo(in.nextString());
					break;
				case COLUMN_RACE_ID:
					character.setRace(new Race(in.nextInt()));
					break;
				case COLUMN_CULTURE_ID:
					character.setCulture(new Culture(in.nextInt()));
					break;
				case COLUMN_PROFESSION_ID:
					character.setProfession(new Profession(in.nextInt()));
					break;
				case "realmId":
					int realmId = in.nextInt();
					switch (realmId) {
						case 1:
							character.setRealm(Realm.CHANNELING);
							break;
						case 2:
							character.setRealm(Realm.ESSENCE);
							break;
						case 3:
							character.setRealm(Realm.MENTALISM);
							break;
					}
					break;
				case COLUMN_REALM:
					character.setRealm(Realm.valueOf(in.nextString()));
					break;
				case "realm2Id":
					realmId = in.nextInt();
					switch (realmId) {
						case 1:
							character.setRealm2(Realm.CHANNELING);
							break;
						case 2:
							character.setRealm2(Realm.ESSENCE);
							break;
						case 3:
							character.setRealm2(Realm.MENTALISM);
							break;
					}
					break;
				case COLUMN_REALM2:
					character.setRealm2(Realm.valueOf(in.nextString()));
					break;
				case COLUMN_REALM3:
					character.setRealm3(Realm.valueOf(in.nextString()));
					break;
				case COLUMN_HEIGHT:
					character.setHeight((short) in.nextInt());
					break;
				case COLUMN_WEIGHT:
					character.setWeight((short) in.nextInt());
					break;
				case COLUMN_CURRENT_HP_LOSS:
					character.setHitPointLoss(in.nextInt());
					break;
				case COLUMN_CURRENT_DEVELOPMENT_POINTS:
					character.setCurrentDevelopmentPoints((short) in.nextInt());
					break;
				case COLUMN_CURRENT_FATIGUE:
					character.setFatigue((short) in.nextInt());
					break;
				case COLUMN_CURRENT_PP_LOSS:
					character.setPowerPointLoss((short) in.nextInt());
					break;
				case COLUMN_BASE_MOVEMENT_RATE:
					character.setBaseMovementRate((short)in.nextInt());
					break;
				case COLUMN_STAT_INCREASES:
					character.setStatIncreases((short) in.nextInt());
					break;
				case COLUMN_MAIN_HAND_ITEM_ID:
					character.setMainHandItem(new Item(in.nextInt()));
					break;
				case COLUMN_OFFHAND_ITEM_ID:
					character.setOffhandItem(new Item(in.nextInt()));
					break;
				case COLUMN_SHIRT_ITEM_ID:
					character.setShirtItem(new Item(in.nextInt()));
					break;
				case COLUMN_PANTS_ITEM_ID:
					character.setPantsItem(new Item(in.nextInt()));
					break;
				case COLUMN_HEAD_ITEM_ID:
					character.setHeadItem(new Item(in.nextInt()));
					break;
				case COLUMN_CHEST_ITEM_ID:
					character.setChestItem(new Item(in.nextInt()));
					break;
				case COLUMN_ARMS_ITEM_ID:
					character.setArmsItem(new Item(in.nextInt()));
					break;
				case COLUMN_LEGS_ITEM_ID:
					character.setLegsItem(new Item(in.nextInt()));
					break;
				case COLUMN_FEET_ITEM_ID:
					character.setFeetItem(new Item(in.nextInt()));
					break;
				case COLUMN_BACK_ITEM_ID:
					character.setBackItem(new Item(in.nextInt()));
					break;
				case COLUMN_BACKPACK_ITEM_ID:
					character.setBackpackItem(new Item(in.nextInt()));
					break;
				case CharacterSkillCostsSchema.TABLE_NAME:
					readSkillCosts(in, character);
					break;
				case CharacterSkillRanksSchema.TABLE_NAME:
					readSkillRanks(in, character);
					break;
				case CharacterSpecializationRanksSchema.TABLE_NAME:
					readSpecializationRanks(in, character);
					break;
				case CharacterSpellListRanksSchema.TABLE_NAME:
					readSpellListRanks(in, character);
					break;
				case CharacterTalentsSchema.TABLE_NAME:
					readTalentTiers(in, character);
					break;
				case CharacterStatsSchema.TABLE_NAME:
					readStats(in, character);
					break;
				case CharacterCurrentLevelSkillRanksSchema.TABLE_NAME:
					readCurrentLevelSkillRanks(in, character);
					break;
				case CharacterCurrentLevelSpecializationRanksSchema.TABLE_NAME:
					readCurrentLevelSpecializationRanks(in, character);
					break;
				case CharacterCurrentLevelSpellListRanksSchema.TABLE_NAME:
					readCurrentLevelSpellListRanks(in, character);
					break;
				case CharacterPurchasedCultureRanksSchema.TABLE_NAME:
					readPurchasedCultureRanks(in, character);
					break;
				case CharacterProfessionSkillsSchema.TABLE_NAME:
					readProfessionSkills(in, character);
					break;
				case CharacterKnacksSchema.TABLE_NAME:
					readKnacks(in, character);
					break;
			}
		}
		in.endObject();
		return character;
	}

	private void readSkillCosts(JsonReader in, Character character) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Skill newSkill = null;
			DevelopmentCostGroup costGroup = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CharacterSkillCostsSchema.COLUMN_SKILL_ID:
						newSkill = new Skill(in.nextInt());
						break;
					case CharacterSkillCostsSchema.COLUMN_COST_GROUP_NAME:
						costGroup = DevelopmentCostGroup.valueOf(in.nextString());
						break;
				}
			}
			if (newSkill != null) {
				character.getSkillCosts().put(newSkill, costGroup);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readSkillRanks(JsonReader in, Character character) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Skill newSkill = null;
			Short ranks = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CharacterSkillRanksSchema.COLUMN_SKILL_ID:
						newSkill = new Skill(in.nextInt());
						break;
					case CharacterSkillRanksSchema.COLUMN_RANKS:
						ranks = (short) in.nextInt();
						break;
				}
			}
			if (newSkill != null) {
				character.getSkillRanks().put(newSkill, ranks);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readSpecializationRanks(JsonReader in, Character character) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Specialization newSpecialization = null;
			Short ranks = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CharacterSpecializationRanksSchema.COLUMN_SPECIALIZATION_ID:
						newSpecialization = new Specialization(in.nextInt());
						break;
					case CharacterSpecializationRanksSchema.COLUMN_RANKS:
						ranks = (short) in.nextInt();
						break;
				}
			}
			if (newSpecialization != null) {
				character.getSpecializationRanks().put(newSpecialization, ranks);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readSpellListRanks(JsonReader in, Character character) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			SpellList newSpellList = null;
			Short ranks = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CharacterSpellListRanksSchema.COLUMN_SPELL_LIST_ID:
						newSpellList = new SpellList(in.nextInt());
						break;
					case CharacterSpellListRanksSchema.COLUMN_RANKS:
						ranks = (short) in.nextInt();
						break;
				}
			}
			if (newSpellList != null) {
				character.getSpellListRanks().put(newSpellList, ranks);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readTalentTiers(JsonReader in, Character character) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Talent newTalent = null;
			TalentInstance talentInstance = new TalentInstance();
			short tiers = 0;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CharacterTalentsSchema.COLUMN_ID:
						talentInstance.setId(in.nextInt());
						break;
					case CharacterTalentsSchema.COLUMN_TALENT_ID:
						newTalent = new Talent(in.nextInt());
						break;
					case CharacterTalentsSchema.COLUMN_TIERS:
						tiers = (short) in.nextInt();
						break;
					case CharacterTalentParametersSchema.TABLE_NAME:
						readTalentParameterValues(in, talentInstance);
						break;
				}
			}
			if (newTalent != null && tiers > 0) {
				talentInstance.setTalent(newTalent);
				talentInstance.setTiers(tiers);
				character.getTalentInstances().add(talentInstance);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readTalentParameterValues(JsonReader in, TalentInstance talentInstance) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Parameter newParameter = null;
			Object value = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CharacterTalentParametersSchema.COLUMN_PARAMETER_NAME:
						newParameter = Parameter.valueOf(in.nextString());
						break;
					case CharacterTalentParametersSchema.COLUMN_INT_VALUE:
						value = in.nextInt();
						break;
					case CharacterTalentParametersSchema.COLUMN_ENUM_NAME:
						value = in.nextString();
						break;
				}
			}
			if (newParameter != null) {
				talentInstance.getParameterValues().put(newParameter, value);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readStats(JsonReader in, Character character) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Statistic newStat = null;
			Short tempValue = null;
			Short potentialValue = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CharacterStatsSchema.COLUMN_STAT_NAME:
						newStat = Statistic.valueOf(in.nextString());
						break;
					case CharacterStatsSchema.COLUMN_CURRENT_VALUE:
						tempValue = (short) in.nextInt();
						break;
					case CharacterStatsSchema.COLUMN_POTENTIAL_VALUE:
						potentialValue = (short) in.nextInt();
						break;
				}
			}
			if (newStat != null) {
				character.getStatTemps().put(newStat, tempValue);
				character.getStatPotentials().put(newStat, potentialValue);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readCurrentLevelSkillRanks(JsonReader in, Character character) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Skill newSkill = null;
			Short ranks = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CharacterCurrentLevelSkillRanksSchema.COLUMN_SKILL_ID:
						newSkill = new Skill(in.nextInt());
						break;
					case CharacterCurrentLevelSkillRanksSchema.COLUMN_RANKS:
						ranks = (short) in.nextInt();
						break;
				}
			}
			if (newSkill != null) {
				character.getCurrentLevelSkillRanks().put(newSkill, ranks);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readCurrentLevelSpecializationRanks(JsonReader in, Character character) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Specialization newSpecialization = null;
			Short ranks = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CharacterCurrentLevelSpecializationRanksSchema.COLUMN_SPECIALIZATION_ID:
						newSpecialization = new Specialization(in.nextInt());
						break;
					case CharacterCurrentLevelSkillRanksSchema.COLUMN_RANKS:
						ranks = (short) in.nextInt();
						break;
				}
			}
			if (newSpecialization != null) {
				character.getCurrentLevelSpecializationRanks().put(newSpecialization, ranks);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readCurrentLevelSpellListRanks(JsonReader in, Character character) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			SpellList newSpellList = null;
			Short ranks = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CharacterCurrentLevelSpellListRanksSchema.COLUMN_SPELL_LIST_ID:
						newSpellList = new SpellList(in.nextInt());
						break;
					case CharacterCurrentLevelSpellListRanksSchema.COLUMN_RANKS:
						ranks = (short) in.nextInt();
						break;
				}
			}
			if (newSpellList != null) {
				character.getCurrentLevelSpellListRanks().put(newSpellList, ranks);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readPurchasedCultureRanks(JsonReader in, Character character) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Specialization newSpecialization = null;
			Skill newSkill = null;
			Short ranks = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CharacterPurchasedCultureRanksSchema.COLUMN_SKILL_ID:
						newSkill = new Skill(in.nextInt());
						break;
					case CharacterPurchasedCultureRanksSchema.COLUMN_SPECIALIZATION_ID:
						newSpecialization = new Specialization(in.nextInt());
						break;
					case CharacterCurrentLevelSkillRanksSchema.COLUMN_RANKS:
						ranks = (short) in.nextInt();
						break;
				}
			}
			if (newSpecialization != null) {
				character.getPurchasedCultureRanks().put(newSpecialization, ranks);
			}
			else if (newSkill != null) {
				character.getPurchasedCultureRanks().put(newSkill, ranks);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readProfessionSkills(JsonReader in, Character character) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Specialization newSpecialization = null;
			Skill newSkill = null;
			SpellList newSpellList = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CharacterProfessionSkillsSchema.COLUMN_SKILL_ID:
						newSkill = new Skill(in.nextInt());
						break;
					case CharacterProfessionSkillsSchema.COLUMN_SPECIALIZATION_ID:
						newSpecialization = new Specialization(in.nextInt());
						break;
					case CharacterProfessionSkillsSchema.COLUMN_SPELL_LIST_ID:
						newSpellList = new SpellList(in.nextInt());
						break;
				}
			}
			if (newSpecialization != null) {
				character.getProfessionSkills().add(newSpecialization);
			}
			else if (newSkill != null) {
				character.getProfessionSkills().add(newSkill);
			}
			else if (newSpellList != null) {
				character.getProfessionSkills().add(newSpellList);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readKnacks(JsonReader in, Character character) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Specialization newSpecialization = null;
			Skill newSkill = null;
			SpellList newSpellList = null;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CharacterKnacksSchema.COLUMN_SKILL_ID:
						newSkill = new Skill(in.nextInt());
						break;
					case CharacterKnacksSchema.COLUMN_SPECIALIZATION_ID:
						newSpecialization = new Specialization(in.nextInt());
						break;
					case CharacterKnacksSchema.COLUMN_SPELL_LIST_ID:
						newSpellList = new SpellList(in.nextInt());
						break;
				}
			}
			if (newSpecialization != null) {
				character.getKnacks().add(newSpecialization);
			}
			else if (newSkill != null) {
				character.getKnacks().add(newSkill);
			}
			else if (newSpellList != null) {
				character.getKnacks().add(newSpellList);
			}
			in.endObject();
		}
		in.endArray();
	}
}
