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
package com.madinnovations.rmu.data.dao.common.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.SizeDao;
import com.madinnovations.rmu.data.dao.common.schemas.SizeSchema;
import com.madinnovations.rmu.data.entities.common.Size;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Size} objects in a SQLite database.
 */
@Singleton
public class SizeDaoDbImpl extends BaseDaoDbImpl<Size> implements SizeDao, SizeSchema {
    /**
     * Creates a new instance of SizeDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public SizeDaoDbImpl(SQLiteOpenHelper helper) {
        super(helper);
    }

	@Override
	public Size getById(int id) {
		return super.getById(id);
	}

	@Override
	public boolean save(Size instance) {
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
	protected int getId(Size instance) {
		return instance.getId();
	}

	@Override
	protected void setId(Size instance, int id) {
		instance.setId(id);
	}

	@Override
    protected Size cursorToEntity(Cursor cursor) {
		Size instance = null;

		if (cursor != null) {
			instance = new Size();
			instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
			instance.setCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CODE)));
			instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
			instance.setExamples(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXAMPLES)));
			instance.setMinHeight(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MIN_HEIGHT)));
			instance.setMaxHeight(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MAX_HEIGHT)));
			instance.setMinWeight(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MIN_WEIGHT)));
			instance.setMaxWeight(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MAX_WEIGHT)));
		}
		return instance;
    }

	@Override
	protected ContentValues getContentValues(Size instance) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COLUMN_CODE, instance.getCode());
		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_EXAMPLES, instance.getExamples());
		initialValues.put(COLUMN_MIN_HEIGHT, instance.getMinHeight());
		initialValues.put(COLUMN_MAX_HEIGHT, instance.getMaxHeight());
		initialValues.put(COLUMN_MIN_WEIGHT, instance.getMinWeight());
		initialValues.put(COLUMN_MAX_WEIGHT, instance.getMaxWeight());
		return initialValues;
	}
}
