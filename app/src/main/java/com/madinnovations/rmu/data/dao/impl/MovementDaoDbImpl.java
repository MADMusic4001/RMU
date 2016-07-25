package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.MovementDao;
import com.madinnovations.rmu.data.dao.schemas.MovementSchema;
import com.madinnovations.rmu.data.entities.Movement;

import java.util.List;

/**
 * Methods for managing {@link Movement} objects in a SQLite database.
 */
public class MovementDaoDbImpl extends DbContentProvider implements MovementDao, MovementSchema {
    /**
     * Creates a new instance of MovementDaoDbImpl
     *
     * @param db  an SQLiteDatabase instance
     */
    public MovementDaoDbImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected Movement cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    public Movement getById(int id) {
        return null;
    }

    @Override
    public List<Movement> getAll() {
        return null;
    }

    @Override
    public boolean save(Movement instance) {
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
