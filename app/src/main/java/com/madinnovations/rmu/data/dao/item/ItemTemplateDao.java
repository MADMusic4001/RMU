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
package com.madinnovations.rmu.data.dao.item;

import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.object.ItemTemplate;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link ItemTemplate} objects in persistent storage.
 */
public interface ItemTemplateDao {
    /**
     * Retrieves a ItemTemplate object from persistent storage.
     *
     * @param id  the id of the ItemTemplate object to retrieve
     * @return the ItemTemplate instance with the given id or null if not found.
     */
    public ItemTemplate getById(int id);

    /**
     * Retrieves all ItemTemplate objects from persistent storage.
     *
     * @return  a List containing all ItemTemplate objects currently in persistent storage.
     */
    public List<ItemTemplate> getAll();

    /**
     * Saves a collection of ItemTemplate instances to persistent storage.
     *
     * @param instance  the collection of ItemTemplate instances to be saved
     * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
     * @return true if successful, otherwise false.
     */
    public boolean save(Collection<ItemTemplate> instance, boolean isNew);

    /**
     * Saves a collection of ItemTemplate instances to persistent storage.
     *
     * @param instance  the collection of ItemTemplate instances to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(Collection<ItemTemplate> instance);

    /**
     * Saves a ItemTemplate object to persistent storage.
     *
     * @param instance  the ItemTemplate object to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(ItemTemplate instance);

    /**
     * Delete the ItemTemplate object with the given id from persistent storage.
     *
     * @param id  the id of the ItemTemplate object to delete
     * @return true if successful, otherwise false.
     */
    public boolean deleteById(int id);

    /**
     * Delete all ItemTemplate objects from persistent storage.
     *
     * @return the number of instances that were deleted.
     */
    public int deleteAll();
}
