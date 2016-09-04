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
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.character.ProfessionSkillCategoryCost;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.SkillCost;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Profession} objects in a SQLite database.
 */
@Singleton
public class ProfessionDaoDbImpl extends BaseDaoDbImpl<Profession> implements ProfessionDao, ProfessionSchema {
	private SkillCategoryDao skillCategoryDao;
	private RealmDao realmDao;

    /**
     * Creates a new instance of ProfessionDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public ProfessionDaoDbImpl(SQLiteOpenHelper helper, SkillCategoryDao skillCategoryDao, RealmDao realmDao) {
        super(helper);
		this.skillCategoryDao = skillCategoryDao;
		this.realmDao = realmDao;
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
		instance.setSkillCategoryCosts(getSkillCostMap(instance));

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
		final String selection = ProfessionSkillCostSchema.COLUMN_PROFESSION_ID + " = ?";

		db.delete(ProfessionSkillCostSchema.TABLE_NAME, selection, selectionArgs);

		for(ProfessionSkillCategoryCost skillCategoryCost : instance.getSkillCategoryCosts()) {
			result &= (db.insert(ProfessionSkillCostSchema.TABLE_NAME, null, getProfessionSkillCostContentValues(instance.getId(),
					skillCategoryCost.getSkillCategory().getId(), skillCategoryCost.getSkillCost())) != -1);
		}
		return result;
	}

	@Override
	public boolean saveSkillCategoryCost(ProfessionSkillCategoryCost instance) {
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		final String selection = ProfessionSkillCostSchema.COLUMN_ID + " = ?";
		ContentValues contentValues = getProfessionSkillCostContentValues(instance.getProfession().getId(),
				instance.getSkillCategory().getId(), instance.getSkillCost());
		boolean result;

		SQLiteDatabase db = helper.getWritableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			if(instance.getId() == -1) {
				instance.setId((int)db.insert(ProfessionSkillCostSchema.TABLE_NAME, null, contentValues));
				result = (instance.getId() != -1);
			}
			else {
				int count = db.update(ProfessionSkillCostSchema.TABLE_NAME, contentValues, selection, selectionArgs);
				result = (count == 1);
			}
			if(result && newTransaction) {
				db.setTransactionSuccessful();
			}
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}
		return true;
	}

	private ContentValues getProfessionSkillCostContentValues(int professionId, int skillCategoryId, SkillCost skillCost) {
		ContentValues values = new ContentValues(4);

		values.put(ProfessionSkillCostSchema.COLUMN_PROFESSION_ID, professionId);
		values.put(ProfessionSkillCostSchema.COLUMN_SKILL_CATEGORY_ID, skillCategoryId);
		values.put(ProfessionSkillCostSchema.COLUMN_FIRST_COST, skillCost.getFirstCost());
		values.put(ProfessionSkillCostSchema.COLUMN_SECOND_COST, skillCost.getAdditionalCost());

		return values;
	}

	private List<ProfessionSkillCategoryCost> getSkillCostMap(Profession profession) {
		final String selectionArgs[] = { String.valueOf(profession.getId()) };
		final String selection = ProfessionSkillCostSchema.COLUMN_PROFESSION_ID + " = ?";

		Cursor cursor = super.query(ProfessionSkillCostSchema.TABLE_NAME, ProfessionSkillCostSchema.COLUMNS, selection,
				selectionArgs, ProfessionSkillCostSchema.COLUMN_SKILL_CATEGORY_ID);
		List<ProfessionSkillCategoryCost> list = new ArrayList<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int id = cursor.getInt(cursor.getColumnIndexOrThrow(ProfessionSkillCostSchema.COLUMN_ID));
			int skillCategoryId = cursor.getInt(cursor.getColumnIndexOrThrow(ProfessionSkillCostSchema.COLUMN_SKILL_CATEGORY_ID));
			SkillCategory skillCategory = skillCategoryDao.getById(skillCategoryId);
			SkillCost skillCost = new SkillCost();
			skillCost.setFirstCost(cursor.getShort(cursor.getColumnIndexOrThrow(ProfessionSkillCostSchema.COLUMN_FIRST_COST)));
			skillCost.setAdditionalCost(cursor.getShort(cursor.getColumnIndexOrThrow(ProfessionSkillCostSchema.COLUMN_SECOND_COST)));
			if(skillCategory != null) {
				list.add(new ProfessionSkillCategoryCost(id, profession, skillCategory, skillCost));
			}
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}

}
