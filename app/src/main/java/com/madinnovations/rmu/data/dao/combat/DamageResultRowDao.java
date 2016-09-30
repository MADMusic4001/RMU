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
package com.madinnovations.rmu.data.dao.combat;

import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.data.entities.combat.DamageTable;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link DamageResultRow} objects in persistent storage.
 */
public interface DamageResultRowDao {
    /**
     * Retrieves a DamageResultRow object from persistent storage.
     *
     * @param id  the id of the DamageResultRow object to retrieve
     * @return the DamageResultRow instance with the given id or null if not found.
     */
    public DamageResultRow getById(int id);

    /**
     * Retrieves all DamageResultRow objects from persistent storage.
     *
     * @return  a List containing all DamageResultRow objects currently in persistent storage.
     */
    public List<DamageResultRow> getAll();

    /**
     * Saves a collection of DamageResultRow instances to persistent storage.
     *
     * @param instance  the collection of DamageResultRow instances to be saved
     * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
     * @return true if successful, otherwise false.
     */
    public boolean save(Collection<DamageResultRow> instance, boolean isNew);

    /**
     * Saves a collection of DamageResultRow instances to persistent storage.
     *
     * @param instance  the collection of DamageResultRow instances to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(Collection<DamageResultRow> instance);

    /**
     * Saves a DamageResultRow object to persistent storage.
     *
     * @param instance  the DamageResultRow object to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(DamageResultRow instance);

    /**
     * Delete the DamageResultRow object with the given id from persistent storage.
     *
     * @param id  the id of the DamageResultRow object to delete
     * @return true if successful, otherwise false.
     */
    public boolean deleteById(int id);

    /**
     * Delete all DamageResultRow objects from persistent storage.
     *
     * @return the number of instances that were deleted.
     */
    public int deleteAll();

    /**
     * Retrieves all DamageResultRow objects that reference the given DamageTable from persistent storage.
     *
     * @param filter  a DamageTable instance whose ID will be used to filter the DamageResultRow objects returned by the query
     * @return  a Collection containing all DamageResultRow objects that reference the given DamageTable currently in persistent storage.
     */
    public Collection<DamageResultRow> getDamageResultRowsForDamageTable(DamageTable filter);

	/**
	 * Deletes all DamageResultRow objects that reference the given DamageTable from persistent storage.
	 *
	 * @param filter  a DamageTable instance whose ID will be used to filter the DamageResultRow objects to be deleted
	 * @return  a Collection containing all DamageResultRow objects that were deleted.
	 */
	public Collection<DamageResultRow> deleteDamageResultRowsForDamageTable(DamageTable filter);

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
	public int deleteWithFilter(String whereClause, String[] whereArgs);

	/**
	 * Begins a database transaction.
	 */
	public void beginTransaction();

	/**
	 * Ends a database transaction
	 *
	 * @param successful  true if the transaction should be marked as successful. If false then the transaction will be rolled
	 *                       back unless {@link SQLiteDatabase#setTransactionSuccessful()} was called prior to calling this
	 *                       method.
	 */
	public void endTransaction(boolean successful);
}
