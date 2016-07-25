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

import com.madinnovations.rmu.data.dao.CreatureVarietyDao;
import com.madinnovations.rmu.data.dao.DbContentProvider;
import com.madinnovations.rmu.data.dao.schemas.CreatureTypeSchema;
import com.madinnovations.rmu.data.entities.CreatureVariety;

import java.util.List;

/**
 * Methods for managing {@link CreatureVariety} objects in a SQLite database.
 */
public class CreatureVarietyDaoDbImpl extends DbContentProvider implements CreatureVarietyDao, CreatureTypeSchema {
	/**
	 * Creates a new instance of CreatureVarietyDaoDbImpl
	 *
	 * @param db  an SQLiteDatabase instance
	 */
	public CreatureVarietyDaoDbImpl(SQLiteDatabase db) {
		super(db);
	}

	@Override
	public CreatureVariety getById(int id) {
		return null;
	}

	@Override
	public List<CreatureVariety> getAll() {
		return null;
	}

	@Override
	public boolean save(CreatureVariety instance) {
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
