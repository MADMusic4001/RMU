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
import com.madinnovations.rmu.data.dao.character.ProfessionDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.dao.spells.SpellListDao;
import com.madinnovations.rmu.data.dao.spells.SpellListTypeDao;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellListSchema;
import com.madinnovations.rmu.data.entities.spells.SpellList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link SpellList} objects in a SQLite database.
 */
@Singleton
public class SpellListDaoDbImpl extends BaseDaoDbImpl<SpellList> implements SpellListDao, SpellListSchema {
	private RealmDao realmDao;
	private SpellListTypeDao spellListTypeDao;
	private ProfessionDao professionDao;

	/**
	 * Creates a new instance of SpellListDaoImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 * @param realmDao  a {@link StatDao} instance
	 * @param spellListTypeDao  a {@link SpellListTypeDao} instance
	 */
	@Inject
	public SpellListDaoDbImpl(@NonNull SQLiteOpenHelper helper, @NonNull RealmDao realmDao, @NonNull SpellListTypeDao spellListTypeDao,
							  @NonNull ProfessionDao professionDao) {
		super(helper);
		this.realmDao = realmDao;
		this.spellListTypeDao = spellListTypeDao;
		this.professionDao = professionDao;
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
	protected int getId(SpellList instance) {
		return instance.getId();
	}

	@Override
	protected void setId(SpellList instance, int id) {
		instance.setId(id);
	}

	@Override
	protected SpellList cursorToEntity(@NonNull Cursor cursor) {
		SpellList instance = new SpellList();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		if(cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_NOTES))) {
			instance.setNotes(null);
		}
		else {
			instance.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES)));
		}
		instance.setRealm(realmDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REALM_ID))));
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_REALM2_ID))) {
			instance.setRealm2(realmDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REALM2_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_PROFESSION_ID))) {
			instance.setProfession(professionDao.getById(cursor.getColumnIndexOrThrow(COLUMN_PROFESSION_ID)));
		}
		instance.setSpellListType(spellListTypeDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SPELL_LIST_TYPE_ID))));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(@NonNull SpellList instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(7);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(6);
		}
		values.put(COLUMN_NAME, instance.getName());
		if(instance.getNotes() == null) {
			values.putNull(COLUMN_NOTES);
		}
		else {
			values.put(COLUMN_NOTES, instance.getNotes());
		}
		values.put(COLUMN_REALM_ID, instance.getRealm().getId());
		values.put(COLUMN_REALM2_ID, instance.getRealm2() != null ? instance.getRealm2().getId() : null);
		values.put(COLUMN_PROFESSION_ID, instance.getProfession() != null ? instance.getProfession().getId() : null);
		values.put(COLUMN_SPELL_LIST_TYPE_ID, instance.getSpellListType().getId());

		return values;
	}
}
