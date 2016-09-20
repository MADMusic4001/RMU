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
import com.madinnovations.rmu.data.dao.creature.CreatureTypeDao;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTypeSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTypeTalentsSchema;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.creature.CreatureType;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link CreatureType} objects in a SQLite database.
 */
@Singleton
public class CreatureTypeDaoDbImpl extends BaseDaoDbImpl<CreatureType> implements CreatureTypeDao, CreatureTypeSchema {
	private CreatureCategoryDao creatureCategoryDao;
	private TalentDao talentDao;

	/**
	 * Creates a new instance of CreatureTypeDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 * @param creatureCategoryDao  a {@link CreatureCategoryDao} instance to read/write CreatureCategory references
	 * @param talentDao  a {@link TalentDao} instance to read/write Talent references
	 */
	@Inject
	public CreatureTypeDaoDbImpl(SQLiteOpenHelper helper, CreatureCategoryDao creatureCategoryDao, TalentDao talentDao) {
		super(helper);
		this.creatureCategoryDao = creatureCategoryDao;
		this.talentDao = talentDao;
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
	protected int getId(CreatureType instance) {
		return instance.getId();
	}

	@Override
	protected void setId(CreatureType instance, int id) {
		instance.setId(id);
	}

	@Override
	protected CreatureType cursorToEntity(@NonNull Cursor cursor) {
		CreatureType instance = new CreatureType();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		instance.setCategory(creatureCategoryDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID))));
		instance.setTalents(getTalents(instance.getId()));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(CreatureType instance) {
		ContentValues initialValues = new ContentValues(4);

		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
		initialValues.put(COLUMN_CATEGORY_ID, instance.getCategory().getId());

		return initialValues;
	}

	@Override
	protected boolean saveRelationships(SQLiteDatabase db, CreatureType instance) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		final String selection = CreatureTypeTalentsSchema.COLUMN_CREATURE_TYPE_ID + " = ?";

		db.delete(CreatureTypeTalentsSchema.TABLE_NAME, selection, selectionArgs);

		if(instance.getTalents() != null) {
			for (Talent talent : instance.getTalents()) {
				result &= (db.insertWithOnConflict(CreatureTypeTalentsSchema.TABLE_NAME, null,
												   getTalentContentValues(instance.getId(), talent.getId()),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}
		return result;
	}

	private ContentValues getTalentContentValues(int creatureCategoryId, int talentId) {
		ContentValues values = new ContentValues(2);

		values.put(CreatureTypeTalentsSchema.COLUMN_CREATURE_TYPE_ID, creatureCategoryId);
		values.put(CreatureTypeTalentsSchema.COLUMN_TALENT_ID, talentId);

		return values;
	}

	private List<Talent> getTalents(int creatureCategoryId) {
		final String selectionArgs[] = { String.valueOf(creatureCategoryId) };
		final String selection = CreatureTypeTalentsSchema.COLUMN_CREATURE_TYPE_ID + " = ?";

		Cursor cursor = super.query(CreatureTypeTalentsSchema.TABLE_NAME, CreatureTypeTalentsSchema.COLUMNS, selection,
									selectionArgs, CreatureTypeTalentsSchema.COLUMN_TALENT_ID);
		List<Talent> list = new ArrayList<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int id = cursor.getInt(cursor.getColumnIndexOrThrow(CreatureTypeTalentsSchema.COLUMN_TALENT_ID));
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
