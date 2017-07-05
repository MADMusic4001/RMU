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
package com.madinnovations.rmu.data.dao.creature.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SizeRxHandler;
import com.madinnovations.rmu.data.dao.character.schemas.RaceTalentParametersSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureVarietySchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyAttacksSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyCriticalCodesSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietySkillsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyStatsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyTalentParametersSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyTalentTiersSchema;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.CriticalCode;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillBonus;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.creature.CreatureType;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;
import com.madinnovations.rmu.data.entities.creature.Outlook;
import com.madinnovations.rmu.data.entities.spells.RealmDBO;
import com.madinnovations.rmu.data.entities.spells.SpellList;

import java.io.IOException;
import java.util.Map;

import javax.inject.Singleton;

/**
 * Json serializer and deserializer for the {@link CreatureVariety} entities
 */
@Singleton
public class CreatureVarietySerializer extends TypeAdapter<CreatureVariety> implements CreatureVarietySchema {
	@SuppressWarnings("unused")
	private static final String TAG = "CreatureVarietySerializ";
	private AttackRxHandler attackRxHandler;
	private SizeRxHandler sizeRxHandler;

	@Override
	public void write(JsonWriter out, CreatureVariety value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_TYPICAL_LEVEL).value(value.getTypicalLevel());
		out.name(COLUMN_LEVEL_SPREAD).value(String.valueOf(value.getLevelSpread()));
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
		if(value.getCriticalSizeModifier() != null) {
			out.name(COLUMN_CRITICAL_SIZE_MODIFIER_ID).value(value.getCriticalSizeModifier().getId());
		}
		out.name(COLUMN_ATTACK_SEQUENCE).value(value.getAttackSequence());
		out.name(COLUMN_TYPE_ID).value(value.getType().getId());
		out.name(COLUMN_SIZE_ID).value(value.getSize().getId());
		out.name(COLUMN_REALM1_ID).value(value.getRealmDBO1().getId());
		if (value.getRealmDBO2() != null) {
			out.name(COLUMN_REALM2_ID).value(value.getRealmDBO2().getId());
		}
		out.name(COLUMN_OUTLOOK_ID).value(value.getOutlook().getId());

		if (value.getRacialStatBonuses() != null && !value.getRacialStatBonuses().isEmpty()) {
			out.name(VarietyStatsSchema.TABLE_NAME);
			out.beginArray();
			for (Map.Entry<Statistic, Short> entry : value.getRacialStatBonuses().entrySet()) {
				out.beginObject();
				out.name(VarietyStatsSchema.COLUMN_STAT_NAME).value(entry.getKey().name());
				out.name(VarietyStatsSchema.COLUMN_BONUS).value(entry.getValue());
				out.endObject();
			}
			out.endArray();
		}

		if (value.getCriticalCodes() != null && !value.getCriticalCodes().isEmpty()) {
			out.name(VarietyCriticalCodesSchema.TABLE_NAME);
			out.beginArray();
			for (CriticalCode criticalCode : value.getCriticalCodes()) {
				out.value(criticalCode.name());
			}
			out.endArray();
		}

