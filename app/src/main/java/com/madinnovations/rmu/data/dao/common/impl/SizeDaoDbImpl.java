package com.madinnovations.rmu.data.dao.common.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.SizeDao;
import com.madinnovations.rmu.data.dao.common.schemas.SizeSchema;
import com.madinnovations.rmu.data.entities.common.Size;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Size} objects in a SQLite database.
 */
@Singleton
public class SizeDaoDbImpl extends BaseDaoDbImpl implements SizeDao, SizeSchema {
    /**
     * Creates a new instance of SizeDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public SizeDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
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
