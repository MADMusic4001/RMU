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
import android.util.Log;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCategoryStatsSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SpecializationSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SpecializationStatsSchema;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Statistic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Specialization} objects in a SQLite database.
 */
@Singleton
public class SpecializationDaoDbImpl extends BaseDaoDbImpl<Specialization> implements SpecializationDao, SpecializationSchema {
	private static final String  TAG = "SpecializationDaoImp";
    private SkillDao skillDao;

    /**
     * Creates a new instance of SpecializationDaoDbImpl
     *
	 * @param skillDao  a {@link SkillDao} instance
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public SpecializationDaoDbImpl(SQLiteOpenHelper helper, SkillDao skillDao) {
        super(helper);
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
    protected int getId(Specialization instance) {
        return instance.getId();
    }

    @Override
    protected void setId(Specialization instance, int id) {
        instance.setId(id);
    }

    @Override
    protected Specialization cursorToEntity(@NonNull Cursor cursor) {
        return cursorToEntity(cursor, skillDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SKILL_ID))));
    }

    @Override
    protected ContentValues getContentValues(Specialization instance) {
        ContentValues values;

        if(instance.getId() != -1) {
            values = new ContentValues(6);
            values.put(COLUMN_ID, instance.getId());
        }
        else {
            values = new ContentValues(5);
        }
        values.put(COLUMN_NAME, instance.getName());
        values.put(COLUMN_DESCRIPTION, instance.getDescription());
        values.put(COLUMN_SKILL_ID, instance.getSkill().getId());
        values.put(COLUMN_SKILL_STATS, instance.isUseSkillStats());
        values.put(COLUMN_CREATURE_ONLY, instance.isCreatureOnly());

        return values;
    }

    @Override
    protected boolean saveRelationships(SQLiteDatabase db, Specialization instance) {
        boolean result = true;
        final String selectionArgs[] = { String.valueOf(instance.getId()) };
        final String selection = SpecializationStatsSchema.COLUMN_SPECIALIZATION_ID + " = ?";

        db.delete(SpecializationStatsSchema.TABLE_NAME, selection, selectionArgs);

        if(instance.getStats() != null){
            for (Statistic stat : instance.getStats()) {
                result &= (db.insertWithOnConflict(SpecializationStatsSchema.TABLE_NAME, null,
												   getSpecializationStat(instance.getId(), stat),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
            }
        }
        return result;
    }

	@Override
	protected boolean deleteRelationships(SQLiteDatabase db, int id) {
		boolean result;
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = SpecializationStatsSchema.COLUMN_SPECIALIZATION_ID + " = ?";

		int numDeleted = db.delete(SpecializationStatsSchema.TABLE_NAME, selection, selectionArgs);
		Log.d(TAG, "Deleted " + numDeleted + " relationships.");
		result = (numDeleted >= 0);

		return result;
	}

	@Override
    public List<Specialization> getSpecializationsForSkill(@NonNull Skill filter) {
        final String selectionArgs[] = { String.valueOf(filter.getId()) };
        final String selection = COLUMN_SKILL_ID + " = ?";
        List<Specialization> list = new ArrayList<>();

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
                    Specialization instance = cursorToEntity(cursor, filter);

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
	public List<Specialization> getCharacterSpecializationsForSkill(@NonNull Skill filter) {
		final String selectionArgs[] = { String.valueOf(filter.getId()), "0" };
		final String selection = COLUMN_SKILL_ID + " = ? AND " + COLUMN_CREATURE_ONLY + " = ?";
		List<Specialization> list = new ArrayList<>();

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
					Specialization instance = cursorToEntity(cursor, filter);

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
	public Collection<Specialization> getCharacterPurchasableSpecializations() {
		final String selectionArgs[] = {"0"};
		final String selection = COLUMN_CREATURE_ONLY + " = ?";
		Collection<Specialization> list = new ArrayList<>();

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
					Specialization instance = cursorToEntity(cursor);

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

	private List<Statistic> getStats(int specializationId) {
        final String selectionArgs[] = { String.valueOf(specializationId) };
        final String selection = SpecializationStatsSchema.COLUMN_SPECIALIZATION_ID + " = ?";

        Cursor cursor = super.query(SpecializationStatsSchema.TABLE_NAME, SpecializationStatsSchema.COLUMNS, selection,
                selectionArgs, SpecializationStatsSchema.COLUMN_STAT_NAME);
        List<Statistic> list = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
			list.add(Statistic.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SkillCategoryStatsSchema.COLUMN_STAT_NAME))));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    private ContentValues getSpecializationStat(int specializationId, Statistic stat) {
        ContentValues values = new ContentValues(2);

        values.put(SpecializationStatsSchema.COLUMN_SPECIALIZATION_ID, specializationId);
        values.put(SpecializationStatsSchema.COLUMN_STAT_NAME, stat.name());

        return values;
    }

    private Specialization cursorToEntity(@NonNull Cursor cursor, @NonNull Skill filterSkill) {
        Specialization instance = new Specialization();

        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
        instance.setSkill(filterSkill);
        instance.setUseSkillStats(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SKILL_STATS)) != 0);
		instance.setCreatureOnly(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CREATURE_ONLY)) != 0);
        if(!instance.isUseSkillStats()) {
            instance.setStats(getStats(instance.getId()));
        }

        return instance;
    }
}
