package com.madinnovations.rmu.data.dao.character.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.ProfessionDao;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSchema;
import com.madinnovations.rmu.data.entities.character.Profession;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Profession} objects in a SQLite database.
 */
@Singleton
public class ProfessionDaoDbImpl extends BaseDaoDbImpl implements ProfessionDao, ProfessionSchema {
    /**
     * Creates a new instance of ProfessionDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public ProfessionDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    protected Profession cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    public Profession getById(int id) {
        return null;
    }

    @Override
    public List<Profession> getAll() {
        return null;
    }

    @Override
    public boolean save(Profession instance) {
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
}
