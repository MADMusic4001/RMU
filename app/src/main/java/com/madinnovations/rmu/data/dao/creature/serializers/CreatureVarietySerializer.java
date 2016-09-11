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

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureVarietySchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyAttacksSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyCriticalCodesSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietySkillsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyStatsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyTalentTiersSchema;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.CriticalCode;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.creature.CreatureType;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;
import com.madinnovations.rmu.data.entities.creature.Outlook;
import com.madinnovations.rmu.data.entities.spells.Realm;

import java.io.IOException;
import java.util.Map;

/**
 * Json serializer and deserializer for the {@link CreatureVariety} entities
 */
public class CreatureVarietySerializer extends TypeAdapter<CreatureVariety> implements CreatureVarietySchema {
	@Override
	public void write(JsonWriter out, CreatureVariety value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_TYPICAL_LEVEL).value(value.getTypicalLevel());
		out.name(COLUMN_LEVEL_SPREAD).value(value.getLevelSpread());
		out.name(COLUMN_HEIGHT).value(value.getHeight());
		out.name(COLUMN_LENGTH).value(value.getLength());
		out.name(COLUMN_WEIGHT).value(value.getWeight());
		out.name(COLUMN_HEALING_RATE).value(value.getHealingRate());
		out.name(COLUMN_BASE_HITS).value(value.getBaseHits());
		out.name(COLUMN_BASE_ENDURANCE).value(value.getBaseEndurance());
		out.name(COLUMN_ARMOR_TYPE).value(value.getArmorType());
		out.name(COLUMN_BASE_MOVEMENT_RATE).value(value.getBaseMovementRate());
		out.name(COLUMN_BASE_CHANNELING_RR).value(value.getBaseChannellingRR());
		out.name(COLUMN_BASE_ESSENCE_RR).value(value.getBaseEssenceRR());
		out.name(COLUMN_BASE_MENTALISM_RR).value(value.getBaseMentalismRR());
		out.name(COLUMN_BASE_PHYSICAL_RR).value(value.getBasePhysicalRR());
		out.name(COLUMN_BASE_FEAR_RR).value(value.getBaseFearRR());
		out.name(COLUMN_BASE_STRIDE).value(value.getBaseStride());
		out.name(COLUMN_LEFTOVER_DP).value(value.getLeftoverDP());
		out.name(COLUMN_ATTACK_SEQUENCE).value(value.getAttackSequence());
		out.name(COLUMN_TYPE_ID).value(value.getType().getId());
		out.name(COLUMN_SIZE_ID).value(value.getSize().getId());
		out.name(COLUMN_REALM1_ID).value(value.getRealm1().getId());
		if (value.getRealm2() == null) {
			out.name(COLUMN_REALM2_ID).value(value.getRealm2().getId());
		}
		out.name(COLUMN_OUTLOOK_ID).value(value.getOutlook().getId());

		if (value.getRacialStatBonuses() != null && !value.getRacialStatBonuses().isEmpty()) {
			out.name(VarietyStatsSchema.TABLE_NAME);
			out.beginArray();
			for (Map.Entry<Stat, Short> entry : value.getRacialStatBonuses().entrySet()) {
				out.beginObject();
				out.name(VarietyStatsSchema.COLUMN_STAT_ID).value(entry.getKey().getId());
				out.name(VarietyStatsSchema.COLUMN_BONUS).value(entry.getValue());
				out.endObject();
			}
			out.endArray();
		}

		if (value.getCriticalCodes() != null && !value.getCriticalCodes().isEmpty()) {
			out.name(VarietyCriticalCodesSchema.TABLE_NAME);
			out.beginArray();
			for (CriticalCode criticalCode : value.getCriticalCodes()) {
				out.value(criticalCode.getId());
			}
			out.endArray();
		}

