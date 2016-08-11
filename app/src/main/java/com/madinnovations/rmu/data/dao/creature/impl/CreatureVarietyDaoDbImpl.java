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
import com.madinnovations.rmu.data.dao.combat.CriticalCodeDao;
import com.madinnovations.rmu.data.dao.common.SizeDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.creature.CreatureTypeDao;
import com.madinnovations.rmu.data.dao.creature.CreatureVarietyDao;
import com.madinnovations.rmu.data.dao.creature.OutlookDao;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureVarietySchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietySkillsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyStatsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyTalentTiersSchema;
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link CreatureVariety} objects in a SQLite database.
 */
@Singleton
public class CreatureVarietyDaoDbImpl extends BaseDaoDbImpl<CreatureVariety> implements CreatureVarietyDao, CreatureVarietySchema {
	private CreatureTypeDao creatureTypeDao;
	private SizeDao sizeDao;
	private CriticalCodeDao criticalCodeDao;
	private RealmDao realmDao;
	private OutlookDao outlookDao;
	private StatDao statDao;
	private SkillDao skillDao;
	private TalentDao talentDao;

	/**
	 * Creates a new instance of CreatureVarietyDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 * @param creatureTypeDao  a {@link CreatureTypeDao} instance
	 * @param sizeDao  a {@link SizeDao} instance
	 * @param criticalCodeDao  a {@link CriticalCodeDao} instance
	 * @param realmDao  a {@link RealmDao} instance
	 * @param outlookDao  a {@link OutlookDao} instance
	 * @param statDao  a {@link StatDao} instance
	 * @param skillDao  a {@link SkillDao} instance
	 * @param talentDao  a {@link TalentDao} instance
	 */
	@Inject
	public CreatureVarietyDaoDbImpl(@NonNull SQLiteOpenHelper helper, @NonNull CreatureTypeDao creatureTypeDao, @NonNull SizeDao sizeDao,
									@NonNull CriticalCodeDao criticalCodeDao, @NonNull RealmDao realmDao, @NonNull OutlookDao outlookDao,
									@NonNull StatDao statDao, @NonNull SkillDao skillDao, @NonNull TalentDao talentDao) {
		super(helper);
		this.creatureTypeDao = creatureTypeDao;
		this.sizeDao = sizeDao;
		this.criticalCodeDao = criticalCodeDao;
		this.realmDao = realmDao;
		this.outlookDao = outlookDao;
		this.statDao = statDao;
		this.skillDao = skillDao;
		this.talentDao = talentDao;
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
	protected int getId(CreatureVariety instance) {
		return instance.getId();
	}

	@Override
	protected void setId(CreatureVariety instance, int id) {
		instance.setId(id);
	}

	@Override
	protected CreatureVariety cursorToEntity(@NonNull Cursor cursor) {
		CreatureVariety instance = new CreatureVariety();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
		instance.setType(creatureTypeDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TYPE_ID))));
		instance.setTypicalLevel(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_TYPICAL_LEVEL)));
		instance.setLevelSpread(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LEVEL_SPREAD)).charAt(0));
		instance.setRacialStatBonuses(getRacialStatBonuses(instance.getId()));
		instance.setHeight(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT)));
		instance.setLength(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_LENGTH)));
		instance.setWeight(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)));
		instance.setHealingRate(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_HEALING_RATE)));
		instance.setBaseHits(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BASE_HITS)));
		instance.setBaseEndurance(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BASE_ENDURANCE)));
		instance.setSize(sizeDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SIZE_ID))));
		instance.setArmorType(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_ARMOR_TYPE)));
		instance.setCriticalCode(criticalCodeDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CRITICAL_CODE_ID))));
		instance.setBaseMovementRate(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BASE_MOVEMENT_RATE)));
		instance.setBaseChannellingRR(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BASE_CHANNELING_RR)));
		instance.setBaseEssenceRR(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BASE_ESSENCE_RR)));
		instance.setBaseMentalismRR(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BASE_MENTALISM_RR)));
		instance.setBasePhysicalRR(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BASE_PHYSICAL_RR)));
		instance.setBaseFearRR(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BASE_FEAR_RR)));
		instance.setRealm1(realmDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REALM1_ID))));
		if(cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_REALM2_ID))) {
			instance.setRealm2(null);
		}
		else {
			instance.setRealm2(realmDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REALM2_ID))));
		}
		instance.setBaseStride(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BASE_STRIDE)));
		instance.setLeftoverDP(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_LEFTOVER_DP)));
		instance.setOutlook(outlookDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_OUTLOOK_ID))));
		instance.setTalentTiersMap(getTalentTiers(instance.getId()));
		instance.setPrimaryAttack(skillDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIMARY_ATTACK_ID))));
		setSkills(instance);

		return instance;
	}

	@Override
	protected ContentValues getContentValues(CreatureVariety instance) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COLUMN_NAME, instance.getName());
		initialValues.put(COLUMN_DESCRIPTION, instance.getDescription());
		initialValues.put(COLUMN_TYPE_ID, instance.getType().getId());
		return initialValues;
	}

	private Map<Stat, Short> getRacialStatBonuses(int creatureVarietyId) {
		final String selectionArgs[] = { String.valueOf(creatureVarietyId) };
		final String selection = VarietyStatsSchema.COLUMN_VARIETY_ID + " = ?";

		Cursor cursor = super.query(VarietyStatsSchema.TABLE_NAME, VarietyStatsSchema.COLUMNS, selection,
				selectionArgs, VarietyStatsSchema.COLUMN_STAT_ID);
		Map<Stat, Short> statShortMap = new HashMap<>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(VarietyStatsSchema.COLUMN_STAT_ID));
			short bonus = cursor.getShort(cursor.getColumnIndexOrThrow(VarietyStatsSchema.COLUMN_BONUS));
			Stat stat = statDao.getById(mappedId);
			if(stat != null) {
				statShortMap.put(stat, bonus);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return statShortMap;
	}

	private Map<Talent, Short> getTalentTiers(int creatureVarietyId) {
		final String selectionArgs[] = { String.valueOf(creatureVarietyId) };
		final String selection = VarietyTalentTiersSchema.COLUMN_VARIETY_ID + " = ?";

		Cursor cursor = super.query(VarietyTalentTiersSchema.TABLE_NAME, VarietyTalentTiersSchema.COLUMNS, selection,
				selectionArgs, VarietyTalentTiersSchema.COLUMN_TALENT_ID);
		Map<Talent, Short> talentTiersMap = new HashMap<>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(VarietyTalentTiersSchema.COLUMN_TALENT_ID));
			short tiers = cursor.getShort(cursor.getColumnIndexOrThrow(VarietyTalentTiersSchema.COLUMN_TIERS));
			Talent talent = talentDao.getById(mappedId);
			if(talent != null) {
				talentTiersMap.put(talent, tiers);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return talentTiersMap;
	}

	private void setSkills(CreatureVariety variety) {
		final String selectionArgs[] = { String.valueOf(variety.getId()) };
		final String selection = VarietySkillsSchema.COLUMN_VARIETY_ID + " = ?";

		Cursor cursor = super.query(VarietySkillsSchema.TABLE_NAME, VarietySkillsSchema.COLUMNS, selection,
				selectionArgs, VarietySkillsSchema.COLUMN_ATT_PRIME_SECONDARY);
		List<Skill> attackList = new ArrayList<>();
		List<Skill> primeList = new ArrayList<>();
		List<Skill> secondaryList = new ArrayList<>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(VarietySkillsSchema.COLUMN_SKILL_ID));
			short attPrimeSecondary = cursor.getShort(cursor.getColumnIndexOrThrow(VarietySkillsSchema.COLUMN_ATT_PRIME_SECONDARY));
			Skill skill = skillDao.getById(mappedId);
			if(skill != null) {
				if(attPrimeSecondary == 0) {
					attackList.add(skill);
				}
				if(attPrimeSecondary == 1) {
					primeList.add(skill);
				}
				if(attPrimeSecondary == 2) {
					secondaryList.add((skill));
				}
			}
			cursor.moveToNext();
		}
		cursor.close();

		variety.setSecondaryAttacks(attackList);
		variety.setPrimeSkills(primeList);
		variety.setSecondarySkills(secondaryList);
	}
}
