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
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.CharacterDao;
import com.madinnovations.rmu.data.dao.character.RaceDao;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillCostsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterStatsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterTalentsSchema;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCost;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.common.Talent;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Character} objects in a SQLite database.
 */
@Singleton
public class CharacterDaoDbImpl extends BaseDaoDbImpl<Character> implements CharacterDao, CharacterSchema {
	private RaceDao raceDao;
	private SkillDao skillDao;
	private TalentDao talentDao;
	private StatDao statDao;

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
		return super.getById(id);
	}

	@Override
	public boolean save(Character instance) {
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
	protected int getId(Character instance) {
		return instance.getId();
	}

	@Override
	protected void setId(Character instance, int id) {
		instance.setId(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Character cursorToEntity(@NonNull Cursor cursor) {
		Character instance = new Character();

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
		instance.setSkillCosts(getSkillCosts(instance.getId()));
		instance.setTalentTiers(getTalentTiers(instance.getId()));
		setStatValues(instance);

		return instance;
	}

	@Override
	protected ContentValues getContentValues(Character instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(10);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(9);
		}
		values.put(COLUMN_NAME, instance.getName());
		values.put(COLUMN_DESCRIPTION, instance.getDescription());
		values.put(COLUMN_RACE_ID, instance.getRace().getId());
		values.put(COLUMN_HEIGHT, instance.getHeight());
		values.put(COLUMN_WEIGHT, instance.getWeight());
		values.put(COLUMN_STRIDE, instance.getStride());
		values.put(COLUMN_CURRENT_HITS, instance.getCurrentHits());
		values.put(COLUMN_MAX_HITS, instance.getMaxHits());
		values.put(COLUMN_CURRENT_DEVELOPMENT_POINTS, instance.getCurrentDevelopmentPoints());

		return values;
	}

	private Map<Skill, Short> getSkillRanks(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CharacterSkillRanksSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterSkillRanksSchema.TABLE_NAME, CharacterSkillRanksSchema.COLUMNS, selection,
									selectionArgs, CharacterSkillRanksSchema.COLUMN_SKILL_ID);
		Map<Skill, Short> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSkillRanksSchema.COLUMN_SKILL_ID));
			Skill instance = skillDao.getById(mappedId);
			if(instance != null) {
				map.put(instance, cursor.getShort(cursor.getColumnIndexOrThrow(CharacterSkillRanksSchema.COLUMN_RANKS)));
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private Map<Skill, SkillCost> getSkillCosts(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CharacterSkillCostsSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterSkillCostsSchema.TABLE_NAME, CharacterSkillCostsSchema.COLUMNS, selection,
									selectionArgs, CharacterSkillCostsSchema.COLUMN_SKILL_ID);
		Map<Skill, SkillCost> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSkillCostsSchema.COLUMN_SKILL_ID));
			Skill instance = skillDao.getById(mappedId);
			SkillCost skillCost = new SkillCost();
			skillCost.setFirstCost(cursor.getShort(cursor.getColumnIndexOrThrow(CharacterSkillCostsSchema.COLUMN_FIRST_COST)));
			skillCost.setAdditionalCost(cursor.getShort(cursor.getColumnIndexOrThrow(
					CharacterSkillCostsSchema.COLUMN_ADDITIONAL_COST)));
			if(instance != null) {
				map.put(instance, skillCost);
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
}
