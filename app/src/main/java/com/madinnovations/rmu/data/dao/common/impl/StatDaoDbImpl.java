package com.madinnovations.rmu.data.dao.common.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.schemas.StatSchema;
import com.madinnovations.rmu.data.entities.common.Stat;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Stat} objects in a SQLite database.
 */
@Singleton
public class StatDaoDbImpl extends BaseDaoDbImpl implements StatDao, StatSchema {
    /**
     * Creates a new instance of StatDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public StatDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    protected Stat cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    public Stat getById(int id) {
        return null;
    }

    @Override
    public List<Stat> getAll() {
        return null;
    }

    @Override
    public boolean save(Stat instance) {
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
