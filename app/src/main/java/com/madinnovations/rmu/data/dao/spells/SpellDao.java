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

import com.madinnovations.rmu.data.entities.spells.Spell;
import com.madinnovations.rmu.data.entities.spells.SpellList;

import java.util.Collection;
import java.util.List;

/**
 * Methods to manage {@link Spell} objects in persistent storage.
 */
public interface SpellDao {
	/**
	 * Retrieves a Spell object from persistent storage.
	 *
	 * @param id  the id of the Spell object to retrieve
	 * @return the Spell instance with the given id or null if not found.
	 */
	public Spell getById(int id);

	/**
	 * Retrieves all Spell objects from persistent storage.
	 *
	 * @return  a List containing all Spell objects currently in persistent storage.
	 */
	public List<Spell> getAll();

	/**
	 * Saves a collection of Spell instances to persistent storage.
	 *
	 * @param instance  the collection of Spell instances to be saved
	 * @param isNew  set to true if the instances have valid IDs but should be inserted instead of updated.
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<Spell> instance, boolean isNew);

	/**
	 * Saves a collection of Spell instances to persistent storage.
	 *
	 * @param instance  the collection of Spell instances to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Collection<Spell> instance);

	/**
	 * Saves a Spell object to persistent storage.
	 *
	 * @param instance  the Spell object to be saved
	 * @return true if successful, otherwise false.
	 */
	public boolean save(Spell instance);

	/**
	 * Delete the Spell object with the given id from persistent storage.
	 *
	 * @param id  the id of the Spell object to delete
	 * @return true if successful, otherwise false.
	 */
	public boolean deleteById(int id);

	/**
	 * Delete all Spell objects from persistent storage.
	 *
	 * @return the number of instances that were deleted.
	 */
	public int deleteAll();

	/**
	 * Retrieves all Spell objects that reference the given SpellList from persistent storage.
	 *
	 * @param filter  a SpellList instance whose ID will be used to filter the Spell objects returned by the query
	 * @return  a List containing all Spell objects that reference the given SpellList currently in persistent storage.
	 */
	public List<Spell> getSpellsForList(SpellList filter);
}
