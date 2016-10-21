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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.CharacterDao;
import com.madinnovations.rmu.data.dao.character.CultureDao;
import com.madinnovations.rmu.data.dao.character.ProfessionDao;
import com.madinnovations.rmu.data.dao.character.RaceDao;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterItemsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillCostsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterStatsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterTalentsSchema;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.item.ItemDao;
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCost;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.object.Item;

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
public class CharacterDaoDbImpl extends BaseDaoDbImpl<Character> implements CharacterDao, CharacterSchema {
	private RaceDao raceDao;
	private SkillDao skillDao;
	private TalentDao talentDao;
	private CultureDao cultureDao;
	private ProfessionDao professionDao;
	private RealmDao realmDao;
	private ItemDao itemDao;

	/**
	 * Creates a new instance of CharacterDaoImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 * @param raceDao  a {@link RaceDao} instance
	 * @param skillDao  a {@link SkillDao} instance
	 * @param talentDao  a {@link TalentDao} instance
	 * @param cultureDao  a {@link CultureDao} instance
	 * @param professionDao  a {@link ProfessionDao} instance
	 * @param realmDao  a {@link RealmDao} instance
	 * @param itemDao  an {@link ItemDao} instance
	 */
	@Inject
	public CharacterDaoDbImpl(SQLiteOpenHelper helper, RaceDao raceDao, SkillDao skillDao, TalentDao talentDao,
							  CultureDao cultureDao, ProfessionDao professionDao, RealmDao realmDao, ItemDao itemDao) {
		super(helper);
		this.raceDao = raceDao;
		this.skillDao = skillDao;
		this.talentDao = talentDao;
		this.cultureDao = cultureDao;
		this.professionDao = professionDao;
		this.realmDao = realmDao;
		this.itemDao = itemDao;
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
		instance.setExperiencePoints(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXPERIENCE_POINTS)));
		instance.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)));
		instance.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)));
		instance.setKnownAs(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KNOWN_AS)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_HAIR_COLOR))) {
			instance.setHairColor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HAIR_COLOR)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_HAIR_STYLE))) {
			instance.setHairStyle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HAIR_STYLE)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_EYE_COLOR))) {
			instance.setEyeColor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EYE_COLOR)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_SKIN_COMPLEXION))) {
			instance.setSkinComplexion(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SKIN_COMPLEXION)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_FACIAL_FEATURES))) {
			instance.setFacialFeatures(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FACIAL_FEATURES)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_IDENTIFYING_MARKS))) {
			instance.setIdentifyingMarks(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IDENTIFYING_MARKS)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_PERSONALITY))) {
			instance.setPersonality(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PERSONALITY)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_MANNERISMS))) {
			instance.setMannerisms(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MANNERISMS)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_HOMETOWN))) {
			instance.setHometown(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HOMETOWN)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_FAMILY_INFO))) {
			instance.setFamilyInfo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FAMILY_INFO)));
		}
		instance.setRace(raceDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RACE_ID))));
		instance.setCulture(cultureDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CULTURE_ID))));
		instance.setProfession(professionDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROFESSION_ID))));
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_REALM_ID))) {
			instance.setRealm(realmDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REALM_ID))));
		}
		instance.setHeight(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT)));
		instance.setWeight(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)));
		instance.setHitPointLoss(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_HP_LOSS)));
		instance.setCurrentDevelopmentPoints(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_DEVELOPMENT_POINTS)));
		instance.setEnduranceLoss(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_ENDURANCE_LOSS)));
		instance.setPowerPointLoss(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_PP_LOSS)));
		instance.setSkillRanks(getSkillRanks(instance.getId()));
		instance.setSkillCosts(getSkillCosts(instance.getId()));
		instance.setTalentTiers(getTalentTiers(instance.getId()));
		instance.setItems(getItems(instance.getId()));
		setStatValues(instance);

		return instance;
	}

	@Override
	protected ContentValues getContentValues(Character instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(26);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(25);
		}
		values.put(COLUMN_EXPERIENCE_POINTS, instance.getExperiencePoints());
		values.put(COLUMN_FIRST_NAME, instance.getFirstName());
		values.put(COLUMN_LAST_NAME, instance.getLastName());
		values.put(COLUMN_KNOWN_AS, instance.getKnownAs());
		values.put(COLUMN_DESCRIPTION, instance.getDescription());
		if(instance.getHairColor() == null) {
			values.putNull(COLUMN_HAIR_COLOR);
		}
		else {
			values.put(COLUMN_HAIR_COLOR, instance.getHairColor());
		}
		if(instance.getHairStyle() == null) {
			values.putNull(COLUMN_HAIR_STYLE);
		}
		else {
			values.put(COLUMN_HAIR_STYLE, instance.getHairStyle());
		}
		if(instance.getEyeColor() == null) {
			values.putNull(COLUMN_EYE_COLOR);
		}
		else {
			values.put(COLUMN_EYE_COLOR, instance.getEyeColor());
		}
		if(instance.getSkinComplexion() == null) {
			values.putNull(COLUMN_SKIN_COMPLEXION);
		}
		else {
			values.put(COLUMN_SKIN_COMPLEXION, instance.getSkinComplexion());
		}
		if(instance.getFacialFeatures() == null) {
			values.putNull(COLUMN_FACIAL_FEATURES);
		}
		else {
			values.put(COLUMN_FACIAL_FEATURES, instance.getFacialFeatures());
		}
		if(instance.getIdentifyingMarks() == null) {
			values.putNull(COLUMN_IDENTIFYING_MARKS);
		}
		else {
			values.put(COLUMN_IDENTIFYING_MARKS, instance.getIdentifyingMarks());
		}
		if(instance.getPersonality() == null) {
			values.putNull(COLUMN_PERSONALITY);
		}
		else {
			values.put(COLUMN_PERSONALITY, instance.getPersonality());
		}
		if(instance.getMannerisms() == null) {
			values.putNull(COLUMN_MANNERISMS);
		}
		else {
			values.put(COLUMN_MANNERISMS, instance.getMannerisms());
		}
		if(instance.getFamilyInfo() == null) {
			values.putNull(COLUMN_FAMILY_INFO);
		}
		else {
			values.put(COLUMN_FAMILY_INFO, instance.getFamilyInfo());
		}
		if(instance.getHometown() == null) {
			values.putNull(COLUMN_HOMETOWN);
		}
		else {
			values.put(COLUMN_HOMETOWN, instance.getHometown());
		}
		values.put(COLUMN_RACE_ID, instance.getRace().getId());
		values.put(COLUMN_CULTURE_ID, instance.getCulture().getId());
		values.put(COLUMN_PROFESSION_ID, instance.getProfession().getId());
		if(instance.getRealm() == null) {
			values.putNull(COLUMN_REALM_ID);
		}
		else {
			values.put(COLUMN_REALM_ID, instance.getRealm().getId());
		}
		values.put(COLUMN_HEIGHT, instance.getHeight());
		values.put(COLUMN_WEIGHT, instance.getWeight());
		values.put(COLUMN_CURRENT_HP_LOSS, instance.getHitPointLoss());
		values.put(COLUMN_CURRENT_DEVELOPMENT_POINTS, instance.getCurrentDevelopmentPoints());
		values.put(COLUMN_CURRENT_ENDURANCE_LOSS, instance.getEnduranceLoss());
		values.put(COLUMN_CURRENT_PP_LOSS, instance.getPowerPointLoss());

		return values;
	}

	@Override
	protected boolean saveRelationships(SQLiteDatabase db, Character instance) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		String selection = CharacterSkillRanksSchema.COLUMN_CHARACTER_ID + " = ?";

		db.delete(CharacterSkillRanksSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<Skill, Short> entry : instance.getSkillRanks().entrySet()) {
			if(entry.getValue() != null) {
				result &= (db.insertWithOnConflict(CharacterSkillRanksSchema.TABLE_NAME, null,
												   getSkillRanksContentValues(instance.getId(), entry.getKey().getId(),
																					   entry.getValue()),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

		selection = CharacterSkillCostsSchema.COLUMN_CHARACTER_ID + " = ?";
		db.delete(CharacterSkillCostsSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<Skill, SkillCost> entry : instance.getSkillCosts().entrySet()) {
			if(entry.getValue() != null && entry.getValue().getFirstCost() != null &&
					entry.getValue().getAdditionalCost() != null) {
				result &= (db.insertWithOnConflict(CharacterSkillCostsSchema.TABLE_NAME, null,
												   getSkillCostsContentValues(instance.getId(),
																			  entry.getKey().getId(),
																			  entry.getValue()),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

		selection = CharacterTalentsSchema.COLUMN_CHARACTER_ID + " = ?";
		db.delete(CharacterTalentsSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<Talent, Short> entry : instance.getTalentTiers().entrySet()) {
			if(entry.getValue() != null) {
				result &= (db.insertWithOnConflict(CharacterTalentsSchema.TABLE_NAME, null,
												   getTalentTiersContentValues(instance.getId(),
																			   entry.getKey().getId(),
																			   entry.getValue()),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

		selection = CharacterStatsSchema.COLUMN_CHARACTER_ID + " = ?";
		db.delete(CharacterStatsSchema.TABLE_NAME, selection, selectionArgs);

		for(Map.Entry<Statistic, Short> entry : instance.getStatTemps().entrySet()) {
			Short potential = instance.getStatPotentials().get(entry.getKey());
			if(entry.getValue() != null && potential != null) {
				result &= (db.insertWithOnConflict(CharacterStatsSchema.TABLE_NAME, null,
												   getStatsContentValues(instance.getId(),
																		 entry.getKey(),
																		 entry.getValue(),
																		 potential),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

		selection = CharacterItemsSchema.COLUMN_CHARACTER_ID + " = ?";
		db.delete(CharacterItemsSchema.TABLE_NAME, selection, selectionArgs);

		for(Item item : instance.getItems()) {
			result &= (db.insertWithOnConflict(CharacterItemsSchema.TABLE_NAME, null,
											   getItemsContentValues(instance.getId(),
																	 item.getId()),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		return result;
	}

	private ContentValues getSkillRanksContentValues(int characterId, int skillId, short skillRanks) {
		ContentValues values = new ContentValues(3);

		values.put(CharacterSkillRanksSchema.COLUMN_CHARACTER_ID, characterId);
		values.put(CharacterSkillRanksSchema.COLUMN_SKILL_ID, skillId);
		values.put(CharacterSkillRanksSchema.COLUMN_RANKS, skillRanks);

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

	private ContentValues getSkillCostsContentValues(int characterId, int skillId, SkillCost skillCost) {
		ContentValues values = new ContentValues(4);

		values.put(CharacterSkillCostsSchema.COLUMN_CHARACTER_ID, characterId);
		values.put(CharacterSkillCostsSchema.COLUMN_SKILL_ID, skillId);
		values.put(CharacterSkillCostsSchema.COLUMN_FIRST_COST, skillCost.getFirstCost());
		values.put(CharacterSkillCostsSchema.COLUMN_ADDITIONAL_COST, skillCost.getAdditionalCost());

		return values;
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

	private ContentValues getTalentTiersContentValues(int characterId, int talentId, short talentTiers) {
		ContentValues values = new ContentValues(3);

		values.put(CharacterTalentsSchema.COLUMN_CHARACTER_ID, characterId);
		values.put(CharacterTalentsSchema.COLUMN_TALENT_ID, talentId);
		values.put(CharacterTalentsSchema.COLUMN_TIERS, talentTiers);

		return values;
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

	private ContentValues getStatsContentValues(int characterId, Statistic stat, short currentValue, short potentialValue) {
		ContentValues values = new ContentValues(4);

		values.put(CharacterStatsSchema.COLUMN_CHARACTER_ID, characterId);
		values.put(CharacterStatsSchema.COLUMN_STAT_NAME, stat.name());
		values.put(CharacterStatsSchema.COLUMN_CURRENT_VALUE, currentValue);
		values.put(CharacterStatsSchema.COLUMN_POTENTIAL_VALUE, potentialValue);

		return values;
	}

	private void setStatValues(Character character) {
		final String selectionArgs[] = { String.valueOf(character.getId()) };
		final String selection = CharacterStatsSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterStatsSchema.TABLE_NAME, CharacterStatsSchema.COLUMNS, selection,
				selectionArgs, CharacterStatsSchema.COLUMN_STAT_NAME);
		Map<Statistic, Short> tempsMap = new HashMap<>(cursor.getCount());
		Map<Statistic, Short> potentialsMap = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Statistic statistic = Statistic.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(
					CharacterStatsSchema.COLUMN_STAT_NAME)));
			tempsMap.put(statistic, cursor.getShort(cursor.getColumnIndexOrThrow(
					CharacterStatsSchema.COLUMN_CURRENT_VALUE)));
			potentialsMap.put(statistic, cursor.getShort(cursor.getColumnIndexOrThrow(
					CharacterStatsSchema.COLUMN_POTENTIAL_VALUE)));
			cursor.moveToNext();
		}
		cursor.close();
		character.setStatTemps(tempsMap);
		character.setStatPotentials(potentialsMap);
	}

	private ContentValues getItemsContentValues(int characterId, int itemId) {
		ContentValues values = new ContentValues(2);

		values.put(CharacterItemsSchema.COLUMN_CHARACTER_ID, characterId);
		values.put(CharacterItemsSchema.COLUMN_ITEM_ID, itemId);

		return values;
	}

	private List<Item> getItems(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CharacterItemsSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterItemsSchema.TABLE_NAME, CharacterItemsSchema.COLUMNS, selection,
									selectionArgs, CharacterItemsSchema.COLUMN_ITEM_ID);
		List<Item> list = new ArrayList<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(CharacterItemsSchema.COLUMN_ITEM_ID));
			Item instance = itemDao.getById(itemId);
			if(instance != null) {
				list.add(instance);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}
}
