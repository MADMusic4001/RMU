/**
 * Copyright (C) 2016 MadInnovations
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.dao.item.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.item.ItemDao;
import com.madinnovations.rmu.data.dao.item.WeaponDao;
import com.madinnovations.rmu.data.dao.item.WeaponTemplateDao;
import com.madinnovations.rmu.data.dao.item.schemas.ItemSchema;
import com.madinnovations.rmu.data.dao.item.schemas.ItemTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponSchema;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.object.Slot;
import com.madinnovations.rmu.data.entities.object.Weapon;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Weapon} objects in a SQLite database.
 */
@Singleton
public class WeaponDaoDbImpl extends BaseDaoDbImpl<Weapon> implements WeaponDao, WeaponSchema {
	ItemDao           itemDao;
	WeaponTemplateDao weaponTemplateDao;

    /**
     * Creates a new instance of WeaponDaoDbImpl
     *
     * @param helper  an {@link SQLiteOpenHelper} instance
	 * @param itemDao  an {@link ItemDao} instance
	 * @param weaponTemplateDao  a {@link WeaponTemplateDao} instance
     */
    @Inject
    public WeaponDaoDbImpl(SQLiteOpenHelper helper, ItemDao itemDao, WeaponTemplateDao weaponTemplateDao) {
        super(helper);
		this.itemDao = itemDao;
		this.weaponTemplateDao = weaponTemplateDao;
    }

    @Override
    public Weapon getById(int id) {
        return super.getById(id);
    }

    @Override
    public boolean save(Weapon instance) {
		boolean isNew = (instance.getId() == -1);
		if(itemDao.save(instance)) {
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
    protected int getId(Weapon instance) {
        return instance.getId();
    }

    @Override
    protected void setId(Weapon instance, int id) {
        instance.setId(id);
    }

	@Override
	public Collection<Weapon> getAllForCampaign(Campaign campaign) {
		final String selectionArgs[] = { String.valueOf(campaign.getId()) };
		String selection = "EXISTS( SELECT NULL FROM " + ItemSchema.TABLE_NAME + " a WHERE a." + ItemSchema.COLUMN_ID + " = " +
				WeaponSchema.COLUMN_ID + " AND a." + ItemSchema.COLUMN_CAMPAIGN_ID + " = ?)";
		Collection<Weapon> collection = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection, selectionArgs, COLUMN_BONUS);

			if (cursor != null) {
				cursor.moveToFirst();
				while(!cursor.isAfterLast()) {
					Weapon instance = cursorToEntity(cursor);
					collection.add(instance);
					cursor.moveToNext();
				}
				cursor.close();
			}
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}

		return collection;
	}

	@Override
	public Collection<Weapon> getAllForSlot(@NonNull Slot slot) {
		final String selectionArgs[] = { slot.name(), slot.name(), Slot.ANY.name() };
		final String selection = " EXISTS(SELECT NULL FROM " + ItemTemplateSchema.TABLE_NAME + " a, " + ItemSchema.TABLE_NAME
				+ " b WHERE a." + ItemTemplateSchema.COLUMN_ID + " = b." + ItemSchema.COLUMN_ITEM_TEMPLATE_ID + " AND b."
				+ ItemSchema.COLUMN_ID + " = " + COLUMN_ID + " AND a." + ItemTemplateSchema.COLUMN_PRIMARY_SLOT + " = ? OR a." +
				ItemTemplateSchema.COLUMN_SECONDARY_SLOT + " = ? OR a." + ItemTemplateSchema.COLUMN_PRIMARY_SLOT + " = ?)";
		Collection<Weapon> weapons = null;

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection,
								  selectionArgs, getSortString());
			if (cursor != null) {
				weapons = new ArrayList<>(cursor.getCount());
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Weapon instance = cursorToEntity(cursor);
					weapons.add(instance);
					cursor.moveToNext();
				}
				cursor.close();
			}
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}

		return weapons;
	}

	@Override
    protected Weapon cursorToEntity(@NonNull Cursor cursor) {
		Item item = itemDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		item.setItemTemplate(weaponTemplateDao.getById(item.getItemTemplate().getId()));
		Weapon instance = new Weapon(item);
        instance.setBonus(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BONUS)));
		instance.setTwoHanded(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TWO_HANDED)) != 0);

        return instance;
    }

    @Override
    protected ContentValues getContentValues(Weapon instance) {
		ContentValues contentValues = new ContentValues(3);

		contentValues.put(COLUMN_ID, instance.getId());
		contentValues.put(COLUMN_BONUS, instance.getBonus());
		contentValues.put(COLUMN_TWO_HANDED, instance.isTwoHanded());

        return contentValues;
    }
}
