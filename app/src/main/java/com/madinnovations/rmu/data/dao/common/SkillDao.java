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

import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link Skill} objects in persistent storage.
 */
public interface SkillDao {
	/**
	 * Retrieves a Skill object from persistent storage.
	 *
	 * @param id  the id of the Skill object to retrieve
	 * @return the Skill instance with the given id or null if not found.
	 */
	public Skill getById(int id);

	/**
	 * Retrieves all Skill objects from persistent storage.
	 *
	 * @return  a List containing all Skill objects currently in persistent storage.
	 */
	public List<Skill> getAll();

	/**
	 * Saves a collection of Skill instances to persistent storage.
	 *
	 * @param instance  the collection of Skill instances to be saved
	 * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<Skill> instance, boolean isNew);

	/**
	 * Saves a collection of Skill instances to persistent storage.
	 *
	 * @param instance  the collection of Skill instances to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<Skill> instance);

	/**
	 * Saves a Skill object to persistent storage.
	 *
	 * @param instance  the Skill object to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Skill instance);

	/**
	 * Delete the Skill object with the given id from persistent storage.
	 *
	 * @param id  the id of the Skill object to delete
	 * @return true if successful, otherwise false.
	 */
	public boolean deleteById(int id);

	/**
	 * Delete all Skill objects from persistent storage.
	 *
	 * @return the number of instances that were deleted.
	 */
	public int deleteAll();

	/**
	 * Retrieves all Skill objects that require specialization from persistent storage.
	 *
	 * @return  a List containing all Skill objects that require specialization currently in persistent storage.
	 */
	public List<Skill> getSpecializationSkills();

	/**
	 * Retrieves all Skill objects that do not require specialization from persistent storage.
	 *
	 * @return  a List containing all Skill objects that do not require specialization currently in persistent storage.
	 */
	public List<Skill> getNonSpecializationSkills();

	/**
	 * Retrieves all Skill objects that reference the given SkillCategory from persistent storage.
	 *
	 * @param filter  a SkillCategory instance whose ID will be used to filter the Skill objects returned by the query
	 * @return  a List containing all Skill objects that reference the given SkillCategory currently in persistent storage.
	 */
	public List<Skill> getSkillsForCategory(SkillCategory filter);
}
