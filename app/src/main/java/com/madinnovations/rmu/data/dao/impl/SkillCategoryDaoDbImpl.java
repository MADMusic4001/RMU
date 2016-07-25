package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.schemas.SkillCategorySchema;
import com.madinnovations.rmu.data.entities.SkillCategory;

import java.util.List;

/**
 * Methods for managing {@link SkillCategory} objects in a SQLite database.
 */
public class SkillCategoryDaoDbImpl extends DbContentProvider implements SkillCategoryDao, SkillCategorySchema {
    /**
     * Creates a new instance of SkillCategoryDaoDbImpl
     *
     * @param db  an SQLiteDatabase instance
     */
    public SkillCategoryDaoDbImpl(SQLiteDatabase db) {
        super(db);
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
    public boolean deleteAll() {
        return false;
    }
}
