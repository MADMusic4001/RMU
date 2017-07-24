/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.dao.character.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.campaign.CampaignDao;
import com.madinnovations.rmu.data.dao.character.CharacterDao;
import com.madinnovations.rmu.data.dao.character.CultureDao;
import com.madinnovations.rmu.data.dao.character.ProfessionDao;
import com.madinnovations.rmu.data.dao.character.RaceDao;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterCurrentLevelSkillRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterCurrentLevelSpecializationRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterCurrentLevelSpellListRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterItemsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterKnacksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterProfessionSkillsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterPurchasedCultureRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillCostsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSpecializationRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSpellListRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterStatsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterTalentParametersSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterTalentsSchema;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.item.ItemDao;
import com.madinnovations.rmu.data.dao.spells.SpellListDao;
import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.common.DevelopmentCostGroup;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.data.entities.spells.SpellList;

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
	@SuppressWarnings("unused")
	private static final String TAG = "CharacterDaoDbImpl";
	private RaceDao raceDao;
	private SkillDao skillDao;
	private TalentDao talentDao;
	private CultureDao cultureDao;
	private ProfessionDao professionDao;
	private ItemDao itemDao;
	private SpecializationDao specializationDao;
	private CampaignDao campaignDao;
	private SpellListDao spellListDao;

	/**
	 * Creates a new instance of CharacterDaoImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 * @param raceDao  a {@link RaceDao} instance
	 * @param skillDao  a {@link SkillDao} instance
	 * @param talentDao  a {@link TalentDao} instance
	 * @param cultureDao  a {@link CultureDao} instance
	 * @param professionDao  a {@link ProfessionDao} instance
	 * @param itemDao  an {@link ItemDao} instance
	 * @param specializationDao  a {@link SpecializationDao} instance
	 * @param campaignDao  a {@link CampaignDao} instance
	 * @param spellListDao  a {@link SpellListDao} instance
	 */
	@Inject
	public CharacterDaoDbImpl(SQLiteOpenHelper helper, RaceDao raceDao, SkillDao skillDao, TalentDao talentDao,
							  CultureDao cultureDao, ProfessionDao professionDao, ItemDao itemDao,
							  SpecializationDao specializationDao, CampaignDao campaignDao, SpellListDao spellListDao) {
		super(helper);
		this.raceDao = raceDao;
		this.skillDao = skillDao;
		this.talentDao = talentDao;
		this.cultureDao = cultureDao;
		this.professionDao = professionDao;
		this.itemDao = itemDao;
		this.specializationDao = specializationDao;
		this.campaignDao = campaignDao;
		this.spellListDao = spellListDao;
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

	@Override
	public List<Character> getAllForCampaign(Campaign campaign) {
		final String selectionArgs[] = { String.valueOf(campaign.getId()) };
		final String selection = CharacterSchema.COLUMN_CAMPAIGN_ID + " = ?";
		List<Character> list = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection, selectionArgs, CharacterSchema.COLUMN_FIRST_NAME);

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

	@SuppressWarnings("unchecked")
	@Override
	protected Character cursorToEntity(@NonNull Cursor cursor) {
		Character instance = new Character();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setCampaign(campaignDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CAMPAIGN_ID))));
		instance.setCurrentLevel(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_LEVEL)));
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
		instance.setRealm(Realm.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REALM))));
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_REALM2))) {
			instance.setRealm2(Realm.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REALM2))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_REALM3))) {
			instance.setRealm3(Realm.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REALM3))));
		}
		instance.setHeight(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT)));
		instance.setWeight(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)));
		instance.setHitPointLoss(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_HP_LOSS)));
		instance.setCurrentDevelopmentPoints(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_DEVELOPMENT_POINTS)));
		instance.setFatigue(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_FATIGUE)));
		instance.setPowerPointLoss(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_PP_LOSS)));
		instance.setBaseMovementRate(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BASE_MOVEMENT_RATE)));
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_MAIN_HAND_ITEM_ID))) {
			instance.setMainHandItem(itemDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MAIN_HAND_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_OFFHAND_ITEM_ID))) {
			instance.setOffhandItem(itemDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_OFFHAND_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_SHIRT_ITEM_ID))) {
			instance.setShirtItem(itemDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SHIRT_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_PANTS_ITEM_ID))) {
			instance.setPantsItem(itemDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PANTS_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_HEAD_ITEM_ID))) {
			instance.setHeadItem(itemDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HEAD_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_CHEST_ITEM_ID))) {
			instance.setChestItem(itemDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CHEST_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_ARMS_ITEM_ID))) {
			instance.setArmsItem(itemDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ARMS_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_LEGS_ITEM_ID))) {
			instance.setLegsItem(itemDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LEGS_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_FEET_ITEM_ID))) {
			instance.setFeetItem(itemDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FEET_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_BACK_ITEM_ID))) {
			instance.setBackItem(itemDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BACK_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_BACKPACK_ITEM_ID))) {
			instance.setBackpackItem(itemDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BACKPACK_ITEM_ID))));
		}
		instance.setSkillRanks(getSkillRanks(instance.getId()));
		instance.setSpecializationRanks(getSpecializationRanks(instance.getId()));
		instance.setSpellListRanks(getSpellListRanks(instance.getId()));
		instance.setSkillCosts(getSkillCosts(instance.getId()));
		instance.setTalentInstances(getTalentInstances(instance.getId()));
		instance.setItems(getItems(instance.getId()));
		instance.setCurrentLevelSkillRanks(getCurrentLevelSkillRanks(instance.getId()));
		instance.setCurrentLevelSpecializationRanks(getCurrentLevelSpecializationRanks(instance.getId()));
		instance.setCurrentLevelSpellListRanks(getCurrentLevelSpellListRanks(instance.getId()));
		instance.setPurchasedCultureRanks(getPurchasedCultureRanks(instance.getId()));
		instance.setStatIncreases(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_STAT_INCREASES)));
		instance.setProfessionSkills(getProfessionSkills(instance.getId()));
		instance.setKnacks(getKnacks(instance.getId()));
		setStatValues(instance);

		return instance;
	}

	@Override
	protected ContentValues getContentValues(Character instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(43);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(42);
		}
		values.put(COLUMN_CAMPAIGN_ID, instance.getCampaign().getId());
		values.put(COLUMN_CURRENT_LEVEL, instance.getCurrentLevel());
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
		values.put(COLUMN_REALM, instance.getRealm().name());
		if(instance.getRealm2() == null) {
			values.putNull(COLUMN_REALM2);
		}
		else {
			values.put(COLUMN_REALM2, instance.getRealm2().name());
		}
		if(instance.getRealm3() == null) {
			values.putNull(COLUMN_REALM3);
		}
		else {
			values.put(COLUMN_REALM3, instance.getRealm3().name());
		}
		values.put(COLUMN_HEIGHT, instance.getHeight());
		values.put(COLUMN_WEIGHT, instance.getWeight());
		values.put(COLUMN_CURRENT_HP_LOSS, instance.getHitPointLoss());
		values.put(COLUMN_CURRENT_DEVELOPMENT_POINTS, instance.getCurrentDevelopmentPoints());
		values.put(COLUMN_CURRENT_FATIGUE, instance.getFatigue());
		values.put(COLUMN_CURRENT_PP_LOSS, instance.getPowerPointLoss());
		values.put(COLUMN_BASE_MOVEMENT_RATE, instance.getBaseMovementRate());
		values.put(COLUMN_STAT_INCREASES, instance.getStatIncreases());
		if(instance.getMainHandItem() == null) {
			values.putNull(COLUMN_MAIN_HAND_ITEM_ID);
		}
		else {
			values.put(COLUMN_MAIN_HAND_ITEM_ID, instance.getMainHandItem().getId());
		}
		if(instance.getOffhandItem() == null) {
			values.putNull(COLUMN_OFFHAND_ITEM_ID);
		}
		else {
			values.put(COLUMN_OFFHAND_ITEM_ID, instance.getOffhandItem().getId());
		}
		if(instance.getShirtItem() == null) {
			values.putNull(COLUMN_SHIRT_ITEM_ID);
		}
		else {
			values.put(COLUMN_SHIRT_ITEM_ID, instance.getShirtItem().getId());
		}
		if(instance.getPantsItem() == null) {
			values.putNull(COLUMN_PANTS_ITEM_ID);
		}
		else {
			values.put(COLUMN_PANTS_ITEM_ID, instance.getPantsItem().getId());
		}
		if(instance.getHeadItem() == null) {
			values.putNull(COLUMN_HEAD_ITEM_ID);
		}
		else {
			values.put(COLUMN_HEAD_ITEM_ID, instance.getHeadItem().getId());
		}
		if(instance.getChestItem() == null) {
			values.putNull(COLUMN_CHEST_ITEM_ID);
		}
		else {
			values.put(COLUMN_CHEST_ITEM_ID, instance.getChestItem().getId());
		}
		if(instance.getArmsItem() == null) {
			values.putNull(COLUMN_ARMS_ITEM_ID);
		}
		else {
			values.put(COLUMN_ARMS_ITEM_ID, instance.getArmsItem().getId());
		}
		if(instance.getLegsItem() == null) {
			values.putNull(COLUMN_LEGS_ITEM_ID);
		}
		else {
			values.put(COLUMN_LEGS_ITEM_ID, instance.getLegsItem().getId());
		}
		if(instance.getFeetItem() == null) {
			values.putNull(COLUMN_FEET_ITEM_ID);
		}
		else {
			values.put(COLUMN_FEET_ITEM_ID, instance.getFeetItem().getId());
		}
		if(instance.getBackItem() == null) {
			values.putNull(COLUMN_BACK_ITEM_ID);
		}
		else {
			values.put(COLUMN_BACK_ITEM_ID, instance.getBackItem().getId());
		}
		if(instance.getBackpackItem() == null) {
			values.putNull(COLUMN_BACKPACK_ITEM_ID);
		}
		else {
			values.put(COLUMN_BACKPACK_ITEM_ID, instance.getBackpackItem().getId());
		}

		return values;
	}

	@Override
	protected boolean deleteRelationships(SQLiteDatabase db, int id) {
		boolean result;
		final String selectionArgs[] = { String.valueOf(id) };

		String selection = CharacterSkillRanksSchema.COLUMN_CHARACTER_ID + " = ?";
		result = (db.delete(CharacterSkillRanksSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = CharacterSpecializationRanksSchema.COLUMN_CHARACTER_ID + " = ?";
		result &= (db.delete(CharacterSpecializationRanksSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = CharacterSpellListRanksSchema.COLUMN_CHARACTER_ID + " = ?";
		result &= (db.delete(CharacterSpellListRanksSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = CharacterSkillCostsSchema.COLUMN_CHARACTER_ID + " = ?";
		result &= (db.delete(CharacterSkillCostsSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = CharacterTalentsSchema.COLUMN_CHARACTER_ID + " = ?";
		result &= (db.delete(CharacterTalentsSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = CharacterTalentParametersSchema.COLUMN_CHARACTER_ID + " = ?";
		result &= (db.delete(CharacterTalentParametersSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = CharacterStatsSchema.COLUMN_CHARACTER_ID + " = ?";
		result &= (db.delete(CharacterStatsSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = CharacterItemsSchema.COLUMN_CHARACTER_ID + " = ?";
		result &= (db.delete(CharacterItemsSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = CharacterCurrentLevelSkillRanksSchema.COLUMN_CHARACTER_ID + " = ?";
		result &= (db.delete(CharacterCurrentLevelSkillRanksSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = CharacterCurrentLevelSpecializationRanksSchema.COLUMN_CHARACTER_ID + " = ?";
		result &= (db.delete(CharacterCurrentLevelSpecializationRanksSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = CharacterCurrentLevelSpellListRanksSchema.COLUMN_CHARACTER_ID + " = ?";
		result &= (db.delete(CharacterCurrentLevelSpellListRanksSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = CharacterPurchasedCultureRanksSchema.COLUMN_CHARACTER_ID + " = ?";
		result &= (db.delete(CharacterPurchasedCultureRanksSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		return result;
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

		selection = CharacterSpecializationRanksSchema.COLUMN_CHARACTER_ID + " = ?";
		db.delete(CharacterSpecializationRanksSchema.TABLE_NAME, selection, selectionArgs);
		for(Map.Entry<Specialization, Short> entry : instance.getSpecializationRanks().entrySet()) {
			if(entry.getValue() != null) {
				result &= (db.insertWithOnConflict(CharacterSpecializationRanksSchema.TABLE_NAME, null,
												   getSpecializationRanksContentValues(instance.getId(), entry.getKey().getId(),
																					   entry.getValue()),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

		selection = CharacterSpellListRanksSchema.COLUMN_CHARACTER_ID + " = ?";
		db.delete(CharacterSpellListRanksSchema.TABLE_NAME, selection, selectionArgs);
		for(Map.Entry<SpellList, Short> entry : instance.getSpellListRanks().entrySet()) {
			if(entry.getValue() != null) {
				result &= (db.insertWithOnConflict(CharacterSpellListRanksSchema.TABLE_NAME, null,
												   getSpellListRanksContentValues(instance.getId(), entry.getKey().getId(),
																					   entry.getValue()),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

		selection = CharacterSkillCostsSchema.COLUMN_CHARACTER_ID + " = ?";
		db.delete(CharacterSkillCostsSchema.TABLE_NAME, selection, selectionArgs);
		for(Map.Entry<Skill, DevelopmentCostGroup> entry : instance.getSkillCosts().entrySet()) {
			if(entry.getValue() != null && !DevelopmentCostGroup.NONE.equals(entry.getValue())) {
				result &= (db.insertWithOnConflict(CharacterSkillCostsSchema.TABLE_NAME, null,
												   getSkillCostsContentValues(instance.getId(),
																			  entry.getKey().getId(),
																			  entry.getValue()),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

		selection = CharacterTalentParametersSchema.COLUMN_CHARACTER_ID + " = ?";
		db.delete(CharacterTalentParametersSchema.TABLE_NAME, selection, selectionArgs);
		selection = CharacterTalentsSchema.COLUMN_CHARACTER_ID + " = ?";
		db.delete(CharacterTalentsSchema.TABLE_NAME, selection, selectionArgs);
		for(TalentInstance talentInstance : instance.getTalentInstances()) {
			int newId = (int)db.insertWithOnConflict(CharacterTalentsSchema.TABLE_NAME, null,
												getTalentTiersContentValues(instance.getId(),
																			talentInstance.getId(),
																			talentInstance.getTalent().getId(),
																			talentInstance.getTiers()),
												SQLiteDatabase.CONFLICT_NONE);
			talentInstance.setId(newId);
			result &= (newId != -1);
			for(Map.Entry<Parameter, Object> paramEntry : talentInstance.getParameterValues().entrySet()) {
				result &= (db.insertWithOnConflict(CharacterTalentParametersSchema.TABLE_NAME, null,
												   getTalentParametersContentValues(instance.getId(),
																				talentInstance.getId(),
																				paramEntry.getKey().name(),
																				paramEntry.getValue()),
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

		selection = CharacterCurrentLevelSkillRanksSchema.COLUMN_CHARACTER_ID + " = ?";
		db.delete(CharacterCurrentLevelSkillRanksSchema.TABLE_NAME, selection, selectionArgs);
		for(Map.Entry<Skill, Short> entry : instance.getCurrentLevelSkillRanks().entrySet()) {
			if(entry.getValue() != null) {
				result &= (db.insertWithOnConflict(CharacterCurrentLevelSkillRanksSchema.TABLE_NAME, null,
												   getCurrentLevelSkillRanksContentValues(instance.getId(),
																						  entry.getKey().getId(),
																						  entry.getValue()),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

		selection = CharacterCurrentLevelSpecializationRanksSchema.COLUMN_CHARACTER_ID + " = ?";
		db.delete(CharacterCurrentLevelSpecializationRanksSchema.TABLE_NAME, selection, selectionArgs);
		for(Map.Entry<Specialization, Short> entry : instance.getCurrentLevelSpecializationRanks().entrySet()) {
			if(entry.getValue() != null) {
				result &= (db.insertWithOnConflict(CharacterCurrentLevelSpecializationRanksSchema.TABLE_NAME, null,
												   getCurrentLevelSpecializationRanksContentValues(instance.getId(),
																							  entry.getKey().getId(),
																							  entry.getValue()),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

		selection = CharacterCurrentLevelSpellListRanksSchema.COLUMN_CHARACTER_ID + " = ?";
		db.delete(CharacterCurrentLevelSpellListRanksSchema.TABLE_NAME, selection, selectionArgs);
		for(Map.Entry<SpellList, Short> entry : instance.getCurrentLevelSpellListRanks().entrySet()) {
			if(entry.getValue() != null) {
				result &= (db.insertWithOnConflict(CharacterCurrentLevelSpellListRanksSchema.TABLE_NAME, null,
												   getCurrentLevelSpellListRanksContentValues(instance.getId(),
																							  entry.getKey().getId(),
																							  entry.getValue()),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

		selection = CharacterPurchasedCultureRanksSchema.COLUMN_CHARACTER_ID + " = ?";
		db.delete(CharacterPurchasedCultureRanksSchema.TABLE_NAME, selection, selectionArgs);
		for(Map.Entry<DatabaseObject, Short> entry : instance.getPurchasedCultureRanks().entrySet()) {
			if(entry.getValue() != null) {
				Integer specializationId = null;
				Integer skillId = null;
				if(entry.getKey() != null) {
					if(entry.getKey() instanceof Specialization) {
						specializationId = entry.getKey().getId();
					}
					else if(entry.getKey() instanceof Skill) {
						skillId = entry.getKey().getId();
					}
				}
				result &= (db.insertWithOnConflict(CharacterPurchasedCultureRanksSchema.TABLE_NAME, null,
												   getPurchasedCultureRanksContentValues(instance.getId(), specializationId,
																						 skillId, entry.getValue()),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
			}
		}

		selection = CharacterProfessionSkillsSchema.COLUMN_CHARACTER_ID + " = ?";
		db.delete(CharacterProfessionSkillsSchema.TABLE_NAME, selection, selectionArgs);
		for(DatabaseObject databaseObject : instance.getProfessionSkills()) {
			Integer specializationId = null;
			Integer skillId = null;
			Integer spellListId = null;
			if(databaseObject instanceof Specialization) {
				specializationId = databaseObject.getId();
			}
			else if(databaseObject instanceof Skill) {
				skillId = databaseObject.getId();
			}
			else if(databaseObject instanceof SpellList) {
				spellListId = databaseObject.getId();
			}
			result &= (db.insertWithOnConflict(CharacterProfessionSkillsSchema.TABLE_NAME, null,
											   getProfessionSkillsContentValues(instance.getId(),
																				specializationId,
																				skillId,
																				spellListId),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}

		selection = CharacterKnacksSchema.COLUMN_CHARACTER_ID + " = ?";
		db.delete(CharacterKnacksSchema.TABLE_NAME, selection, selectionArgs);
		for(DatabaseObject databaseObject : instance.getProfessionSkills()) {
			Integer specializationId = null;
			Integer skillId = null;
			Integer spellListId = null;
			if(databaseObject instanceof Specialization) {
				specializationId = databaseObject.getId();
			}
			else if(databaseObject instanceof Skill) {
				skillId = databaseObject.getId();
			}
			else if(databaseObject instanceof SpellList) {
				spellListId = databaseObject.getId();
			}
			result &= (db.insertWithOnConflict(CharacterKnacksSchema.TABLE_NAME, null,
											   getKnacksContentValues(instance.getId(), specializationId, skillId, spellListId),
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

	private ContentValues getSpecializationRanksContentValues(int characterId, int specializationId, short skillRanks) {
		ContentValues values = new ContentValues(3);

		values.put(CharacterSpecializationRanksSchema.COLUMN_CHARACTER_ID, characterId);
		values.put(CharacterSpecializationRanksSchema.COLUMN_SPECIALIZATION_ID, specializationId);
		values.put(CharacterSpecializationRanksSchema.COLUMN_RANKS, skillRanks);

		return values;
	}

	private ContentValues getSpellListRanksContentValues(int characterId, int spellListId, short skillRanks) {
		ContentValues values = new ContentValues(3);

		values.put(CharacterSpellListRanksSchema.COLUMN_CHARACTER_ID, characterId);
		values.put(CharacterSpellListRanksSchema.COLUMN_SPELL_LIST_ID, spellListId);
		values.put(CharacterSpellListRanksSchema.COLUMN_RANKS, skillRanks);

		return values;
	}

	private ContentValues getCurrentLevelSkillRanksContentValues(int characterId, int skillId, short skillRanks) {
		ContentValues values = new ContentValues(3);

		values.put(CharacterCurrentLevelSkillRanksSchema.COLUMN_CHARACTER_ID, characterId);
		values.put(CharacterCurrentLevelSkillRanksSchema.COLUMN_SKILL_ID, skillId);
		values.put(CharacterCurrentLevelSkillRanksSchema.COLUMN_RANKS, skillRanks);

		return values;
	}

	private ContentValues getCurrentLevelSpecializationRanksContentValues(int characterId, int specializationId, short skillRanks) {
		ContentValues values = new ContentValues(3);

		values.put(CharacterCurrentLevelSpecializationRanksSchema.COLUMN_CHARACTER_ID, characterId);
		values.put(CharacterCurrentLevelSpecializationRanksSchema.COLUMN_SPECIALIZATION_ID, specializationId);
		values.put(CharacterCurrentLevelSpecializationRanksSchema.COLUMN_RANKS, skillRanks);

		return values;
	}

	private ContentValues getCurrentLevelSpellListRanksContentValues(int characterId, int spellListId, short skillRanks) {
		ContentValues values = new ContentValues(3);

		values.put(CharacterCurrentLevelSpellListRanksSchema.COLUMN_CHARACTER_ID, characterId);
		values.put(CharacterCurrentLevelSpellListRanksSchema.COLUMN_SPELL_LIST_ID, spellListId);
		values.put(CharacterCurrentLevelSpellListRanksSchema.COLUMN_RANKS, skillRanks);

		return values;
	}

	private ContentValues getPurchasedCultureRanksContentValues(int characterId, Integer specializationId, Integer skillId,
																short ranks) {
		ContentValues values = new ContentValues(4);

		values.put(CharacterPurchasedCultureRanksSchema.COLUMN_CHARACTER_ID, characterId);
		if(specializationId == null) {
			values.putNull(CharacterPurchasedCultureRanksSchema.COLUMN_SPECIALIZATION_ID);
		}
		else {
			values.put(CharacterPurchasedCultureRanksSchema.COLUMN_SPECIALIZATION_ID, specializationId);
		}
		if(skillId == null) {
			values.putNull(CharacterPurchasedCultureRanksSchema.COLUMN_SKILL_ID);
		}
		else {
			values.put(CharacterPurchasedCultureRanksSchema.COLUMN_SKILL_ID, skillId);
		}
		values.put(CharacterPurchasedCultureRanksSchema.COLUMN_RANKS, ranks);

		return values;
	}

	private ContentValues getProfessionSkillsContentValues(int characterId, Integer specializationId, Integer skillId,
														   Integer spellListId) {
		ContentValues values = new ContentValues(4);

		values.put(CharacterProfessionSkillsSchema.COLUMN_CHARACTER_ID, characterId);
		if(specializationId == null) {
			values.putNull(CharacterProfessionSkillsSchema.COLUMN_SPECIALIZATION_ID);
		}
		else {
			values.put(CharacterProfessionSkillsSchema.COLUMN_SPECIALIZATION_ID, specializationId);
		}
		if(skillId == null) {
			values.putNull(CharacterProfessionSkillsSchema.COLUMN_SKILL_ID);
		}
		else {
			values.put(CharacterProfessionSkillsSchema.COLUMN_SKILL_ID, skillId);
		}
		if(spellListId == null) {
			values.putNull(CharacterProfessionSkillsSchema.COLUMN_SPELL_LIST_ID);
		}
		else {
			values.put(CharacterProfessionSkillsSchema.COLUMN_SPELL_LIST_ID, spellListId);
		}

		return values;
	}

	private ContentValues getKnacksContentValues(int characterId, Integer specializationId, Integer skillId,
												 Integer spellListId) {
		ContentValues values = new ContentValues(4);

		values.put(CharacterKnacksSchema.COLUMN_CHARACTER_ID, characterId);
		if(specializationId == null) {
			values.putNull(CharacterKnacksSchema.COLUMN_SPECIALIZATION_ID);
		}
		else {
			values.put(CharacterKnacksSchema.COLUMN_SPECIALIZATION_ID, specializationId);
		}
		if(skillId == null) {
			values.putNull(CharacterKnacksSchema.COLUMN_SKILL_ID);
		}
		else {
			values.put(CharacterKnacksSchema.COLUMN_SKILL_ID, skillId);
		}
		if(spellListId == null) {
			values.putNull(CharacterKnacksSchema.COLUMN_SPELL_LIST_ID);
		}
		else {
			values.put(CharacterKnacksSchema.COLUMN_SPELL_LIST_ID, spellListId);
		}

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

	private Map<Specialization, Short> getSpecializationRanks(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CharacterSpecializationRanksSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterSpecializationRanksSchema.TABLE_NAME, CharacterSpecializationRanksSchema.COLUMNS,
									selection, selectionArgs, CharacterSpecializationRanksSchema.COLUMN_SPECIALIZATION_ID);
		Map<Specialization, Short> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(
					CharacterSpecializationRanksSchema.COLUMN_SPECIALIZATION_ID));
			Specialization instance = specializationDao.getById(mappedId);
			if(instance != null) {
				map.put(instance, cursor.getShort(cursor.getColumnIndexOrThrow(CharacterSpecializationRanksSchema.COLUMN_RANKS)));
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private Map<SpellList, Short> getSpellListRanks(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CharacterSpellListRanksSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterSpellListRanksSchema.TABLE_NAME, CharacterSpellListRanksSchema.COLUMNS, selection,
									selectionArgs, CharacterSpellListRanksSchema.COLUMN_SPELL_LIST_ID);
		Map<SpellList, Short> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSpellListRanksSchema.COLUMN_SPELL_LIST_ID));
			SpellList instance = spellListDao.getById(mappedId);
			if(instance != null) {
				map.put(instance, cursor.getShort(cursor.getColumnIndexOrThrow(CharacterSpellListRanksSchema.COLUMN_RANKS)));
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private Map<Skill, Short> getCurrentLevelSkillRanks(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CharacterCurrentLevelSkillRanksSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterCurrentLevelSkillRanksSchema.TABLE_NAME,
									CharacterCurrentLevelSkillRanksSchema.COLUMNS, selection, selectionArgs,
									CharacterCurrentLevelSkillRanksSchema.COLUMN_SKILL_ID);
		Map<Skill, Short> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CharacterCurrentLevelSkillRanksSchema.COLUMN_SKILL_ID));
			Skill instance = skillDao.getById(mappedId);
			if(instance != null) {
				map.put(instance, cursor.getShort(cursor.getColumnIndexOrThrow(
						CharacterCurrentLevelSkillRanksSchema.COLUMN_RANKS)));
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private Map<Specialization, Short> getCurrentLevelSpecializationRanks(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CharacterCurrentLevelSpecializationRanksSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterCurrentLevelSpecializationRanksSchema.TABLE_NAME,
				CharacterCurrentLevelSpecializationRanksSchema.COLUMNS, selection, selectionArgs,
				CharacterCurrentLevelSpecializationRanksSchema.COLUMN_SPECIALIZATION_ID);
		Map<Specialization, Short> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(
					CharacterCurrentLevelSpecializationRanksSchema.COLUMN_SPECIALIZATION_ID));
			Specialization instance = specializationDao.getById(mappedId);
			if(instance != null) {
				map.put(instance, cursor.getShort(cursor.getColumnIndexOrThrow(
						CharacterCurrentLevelSpecializationRanksSchema.COLUMN_RANKS)));
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private Map<SpellList, Short> getCurrentLevelSpellListRanks(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CharacterCurrentLevelSpellListRanksSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterCurrentLevelSpellListRanksSchema.TABLE_NAME,
									CharacterCurrentLevelSpellListRanksSchema.COLUMNS, selection, selectionArgs,
									CharacterCurrentLevelSpellListRanksSchema.COLUMN_SPELL_LIST_ID);
		Map<SpellList, Short> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(
					CharacterCurrentLevelSpellListRanksSchema.COLUMN_SPELL_LIST_ID));
			SpellList instance = spellListDao.getById(mappedId);
			if(instance != null) {
				map.put(instance, cursor.getShort(cursor.getColumnIndexOrThrow(
						CharacterCurrentLevelSpellListRanksSchema.COLUMN_RANKS)));
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private Map<DatabaseObject, Short> getPurchasedCultureRanks(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CharacterPurchasedCultureRanksSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterPurchasedCultureRanksSchema.TABLE_NAME,
									CharacterPurchasedCultureRanksSchema.COLUMNS, selection, selectionArgs,
									CharacterPurchasedCultureRanksSchema.COLUMN_RANKS);
		Map<DatabaseObject, Short> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			short ranks = cursor.getShort(cursor.getColumnIndexOrThrow(CharacterPurchasedCultureRanksSchema.COLUMN_RANKS));
			if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterPurchasedCultureRanksSchema.COLUMN_SPECIALIZATION_ID))) {
				int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(
						CharacterPurchasedCultureRanksSchema.COLUMN_SPECIALIZATION_ID));
				Specialization instance = specializationDao.getById(mappedId);
				if(instance != null) {
					map.put(instance, ranks);
				}
			}
			else if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterPurchasedCultureRanksSchema.COLUMN_SKILL_ID))) {
				int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(
						CharacterPurchasedCultureRanksSchema.COLUMN_SKILL_ID));
				Skill instance = skillDao.getById(mappedId);
				if(instance != null) {
					map.put(instance, ranks);
				}
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private List<DatabaseObject> getProfessionSkills(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CharacterProfessionSkillsSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterProfessionSkillsSchema.TABLE_NAME,
									CharacterProfessionSkillsSchema.COLUMNS, selection, selectionArgs, null);
		List<DatabaseObject> list = new ArrayList<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterProfessionSkillsSchema.COLUMN_SPECIALIZATION_ID))) {
				int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(
						CharacterProfessionSkillsSchema.COLUMN_SPECIALIZATION_ID));
				Specialization instance = specializationDao.getById(mappedId);
				if(instance != null) {
					list.add(instance);
				}
			}
			else if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterProfessionSkillsSchema.COLUMN_SKILL_ID))) {
				int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(
						CharacterProfessionSkillsSchema.COLUMN_SKILL_ID));
				Skill instance = skillDao.getById(mappedId);
				if(instance != null) {
					list.add(instance);
				}
			}
			else if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterProfessionSkillsSchema.COLUMN_SPELL_LIST_ID))) {
				int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(
						CharacterProfessionSkillsSchema.COLUMN_SPELL_LIST_ID));
				SpellList instance = spellListDao.getById(mappedId);
				if(instance != null) {
					list.add(instance);
				}
			}
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}

	private List<DatabaseObject> getKnacks(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CharacterKnacksSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterKnacksSchema.TABLE_NAME,
									CharacterKnacksSchema.COLUMNS, selection, selectionArgs, null);
		List<DatabaseObject> list = new ArrayList<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterKnacksSchema.COLUMN_SPECIALIZATION_ID))) {
				int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CharacterKnacksSchema.COLUMN_SPECIALIZATION_ID));
				Specialization instance = specializationDao.getById(mappedId);
				if(instance != null) {
					list.add(instance);
				}
			}
			else if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterKnacksSchema.COLUMN_SKILL_ID))) {
				int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CharacterKnacksSchema.COLUMN_SKILL_ID));
				Skill instance = skillDao.getById(mappedId);
				if(instance != null) {
					list.add(instance);
				}
			}
			else if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterKnacksSchema.COLUMN_SPELL_LIST_ID))) {
				int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CharacterKnacksSchema.COLUMN_SPELL_LIST_ID));
				SpellList instance = spellListDao.getById(mappedId);
				if(instance != null) {
					list.add(instance);
				}
			}
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}

	private ContentValues getSkillCostsContentValues(int characterId, int skillId, DevelopmentCostGroup costGroup) {
		ContentValues values = new ContentValues(3);

		values.put(CharacterSkillCostsSchema.COLUMN_CHARACTER_ID, characterId);
		values.put(CharacterSkillCostsSchema.COLUMN_SKILL_ID, skillId);
		values.put(CharacterSkillCostsSchema.COLUMN_COST_GROUP_NAME, costGroup.name());

		return values;
	}

	private Map<Skill, DevelopmentCostGroup> getSkillCosts(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CharacterSkillCostsSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterSkillCostsSchema.TABLE_NAME, CharacterSkillCostsSchema.COLUMNS, selection,
									selectionArgs, CharacterSkillCostsSchema.COLUMN_SKILL_ID);
		Map<Skill, DevelopmentCostGroup> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSkillCostsSchema.COLUMN_SKILL_ID));
			Skill instance = skillDao.getById(mappedId);
			DevelopmentCostGroup costGroup;
			costGroup = DevelopmentCostGroup.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(
					CharacterSkillCostsSchema.COLUMN_COST_GROUP_NAME)));
			if(instance != null) {
				map.put(instance, costGroup);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private List<TalentInstance> getTalentInstances(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		final String selection = CharacterTalentsSchema.COLUMN_CHARACTER_ID + " = ?";

		Cursor cursor = super.query(CharacterTalentsSchema.TABLE_NAME, CharacterTalentsSchema.COLUMNS, selection,
				selectionArgs, CharacterTalentsSchema.COLUMN_TALENT_ID);
		List<TalentInstance> list = new ArrayList<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CharacterTalentsSchema.COLUMN_ID));
			TalentInstance talentInstance = new TalentInstance();
			talentInstance.setId(mappedId);
			Talent talent = talentDao.getById(cursor.getInt(
					cursor.getColumnIndexOrThrow(CharacterTalentsSchema.COLUMN_TALENT_ID)));
			talentInstance.setTalent(talent);
			talentInstance.setTiers(cursor.getShort(cursor.getColumnIndexOrThrow(CharacterTalentsSchema.COLUMN_TIERS)));
			list.add(talentInstance);
			talentInstance.setParameterValues(getTalentParameterInstances(id, mappedId));
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}

	private ContentValues getTalentTiersContentValues(int characterId, int talentInstanceId, int talentId, short talentTiers) {
		ContentValues values;

		if(talentInstanceId != -1) {
			values = new ContentValues(4);
			values.put(COLUMN_ID, talentInstanceId);
		}
		else {
			values = new ContentValues(3);
		}
		values.put(CharacterTalentsSchema.COLUMN_CHARACTER_ID, characterId);
		values.put(CharacterTalentsSchema.COLUMN_TALENT_ID, talentId);
		values.put(CharacterTalentsSchema.COLUMN_TIERS, talentTiers);

		return values;
	}

	private ContentValues getTalentParametersContentValues(int characterId, int talentInstanceId, String parameterName, Object
			value) {
		ContentValues values = new ContentValues(5);

		values.put(CharacterTalentParametersSchema.COLUMN_CHARACTER_ID, characterId);
		values.put(CharacterTalentParametersSchema.COLUMN_TALENT_INSTANCE_ID, talentInstanceId);
		values.put(CharacterTalentParametersSchema.COLUMN_PARAMETER_NAME, parameterName);
		if(value == null) {
			values.putNull(CharacterTalentParametersSchema.COLUMN_INT_VALUE);
			values.putNull(CharacterTalentParametersSchema.COLUMN_ENUM_NAME);
		}
		else if(value instanceof Integer) {
			values.put(CharacterTalentParametersSchema.COLUMN_INT_VALUE, (Integer)value);
			values.putNull(CharacterTalentParametersSchema.COLUMN_ENUM_NAME);
		}
		else {
			values.putNull(CharacterTalentParametersSchema.COLUMN_INT_VALUE);
			values.put(CharacterTalentParametersSchema.COLUMN_ENUM_NAME, (String)value);
		}

		return values;
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

	private Map<Parameter, Object> getTalentParameterInstances(int characterId, int talentId) {
		final String selectionArgs[] = { String.valueOf(characterId), String.valueOf(talentId) };
		final String selection = CharacterTalentParametersSchema.COLUMN_CHARACTER_ID
				+ " = ? AND "
				+ CharacterTalentParametersSchema.COLUMN_TALENT_INSTANCE_ID
				+ " = ?";

		Cursor cursor = super.query(CharacterTalentParametersSchema.TABLE_NAME, CharacterTalentParametersSchema.COLUMNS,
									selection, selectionArgs, CharacterTalentParametersSchema.COLUMN_PARAMETER_NAME);
		Map<Parameter, Object> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String paramName = cursor.getString(cursor.getColumnIndexOrThrow(
					CharacterTalentParametersSchema.COLUMN_PARAMETER_NAME));
			Parameter parameter = Parameter.valueOf(paramName);
			Object value = null;
			if (!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterTalentParametersSchema.COLUMN_INT_VALUE))) {
				value = cursor.getInt(cursor.getColumnIndexOrThrow(CharacterTalentParametersSchema.COLUMN_INT_VALUE));
			}
			else if (!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterTalentParametersSchema.COLUMN_ENUM_NAME))) {
				value = cursor.getString(cursor.getColumnIndexOrThrow(CharacterTalentParametersSchema.COLUMN_ENUM_NAME));
			}
			map.put(parameter, value);
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

}
