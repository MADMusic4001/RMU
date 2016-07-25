package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.ItemDao;
import com.madinnovations.rmu.data.dao.schemas.ItemSchema;
import com.madinnovations.rmu.data.entities.Item;

import java.util.List;

/**
 * Methods for managing {@link Item} objects in a SQLite database.
 */
public class ItemDaoDbImpl extends DbContentProvider implements ItemDao, ItemSchema {
    public ItemDaoDbImpl(SQLiteDatabase db) {
        super(db);
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
    public boolean deleteAll() {
        return false;
    }
}
