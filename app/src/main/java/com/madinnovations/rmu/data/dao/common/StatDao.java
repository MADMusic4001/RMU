package com.madinnovations.rmu.data.dao.common;

import com.madinnovations.rmu.data.entities.common.Stat;

import java.util.List;

/**
 * Methods to manage {@link Stat} objects in persistent storage.
 */
public interface StatDao {
    /**
     * Retrieves a Skill object from persistent storage.
     *
     * @param id  the id of the Skill object to retrieve
     * @return the Skill instance with the given id or null if not found.
     */
    public Stat getById(int id);

    /**
     * Retrieves all Skill objects from persistent storage.
     *
     * @return  a List containing all Skill objects currently in persistent storage.
     */
    public List<Stat> getAll();

    /**
     * Saves a Skill object to persistent storage.
     *
     * @param instance  the Skill object to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(Stat instance);

    /**
     * Delete the Skill object with the given id from persistent storage.
     *
     * @param id  the id of the Skill object to delete
     * @return true if successful, otherwise false.
     */
    public boolean deleteById(int id);

    /**
     * Delete all Skill objects from persistent storage.
     *
     * @return true if successful, otherwise false.
     */
    public boolean deleteAll();
}
