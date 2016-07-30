package com.madinnovations.rmu.data.dao.combat.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultSchema;
import com.madinnovations.rmu.data.entities.combat.DamageResult;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link DamageResult} objects in a SQLite database.
 */
@Singleton
public class DamageResultDaoDbImpl extends BaseDaoDbImpl<DamageResult> implements DamageResultDao, DamageResultSchema {
    /**
     * Creates a new instance of DamageResultDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public DamageResultDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    public DamageResult getById(int id) {
        return null;
    }

    @Override
    public List<DamageResult> getAll() {
        return null;
    }

    @Override
    public boolean save(DamageResult instance) {
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
    protected DamageResult cursorToEntity(Cursor cursor) {
        return null;
    }

	@Override
	protected ContentValues getContentValues(DamageResult instance) {
		return null;
	}
}
