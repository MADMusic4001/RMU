package com.madinnovations.rmu.data.dao.item;

import com.madinnovations.rmu.data.entities.object.Item;

import java.util.List;

/**
 * Methods to manage {@link Item} objects in persistent storage.
 */
public interface ItemDao {
    /**
     * Retrieves a Item object from persistent storage.
     *
     * @param id  the id of the Item object to retrieve
     * @return the Item instance with the given id or null if not found.
     */
    public Item getById(int id);

    /**
     * Retrieves all Item objects from persistent storage.
     *
     * @return  a List containing all Item objects currently in persistent storage.
     */
    public List<Item> getAll();

    /**
     * Saves a Item object to persistent storage.
     *
     * @param instance  the Item object to be saved
     * @return true if successful, otherwise false.
     */
    public boolean save(Item instance);

    /**
     * Delete the Item object with the given id from persistent storage.
     *
     * @param id  the id of the Item object to delete
     * @return true if successful, otherwise false.
     */
    public boolean deleteById(int id);

    /**
     * Delete all Item objects from persistent storage.
     *
     * @return the number of instances that were deleted.
     */
    public int deleteAll();
}
