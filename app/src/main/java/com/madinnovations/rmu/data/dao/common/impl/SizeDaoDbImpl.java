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
import android.support.annotation.NonNull;

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
    protected Size cursorToEntity(@NonNull Cursor cursor) {
		Size instance = new Size();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CODE)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setExamples(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXAMPLES)));
		if(cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_MIN_HEIGHT))) {
			instance.setMinHeight(null);
		}
		else {
			instance.setMinHeight(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MIN_HEIGHT)));
		}
		if(cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_MAX_HEIGHT))) {
			instance.setMaxWeight(null);
		}
		else {
			instance.setMaxHeight(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MAX_HEIGHT)));
		}
		if(cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_MIN_WEIGHT))) {
			instance.setMinWeight(null);
		}
		else {
			instance.setMinWeight(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MIN_WEIGHT)));
		}
		if(cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_MAX_WEIGHT))) {
			instance.setMaxWeight(null);
		}
		else {
			instance.setMaxWeight(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MAX_WEIGHT)));
		}

		return instance;
    }

	@Override
	protected ContentValues getContentValues(Size instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(8);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(7);
		}
		values.put(COLUMN_CODE, instance.getCode());
		values.put(COLUMN_NAME, instance.getName());
		values.put(COLUMN_EXAMPLES, instance.getExamples());
		values.put(COLUMN_MIN_HEIGHT, instance.getMinHeight());
		values.put(COLUMN_MAX_HEIGHT, instance.getMaxHeight());
		values.put(COLUMN_MIN_WEIGHT, instance.getMinWeight());
		values.put(COLUMN_MAX_WEIGHT, instance.getMaxWeight());

		return values;
	}
}
