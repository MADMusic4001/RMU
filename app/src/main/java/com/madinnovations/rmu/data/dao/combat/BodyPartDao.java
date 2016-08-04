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

import com.madinnovations.rmu.data.entities.combat.BodyPart;

import java.util.List;

/**
 * Methods to manage {@link BodyPart} objects in persistent storage.
 */
public interface BodyPartDao {
    /**
     * Retrieves a BodyPart object from persistent storage.
     *
     * @param id  the id of the BodyPart object to retrieve
     * @return the BodyPart instance with the given id or null if not found.
     */
    public BodyPart getById(int id);

    /**
     * Retrieves all BodyPart objects from persistent storage.
     *
     * @return  a List containing all BodyPart objects currently in persistent storage.
     */
    public List<BodyPart> getAll();

    /**
     * Saves a BodyPart object to persistent storage.
     *
     * @param instance  the BodyPart object to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(BodyPart instance);

    /**
     * Delete the BodyPart object with the given id from persistent storage.
     *
     * @param id  the id of the BodyPart object to delete
     * @return true if successful, otherwise false.
     */
    public boolean deleteById(int id);

    /**
     * Delete all BodyPart objects from persistent storage.
     *
     * @return the number of instances that were deleted.
     */
    public int deleteAll();
}
