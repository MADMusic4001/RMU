package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.DamageTableDao;
import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.schemas.DamageTableSchema;
import com.madinnovations.rmu.data.entities.DamageTable;

import java.util.List;

/**
 * Methods for managing {@link DamageTable} objects in a SQLite database.
 */
public class DamageTableDaoDbImpl extends DbContentProvider implements DamageTableDao, DamageTableSchema {
    public DamageTableDaoDbImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public DamageTable getById(int id) {
        return null;
    }

    @Override
    public List<DamageTable> getAll() {
        return null;
    }

    @Override
    public boolean save(DamageTable instance) {
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
    protected DamageTable cursorToEntity(Cursor cursor) {
        return null;
    }
}
