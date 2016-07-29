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
package com.madinnovations.rmu.data.dao.combat.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.CriticalCodeDao;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalCodeSchema;
import com.madinnovations.rmu.data.entities.combat.CriticalCode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link CriticalCode} objects in a SQLite database.
 */
@Singleton
public class CriticalCodeDaoDbImpl extends BaseDaoDbImpl implements CriticalCodeDao, CriticalCodeSchema {
	/**
	 * Creates a new instance of CriticalCodeDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 */
	@Inject
	public CriticalCodeDaoDbImpl(SQLiteOpenHelper helper) {
		super(helper);
	}

	@Override
	public CriticalCode getById(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = COLUMN_ID + " = ?";
		CriticalCode instance = new CriticalCode();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = super.query(TABLE_NAME, COLUMNS, selection,
										selectionArgs, COLUMN_ID);
			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					instance = cursorToEntity(cursor);
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

		return instance;
	}

	@Override
	public List<CriticalCode> getAll() {
		List<CriticalCode> list = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = super.query(TABLE_NAME, COLUMNS, null, null, COLUMN_ID);

			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					CriticalCode instance = cursorToEntity(cursor);
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
	public boolean save(CriticalCode instance) {
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		final String selection = COLUMN_ID + " = ?";
		ContentValues contentValues = setContentValue(instance);
		boolean result;

		SQLiteDatabase db = helper.getWritableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			if(instance.getId() == -1) {
				instance.setId((int)db.insert(TABLE_NAME, null, contentValues));
				result = (instance.getId() != -1);
			}
			else {
				contentValues.put(COLUMN_ID, instance.getId());
				int count = db.update(TABLE_NAME, contentValues, selection, selectionArgs);
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

	@Override
	public boolean deleteById(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = COLUMN_ID + " = ?";

		SQLiteDatabase db = helper.getWritableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			db.delete(TABLE_NAME, selection, selectionArgs);
			if(newTransaction) {
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

	@Override
	public int deleteAll() {
		int count = 0;

		SQLiteDatabase db = helper.getWritableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			count = db.delete(TABLE_NAME, null, null);
			if(newTransaction) {
				db.setTransactionSuccessful();
			}
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}

		return count;
	}

	@Override
	protected CriticalCode cursorToEntity(Cursor cursor) {
		CriticalCode instance = new CriticalCode();

		if (cursor != null) {
			instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
			instance.setCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CODE)));
			instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		}
		return instance;
	}

	private ContentValues setContentValue(CriticalCode instance) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COLUMN_CODE, instance.getCode());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
		return initialValues;
	}
}
