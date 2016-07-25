package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.SkillCostDao;
import com.madinnovations.rmu.data.entities.SkillCost;

import java.util.List;

/**
 * Methods for managing {@link SkillCost} objects in a SQLite database.
 */
public class SkillCostDaoDbImpl extends DbContentProvider implements SkillCostDao {
    /**
     * Creates a new instance of SkillCostDaoDbImpl
     *
     * @param db  an SQLiteDatabase instance
     */
    public SkillCostDaoDbImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected SkillCost cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    public SkillCost getById(int id) {
        return null;
    }

    @Override
    public List<SkillCost> getAll() {
        return null;
    }

    @Override
    public boolean save(SkillCost instance) {
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
