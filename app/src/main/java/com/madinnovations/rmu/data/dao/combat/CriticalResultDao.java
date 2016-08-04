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

import com.madinnovations.rmu.data.entities.combat.CriticalResult;

import java.util.List;

/**
 * Methods to manage {@link CriticalResult} objects in persistent storage.
 */
public interface CriticalResultDao {
    /**
     * Retrieves a CriticalResult object from persistent storage.
     *
     * @param id  the id of the CriticalResult object to retrieve
     * @return the CriticalResult instance with the given id or null if not found.
     */
    public CriticalResult getById(int id);

    /**
     * Retrieves all CriticalResult objects from persistent storage.
     *
     * @return  a List containing all CriticalResult objects currently in persistent storage.
     */
    public List<CriticalResult> getAll();

    /**
     * Saves a CriticalResult object to persistent storage.
     *
     * @param instance  the CriticalResult object to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(CriticalResult instance);

    /**
     * Delete the CriticalResult object with the given id from persistent storage.
     *
     * @param id  the id of the CriticalResult object to delete
     * @return true if successful, otherwise false.
     */
    public boolean deleteById(int id);

    /**
     * Delete all CriticalResult objects from persistent storage.
     *
     * @return the number of instances that were deleted.
     */
    public int deleteAll();
}
