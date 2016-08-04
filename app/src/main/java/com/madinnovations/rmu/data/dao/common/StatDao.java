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
package com.madinnovations.rmu.data.dao.common;

import com.madinnovations.rmu.data.entities.common.Stat;

import java.util.List;

/**
 * Methods to manage {@link Stat} objects in persistent storage.
 */
public interface StatDao {
    /**
     * Retrieves a Skill object from persistent storage.
     *
     * @param id  the id of the Skill object to retrieve
     * @return the Skill instance with the given id or null if not found.
     */
    public Stat getById(int id);

    /**
     * Retrieves all Skill objects from persistent storage.
     *
     * @return  a List containing all Skill objects currently in persistent storage.
     */
    public List<Stat> getAll();

    /**
     * Saves a Skill object to persistent storage.
     *
     * @param instance  the Skill object to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(Stat instance);

    /**
     * Delete the Skill object with the given id from persistent storage.
     *
     * @param id  the id of the Skill object to delete
     * @return true if successful, otherwise false.
     */
    public boolean deleteById(int id);

    /**
     * Delete all Skill objects from persistent storage.
     *
     * @return the number of instances that were deleted.
     */
    public int deleteAll();
}
