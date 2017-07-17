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
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSkillRanksSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSpecializationRanksSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSpellListRanksSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTalentParametersSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTalentsSchema;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Skill;
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
		out.name(COLUMN_NUM_CREATURES).value(value.getNumCreatures());
		if(value.getSkillRanks().size() > 0) {
			writeSkillRanks(out, value);
		}
		if(value.getSpecializationRanks().size() > 0) {
			writeSpecializationRanks(out, value);
		}
		if(value.getSpellListRanks().size() > 0) {
			writeSpellListRanks(out, value);
		}
		if(value.getTalentInstances().size() > 0) {
			writeTalents(out, value);
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
				case COLUMN_NUM_CREATURES:
					creature.setNumCreatures((short) in.nextInt());
					break;
				case CreatureSkillRanksSchema.TABLE_NAME:
					readSkillRanks(in, creature);
					break;
				case CreatureSpecializationRanksSchema.TABLE_NAME:
					readSpecializationRanks(in, creature);
					break;
				case CreatureSpellListRanksSchema.TABLE_NAME:
					readSpellListRanks(in, creature);
					break;
				case CreatureTalentsSchema.TABLE_NAME:
					readTalents(in, creature);
					break;
			}
		}
		in.endObject();

		return creature;
	}

	public void writeSkillRanks(JsonWriter out, Creature value) throws IOException {
		out.name(CreatureSkillRanksSchema.TABLE_NAME).beginArray();
		for(Map.Entry<Skill, Short> entry : value.getSkillRanks().entrySet()) {
			out.beginObject();
			out.name(CreatureSkillRanksSchema.COLUMN_SKILL_ID).value(entry.getKey().getId());
			out.name(CreatureSkillRanksSchema.COLUMN_RANKS).value(entry.getValue());
			out.endObject();
		}
		out.endArray();
	}

	public void writeSpecializationRanks(JsonWriter out, Creature value) throws IOException {
		out.name(CreatureSpecializationRanksSchema.TABLE_NAME).beginArray();
		for(Map.Entry<Specialization, Short> entry : value.getSpecializationRanks().entrySet()) {
			out.beginObject();
			out.name(CreatureSpecializationRanksSchema.COLUMN_SPECIALIZATION_ID).value(entry.getKey().getId());
			out.name(CreatureSpecializationRanksSchema.COLUMN_RANKS).value(entry.getValue());
			out.endObject();
		}
		out.endArray();
	}

	public void writeSpellListRanks(JsonWriter out, Creature value) throws IOException {
		out.name(CreatureSpellListRanksSchema.TABLE_NAME).beginArray();
		for(Map.Entry<SpellList, Short> entry : value.getSpellListRanks().entrySet()) {
			out.beginObject();
			out.name(CreatureSpellListRanksSchema.COLUMN_SPELL_LIST_ID).value(entry.getKey().getId());
			out.name(CreatureSpellListRanksSchema.COLUMN_RANKS).value(entry.getValue());
			out.endObject();
		}
		out.endArray();
	}

	public void writeTalents(JsonWriter out, Creature value) throws IOException {
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

	private void readSkillRanks(JsonReader in, Creature creature) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			Skill skill = null;
			Short ranks = 0;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CreatureSkillRanksSchema.COLUMN_SKILL_ID:
						skill = new Skill(in.nextInt());
						break;
					case CreatureSkillRanksSchema.COLUMN_RANKS:
						ranks = (short)in.nextInt();
						break;
				}
			}
			creature.getSkillRanks().put(skill, ranks);
			in.endObject();
		}
		in.endArray();
	}

	private void readSpecializationRanks(JsonReader in, Creature creature) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			Specialization specialization = null;
			Short ranks = 0;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CreatureSpecializationRanksSchema.COLUMN_SPECIALIZATION_ID:
						specialization = new Specialization(in.nextInt());
						break;
					case CreatureSpecializationRanksSchema.COLUMN_RANKS:
						ranks = (short)in.nextInt();
						break;
				}
			}
			creature.getSpecializationRanks().put(specialization, ranks);
			in.endObject();
		}
		in.endArray();
	}

	private void readSpellListRanks(JsonReader in, Creature creature) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			SpellList spellList = null;
			Short ranks = 0;
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case CreatureSpellListRanksSchema.COLUMN_SPELL_LIST_ID:
						spellList = new SpellList(in.nextInt());
						break;
					case CreatureSpellListRanksSchema.COLUMN_RANKS:
						ranks = (short)in.nextInt();
						break;
				}
			}
			creature.getSpellListRanks().put(spellList, ranks);
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
}
