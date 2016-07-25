package com.madinnovations.rmu.data.dao.common.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.TalentCategoryDao;
import com.madinnovations.rmu.data.dao.common.schemas.TalentCategorySchema;
import com.madinnovations.rmu.data.entities.common.TalentCategory;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link TalentCategory} objects in a SQLite database.
 */
@Singleton
public class TalentCategoryDaoDbImpl extends BaseDaoDbImpl implements TalentCategoryDao, TalentCategorySchema {
    /**
     * Creates a new instance of TalentCategoryDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public TalentCategoryDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
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
