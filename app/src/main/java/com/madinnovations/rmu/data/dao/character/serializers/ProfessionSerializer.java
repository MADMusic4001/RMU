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
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionAssignableSkillCostSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSkillCategoryCostSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSkillCostSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionalSkillCategoriesSchema;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.SkillCost;
import com.madinnovations.rmu.data.entities.spells.Realm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Json serializer and deserializer for the {@link Profession} entities
 */
public class ProfessionSerializer extends TypeAdapter<Profession> implements ProfessionSchema {
	private static final String ASSIGNABLE_SKILL_COST_LIST = "assignableSkillCostList";

	@Override
	public void write(JsonWriter out, Profession value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		if(value.getRealm1() != null) {
			out.name(COLUMN_REALM1_ID).value(value.getRealm1().getId());
		}
		if(value.getRealm2() != null) {
			out.name(COLUMN_REALM2_ID).value(value.getRealm2().getId());
		}

		out.name(ProfessionSkillCategoryCostSchema.TABLE_NAME).beginArray();
		for(Map.Entry<SkillCategory, SkillCost> entry : value.getSkillCategoryCosts().entrySet()) {
			out.beginObject();
			out.name(ProfessionSkillCategoryCostSchema.COLUMN_SKILL_CATEGORY_ID).value(entry.getKey().getId());
			out.name(ProfessionSkillCategoryCostSchema.COLUMN_FIRST_COST).value(entry.getValue().getFirstCost());
			out.name(ProfessionSkillCategoryCostSchema.COLUMN_SECOND_COST).value(entry.getValue().getAdditionalCost());
			out.endObject();
		}
		out.endArray();

		out.name(ProfessionSkillCostSchema.TABLE_NAME).beginObject();
		for(Map.Entry<Skill, SkillCost> entry : value.getSkillCosts().entrySet()) {
			out.beginObject();
			out.name(ProfessionSkillCostSchema.COLUMN_SKILL_ID).value(entry.getKey().getId());
			out.name(ProfessionSkillCostSchema.COLUMN_FIRST_COST).value(entry.getValue().getFirstCost());
			out.name(ProfessionSkillCostSchema.COLUMN_SECOND_COST).value(entry.getValue().getAdditionalCost());
			out.endObject();
		}
		out.endArray();

		out.name(ProfessionAssignableSkillCostSchema.TABLE_NAME).beginArray();
		for(Map.Entry<SkillCategory, List<SkillCost>> entry : value.getAssignableSkillCosts().entrySet()) {
			out.beginObject();
			out.name(ProfessionAssignableSkillCostSchema.COLUMN_SKILL_CATEGORY_ID).value(entry.getKey().getId());
			out.name(ASSIGNABLE_SKILL_COST_LIST).beginArray();
			for(SkillCost skillCost : entry.getValue()) {
				out.beginObject();
				out.name(ProfessionAssignableSkillCostSchema.COLUMN_FIRST_COST).value(skillCost.getFirstCost());
				out.name(ProfessionAssignableSkillCostSchema.COLUMN_SECOND_COST).value(skillCost.getAdditionalCost());
				out.endObject();
			}
			out.endArray();
			out.endObject();
		}
		out.endArray();

		out.name(ProfessionalSkillCategoriesSchema.TABLE_NAME).beginArray();
		for(SkillCategory category : value.getProfessionalSkillCategories()) {
			out.value(category.getId());
		}
		out.endArray();

		out.endObject().flush();
	}

	@Override
	public Profession read(JsonReader in) throws IOException {
		Profession profession = new Profession();
		in.beginObject();
		while(in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					profession.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					profession.setName(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					profession.setDescription(in.nextString());
					break;
				case COLUMN_REALM1_ID:
					profession.setRealm1(new Realm(in.nextInt()));
					break;
				case COLUMN_REALM2_ID:
					profession.setRealm2(new Realm(in.nextInt()));
					break;
				case ProfessionSkillCategoryCostSchema.TABLE_NAME:
					readSkillCategoryCosts(in, profession);
					break;
				case ProfessionSkillCostSchema.TABLE_NAME:
					readSkillCosts(in, profession);
					break;
				case ProfessionAssignableSkillCostSchema.TABLE_NAME:
					readAssignableSkillCosts(in, profession);
					break;
				case ProfessionalSkillCategoriesSchema.TABLE_NAME:
					in.beginArray();
					while (in.hasNext()) {
						SkillCategory skillCategory = new SkillCategory(in.nextInt());
						profession.getProfessionalSkillCategories().add(skillCategory);
					}
					in.endArray();
					break;
			}
		}
		in.endObject();
		return profession;
	}

	private void readSkillCategoryCosts(JsonReader in, Profession profession) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			SkillCategory newSkillCategory = null;
			SkillCost skillCost = new SkillCost();
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case ProfessionSkillCostSchema.COLUMN_SKILL_ID:
						newSkillCategory = new SkillCategory(in.nextInt());
						break;
					case ProfessionSkillCostSchema.COLUMN_FIRST_COST:
						skillCost.setFirstCost((short)in.nextInt());
						break;
					case ProfessionSkillCostSchema.COLUMN_SECOND_COST:
						skillCost.setAdditionalCost((short)in.nextInt());
						break;
				}
			}
			if(newSkillCategory != null) {
				profession.getSkillCategoryCosts().put(newSkillCategory, skillCost);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readSkillCosts(JsonReader in, Profession profession) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			Skill newSkill = null;
			SkillCost skillCost = new SkillCost();
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case ProfessionSkillCostSchema.COLUMN_SKILL_ID:
						newSkill = new Skill(in.nextInt());
						break;
					case ProfessionSkillCostSchema.COLUMN_FIRST_COST:
						skillCost.setFirstCost((short)in.nextInt());
						break;
					case ProfessionSkillCostSchema.COLUMN_SECOND_COST:
						skillCost.setAdditionalCost((short)in.nextInt());
						break;
				}
			}
			if(newSkill != null) {
				profession.getSkillCosts().put(newSkill, skillCost);
			}
			in.endObject();
		}
		in.endArray();
	}

	private void readAssignableSkillCosts(JsonReader in, Profession profession) throws IOException {
		in.beginArray();
		while(in.hasNext()) {
			SkillCategory newSkillCategory = null;
			List<SkillCost> skillCostList = new ArrayList<>();
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case ProfessionAssignableSkillCostSchema.COLUMN_SKILL_CATEGORY_ID:
						newSkillCategory = new SkillCategory(in.nextInt());
						break;
					case ASSIGNABLE_SKILL_COST_LIST:
						in.beginArray();
						while (in.hasNext()) {
							SkillCost skillCost = new SkillCost();
							in.beginObject();
							while (in.hasNext()) {
								switch (in.nextName()) {
									case ProfessionAssignableSkillCostSchema.COLUMN_FIRST_COST:
										skillCost.setFirstCost((short) in.nextInt());
										break;
									case ProfessionAssignableSkillCostSchema.COLUMN_SECOND_COST:
										skillCost.setAdditionalCost((short) in.nextInt());
										break;
								}
							}
							skillCostList.add(skillCost);
							in.endObject();
						}
						in.endArray();
						break;
				}
			}
			if(newSkillCategory != null) {
				profession.getAssignableSkillCosts().put(newSkillCategory, skillCostList);
			}
			in.endObject();
		}
		in.endArray();
	}
}
