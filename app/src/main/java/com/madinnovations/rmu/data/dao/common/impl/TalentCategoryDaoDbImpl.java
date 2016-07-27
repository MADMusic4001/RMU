package com.madinnovations.rmu.data.dao.common.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.TalentCategoryDao;
import com.madinnovations.rmu.data.dao.common.schemas.TalentCategorySchema;
import com.madinnovations.rmu.data.entities.common.TalentCategory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link TalentCategory} objects in a SQLite database.
 */
@Singleton
public class TalentCategoryDaoDbImpl extends BaseDaoDbImpl implements TalentCategoryDao, TalentCategorySchema {
	/**
	 * Creates a new instance of TalentCategoryDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 */
	@Inject
	public TalentCategoryDaoDbImpl(SQLiteOpenHelper helper) {
		super(helper);
	}

	@Override
	public TalentCategory getById(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = COLUMN_ID + " = ?";
		TalentCategory instance = new TalentCategory();

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
	public List<TalentCategory> getAll() {
		List<TalentCategory> list = new ArrayList<>();

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
					TalentCategory instance = cursorToEntity(cursor);
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
	public boolean save(TalentCategory instance) {
		Log.d("TalentCategoryDaoDbImpl", "In save");
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

	@SuppressWarnings("unchecked")
	@Override
	protected TalentCategory cursorToEntity(Cursor cursor) {
		TalentCategory instance = new TalentCategory();

		if (cursor != null) {
			instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
			instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
			instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		}
		return instance;
	}

	private ContentValues setContentValue(TalentCategory instance) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
		return initialValues;
	}
}