		if (value.getTalentTiersMap() != null && !value.getTalentTiersMap().isEmpty()) {
			out.name(VarietyTalentTiersSchema.TABLE_NAME);
			out.beginArray();
			for (Map.Entry<Talent, Short> entry : value.getTalentTiersMap().entrySet()) {
				out.beginObject();
				out.name(VarietyTalentTiersSchema.COLUMN_TALENT_ID).value(entry.getKey().getId());
				out.name(VarietyTalentTiersSchema.COLUMN_TIERS).value(entry.getValue());
				out.endObject();
			}
			out.endArray();
		}

		if (value.getAttackBonusesMap() != null && !value.getAttackBonusesMap().isEmpty()) {
			out.name(VarietyAttacksSchema.TABLE_NAME);
			out.beginArray();
			for (Map.Entry<Attack, Short> entry : value.getAttackBonusesMap().entrySet()) {
				out.beginObject();
				out.name(VarietyAttacksSchema.COLUMN_ATTACK_ID).value(entry.getKey().getId());
				out.name(VarietyAttacksSchema.COLUMN_ATTACK_BONUS).value(entry.getValue());
				out.endObject();
			}
			out.endArray();
		}

		if (value.getSkillBonusesMap() != null && !value.getSkillBonusesMap().isEmpty()) {
			out.name(VarietySkillsSchema.TABLE_NAME);
			out.beginArray();
			for (Map.Entry<Skill, Short> entry : value.getSkillBonusesMap().entrySet()) {
				out.beginObject();
				out.name(VarietySkillsSchema.COLUMN_SKILL_ID).value(entry.getKey().getId());
				out.name(VarietySkillsSchema.COLUMN_SKILL_BONUS).value(entry.getValue());
				out.endObject();
			}
			out.endArray();
		}
		out.endObject();
		out.flush();
	}

	@Override
	public CreatureVariety read(JsonReader in) throws IOException {
		CreatureVariety creatureVariety = new CreatureVariety();

		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					creatureVariety.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					creatureVariety.setName(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					creatureVariety.setDescription(in.nextString());
					break;
				case COLUMN_TYPICAL_LEVEL:
					creatureVariety.setTypicalLevel((short) in.nextInt());
					break;
				case COLUMN_LEVEL_SPREAD:
					creatureVariety.setLevelSpread(in.nextString().charAt(0));
					break;
				case COLUMN_HEIGHT:
					creatureVariety.setHeight((short) in.nextInt());
					break;
				case COLUMN_LENGTH:
					creatureVariety.setLength((short) in.nextInt());
					break;
				case COLUMN_WEIGHT:
					creatureVariety.setWeight((short) in.nextInt());
					break;
				case COLUMN_HEALING_RATE:
					creatureVariety.setHealingRate((short) in.nextInt());
					break;
				case COLUMN_BASE_HITS:
					creatureVariety.setBaseHits((short) in.nextInt());
					break;
				case COLUMN_BASE_ENDURANCE:
					creatureVariety.setBaseEndurance((short) in.nextInt());
					break;
				case COLUMN_ARMOR_TYPE:
					creatureVariety.setArmorType((short) in.nextInt());
					break;
				case COLUMN_BASE_MOVEMENT_RATE:
					creatureVariety.setBaseMovementRate((short) in.nextInt());
					break;
				case COLUMN_BASE_CHANNELING_RR:
					creatureVariety.setBaseChannellingRR((short) in.nextInt());
					break;
				case COLUMN_BASE_ESSENCE_RR:
					creatureVariety.setBaseEssenceRR((short) in.nextInt());
					break;
				case COLUMN_BASE_MENTALISM_RR:
					creatureVariety.setBaseMentalismRR((short) in.nextInt());
					break;
				case COLUMN_BASE_PHYSICAL_RR:
					creatureVariety.setBasePhysicalRR((short) in.nextInt());
					break;
				case COLUMN_BASE_FEAR_RR:
					creatureVariety.setBaseFearRR((short) in.nextInt());
					break;
				case COLUMN_BASE_STRIDE:
					creatureVariety.setBaseStride((short) in.nextInt());
					break;
				case COLUMN_LEFTOVER_DP:
					creatureVariety.setLeftoverDP((short) in.nextInt());
					break;
				case COLUMN_ATTACK_SEQUENCE:
					creatureVariety.setAttackSequence(in.nextString());
					break;
				case COLUMN_TYPE_ID:
					creatureVariety.setType(new CreatureType(in.nextInt()));
					break;
				case COLUMN_SIZE_ID:
					creatureVariety.setSize(new Size(in.nextInt()));
					break;
				case COLUMN_REALM1_ID:
					creatureVariety.setRealm1(new Realm(in.nextInt()));
					break;
				case COLUMN_REALM2_ID:
					creatureVariety.setRealm2(new Realm(in.nextInt()));
					break;
				case COLUMN_OUTLOOK_ID:
					creatureVariety.setOutlook(new Outlook(in.nextInt()));
					break;
				case VarietyStatsSchema.TABLE_NAME:
					readRacialStatBonuses(in, creatureVariety);
					break;
				case VarietyCriticalCodesSchema.TABLE_NAME:
					readCriticalCodes(in, creatureVariety);
					break;
				case VarietyTalentTiersSchema.TABLE_NAME:
					readTalentTiers(in, creatureVariety);
					break;
				case VarietyAttacksSchema.TABLE_NAME:
					readAttackBonuses(in, creatureVariety);
					break;
				case VarietySkillsSchema.TABLE_NAME:
					readSkillBonuses(in, creatureVariety);
					break;
			}
		}
		return creatureVariety;
	}

	private void readRacialStatBonuses(JsonReader in, CreatureVariety creatureVariety) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			Stat newStat = null;
			short newBonus = 0;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case VarietyStatsSchema.COLUMN_STAT_ID:
						newStat = new Stat(in.nextInt());
						break;
					case VarietyStatsSchema.COLUMN_BONUS:
						newBonus = (short) in.nextInt();
						break;
				}
			}
			if (newStat != null) {
				creatureVariety.getRacialStatBonuses().put(newStat, newBonus);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readCriticalCodes(JsonReader in, CreatureVariety creatureVariety) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			creatureVariety.getCriticalCodes().add(new CriticalCode(in.nextInt()));
		}
		in.endArray();
	}

	private void readTalentTiers(JsonReader in, CreatureVariety creatureVariety) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			Talent newTalent = null;
			short newTiers = 0;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case VarietyTalentTiersSchema.COLUMN_TALENT_ID:
						newTalent = new Talent(in.nextInt());
						break;
					case VarietyTalentTiersSchema.COLUMN_TIERS:
						newTiers = (short) in.nextInt();
						break;
				}
			}
			if (newTalent != null) {
				creatureVariety.getTalentTiersMap().put(newTalent, newTiers);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readAttackBonuses(JsonReader in, CreatureVariety creatureVariety) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			Attack newAttack = null;
			short newBonus = 0;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case VarietyAttacksSchema.COLUMN_ATTACK_ID:
						newAttack = new Attack(in.nextInt());
						break;
					case VarietyAttacksSchema.COLUMN_ATTACK_BONUS:
						newBonus = (short) in.nextInt();
						break;
				}
			}
			if (newAttack != null) {
				creatureVariety.getAttackBonusesMap().put(newAttack, newBonus);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readSkillBonuses(JsonReader in, CreatureVariety creatureVariety) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			Skill newSkill = null;
			short newBonus = 0;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case VarietySkillsSchema.COLUMN_SKILL_ID:
						newSkill = new Skill(in.nextInt());
						break;
					case VarietySkillsSchema.COLUMN_SKILL_BONUS:
						newBonus = (short) in.nextInt();
						break;
				}
			}
			if (newSkill != null) {
				creatureVariety.getSkillBonusesMap().put(newSkill, newBonus);
			}
			in.endObject();
		}
		in.endArray();
	}
}
