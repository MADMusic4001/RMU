package com.madinnovations.rmu.data.dao.item.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.item.ItemDao;
import com.madinnovations.rmu.data.dao.item.schemas.ItemSchema;
import com.madinnovations.rmu.data.entities.object.Item;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Item} objects in a SQLite database.
 */
@Singleton
public class ItemDaoDbImpl extends BaseDaoDbImpl implements ItemDao, ItemSchema {
    /**
     * Creates a new instance of ItemDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public ItemDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    protected Item cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    public Item getById(int id) {
        return null;
    }

    @Override
    public List<Item> getAll() {
        return null;
    }

    @Override
    public boolean save(Item instance) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public int deleteAll() {
        return 0;
    }
}
