package com.madinnovations.rmu.data.dao.combat.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageTableSchema;
import com.madinnovations.rmu.data.entities.combat.DamageTable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link DamageTable} objects in a SQLite database.
 */
@Singleton
public class DamageTableDaoDbImpl extends BaseDaoDbImpl<DamageTable> implements DamageTableDao, DamageTableSchema {
    /**
     * Creates a new instance of DamageTableDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public DamageTableDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    public DamageTable getById(int id) {
        return null;
    }

    @Override
    public List<DamageTable> getAll() {
        return null;
    }

    @Override
    public boolean save(DamageTable instance) {
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

    @Override
    protected DamageTable cursorToEntity(Cursor cursor) {
        return null;
    }

	@Override
	protected ContentValues getContentValues(DamageTable instance) {
		return null;
	}
}
