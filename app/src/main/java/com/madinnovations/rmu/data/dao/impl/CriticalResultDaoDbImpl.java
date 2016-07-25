package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.CriticalResultDao;
import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.schemas.CriticalResultSchema;
import com.madinnovations.rmu.data.entities.CriticalResult;

import java.util.List;

/**
 * Methods for managing {@link CriticalResult} objects in a SQLite database.
 */
public class CriticalResultDaoDbImpl extends DbContentProvider implements CriticalResultDao, CriticalResultSchema {
    public CriticalResultDaoDbImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public CriticalResult getById(int id) {
        return null;
    }

    @Override
    public List<CriticalResult> getAll() {
        return null;
    }

    @Override
    public boolean save(CriticalResult instance) {
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
    protected CriticalResult cursorToEntity(Cursor cursor) {
        return null;
    }
}
