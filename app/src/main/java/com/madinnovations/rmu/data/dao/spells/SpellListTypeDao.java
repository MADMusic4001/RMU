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
package com.madinnovations.rmu.data.dao.spells;

import com.madinnovations.rmu.data.entities.spells.SpellListType;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link SpellListType} objects in persistent storage.
 */
public interface SpellListTypeDao {
	/**
	 * Retrieves a SpellListType object from persistent storage.
	 *
	 * @param id  the id of the SpellListType object to retrieve
	 * @return the SpellListType instance with the given id or null if not found.
	 */
	public SpellListType getById(int id);

	/**
	 * Retrieves all SpellListType objects from persistent storage.
	 *
	 * @return  a List containing all SpellListType objects currently in persistent storage.
	 */
	public List<SpellListType> getAll();

	/**
	 * Saves a collection of SpellListType instances to persistent storage.
	 *
	 * @param instance  the collection of SpellListType instances to be saved
	 * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<SpellListType> instance, boolean isNew);

	/**
	 * Saves a collection of SpellListType instances to persistent storage.
	 *
	 * @param instance  the collection of SpellListType instances to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<SpellListType> instance);

	/**
	 * Saves a SpellListType object to persistent storage.
	 *
	 * @param instance  the SpellListType object to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(SpellListType instance);

	/**
	 * Delete the SpellListType object with the given id from persistent storage.
	 *
	 * @param id  the id of the SpellListType object to delete
	 * @return true if successful, otherwise false.
	 */
	public boolean deleteById(int id);

	/**
	 * Delete all SpellListType objects from persistent storage.
	 *
	 * @return the number of instances that were deleted.
	 */
	public int deleteAll();
}
