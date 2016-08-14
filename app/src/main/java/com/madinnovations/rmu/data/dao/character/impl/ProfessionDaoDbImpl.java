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
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSkillCostSchema;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.SkillCost;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Profession} objects in a SQLite database.
 */
@Singleton
public class ProfessionDaoDbImpl extends BaseDaoDbImpl<Profession> implements ProfessionDao, ProfessionSchema {
	private SkillCategoryDao skillCategoryDao;

    /**
     * Creates a new instance of ProfessionDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public ProfessionDaoDbImpl(SQLiteOpenHelper helper, SkillCategoryDao skillCategoryDao) {
        super(helper);
		this.skillCategoryDao = skillCategoryDao;
    }

	@Override
	public Profession getById(int id) {
		return super.getById(id);
	}

	@Override
	public boolean save(Profession instance) {
		return super.save(instance);
	}

	@Override
	public boolean deleteById(int id) {
		return super.deleteById(id);
	}

	@Override
	public int deleteAll() {
		return super.deleteAll();
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
		instance.setSkillCostMap(getSkillCostMap(instance.getId()));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(Profession instance) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
		return initialValues;
	}

	@Override
	protected boolean saveRelationships(SQLiteDatabase db, Profession instance) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		final String selection = ProfessionSkillCostSchema.COLUMN_PROFESSION_ID + " = ?";

		db.delete(ProfessionSkillCostSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<SkillCategory, SkillCost> entry : instance.getSkillCostMap().entrySet()) {
			result &= (db.insert(ProfessionSkillCostSchema.TABLE_NAME, null, getProfessionSkillCostContentValues(instance.getId(),
					entry.getKey(), entry.getValue())) != -1);
		}
		return result;
	}

	private ContentValues getProfessionSkillCostContentValues(int professionId, SkillCategory skillCategory, SkillCost skillCost) {
		ContentValues values = new ContentValues();
		values.put(ProfessionSkillCostSchema.COLUMN_PROFESSION_ID, professionId);
		values.put(ProfessionSkillCostSchema.COLUMN_SKILL_CATEGORY_ID, skillCategory.getId());
		values.put(ProfessionSkillCostSchema.COLUMN_FIRST_COST, skillCost.getFirstCost());
		values.put(ProfessionSkillCostSchema.COLUMN_SECOND_COST, skillCost.getAdditionalCost());
		return values;
	}

	private Map<SkillCategory, SkillCost> getSkillCostMap(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = ProfessionSkillCostSchema.COLUMN_PROFESSION_ID + " = ?";

		Cursor cursor = super.query(ProfessionSkillCostSchema.TABLE_NAME, ProfessionSkillCostSchema.COLUMNS, selection,
				selectionArgs, ProfessionSkillCostSchema.COLUMN_SKILL_CATEGORY_ID);
		Map<SkillCategory, SkillCost> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(ProfessionSkillCostSchema.COLUMN_SKILL_CATEGORY_ID));
			SkillCategory instance = skillCategoryDao.getById(mappedId);
			SkillCost skillCost = new SkillCost();
			skillCost.setFirstCost(cursor.getShort(cursor.getColumnIndexOrThrow(ProfessionSkillCostSchema.COLUMN_FIRST_COST)));
			skillCost.setAdditionalCost(cursor.getShort(cursor.getColumnIndexOrThrow(ProfessionSkillCostSchema.COLUMN_SECOND_COST)));
			if(instance != null) {
				map.put(instance, skillCost);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

}
