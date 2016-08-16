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
}
