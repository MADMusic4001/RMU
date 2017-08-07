/*
 *
 *   Copyright (C) 2017 MadInnovations
 *   <p/>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   <p/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   <p/>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  
 *
 */
package com.madinnovations.rmu.data.dao.item.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.item.HerbTemplateDao;
import com.madinnovations.rmu.data.dao.item.NaturalsTemplateDao;
import com.madinnovations.rmu.data.dao.item.schemas.HerbTemplateSchema;
import com.madinnovations.rmu.data.entities.item.HerbTemplate;
import com.madinnovations.rmu.data.entities.item.NaturalsTemplate;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link HerbTemplate} objects in a SQLite database.
 */
@SuppressWarnings("unused")
@Singleton
public class HerbTemplateDaoDbImpl extends BaseDaoDbImpl<HerbTemplate> implements HerbTemplateDao, HerbTemplateSchema {
	private static final String TAG = "HerbTemplateDaoDbImpl";
	private NaturalsTemplateDao naturalsTemplateDao;

    /**
     * Creates a new instance of WeaponDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
	 * @param naturalsTemplateDao  an {@link NaturalsTemplateDao} instance
     */
    @Inject
    public HerbTemplateDaoDbImpl(SQLiteOpenHelper helper, NaturalsTemplateDao naturalsTemplateDao) {
        super(helper);
		this.naturalsTemplateDao = naturalsTemplateDao;
    }

    @Override
    public HerbTemplate getById(int id) {
        return super.getById(id);
    }

	@Override
    public boolean deleteById(int id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		boolean result;
		try {
			result = super.deleteById(id);
			if (result) {
				result = naturalsTemplateDao.deleteById(id);
			}
			if(newTransaction && result) {
				db.setTransactionSuccessful();
			}
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}
		return result;
    }

    @Override
    public int deleteAll() {
        return super.deleteAll();
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String[] getColumns() {
        return COLUMNS;
    }

    @Override
    protected String getIdColumnName() {
        return COLUMN_ID;
    }

    @Override
    protected int getId(HerbTemplate instance) {
        return instance.getId();
    }

    @Override
    protected void setId(HerbTemplate instance, int id) {
        instance.setId(id);
    }

    @Override
    protected HerbTemplate cursorToEntity(@NonNull Cursor cursor) {
		NaturalsTemplate naturalsTemplate = naturalsTemplateDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		HerbTemplate instance = new HerbTemplate(naturalsTemplate);
        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));

        return instance;
    }

    @Override
    protected ContentValues getContentValues(HerbTemplate instance) {
		ContentValues contentValues = new ContentValues(8);

		contentValues.put(COLUMN_ID, instance.getId());

        return contentValues;
    }
}
