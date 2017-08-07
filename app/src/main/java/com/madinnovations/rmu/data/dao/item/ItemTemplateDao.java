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
package com.madinnovations.rmu.data.dao.item;

import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.entities.item.ItemTemplate;
import com.madinnovations.rmu.data.entities.item.Slot;

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
    ItemTemplate getById(int id);

    /**
     * Retrieves all ItemTemplate objects from persistent storage.
     *
     * @return  a List containing all ItemTemplate objects currently in persistent storage.
     */
    List<ItemTemplate> getAll();

    /**
     * Saves a collection of ItemTemplate instances to persistent storage.
     *
     * @param instance  the collection of ItemTemplate instances to be saved
     * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
     * @return true if successful, otherwise false.
     */
    boolean save(Collection<ItemTemplate> instance, boolean isNew);

    /**
     * Saves a collection of ItemTemplate instances to persistent storage.
     *
     * @param instance  the collection of ItemTemplate instances to be saved
     * @return true if successful, otherwise false.
     */
    boolean save(Collection<ItemTemplate> instance);

    /**
     * Saves a ItemTemplate object to persistent storage.
     *
     * @param instance  the ItemTemplate object to be saved
     * @return true if successful, otherwise false.
     */
    boolean save(ItemTemplate instance);

	/**
	 * Saves an ItemTemplate object to persistent storage.
	 *
	 * @param instance  the ItemTemplate object to be saved
	 * @param isNew  true if the instances should be treated as new regardless of their ID values
	 * @return true if successful, otherwise false.
	 */
	boolean save(ItemTemplate instance, boolean isNew);

    /**
     * Delete the ItemTemplate object with the given id from persistent storage.
     *
     * @param id  the id of the ItemTemplate object to delete
     * @return true if successful, otherwise false.
     */
    boolean deleteById(int id);

    /**
     * Delete all ItemTemplate objects from persistent storage.
     *
     * @return the number of instances that were deleted.
     */
    int deleteAll();

	/**
	 * Retrieves all ItemTemplate objects from persistent storage that can be equipped in the given slot.
	 *
	 * @param slot  an equipment slot
	 * @return  a Collection containing all ItemTemplate objects currently in persistent storage that can be equipped in the given slot.
	 */
    Collection<ItemTemplate> getAllForSlot(@NonNull Slot slot);

	/**
	 * Retrieves all ItemTemplate objects from persistent storage that have no subclass.
	 *
	 * @return  a Collection containing all ItemTemplate objects with no subclass currently in persistent storage.
	 */
	Collection<ItemTemplate> getAllWithoutSubclass();
}
