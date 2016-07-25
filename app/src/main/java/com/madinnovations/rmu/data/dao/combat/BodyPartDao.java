package com.madinnovations.rmu.data.dao.combat;

import com.madinnovations.rmu.data.entities.combat.BodyPart;

import java.util.List;

/**
 * Methods to manage {@link BodyPart} objects in persistent storage.
 */
public interface BodyPartDao {
    /**
     * Retrieves a BodyPart object from persistent storage.
     *
     * @param id  the id of the BodyPart object to retrieve
     * @return the BodyPart instance with the given id or null if not found.
     */
    public BodyPart getById(int id);

    /**
     * Retrieves all BodyPart objects from persistent storage.
     *
     * @return  a List containing all BodyPart objects currently in persistent storage.
     */
    public List<BodyPart> getAll();

    /**
     * Saves a BodyPart object to persistent storage.
     *
     * @param instance  the BodyPart object to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(BodyPart instance);

    /**
     * Delete the BodyPart object with the given id from persistent storage.
     *
     * @param id  the id of the BodyPart object to delete
     * @return true if successful, otherwise false.
     */
    public boolean deleteById(int id);

    /**
     * Delete all BodyPart objects from persistent storage.
     *
     * @return true if successful, otherwise false.
     */
    public boolean deleteAll();
}
