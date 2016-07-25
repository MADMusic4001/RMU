package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.ProfessionDao;
import com.madinnovations.rmu.data.dao.schemas.ProfessionSchema;
import com.madinnovations.rmu.data.entities.Profession;

import java.util.List;

/**
 * Methods for managing {@link Profession} objects in a SQLite database.
 */
public class ProfessionDaoDbImpl extends DbContentProvider implements ProfessionDao, ProfessionSchema {
    /**
     * Creates a new instance of ProfessionDaoDbImpl
     *
     * @param db  an SQLiteDatabase instance
     */
    public ProfessionDaoDbImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected Profession cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    public Profession getById(int id) {
        return null;
    }

    @Override
    public List<Profession> getAll() {
        return null;
    }

    @Override
    public boolean save(Profession instance) {
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
