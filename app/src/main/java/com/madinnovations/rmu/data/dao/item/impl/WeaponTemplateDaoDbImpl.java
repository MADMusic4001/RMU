/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.dao.item.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.dao.item.ItemTemplateDao;
import com.madinnovations.rmu.data.dao.item.WeaponTemplateDao;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponTemplateSchema;
import com.madinnovations.rmu.data.entities.object.ItemTemplate;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link WeaponTemplate} objects in a SQLite database.
 */
@Singleton
public class WeaponTemplateDaoDbImpl extends BaseDaoDbImpl<WeaponTemplate> implements WeaponTemplateDao, WeaponTemplateSchema {
	private ItemTemplateDao   itemTemplateDao;
	private SpecializationDao specializationDao;
	private DamageTableDao    damageTableDao;

    /**
     * Creates a new instance of WeaponDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public WeaponTemplateDaoDbImpl(SQLiteOpenHelper helper, ItemTemplateDao itemTemplateDao, SpecializationDao specializationDao,
								   DamageTableDao damageTableDao) {
        super(helper);
		this.itemTemplateDao = itemTemplateDao;
		this.specializationDao = specializationDao;
		this.damageTableDao = damageTableDao;
    }

    @Override
    public WeaponTemplate getById(int id) {
        return super.getById(id);
    }

    @Override
    public boolean save(WeaponTemplate instance) {
		boolean isNew = (instance.getId() == -1);
		if(itemTemplateDao.save(instance)) {
			int id = instance.getId();
			final String selectionArgs[] = {String.valueOf(getId(instance))};
			final String selection = getIdColumnName() + " = ?";
			ContentValues contentValues = getContentValues(instance);
			boolean result;

			SQLiteDatabase db = helper.getWritableDatabase();
			boolean newTransaction = !db.inTransaction();
			if (newTransaction) {
				db.beginTransaction();
			}
			try {
				if (isNew) {
					result = (db.insertWithOnConflict(getTableName(), null, contentValues, SQLiteDatabase.CONFLICT_NONE) == id);
				}
				else {
					int count = db.update(getTableName(), contentValues, selection, selectionArgs);
					result = (count == 1);
				}
				result &= saveRelationships(db, instance);
				if (result && newTransaction) {
					db.setTransactionSuccessful();
				}
			}
			finally {
				if (newTransaction) {
					db.endTransaction();
				}
			}
		}
		return true;
    }

    @Override
    public boolean deleteById(int id) {
        return super.deleteById(id);
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
    protected int getId(WeaponTemplate instance) {
        return instance.getId();
    }

    @Override
    protected void setId(WeaponTemplate instance, int id) {
        instance.setId(id);
    }

    @Override
    protected WeaponTemplate cursorToEntity(@NonNull Cursor cursor) {
		ItemTemplate itemTemplate = itemTemplateDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		WeaponTemplate instance = new WeaponTemplate(itemTemplate);
        instance.setCombatSpecialization(specializationDao.getById(cursor.getInt(
        		cursor.getColumnIndexOrThrow(COLUMN_SPECIALIZATION_ID))));
		instance.setDamageTable(damageTableDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DAMAGE_TABLE_ID))));
		instance.setBraceable(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BRACEABLE)) != 0);

        return instance;
    }

    @Override
    protected ContentValues getContentValues(WeaponTemplate instance) {
		ContentValues contentValues = new ContentValues(4);

		contentValues.put(COLUMN_ID, instance.getId());
		contentValues.put(COLUMN_SPECIALIZATION_ID, instance.getCombatSpecialization().getId());
		contentValues.put(COLUMN_DAMAGE_TABLE_ID, instance.getDamageTable().getId());
		contentValues.put(COLUMN_BRACEABLE, instance.isBraceable());

        return contentValues;
    }
}
