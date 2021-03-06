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
package com.madinnovations.rmu.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.LruCache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Common DAO method implementations
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
	protected abstract T cursorToEntity(@NonNull Cursor cursor);

	/**
	 * Gets a {@link ContentValues} instance containing the values to use to insert or update the database.
	 *
	 * @param instance  the instance to get the values from
	 * @return a ContentValues instance.
	 */
	protected abstract ContentValues getContentValues(T instance);

	/**
	 * Gets the name of the database table.
	 *
	 * @return the name of the database table.
	 */
	protected abstract String getTableName();

	/**
	 * Gets an array of the table column names.
	 *
	 * @return  the array of column names.
	 */
	protected abstract String[] getColumns();

	/**
	 * The name of the id column for the table.
	 *
	 * @return the name of the id column.
	 */
	protected abstract String getIdColumnName();

	/**
	 * Gets the sort string to use when executing queries.
	 *
	 * @return  the string that will be passed to the query method for sorting results.
	 */
	protected String getSortString() {
		return getIdColumnName();
	}

	/**
	 * Gets the value of the id for a T instance.
	 *
	 * @param instance  a T instance
	 * @return the value of the id for the T isntance.
	 */
	protected abstract int getId(T instance);

	/**
	 * Sets the value of the id for a T instance.
	 *
	 * @param instance  a T instance
	 * @param id  the value of the id
	 */
	protected abstract void setId(T instance, int id);

	/**
	 * Saves any related instances.
	 *
	 * @param db  the SQLiteDatabase instance to use to save the relationships
	 * @param instance  the T instance being saved
	 * @return  true if no error occurred.
	 */
	protected boolean saveRelationships(SQLiteDatabase db, T instance) {
		return true;
	}

	/**
	 * Allows relationships to be deleted prior to the T instance being deleted to prevent foreign key failures.
	 *
	 * @param db  the SQLiteDatabase instance to use to delete the relationships
	 * @param id  the id of the T instance being deleted
	 * @return  true if no error occurred.
	 */
	protected boolean deleteRelationships(SQLiteDatabase db, int id) {
		return true;
	}

	/**
	 * Gets the cache for T instances.
	 *
	 * @return an LruCache instance with Integer keys and T values or null if T is not cached.
	 */
	protected LruCache<Integer, T> getCache() {
		return null;
	}

	/**
	 * Begins a database transaction.
	 */
	public void beginTransaction() {
		helper.getWritableDatabase().beginTransaction();
	}

	/**
	 * Ends a database transaction
	 *
	 * @param successful  true if the transaction should be marked as successful. If false then the transaction will be rolled
	 *                       back unless {@link SQLiteDatabase#setTransactionSuccessful()} was called prior to calling this
	 *                       method.
	 */
	public void endTransaction(boolean successful) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if(successful) {
			db.setTransactionSuccessful();
		}
		db.endTransaction();
	}

	/**
	 * Retrieves a T object from persistent storage.
	 *
	 * @param id  the id of the T object to retrieve
	 * @return the T instance with the given id or null if not found.
	 */
	public T getById(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = getIdColumnName() + " = ?";
		LruCache<Integer, T> cache = getCache();
		T instance = null;

		if(cache != null) {
			instance = cache.get(id);
			if (instance != null) {
				return instance;
			}
		}
		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection,
					selectionArgs, getSortString());
			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					instance = cursorToEntity(cursor);
					if(cache != null) {
						cache.put(getId(instance), instance);
					}
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

	/**
	 * Retrieves all T objects from persistent storage.
	 *
	 * @return  a List containing all T objects currently in persistent storage.
	 */
	public List<T> getAll() {
		List<T> list = new ArrayList<>();
		LruCache<Integer, T> cache = getCache();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), null, null, getSortString());

			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					T instance = cursorToEntity(cursor);
					list.add(instance);
					cursor.moveToNext();
					if(cache != null) {
						cache.put(getId(instance), instance);
					}
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

	/**
	 * Saves a collection of T instances to persistent storage.
	 *
	 * @param collection  the collection of T instances to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<T> collection) {
		return save(collection, false);
	}

	/**
	 * Saves a collection of T instances to persistent storage.
	 *
	 * @param collection  the collection of T instances to be saved
	 * @param isNew  true if the instances should be treated as new regardless of their ID values
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<T> collection, boolean isNew) {
		boolean result = true;

		SQLiteDatabase db = helper.getWritableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			for(T t : collection) {
				result &= save(t, isNew);
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

	/**
	 * Saves a T object to persistent storage.
	 *
	 * @param instance  the T object to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(T instance) {
		return save(instance, false);
	}

	/**
	 * Saves a T object to persistent storage.
	 *
	 * @param instance  the T object to be saved
	 * @param isNew  true if the instances should be treated as new regardless of their ID values
	 * @return true if successful, otherwise false.
	 */
	public boolean save(T instance, boolean isNew) {
		final String selectionArgs[] = { String.valueOf(getId(instance)) };
		final String selection = getIdColumnName() + " = ?";
		ContentValues contentValues = getContentValues(instance);
		boolean result;
		LruCache<Integer, T> cache = getCache();

		SQLiteDatabase db = helper.getWritableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			if(getId(instance) == -1 || isNew) {
				setId(instance, (int)db.insertWithOnConflict(getTableName(), null, contentValues, SQLiteDatabase.CONFLICT_NONE));
				result = (getId(instance) != -1);
			}
			else {
				int count = db.update(getTableName(), contentValues, selection, selectionArgs);
				result = (count == 1);
			}
			result &= saveRelationships(db, instance);
			if(cache != null) {
				cache.put(getId(instance), instance);
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

	/**
	 * Delete the T object from persistent storage.
	 *
	 * @param instance  the T object to delete
	 * @return true if successful, otherwise false.
	 */
	public boolean delete(T instance) {
		return deleteById(getId(instance));
	}

	/**
	 * Delete the T object with the given id from persistent storage.
	 *
	 * @param id  the id of the T object to delete
	 * @return true if successful, otherwise false.
	 */
	public boolean deleteById(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = getIdColumnName() + " = ?";
		LruCache<Integer, T> cache = getCache();

		SQLiteDatabase db = helper.getWritableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			if(deleteRelationships(db, id)) {
				db.delete(getTableName(), selection, selectionArgs);
			}
			if(cache != null) {
				cache.remove(id);
			}
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

	/**
	 * Delete all T objects from persistent storage.
	 *
	 * @return the number of instances that were deleted.
	 */
	public int deleteAll() {
		return deleteWithFilter(null, null);
	}

	/**
	 * Delete all T objects that match the given filter information
	 *
	 * @param whereClause the optional WHERE clause to apply when deleting.
	 *            Passing null will delete all rows.
	 * @param whereArgs You may include ?s in the where clause, which
	 *            will be replaced by the values from whereArgs. The values
	 *            will be bound as Strings.
	 * @return the number of rows affected if a whereClause is passed in, 0
	 *         otherwise. To remove all rows and get a count pass "1" as the
	 *         whereClause.
	 */
	@SuppressWarnings("WeakerAccess")
	public int deleteWithFilter(String whereClause, String[] whereArgs) {
		int count = 0;
		LruCache<Integer, T> cache = getCache();

		SQLiteDatabase db = helper.getWritableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			count = db.delete(getTableName(), whereClause, whereArgs);
			if(cache != null) {
				cache.evictAll();
			}
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

	/**
	 * Inserts a new record into the database.
	 *
	 * @see SQLiteDatabase#insert(String, String, ContentValues)
	 *
	 * @param tableName  the name of the table in which to insert the record
	 * @param values  a {@link ContentValues} instance containing the data to be inserted
	 * @return  the row id of the newly inserted record or -1 if an error occurred
	 */
	@SuppressWarnings("unused")
	public long insert(String tableName, ContentValues values) {
		SQLiteDatabase db = helper.getWritableDatabase();
		return db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_NONE);
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
	@SuppressWarnings("unused")
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
	@SuppressWarnings({"unused", "WeakerAccess"})
	public Cursor rawQuery(String sql, String[] selectionArgs) {
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery(sql, selectionArgs);
	}
}