package com.madinnovations.rmu.data.dao.common.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.SkillCostDao;
import com.madinnovations.rmu.data.entities.common.SkillCost;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link SkillCost} objects in a SQLite database.
 */
@Singleton
public class SkillCostDaoDbImpl extends BaseDaoDbImpl implements SkillCostDao {
    /**
     * Creates a new instance of SkillCostDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public SkillCostDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
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
    public int deleteAll() {
        return 0;
    }
}
