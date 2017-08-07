/*
 *
 *   Copyright (C) 2017 MadInnovations
 *   <p/>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   <p/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   <p/>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  
 *
 */
package com.madinnovations.rmu.data.dao.item;

import com.madinnovations.rmu.data.entities.item.NaturalsTemplate;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link NaturalsTemplate} objects in persistent storage.
 */
public interface NaturalsTemplateDao {
    /**
     * Retrieves a NaturalsTemplate object from persistent storage.
     *
     * @param id  the id of the NaturalsTemplate object to retrieve
     * @return the NaturalsTemplate instance with the given id or null if not found.
     */
    NaturalsTemplate getById(int id);

    /**
     * Retrieves all NaturalsTemplate objects from persistent storage.
     *
     * @return  a List containing all NaturalsTemplate objects currently in persistent storage.
     */
    List<NaturalsTemplate> getAll();

    /**
     * Saves a collection of NaturalsTemplate instances to persistent storage.
     *
     * @param instance  the collection of NaturalsTemplate instances to be saved
     * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
     * @return true if successful, otherwise false.
     */
    boolean save(Collection<NaturalsTemplate> instance, boolean isNew);

    /**
     * Saves a collection of NaturalsTemplate instances to persistent storage.
     *
     * @param instance  the collection of NaturalsTemplate instances to be saved
     * @return true if successful, otherwise false.
     */
    boolean save(Collection<NaturalsTemplate> instance);

    /**
     * Saves a NaturalsTemplate object to persistent storage.
     *
     * @param instance  the NaturalsTemplate object to be saved
     * @return true if successful, otherwise false.
     */
    boolean save(NaturalsTemplate instance);

    /**
     * Saves an NaturalsTemplate object to persistent storage.
     *
     * @param instance  the NaturalsTemplate object to be saved
     * @param isNew  true if the instances should be treated as new regardless of their ID values
     * @return true if successful, otherwise false.
     */
    boolean save(NaturalsTemplate instance, boolean isNew);

    /**
     * Delete the NaturalsTemplate object with the given id from persistent storage.
     *
     * @param id  the id of the NaturalsTemplate object to delete
     * @return true if successful, otherwise false.
     */
    boolean deleteById(int id);

    /**
     * Delete all NaturalsTemplate objects from persistent storage.
     *
     * @return the number of instances that were deleted.
     */
    int deleteAll();
}
