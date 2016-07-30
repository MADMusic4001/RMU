package com.madinnovations.rmu.data.dao.character.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.CultureDao;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSchema;
import com.madinnovations.rmu.data.entities.character.Culture;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Culture} objects in a SQLite database.
 */
@Singleton
public class CultureDaoDbImpl extends BaseDaoDbImpl<Culture> implements CultureDao, CultureSchema {
    /**
     * Creates a new CultureDaoDbImpl instance
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public CultureDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    public Culture getById(int id) {
        return null;
    }

    @Override
    public List<Culture> getAll() {
        return null;
    }

    @Override
    public boolean save(Culture instance) {
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

    @SuppressWarnings("unchecked")
	@Override
    protected Culture cursorToEntity(Cursor cursor) {
        return null;
    }

	@Override
	protected ContentValues getContentValues(Culture instance) {
		return null;
	}
}
