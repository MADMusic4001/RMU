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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.AttackDao;
import com.madinnovations.rmu.data.dao.common.SizeDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.creature.CreatureTypeDao;
import com.madinnovations.rmu.data.dao.creature.CreatureVarietyDao;
import com.madinnovations.rmu.data.dao.creature.OutlookDao;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureVarietySchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyAttacksSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyCriticalCodesSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietySkillsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyStatsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyTalentParametersSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyTalentTiersSchema;
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.dao.spells.SpellListDao;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.CriticalCode;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.SkillBonus;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;
import com.madinnovations.rmu.view.RMUAppException;

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
public class CreatureVarietyDaoDbImpl extends BaseDaoDbImpl<CreatureVariety> implements CreatureVarietyDao,
		CreatureVarietySchema {
	@SuppressWarnings("unused")
	private static final String TAG = "CreatureVarietyDaoDbImp";
	private CreatureTypeDao creatureTypeDao;
	private SizeDao sizeDao;
	private RealmDao realmDao;
	private OutlookDao outlookDao;
	private SkillDao skillDao;
	private SpecializationDao specializationDao;
	private SpellListDao spellListDao;
	private TalentDao talentDao;
	private AttackDao attackDao;

	/**
	 * Creates a new instance of CreatureVarietyDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 * @param creatureTypeDao  a {@link CreatureTypeDao} instance
	 * @param sizeDao  a {@link SizeDao} instance
	 * @param realmDao  a {@link RealmDao} instance
	 * @param outlookDao  an {@link OutlookDao} instance
	 * @param skillDao  a {@link SkillDao} instance
	 * @param specializationDao  a {@link SpecializationDao} instance
	 * @param spellListDao  a {@link SpellListDao} instance
	 * @param talentDao  a {@link TalentDao} instance
	 * @param attackDao  an {@link AttackDao} instance
	 */
	@Inject
	public CreatureVarietyDaoDbImpl(@NonNull SQLiteOpenHelper helper, @NonNull CreatureTypeDao creatureTypeDao,
									@NonNull SizeDao sizeDao, @NonNull RealmDao realmDao, @NonNull OutlookDao outlookDao,
									@NonNull SkillDao skillDao, @NonNull SpecializationDao specializationDao,
									@NonNull SpellListDao spellListDao, @NonNull TalentDao talentDao,
									@NonNull AttackDao attackDao) {
		super(helper);
		this.creatureTypeDao = creatureTypeDao;
		this.sizeDao = sizeDao;
		this.realmDao = realmDao;
		this.outlookDao = outlookDao;
		this.skillDao = skillDao;
		this.specializationDao = specializationDao;
		this.spellListDao = spellListDao;
		this.talentDao = talentDao;
		this.attackDao = attackDao;
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
		instance.setCriticalCodes(getCriticalCodes(instance.getId()));
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
		instance.setTalentInstancesList(getTalentInstances(instance.getId()));
		instance.setAttackSequence(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ATTACK_SEQUENCE)));
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_CRITICAL_SIZE_MODIFIER_ID))) {
			instance.setCriticalSizeModifier(sizeDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(
					COLUMN_CRITICAL_SIZE_MODIFIER_ID))));
		}
		instance.setAttackBonusesMap(getAttackBonuses(instance.getId()));
		instance.setSkillBonusesList(getSkillBonuses(instance.getId()));

		return instance;
	}

	@Override
	protected ContentValues getContentValues(CreatureVariety instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(28);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(27);
		}
		values.put(COLUMN_NAME, instance.getName());
		values.put(COLUMN_DESCRIPTION, instance.getDescription());
		values.put(COLUMN_TYPE_ID, instance.getType().getId());
		values.put(COLUMN_TYPICAL_LEVEL, instance.getTypicalLevel());
		values.put(COLUMN_LEVEL_SPREAD, String.valueOf(instance.getLevelSpread()));
		values.put(COLUMN_HEIGHT, instance.getHeight());
		values.put(COLUMN_LENGTH, instance.getLength());
		values.put(COLUMN_WEIGHT, instance.getWeight());
		values.put(COLUMN_HEALING_RATE, instance.getHealingRate());
		values.put(COLUMN_BASE_HITS, instance.getBaseHits());
		values.put(COLUMN_BASE_ENDURANCE, instance.getBaseEndurance());
		values.put(COLUMN_SIZE_ID, instance.getSize().getId());
		values.put(COLUMN_ARMOR_TYPE, instance.getArmorType());
		values.put(COLUMN_BASE_MOVEMENT_RATE, instance.getBaseMovementRate());
		values.put(COLUMN_BASE_CHANNELING_RR, instance.getBaseChannellingRR());
		values.put(COLUMN_BASE_ESSENCE_RR, instance.getBaseEssenceRR());
		values.put(COLUMN_BASE_MENTALISM_RR, instance.getBaseMentalismRR());
		values.put(COLUMN_BASE_PHYSICAL_RR, instance.getBasePhysicalRR());
		values.put(COLUMN_BASE_FEAR_RR, instance.getBaseFearRR());
		values.put(COLUMN_REALM1_ID, instance.getRealm1().getId());
		values.put(COLUMN_REALM2_ID, instance.getRealm2() != null ? instance.getRealm2().getId() : null);
		values.put(COLUMN_BASE_STRIDE, instance.getBaseStride());
		values.put(COLUMN_LEFTOVER_DP, instance.getLeftoverDP());
		values.put(COLUMN_OUTLOOK_ID, instance.getOutlook().getId());
		if(instance.getCriticalSizeModifier() == null) {
			values.putNull(COLUMN_CRITICAL_SIZE_MODIFIER_ID);
		}
		else {
			values.put(COLUMN_CRITICAL_SIZE_MODIFIER_ID, instance.getCriticalSizeModifier().getId());
		}
		values.put(COLUMN_ATTACK_SEQUENCE, instance.getAttackSequence());
		return values;
	}

	@Override
	protected boolean saveRelationships(SQLiteDatabase db, CreatureVariety instance) {
		boolean result = saveRacialStatBonuses(db, instance.getId(), instance.getRacialStatBonuses());
		result &= saveCriticalCodes(db, instance.getId(), instance.getCriticalCodes());
		result &= saveTalentInstancesList(db, instance.getId(), instance.getTalentInstancesList());
		result &= saveAttackBonusesMap(db, instance.getId(), instance.getAttackBonusesMap());
		result &= saveSkillBonusesList(db, instance.getId(), instance.getSkillBonusesList());
		return result;
	}

	@Override
	protected boolean deleteRelationships(SQLiteDatabase db, int id) {
		boolean result;

		final String selectionArgs[] = { String.valueOf(id) };
		String selection = VarietyStatsSchema.COLUMN_VARIETY_ID + " = ?";
		result = (db.delete(VarietyStatsSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = VarietyCriticalCodesSchema.COLUMN_VARIETY_ID + " = ?";
		result |= (db.delete(VarietyCriticalCodesSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = VarietyTalentParametersSchema.COLUMN_VARIETY_ID + " = ?";
		result |= (db.delete(VarietyTalentParametersSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = VarietyTalentTiersSchema.COLUMN_VARIETY_ID + " = ?";
		result |= (db.delete(VarietyTalentTiersSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = VarietyAttacksSchema.COLUMN_VARIETY_ID + " = ?";
		result |= (db.delete(VarietyAttacksSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = VarietySkillsSchema.COLUMN_VARIETY_ID + " = ?";
		result |= (db.delete(VarietySkillsSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		return result;
	}

	private boolean saveRacialStatBonuses(SQLiteDatabase db, int creatureVarietyId, Map<Statistic, Short> statBonusesMap) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(creatureVarietyId) };
		final String selection = VarietyStatsSchema.COLUMN_VARIETY_ID + " = ?";

		db.delete(VarietyStatsSchema.TABLE_NAME, selection, selectionArgs);

		ContentValues values = new ContentValues(3);
		for(Map.Entry<Statistic, Short> entry : statBonusesMap.entrySet()) {
			values.put(VarietyStatsSchema.COLUMN_VARIETY_ID, creatureVarietyId);
			values.put(VarietyStatsSchema.COLUMN_STAT_NAME, entry.getKey().name());
			values.put(VarietyStatsSchema.COLUMN_BONUS, entry.getValue());
			result &= (db.insertWithOnConflict(VarietyStatsSchema.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		return result;
	}

	private boolean saveCriticalCodes(SQLiteDatabase db, int creatureVarietyId, List<CriticalCode> criticalCodeList) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(creatureVarietyId) };
		final String selection = VarietyCriticalCodesSchema.COLUMN_VARIETY_ID + " = ?";

		db.delete(VarietyCriticalCodesSchema.TABLE_NAME, selection, selectionArgs);

		ContentValues values = new ContentValues(2);
		for(CriticalCode criticalCode : criticalCodeList) {
			values.put(VarietyCriticalCodesSchema.COLUMN_VARIETY_ID, creatureVarietyId);
			values.put(VarietyCriticalCodesSchema.COLUMN_CRITICAL_CODE_NAME, criticalCode.name());
			result &= (db.insertWithOnConflict(VarietyCriticalCodesSchema.TABLE_NAME, null, values,
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		return result;
	}

	private boolean saveTalentInstancesList(SQLiteDatabase db, int creatureVarietyId, List<TalentInstance> talentInstancesList) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(creatureVarietyId) };
		final String selection = VarietyTalentTiersSchema.COLUMN_VARIETY_ID + " = ?";

		db.delete(VarietyTalentParametersSchema.TABLE_NAME, selection, selectionArgs);
		db.delete(VarietyTalentTiersSchema.TABLE_NAME, selection, selectionArgs);

		ContentValues values;
		for(TalentInstance talentInstance : talentInstancesList) {
			if(talentInstance.getId() != -1) {
				values = new ContentValues(4);
				values.put(VarietyTalentTiersSchema.COLUMN_ID, talentInstance.getId());
			}
			else {
				values = new ContentValues(3);
			}
			values.put(VarietyTalentTiersSchema.COLUMN_VARIETY_ID, creatureVarietyId);
			values.put(VarietyTalentTiersSchema.COLUMN_TALENT_ID, talentInstance.getTalent().getId());
			values.put(VarietyTalentTiersSchema.COLUMN_TIERS, talentInstance.getTiers());
			int id = (int)db.insertWithOnConflict(VarietyTalentTiersSchema.TABLE_NAME, null, values,
											 SQLiteDatabase.CONFLICT_NONE);
			talentInstance.setId(id);
			result &= (id != -1);
			result &= saveTalentParameterValues(db, creatureVarietyId, talentInstance.getId(),
												talentInstance.getParameterValues());
		}
		return result;
	}

	private boolean saveAttackBonusesMap(SQLiteDatabase db, int creatureVarietyId, Map<Attack, Short> attackBonusesMap) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(creatureVarietyId) };
		final String selection = VarietyAttacksSchema.COLUMN_VARIETY_ID + " = ?";

		db.delete(VarietyAttacksSchema.TABLE_NAME, selection, selectionArgs);

		ContentValues values = new ContentValues(3);
		for(Map.Entry<Attack, Short> entry : attackBonusesMap.entrySet()) {
			values.put(VarietyAttacksSchema.COLUMN_VARIETY_ID, creatureVarietyId);
			values.put(VarietyAttacksSchema.COLUMN_ATTACK_ID, entry.getKey().getId());
			values.put(VarietyAttacksSchema.COLUMN_ATTACK_BONUS, entry.getValue());
			result &= (db.insertWithOnConflict(VarietyAttacksSchema.TABLE_NAME, null, values,
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		return result;
	}

	private boolean saveSkillBonusesList(SQLiteDatabase db, int creatureVarietyId, List<SkillBonus> skillBonusesList) {
		boolean result = true;
		final String selectionArgs[] = { String.valueOf(creatureVarietyId) };
		final String selection = VarietySkillsSchema.COLUMN_VARIETY_ID + " = ?";

		db.delete(VarietySkillsSchema.TABLE_NAME, selection, selectionArgs);

		ContentValues values = new ContentValues(5);
		for(SkillBonus skillBonus : skillBonusesList) {
			values.put(VarietySkillsSchema.COLUMN_VARIETY_ID, creatureVarietyId);
			if(skillBonus.getSkill() != null) {
				values.put(VarietySkillsSchema.COLUMN_SKILL_ID, skillBonus.getSkill().getId());
			}
			else {
				values.putNull(VarietySkillsSchema.COLUMN_SKILL_ID);
			}
			if(skillBonus.getSpecialization() != null) {
				values.put(VarietySkillsSchema.COLUMN_SPECIALIZATION_ID, skillBonus.getSpecialization().getId());
			}
			else {
				values.putNull(VarietySkillsSchema.COLUMN_SPECIALIZATION_ID);
			}
			if(skillBonus.getSpellList() != null) {
				values.put(VarietySkillsSchema.COLUMN_SPELL_LIST_ID, skillBonus.getSpellList().getId());
			}
			else {
				values.putNull(VarietySkillsSchema.COLUMN_SPELL_LIST_ID);
			}
			values.put(VarietySkillsSchema.COLUMN_SKILL_BONUS, skillBonus.getBonus());
			result &= (db.insertWithOnConflict(VarietySkillsSchema.TABLE_NAME, null, values,
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		return result;
	}

	private Map<Statistic, Short> getRacialStatBonuses(int creatureVarietyId) {
		final String selectionArgs[] = { String.valueOf(creatureVarietyId) };
		final String selection = VarietyStatsSchema.COLUMN_VARIETY_ID + " = ?";

		Cursor cursor = super.query(VarietyStatsSchema.TABLE_NAME, VarietyStatsSchema.COLUMNS, selection,
				selectionArgs, VarietyStatsSchema.COLUMN_STAT_NAME);
		Map<Statistic, Short> statShortMap = new HashMap<>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			short bonus = cursor.getShort(cursor.getColumnIndexOrThrow(VarietyStatsSchema.COLUMN_BONUS));
			statShortMap.put(Statistic.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(
					VarietyStatsSchema.COLUMN_STAT_NAME))), bonus);
			cursor.moveToNext();
		}
		cursor.close();

		return statShortMap;
	}

	private List<CriticalCode> getCriticalCodes(int creatureVarietyId) {
		final String selectionArgs[] = { String.valueOf(creatureVarietyId) };
		final String selection = VarietyTalentTiersSchema.COLUMN_VARIETY_ID + " = ?";

		Cursor cursor = super.query(VarietyCriticalCodesSchema.TABLE_NAME, VarietyCriticalCodesSchema.COLUMNS, selection,
				selectionArgs, VarietyCriticalCodesSchema.COLUMN_CRITICAL_CODE_NAME);
		List<CriticalCode> criticalCodesList = new ArrayList<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String criticalCodeName = cursor.getString(cursor.getColumnIndexOrThrow(
					VarietyCriticalCodesSchema.COLUMN_CRITICAL_CODE_NAME));
			try {
				CriticalCode criticalCode = CriticalCode.valueOf(criticalCodeName);
				criticalCodesList.add(criticalCode);
			}
			catch(IllegalArgumentException e) {
				throw new RMUAppException("Invalid CriticalCode name");
			}
			cursor.moveToNext();
		}
		cursor.close();

		return criticalCodesList;
	}

	private List<TalentInstance> getTalentInstances(int creatureVarietyId) {
		final String selectionArgs[] = { String.valueOf(creatureVarietyId) };
		final String selection = VarietyTalentTiersSchema.COLUMN_VARIETY_ID + " = ?";

		Cursor cursor = super.query(VarietyTalentTiersSchema.TABLE_NAME, VarietyTalentTiersSchema.COLUMNS, selection,
				selectionArgs, VarietyTalentTiersSchema.COLUMN_TALENT_ID);
		List<TalentInstance> list = new ArrayList<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TalentInstance talentInstance = new TalentInstance();
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(VarietyTalentTiersSchema.COLUMN_ID));
			talentInstance.setId(mappedId);
			Talent talent = talentDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(
					VarietyTalentTiersSchema.COLUMN_TALENT_ID)));
			talentInstance.setTalent(talent);
			talentInstance.setTiers(cursor.getShort(cursor.getColumnIndexOrThrow(VarietyTalentTiersSchema.COLUMN_TIERS)));
			list.add(talentInstance);
			talentInstance.setParameterValues(getTalentParameters(creatureVarietyId, mappedId));
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}

	private Map<Attack, Short> getAttackBonuses(int creatureVarietyId) {
		final String selectionArgs[] = { String.valueOf(creatureVarietyId) };
		final String selection = VarietyTalentTiersSchema.COLUMN_VARIETY_ID + " = ?";

		Cursor cursor = super.query(VarietyAttacksSchema.TABLE_NAME, VarietyAttacksSchema.COLUMNS, selection,
				selectionArgs, VarietyAttacksSchema.COLUMN_ATTACK_ID);
		Map<Attack, Short> attackBonusesMap = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(VarietyAttacksSchema.COLUMN_ATTACK_ID));
			short bonuses = cursor.getShort(cursor.getColumnIndexOrThrow(VarietyAttacksSchema.COLUMN_ATTACK_BONUS));
			Attack attack = attackDao.getById(mappedId);
			if(attack != null) {
				attackBonusesMap.put(attack, bonuses);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return attackBonusesMap;
	}

	private List<SkillBonus> getSkillBonuses(int creatureVarietyId) {
		final String selectionArgs[] = { String.valueOf(creatureVarietyId) };
		final String selection = VarietyTalentTiersSchema.COLUMN_VARIETY_ID + " = ?";

		Cursor cursor = super.query(VarietySkillsSchema.TABLE_NAME, VarietySkillsSchema.COLUMNS, selection,
				selectionArgs, VarietySkillsSchema.COLUMN_SKILL_ID);
		List<SkillBonus> skillBonusesList = new ArrayList<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			SkillBonus skillBonus = new SkillBonus();
			if(!cursor.isNull(cursor.getColumnIndexOrThrow(VarietySkillsSchema.COLUMN_SKILL_ID))) {
				int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(VarietySkillsSchema.COLUMN_SKILL_ID));
				skillBonus.setSkill(skillDao.getById(mappedId));
			}
			else if(!cursor.isNull(cursor.getColumnIndexOrThrow(VarietySkillsSchema.COLUMN_SPECIALIZATION_ID))) {
				int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(VarietySkillsSchema.COLUMN_SPECIALIZATION_ID));
				skillBonus.setSpecialization(specializationDao.getById(mappedId));
			}
			else if(!cursor.isNull(cursor.getColumnIndexOrThrow(VarietySkillsSchema.COLUMN_SPELL_LIST_ID))) {
				int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(VarietySkillsSchema.COLUMN_SPELL_LIST_ID));
				skillBonus.setSpellList(spellListDao.getById(mappedId));
			}
			skillBonus.setBonus(cursor.getShort(cursor.getColumnIndexOrThrow(VarietySkillsSchema.COLUMN_SKILL_BONUS)));
			skillBonusesList.add(skillBonus);
			cursor.moveToNext();
		}
		cursor.close();

		return skillBonusesList;
	}

	private Map<Parameter, Object> getTalentParameters(int varietyId, int talentInstanceId) {
		final String selectionArgs[] = { String.valueOf(varietyId), String.valueOf(talentInstanceId) };
		final String selection = VarietyTalentParametersSchema.COLUMN_VARIETY_ID +
				" = ? AND " +
				VarietyTalentParametersSchema.COLUMN_TALENT_INSTANCE_ID +
				" = ?";

		Cursor cursor = super.query(VarietyTalentParametersSchema.TABLE_NAME, VarietyTalentParametersSchema.COLUMNS, selection,
									selectionArgs, VarietyTalentParametersSchema.COLUMN_PARAMETER_NAME);
		Map<Parameter, Object> map = new HashMap<>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String parameterName = cursor.getString(cursor.getColumnIndexOrThrow(
					VarietyTalentParametersSchema.COLUMN_PARAMETER_NAME));
			Parameter instance = Parameter.valueOf(parameterName);
			Object value = null;
			if(!cursor.isNull(cursor.getColumnIndexOrThrow(VarietyTalentParametersSchema.COLUMN_INT_VALUE))) {
				value = cursor.getInt(cursor.getColumnIndexOrThrow(VarietyTalentParametersSchema.COLUMN_INT_VALUE));
			}
			else if(!cursor.isNull(cursor.getColumnIndexOrThrow(VarietyTalentParametersSchema.COLUMN_ENUM_NAME))) {
				value = cursor.getString(cursor.getColumnIndexOrThrow(VarietyTalentParametersSchema.COLUMN_ENUM_NAME));
			}
			map.put(instance, value);
			cursor.moveToNext();
		}
		cursor.close();

		return map;
	}

	private boolean saveTalentParameterValues(SQLiteDatabase db, int varietyId, int talentInstanceId,
											  Map<Parameter, Object> parameterValuesMap) {
		boolean result = true;

		for(Map.Entry<Parameter, Object> entry : parameterValuesMap.entrySet()) {
			result &= (db.insertWithOnConflict(VarietyTalentParametersSchema.TABLE_NAME, null,
											   getParameterValuesValues(varietyId, talentInstanceId, entry.getKey(),
																		entry.getValue()),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		return result;
	}

	private ContentValues getParameterValuesValues(int varietyId, int talentInstanceId, Parameter parameter, Object value) {
		ContentValues values = new ContentValues(5);
		values.put(VarietyTalentParametersSchema.COLUMN_VARIETY_ID, varietyId);
		values.put(VarietyTalentParametersSchema.COLUMN_TALENT_INSTANCE_ID, talentInstanceId);
		values.put(VarietyTalentParametersSchema.COLUMN_PARAMETER_NAME, parameter.name());
		if(value == null) {
			values.putNull(VarietyTalentParametersSchema.COLUMN_INT_VALUE);
			values.putNull(VarietyTalentParametersSchema.COLUMN_ENUM_NAME);
		}
		else if(value instanceof  Integer){
			values.put(VarietyTalentParametersSchema.COLUMN_INT_VALUE, (Integer)value);
			values.putNull(VarietyTalentParametersSchema.COLUMN_ENUM_NAME);
		}
		else {
			values.putNull(VarietyTalentParametersSchema.COLUMN_INT_VALUE);
			values.put(VarietyTalentParametersSchema.COLUMN_ENUM_NAME, (String)value);
		}

		return values;
	}
}
