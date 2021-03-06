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

import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.item.Item;
import com.madinnovations.rmu.data.entities.item.Slot;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link Item} objects in persistent storage.
 */
public interface ItemDao {
    /**
     * Retrieves a Item object from persistent storage.
     *
     * @param id  the id of the Item object to retrieve
     * @return the Item instance with the given id or null if not found.
     */
    Item getById(int id);

    /**
     * Retrieves all Item objects from persistent storage.
     *
     * @return  a List containing all Item objects currently in persistent storage.
     */
    List<Item> getAll();

    /**
     * Saves a collection of Item instances to persistent storage.
     *
     * @param instance  the collection of Item instances to be saved
     * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
     * @return true if successful, otherwise false.
     */
    boolean save(Collection<Item> instance, boolean isNew);

    /**
     * Saves a collection of Item instances to persistent storage.
     *
     * @param instance  the collection of Item instances to be saved
     * @return true if successful, otherwise false.
     */
    boolean save(Collection<Item> instance);

    /**
     * Saves a Item object to persistent storage.
     *
     * @param instance  the Item object to be saved
     * @return true if successful, otherwise false.
     */
    boolean save(Item instance);

	/**
	 * Saves a Item object to persistent storage.
	 *
	 * @param instance  the Item object to be saved
	 * @param isNew  set to true if the instance has a valid IDs but should be inserted instead of updated.
	 * @return true if successful, otherwise false.
	 */
	boolean save(Item instance, boolean isNew);

    /**
     * Delete the Item object with the given id from persistent storage.
     *
     * @param id  the id of the Item object to delete
     * @return true if successful, otherwise false.
     */
    boolean deleteById(int id);

    /**
     * Delete all Item objects from persistent storage.
     *
     * @return the number of instances that were deleted.
     */
    int deleteAll();

    /**
     * Retrieves all Item objects from persistent storage for the given campaign.
     *
     * @param campaign  a {@link Campaign} instance
     * @return the collection of Item instances for the given campaign
     */
    Collection<Item> getAllForCampaign(Campaign campaign);

	/**
	 * Retrieves all Item objects from persistent storage that can be equipped in the given slot.
	 *
	 * @param slot  an equipment slot
	 * @return  a List containing all Item objects currently in persistent storage that can be equipped in the given slot.
	 */
	Collection<Item> getAllForSlot(@NonNull Slot slot);

	/**
	 * Retrieves all Item objects from persistent storage that have no subclass.
	 *
	 * @return  a Collection containing all Item objects with no subclass currently in persistent storage.
	 */
	Collection<Item> getAllWithoutSubclass();
}
