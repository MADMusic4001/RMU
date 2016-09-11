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

import com.madinnovations.rmu.data.entities.combat.CriticalType;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link CriticalType} objects in persistent storage.
 */
public interface CriticalTypeDao {
    /**
     * Retrieves a CriticalType object from persistent storage.
     *
     * @param id  the id of the CriticalType object to retrieve
     * @return the CriticalType instance with the given id or null if not found.
     */
    public CriticalType getById(int id);

    /**
     * Retrieves all CriticalType objects from persistent storage.
     *
     * @return  a List containing all CriticalType objects currently in persistent storage.
     */
    public List<CriticalType> getAll();

    /**
     * Saves a collection of CriticalType instances to persistent storage.
     *
     * @param instance  the collection of CriticalType instances to be saved
     * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
     * @return true if successful, otherwise false.
     */
    public boolean save(Collection<CriticalType> instance, boolean isNew);

    /**
     * Saves a collection of CriticalType instances to persistent storage.
     *
     * @param instance  the collection of CriticalType instances to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(Collection<CriticalType> instance);

    /**
     * Saves a CriticalType object to persistent storage.
     *
     * @param instance  the CriticalType object to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(CriticalType instance);

    /**
     * Delete the CriticalType object with the given id from persistent storage.
     *
     * @param id  the id of the CriticalType object to delete
     * @return true if successful, otherwise false.
     */
    public boolean deleteById(int id);

    /**
     * Delete all CriticalType objects from persistent storage.
     *
     * @return the number of instances that were deleted.
     */
    public int deleteAll();

	/**
     * Retrieves a CriticalType object from persistent storage.
     *
     * @param code  the code of the CriticalType object to retrieve
     * @return the CriticalType instance with the given code or null if not found.
     */
    public CriticalType getByCode(char code);
}
