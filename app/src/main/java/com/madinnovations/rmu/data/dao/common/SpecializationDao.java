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
package com.madinnovations.rmu.data.dao.common;

import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Specialization;

import java.util.Collection;
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
	 * Retrieves a Specialization object from persistent storage.
	 *
	 * @param name  the name of the Specialization object to retrieve
	 * @return the Specialization instance with the given name or null if not found.
	 */
	public Specialization getByName(String name);

	/**
	 * Retrieves all Specialization objects from persistent storage.
	 *
	 * @return  a List containing all Specialization objects currently in persistent storage.
	 */
	public List<Specialization> getAll();

	/**
	 * Saves a collection of Specialization instances to persistent storage.
	 *
	 * @param instance  the collection of Specialization instances to be saved
	 * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<Specialization> instance, boolean isNew);

	/**
	 * Saves a collection of Specialization instances to persistent storage.
	 *
	 * @param instance  the collection of Specialization instances to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<Specialization> instance);

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

	/**
	 * Retrieves all Specialization objects that reference the given skill from persistent storage.
	 *
	 * @param filter  a Skill instance whose ID will be used to filter the Specialization objects returned by the query
	 * @return  a List containing all Specialization objects that reference the given skill currently in persistent storage.
	 */
	public List<Specialization> getSpecializationsForSkill(Skill filter);

	/**
	 * Retrieves all Specialization objects that reference the given skill and are not creature only from persistent storage.
	 *
	 * @param filter  a Skill instance whose ID will be used to filter the Specialization objects returned by the query
	 * @return  a List containing all Specialization objects that reference the given skill currently in persistent storage.
	 */
	public List<Specialization> getCharacterSpecializationsForSkill(Skill filter);

	/**
	 * Retrieves all Specialization objects that a player character can purchase.
	 *
	 * @return  a Collection of Specialization instances.
	 */
	public Collection<Specialization> getCharacterPurchasableSpecializations();

	/**
	 * Retrieves all Specializations for weapon skills.
	 *
	 * @return  a Collection of Specialization instances.
	 */
	public Collection<Specialization> getWeaponSpecializations();
}
