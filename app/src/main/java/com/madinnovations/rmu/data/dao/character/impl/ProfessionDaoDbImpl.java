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
package com.madinnovations.rmu.data.dao.character.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.ProfessionDao;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionAssignableSkillCostSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSkillCategoryCostSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSkillCostSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionalSkillCategoriesSchema;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.common.DevelopmentCostGroup;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.spells.Realm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Profession} objects in a SQLite database.
 */
@Singleton
public class ProfessionDaoDbImpl extends BaseDaoDbImpl<Profession> implements ProfessionDao, ProfessionSchema {
	private SkillCategoryDao skillCategoryDao;
	private SkillDao skillDao;

    /**
     * Creates a new instance of ProfessionDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public ProfessionDaoDbImpl(SQLiteOpenHelper helper, SkillCategoryDao skillCategoryDao, SkillDao skillDao) {
        super(helper);
		this.skillCategoryDao = skillCategoryDao;
		this.skillDao = skillDao;
    }

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumns() {
		return COLUMNS;
	}

	@Override
	protected String getIdColumnName() {
		return COLUMN_ID;
	}

	@Override
	protected int getId(Profession instance) {
		return instance.getId();
	}

	@Override
	protected void setId(Profession instance, int id) {
		instance.setId(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Profession cursorToEntity(@NonNull Cursor cursor) {
		Profession instance = new Profession();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_REALM1))) {
			instance.setRealm1(Realm.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REALM1))));
			if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_REALM2))) {
				instance.setRealm2(Realm.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REALM2))));
			}
		}
		instance.setSkillCategoryCosts(getSkillCategoryCostMap(instance));
		instance.setSkillCosts(getSkillCostMap(instance));
		instance.setAssignableSkillCostsMap(getAssignableSkillCostMap(instance));
		instance.setProfessionalSkillCategories(getProfessionalSkillCategories(instance));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(Profession instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(5);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(4);
		}
		values.put(COLUMN_NAME, instance.getName());
		values.put(COLUMN_DESCRIPTION, instance.getDescription());
		if(instance.getRealm1() != null) {
			values.put(COLUMN_REALM1, instance.getRealm1().name());
		}
		else {
			values.putNull(COLUMN_REALM1);
		}
		if(instance.getRealm2() != null) {
			values.put(COLUMN_REALM2, instance.getRealm2().name());
		}
		else {
			values.putNull(COLUMN_REALM2);
		}

		return values;
	}

	@Override
	protected boolean deleteRelationships(SQLiteDatabase db, int id) {
		boolean result;
		final String selectionArgs[] = { String.valueOf(id) };

		String selection = ProfessionSkillCostSchema.COLUMN_PROFESSION_ID + " = ?";
		result = db.delete(ProfessionSkillCostSchema.TABLE_NAME, selection, selectionArgs) >= 0;

		selection = ProfessionSkillCategoryCostSchema.COLUMN_PROFESSION_ID + " = ?";
		result &= db.delete(ProfessionSkillCategoryCostSchema.TABLE_NAME, selection, selectionArgs) >= 0;

		selection = ProfessionAssignableSkillCostSchema.COLUMN_PROFESSION_ID + " = ?";
		result &= db.delete(ProfessionAssignableSkillCostSchema.TABLE_NAME, selection, selectionArgs) >= 0;

		return result;
	}

	@Override
	protected boolean saveRelationships(SQLiteDatabase db, Profession instance) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		String selection = ProfessionSkillCostSchema.COLUMN_PROFESSION_ID + " = ?";

		db.delete(ProfessionSkillCostSchema.TABLE_NAME, selection, selectionArgs);
		for(Map.Entry<Skill, DevelopmentCostGroup> entry : instance.getSkillCosts().entrySet()) {
			result &= (db.insertWithOnConflict(ProfessionSkillCostSchema.TABLE_NAME, null,
											   getProfessionSkillCostContentValues(instance.getId(), entry.getKey().getId(),
																				   entry.getValue()),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}

		selection = ProfessionSkillCategoryCostSchema.COLUMN_PROFESSION_ID + " = ?";
		db.delete(ProfessionSkillCategoryCostSchema.TABLE_NAME, selection, selectionArgs);
		for(Map.Entry<SkillCategory, DevelopmentCostGroup> entry : instance.getSkillCategoryCosts().entrySet()) {
			result &= (db.insertWithOnConflict(ProfessionSkillCategoryCostSchema.TABLE_NAME, null,
											   getProfessionSkillCategoryCostContentValues(instance.getId(),
																						   entry.getKey().getId(),
																						   entry.getValue()),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}

		selection = ProfessionAssignableSkillCostSchema.COLUMN_PROFESSION_ID + " = ?";
		db.delete(ProfessionAssignableSkillCostSchema.TABLE_NAME, selection, selectionArgs);
		for(Map.Entry<SkillCategory, List<DevelopmentCostGroup>> entry : instance.getAssignableSkillCostsMap().entrySet()) {
			for(DevelopmentCostGroup costGroup : entry.getValue()) {
				result &= (db.insertWithOnConflict(ProfessionAssignableSkillCostSchema.TABLE_NAME, null,
												   getProfessionAssignableCostContentValues(instance.getId(),
																							entry.getKey().getId(), costGroup),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

		selection = ProfessionalSkillCategoriesSchema.COLUMN_PROFESSION_ID + " = ?";
		db.delete(ProfessionalSkillCategoriesSchema.TABLE_NAME, selection, selectionArgs);
		for(SkillCategory skillCategory : instance.getProfessionalSkillCategories()) {
			result &= (db.insertWithOnConflict(ProfessionalSkillCategoriesSchema.TABLE_NAME, null,
											   getProfessionalSkillCategoryContentValues(instance.getId(),
																						skillCategory.getId()),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}

		return result;
	}

	private ContentValues getProfessionSkillCostContentValues(int professionId, int skillId, DevelopmentCostGroup costGroup) {
		ContentValues values = new ContentValues(3);

		values.put(ProfessionSkillCostSchema.COLUMN_PROFESSION_ID, professionId);
		values.put(ProfessionSkillCostSchema.COLUMN_SKILL_ID, skillId);
		if(costGroup == null) {
			values.put(ProfessionSkillCostSchema.COLUMN_COST_GROUP_NAME, DevelopmentCostGroup.NONE.name());
		}
		else {
			values.put(ProfessionSkillCostSchema.COLUMN_COST_GROUP_NAME, costGroup.name());
		}

		return values;
	}

	private ContentValues getProfessionSkillCategoryCostContentValues(int professionId, int skillCategoryId, DevelopmentCostGroup
			costGroup) {
		ContentValues values = new ContentValues(3);

		values.put(ProfessionSkillCategoryCostSchema.COLUMN_PROFESSION_ID, professionId);
		values.put(ProfessionSkillCategoryCostSchema.COLUMN_SKILL_CATEGORY_ID, skillCategoryId);
		if(costGroup == null) {
			values.put(ProfessionSkillCategoryCostSchema.COLUMN_COST_GROUP_NAME, DevelopmentCostGroup.NONE.name());
		}
		else {
			values.put(ProfessionSkillCategoryCostSchema.COLUMN_COST_GROUP_NAME, costGroup.name());
		}

		return values;
	}

	private ContentValues getProfessionAssignableCostContentValues(int professionId, int skillCategoryId, DevelopmentCostGroup
			costGroup) {
		ContentValues values = new ContentValues(3);

		values.put(ProfessionAssignableSkillCostSchema.COLUMN_PROFESSION_ID, professionId);
		values.put(ProfessionAssignableSkillCostSchema.COLUMN_SKILL_CATEGORY_ID, skillCategoryId);
		if(costGroup == null) {
			values.put(ProfessionAssignableSkillCostSchema.COLUMN_COST_GROUP_NAME, DevelopmentCostGroup.NONE.name());
		}
		else {
			values.put(ProfessionAssignableSkillCostSchema.COLUMN_COST_GROUP_NAME, costGroup.name());
		}

		return values;
	}

	private ContentValues getProfessionalSkillCategoryContentValues(int professionId, int skillCategoryId) {
		ContentValues values = new ContentValues(2);

		values.put(ProfessionalSkillCategoriesSchema.COLUMN_PROFESSION_ID, professionId);
		values.put(ProfessionalSkillCategoriesSchema.COLUMN_SKILL_CATEGORY_ID, skillCategoryId);

		return values;
	}

	private Map<Skill, DevelopmentCostGroup> getSkillCostMap(Profession profession) {
		final String selectionArgs[] = { String.valueOf(profession.getId()) };
		final String selection = ProfessionSkillCostSchema.COLUMN_PROFESSION_ID + " = ?";

		Cursor cursor = super.query(ProfessionSkillCostSchema.TABLE_NAME, ProfessionSkillCostSchema.COLUMNS, selection,
				selectionArgs, ProfessionSkillCostSchema.COLUMN_SKILL_ID);
		Map<Skill, DevelopmentCostGroup> map = new TreeMap<>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int skillId = cursor.getInt(cursor.getColumnIndexOrThrow(ProfessionSkillCostSchema.COLUMN_SKILL_ID));
			Skill skill = skillDao.getById(skillId);
			DevelopmentCostGroup costGroup = DevelopmentCostGroup.valueOf(cursor.getString(
					cursor.getColumnIndexOrThrow(ProfessionSkillCostSchema.COLUMN_COST_GROUP_NAME)));
			if(skill != null) {
				map.put(skill, costGroup);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private Map<SkillCategory, DevelopmentCostGroup> getSkillCategoryCostMap(Profession profession) {
		final String selectionArgs[] = { String.valueOf(profession.getId()) };
		final String selection = ProfessionSkillCategoryCostSchema.COLUMN_PROFESSION_ID + " = ?";

		Cursor cursor = super.query(ProfessionSkillCategoryCostSchema.TABLE_NAME, ProfessionSkillCategoryCostSchema.COLUMNS,
									selection, selectionArgs, ProfessionSkillCategoryCostSchema.COLUMN_SKILL_CATEGORY_ID);
		Map<SkillCategory, DevelopmentCostGroup> map = new TreeMap<>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int skillCategoryId = cursor.getInt(cursor.getColumnIndexOrThrow(
					ProfessionSkillCategoryCostSchema.COLUMN_SKILL_CATEGORY_ID));
			SkillCategory skillCategory = skillCategoryDao.getById(skillCategoryId);
			DevelopmentCostGroup costGroup = DevelopmentCostGroup.valueOf(cursor.getString(
					cursor.getColumnIndexOrThrow(ProfessionSkillCategoryCostSchema.COLUMN_COST_GROUP_NAME)));
			if(skillCategory != null) {
				map.put(skillCategory, costGroup);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private Map<SkillCategory, List<DevelopmentCostGroup>> getAssignableSkillCostMap(Profession profession) {
		final String selectionArgs[] = { String.valueOf(profession.getId()) };
		final String selection = ProfessionAssignableSkillCostSchema.COLUMN_PROFESSION_ID + " = ?";

		Cursor cursor = super.query(ProfessionAssignableSkillCostSchema.TABLE_NAME, ProfessionAssignableSkillCostSchema.COLUMNS,
									selection, selectionArgs, ProfessionAssignableSkillCostSchema.COLUMN_SKILL_CATEGORY_ID);
		Map<SkillCategory, List<DevelopmentCostGroup>> map = new TreeMap<>();
		List<DevelopmentCostGroup> skillCostList = new ArrayList<>();
		SkillCategory skillCategory = null;
		cursor.moveToFirst();
		int lastId = -1;
		while (!cursor.isAfterLast()) {
			int skillCategoryId = cursor.getInt(cursor.getColumnIndexOrThrow(
					ProfessionAssignableSkillCostSchema.COLUMN_SKILL_CATEGORY_ID));
			if(skillCategoryId != lastId) {
				if(lastId != -1) {
					map.put(skillCategory, skillCostList);
				}
				skillCostList = new ArrayList<>();
			}
			lastId = skillCategoryId;
			skillCategory = skillCategoryDao.getById(skillCategoryId);
			DevelopmentCostGroup costGroup = DevelopmentCostGroup.valueOf(cursor.getString(
					cursor.getColumnIndexOrThrow(ProfessionAssignableSkillCostSchema.COLUMN_COST_GROUP_NAME)));
			skillCostList.add(costGroup);
			cursor.moveToNext();
		}
		cursor.close();
		if(skillCategory != null) {
			map.put(skillCategory, skillCostList);
		}

		return map;
	}

	private List<SkillCategory> getProfessionalSkillCategories(Profession profession) {
		final String selectionArgs[] = { String.valueOf(profession.getId()) };
		final String selection = ProfessionalSkillCategoriesSchema.COLUMN_PROFESSION_ID + " = ?";

		Cursor cursor = super.query(ProfessionalSkillCategoriesSchema.TABLE_NAME, ProfessionalSkillCategoriesSchema.COLUMNS,
									selection, selectionArgs, ProfessionalSkillCategoriesSchema.COLUMN_SKILL_CATEGORY_ID);
		List<SkillCategory> list = new ArrayList<>(cursor.getCount());
		SkillCategory skillCategory;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int skillCategoryId = cursor.getInt(cursor.getColumnIndexOrThrow(
					ProfessionalSkillCategoriesSchema.COLUMN_SKILL_CATEGORY_ID));
			skillCategory = skillCategoryDao.getById(skillCategoryId);
			list.add(skillCategory);
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}
}
