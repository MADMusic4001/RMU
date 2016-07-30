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

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.creature.CreatureVarietyDao;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTypeSchema;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link CreatureVariety} objects in a SQLite database.
 */
@Singleton
public class CreatureVarietyDaoDbImpl extends BaseDaoDbImpl<CreatureVariety> implements CreatureVarietyDao, CreatureTypeSchema {
	/**
	 * Creates a new instance of CreatureVarietyDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 */
	@Inject
	public CreatureVarietyDaoDbImpl(SQLiteOpenHelper helper) {
		super(helper);
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
	public int deleteAll() {
		return 0;
	}

	@Override
	protected CreatureVariety cursorToEntity(Cursor cursor) {
		return null;
	}

	@Override
	protected ContentValues getContentValues(CreatureVariety instance) {
		return null;
	}
}
