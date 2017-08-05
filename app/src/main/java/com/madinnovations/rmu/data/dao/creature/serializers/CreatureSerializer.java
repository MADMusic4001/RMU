/*
 *
 *   Copyright (C) 2017 MadInnovations
 *   <p/>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   <p/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   <p/>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *
 */

package com.madinnovations.rmu.data.dao.creature.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureAttackBonusSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSkillBonusSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSpecializationBonusSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSpellListBonusSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTalentParametersSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTalentsSchema;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillBonus;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;
import com.madinnovations.rmu.data.entities.spells.SpellList;

import java.io.IOException;
import java.util.Map;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 *         Created 7/16/2017.
 */

public class CreatureSerializer extends TypeAdapter<Creature> implements CreatureSchema {
	@Override
	public void write(JsonWriter out, Creature value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_CAMPAIGN_ID).value(value.getCampaign().getId());
		out.name(COLUMN_CREATURE_VARIETY_ID).value(value.getCreatureVariety().getId());
		out.name(COLUMN_CREATURE_ARCHETYPE_ID).value(value.getArchetype().getId());
		out.name(COLUMN_LEVEL).value(value.getCurrentLevel());
		out.name(COLUMN_MAX_HITS).value(value.getMaxHits());
		out.name(COLUMN_CURRENT_HITS).value(value.getCurrentHits());
		out.name(COLUMN_CURRENT_DPS).value(value.getCurrentDevelopmentPoints());
		out.name(COLUMN_BASE_MOVEMENT_RATE).value(value.getBaseMovementRate());
		out.name(COLUMN_NUM_CREATURES).value(value.getNumCreatures());
		if(value.getPrimarySkillBonusesList().size() > 0 || value.getSecondarySkillBonusesList().size() > 0) {
			writeSkillBonuses(out, value);
		}
		if(value.getSpecializationBonuses().size() > 0) {
			writeSpecializationBonuses(out, value);
		}
		if(value.getSpellListBonuses().size() > 0) {
			writeSpellListBonuses(out, value);
		}
		if(value.getTalentInstances().size() > 0) {
			writeTalents(out, value);
		}
		if(value.getPrimaryAttackBonusesMap().size() > 0 || value.getSecondaryAttackBonusesMap().size() > 0) {
			writeAttackBonuses(out, value);
		}
		out.endObject();
	}

	@Override
	public Creature read(JsonReader in) throws IOException {
		Creature creature = new Creature();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					creature.setId(in.nextInt());
					break;
				case COLUMN_CAMPAIGN_ID:
					creature.setCampaign(new Campaign(in.nextInt()));
					break;
				case COLUMN_CREATURE_VARIETY_ID:
					creature.setCreatureVariety(new CreatureVariety(in.nextInt()));
					break;
				case COLUMN_CREATURE_ARCHETYPE_ID:
					creature.setArchetype(new CreatureArchetype(in.nextInt()));
					break;
				case COLUMN_LEVEL:
					creature.setCurrentLevel((short) in.nextInt());
					break;
				case COLUMN_MAX_HITS:
					creature.setMaxHits(in.nextInt());
					break;
				case COLUMN_CURRENT_HITS:
					creature.setCurrentHits(in.nextInt());
					break;
				case COLUMN_CURRENT_DPS:
					creature.setCurrentDevelopmentPoints((short) in.nextInt());
					break;
				case COLUMN_BASE_MOVEMENT_RATE:
					creature.setBaseMovementRate((short)in.nextInt());
					break;
				case COLUMN_NUM_CREATURES:
					creature.setNumCreatures((short) in.nextInt());
					break;
				case CreatureSkillBonusSchema.TABLE_NAME:
					readSkillBonuses(in, creature);
					break;
				case CreatureSpecializationBonusSchema.TABLE_NAME:
					readSpecializationBonuses(in, creature);
					break;
				case CreatureSpellListBonusSchema.TABLE_NAME:
					readSpellListBonuses(in, creature);
					break;
				case CreatureTalentsSchema.TABLE_NAME:
					readTalents(in, creature);
					break;
				case CreatureAttackBonusSchema.TABLE_NAME:
					readAttackBonuses(in, creature);
					break;
			}
		}
		in.endObject();

		return creature;
	}

	private void writeSkillBonuses(JsonWriter out, Creature value) throws IOException {
		out.name(CreatureSkillBonusSchema.TABLE_NAME).beginArray();
		for(SkillBonus skillBonus : value.getPrimarySkillBonusesList()) {
			out.beginObject();
			out.name(CreatureSkillBonusSchema.COLUMN_SKILL_ID).value(skillBonus.getSkill().getId());
			out.name(CreatureSkillBonusSchema.COLUMN_BONUS).value(skillBonus.getBonus());
			out.name(CreatureSkillBonusSchema.COLUMN_IS_PRIMARY).value(true);
			out.endObject();
		}
		for (SkillBonus skillBonus : value.getSecondarySkillBonusesList()) {
			out.beginObject();
			out.name(CreatureSkillBonusSchema.COLUMN_SKILL_ID).value(skillBonus.getSkill().getId());
			out.name(CreatureSkillBonusSchema.COLUMN_BONUS).value(skillBonus.getBonus());
			out.name(CreatureSkillBonusSchema.COLUMN_IS_PRIMARY).value(false);
			out.endObject();
		}
		out.endArray();
	}

	private void writeSpecializationBonuses(JsonWriter out, Creature value) throws IOException {
		out.name(CreatureSpecializationBonusSchema.TABLE_NAME).beginArray();
		for(Map.Entry<Specialization, Short> entry : value.getSpecializationBonuses().entrySet()) {
			out.beginObject();
			out.name(CreatureSpecializationBonusSchema.COLUMN_SPECIALIZATION_ID).value(entry.getKey().getId());
			out.name(CreatureSpecializationBonusSchema.COLUMN_BONUS).value(entry.getValue());
			out.endObject();
		}
		out.endArray();
	}

	private void writeSpellListBonuses(JsonWriter out, Creature value) throws IOException {
		out.name(CreatureSpellListBonusSchema.TABLE_NAME).beginArray();
		for(Map.Entry<SpellList, Short> entry : value.getSpellListBonuses().entrySet()) {
			out.beginObject();
			out.name(CreatureSpellListBonusSchema.COLUMN_SPELL_LIST_ID).value(entry.getKey().getId());
			out.name(CreatureSpellListBonusSchema.COLUMN_BONUS).value(entry.getValue());
			out.endObject();
		}
		out.endArray();
	}

	private void writeTalents(JsonWriter out, Creature value) throws IOException {
		out.name(CreatureTalentsSchema.TABLE_NAME).beginArray();
		for(TalentInstance talentInstance : value.getTalentInstances()) {
			out.beginObject();
			out.name(CreatureTalentsSchema.COLUMN_TALENT_ID).value(talentInstance.getTalent().getId());
			out.name(CreatureTalentsSchema.COLUMN_TIERS).value(talentInstance.getTiers());
			if(talentInstance.getParameterValues().size() > 0) {
				out.name(CreatureTalentParametersSchema.TABLE_NAME).beginArray();
				for(Map.Entry<Parameter, Object> entry : talentInstance.getParameterValues().entrySet()) {
					out.beginObject();
					out.name(CreatureTalentParametersSchema.COLUMN_PARAMETER_NAME).value(entry.getKey().name());
					if(entry.getValue() != null) {
						if (entry.getValue() instanceof Integer) {
							out.name(CreatureTalentParametersSchema.COLUMN_INT_VALUE).value((Integer) entry.getValue());
						}
						else {
							out.name(CreatureTalentParametersSchema.COLUMN_ENUM_NAME).value((String) entry.getValue());
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

	private void writeAttackBonuses(JsonWriter out, Creature value) throws IOException {
		out.name(CreatureAttackBonusSchema.TABLE_NAME).beginArray();
		for(Map.Entry<Attack, Short> entry : value.getPrimaryAttackBonusesMap().entrySet()) {
			out.beginObject();
			out.name(CreatureAttackBonusSchema.COLUMN_ATTACK_ID).value(entry.getKey().getId());
			out.name(CreatureAttackBonusSchema.COLUMN_BONUS).value(entry.getValue());
			out.name(CreatureAttackBonusSchema.COLUMN_IS_PRIMARY).value(true);
			out.endObject();
		}
		for(Map.Entry<Attack, Short> entry : value.getSecondaryAttackBonusesMap().entrySet()) {
			out.beginObject();
			out.name(CreatureAttackBonusSchema.COLUMN_ATTACK_ID).value(entry.getKey().getId());
			out.name(CreatureAttackBonusSchema.COLUMN_BONUS).value(entry.getValue());
			out.name(CreatureAttackBonusSchema.COLUMN_IS_PRIMARY).value(false);
			out.endObject();
		}
		out.endArray();
	}

	private void readSkillBonuses(JsonReader in, Creature creature) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			SkillBonus skillBonus = new SkillBonus();
			boolean primary = false;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CreatureSkillBonusSchema.COLUMN_SKILL_ID:
						skillBonus.setSkill(new Skill(in.nextInt()));
						break;
					case CreatureSkillBonusSchema.COLUMN_BONUS:
						skillBonus.setBonus((short)in.nextInt());
						break;
					case CreatureSkillBonusSchema.COLUMN_IS_PRIMARY:
						primary = in.nextBoolean();
						break;
				}
			}
			if(primary) {
				creature.getPrimarySkillBonusesList().add(skillBonus);
			}
			else {
				creature.getSecondarySkillBonusesList().add(skillBonus);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readSpecializationBonuses(JsonReader in, Creature creature) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			Specialization specialization = null;
			short bonus = 0;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CreatureSpecializationBonusSchema.COLUMN_SPECIALIZATION_ID:
						specialization = new Specialization(in.nextInt());
						break;
					case CreatureSpecializationBonusSchema.COLUMN_BONUS:
						bonus = (short)in.nextInt();
						break;
				}
			}
			creature.getSpecializationBonuses().put(specialization, bonus);
			in.endObject();
		}
		in.endArray();
	}

	private void readSpellListBonuses(JsonReader in, Creature creature) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			SpellList spellList = null;
			short bonus = 0;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CreatureSpellListBonusSchema.COLUMN_SPELL_LIST_ID:
						spellList = new SpellList(in.nextInt());
						break;
					case CreatureSpellListBonusSchema.COLUMN_BONUS:
						bonus = (short)in.nextInt();
						break;
				}
			}
			creature.getSpellListBonuses().put(spellList, bonus);
			in.endObject();
		}
		in.endArray();
	}

	private void readTalents(JsonReader in, Creature creature) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			TalentInstance talentInstance = new TalentInstance();
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CreatureTalentsSchema.COLUMN_TALENT_ID:
						talentInstance.setTalent(new Talent(in.nextInt()));
						break;
					case CreatureTalentsSchema.COLUMN_TIERS:
						talentInstance.setTiers((short)in.nextInt());
						break;
					case CreatureTalentParametersSchema.TABLE_NAME:
						in.beginArray();
						while (in.hasNext()) {
							Parameter parameter = null;
							Object value = null;
							in.beginObject();
							while(in.hasNext()) {
								switch (in.nextName()) {
									case CreatureTalentParametersSchema.COLUMN_PARAMETER_NAME:
										parameter = Parameter.valueOf(in.nextString());
										break;
									case CreatureTalentParametersSchema.COLUMN_ENUM_NAME:
										value = in.nextString();
										break;
									case CreatureTalentParametersSchema.COLUMN_INT_VALUE:
										value = in.nextInt();
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
			creature.getTalentInstances().add(talentInstance);
			in.endObject();
		}
		in.endArray();
	}

	private void readAttackBonuses(JsonReader in, Creature creature) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			Attack attack = null;
			short bonus = 0;
			boolean primary = false;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CreatureAttackBonusSchema.COLUMN_ATTACK_ID:
						attack = new Attack(in.nextInt());
						break;
					case CreatureAttackBonusSchema.COLUMN_BONUS:
						bonus = (short)in.nextInt();
						break;
					case CreatureAttackBonusSchema.COLUMN_IS_PRIMARY:
						primary = in.nextBoolean();
						break;
				}
			}
			if(primary) {
				creature.getPrimaryAttackBonusesMap().put(attack, bonus);
			}
			else {
				creature.getSecondaryAttackBonusesMap().put(attack, bonus);
			}
			in.endObject();
		}
		in.endArray();
	}
}
