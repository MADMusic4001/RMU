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
package com.madinnovations.rmu.data.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.madinnovations.rmu.data.dao.CharacterDao;
import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.schemas.CharacterSchema;
import com.madinnovations.rmu.data.entities.Character;

import java.util.ArrayList;
import java.util.List;

/**
 * Methods for managing {@link Character} objects in a SQLite database.
 */
public class CharacterDaoDbImpl extends DbContentProvider implements CharacterDao, CharacterSchema {
	private ContentValues initialValues;
	/**
	 * Creates a new instance of CharacterDaoImpl
	 *
	 * @param db  an SQLiteDatabase instance
	 */
	public CharacterDaoDbImpl(SQLiteDatabase db) {
		super(db);
	}

	@Override
	public Character getById(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = COLUMN_ID + " = ?";
		Character user = new Character();
		Cursor cursor = super.query(TABLE_NAME, COLUMNS, selection,
							 selectionArgs, COLUMN_ID);
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				user = cursorToEntity(cursor);
				cursor.moveToNext();
			}
			cursor.close();
		}

		return user;
	}

	@Override
	public List<Character> getAll() {
		List<Character> userList = new ArrayList<>();
		Cursor cursor = super.query(TABLE_NAME, COLUMNS, null,
							 null, COLUMN_ID);

		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Character user = cursorToEntity(cursor);
				userList.add(user);
				cursor.moveToNext();
			}
			cursor.close();
		}

		return userList;
	}

	@Override
	public boolean save(Character instance) {
		// set values
		setContentValue(instance);
		try {
			return super.insert(TABLE_NAME, getContentValue()) > 0;
		} catch (SQLiteConstraintException ex){
			Log.w("Database", ex.getMessage());
			return false;
		}	}

	@Override
	public boolean deleteById(int instanceId) {
		return false;
	}

	@Override
	public boolean deleteAll() {
		return false;
	}

	@Override
	protected Character cursorToEntity(Cursor cursor) {
		Character character = new Character();

		int idIndex;
		int nameIndex;
		int descriptionIndex;
		int raceIdIndex;

		if (cursor != null) {
			if (cursor.getColumnIndex(COLUMN_ID) != -1) {
				idIndex = cursor.getColumnIndexOrThrow(COLUMN_ID);
				character.setId(cursor.getInt(idIndex));
			}
			if (cursor.getColumnIndex(COLUMN_NAME) != -1) {
				nameIndex = cursor.getColumnIndexOrThrow(
						COLUMN_NAME);
				character.setName(cursor.getString(nameIndex));
			}
			if (cursor.getColumnIndex(COLUMN_DESCRIPTION) != -1) {
				descriptionIndex = cursor.getColumnIndexOrThrow(
						COLUMN_DESCRIPTION);
				character.setDescription(cursor.getString(descriptionIndex));
			}
			if (cursor.getColumnIndex(COLUMN_RACE_ID) != -1) {
				raceIdIndex = cursor.getColumnIndexOrThrow(COLUMN_RACE_ID);
				// TODO: get Race from db using race id
//				user.createdDate = new Date(cursor.getLong(raceIdIndex));
			}

		}
		return character;
	}

	private void setContentValue(Character character) {
		initialValues = new ContentValues();
		initialValues.put(COLUMN_ID, character.getId());
		initialValues.put(COLUMN_NAME, character.getName());
		initialValues.put(COLUMN_DESCRIPTION, character.getDescription());
		initialValues.put(COLUMN_RACE_ID, character.getRace().getId());
		initialValues.put(COLUMN_HEIGHT, character.getHeight());
		initialValues.put(COLUMN_WEIGHT, character.getWeight());
		initialValues.put(COLUMN_STRIDE, character.getStride());
		initialValues.put(COLUMN_CURRENT_HITS, character.getCurrentHits());
		initialValues.put(COLUMN_MAX_HITS, character.getMaxHits());
		initialValues.put(COLUMN_CURRENT_DEVELOPMENT_POINTS, character.getCurrentDevelopmentPoints());
	}

	private ContentValues getContentValue() {
		return initialValues;
	}
}
