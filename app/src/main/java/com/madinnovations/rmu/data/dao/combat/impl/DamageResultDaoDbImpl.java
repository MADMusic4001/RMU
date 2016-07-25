package com.madinnovations.rmu.data.dao.combat.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultSchema;
import com.madinnovations.rmu.data.entities.combat.DamageResult;

import java.util.List;

import javax.inject.Inject;

/**
 * Methods for managing {@link DamageResult} objects in a SQLite database.
 */
public class DamageResultDaoDbImpl extends BaseDaoDbImpl implements DamageResultDao, DamageResultSchema {
    /**
     * Creates a new instance of DamageResultDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public DamageResultDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
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
