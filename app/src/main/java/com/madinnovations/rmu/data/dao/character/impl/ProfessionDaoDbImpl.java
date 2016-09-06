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
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.SkillCost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Profession} objects in a SQLite database.
 */
@Singleton
public class ProfessionDaoDbImpl extends BaseDaoDbImpl<Profession> implements ProfessionDao, ProfessionSchema {
	private SkillCategoryDao skillCategoryDao;
	private SkillDao skillDao;
	private RealmDao realmDao;

    /**
     * Creates a new instance of ProfessionDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public ProfessionDaoDbImpl(SQLiteOpenHelper helper, SkillCategoryDao skillCategoryDao, RealmDao realmDao, SkillDao skillDao) {
        super(helper);
		this.skillCategoryDao = skillCategoryDao;
		this.realmDao = realmDao;
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
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_REALM1_ID))) {
			instance.setRealm1(realmDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REALM1_ID))));
			if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_REALM2_ID))) {
				instance.setRealm2(realmDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REALM2_ID))));
			}
		}
		instance.setSkillCategoryCosts(getSkillCategoryCostMap(instance));
		instance.setSkillCosts(getSkillCostMap(instance));
		instance.setAssignableSkillCosts(getAssignableSkillCostMap(instance));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(Profession instance) {
		ContentValues initialValues = new ContentValues(4);

		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
		if(instance.getRealm1() != null) {
			initialValues.put(COLUMN_REALM1_ID, instance.getRealm1().getId());
		}
		else {
			initialValues.putNull(COLUMN_REALM1_ID);
		}
		if(instance.getRealm2() != null) {
			initialValues.put(COLUMN_REALM2_ID, instance.getRealm2().getId());
		}
		else {
			initialValues.putNull(COLUMN_REALM2_ID);
		}

		return initialValues;
	}

	@Override
	protected boolean deleteRelationships(SQLiteDatabase db, int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = ProfessionSkillCostSchema.COLUMN_PROFESSION_ID + " = ?";

		return db.delete(ProfessionSkillCostSchema.TABLE_NAME, selection, selectionArgs) >= 0;
	}

	@Override
	protected boolean saveRelationships(SQLiteDatabase db, Profession instance) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		String selection = ProfessionSkillCostSchema.COLUMN_PROFESSION_ID + " = ?";

		db.delete(ProfessionSkillCostSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<Skill, SkillCost> entry : instance.getSkillCosts().entrySet()) {
			result &= (db.insert(ProfessionSkillCostSchema.TABLE_NAME, null,
								 getProfessionSkillCostContentValues(instance.getId(), entry.getKey().getId(),
																	 entry.getValue())) != -1);
		}

		selection = ProfessionSkillCategoryCostSchema.COLUMN_PROFESSION_ID + " = ?";
		db.delete(ProfessionSkillCategoryCostSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<SkillCategory, SkillCost> entry : instance.getSkillCategoryCosts().entrySet()) {
			result &= (db.insert(ProfessionSkillCategoryCostSchema.TABLE_NAME, null,
								 getProfessionSkillCategoryCostContentValues(instance.getId(), entry.getKey().getId(),
																	 entry.getValue())) != -1);
		}

		selection = ProfessionAssignableSkillCostSchema.COLUMN_PROFESSION_ID + " = ?";
		db.delete(ProfessionAssignableSkillCostSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<SkillCategory, List<SkillCost>> entry : instance.getAssignableSkillCosts().entrySet()) {
			for(SkillCost skillCost : entry.getValue()) {
				result &= (db.insert(ProfessionAssignableSkillCostSchema.TABLE_NAME, null,
									 getProfessionAssignableCostContentValues(instance.getId(), entry.getKey().getId(),
																		 skillCost)) != -1);
			}
		}
		return result;
	}

	private ContentValues getProfessionSkillCostContentValues(int professionId, int skillId, SkillCost skillCost) {
		ContentValues values = new ContentValues(4);

		values.put(ProfessionSkillCostSchema.COLUMN_PROFESSION_ID, professionId);
		values.put(ProfessionSkillCostSchema.COLUMN_SKILL_ID, skillId);
		values.put(ProfessionSkillCostSchema.COLUMN_FIRST_COST, skillCost.getFirstCost());
		values.put(ProfessionSkillCostSchema.COLUMN_SECOND_COST, skillCost.getAdditionalCost());

		return values;
	}

	private ContentValues getProfessionSkillCategoryCostContentValues(int professionId, int skillCategoryId, SkillCost
			skillCost) {
		ContentValues values = new ContentValues(4);

		values.put(ProfessionSkillCategoryCostSchema.COLUMN_PROFESSION_ID, professionId);
		values.put(ProfessionSkillCategoryCostSchema.COLUMN_SKILL_CATEGORY_ID, skillCategoryId);
		values.put(ProfessionSkillCategoryCostSchema.COLUMN_FIRST_COST, skillCost.getFirstCost());
		values.put(ProfessionSkillCategoryCostSchema.COLUMN_SECOND_COST, skillCost.getAdditionalCost());

		return values;
	}

	private ContentValues getProfessionAssignableCostContentValues(int professionId, int skillCategoryId, SkillCost
			skillCost) {
		ContentValues values = new ContentValues(4);

		values.put(ProfessionAssignableSkillCostSchema.COLUMN_PROFESSION_ID, professionId);
		values.put(ProfessionAssignableSkillCostSchema.COLUMN_SKILL_CATEGORY_ID, skillCategoryId);
		values.put(ProfessionAssignableSkillCostSchema.COLUMN_FIRST_COST, skillCost.getFirstCost());
		values.put(ProfessionAssignableSkillCostSchema.COLUMN_SECOND_COST, skillCost.getAdditionalCost());

		return values;
	}

	private Map<Skill, SkillCost> getSkillCostMap(Profession profession) {
		final String selectionArgs[] = { String.valueOf(profession.getId()) };
		final String selection = ProfessionSkillCostSchema.COLUMN_PROFESSION_ID + " = ?";

		Cursor cursor = super.query(ProfessionSkillCostSchema.TABLE_NAME, ProfessionSkillCostSchema.COLUMNS, selection,
				selectionArgs, ProfessionSkillCostSchema.COLUMN_SKILL_ID);
		Map<Skill, SkillCost> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int skillId = cursor.getInt(cursor.getColumnIndexOrThrow(ProfessionSkillCostSchema.COLUMN_SKILL_ID));
			Skill skill = skillDao.getById(skillId);
			SkillCost skillCost = new SkillCost();
			skillCost.setFirstCost(cursor.getShort(cursor.getColumnIndexOrThrow(ProfessionSkillCostSchema.COLUMN_FIRST_COST)));
			skillCost.setAdditionalCost(cursor.getShort(cursor.getColumnIndexOrThrow(ProfessionSkillCostSchema.COLUMN_SECOND_COST)));
			if(skill != null) {
				map.put(skill, skillCost);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private Map<SkillCategory, SkillCost> getSkillCategoryCostMap(Profession profession) {
		final String selectionArgs[] = { String.valueOf(profession.getId()) };
		final String selection = ProfessionSkillCategoryCostSchema.COLUMN_PROFESSION_ID + " = ?";

		Cursor cursor = super.query(ProfessionSkillCategoryCostSchema.TABLE_NAME, ProfessionSkillCategoryCostSchema.COLUMNS,
									selection, selectionArgs, ProfessionSkillCategoryCostSchema.COLUMN_SKILL_CATEGORY_ID);
		Map<SkillCategory, SkillCost> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int skillCategoryId = cursor.getInt(cursor.getColumnIndexOrThrow(
					ProfessionSkillCategoryCostSchema.COLUMN_SKILL_CATEGORY_ID));
			SkillCategory skillCategory = skillCategoryDao.getById(skillCategoryId);
			SkillCost skillCost = new SkillCost();
			skillCost.setFirstCost(cursor.getShort(cursor.getColumnIndexOrThrow(
					ProfessionSkillCategoryCostSchema.COLUMN_FIRST_COST)));
			skillCost.setAdditionalCost(cursor.getShort(cursor.getColumnIndexOrThrow(
					ProfessionSkillCategoryCostSchema.COLUMN_SECOND_COST)));
			if(skillCategory != null) {
				map.put(skillCategory, skillCost);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private Map<SkillCategory, List<SkillCost>> getAssignableSkillCostMap(Profession profession) {
		final String selectionArgs[] = { String.valueOf(profession.getId()) };
		final String selection = ProfessionAssignableSkillCostSchema.COLUMN_PROFESSION_ID + " = ?";

		Cursor cursor = super.query(ProfessionAssignableSkillCostSchema.TABLE_NAME, ProfessionAssignableSkillCostSchema.COLUMNS,
									selection, selectionArgs, ProfessionAssignableSkillCostSchema.COLUMN_SKILL_CATEGORY_ID);
		Map<SkillCategory, List<SkillCost>> map = new HashMap<>();
		List<SkillCost> skillCostList = new ArrayList<>();
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
			SkillCost skillCost = new SkillCost();
			skillCost.setFirstCost(cursor.getShort(cursor.getColumnIndexOrThrow(
					ProfessionAssignableSkillCostSchema.COLUMN_FIRST_COST)));
			skillCost.setAdditionalCost(cursor.getShort(cursor.getColumnIndexOrThrow(
					ProfessionAssignableSkillCostSchema.COLUMN_SECOND_COST)));
			skillCostList.add(skillCost);
			cursor.moveToNext();
		}
		cursor.close();
		if(skillCategory != null) {
			map.put(skillCategory, skillCostList);
		}

		return map;
	}
}
