package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.DamageResultDao;
import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.schemas.DamageResultSchema;
import com.madinnovations.rmu.data.entities.DamageResult;

import java.util.List;

/**
 * Methods for managing {@link DamageResult} objects in a SQLite database.
 */
public class DamageResultDaoDbImpl extends DbContentProvider implements DamageResultDao, DamageResultSchema {
    public DamageResultDaoDbImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public DamageResult getById(int id) {
        return null;
    }

    @Override
    public List<DamageResult> getAll() {
        return null;
    }

    @Override
    public boolean save(DamageResult instance) {
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
    protected DamageResult cursorToEntity(Cursor cursor) {
        return null;
    }
}
