package com.madinnovations.rmu.data.dao.combat;

import com.madinnovations.rmu.data.entities.combat.DamageTable;

import java.util.List;

/**
 * Methods to manage {@link DamageTable} objects in persistent storage.
 */
public interface DamageTableDao {
    /**
     * Retrieves a DamageTable object from persistent storage.
     *
     * @param id  the id of the DamageTable object to retrieve
     * @return the DamageTable instance with the given id or null if not found.
     */
    public DamageTable getById(int id);

    /**
     * Retrieves all DamageTable objects from persistent storage.
     *
     * @return  a List containing all DamageTable objects currently in persistent storage.
     */
    public List<DamageTable> getAll();

    /**
     * Saves a DamageTable object to persistent storage.
     *
     * @param instance  the DamageTable object to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(DamageTable instance);

    /**
     * Delete the DamageTable object with the given id from persistent storage.
     *
     * @param id  the id of the DamageTable object to delete
     * @return true if successful, otherwise false.
     */
    public boolean deleteById(int id);

    /**
     * Delete all DamageTable objects from persistent storage.
     *
     * @return true if successful, otherwise false.
     */
    public boolean deleteAll();
}
