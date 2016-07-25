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

import com.madinnovations.rmu.data.entities.common.Movement;

import java.util.List;

/**
 * Methods to manage {@link Movement} objects in persistent storage.
 */
public interface MovementDao {
	/**
	 * Retrieves a Movement object from persistent storage.
	 *
	 * @param id  the id of the Movement object to retrieve
	 * @return the Movement instance with the given id or null if not found.
	 */
	public Movement getById(int id);

	/**
	 * Retrieves all Movement objects from persistent storage.
	 *
	 * @return  a List containing all Movement objects currently in persistent storage.
	 */
	public List<Movement> getAll();

	/**
	 * Saves a Movement object to persistent storage.
	 *
	 * @param instance  the Movement object to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Movement instance);

	/**
	 * Delete the Movement object with the given id from persistent storage.
	 *
	 * @param id  the id of the Movement object to delete
	 * @return true if successful, otherwise false.
	 */
	public boolean deleteById(int id);

	/**
	 * Delete all Movement objects from persistent storage.
	 *
	 * @return true if successful, otherwise false.
	 */
	public boolean deleteAll();
}
