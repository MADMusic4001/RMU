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
package com.madinnovations.rmu.data.dao.character.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.CharacterDao;
import com.madinnovations.rmu.data.dao.character.RaceDao;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterStatsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterTalentsSchema;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.common.Talent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Character} objects in a SQLite database.
 */
@Singleton
public class CharacterDaoDbImpl extends BaseDaoDbImpl implements CharacterDao, CharacterSchema {
	private RaceDao raceDao;
	private SkillDao skillDao;
	private TalentDao talentDao;
	private StatDao statDao;
	private ContentValues initialValues;

	/**
	 * Creates a new instance of CharacterDaoImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 */
	@Inject
	public CharacterDaoDbImpl(SQLiteOpenHelper helper, RaceDao raceDao, SkillDao skillDao, TalentDao talentDao, StatDao statDao) {
		super(helper);
		this.raceDao = raceDao;
		this.skillDao = skillDao;
		this.talentDao = talentDao;
		this.statDao = statDao;
	}

	@Override
	public Character getById(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = COLUMN_ID + " = ?";
		Character instance = new Character();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = super.query(TABLE_NAME, COLUMNS, selection,
								 selectionArgs, COLUMN_ID);
			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					instance = cursorToEntity(cursor);
					cursor.moveToNext();
				}
				cursor.close();
			}
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}

		return instance;
	}

	@Override
	public List<Character> getAll() {
		List<Character> list = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = super.query(TABLE_NAME, COLUMNS, null,
								 null, COLUMN_ID);

			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Character instance = cursorToEntity(cursor);
					list.add(instance);
					cursor.moveToNext();
				}
				cursor.close();
			}
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}

		return list;
	}

	@Override
	public boolean save(Character instance) {
		// set values
		setContentValue(instance);
		try {
			return super.insert(TABLE_NAME, getContentValue()) > 0;
		} catch (SQLiteConstraintException ex){
			Log.w("Database", ex.getMessage());
			return false;
		}
	}

	@Override
	public boolean deleteById(int instanceId) {
		return false;
	}

	@Override
	public int deleteAll() {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Character cursorToEntity(Cursor cursor) {
		Character instance = new Character();

		if (cursor != null) {
			instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
			instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
			instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
			instance.setRace(raceDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RACE_ID))));
			instance.setHeight(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT)));
			instance.setWeight(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)));
			instance.setStride(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_STRIDE)));
			instance.setCurrentHits(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_HITS)));
			instance.setMaxHits(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_MAX_HITS)));
			instance.setCurrentDevelopmentPoints(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_DEVELOPMENT_POINTS)));
			instance.setSkillRanks(getSkillRanks(instance.getId()));
			instance.setTalentTiers(getTalentTiers(instance.getId()));
			setStatValues(instance);
		}
		return instance;
	}

	private Map<Skill, Short> getSkillRanks(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CharacterSkillsSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterSkillsSchema.TABLE_NAME, CharacterSkillsSchema.COLUMNS, selection,
				selectionArgs, CharacterSkillsSchema.COLUMN_SKILL_ID);
		Map<Skill, Short> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSkillsSchema.COLUMN_SKILL_ID));
			Skill instance = skillDao.getById(mappedId);
			if(instance != null) {
				map.put(instance, cursor.getShort(cursor.getColumnIndexOrThrow(CharacterSkillsSchema.COLUMN_RANKS)));
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private Map<Talent, Short> getTalentTiers(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CharacterTalentsSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterTalentsSchema.TABLE_NAME, CharacterTalentsSchema.COLUMNS, selection,
				selectionArgs, CharacterTalentsSchema.COLUMN_TALENT_ID);
		Map<Talent, Short> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CharacterTalentsSchema.COLUMN_TALENT_ID));
			Talent instance = talentDao.getById(mappedId);
			if(instance != null) {
				map.put(instance, cursor.getShort(cursor.getColumnIndexOrThrow(CharacterTalentsSchema.COLUMN_TIERS)));
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private void setStatValues(Character character) {
		final String selectionArgs[] = { String.valueOf(character.getId()) };
		final String selection = CharacterStatsSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterStatsSchema.TABLE_NAME, CharacterStatsSchema.COLUMNS, selection,
				selectionArgs, CharacterStatsSchema.COLUMN_STAT_ID);
		Map<Stat, Short> tempsMap = new HashMap<>(cursor.getCount());
		Map<Stat, Short> potentialsMap = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CharacterStatsSchema.COLUMN_STAT_ID));
			Stat stat = statDao.getById(mappedId);
			if(stat != null) {
				tempsMap.put(stat, cursor.getShort(cursor.getColumnIndexOrThrow(CharacterStatsSchema.COLUMN_CURRENT_VALUE)));
				potentialsMap.put(stat, cursor.getShort(cursor.getColumnIndexOrThrow(CharacterStatsSchema.COLUMN_POTENTIAL_VALUE)));
			}
			cursor.moveToNext();
		}
		cursor.close();
		character.setStatTemps(tempsMap);
		character.setStatPotentials(potentialsMap);
	}

	private void setContentValue(Character character) {
		initialValues = new ContentValues();
		initialValues.put(COLUMN_ID, character.getId());
		initialValues.put(COLUMN_NAME, character.getName());
		initialValues.put(COLUMN_DESCRIPTION, character.getDescription());
		initialValues.put(COLUMN_RACE_ID, character.getRace().getId());
		initialValues.put(COLUMN_HEIGHT, character.getHeight());
		initialValues.put(COLUMN_WEIGHT, character.getWeight());
		initialValues.put(COLUMN_STRIDE, character.getStride());
		initialValues.put(COLUMN_CURRENT_HITS, character.getCurrentHits());
		initialValues.put(COLUMN_MAX_HITS, character.getMaxHits());
		initialValues.put(COLUMN_CURRENT_DEVELOPMENT_POINTS, character.getCurrentDevelopmentPoints());
	}

	private ContentValues getContentValue() {
		return initialValues;
	}
}
