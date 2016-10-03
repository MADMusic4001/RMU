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
import com.madinnovations.rmu.data.entities.spells.Realm;

import java.io.IOException;
import java.util.Map;

/**
 * Json serializer and deserializer for the {@link Character} entities
 */
public class CharacterSerializer extends TypeAdapter<Character> implements CharacterSchema {
	@Override
	public void write(JsonWriter out, Character value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
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
		out.name(COLUMN_REALM_ID).value(value.getRealm().getId());
		out.name(COLUMN_HEIGHT).value(value.getHeight());
		out.name(COLUMN_WEIGHT).value(value.getWeight());
		out.name(COLUMN_CURRENT_HITS).value(value.getCurrentHits());
		out.name(COLUMN_MAX_HITS).value(value.getMaxHits());
		out.name(COLUMN_CURRENT_DEVELOPMENT_POINTS).value(value.getCurrentDevelopmentPoints());

		out.name(CharacterSkillCostsSchema.TABLE_NAME).beginArray();
		for(Map.Entry<Skill, SkillCost> entry : value.getSkillCosts().entrySet()) {
			out.beginObject();
			out.name(CharacterSkillCostsSchema.COLUMN_SKILL_ID).value(entry.getKey().getId());
			out.name(CharacterSkillCostsSchema.COLUMN_FIRST_COST).value(entry.getValue().getFirstCost());
			out.name(CharacterSkillCostsSchema.COLUMN_ADDITIONAL_COST).value(entry.getValue().getAdditionalCost());
			out.endObject();
		}
		out.endArray();

		out.name(CharacterSkillRanksSchema.TABLE_NAME).beginArray();
		for(Map.Entry<Skill, Short> entry : value.getSkillRanks().entrySet()) {
			out.beginObject();
			out.name(CharacterSkillRanksSchema.COLUMN_SKILL_ID).value(entry.getKey().getId());
			out.name(CharacterSkillRanksSchema.COLUMN_RANKS).value(entry.getValue());
			out.endObject();
		}
		out.endArray();

		out.name(CharacterTalentsSchema.TABLE_NAME).beginArray();
		for(Map.Entry<Talent, Short> entry : value.getTalentTiers().entrySet()) {
			out.beginObject();
			out.name(CharacterTalentsSchema.COLUMN_TALENT_ID).value(entry.getKey().getId());
			out.name(CharacterTalentsSchema.COLUMN_TIERS).value(entry.getValue());
			out.endObject();
		}
		out.endArray();

		out.name(CharacterStatsSchema.TABLE_NAME).beginArray();
		for(Map.Entry<Stat, Short> entry : value.getStatTemps().entrySet()) {
			out.beginObject();
			out.name(CharacterStatsSchema.COLUMN_STAT_ID).value(entry.getKey().getId());
			out.name(CharacterStatsSchema.COLUMN_CURRENT_VALUE).value(entry.getValue());
			out.name(CharacterStatsSchema.COLUMN_POTENTIAL_VALUE).value(value.getStatPotentials().get(entry.getKey()));
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
		while(in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					character.setId(in.nextInt());
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
				case COLUMN_REALM_ID:
					character.setRealm(new Realm(in.nextInt()));
					break;
				case COLUMN_HEIGHT:
					character.setHeight((short)in.nextInt());
					break;
				case COLUMN_WEIGHT:
					character.setWeight((short)in.nextInt());
					break;
				case COLUMN_CURRENT_HITS:
					character.setCurrentHits((short)in.nextInt());
					break;
				case COLUMN_MAX_HITS:
					character.setMaxHits((short)in.nextInt());
					break;
				case COLUMN_CURRENT_DEVELOPMENT_POINTS:
					character.setCurrentDevelopmentPoints((short)in.nextInt());
					break;
				case CharacterSkillCostsSchema.TABLE_NAME:
					readSkillCosts(in, character);
					break;
				case CharacterSkillRanksSchema.TABLE_NAME:
					readSkillRanks(in, character);
					break;
				case CharacterTalentsSchema.TABLE_NAME:
					readTalentTiers(in, character);
					break;
				case CharacterStatsSchema.TABLE_NAME:
					readStats(in, character);
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
			SkillCost skillCost = new SkillCost();
			in.beginObject();
			while(in.hasNext()) {
				switch(in.nextName()) {
					case CharacterSkillCostsSchema.COLUMN_SKILL_ID:
						newSkill = new Skill(in.nextInt());
						break;
					case CharacterSkillCostsSchema.COLUMN_FIRST_COST:
						skillCost.setFirstCost((short)in.nextInt());
						break;
					case CharacterSkillCostsSchema.COLUMN_ADDITIONAL_COST:
						skillCost.setAdditionalCost((short)in.nextInt());
						break;
				}
			}
			if(newSkill != null) {
				character.getSkillCosts().put(newSkill, skillCost);
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
			while(in.hasNext()) {
				switch (in.nextName()) {
					case CharacterSkillRanksSchema.COLUMN_SKILL_ID:
						newSkill = new Skill(in.nextInt());
						break;
					case CharacterSkillRanksSchema.COLUMN_RANKS:
						ranks = (short)in.nextInt();
						break;
				}
			}
			if(newSkill != null) {
				character.getSkillRanks().put(newSkill, ranks);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readTalentTiers(JsonReader in, Character character) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Talent newTalent = null;
			Short tiers = null;
			in.beginObject();
			while(in.hasNext()) {
				switch (in.nextName()) {
					case CharacterTalentsSchema.COLUMN_TALENT_ID:
						newTalent = new Talent(in.nextInt());
						break;
					case CharacterTalentsSchema.COLUMN_TIERS:
						tiers = (short)in.nextInt();
						break;
				}
			}
			if(newTalent != null) {
				character.getTalentTiers().put(newTalent, tiers);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readStats(JsonReader in, Character character) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Stat newStat = null;
			Short tempValue = null;
			Short potentialValue = null;
			in.beginObject();
			while(in.hasNext()) {
				switch(in.nextName()) {
					case CharacterStatsSchema.COLUMN_STAT_ID:
						newStat = new Stat(in.nextInt());
						break;
					case CharacterStatsSchema.COLUMN_CURRENT_VALUE:
						tempValue = (short)in.nextInt();
						break;
					case CharacterStatsSchema.COLUMN_POTENTIAL_VALUE:
						potentialValue = (short)in.nextInt();
						break;
				}
			}
			if(newStat != null) {
				character.getStatTemps().put(newStat, tempValue);
				character.getStatPotentials().put(newStat, potentialValue);
			}
			in.endObject();
		}
		in.endArray();
	}
}
