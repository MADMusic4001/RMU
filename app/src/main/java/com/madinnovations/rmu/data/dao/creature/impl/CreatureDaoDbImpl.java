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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.creature.CreatureDao;
import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSchema;
import com.madinnovations.rmu.data.entities.creature.Creature;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Creature} objects in a SQLite database.
 */
@Singleton
public class CreatureDaoDbImpl extends BaseDaoDbImpl implements CreatureDao, CreatureSchema {
	/**
	 * Creates a new instance of CreatureDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 */
	@Inject
	public CreatureDaoDbImpl(SQLiteOpenHelper helper) {
		super(helper);
	}

	@Override
	public Creature getById(int id) {
		return null;
	}

	@Override
	public List<Creature> getAll() {
		return null;
	}

	@Override
	public boolean save(Creature instance) {
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
	protected <T> T cursorToEntity(Cursor cursor) {
		return null;
	}
}
