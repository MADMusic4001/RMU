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
package com.madinnovations.rmu.data.dao.spells.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.spells.SpellDao;
import com.madinnovations.rmu.data.dao.spells.SpellListDao;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellSchema;
import com.madinnovations.rmu.data.entities.spells.Spell;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Spell} objects in a SQLite database.
 */
@Singleton
public class SpellDaoDbImpl extends BaseDaoDbImpl<Spell> implements SpellDao, SpellSchema {
	private SpellListDao spellListDao;

	/**
	 * Creates a new instance of SpellDaoImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 * @param spellListDao  a {@link SpellListDao} instance
	 */
	@Inject
	public SpellDaoDbImpl(@NonNull SQLiteOpenHelper helper, @NonNull SpellListDao spellListDao) {
		super(helper);
		this.spellListDao = spellListDao;
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
	protected int getId(Spell instance) {
		return instance.getId();
	}

	@Override
	protected void setId(Spell instance, int id) {
		instance.setId(id);
	}

	@Override
	protected Spell cursorToEntity(@NonNull Cursor cursor) {
		Spell instance = new Spell();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		instance.setSpellList(spellListDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SPELL_LIST_ID))));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(Spell instance) {
		ContentValues initialValues = new ContentValues(4);

		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
		initialValues.put(COLUMN_SPELL_LIST_ID, instance.getSpellList().getId());

		return initialValues;
	}
}
