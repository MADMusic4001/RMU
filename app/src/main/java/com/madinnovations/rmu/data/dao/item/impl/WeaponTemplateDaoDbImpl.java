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
import android.util.Log;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.AttackDao;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.dao.item.ItemTemplateDao;
import com.madinnovations.rmu.data.dao.item.WeaponTemplateDao;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponTemplateSchema;
import com.madinnovations.rmu.data.entities.item.ItemTemplate;
import com.madinnovations.rmu.data.entities.item.WeaponTemplate;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link WeaponTemplate} objects in a SQLite database.
 */
@Singleton
public class WeaponTemplateDaoDbImpl extends BaseDaoDbImpl<WeaponTemplate> implements WeaponTemplateDao, WeaponTemplateSchema {
	private static final String TAG = "WeaponTemplateDaoDbImpl";
	private ItemTemplateDao   itemTemplateDao;
	private SpecializationDao specializationDao;
	private DamageTableDao    damageTableDao;
	private AttackDao         attackDao;

    /**
     * Creates a new instance of WeaponDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
	 * @param itemTemplateDao  an {@link ItemTemplateDao} instance
	 * @param specializationDao  a {@link SpecializationDao} instance
	 * @param damageTableDao  a {@link DamageTableDao} instance
	 * @param attackDao  an {@link AttackDao} instance
     */
    @Inject
    public WeaponTemplateDaoDbImpl(SQLiteOpenHelper helper, ItemTemplateDao itemTemplateDao, SpecializationDao specializationDao,
								   DamageTableDao damageTableDao, AttackDao attackDao) {
        super(helper);
		this.itemTemplateDao = itemTemplateDao;
		this.specializationDao = specializationDao;
		this.damageTableDao = damageTableDao;
		this.attackDao = attackDao;
    }

    @Override
    public WeaponTemplate getById(int id) {
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
		instance.setFumble(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_FUMBLE)));
		instance.setLength(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_LENGTH)));
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_SIZE_ADJUSTMENT))) {
			instance.setSizeAdjustment(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_SIZE_ADJUSTMENT)));
		}
		instance.setAttack(attackDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ATTACK_ID))));
		if(instance.getAttack().getId() != 1) {
			Log.d(TAG, "cursorToEntity: attack = " + instance.getAttack().print());
		}

        return instance;
    }

    @Override
    protected ContentValues getContentValues(WeaponTemplate instance) {
		ContentValues contentValues = new ContentValues(8);

		contentValues.put(COLUMN_ID, instance.getId());
		contentValues.put(COLUMN_SPECIALIZATION_ID, instance.getCombatSpecialization().getId());
		contentValues.put(COLUMN_DAMAGE_TABLE_ID, instance.getDamageTable().getId());
		contentValues.put(COLUMN_BRACEABLE, instance.isBraceable());
		contentValues.put(COLUMN_FUMBLE, instance.getFumble());
		contentValues.put(COLUMN_LENGTH, instance.getLength());
		if(instance.getSizeAdjustment() != null) {
			contentValues.put(COLUMN_SIZE_ADJUSTMENT, instance.getSizeAdjustment());
		}
		else {
			contentValues.putNull(COLUMN_SIZE_ADJUSTMENT);
		}
		contentValues.put(COLUMN_ATTACK_ID, instance.getAttack().getId());
		Log.d(TAG, "getContentValues: attack = " + instance.getAttack().print());

        return contentValues;
    }
}
