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

import com.madinnovations.rmu.data.entities.common.Size;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link Size} objects in persistent storage.
 */
public interface SizeDao {
	/**
	 * Retrieves a Size object from persistent storage.
	 *
	 * @param id  the id of the Size object to retrieve
	 * @return the Size instance with the given id or null if not found.
	 */
	public Size getById(int id);

	/**
	 * Retrieves all Size objects from persistent storage.
	 *
	 * @return  a List containing all Size objects currently in persistent storage.
	 */
	public List<Size> getAll();

	/**
	 * Saves a collection of Size instances to persistent storage.
	 *
	 * @param instance  the collection of Size instances to be saved
	 * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<Size> instance, boolean isNew);

	/**
	 * Saves a collection of Size instances to persistent storage.
	 *
	 * @param instance  the collection of Size instances to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<Size> instance);

	/**
	 * Saves a Size object to persistent storage.
	 *
	 * @param instance  the Size object to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Size instance);

	/**
	 * Delete the Size object with the given id from persistent storage.
	 *
	 * @param id  the id of the Size object to delete
	 * @return true if successful, otherwise false.
	 */
	public boolean deleteById(int id);

	/**
	 * Delete all Size objects from persistent storage.
	 *
	 * @return the number of instances that were deleted.
	 */
	public int deleteAll();
}
