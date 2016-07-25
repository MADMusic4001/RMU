package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.CriticalTypeDao;
import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.schemas.CriticalTypeSchema;
import com.madinnovations.rmu.data.entities.CriticalType;

import java.util.List;

/**
 * Methods for managing {@link CriticalType} objects in a SQLite database.
 */
public class CriticalTypeDaoDbImpl extends DbContentProvider implements CriticalTypeDao, CriticalTypeSchema {
    public CriticalTypeDaoDbImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public CriticalType getById(int id) {
        return null;
    }

    @Override
    public List<CriticalType> getAll() {
        return null;
    }

    @Override
    public boolean save(CriticalType instance) {
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
    protected CriticalType cursorToEntity(Cursor cursor) {
        return null;
    }
}
