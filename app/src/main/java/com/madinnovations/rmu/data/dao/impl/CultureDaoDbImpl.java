package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.CultureDao;
import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.schemas.CultureSchema;
import com.madinnovations.rmu.data.entities.Culture;

import java.util.List;

/**
 * Methods for managing {@link Culture} objects in a SQLite database.
 */
public class CultureDaoDbImpl extends DbContentProvider implements CultureDao, CultureSchema {
    /**
     * Creates a new CultureDaoDbImpl instance
     *
     * @param db  the DbContentProvider to use for accessing the database
     */
    public CultureDaoDbImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public Culture getById(int id) {
        return null;
    }

    @Override
    public List<Culture> getAll() {
        return null;
    }

    @Override
    public boolean save(Culture instance) {
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

    @Override
    protected <T> T cursorToEntity(Cursor cursor) {
        return null;
    }
}
