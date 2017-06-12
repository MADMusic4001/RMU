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
package com.madinnovations.rmu.data.dao.item.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.common.BiomeDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.item.ItemTemplateDao;
import com.madinnovations.rmu.data.dao.item.schemas.ArmorTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.HerbTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.ItemTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.NaturalsTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.PoisonTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.SubstanceTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponTemplateSchema;
import com.madinnovations.rmu.data.entities.common.ManeuverDifficulty;
import com.madinnovations.rmu.data.entities.object.ArmorTemplate;
import com.madinnovations.rmu.data.entities.object.Form;
import com.madinnovations.rmu.data.entities.object.HerbTemplate;
import com.madinnovations.rmu.data.entities.object.ItemTemplate;
import com.madinnovations.rmu.data.entities.object.PoisonTemplate;
import com.madinnovations.rmu.data.entities.object.Prep;
import com.madinnovations.rmu.data.entities.object.Slot;
import com.madinnovations.rmu.data.entities.object.SubstanceTemplate;
import com.madinnovations.rmu.data.entities.object.SubstanceType;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link ItemTemplate} objects in a SQLite database.
 */
@Singleton
public class ItemTemplateDaoDbImpl extends BaseDaoDbImpl<ItemTemplate> implements ItemTemplateDao, ItemTemplateSchema {
	private BiomeDao biomeDao;
	private SkillDao skillDao;
	private DamageTableDao damageTableDao;

