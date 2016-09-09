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
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSkillCategoryCostSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSkillCostSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionalSkillCategoriesSchema;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.SkillCost;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Json serializer and deserializer for the {@link Profession} entities
 */
public class ProfessionSerializer implements JsonSerializer<Profession>, JsonDeserializer<Profession>, ProfessionSchema {
	private RealmDao         realmDao;
	private SkillDao         skillDao;
	private SkillCategoryDao skillCategoryDao;

	/**
	 * Creates a new CharacterSerializer instance.
	 */
	@Inject
	public ProfessionSerializer(RealmDao realmDao, SkillDao skillDao, SkillCategoryDao skillCategoryDao) {
		this.realmDao = realmDao;
		this.skillDao = skillDao;
		this.skillCategoryDao = skillCategoryDao;
	}

	@Override
	public JsonElement serialize(Profession src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_NAME, src.getName());
		jsonObject.addProperty(COLUMN_DESCRIPTION, src.getDescription());
		if(src.getRealm1() != null) {
			jsonObject.addProperty(COLUMN_REALM1_ID, src.getRealm1().getId());
		}
		if(src.getRealm2() != null) {
			jsonObject.addProperty(COLUMN_REALM2_ID, src.getRealm2().getId());
		}

		final JsonArray skillCategoryCostsArray = new JsonArray();
		for(Map.Entry<SkillCategory, SkillCost> entry : src.getSkillCategoryCosts().entrySet()) {
			JsonObject skillCategoryCostEntry = new JsonObject();
			skillCategoryCostEntry.addProperty(ProfessionSkillCategoryCostSchema.COLUMN_ID, entry.getKey().getId());
			skillCategoryCostEntry.addProperty(ProfessionSkillCategoryCostSchema.COLUMN_FIRST_COST, entry.getValue().getFirstCost());
			skillCategoryCostEntry.addProperty(ProfessionSkillCategoryCostSchema.COLUMN_SECOND_COST, entry.getValue().getAdditionalCost());
			skillCategoryCostsArray.add(skillCategoryCostEntry);
		}
		jsonObject.add(ProfessionSkillCategoryCostSchema.TABLE_NAME, skillCategoryCostsArray);

		final JsonArray skillCostsArray = new JsonArray();
		for(Map.Entry<Skill, SkillCost> entry : src.getSkillCosts().entrySet()) {
			JsonObject skillCostEntry = new JsonObject();
			skillCostEntry.addProperty(ProfessionSkillCostSchema.COLUMN_ID, entry.getKey().getId());
			skillCostEntry.addProperty(ProfessionSkillCostSchema.COLUMN_FIRST_COST, entry.getValue().getFirstCost());
			skillCostEntry.addProperty(ProfessionSkillCostSchema.COLUMN_SECOND_COST, entry.getValue().getAdditionalCost());
			skillCostsArray.add(skillCostEntry);
		}
		jsonObject.add(ProfessionSkillCostSchema.TABLE_NAME, skillCostsArray);

		final JsonArray assignableCostsArray = new JsonArray();
		for(Map.Entry<SkillCategory, List<SkillCost>> entry : src.getAssignableSkillCosts().entrySet()) {
			JsonObject assignableCostEntry = new JsonObject();
			assignableCostEntry.addProperty(ProfessionSkillCategoryCostSchema.COLUMN_ID, entry.getKey().getId());
			JsonArray costsArray = new JsonArray();
			for(SkillCost skillCost : entry.getValue()) {
				JsonObject skillCostObject = new JsonObject();
				skillCostObject.addProperty(ProfessionSkillCostSchema.COLUMN_FIRST_COST, skillCost.getFirstCost());
				skillCostObject.addProperty(ProfessionSkillCostSchema.COLUMN_SECOND_COST, skillCost.getAdditionalCost());
				costsArray.add(skillCostObject);
			}
			assignableCostEntry.add(COLUMN_COST_ARRAY, costsArray);
			assignableCostsArray.add(assignableCostEntry);
		}
		jsonObject.add(ProfessionSkillCategoryCostSchema.TABLE_NAME, assignableCostsArray);

		final JsonArray professionalSkillCategories = new JsonArray();
		for(SkillCategory category : src.getProfessionalSkillCategories()) {
			professionalSkillCategories.add(category.getId());
		}
		jsonObject.add(ProfessionalSkillCategoriesSchema.TABLE_NAME, professionalSkillCategories);

