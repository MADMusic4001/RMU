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

import com.madinnovations.rmu.data.entities.combat.DamageTable;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link DamageTable} objects in persistent storage.
 */
public interface DamageTableDao {
    /**
     * Retrieves a DamageTable object from persistent storage.
     *
     * @param id  the id of the DamageTable object to retrieve
     * @return the DamageTable instance with the given id or null if not found.
     */
    public DamageTable getById(int id);

    /**
     * Retrieves all DamageTable objects from persistent storage.
     *
     * @return  a List containing all DamageTable objects currently in persistent storage.
     */
    public List<DamageTable> getAll();

    /**
     * Saves a collection of DamageTable instances to persistent storage.
     *
     * @param instance  the collection of DamageTable instances to be saved
     * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
     * @return true if successful, otherwise false.
     */
    public boolean save(Collection<DamageTable> instance, boolean isNew);

    /**
     * Saves a collection of DamageTable instances to persistent storage.
     *
     * @param instance  the collection of DamageTable instances to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(Collection<DamageTable> instance);

    /**
     * Saves a DamageTable object to persistent storage.
     *
     * @param instance  the DamageTable object to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(DamageTable instance);

    /**
     * Delete the DamageTable object with the given id from persistent storage.
     *
     * @param id  the id of the DamageTable object to delete
     * @return true if successful, otherwise false.
     */
    public boolean deleteById(int id);

    /**
     * Delete the given DamageTable object from persistent storage.
     *
     * @param damageTable  the DamageTable object to delete
     * @return true if successful, otherwise false.
     */
    public boolean delete(DamageTable damageTable);

    /**
     * Delete all DamageTable objects from persistent storage.
     *
     * @return the number of instances that were deleted.
     */
    public int deleteAll();

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
