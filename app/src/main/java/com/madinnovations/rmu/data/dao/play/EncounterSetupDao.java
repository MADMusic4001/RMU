/**
 * Copyright (C) 2017 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.dao.play;

import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link EncounterSetup} objects in persistent storage.
 */
public interface EncounterSetupDao {
	/**
	 * Retrieves a CombatSetup object from persistent storage.
	 *
	 * @param id  the id of the CombatSetup object to retrieve
	 * @return the CombatSetup instance with the given id or null if not found.
	 */
	public EncounterSetup getById(int id);

	/**
	 * Retrieves all CombatSetup objects from persistent storage.
	 *
	 * @return  a List containing all CombatSetup objects currently in persistent storage.
	 */
	public List<EncounterSetup> getAll();

	/**
	 * Saves a collection of CombatSetup instances to persistent storage.
	 *
	 * @param instance  the collection of CombatSetup instances to be saved
	 * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<EncounterSetup> instance, boolean isNew);

	/**
	 * Saves a collection of CombatSetup instances to persistent storage.
	 *
	 * @param instance  the collection of CombatSetup instances to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<EncounterSetup> instance);

	/**
	 * Saves a CombatSetup object to persistent storage.
	 *
	 * @param instance  the CombatSetup object to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(EncounterSetup instance);

	/**
	 * Delete the CombatSetup object with the given id from persistent storage.
	 *
	 * @param id  the id of the CombatSetup object to delete
	 * @return true if successful, otherwise false.
	 */
	public boolean deleteById(int id);

	/**
	 * Delete all CombatSetup objects from persistent storage.
	 *
	 * @return the number of instances that were deleted.
	 */
	public int deleteAll();

	/**
	 * Retrieves a CombatSetup object from persistent storage.
	 *
	 * @param campaign  a {@link Campaign} instance
	 * @return the CombatSetup instance with the most recent timestamp.
	 */
	public EncounterSetup getMostRecentForCampaign(Campaign campaign);

	/**
	 * Retrieves all CombatSetup objects from persistent storage for the given campaign.
	 *
	 * @param campaign  a {@link Campaign} instance
	 * @return the collection of CombatSetup instances for the given campaign
	 */
	public Collection<EncounterSetup> getAllForCampaign(Campaign campaign);
}
