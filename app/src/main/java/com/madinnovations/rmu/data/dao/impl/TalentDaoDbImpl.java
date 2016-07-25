package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.TalentDao;
import com.madinnovations.rmu.data.dao.schemas.TalentSchema;
import com.madinnovations.rmu.data.entities.Talent;

import java.util.List;

/**
 * Methods for managing {@link Talent} objects in a SQLite database.
 */
public class TalentDaoDbImpl extends DbContentProvider implements TalentDao, TalentSchema {
    /**
     * Creates a new instance of TalentDaoDbImpl
     *
     * @param db  an SQLiteDatabase instance
     */
    public TalentDaoDbImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected Talent cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    public Talent getById(int id) {
        return null;
    }

    @Override
    public List<Talent> getAll() {
        return null;
    }

    @Override
    public boolean save(Talent instance) {
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
