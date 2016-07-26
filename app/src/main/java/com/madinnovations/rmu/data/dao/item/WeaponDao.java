package com.madinnovations.rmu.data.dao.item;

import com.madinnovations.rmu.data.entities.object.Weapon;

import java.util.List;

/**
 * Methods to manage {@link Weapon} objects in persistent storage.
 */
public interface WeaponDao {
    /**
     * Retrieves a Weapon object from persistent storage.
     *
     * @param id  the id of the Weapon object to retrieve
     * @return the Weapon instance with the given id or null if not found.
     */
    public Weapon getById(int id);

    /**
     * Retrieves all Weapon objects from persistent storage.
     *
     * @return  a List containing all Weapon objects currently in persistent storage.
     */
    public List<Weapon> getAll();

    /**
     * Saves a Weapon object to persistent storage.
     *
     * @param instance  the Weapon object to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(Weapon instance);

    /**
     * Delete the Weapon object with the given id from persistent storage.
     *
     * @param id  the id of the Weapon object to delete
     * @return true if successful, otherwise false.
     */
    public boolean deleteById(int id);

    /**
     * Delete all Weapon objects from persistent storage.
     *
     * @return the number of instances that were deleted.
     */
    public int deleteAll();
}
