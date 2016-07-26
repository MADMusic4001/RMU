package com.madinnovations.rmu.data.dao.common.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCategorySchema;
import com.madinnovations.rmu.data.entities.common.SkillCategory;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link SkillCategory} objects in a SQLite database.
 */
@Singleton
public class SkillCategoryDaoDbImpl extends BaseDaoDbImpl implements SkillCategoryDao, SkillCategorySchema {
    /**
     * Creates a new instance of SkillCategoryDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public SkillCategoryDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    protected SkillCategory cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    public SkillCategory getById(int id) {
        return null;
    }

    @Override
    public List<SkillCategory> getAll() {
        return null;
    }

    @Override
    public boolean save(SkillCategory instance) {
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
