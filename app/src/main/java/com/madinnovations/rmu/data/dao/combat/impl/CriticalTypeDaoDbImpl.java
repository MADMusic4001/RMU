package com.madinnovations.rmu.data.dao.combat.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.combat.CriticalTypeDao;
import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalTypeSchema;
import com.madinnovations.rmu.data.entities.combat.CriticalType;

import java.util.List;

import javax.inject.Inject;

/**
 * Methods for managing {@link CriticalType} objects in a SQLite database.
 */
public class CriticalTypeDaoDbImpl extends BaseDaoDbImpl implements CriticalTypeDao, CriticalTypeSchema {
    /**
     * Creates a new instance of CriticalTypeDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public CriticalTypeDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
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