		return jsonObject;
	}

	@Override
	public Profession deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Profession profession = new Profession();
		JsonObject jsonObject = json.getAsJsonObject();
		profession.setId(jsonObject.get(COLUMN_ID).getAsInt());
		profession.setName(jsonObject.get(COLUMN_NAME).getAsString());
		profession.setDescription(jsonObject.get(COLUMN_DESCRIPTION).getAsString());
		JsonElement element = jsonObject.get(COLUMN_REALM1_ID);
		if(element != null) {
			profession.setRealm1(realmDao.getById(element.getAsInt()));
		}
		element = jsonObject.get(COLUMN_REALM2_ID);
		if(element != null) {
			profession.setRealm2(realmDao.getById(element.getAsInt()));
		}

		JsonArray skillCategoryCosts = jsonObject.getAsJsonArray(ProfessionSkillCategoryCostSchema.TABLE_NAME);
		for(int i = 0; i < skillCategoryCosts.size(); i ++ ) {
			final JsonObject skillCostObject = skillCategoryCosts.get(i).getAsJsonObject();
			SkillCategory newSkillCategory = skillCategoryDao
					.getById(skillCostObject.get(ProfessionSkillCategoryCostSchema.COLUMN_ID).getAsInt());
			SkillCost skillCost = new SkillCost(
					skillCostObject.get(ProfessionSkillCategoryCostSchema.COLUMN_FIRST_COST).getAsShort(),
					skillCostObject.get(ProfessionSkillCategoryCostSchema.COLUMN_SECOND_COST).getAsShort());
			profession.getSkillCategoryCosts().put(newSkillCategory, skillCost);
		}

		JsonArray skillCosts = jsonObject.getAsJsonArray(ProfessionSkillCostSchema.TABLE_NAME);
		for(int i = 0; i < skillCosts.size(); i ++ ) {
			final JsonObject skillCostObject = skillCosts.get(i).getAsJsonObject();
			Skill newSkill = skillDao
					.getById(skillCostObject.get(ProfessionSkillCostSchema.COLUMN_ID).getAsInt());
			SkillCost skillCost = new SkillCost(
					skillCostObject.get(ProfessionSkillCostSchema.COLUMN_FIRST_COST).getAsShort(),
					skillCostObject.get(ProfessionSkillCostSchema.COLUMN_SECOND_COST).getAsShort());
			profession.getSkillCosts().put(newSkill, skillCost);
		}

		JsonArray assignableSkillCosts = jsonObject.getAsJsonArray(ProfessionSkillCategoryCostSchema.TABLE_NAME);
		for(int i = 0; i < assignableSkillCosts.size(); i ++ ) {
			final JsonObject skillRankObject = assignableSkillCosts.get(i).getAsJsonObject();
			SkillCategory newSkillCategory = skillCategoryDao
					.getById(skillRankObject.get(CharacterSkillRanksSchema.COLUMN_SKILL_ID).getAsInt());
			JsonArray skillCostsList = skillRankObject.getAsJsonArray(COLUMN_COST_ARRAY);
			List<SkillCost> skillCostList = new ArrayList<>(skillCostsList.size());
			for(int j = 0; j < skillCostsList.size(); j++) {
				JsonObject skillCostObject = skillCostsList.getAsJsonObject();
				SkillCost skillCost = new SkillCost(
						skillCostObject.get(ProfessionSkillCostSchema.COLUMN_FIRST_COST).getAsShort(),
						skillCostObject.get(ProfessionSkillCostSchema.COLUMN_SECOND_COST).getAsShort());
				skillCostList.add(skillCost);
			}
			profession.getAssignableSkillCosts().put(newSkillCategory, skillCostList);
		}

		JsonArray professionSkillCategories = jsonObject.getAsJsonArray(ProfessionalSkillCategoriesSchema.TABLE_NAME);
		for(int i = 0; i < professionSkillCategories.size(); i ++ ) {
			final JsonObject skillCategoryObject = professionSkillCategories.get(i).getAsJsonObject();
			SkillCategory skillCategory = skillCategoryDao.getById(
					skillCategoryObject.get(ProfessionalSkillCategoriesSchema.COLUMN_SKILL_CATEGORY_ID).getAsInt());
			profession.getProfessionalSkillCategories().add(skillCategory);
		}

		return profession;
	}
}
