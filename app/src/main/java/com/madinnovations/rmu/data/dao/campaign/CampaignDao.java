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
package com.madinnovations.rmu.data.dao.campaign;

import com.madinnovations.rmu.data.entities.campaign.Campaign;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link Campaign} objects in persistent storage.
 */
public interface CampaignDao {
	/**
	 * Retrieves a Campaign object from persistent storage.
	 *
	 * @param id  the id of the Campaign object to retrieve
	 * @return the Campaign instance with the given id or null if not found.
	 */
	public Campaign getById(int id);

	/**
	 * Retrieves all Campaign objects from persistent storage.
	 *
	 * @return  a List containing all Campaign objects currently in persistent storage.
	 */
	public List<Campaign> getAll();

	/**
	 * Saves a collection of Campaign instances to persistent storage.
	 *
	 * @param instance  the collection of Campaign instances to be saved
	 * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<Campaign> instance, boolean isNew);

	/**
	 * Saves a collection of Campaign instances to persistent storage.
	 *
	 * @param instance  the collection of Campaign instances to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<Campaign> instance);

	/**
	 * Saves a Campaign object to persistent storage.
	 *
	 * @param instance  the Campaign object to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Campaign instance);

	/**
	 * Saves a Campaign object to persistent storage.
	 *
	 * @param instance  the Campaign object to be saved
	 * @param isNew  set to true if the instance has a valid IDs but should be inserted instead of updated.
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Campaign instance, boolean isNew);

	/**
	 * Delete the Campaign object with the given id from persistent storage.
	 *
	 * @param id  the id of the Campaign object to delete
	 * @return true if successful, otherwise false.
	 */
	public boolean deleteById(int id);

	/**
	 * Delete all Campaign objects from persistent storage.
	 *
	 * @return the number of instances that were deleted.
	 */
	public int deleteAll();
}
