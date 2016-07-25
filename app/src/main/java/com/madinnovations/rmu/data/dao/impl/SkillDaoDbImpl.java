package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.SkillDao;
import com.madinnovations.rmu.data.dao.schemas.SkillSchema;
import com.madinnovations.rmu.data.entities.Skill;

import java.util.List;

/**
 * Methods for managing {@link Skill} objects in a SQLite database.
 */
public class SkillDaoDbImpl extends DbContentProvider implements SkillDao, SkillSchema {
    /**
     * Creates a new instance of SkillDaoDbImpl
     *
     * @param db  an SQLiteDatabase instance
     */
    public SkillDaoDbImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected Skill cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    public Skill getById(int id) {
        return null;
    }

    @Override
    public List<Skill> getAll() {
        return null;
    }

    @Override
    public boolean save(Skill instance) {
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
