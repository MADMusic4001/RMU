package com.madinnovations.rmu.data.dao.common.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.SizeDao;
import com.madinnovations.rmu.data.dao.common.schemas.SizeSchema;
import com.madinnovations.rmu.data.entities.common.Size;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Size} objects in a SQLite database.
 */
@Singleton
public class SizeDaoDbImpl extends BaseDaoDbImpl<Size> implements SizeDao, SizeSchema {
    /**
     * Creates a new instance of SizeDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public SizeDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    public Size getById(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = COLUMN_ID + " = ?";
		Size instance = new Size();

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
    public List<Size> getAll() {
		List<Size> list = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = super.query(TABLE_NAME, COLUMNS, null,
										null, COLUMN_ID);

			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Size instance = cursorToEntity(cursor);
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
    public boolean save(Size instance) {
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		final String selection = COLUMN_ID + " = ?";
		ContentValues contentValues = getContentValues(instance);
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
    protected Size cursorToEntity(Cursor cursor) {
		Size instance = null;

		if (cursor != null) {
			instance = new Size();
			instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
			instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
			instance.setMinWeight(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MIN_WEIGHT)));
			instance.setMaxWeight(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MAX_WEIGHT)));
		}
		return instance;
    }

	@Override
	protected ContentValues getContentValues(Size instance) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_MIN_WEIGHT, instance.getMinWeight());
		initialValues.put(COLUMN_MAX_WEIGHT, instance.getMaxWeight());
		return initialValues;
	}
}
