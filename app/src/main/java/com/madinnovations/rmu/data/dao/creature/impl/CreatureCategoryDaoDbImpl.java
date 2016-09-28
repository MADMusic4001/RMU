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
package com.madinnovations.rmu.data.dao.creature.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.creature.CreatureCategoryDao;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureCategorySchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureCategoryTalentsSchema;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.creature.CreatureCategory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link CreatureCategory} objects in a SQLite database.
 */
@Singleton
public class CreatureCategoryDaoDbImpl extends BaseDaoDbImpl<CreatureCategory>
		implements CreatureCategoryDao, CreatureCategorySchema {
	private TalentDao talentDao;

	/**
	 * Creates a new instance of CreatureCategoryDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 * @param talentDao  a {@link TalentDao} instance to use when reading/writing Talent references
	 */
	@Inject
	public CreatureCategoryDaoDbImpl(SQLiteOpenHelper helper, TalentDao talentDao) {
		super(helper);
		this.talentDao = talentDao;
	}

	@Override
	public CreatureCategory getById(int id) {
		return super.getById(id);
	}

	@Override
	public boolean save(CreatureCategory instance) {
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
	protected int getId(CreatureCategory instance) {
		return instance.getId();
	}

	@Override
	protected void setId(CreatureCategory instance, int id) {
		instance.setId(id);
	}

	@Override
	protected CreatureCategory cursorToEntity(@NonNull Cursor cursor) {
		CreatureCategory instance = new CreatureCategory();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		instance.setTalents(getTalents(instance.getId()));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(CreatureCategory instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(3);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(2);
		}
		values.put(COLUMN_NAME, instance.getName());
		values.put(COLUMN_DESCRIPTION, instance.getDescription());

		return values;
	}

	@Override
	protected boolean saveRelationships(SQLiteDatabase db, CreatureCategory instance) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		final String selection = CreatureCategoryTalentsSchema.COLUMN_CREATURE_CATEGORY_ID + " = ?";

		db.delete(CreatureCategoryTalentsSchema.TABLE_NAME, selection, selectionArgs);

		if(instance.getTalents() != null) {
			for (Talent talent : instance.getTalents()) {
				result &= (db.insertWithOnConflict(CreatureCategoryTalentsSchema.TABLE_NAME, null,
												   getTalentContentValues(instance.getId(), talent.getId()),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}
		return result;
	}

	private ContentValues getTalentContentValues(int creatureCategoryId, int talentId) {
		ContentValues values = new ContentValues(2);

		values.put(CreatureCategoryTalentsSchema.COLUMN_CREATURE_CATEGORY_ID, creatureCategoryId);
		values.put(CreatureCategoryTalentsSchema.COLUMN_TALENT_ID, talentId);

		return values;
	}

	private List<Talent> getTalents(int creatureCategoryId) {
		final String selectionArgs[] = { String.valueOf(creatureCategoryId) };
		final String selection = CreatureCategoryTalentsSchema.COLUMN_CREATURE_CATEGORY_ID + " = ?";

		Cursor cursor = super.query(CreatureCategoryTalentsSchema.TABLE_NAME, CreatureCategoryTalentsSchema.COLUMNS, selection,
									selectionArgs, CreatureCategoryTalentsSchema.COLUMN_TALENT_ID);
		List<Talent> list = new ArrayList<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int id = cursor.getInt(cursor.getColumnIndexOrThrow(CreatureCategoryTalentsSchema.COLUMN_TALENT_ID));
			Talent instance = talentDao.getById(id);
			if(instance != null) {
				list.add(instance);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}
}
