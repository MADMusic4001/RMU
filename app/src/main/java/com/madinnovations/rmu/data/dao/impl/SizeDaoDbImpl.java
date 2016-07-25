package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.SizeDao;
import com.madinnovations.rmu.data.dao.schemas.SizeSchema;
import com.madinnovations.rmu.data.entities.Size;

import java.util.List;

/**
 * Methods for managing {@link Size} objects in a SQLite database.
 */
public class SizeDaoDbImpl extends DbContentProvider implements SizeDao, SizeSchema {
    /**
     * Creates a new instance of SizeDaoDbImpl
     *
     * @param db  an SQLiteDatabase instance
     */
    public SizeDaoDbImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected Size cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    public Size getById(int id) {
        return null;
    }

    @Override
    public List<Size> getAll() {
        return null;
    }

    @Override
    public boolean save(Size instance) {
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