		if (value.getTalentInstancesList() != null && !value.getTalentInstancesList().isEmpty()) {
			out.name(VarietyTalentTiersSchema.TABLE_NAME);
			out.beginArray();
			for (TalentInstance talentInstance : value.getTalentInstancesList()) {
				out.beginObject();
				out.name(VarietyTalentTiersSchema.COLUMN_ID).value(talentInstance.getId());
				out.name(VarietyTalentTiersSchema.COLUMN_TALENT_ID).value(talentInstance.getTalent().getId());
				out.name(VarietyTalentTiersSchema.COLUMN_TIERS).value(talentInstance.getTiers());
				if(!talentInstance.getParameterValues().isEmpty()) {
					out.name(VarietyTalentParametersSchema.TABLE_NAME);
					out.beginArray();
					for(Map.Entry<Parameter, Object> paramEntry : talentInstance.getParameterValues().entrySet()) {
						out.beginObject();
						out.name(VarietyTalentParametersSchema.COLUMN_PARAMETER_NAME).value(paramEntry.getKey().name());
						if(paramEntry.getValue() != null) {
							if(paramEntry.getValue() instanceof Integer) {
								out.name(VarietyTalentParametersSchema.COLUMN_INT_VALUE).value((Integer) paramEntry.getValue());
							}
							else {
								out.name(VarietyTalentParametersSchema.COLUMN_ENUM_NAME).value((String) paramEntry.getValue());
							}
						}
						out.endObject();
					}
					out.endArray();
				}
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

		if (value.getSkillBonusesList() != null && !value.getSkillBonusesList().isEmpty()) {
			out.name(VarietySkillsSchema.TABLE_NAME);
			out.beginArray();
			for (SkillBonus skillBonus : value.getSkillBonusesList()) {
				out.beginObject();
				if(skillBonus.getSkill() != null) {
					out.name(VarietySkillsSchema.COLUMN_SKILL_ID).value(skillBonus.getSkill().getId());
				}
				else if(skillBonus.getSpecialization() != null) {
					out.name(VarietySkillsSchema.COLUMN_SPECIALIZATION_ID).value(skillBonus.getSpecialization().getId());
				}
				else if(skillBonus.getSpellList() != null) {
					out.name(VarietySkillsSchema.COLUMN_SPELL_LIST_ID).value(skillBonus.getSpellList().getId());
				}
				out.name(VarietySkillsSchema.COLUMN_SKILL_BONUS).value(skillBonus.getBonus());
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
		creatureVariety.setAttackRxHandler(attackRxHandler);
		creatureVariety.setSizeRxHandler(sizeRxHandler);

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
				case COLUMN_CRITICAL_SIZE_MODIFIER_ID:
					creatureVariety.setCriticalSizeModifier(new Size(in.nextInt()));
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
					creatureVariety.setRealmDBO1(new RealmDBO(in.nextInt()));
					break;
				case COLUMN_REALM2_ID:
					creatureVariety.setRealmDBO2(new RealmDBO(in.nextInt()));
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
		in.endObject();
		return creatureVariety;
	}

	private void readRacialStatBonuses(JsonReader in, CreatureVariety creatureVariety) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			Statistic newStat = null;
			short newBonus = 0;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case VarietyStatsSchema.COLUMN_STAT_NAME:
					case "statId":
						newStat = Statistic.valueOf(in.nextString());
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
			creatureVariety.getCriticalCodes().add(CriticalCode.valueOf(in.nextString()));
		}
		in.endArray();
	}

	private void readTalentTiers(JsonReader in, CreatureVariety creatureVariety) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			Talent newTalent = null;
			TalentInstance talentInstance = new TalentInstance();
			short newTiers = 0;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case VarietyTalentTiersSchema.COLUMN_ID:
						talentInstance.setId(in.nextInt());
						break;
					case VarietyTalentTiersSchema.COLUMN_TALENT_ID:
						newTalent = new Talent(in.nextInt());
						break;
					case VarietyTalentTiersSchema.COLUMN_TIERS:
						newTiers = (short) in.nextInt();
						break;
					case VarietyTalentParametersSchema.TABLE_NAME:
						readTalentParameterValues(in, talentInstance);
						break;
				}
			}
			talentInstance.setTalent(newTalent);
			talentInstance.setTiers(newTiers);
			creatureVariety.getTalentInstancesList().add(talentInstance);
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
			SkillBonus skillBonus = new SkillBonus();
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case VarietySkillsSchema.COLUMN_SKILL_ID:
						skillBonus.setSkill(new Skill(in.nextInt()));
						break;
					case VarietySkillsSchema.COLUMN_SPECIALIZATION_ID:
						skillBonus.setSpecialization(new Specialization(in.nextInt()));
						break;
					case VarietySkillsSchema.COLUMN_SPELL_LIST_ID:
						skillBonus.setSpellList(new SpellList(in.nextInt()));
						break;
					case VarietySkillsSchema.COLUMN_SKILL_BONUS:
						skillBonus.setBonus((short)in.nextInt());
						break;
				}
			}
			creatureVariety.getSkillBonusesList().add(skillBonus);
			in.endObject();
		}
		in.endArray();
	}

	private void readTalentParameterValues(JsonReader in, TalentInstance talentInstance) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			Parameter parameter = null;
			Object value = null;
			in.beginObject();
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
			in.endObject();
			if(parameter != null) {
				talentInstance.getParameterValues().put(parameter, value);
			}
		}
		in.endArray();
	}

	// Getters and setters
	public void setAttackRxHandler(AttackRxHandler attackRxHandler) {
		this.attackRxHandler = attackRxHandler;
	}
	public void setSizeRxHandler(SizeRxHandler sizeRxHandler) {
		this.sizeRxHandler = sizeRxHandler;
	}
}
