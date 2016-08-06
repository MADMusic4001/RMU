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
import com.madinnovations.rmu.data.dao.creature.CreatureTemplateDao;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTemplateSchema;
import com.madinnovations.rmu.data.entities.creature.CreatureTemplate;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 7/30/2016.
 */
public class CreatureTemplateDaoDbImpl extends BaseDaoDbImpl<CreatureTemplate> implements CreatureTemplateDao,
		CreatureTemplateSchema{
	/**
	 * Creates a new instance of CreatureTemplateDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 */
	public CreatureTemplateDaoDbImpl(SQLiteOpenHelper helper) {
		super(helper);
	}

	@Override
	public CreatureTemplate getById(int id) {
		return super.getById(id);
	}

	@Override
	public boolean save(CreatureTemplate instance) {
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
	protected int getId(CreatureTemplate instance) {
		return instance.getId();
	}

	@Override
	protected void setId(CreatureTemplate instance, int id) {
		instance.setId(id);
	}

	@Override
	protected CreatureTemplate cursorToEntity(Cursor cursor) {
		return null;
	}

	@Override
	protected ContentValues getContentValues(CreatureTemplate instance) {
		return null;
	}
}
