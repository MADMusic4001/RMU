package com.madinnovations.rmu.data.dao;

import com.madinnovations.rmu.data.entities.CriticalType;

import java.util.List;

/**
 * Methods to manage {@link CriticalType} objects in persistent storage.
 */
public interface CriticalTypeDao {
    /**
     * Retrieves a CriticalType object from persistent storage.
     *
     * @param id  the id of the CriticalType object to retrieve
     * @return the CriticalType instance with the given id or null if not found.
     */
    public CriticalType getById(int id);

    /**
     * Retrieves all CriticalType objects from persistent storage.
     *
     * @return  a List containing all CriticalType objects currently in persistent storage.
     */
    public List<CriticalType> getAll();

    /**
     * Saves a CriticalType object to persistent storage.
     *
     * @param instance  the CriticalType object to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(CriticalType instance);

    /**
     * Delete the CriticalType object with the given id from persistent storage.
     *
     * @param id  the id of the CriticalType object to delete
     * @return true if successful, otherwise false.
     */
    public boolean deleteById(int id);

    /**
     * Delete all CriticalType objects from persistent storage.
     *
     * @return true if successful, otherwise false.
     */
    public boolean deleteAll();
}
