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
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 7/24/2016.
 */
public abstract class BaseDaoDbImpl<T> {
	protected SQLiteOpenHelper helper;

	/**
	 * Creates a BaseDaoDbImpl instance
	 *
	 * @param helper  an {@link SQLiteOpenHelper} instance to use to access the database
     */
	public BaseDaoDbImpl(SQLiteOpenHelper helper) {
		this.helper = helper;
	}

	/**
	 * Creates an instance of type T from the data in the current cursor row.
	 *
	 * @param cursor  a{@link Cursor} positioned at a valid row in a query result
	 * @return an instance of type T
	 */
	protected abstract T cursorToEntity(Cursor cursor);

	/**
	 * Gets a {@link ContentValues} instance containing the values to use to insert or update the database.
	 *
	 * @param instance  the instance to get the values from
	 * @return a ContentValues instance.
	 */
	protected abstract ContentValues getContentValues(T instance);

	/**
	 * Inserts a new record into the database.
	 *
	 * @see SQLiteDatabase#insert(String, String, ContentValues)
	 *
	 * @param tableName  the name of the table in which to insert the record
	 * @param values  a {@link ContentValues} instance containing the data to be inserted
	 * @return  the row id of the newly inserted record or -1 if an error occurred
	 */
	public long insert(String tableName, ContentValues values) {
		SQLiteDatabase db = helper.getWritableDatabase();
		return db.insert(tableName, null, values);
	}

	/**
	 * Executes a query against the database.
	 *
	 * @see SQLiteDatabase#query(String, String[], String, String[], String, String, String)
	 *
	 * @param tableName  the name of the table on which to execute the query
	 * @param columns  a String array containing the names of the columns to return in the results
	 * @param selection  the where clause for the query excluding the word 'WHERE'
	 * @param selectionArgs  the where clause parameters
	 * @param sortOrder  an order by clause excluding the words 'ORDER BY'
	 * @return  A {@link Cursor} object, which is positioned before the first entry. Note that Cursors are not synchronized
	 */
	public Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.query(tableName, columns, selection, selectionArgs, null, null, sortOrder);
	}

	/**
	 * Executes a query against the database.
	 *
	 * @see SQLiteDatabase#query(String, String[], String, String[], String, String, String, String)
	 *
	 * @param tableName  the name of the table on which to execute the query
	 * @param columns  a String array containing the names of the columns to return in the results
	 * @param selection  the where clause for the query excluding the word 'WHERE'
	 * @param selectionArgs  the where clause parameters
	 * @param sortOrder  an order by clause excluding the words 'ORDER BY'
	 * @param limit  the maximum number of records to retrieve (e.g. "50" would limit to no more than 50 records returned)
	 * @return  A {@link Cursor} object, which is positioned before the first entry. Note that Cursors are not synchronized
	 */
	public Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs, String sortOrder,
						String limit) {
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.query(tableName, columns, selection,
						 selectionArgs, null, null, sortOrder, limit);
	}

	/**
	 * Executes a query against the database.
	 *
	 * @see SQLiteDatabase#query(String, String[], String, String[], String, String, String, String)
	 *
	 * @param tableName  the name of the table on which to execute the query
	 * @param columns  a String array containing the names of the columns to return in the results
	 * @param selection  the where clause for the query excluding the word 'WHERE'
	 * @param selectionArgs  the where clause parameters
	 * @param groupBy  a group by clause excluding the words "GROUP BY"
	 * @param having  a HAVING clause excluding the word "HAVING"
	 * @param orderBy  an order by clause excluding the words 'ORDER BY'
	 * @param limit  the maximum number of records to retrieve (e.g. "50" would limit to no more than 50 records returned)
	 * @return  A {@link Cursor} object, which is positioned before the first entry. Note that Cursors are not synchronized
	 */
	public Cursor query(String tableName, String[] columns,
						String selection, String[] selectionArgs, String groupBy,
						String having, String orderBy, String limit) {

		SQLiteDatabase db = helper.getReadableDatabase();
		return db.query(tableName, columns, selection,
						 selectionArgs, groupBy, having, orderBy, limit);
	}

	/**
	 * Updates an existing record in the database.
	 *
	 * @see SQLiteDatabase#update(String, ContentValues, String, String[])
	 *
	 * @param tableName  the name of the table in which to insert the record
	 * @param values  a {@link ContentValues} instance containing the data to be inserted
	 * @param selection  the where clause for the query excluding the word 'WHERE'
	 * @param selectionArgs  the where clause parameters
	 * @return the number of rows affected.
	 */
	public int update(String tableName, ContentValues values,
					  String selection, String[] selectionArgs) {
		SQLiteDatabase db = helper.getWritableDatabase();
		return db.update(tableName, values, selection,
						  selectionArgs);
	}

	/**
	 * Executes an SQL query
	 *
	 * @see SQLiteDatabase#rawQuery(String, String[])
	 *
	 * @param sql  the SQL query. The SQL string must not be ; terminated
	 * @param selectionArgs  ou may include ?s in where clause in the query, which will be replaced by the values from
	 *                          selectionArgs. The values will be bound as Strings.
	 * @return  A {@link Cursor} object, which is positioned before the first entry. Note that Cursors are not synchronized
	 */
	public Cursor rawQuery(String sql, String[] selectionArgs) {
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery(sql, selectionArgs);
	}
}