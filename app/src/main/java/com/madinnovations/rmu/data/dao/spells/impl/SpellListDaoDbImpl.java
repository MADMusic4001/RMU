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
package com.madinnovations.rmu.data.dao.spells.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.ProfessionDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.spells.SpellListDao;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellListSchema;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.data.entities.spells.SpellList;
import com.madinnovations.rmu.data.entities.spells.SpellListType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link SpellList} objects in a SQLite database.
 */
@Singleton
public class SpellListDaoDbImpl extends BaseDaoDbImpl<SpellList> implements SpellListDao, SpellListSchema {
	@SuppressWarnings("unused")
	private static final String TAG = "SpellListDaoDbImpl";
	private ProfessionDao professionDao;
	private SkillDao skillDao;

	/**
	 * Creates a new instance of SpellListDaoImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 * @param professionDao  a {@link ProfessionDao} instance
	 * @param skillDao  a {@link SkillDao} instance
	 */
	@Inject
	public SpellListDaoDbImpl(@NonNull SQLiteOpenHelper helper, @NonNull ProfessionDao professionDao, @NonNull SkillDao skillDao) {
		super(helper);
		this.professionDao = professionDao;
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
	protected int getId(SpellList instance) {
		return instance.getId();
	}

	@Override
	protected void setId(SpellList instance, int id) {
		instance.setId(id);
	}

	@Override
	protected SpellList cursorToEntity(@NonNull Cursor cursor) {
		SpellList instance = new SpellList();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		if(cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_NOTES))) {
			instance.setNotes(null);
		}
		else {
			instance.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES)));
		}
		instance.setRealm(Realm.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REALM))));
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_REALM2))) {
			instance.setRealm2(Realm.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REALM2))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_PROFESSION_ID))) {
			instance.setProfession(professionDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROFESSION_ID))));
		}
		instance.setSpellListType(SpellListType.valueOf(
				cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SPELL_LIST_TYPE_NAME))));
		instance.setSkill(skillDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SKILL_ID))));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(@NonNull SpellList instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(8);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(7);
		}
		values.put(COLUMN_NAME, instance.getName());
		if(instance.getNotes() == null) {
			values.putNull(COLUMN_NOTES);
		}
		else {
			values.put(COLUMN_NOTES, instance.getNotes());
		}
		values.put(COLUMN_REALM, instance.getRealm().name());
		if(instance.getRealm2() == null ) {
			values.putNull(COLUMN_REALM2);
		}
		else {
			values.put(COLUMN_REALM2, instance.getRealm2().name());
		}
		if(instance.getProfession() == null) {
			values.putNull(COLUMN_PROFESSION_ID);
		}
		else {
			values.put(COLUMN_PROFESSION_ID, instance.getProfession().getId());
		}
		values.put(COLUMN_SPELL_LIST_TYPE_NAME, instance.getSpellListType().name());
		values.put(COLUMN_SKILL_ID, instance.getSkill().getId());

		return values;
	}

	@Override
	public Collection<SpellList> getAllListsForCharacter(Character character) {
		String selectionArgs[];
		final StringBuilder selection = new StringBuilder();
		int index = 0;
		if(character.getRealm2() != null) {
			if(character.getProfession() != null) {
				selectionArgs = new String[3];
			}
			else {
				selectionArgs = new String[2];
			}
			selectionArgs[index++] = character.getRealm().name();
			selectionArgs[index++] = character.getRealm2().name();
			selection.append(COLUMN_REALM + " in (?, ?)");
		}
		else {
			if(character.getProfession() != null) {
				selectionArgs = new String[2];
			}
			else {
				selectionArgs = new String[1];
			}
			if(character.getRealm() != null) {
				selectionArgs[index++] = character.getRealm().name();
			}
			else {
				selectionArgs[index++] = "1";
			}
			selection.append(COLUMN_REALM + " = ?");
		}
		if(character.getProfession() != null) {
			selectionArgs[index] = String.valueOf(character.getProfession().getId());
			selection.append(" and (" + COLUMN_PROFESSION_ID + " is null or " + COLUMN_PROFESSION_ID + " = ?)");
		}
		else {
			selection.append(" and " + COLUMN_PROFESSION_ID + " is null");
		}
		List<SpellList> list = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection.toString(), selectionArgs, getIdColumnName());

			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					SpellList instance = cursorToEntity(cursor);

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
}
