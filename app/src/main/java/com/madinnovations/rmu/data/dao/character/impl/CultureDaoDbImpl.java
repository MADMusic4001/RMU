package com.madinnovations.rmu.data.dao.character.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.character.CultureDao;
import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSchema;
import com.madinnovations.rmu.data.entities.character.Culture;

import java.util.List;

/**
 * Methods for managing {@link Culture} objects in a SQLite database.
 */
public class CultureDaoDbImpl extends BaseDaoDbImpl implements CultureDao, CultureSchema {
    /**
     * Creates a new CultureDaoDbImpl instance
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    public CultureDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    public Culture getById(int id) {
        return null;
    }

    @Override
    public List<Culture> getAll() {
        return null;
    }

    @Override
    public boolean save(Culture instance) {
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
    protected <T> T cursorToEntity(Cursor cursor) {
        return null;
    }
}
