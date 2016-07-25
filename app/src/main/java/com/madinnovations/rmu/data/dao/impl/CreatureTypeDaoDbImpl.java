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
package com.madinnovations.rmu.data.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.madinnovations.rmu.data.dao.CreatureTypeDao;
import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.schemas.CreatureTypeSchema;
import com.madinnovations.rmu.data.entities.CreatureType;

import java.util.List;

/**
 * Methods for managing {@link CreatureType} objects in a SQLite database.
 */
public class CreatureTypeDaoDbImpl extends DbContentProvider implements CreatureTypeDao, CreatureTypeSchema {
	/**
	 * Creates a new instance of CreatureTypeDaoDbImpl
	 *
	 * @param db  an SQLiteDatabase instance
	 */
	public CreatureTypeDaoDbImpl(SQLiteDatabase db) {
		super(db);
	}

	@Override
	public CreatureType getById(int id) {
		return null;
	}

	@Override
	public List<CreatureType> getAll() {
		return null;
	}

	@Override
	public boolean save(CreatureType instance) {
		return false;
	}

	@Override
	public boolean deleteById(int id) {
		return false;
	}

	@Override
	public boolean deleteAll() {
		return false;
	}

	@Override
	protected <T> T cursorToEntity(Cursor cursor) {
		return null;
	}
}
