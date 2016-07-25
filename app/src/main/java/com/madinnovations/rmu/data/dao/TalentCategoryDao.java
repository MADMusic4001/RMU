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
package com.madinnovations.rmu.data.dao;

import com.madinnovations.rmu.data.entities.TalentCategory;

import java.util.List;

/**
 * Methods to manage {@link TalentCategory} objects in persistent storage.
 */
public interface TalentCategoryDao {
	/**
	 * Retrieves a TalentCategory object from persistent storage.
	 *
	 * @param id  the id of the TalentCategory object to retrieve
	 * @return the TalentCategory instance with the given id or null if not found.
	 */
	public TalentCategory getById(int id);

	/**
	 * Retrieves all TalentCategory objects from persistent storage.
	 *
	 * @return  a List containing all TalentCategory objects currently in persistent storage.
	 */
	public List<TalentCategory> getAll();

	/**
	 * Saves a TalentCategory object to persistent storage.
	 *
	 * @param instance  the TalentCategory object to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(TalentCategory instance);

	/**
	 * Delete the TalentCategory object with the given id from persistent storage.
	 *
	 * @param id  the id of the TalentCategory object to delete
	 * @return true if successful, otherwise false.
	 */
	public boolean deleteById(int id);

	/**
	 * Delete all TalentCategory objects from persistent storage.
	 *
	 * @return true if successful, otherwise false.
	 */
	public boolean deleteAll();
}
