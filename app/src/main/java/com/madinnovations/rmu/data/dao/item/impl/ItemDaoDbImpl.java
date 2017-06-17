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
import com.madinnovations.rmu.data.dao.campaign.CampaignDao;
import com.madinnovations.rmu.data.dao.common.SizeDao;
import com.madinnovations.rmu.data.dao.item.ItemDao;
import com.madinnovations.rmu.data.dao.item.ItemTemplateDao;
import com.madinnovations.rmu.data.dao.item.schemas.ItemSchema;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponSchema;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.object.Slot;
import com.madinnovations.rmu.data.entities.object.Weapon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Item} objects in a SQLite database.
 */
@Singleton
public class ItemDaoDbImpl extends BaseDaoDbImpl<Item> implements ItemDao, ItemSchema {
	private CampaignDao     campaignDao;
	private ItemTemplateDao itemTemplateDao;
	private SizeDao         sizeDao;

    /**
     * Creates a new instance of ItemDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public ItemDaoDbImpl(SQLiteOpenHelper helper, CampaignDao campaignDao, ItemTemplateDao itemTemplateDao, SizeDao sizeDao) {
        super(helper);
		this.campaignDao = campaignDao;
		this.itemTemplateDao = itemTemplateDao;
		this.sizeDao = sizeDao;
    }

	@Override
	public Item getById(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		Item instance = null;

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = db.rawQuery(ItemSchema.QUERY_BY_ID, selectionArgs);
			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					instance = cursorToEntity(cursor);
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

		return instance;
	}

	@Override
	public boolean save(Item instance) {
		return super.save(instance);
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
	protected int getId(Item instance) {
		return instance.getId();
	}

	@Override
	protected void setId(Item instance, int id) {
		instance.setId(id);
	}

	@Override
	public boolean save(Item instance, boolean isNew) {
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		final String selection = COLUMN_ID + " = ?";
		final String weaponSelection = WeaponSchema.COLUMN_ID + " = ?";
		ContentValues contentValues = getContentValues(instance);
		boolean result;

		SQLiteDatabase db = helper.getWritableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			if(getId(instance) == -1 || isNew) {
				setId(instance, (int)db.insertWithOnConflict(getTableName(), null, contentValues, SQLiteDatabase.CONFLICT_NONE));
				result = (getId(instance) != -1);
				if(instance instanceof Weapon) {
					ContentValues weaponContentValues = getWeaponContentValues((Weapon)instance);
					db.insertWithOnConflict(WeaponSchema.TABLE_NAME, null, weaponContentValues, SQLiteDatabase.CONFLICT_NONE);
				}
			}
			else {
				int count = db.update(getTableName(), contentValues, selection, selectionArgs);
				result = (count == 1);
				if(instance instanceof Weapon) {
					ContentValues weaponContentValues = getWeaponContentValues((Weapon)instance);
					if(db.update(WeaponSchema.TABLE_NAME, weaponContentValues, weaponSelection, selectionArgs) != 1) {
						weaponContentValues = getWeaponContentValues((Weapon)instance);
						db.insertWithOnConflict(WeaponSchema.TABLE_NAME, null, weaponContentValues, SQLiteDatabase.CONFLICT_NONE);
					}
				}
			}
			if(result && newTransaction) {
				db.setTransactionSuccessful();
			}
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}
		return true;
	}

	@Override
	public List<Item> getAll() {
		List<Item> list = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = db.rawQuery(QUERY_ALL, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Item instance = cursorToEntity(cursor);
				list.add(instance);
				cursor.moveToNext();
			}
			cursor.close();
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}

		return list;
	}

	@Override
	public Collection<Item> getAllForCampaign(Campaign campaign) {
		final String selectionArgs[] = { String.valueOf(campaign.getId()) };
		Collection<Item> collection = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = db.rawQuery(QUERY_BY_CAMPAIGN, selectionArgs);

			if (cursor != null) {
				cursor.moveToFirst();
				while(!cursor.isAfterLast()) {
					Item instance = cursorToEntity(cursor);
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
	public Collection<Item> getAllForSlot(@NonNull Slot slot) {
		final String selectionArgs[] = { slot.name(), slot.name(), Slot.ANY.name() };
		Collection<Item> items = null;

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = db.rawQuery(QUERY_BY_SLOT, selectionArgs);
			if (cursor != null) {
				items = new ArrayList<>(cursor.getCount());
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Item instance = cursorToEntity(cursor);
					items.add(instance);
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

		return items;
	}

	@Override
	public Collection<Item> getAllWithoutSubclass() {
		Collection<Item> items = null;

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = db.rawQuery(QUERY_NO_SUBCLASS, null);
			if (cursor != null) {
				items = new ArrayList<>(cursor.getCount());
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Item instance = cursorToEntity(cursor);
					items.add(instance);
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

		return items;
	}

	@Override
    protected Item cursorToEntity(@NonNull Cursor cursor) {
		Item instance;

		if(!cursor.isNull(cursor.getColumnIndexOrThrow(WeaponSchema.COLUMN_BONUS))) {
			instance = new Weapon();
			((Weapon)instance).setBonus(cursor.getShort(cursor.getColumnIndexOrThrow(WeaponSchema.COLUMN_BONUS)));
			((Weapon)instance).setTwoHanded(cursor.getInt(cursor.getColumnIndexOrThrow(WeaponSchema.COLUMN_TWO_HANDED)) != 0);
		}
		else {
			instance = new Item();
		}
		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setCampaign(campaignDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CAMPAIGN_ID))));
		instance.setCampaign(new Campaign(1));
		instance.setItemTemplate(itemTemplateDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ITEM_TEMPLATE_ID))));
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_NAME))) {
			instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_HISTORY))) {
			instance.setHistory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HISTORY)));
		}
		instance.setSize(sizeDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SIZE_ID))));
		instance.setLevel(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)));

		return instance;
    }

    @Override
    protected ContentValues getContentValues(Item instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(7);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(6);
		}
		values.put(COLUMN_CAMPAIGN_ID, instance.getCampaign().getId());
		values.put(COLUMN_ITEM_TEMPLATE_ID, instance.getItemTemplate().getId());
		if(instance.getName() == null) {
			values.putNull(COLUMN_NAME);
		}
		else {
			values.put(COLUMN_NAME, instance.getName());
		}
		if(instance.getHistory() == null) {
			values.putNull(COLUMN_HISTORY);
		}
		else {
			values.put(COLUMN_HISTORY, instance.getHistory());
		}
		values.put(COLUMN_SIZE_ID, instance.getSize().getId());
		values.put(COLUMN_LEVEL, instance.getLevel());

		return values;
    }

	private ContentValues getWeaponContentValues(Weapon instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(3);
			values.put(WeaponSchema.COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(2);
		}
		values.put(WeaponSchema.COLUMN_BONUS, instance.getBonus());
		values.put(WeaponSchema.COLUMN_TWO_HANDED, instance.isTwoHanded());

		return values;
	}
}
