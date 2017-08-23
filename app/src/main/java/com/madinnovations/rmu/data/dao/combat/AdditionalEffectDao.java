/*
 *
 *   Copyright (C) 2017 MadInnovations
 *   <p/>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   <p/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   <p/>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  
 *
 */
package com.madinnovations.rmu.data.dao.combat;

import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.entities.combat.AdditionalEffect;
import com.madinnovations.rmu.data.entities.combat.CriticalResult;
import com.madinnovations.rmu.data.entities.combat.Fumble;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link AdditionalEffect} objects in persistent storage.
 */
public interface AdditionalEffectDao {
	/**
	 * Retrieves an AdditionalEffect object from persistent storage.
	 *
	 * @param id  the id of the AdditionalEffect object to retrieve
	 * @return the AdditionalEffect instance with the given id or null if not found.
	 */
	AdditionalEffect getById(int id);

	/**
	 * Retrieves all AdditionalEffect objects from persistent storage.
	 *
	 * @return  a List containing all AdditionalEffect objects currently in persistent storage.
	 */
	List<AdditionalEffect> getAll();

	/**
	 * Saves a collection of AdditionalEffect instances to persistent storage.
	 *
	 * @param instance  the collection of AdditionalEffect instances to be saved
	 * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
	 * @return true if successful, otherwise false.
	 */
	boolean save(Collection<AdditionalEffect> instance, boolean isNew);

	/**
	 * Saves a collection of AdditionalEffect instances to persistent storage.
	 *
	 * @param instance  the collection of AdditionalEffect instances to be saved
	 * @return true if successful, otherwise false.
	 */
	boolean save(Collection<AdditionalEffect> instance);

	/**
	 * Saves a AdditionalEffect object to persistent storage.
	 *
	 * @param instance  the AdditionalEffect object to be saved
	 * @return true if successful, otherwise false.
	 */
	boolean save(AdditionalEffect instance);

	/**
	 * Delete the AdditionalEffect object with the given id from persistent storage.
	 *
	 * @param id  the id of the AdditionalEffect object to delete
	 * @return true if successful, otherwise false.
	 */
	boolean deleteById(int id);

	/**
	 * Delete all AdditionalEffect objects from persistent storage.
	 *
	 * @return the number of instances that were deleted.
	 */
	int deleteAll();

	/**
	 * Retrieves all AdditionalEffect objects from persistent storage for the given CriticalResult instance.
	 *
	 * @param criticalResult  a CriticalResult instance
	 * @return  a List containing all AdditionalEffect objects currently in persistent storage for the given CriticalResult
	 * instance.
	 */
	List<AdditionalEffect> getForCriticalResult(@NonNull CriticalResult criticalResult);

	/**
	 * Retrieves all AdditionalEffect objects from persistent storage for the given Fumble instance.
	 *
	 * @param fumble  a Fumble instance
	 * @return  a List containing all AdditionalEffect objects currently in persistent storage for the given Fumble instance.
	 */
	List<AdditionalEffect> getForFumble(@NonNull Fumble fumble);
}
