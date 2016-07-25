package com.madinnovations.rmu.data.dao.combat.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.combat.CriticalResultDao;
import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalResultSchema;
import com.madinnovations.rmu.data.entities.combat.CriticalResult;

import java.util.List;

import javax.inject.Inject;

/**
 * Methods for managing {@link CriticalResult} objects in a SQLite database.
 */
public class CriticalResultDaoDbImpl extends BaseDaoDbImpl implements CriticalResultDao, CriticalResultSchema {
    /**
     * Creates a new instance of CriticalResultDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public CriticalResultDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
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
