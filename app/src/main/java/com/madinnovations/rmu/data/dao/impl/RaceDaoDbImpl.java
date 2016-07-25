package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.RaceDao;
import com.madinnovations.rmu.data.dao.schemas.RaceSchema;
import com.madinnovations.rmu.data.entities.Race;

import java.util.List;

/**
 * Methods for managing {@link Race} objects in a SQLite database.
 */
public class RaceDaoDbImpl extends DbContentProvider implements RaceDao, RaceSchema {
    /**
     * Creates a new instance of RaceDaoDbImpl
     *
     * @param db  an SQLiteDatabase instance
     */
    public RaceDaoDbImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected Race cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    public Race getById(int id) {
        return null;
    }

    @Override
    public List<Race> getAll() {
        return null;
    }

    @Override
    public boolean save(Race instance) {
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
