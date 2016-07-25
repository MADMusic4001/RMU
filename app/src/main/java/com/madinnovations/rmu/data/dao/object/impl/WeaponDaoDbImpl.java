package com.madinnovations.rmu.data.dao.object.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.object.WeaponDao;
import com.madinnovations.rmu.data.dao.object.schemas.WeaponSchema;
import com.madinnovations.rmu.data.entities.object.Weapon;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Weapon} objects in a SQLite database.
 */
@Singleton
public class WeaponDaoDbImpl extends BaseDaoDbImpl implements WeaponDao, WeaponSchema {
    /**
     * Creates a new instance of WeaponDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public WeaponDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    protected Weapon cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    public Weapon getById(int id) {
        return null;
    }

    @Override
    public List<Weapon> getAll() {
        return null;
    }

    @Override
    public boolean save(Weapon instance) {
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
