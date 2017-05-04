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
package com.madinnovations.rmu.data.dao.combat;

import com.madinnovations.rmu.data.entities.combat.Attack;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link Attack} objects in persistent storage.
 */
public interface AttackDao {
	/**
	 * Retrieves an Attack object from persistent storage.
	 *
	 * @param id  the id of the Attack object to retrieve
	 * @return the Attack instance with the given id or null if not found.
	 */
	Attack getById(int id);

	/**
	 * Retrieves an Attack object from persistent storage.
	 *
	 * @param code  the code of the Attack object to retrieve
	 * @return the Attack instance with the given code or null if not found.
	 */
	Attack getByCode(String code);

	/**
	 * Retrieves all Attack objects from persistent storage.
	 *
	 * @return  a List containing all Attack objects currently in persistent storage.
	 */
	List<Attack> getAll();

	/**
	 * Saves a collection of Attack instances to persistent storage.
	 *
	 * @param instance  the collection of Attack instances to be saved
	 * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
	 * @return true if successful, otherwise false.
	 */
	boolean save(Collection<Attack> instance, boolean isNew);

	/**
	 * Saves a collection of Attack instances to persistent storage.
	 *
	 * @param instance  the collection of Attack instances to be saved
	 * @return true if successful, otherwise false.
	 */
	boolean save(Collection<Attack> instance);

	/**
	 * Saves a Attack object to persistent storage.
	 *
	 * @param instance  the Attack object to be saved
	 * @return true if successful, otherwise false.
	 */
	boolean save(Attack instance);

	/**
	 * Delete the Attack object with the given id from persistent storage.
	 *
	 * @param id  the id of the Attack object to delete
	 * @return true if successful, otherwise false.
	 */
	boolean deleteById(int id);

	/**
	 * Delete all Attack objects from persistent storage.
	 *
	 * @return the number of instances that were deleted.
	 */
	int deleteAll();
}
