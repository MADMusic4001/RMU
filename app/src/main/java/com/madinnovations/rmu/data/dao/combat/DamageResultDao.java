package com.madinnovations.rmu.data.dao.combat;

import com.madinnovations.rmu.data.entities.combat.DamageResult;

import java.util.List;

/**
 * Methods to manage {@link DamageResult} objects in persistent storage.
 */
public interface DamageResultDao {
    /**
     * Retrieves a DamageResult object from persistent storage.
     *
     * @param id  the id of the DamageResult object to retrieve
     * @return the DamageResult instance with the given id or null if not found.
     */
    public DamageResult getById(int id);

    /**
     * Retrieves all DamageResult objects from persistent storage.
     *
     * @return  a List containing all DamageResult objects currently in persistent storage.
     */
    public List<DamageResult> getAll();

    /**
     * Saves a DamageResult object to persistent storage.
     *
     * @param instance  the DamageResult object to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(DamageResult instance);

    /**
     * Delete the DamageResult object with the given id from persistent storage.
     *
     * @param id  the id of the DamageResult object to delete
     * @return true if successful, otherwise false.
     */
    public boolean deleteById(int id);

    /**
     * Delete all DamageResult objects from persistent storage.
     *
     * @return true if successful, otherwise false.
     */
    public boolean deleteAll();
}
