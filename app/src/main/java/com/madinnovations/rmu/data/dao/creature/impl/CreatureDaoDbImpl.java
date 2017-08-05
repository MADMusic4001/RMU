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
package com.madinnovations.rmu.data.dao.creature.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.campaign.CampaignDao;
import com.madinnovations.rmu.data.dao.combat.AttackDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.creature.CreatureArchetypeDao;
import com.madinnovations.rmu.data.dao.creature.CreatureDao;
import com.madinnovations.rmu.data.dao.creature.CreatureVarietyDao;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureAttackBonusSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSkillBonusSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSpecializationBonusSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSpellListBonusSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTalentParametersSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTalentsSchema;
import com.madinnovations.rmu.data.dao.spells.SpellListDao;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.SkillBonus;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.spells.SpellList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Creature} objects in a SQLite database.
 */
@Singleton
public class CreatureDaoDbImpl extends BaseDaoDbImpl<Creature> implements CreatureDao, CreatureSchema {
	private AttackDao            attackDao;
	private CampaignDao          campaignDao;
	private CreatureVarietyDao   creatureVarietyDao;
	private CreatureArchetypeDao creatureArchetypeDao;
	private SkillDao             skillDao;
	private SpecializationDao    specializationDao;
	private SpellListDao         spellListDao;
	private TalentDao            talentDao;

