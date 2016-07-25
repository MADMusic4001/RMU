package com.madinnovations.rmu.data.dao;

import com.madinnovations.rmu.data.entities.CriticalResult;

import java.util.List;

/**
 * Methods to manage {@link CriticalResult} objects in persistent storage.
 */
public interface CriticalResultDao {
    /**
     * Retrieves a CriticalResult object from persistent storage.
     *
     * @param id  the id of the CriticalResult object to retrieve
     * @return the CriticalResult instance with the given id or null if not found.
     */
    public CriticalResult getById(int id);

    /**
     * Retrieves all CriticalResult objects from persistent storage.
     *
     * @return  a List containing all CriticalResult objects currently in persistent storage.
     */
    public List<CriticalResult> getAll();

    /**
     * Saves a CriticalResult object to persistent storage.
     *
     * @param instance  the CriticalResult object to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(CriticalResult instance);

    /**
     * Delete the CriticalResult object with the given id from persistent storage.
     *
     * @param id  the id of the CriticalResult object to delete
     * @return true if successful, otherwise false.
     */
    public boolean deleteById(int id);

    /**
     * Delete all CriticalResult objects from persistent storage.
     *
     * @return true if successful, otherwise false.
     */
    public boolean deleteAll();
}
