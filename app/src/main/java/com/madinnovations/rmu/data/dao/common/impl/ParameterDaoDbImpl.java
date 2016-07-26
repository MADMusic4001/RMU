package com.madinnovations.rmu.data.dao.common.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.ParameterDao;
import com.madinnovations.rmu.data.dao.common.schemas.ParameterSchema;
import com.madinnovations.rmu.data.entities.common.Parameter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Parameter} objects in a SQLite database.
 */
@Singleton
public class ParameterDaoDbImpl extends BaseDaoDbImpl implements ParameterDao, ParameterSchema {
    /**
     * Creates a new instance of ParameterDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public ParameterDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

    @Override
    public Parameter getById(int id) {
        return null;
    }

    @Override
    public List<Parameter> getAll() {
        return null;
    }

    @Override
    public boolean save(Parameter instance) {
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
    protected Parameter cursorToEntity(Cursor cursor) {
        Parameter instance = null;
        int columnIndex;

        if (cursor != null) {
            instance = new Parameter();
            instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            instance.setValue(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_VALUE)));
            instance.setPerTier(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PER_TIER)) == 1 ? true : false);
        }
        return instance;
    }
}
