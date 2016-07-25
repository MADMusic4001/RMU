package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.BodyPartDao;
import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.schemas.BodyPartSchema;
import com.madinnovations.rmu.data.entities.BodyPart;

import java.util.List;

/**
 * Methods for managing {@link BodyPart} objects in a SQLite database.
 */
public class BodyPartDaoDbImpl extends DbContentProvider implements BodyPartDao, BodyPartSchema {
    public BodyPartDaoDbImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public BodyPart getById(int id) {
        return null;
    }

    @Override
    public List<BodyPart> getAll() {
        return null;
    }

    @Override
    public boolean save(BodyPart instance) {
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
    protected BodyPart cursorToEntity(Cursor cursor) {
        return null;
    }
}
