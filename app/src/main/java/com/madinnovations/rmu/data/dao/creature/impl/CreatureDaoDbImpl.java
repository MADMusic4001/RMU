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
import com.madinnovations.rmu.data.dao.campaign.CampaignDao;
import com.madinnovations.rmu.data.dao.creature.CreatureDao;
import com.madinnovations.rmu.data.dao.creature.CreatureVarietyDao;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSchema;
import com.madinnovations.rmu.data.entities.creature.Creature;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Creature} objects in a SQLite database.
 */
@Singleton
public class CreatureDaoDbImpl extends BaseDaoDbImpl<Creature> implements CreatureDao, CreatureSchema {
	private CampaignDao campaignDao;
	private CreatureVarietyDao creatureVarietyDao;

	/**
	 * Creates a new instance of CreatureDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 * @param campaignDao  a {@link CampaignDao} instance
	 * @param creatureVarietyDao  a {@link CreatureVarietyDao} instance
	 */
	@Inject
	public CreatureDaoDbImpl(SQLiteOpenHelper helper, CampaignDao campaignDao, CreatureVarietyDao creatureVarietyDao) {
		super(helper);
		this.campaignDao = campaignDao;
		this.creatureVarietyDao = creatureVarietyDao;
	}

	@Override
	public Creature getById(int id) {
		return super.getById(id);
	}

	@Override
	public boolean save(Creature instance) {
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
	protected int getId(Creature instance) {
		return instance.getId();
	}

	@Override
	protected void setId(Creature instance, int id) {
		instance.setId(id);
	}

	@Override
	protected Creature cursorToEntity(@NonNull Cursor cursor) {
		Creature instance = new Creature();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setCampaign(campaignDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CAMPAIGN_ID))));
		instance.setCreatureVariety(creatureVarietyDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CREATURE_VARIETY_ID))));
		instance.setCurrentHits(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_HITS)));
		instance.setMaxHits(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MAX_HITS)));
		instance.setLevel(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(Creature instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(6);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(5);
		}
		values.put(COLUMN_CAMPAIGN_ID, instance.getCampaign().getId());
		values.put(COLUMN_CREATURE_VARIETY_ID, instance.getCreatureVariety().getId());
		values.put(COLUMN_CURRENT_HITS, instance.getCurrentHits());
		values.put(COLUMN_MAX_HITS, instance.getMaxHits());
		values.put(COLUMN_LEVEL, instance.getLevel());

		return values;
	}
}
