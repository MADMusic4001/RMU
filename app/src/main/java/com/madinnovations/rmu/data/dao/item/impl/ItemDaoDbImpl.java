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
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.item.ItemDao;
import com.madinnovations.rmu.data.dao.item.schemas.ItemSchema;
import com.madinnovations.rmu.data.entities.object.Item;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Item} objects in a SQLite database.
 */
@Singleton
public class ItemDaoDbImpl extends BaseDaoDbImpl<Item> implements ItemDao, ItemSchema {
    /**
     * Creates a new instance of ItemDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public ItemDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

	@Override
	public Item getById(int id) {
		return super.getById(id);
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
    protected Item cursorToEntity(Cursor cursor) {
		Item instance = new Item();

		if (cursor != null) {
			instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
			instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
			instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
			instance.setWeight(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)));
		}
		return instance;
    }

    @Override
    protected ContentValues getContentValues(Item instance) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
		initialValues.put(COLUMN_WEIGHT, instance.getWeight());
		return initialValues;
    }
}
