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
import com.madinnovations.rmu.data.dao.item.ArmorTemplateDao;
import com.madinnovations.rmu.data.dao.item.ItemTemplateDao;
import com.madinnovations.rmu.data.dao.item.schemas.ArmorTemplateSchema;
import com.madinnovations.rmu.data.entities.item.ArmorTemplate;
import com.madinnovations.rmu.data.entities.item.ItemTemplate;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link ArmorTemplate} objects in a SQLite database.
 */
@SuppressWarnings("unused")
@Singleton
public class ArmorTemplateDaoDbImpl extends BaseDaoDbImpl<ArmorTemplate> implements ArmorTemplateDao, ArmorTemplateSchema {
	private static final String TAG = "ArmorTemplateDaoDbImpl";
	private ItemTemplateDao   itemTemplateDao;

    /**
     * Creates a new instance of WeaponDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
	 * @param itemTemplateDao  an {@link ItemTemplateDao} instance
     */
    @Inject
    public ArmorTemplateDaoDbImpl(SQLiteOpenHelper helper, ItemTemplateDao itemTemplateDao) {
        super(helper);
		this.itemTemplateDao = itemTemplateDao;
    }

    @Override
    public ArmorTemplate getById(int id) {
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
				result = itemTemplateDao.deleteById(id);
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
    protected int getId(ArmorTemplate instance) {
        return instance.getId();
    }

    @Override
    protected void setId(ArmorTemplate instance, int id) {
        instance.setId(id);
    }

    @Override
    protected ArmorTemplate cursorToEntity(@NonNull Cursor cursor) {
		ItemTemplate itemTemplate = itemTemplateDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		ArmorTemplate instance = new ArmorTemplate(itemTemplate);
		instance.setSmallCost(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_SMALL_COST)));
		instance.setMediumCost(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_MEDIUM_COST)));
		instance.setLargeCost(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_LARGE_COST)));
		instance.setBigCost(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_BIG_COST)));
		instance.setWeightPercent(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT_PERCENT)));
		instance.setArmorType(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_ARMOR_TYPE)));

        return instance;
    }

    @Override
    protected ContentValues getContentValues(ArmorTemplate instance) {
		ContentValues contentValues = new ContentValues(7);

		contentValues.put(COLUMN_ID, instance.getId());
		contentValues.put(COLUMN_SMALL_COST, instance.getSmallCost());
		contentValues.put(COLUMN_MEDIUM_COST, instance.getMediumCost());
		contentValues.put(COLUMN_BIG_COST, instance.getBigCost());
		contentValues.put(COLUMN_LARGE_COST, instance.getLargeCost());
		contentValues.put(COLUMN_WEIGHT_PERCENT, instance.getWeightPercent());
		contentValues.put(COLUMN_ARMOR_TYPE, instance.getArmorType());

        return contentValues;
    }
}
