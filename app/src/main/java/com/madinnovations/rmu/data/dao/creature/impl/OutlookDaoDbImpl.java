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
package com.madinnovations.rmu.data.dao.creature.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.creature.OutlookDao;
import com.madinnovations.rmu.data.dao.creature.schemas.OutlookSchema;
import com.madinnovations.rmu.data.entities.creature.Outlook;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Outlook} objects in a SQLite database.
 */
@Singleton
public class OutlookDaoDbImpl extends BaseDaoDbImpl<Outlook>
		implements OutlookDao, OutlookSchema {
	/**
	 * Creates a new instance of OutlookDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 */
	@Inject
	public OutlookDaoDbImpl(SQLiteOpenHelper helper) {
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
	protected int getId(Outlook instance) {
		return instance.getId();
	}

	@Override
	protected void setId(Outlook instance, int id) {
		instance.setId(id);
	}

	@Override
	protected Outlook cursorToEntity(@NonNull Cursor cursor) {
		Outlook instance = new Outlook();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(Outlook instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(3);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(2);
		}
		values.put(COLUMN_NAME, instance.getName());
		values.put(COLUMN_DESCRIPTION, instance.getDescription());

		return values;
	}
}
