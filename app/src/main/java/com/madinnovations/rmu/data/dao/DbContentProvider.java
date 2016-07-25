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
package com.madinnovations.rmu.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 7/24/2016.
 */
public abstract class DbContentProvider {
	public SQLiteDatabase mDb;

	public int delete(String tableName, String selection,
					  String[] selectionArgs) {
		return mDb.delete(tableName, selection, selectionArgs);
	}

	public long insert(String tableName, ContentValues values) {
		return mDb.insert(tableName, null, values);
	}

	protected abstract <T> T cursorToEntity(Cursor cursor);

	public DbContentProvider(SQLiteDatabase db) {
		this.mDb = db;
	}

	public Cursor query(String tableName, String[] columns,
						String selection, String[] selectionArgs, String sortOrder) {

		final Cursor cursor = mDb.query(tableName, columns,
										selection, selectionArgs, null, null, sortOrder);

		return cursor;
	}

	public Cursor query(String tableName, String[] columns,
						String selection, String[] selectionArgs, String sortOrder,
						String limit) {

		return mDb.query(tableName, columns, selection,
						 selectionArgs, null, null, sortOrder, limit);
	}

	public Cursor query(String tableName, String[] columns,
						String selection, String[] selectionArgs, String groupBy,
						String having, String orderBy, String limit) {

		return mDb.query(tableName, columns, selection,
						 selectionArgs, groupBy, having, orderBy, limit);
	}

	public int update(String tableName, ContentValues values,
					  String selection, String[] selectionArgs) {
		return mDb.update(tableName, values, selection,
						  selectionArgs);
	}

	public Cursor rawQuery(String sql, String[] selectionArgs) {
		return mDb.rawQuery(sql, selectionArgs);
	}
}