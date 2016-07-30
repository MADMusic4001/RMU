package com.madinnovations.rmu.data.dao.item.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.item.WeaponDao;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponSchema;
import com.madinnovations.rmu.data.entities.object.Weapon;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Weapon} objects in a SQLite database.
 */
@Singleton
public class WeaponDaoDbImpl extends BaseDaoDbImpl<Weapon> implements WeaponDao, WeaponSchema {
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
    public int deleteAll() {
        return 0;
    }

    @Override
    protected Weapon cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    protected ContentValues getContentValues(Weapon instance) {
        return null;
    }
}
