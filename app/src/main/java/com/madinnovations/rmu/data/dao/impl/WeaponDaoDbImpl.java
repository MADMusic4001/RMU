package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.WeaponDao;
import com.madinnovations.rmu.data.dao.schemas.WeaponSchema;
import com.madinnovations.rmu.data.entities.Weapon;

import java.util.List;

/**
 * Methods for managing {@link Weapon} objects in a SQLite database.
 */
public class WeaponDaoDbImpl extends DbContentProvider implements WeaponDao, WeaponSchema {
    public WeaponDaoDbImpl(SQLiteDatabase db) {
        super(db);
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
