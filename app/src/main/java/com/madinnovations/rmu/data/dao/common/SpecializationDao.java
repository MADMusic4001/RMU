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

import com.madinnovations.rmu.data.entities.common.Specialization;

import java.util.List;

/**
 * Methods to manage {@link Specialization} objects in persistent storage.
 */
public interface SpecializationDao {
	/**
	 * Retrieves a Specialization object from persistent storage.
	 *
	 * @param id  the id of the Specialization object to retrieve
	 * @return the Specialization instance with the given id or null if not found.
	 */
	public Specialization getById(int id);

	/**
	 * Retrieves all Specialization objects from persistent storage.
	 *
	 * @return  a List containing all Specialization objects currently in persistent storage.
	 */
	public List<Specialization> getAll();

	/**
	 * Saves a Specialization object to persistent storage.
	 *
	 * @param instance  the Specialization object to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Specialization instance);

	/**
	 * Delete the Specialization object with the given id from persistent storage.
	 *
	 * @param id  the id of the Specialization object to delete
	 * @return true if successful, otherwise false.
	 */
	public boolean deleteById(int id);

	/**
	 * Delete all Specialization objects from persistent storage.
	 *
	 * @return the number of instances that were deleted.
	 */
	public int deleteAll();
}
