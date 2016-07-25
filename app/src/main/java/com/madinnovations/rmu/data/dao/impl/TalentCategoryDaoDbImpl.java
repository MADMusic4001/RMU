package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.TalentCategoryDao;
import com.madinnovations.rmu.data.dao.schemas.TalentCategorySchema;
import com.madinnovations.rmu.data.entities.TalentCategory;

import java.util.List;

/**
 * Methods for managing {@link TalentCategory} objects in a SQLite database.
 */
public class TalentCategoryDaoDbImpl extends DbContentProvider implements TalentCategoryDao, TalentCategorySchema {
    /**
     * Creates a new instance of TalentCategoryDaoDbImpl
     *
     * @param db  an SQLiteDatabase instance
     */
    public TalentCategoryDaoDbImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected TalentCategory cursorToEntity(Cursor cursor) {
        return null;
    }

    @Override
    public TalentCategory getById(int id) {
        return null;
    }

    @Override
    public List<TalentCategory> getAll() {
        return null;
    }

    @Override
    public boolean save(TalentCategory instance) {
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