	/**
	 * Creates a new instance of CreatureDaoDbImpl
	 *
	 * @param helper  an SQLiteOpenHelper instance
	 * @param attackDao  a {@link AttackDao} instance
	 * @param campaignDao  a {@link CampaignDao} instance
	 * @param creatureVarietyDao  a {@link CreatureVarietyDao} instance
	 * @param creatureArchetypeDao  a {@link CreatureArchetypeDao} instance
	 * @param skillDao  a {@link SkillDao} instance
	 * @param specializationDao  a {@link SpecializationDao} instance
	 * @param spellListDao  a {@link SpellListDao} instance
	 * @param talentDao  a {@link TalentDao} instance
	 */
	@Inject
	public CreatureDaoDbImpl(SQLiteOpenHelper helper, AttackDao attackDao, CampaignDao campaignDao,
							 CreatureVarietyDao creatureVarietyDao, CreatureArchetypeDao creatureArchetypeDao, SkillDao skillDao,
							 SpecializationDao specializationDao, SpellListDao spellListDao, TalentDao talentDao) {
		super(helper);
		this.attackDao = attackDao;
		this.campaignDao = campaignDao;
		this.creatureVarietyDao = creatureVarietyDao;
		this.creatureArchetypeDao = creatureArchetypeDao;
		this.skillDao = skillDao;
		this.specializationDao = specializationDao;
		this.spellListDao = spellListDao;
		this.talentDao = talentDao;
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
	public List<Creature> getAllForCampaign(Campaign campaign) {
		final String selectionArgs[] = { String.valueOf(campaign.getId()) };
		final String selection = COLUMN_CAMPAIGN_ID + " = ?";
		List<Creature> list = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection, selectionArgs, COLUMN_LEVEL);

			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Creature instance = cursorToEntity(cursor);
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
	protected boolean saveRelationships(SQLiteDatabase db, Creature instance) {
		boolean result = true;
		String selectionArgs[] = { String.valueOf(instance.getId()) };
		String selection = CreatureSkillBonusSchema.COLUMN_CREATURE_ID + " = ?";

		db.delete(CreatureSkillBonusSchema.TABLE_NAME, selection, selectionArgs);

		for(SkillBonus skillBonus : instance.getPrimarySkillBonusesList()) {
			result &= (db.insertWithOnConflict(CreatureSkillBonusSchema.TABLE_NAME, null,
											   getSkillBonusValues(instance.getId(), skillBonus, true),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}
		for(SkillBonus skillBonus : instance.getSecondarySkillBonusesList()) {
			result &= (db.insertWithOnConflict(CreatureSkillBonusSchema.TABLE_NAME, null,
					getSkillBonusValues(instance.getId(), skillBonus, false),
					SQLiteDatabase.CONFLICT_NONE) != -1);
		}

		for(Map.Entry<Specialization, Short> entry : instance.getSpecializationBonuses().entrySet()) {
				result &= (db.insertWithOnConflict(CreatureSpecializationBonusSchema.TABLE_NAME, null,
												   getSpecializationRanksValues(instance.getId(), entry.getKey().getId(),
																				entry.getValue()),
												   SQLiteDatabase.CONFLICT_NONE) != -1);
		}

		for(Map.Entry<SpellList, Short> entry : instance.getSpellListBonuses().entrySet()) {
			result &= (db.insertWithOnConflict(CreatureSpellListBonusSchema.TABLE_NAME, null,
											   getSpellListRanksValues(instance.getId(), entry.getKey().getId(),
																	   entry.getValue()),
											   SQLiteDatabase.CONFLICT_NONE) != -1);
		}

		for(TalentInstance talentInstance : instance.getTalentInstances()) {
			int id = (int)db.insertWithOnConflict(CreatureTalentsSchema.TABLE_NAME, null,
											   getTalentsValues(instance.getId(), talentInstance.getTalent().getId(),
																talentInstance.getTiers()),
											   SQLiteDatabase.CONFLICT_NONE);
			if(id != -1) {
				talentInstance.setId(id);
				for(Map.Entry<Parameter, Object> entry : talentInstance.getParameterValues().entrySet()) {
					result &= (db.insertWithOnConflict(CreatureTalentParametersSchema.TABLE_NAME, null,
													   getTalentParameterValues(instance.getId(), talentInstance.getId(),
																				entry.getKey(), entry.getValue()),
													   SQLiteDatabase.CONFLICT_NONE) != -1);
				}
			}
		}

		return result;
	}

	@Override
	protected boolean deleteRelationships(SQLiteDatabase db, int id) {
		boolean result;
		final String selectionArgs[] = { String.valueOf(id) };

		String selection = CreatureSkillBonusSchema.COLUMN_CREATURE_ID + " = ?";
		result = (db.delete(CreatureSkillBonusSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = CreatureSpecializationBonusSchema.COLUMN_CREATURE_ID + " = ?";
		result &= (db.delete(CreatureSpecializationBonusSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = CreatureSpellListBonusSchema.COLUMN_CREATURE_ID + " = ?";
		result &= (db.delete(CreatureSpellListBonusSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = CreatureTalentParametersSchema.COLUMN_CREATURE_ID + " = ?";
		result &= (db.delete(CreatureTalentParametersSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		selection = CreatureTalentsSchema.COLUMN_CREATURE_ID + " = ?";
		result &= (db.delete(CreatureTalentsSchema.TABLE_NAME, selection, selectionArgs) >= 0);

		return result;
	}

	@Override
	protected Creature cursorToEntity(@NonNull Cursor cursor) {
		Creature instance = new Creature();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setCampaign(campaignDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CAMPAIGN_ID))));
		instance.setCreatureVariety(creatureVarietyDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CREATURE_VARIETY_ID))));
		instance.setArchetype(creatureArchetypeDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(
				COLUMN_CREATURE_ARCHETYPE_ID))));
		instance.setCurrentHits(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_HITS)));
		instance.setMaxHits(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MAX_HITS)));
		instance.setCurrentLevel(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)));
		instance.setCurrentDevelopmentPoints(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_DPS)));
		instance.setBaseMovementRate(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BASE_MOVEMENT_RATE)));
		instance.setNumCreatures(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_NUM_CREATURES)));
		setSkillBonuses(instance);
		setSpecializationBonuses(instance);
		setSpellListBonuses(instance);
		setTalents(instance);
		setAttackBonuses(instance);

		return instance;
	}

	@Override
	protected ContentValues getContentValues(Creature instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(10);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(9);
		}
		values.put(COLUMN_CAMPAIGN_ID, instance.getCampaign().getId());
		values.put(COLUMN_CREATURE_VARIETY_ID, instance.getCreatureVariety().getId());
		values.put(COLUMN_CREATURE_ARCHETYPE_ID, instance.getArchetype().getId());
		values.put(COLUMN_CURRENT_HITS, instance.getCurrentHits());
		values.put(COLUMN_MAX_HITS, instance.getMaxHits());
		values.put(COLUMN_LEVEL, instance.getCurrentLevel());
		values.put(COLUMN_CURRENT_DPS, instance.getCurrentDevelopmentPoints());
		values.put(COLUMN_BASE_MOVEMENT_RATE, instance.getBaseMovementRate());
		values.put(COLUMN_NUM_CREATURES, instance.getNumCreatures());

		return values;
	}

	private void setSkillBonuses(Creature creature) {
		final String selectionArgs[] = { String.valueOf(creature.getId()) };
		final String selection = CreatureSkillBonusSchema.COLUMN_CREATURE_ID + " = ?";

		Cursor cursor = super.query(CreatureSkillBonusSchema.TABLE_NAME,
									CreatureSkillBonusSchema.COLUMNS, selection, selectionArgs,
									CreatureSkillBonusSchema.COLUMN_BONUS);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			SkillBonus skillBonus = new SkillBonus();
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CreatureSkillBonusSchema.COLUMN_SKILL_ID));
			skillBonus.setSkill(skillDao.getById(mappedId));
			skillBonus.setBonus(cursor.getShort(cursor.getColumnIndexOrThrow(CreatureSkillBonusSchema.COLUMN_BONUS)));
			boolean primary = cursor.getInt(cursor.getColumnIndexOrThrow(CreatureSkillBonusSchema.COLUMN_IS_PRIMARY)) != 0;
			if(primary) {
				creature.getPrimarySkillBonusesList().add(skillBonus);
			}
			else {
				creature.getSecondarySkillBonusesList().add(skillBonus);
			}
			cursor.moveToNext();
		}
		cursor.close();
	}

	private void setSpecializationBonuses(Creature creature) {
		final String selectionArgs[] = { String.valueOf(creature.getId()) };
		final String selection = CreatureSpecializationBonusSchema.COLUMN_CREATURE_ID + " = ?";

		Cursor cursor = super.query(CreatureSpecializationBonusSchema.TABLE_NAME,
									CreatureSpecializationBonusSchema.COLUMNS, selection, selectionArgs,
									CreatureSpecializationBonusSchema.COLUMN_BONUS);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			short bonus = cursor.getShort(cursor.getColumnIndexOrThrow(CreatureSpecializationBonusSchema.COLUMN_BONUS));
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(
					CreatureSpecializationBonusSchema.COLUMN_SPECIALIZATION_ID));
			Specialization instance = specializationDao.getById(mappedId);
			if(instance != null) {
				creature.getSpecializationBonuses().put(instance, bonus);
			}
			cursor.moveToNext();
		}
		cursor.close();
	}

	private void setSpellListBonuses(Creature creature) {
		final String selectionArgs[] = { String.valueOf(creature.getId()) };
		final String selection = CreatureSpellListBonusSchema.COLUMN_CREATURE_ID + " = ?";

		Cursor cursor = super.query(CreatureSpellListBonusSchema.TABLE_NAME,
									CreatureSpellListBonusSchema.COLUMNS, selection, selectionArgs,
									CreatureSpellListBonusSchema.COLUMN_BONUS);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			short bonus = cursor.getShort(cursor.getColumnIndexOrThrow(CreatureSpellListBonusSchema.COLUMN_BONUS));
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CreatureSpellListBonusSchema.COLUMN_SPELL_LIST_ID));
			SpellList instance = spellListDao.getById(mappedId);
			if(instance != null) {
				creature.getSpellListBonuses().put(instance, bonus);
			}
			cursor.moveToNext();
		}
		cursor.close();
	}

	private void setTalents(Creature creature) {
		final String selectionArgs[] = { String.valueOf(creature) };
		final String selection = CreatureSpellListBonusSchema.COLUMN_CREATURE_ID + " = ?";

		Cursor cursor = super.query(CreatureTalentsSchema.TABLE_NAME,
									CreatureTalentsSchema.COLUMNS, selection, selectionArgs,
									CreatureTalentsSchema.COLUMN_ID);

		Cursor parametersCursor = super.query(CreatureTalentParametersSchema.TABLE_NAME,
											  CreatureTalentParametersSchema.COLUMNS, selection, selectionArgs,
											  CreatureTalentParametersSchema.COLUMN_TALENT_INSTANCE_ID);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			short tiers = cursor.getShort(cursor.getColumnIndexOrThrow(CreatureTalentsSchema.COLUMN_TIERS));
			int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(
					CreatureTalentsSchema.COLUMN_TALENT_ID));
			Talent instance = talentDao.getById(mappedId);
			if(instance != null) {
				TalentInstance talentInstance = new TalentInstance();
				talentInstance.setTalent(instance);
				talentInstance.setTiers(tiers);
				parametersCursor.moveToFirst();
				while(!parametersCursor.isAfterLast()) {
					int talentId = parametersCursor.getInt(parametersCursor.getColumnIndexOrThrow(
							CreatureTalentParametersSchema.COLUMN_TALENT_INSTANCE_ID));
					if(talentId == instance.getId()) {
						Parameter parameter = Parameter.valueOf(parametersCursor.getString(
								parametersCursor.getColumnIndexOrThrow(CreatureTalentParametersSchema.COLUMN_PARAMETER_NAME)));
						Object value = null;
						if(!parametersCursor.isNull(parametersCursor.getColumnIndexOrThrow(
								CreatureTalentParametersSchema.COLUMN_ENUM_NAME))) {
							value = parametersCursor.getString(parametersCursor.getColumnIndexOrThrow(
									CreatureTalentParametersSchema.COLUMN_ENUM_NAME));
						}
						else if(!parametersCursor.isNull(parametersCursor.getColumnIndexOrThrow(
								CreatureTalentParametersSchema.COLUMN_INT_VALUE))) {
							value = parametersCursor.getInt(parametersCursor.getColumnIndexOrThrow(
									CreatureTalentParametersSchema.COLUMN_INT_VALUE));
						}
						talentInstance.getParameterValues().put(parameter, value);
					}
					parametersCursor.moveToNext();
				}
				creature.getTalentInstancesList().add(talentInstance);
			}
			cursor.moveToNext();
		}
		cursor.close();
	}

	private void setAttackBonuses(Creature creature) {
		final String selectionArgs[] = { String.valueOf(creature.getId()) };
		final String selection = CreatureAttackBonusSchema.COLUMN_CREATURE_ID + " = ?";

		Cursor cursor = super.query(CreatureAttackBonusSchema.TABLE_NAME,
				CreatureAttackBonusSchema.COLUMNS, selection, selectionArgs,
				CreatureAttackBonusSchema.COLUMN_BONUS);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int    mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CreatureAttackBonusSchema.COLUMN_ATTACK_ID));
			Attack attack = attackDao.getById(mappedId);
			short bonus = cursor.getShort(cursor.getColumnIndexOrThrow(CreatureAttackBonusSchema.COLUMN_BONUS));
			boolean primary = cursor.getInt(cursor.getColumnIndexOrThrow(CreatureAttackBonusSchema.COLUMN_IS_PRIMARY)) != 0;
			if(primary) {
				creature.getPrimaryAttackBonusesMap().put(attack, bonus);
			}
			else {
				creature.getSecondaryAttackBonusesMap().put(attack, bonus);
			}
			cursor.moveToNext();
		}
		cursor.close();
	}

	private ContentValues getSkillBonusValues(int creatureId, SkillBonus  skillBonus, boolean primary) {
		ContentValues values = new ContentValues(4);

		values.put(CreatureSkillBonusSchema.COLUMN_CREATURE_ID, creatureId);
		values.put(CreatureSkillBonusSchema.COLUMN_SKILL_ID, skillBonus.getSkill().getId());
		values.put(CreatureSkillBonusSchema.COLUMN_BONUS, skillBonus.getBonus());
		values.put(CreatureSkillBonusSchema.COLUMN_IS_PRIMARY, primary);

		return values;
	}

	private ContentValues getSpecializationRanksValues(int creatureId, int specializationId, short ranks) {
		ContentValues values = new ContentValues(3);

		values.put(CreatureSpecializationBonusSchema.COLUMN_CREATURE_ID, creatureId);
		values.put(CreatureSpecializationBonusSchema.COLUMN_SPECIALIZATION_ID, specializationId);
		values.put(CreatureSpecializationBonusSchema.COLUMN_BONUS, ranks);

		return values;
	}

	private ContentValues getSpellListRanksValues(int creatureId, int spellListId, short ranks) {
		ContentValues values = new ContentValues(3);

		values.put(CreatureSpellListBonusSchema.COLUMN_CREATURE_ID, creatureId);
		values.put(CreatureSpellListBonusSchema.COLUMN_SPELL_LIST_ID, spellListId);
		values.put(CreatureSpellListBonusSchema.COLUMN_BONUS, ranks);

		return values;
	}

	private ContentValues getTalentsValues(int creatureId, int talentId, short tiers) {
		ContentValues values = new ContentValues(3);

		values.put(CreatureTalentsSchema.COLUMN_CREATURE_ID, creatureId);
		values.put(CreatureTalentsSchema.COLUMN_TALENT_ID, talentId);
		values.put(CreatureTalentsSchema.COLUMN_TIERS, tiers);

		return values;
	}

	private ContentValues getTalentParameterValues(int creatureId, int talentId, Parameter parameter, Object value) {
		ContentValues values = new ContentValues(5);

		values.put(CreatureTalentParametersSchema.COLUMN_CREATURE_ID, creatureId);
		values.put(CreatureTalentParametersSchema.COLUMN_TALENT_INSTANCE_ID, talentId);
		values.put(CreatureTalentParametersSchema.COLUMN_PARAMETER_NAME, parameter.name());
		if(value == null) {
			values.putNull(CreatureTalentParametersSchema.COLUMN_ENUM_NAME);
			values.putNull(CreatureTalentParametersSchema.COLUMN_INT_VALUE);
		}
		if(value instanceof Integer) {
			values.putNull(CreatureTalentParametersSchema.COLUMN_ENUM_NAME);
			values.put(CreatureTalentParametersSchema.COLUMN_INT_VALUE, (Integer)value);
		}
		else {
			values.put(CreatureTalentParametersSchema.COLUMN_ENUM_NAME, (String)value);
			values.putNull(CreatureTalentParametersSchema.COLUMN_INT_VALUE);
		}

		return values;
	}
}
