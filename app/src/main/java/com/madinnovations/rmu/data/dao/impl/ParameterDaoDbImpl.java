package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.ParameterDao;
import com.madinnovations.rmu.data.dao.schemas.ParameterSchema;
import com.madinnovations.rmu.data.entities.Parameter;

import java.util.List;

/**
 * Methods for managing {@link Parameter} objects in a SQLite database.
 */
public class ParameterDaoDbImpl extends DbContentProvider implements ParameterDao, ParameterSchema {
    /**
     * Creates a new instance of ParameterDaoDbImpl
     *
     * @param db  an SQLiteDatabase instance
     */
    public ParameterDaoDbImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected Parameter cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    public Parameter getById(int id) {
        return null;
    }

    @Override
    public List<Parameter> getAll() {
        return null;
    }

    @Override
    public boolean save(Parameter instance) {
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
