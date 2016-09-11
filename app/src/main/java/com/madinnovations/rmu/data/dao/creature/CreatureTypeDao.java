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
package com.madinnovations.rmu.data.dao.creature;

import com.madinnovations.rmu.data.entities.creature.CreatureType;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link CreatureType} objects in persistent storage.
 */
public interface CreatureTypeDao {
	/**
	 * Retrieves a CreatureType object from persistent storage.
	 *
	 * @param id  the id of the CreatureType object to retrieve
	 * @return the CreatureType instance with the given id or null if not found.
	 */
	public CreatureType getById(int id);

	/**
	 * Retrieves all CreatureType objects from persistent storage.
	 *
	 * @return  a List containing all CreatureType objects currently in persistent storage.
	 */
	public List<CreatureType> getAll();

	/**
	 * Saves a collection of CreatureType instances to persistent storage.
	 *
	 * @param instance  the collection of CreatureType instances to be saved
	 * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<CreatureType> instance, boolean isNew);

	/**
	 * Saves a collection of CreatureType instances to persistent storage.
	 *
	 * @param instance  the collection of CreatureType instances to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<CreatureType> instance);

	/**
	 * Saves a CreatureType object to persistent storage.
	 *
	 * @param instance  the CreatureType object to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(CreatureType instance);

	/**
	 * Delete the CreatureType object with the given id from persistent storage.
	 *
	 * @param id  the id of the CreatureType object to delete
	 * @return true if successful, otherwise false.
	 */
	public boolean deleteById(int id);

	/**
	 * Delete all CreatureType objects from persistent storage.
	 *
	 * @return the number of instances that were deleted.
	 */
	public int deleteAll();
}
