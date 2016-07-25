package com.madinnovations.rmu.data.dao.combat.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.combat.BodyPartDao;
import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.schemas.BodyPartSchema;
import com.madinnovations.rmu.data.entities.combat.BodyPart;

import java.util.List;

import javax.inject.Inject;

/**
 * Methods for managing {@link BodyPart} objects in a SQLite database.
 */
public class BodyPartDaoDbImpl extends BaseDaoDbImpl implements BodyPartDao, BodyPartSchema {
    /**
     * Creates a new instance of BodyPartDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public BodyPartDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
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