    /**
     * Creates a new instance of ItemTemplateDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public ItemTemplateDaoDbImpl(SQLiteOpenHelper helper, BiomeDao biomeDao, SkillDao skillDao, DamageTableDao damageTableDao) {
        super(helper);
		this.biomeDao = biomeDao;
		this.skillDao = skillDao;
		this.damageTableDao = damageTableDao;
    }

	@Override
	public ItemTemplate getById(int id) {
		final String selectionArgs[] = { String.valueOf(id) };
		ItemTemplate instance = null;

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = db.rawQuery(ItemTemplateSchema.QUERY_BY_ID, selectionArgs);
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
	public List<ItemTemplate> getAll() {
		List<ItemTemplate> list = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = db.rawQuery(QUERY_ALL, null);

			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					ItemTemplate instance = cursorToEntity(cursor);
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
	public boolean save(ItemTemplate instance) {
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
	protected int getId(ItemTemplate instance) {
		return instance.getId();
	}

	@Override
	protected void setId(ItemTemplate instance, int id) {
		instance.setId(id);
	}

	@Override
	public Collection<ItemTemplate> getAllForSlot(@NonNull Slot slot) {
		final String selectionArgs[] = { slot.name(), slot.name(), Slot.ANY.name() };
		final String selection = COLUMN_PRIMARY_SLOT + " = ? OR " + COLUMN_SECONDARY_SLOT + " = ? OR "
				+ COLUMN_PRIMARY_SLOT + " = ?";
		Collection<ItemTemplate> itemTemplates = null;

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection,
								  selectionArgs, getSortString());
			if (cursor != null) {
				itemTemplates = new ArrayList<>(cursor.getCount());
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					ItemTemplate instance = cursorToEntity(cursor);
					itemTemplates.add(instance);
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

		return itemTemplates;
	}

	@Override
    protected ItemTemplate cursorToEntity(@NonNull Cursor cursor) {
		ItemTemplate instance;

		if(!cursor.isNull(cursor.getColumnIndexOrThrow(ArmorTemplateSchema.ARMOR_ID))) {
			instance = new ArmorTemplate();
			((ArmorTemplate)instance).setSmallCost(cursor.getFloat(cursor.getColumnIndexOrThrow(ArmorTemplateSchema.COLUMN_SMALL_COST)));
			((ArmorTemplate)instance).setMediumCost(cursor.getFloat(cursor.getColumnIndexOrThrow(ArmorTemplateSchema.COLUMN_MEDIUM_COST)));
			((ArmorTemplate)instance).setBigCost(cursor.getFloat(cursor.getColumnIndexOrThrow(ArmorTemplateSchema.COLUMN_BIG_COST)));
			((ArmorTemplate)instance).setLargeCost(cursor.getFloat(cursor.getColumnIndexOrThrow(ArmorTemplateSchema.COLUMN_LARGE_COST)));
			((ArmorTemplate)instance).setWeightPercent(cursor.getFloat(cursor.getColumnIndexOrThrow(ArmorTemplateSchema.COLUMN_WEIGHT_PERCENT)));
			((ArmorTemplate)instance).setArmorType(cursor.getShort(cursor.getColumnIndexOrThrow(ArmorTemplateSchema.COLUMN_ARMOR_TYPE)));
		}
		else if(!cursor.isNull(cursor.getColumnIndexOrThrow(HerbTemplateSchema.HERB_ID))) {
			instance = new HerbTemplate();
			((HerbTemplate)instance).setBiome(biomeDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.COLUMN_BIOME_ID))));
			((HerbTemplate)instance).setForm(Form.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.COLUMN_FORM_NAME))));
			((HerbTemplate)instance).setPrep(Prep.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.COLUMN_PREP_NAME))));
			((HerbTemplate)instance).setSeason(cursor.getString(cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.COLUMN_SEASON)));
			((HerbTemplate)instance).setEffects(cursor.getString(cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.COLUMN_EFFECTS)));
		}
		else if(!cursor.isNull(cursor.getColumnIndexOrThrow(PoisonTemplateSchema.POISON_ID))) {
			instance = new PoisonTemplate();
			((PoisonTemplate)instance).setBiome(biomeDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.COLUMN_BIOME_ID))));
			((PoisonTemplate)instance).setForm(Form.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.COLUMN_FORM_NAME))));
			((PoisonTemplate)instance).setPrep(Prep.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.COLUMN_PREP_NAME))));
			((PoisonTemplate)instance).setSeason(cursor.getString(cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.COLUMN_SEASON)));
			((PoisonTemplate)instance).setEffects(cursor.getString(cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.COLUMN_EFFECTS)));
		}
		else if(!cursor.isNull(cursor.getColumnIndexOrThrow(SubstanceTemplateSchema.SUBSTANCE_ID))) {
			instance = new SubstanceTemplate();
			((SubstanceTemplate)instance).setSubstanceType(SubstanceType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SubstanceTemplateSchema.COLUMN_SUBSTANCE_TYPE_NAME))));
			((SubstanceTemplate)instance).setHardness(cursor.getFloat(cursor.getColumnIndexOrThrow(SubstanceTemplateSchema.COLUMN_HARDNESS)));
			((SubstanceTemplate)instance).setDescription(cursor.getString(cursor.getColumnIndexOrThrow(SubstanceTemplateSchema.COLUMN_DESCRIPTION)));
		}
		else if(!cursor.isNull(cursor.getColumnIndexOrThrow(WeaponTemplateSchema.WEAPON_ID))) {
			instance = new WeaponTemplate();
			((WeaponTemplate)instance).setBraceable(cursor.getInt(cursor.getColumnIndexOrThrow(WeaponTemplateSchema.COLUMN_BRACEABLE)) != 0);
			((WeaponTemplate)instance).setCombatSkill(skillDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(WeaponTemplateSchema.COLUMN_SKILL_ID))));
			((WeaponTemplate)instance).setDamageTable(damageTableDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(WeaponTemplateSchema.COLUMN_DAMAGE_TABLE_ID))));
		}
		else {
			instance = new ItemTemplate();
		}
		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setWeight(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)));
		instance.setBaseCost(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_BASE_COST)));
		instance.setStrength(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_STRENGTH)));
		instance.setConstructionTime(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_CONSTRUCTION_TIME)));
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_MANEUVER_DIFFICULTY))) {
			instance.setManeuverDifficulty(ManeuverDifficulty.valueOf(cursor.getString(
					cursor.getColumnIndexOrThrow(COLUMN_MANEUVER_DIFFICULTY))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_NOTES))) {
			instance.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_PRIMARY_SLOT))) {
			instance.setPrimarySlot(Slot.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRIMARY_SLOT))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_SECONDARY_SLOT))) {
			instance.setSecondarySlot(Slot.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SECONDARY_SLOT))));
		}

		return instance;
    }

    @Override
    protected ContentValues getContentValues(ItemTemplate instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(10);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(9);
		}
		values.put(COLUMN_NAME, instance.getName());
		values.put(COLUMN_WEIGHT, instance.getWeight());
		values.put(COLUMN_BASE_COST, instance.getBaseCost());
		values.put(COLUMN_STRENGTH, instance.getStrength());
		values.put(COLUMN_CONSTRUCTION_TIME, instance.getConstructionTime());
		if(instance.getManeuverDifficulty() == null) {
			values.putNull(COLUMN_MANEUVER_DIFFICULTY);
		}
		else {
			values.put(COLUMN_MANEUVER_DIFFICULTY, instance.getManeuverDifficulty().name());
		}
		if(instance.getNotes() == null) {
			values.putNull(COLUMN_NOTES);
		}
		else {
			values.put(COLUMN_NOTES, instance.getNotes());
		}
		if(instance.getPrimarySlot() == null) {
			values.putNull(COLUMN_PRIMARY_SLOT);
		}
		else {
			values.put(COLUMN_PRIMARY_SLOT, instance.getPrimarySlot().name());
		}
		if(instance.getSecondarySlot() == null) {
			values.putNull(COLUMN_SECONDARY_SLOT);
		}
		else {
			values.put(COLUMN_SECONDARY_SLOT, instance.getSecondarySlot().name());
		}

		return values;
    }
}
