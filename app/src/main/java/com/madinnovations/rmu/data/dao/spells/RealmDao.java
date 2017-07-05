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

import com.madinnovations.rmu.data.entities.spells.RealmDBO;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link RealmDBO} objects in persistent storage.
 */
public interface RealmDao {
	/**
	 * Retrieves a Realm object from persistent storage.
	 *
	 * @param id  the id of the Realm object to retrieve
	 * @return the Realm instance with the given id or null if not found.
	 */
	public RealmDBO getById(int id);

	/**
	 * Retrieves all Realm objects from persistent storage.
	 *
	 * @return  a List containing all Realm objects currently in persistent storage.
	 */
	public List<RealmDBO> getAll();

	/**
	 * Saves a collection of Realm instances to persistent storage.
	 *
	 * @param instance  the collection of Realm instances to be saved
	 * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<RealmDBO> instance, boolean isNew);

	/**
	 * Saves a collection of Realm instances to persistent storage.
	 *
	 * @param instance  the collection of Realm instances to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<RealmDBO> instance);

	/**
	 * Saves a Realm object to persistent storage.
	 *
	 * @param instance  the Realm object to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(RealmDBO instance);

	/**
	 * Delete the Realm object with the given id from persistent storage.
	 *
	 * @param id  the id of the Realm object to delete
	 * @return true if successful, otherwise false.
	 */
	public boolean deleteById(int id);

	/**
	 * Delete all Realm objects from persistent storage.
	 *
	 * @return the number of instances that were deleted.
	 */
	public int deleteAll();
}
