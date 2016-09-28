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
package com.madinnovations.rmu.data.dao.common.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.schemas.SkillSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillStatsSchema;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Stat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Skill} objects in a SQLite database.
 */
@Singleton
public class SkillDaoDbImpl extends BaseDaoDbImpl<Skill> implements SkillDao, SkillSchema {
    private SkillCategoryDao skillCategoryDao;
	private StatDao statDao;

    /**
     * Creates a new instance of SkillDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public SkillDaoDbImpl(SQLiteOpenHelper helper, SkillCategoryDao skillCategoryDao, StatDao statDao) {
        super(helper);
        this.skillCategoryDao = skillCategoryDao;
		this.statDao = statDao;
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
	protected String getSortString() {
		return COLUMN_NAME;
	}

	@Override
    protected int getId(Skill instance) {
        return instance.getId();
    }

    @Override
    protected void setId(Skill instance, int id) {
        instance.setId(id);
    }

    @Override
    protected Skill cursorToEntity(@NonNull Cursor cursor) {
		return cursorToEntity(cursor, skillCategoryDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID))));
    }

    @Override
    protected ContentValues getContentValues(Skill instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(8);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(7);
		}
        values.put(COLUMN_NAME, instance.getName());
        values.put(COLUMN_DESCRIPTION, instance.getDescription());
        values.put(COLUMN_CATEGORY_ID, instance.getCategory().getId());
        values.put(COLUMN_REQUIRES_SPECIALIZATION, instance.isRequiresSpecialization());
        values.put(COLUMN_USE_CATEGORY_STATS, instance.isUseCategoryStats());
		values.put(COLUMN_REQUIRES_CONCENTRATION, instance.isRequiresConcentration());
		values.put(COLUMN_IS_LORE, instance.isLore());

        return values;
    }

    @Override
    protected boolean saveRelationships(SQLiteDatabase db, Skill instance) {
        boolean result = true;
        final String selectionArgs[] = { String.valueOf(instance.getId()) };
        final String selection = SkillStatsSchema.COLUMN_SKILL_ID + " = ?";

        db.delete(SkillStatsSchema.TABLE_NAME, selection, selectionArgs);

		if(instance.getStats() != null) {
			for (Stat stat : instance.getStats()) {
				result &= (db.insertWithOnConflict(SkillStatsSchema.TABLE_NAME, null,
												   getSkillStat(instance.getId(), stat.getId()),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

        return result;
    }

	@Override
	public List<Skill> getSpecializationSkills() {
		final String selectionArgs[] = { "1" };
		final String selection = COLUMN_REQUIRES_SPECIALIZATION + " = ?";
		List<Skill> list = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection, selectionArgs, COLUMN_NAME);

			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Skill instance = cursorToEntity(cursor);
					list.add(instance);
					cursor.moveToNext();
				}
				cursor.close();
			}
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}

		return list;
	}

	@Override
	public List<Skill> getNonSpecializationSkills() {
		final String selectionArgs[] = { "0" };
		final String selection = COLUMN_REQUIRES_SPECIALIZATION + " = ?";
		List<Skill> list = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection, selectionArgs, COLUMN_NAME);

			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Skill instance = cursorToEntity(cursor);
					list.add(instance);
					cursor.moveToNext();
				}
				cursor.close();
			}
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}

		return list;
	}

	@Override
	public List<Skill> getSkillsForCategory(@NonNull SkillCategory filter) {
		final String selectionArgs[] = { String.valueOf(filter.getId()) };
		final String selection = COLUMN_CATEGORY_ID + " = ?";
		List<Skill> list = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection, selectionArgs, getIdColumnName());

			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Skill instance = cursorToEntity(cursor, filter);

					list.add(instance);
					cursor.moveToNext();
				}
				cursor.close();
			}
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}

		return list;
	}

	protected Skill cursorToEntity(@NonNull Cursor cursor, SkillCategory skillCategory) {
		Skill instance = new Skill();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		instance.setCategory(skillCategory);
		instance.setRequiresSpecialization(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUIRES_SPECIALIZATION)) != 0);
		instance.setUseCategoryStats(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USE_CATEGORY_STATS)) != 0);
		instance.setRequiresConcentration(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REQUIRES_CONCENTRATION)) != 0);
		instance.setLore(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_LORE)) != 0);
		if(!instance.isUseCategoryStats()) {
			instance.setStats(getStats(instance.getId()));
		}
		else {
			instance.setStats(null);
		}

		return instance;
	}

	private ContentValues getSkillStat(int skillId, int statId) {
        ContentValues values = new ContentValues(3);

        values.put(SkillStatsSchema.COLUMN_SKILL_ID, skillId);
        values.put(SkillStatsSchema.COLUMN_STAT_ID, statId);

        return values;
    }

	private List<Stat> getStats(int skillId) {
		final String selectionArgs[] = { String.valueOf(skillId) };
		final String selection = SkillStatsSchema.COLUMN_SKILL_ID + " = ?";

		Cursor cursor = super.query(SkillStatsSchema.TABLE_NAME, SkillStatsSchema.COLUMNS, selection,
									selectionArgs, SkillStatsSchema.COLUMN_STAT_ID);
		List<Stat> list = new ArrayList<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int id = cursor.getInt(cursor.getColumnIndexOrThrow(SkillStatsSchema.COLUMN_STAT_ID));
			Stat instance = statDao.getById(id);
			if(instance != null) {
				list.add(instance);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}
}
