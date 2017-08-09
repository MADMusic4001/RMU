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
import com.madinnovations.rmu.data.dao.combat.AttackDao;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.common.BiomeDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.dao.item.ItemTemplateDao;
import com.madinnovations.rmu.data.dao.item.schemas.ArmorTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.HerbTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.ItemTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.NaturalsTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.PoisonTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.SubstanceTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponTemplateSchema;
import com.madinnovations.rmu.data.entities.common.ManeuverDifficulty;
import com.madinnovations.rmu.data.entities.item.ArmorTemplate;
import com.madinnovations.rmu.data.entities.item.Cost;
import com.madinnovations.rmu.data.entities.item.Form;
import com.madinnovations.rmu.data.entities.item.HerbTemplate;
import com.madinnovations.rmu.data.entities.item.ItemTemplate;
import com.madinnovations.rmu.data.entities.item.MoneyUnit;
import com.madinnovations.rmu.data.entities.item.NaturalsTemplate;
import com.madinnovations.rmu.data.entities.item.PoisonTemplate;
import com.madinnovations.rmu.data.entities.item.Prep;
import com.madinnovations.rmu.data.entities.item.Slot;
import com.madinnovations.rmu.data.entities.item.SubstanceTemplate;
import com.madinnovations.rmu.data.entities.item.SubstanceType;
import com.madinnovations.rmu.data.entities.item.WeaponTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link ItemTemplate} objects in a SQLite database.
 */
@SuppressWarnings("unused")
@Singleton
public class ItemTemplateDaoDbImpl extends BaseDaoDbImpl<ItemTemplate> implements ItemTemplateDao, ItemTemplateSchema {
	private static final String TAG = "ItemTemplateDaoDbImpl";
	private AttackDao         attackDao;
	private BiomeDao          biomeDao;
	private DamageTableDao    damageTableDao;
	private SpecializationDao specializationDao;

    /**
     * Creates a new instance of ItemTemplateDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public ItemTemplateDaoDbImpl(SQLiteOpenHelper helper, AttackDao attackDao, BiomeDao biomeDao,DamageTableDao damageTableDao,
								 SpecializationDao specializationDao) {
        super(helper);
		this.attackDao = attackDao;
		this.biomeDao = biomeDao;
		this.specializationDao = specializationDao;
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

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				ItemTemplate instance = cursorToEntity(cursor);
				list.add(instance);
				cursor.moveToNext();
			}
			cursor.close();
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
	public boolean save(ItemTemplate instance, boolean isNew) {
		final String selectionArgs[] = { String.valueOf(instance.getId()) };
		final String selection = COLUMN_ID + " = ?";
		final String childTemplateSelection = WeaponTemplateSchema.COLUMN_ITEM_TEMPLATE_ID + " = ?";
		final String naturalsTemplateSelection = HerbTemplateSchema.COLUMN_NATURALS_TEMPLATE_ID + " = ?";
		ContentValues contentValues = getContentValues(instance);
		boolean result;

		SQLiteDatabase db = helper.getWritableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			if(getId(instance) == -1 || isNew) {
				setId(instance, (int)db.insertWithOnConflict(getTableName(), null, contentValues, SQLiteDatabase.CONFLICT_NONE));
				result = (getId(instance) != -1);
				if(instance instanceof ArmorTemplate) {
					ContentValues childContentValues = getArmorContentValues((ArmorTemplate)instance);
					db.insertWithOnConflict(ArmorTemplateSchema.TABLE_NAME, null, childContentValues,
											SQLiteDatabase.CONFLICT_NONE);
				}
				if(instance instanceof NaturalsTemplate) {
					ContentValues childContentValues = getNaturalsContentValues((NaturalsTemplate)instance);
					db.insertWithOnConflict(NaturalsTemplateSchema.TABLE_NAME, null, childContentValues,
											SQLiteDatabase.CONFLICT_NONE);
					if(instance instanceof HerbTemplate) {
						childContentValues = getHerbContentValues((HerbTemplate)instance);
						db.insertWithOnConflict(HerbTemplateSchema.TABLE_NAME, null, childContentValues,
												SQLiteDatabase.CONFLICT_NONE);
					}
					if(instance instanceof PoisonTemplate) {
						childContentValues = getPoisonContentValues((PoisonTemplate)instance);
						db.insertWithOnConflict(PoisonTemplateSchema.TABLE_NAME, null, childContentValues,
												SQLiteDatabase.CONFLICT_NONE);
					}
				}
				if(instance instanceof  SubstanceTemplate) {
					ContentValues childContentValues = getSubstancesContentValues((SubstanceTemplate)instance);
					db.insertWithOnConflict(SubstanceTemplateSchema.TABLE_NAME, null, childContentValues,
											SQLiteDatabase.CONFLICT_NONE);
				}
				if(instance instanceof WeaponTemplate) {
					ContentValues weaponContentValues = getWeaponContentValues((WeaponTemplate)instance);
					db.insertWithOnConflict(WeaponTemplateSchema.TABLE_NAME, null, weaponContentValues,
											SQLiteDatabase.CONFLICT_NONE);
				}
			}
			else {
				int count = db.update(getTableName(), contentValues, selection, selectionArgs);
				result = (count == 1);
				if(instance instanceof ArmorTemplate) {
					ContentValues childContentValues = getArmorContentValues((ArmorTemplate) instance);
					if(db.update(ArmorTemplateSchema.TABLE_NAME, childContentValues, childTemplateSelection, selectionArgs)
							!= 1) {
						childContentValues = getArmorContentValues((ArmorTemplate) instance);
						db.insertWithOnConflict(ArmorTemplateSchema.TABLE_NAME, null, childContentValues,
												SQLiteDatabase.CONFLICT_NONE);
					}
				}
				if(instance instanceof NaturalsTemplate) {
					ContentValues childContentValues = getNaturalsContentValues((NaturalsTemplate) instance);
					if(db.update(NaturalsTemplateSchema.TABLE_NAME, childContentValues, childTemplateSelection, selectionArgs)
							!= 1) {
						childContentValues = getNaturalsContentValues((NaturalsTemplate) instance);
						db.insertWithOnConflict(NaturalsTemplateSchema.TABLE_NAME, null, childContentValues,
												SQLiteDatabase.CONFLICT_NONE);
					}
					if(instance instanceof HerbTemplate) {
						childContentValues = getHerbContentValues((HerbTemplate) instance);
						if(db.update(HerbTemplateSchema.TABLE_NAME, childContentValues, naturalsTemplateSelection, selectionArgs)
								!= 1) {
							childContentValues = getHerbContentValues((HerbTemplate) instance);
							db.insertWithOnConflict(HerbTemplateSchema.TABLE_NAME, null, childContentValues,
													SQLiteDatabase.CONFLICT_NONE);
						}
					}
					if(instance instanceof PoisonTemplate) {
						childContentValues = getPoisonContentValues((PoisonTemplate) instance);
						if(db.update(PoisonTemplateSchema.TABLE_NAME, childContentValues, naturalsTemplateSelection,
									 selectionArgs) != 1) {
							childContentValues = getPoisonContentValues((PoisonTemplate) instance);
							db.insertWithOnConflict(PoisonTemplateSchema.TABLE_NAME, null, childContentValues,
													SQLiteDatabase.CONFLICT_NONE);
						}
					}
				}
				if(instance instanceof SubstanceTemplate) {
					ContentValues childContentValues = getSubstancesContentValues((SubstanceTemplate) instance);
					if(db.update(SubstanceTemplateSchema.TABLE_NAME, childContentValues, childTemplateSelection, selectionArgs)
							!= 1) {
						childContentValues = getSubstancesContentValues((SubstanceTemplate) instance);
						db.insertWithOnConflict(SubstanceTemplateSchema.TABLE_NAME, null, childContentValues,
												SQLiteDatabase.CONFLICT_NONE);
					}
				}
				if(instance instanceof WeaponTemplate) {
					ContentValues weaponContentValues = getWeaponContentValues((WeaponTemplate)instance);
					if(db.update(WeaponTemplateSchema.TABLE_NAME, weaponContentValues, childTemplateSelection, selectionArgs)
							!= 1) {
						weaponContentValues = getWeaponContentValues((WeaponTemplate)instance);
						db.insertWithOnConflict(WeaponTemplateSchema.TABLE_NAME, null, weaponContentValues,
												SQLiteDatabase.CONFLICT_NONE);
					}
				}
			}
			if(result && newTransaction) {
				db.setTransactionSuccessful();
			}
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}
		return true;
	}

	@Override
	public Collection<ItemTemplate> getAllForSlot(@NonNull Slot slot) {
		final String selectionArgs[] = { slot.name(), slot.name(), Slot.ANY.name() };
		Collection<ItemTemplate> itemTemplates = null;

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = db.rawQuery(QUERY_FOR_SLOT, selectionArgs);
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
	public Collection<ItemTemplate> getAllWithoutSubclass() {
		Collection<ItemTemplate> itemTemplates = null;

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = db.rawQuery(QUERY_NO_SUBCLASS, null);
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

	/*
	 * This cursorToEntity implementation implements polymorphism for the ItemTemplate hierarchy. In order for this to work all
	 * child tables id values must be the same as their parent item template records and the queries must left outer join each to
	 * the child tables. For example {@link ItemTemplateSchema#SELECT_FROM}
	 */
	@Override
    protected ItemTemplate cursorToEntity(@NonNull Cursor cursor) {
		ItemTemplate instance = null;

		if(!cursor.isNull(cursor.getColumnIndexOrThrow(ArmorTemplateSchema.ARMOR_ID))) {
			ArmorTemplate armorTemplate = new ArmorTemplate();
			armorTemplate.setSmallCost(cursor.getFloat(cursor.getColumnIndexOrThrow(ArmorTemplateSchema.COLUMN_SMALL_COST)));
			armorTemplate.setMediumCost(cursor.getFloat(cursor.getColumnIndexOrThrow(ArmorTemplateSchema.COLUMN_MEDIUM_COST)));
			armorTemplate.setBigCost(cursor.getFloat(cursor.getColumnIndexOrThrow(ArmorTemplateSchema.COLUMN_BIG_COST)));
			armorTemplate.setLargeCost(cursor.getFloat(cursor.getColumnIndexOrThrow(ArmorTemplateSchema.COLUMN_LARGE_COST)));
			armorTemplate.setWeightPercent(cursor.getFloat(cursor.getColumnIndexOrThrow(
					ArmorTemplateSchema.COLUMN_WEIGHT_PERCENT)));
			armorTemplate.setArmorType(cursor.getShort(cursor.getColumnIndexOrThrow(ArmorTemplateSchema.COLUMN_ARMOR_TYPE)));
			instance = armorTemplate;
		}
		else if(!cursor.isNull(cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.NATURAL_ID))) {
			if(!cursor.isNull(cursor.getColumnIndexOrThrow(HerbTemplateSchema.HERB_ID))) {
				instance = new HerbTemplate();
			}
			else if(!cursor.isNull(cursor.getColumnIndexOrThrow(PoisonTemplateSchema.POISON_ID))) {
				instance = new PoisonTemplate();
			}
			if(instance != null) {
				NaturalsTemplate naturalsTemplate = (NaturalsTemplate)instance;
				naturalsTemplate.setBiome(biomeDao.getById(cursor.getInt(
						cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.COLUMN_BIOME_ID))));
				naturalsTemplate.setForm(Form.valueOf(cursor.getString(
						cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.COLUMN_FORM_NAME))));
				naturalsTemplate.setPrep(Prep.valueOf(cursor.getString(
						cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.COLUMN_PREP_NAME))));
				naturalsTemplate.setSeason(cursor.getString(
						cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.COLUMN_SEASON)));
				naturalsTemplate.setEffects(cursor.getString(
						cursor.getColumnIndexOrThrow(NaturalsTemplateSchema.COLUMN_EFFECTS)));
			}
			else {
				instance = new ItemTemplate();
			}
		}
		else if(!cursor.isNull(cursor.getColumnIndexOrThrow(SubstanceTemplateSchema.SUBSTANCE_ID))) {
			SubstanceTemplate substanceTemplate = new SubstanceTemplate();
			substanceTemplate.setSubstanceType(SubstanceType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(
					SubstanceTemplateSchema.COLUMN_SUBSTANCE_TYPE_NAME))));
			substanceTemplate.setHardness(cursor.getFloat(cursor.getColumnIndexOrThrow(SubstanceTemplateSchema.COLUMN_HARDNESS)));
			substanceTemplate.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(
					SubstanceTemplateSchema.COLUMN_DESCRIPTION)));
			instance = substanceTemplate;
		}
		else if(!cursor.isNull(cursor.getColumnIndexOrThrow(WeaponTemplateSchema.WEAPON_ID))) {
			WeaponTemplate weaponTemplate = new WeaponTemplate();
			weaponTemplate.setBraceable(cursor.getInt(cursor.getColumnIndexOrThrow(WeaponTemplateSchema.COLUMN_BRACEABLE)) != 0);
			weaponTemplate.setFumble(cursor.getShort(cursor.getColumnIndexOrThrow(WeaponTemplateSchema.COLUMN_FUMBLE)));
			weaponTemplate.setLength(cursor.getFloat(cursor.getColumnIndexOrThrow(WeaponTemplateSchema.COLUMN_LENGTH)));
			weaponTemplate.setSizeAdjustment(cursor.getShort(cursor.getColumnIndexOrThrow(
					WeaponTemplateSchema.COLUMN_SIZE_ADJUSTMENT)));
			weaponTemplate.setCombatSpecialization(specializationDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(
					WeaponTemplateSchema.COLUMN_SPECIALIZATION_ID))));
			weaponTemplate.setDamageTable(damageTableDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(
					WeaponTemplateSchema.COLUMN_DAMAGE_TABLE_ID))));
			weaponTemplate.setAttack(attackDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(
					WeaponTemplateSchema.COLUMN_ATTACK_ID))));
			instance = weaponTemplate;
		}
		else {
			instance = new ItemTemplate();
		}
		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
		instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
		instance.setWeight(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)));
		Cost baseCost = new Cost();
		baseCost.setValue(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BASE_COST_VALUE)));
		baseCost.setUnit(MoneyUnit.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BASE_COST_UNIT))));
		instance.setBaseCost(baseCost);
		instance.setStrength(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_STRENGTH)));
		instance.setConstructionTime(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONSTRUCTION_TIME)));
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
			values = new ContentValues(11);
			values.put(COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(10);
		}
		values.put(COLUMN_NAME, instance.getName());
		values.put(COLUMN_WEIGHT, instance.getWeight());
		values.put(COLUMN_BASE_COST_VALUE, instance.getBaseCost().getValue());
		values.put(COLUMN_BASE_COST_UNIT, instance.getBaseCost().getUnit().name());
		values.put(COLUMN_STRENGTH, instance.getStrength());
		values.put(COLUMN_CONSTRUCTION_TIME, instance.getConstructionTime());
		if(instance.getManeuverDifficulty() == null || ManeuverDifficulty.MEDIUM.equals(instance.getManeuverDifficulty())) {
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

	private ContentValues getArmorContentValues(ArmorTemplate instance) {
		ContentValues values;

		values = new ContentValues(7);
		values.put(ArmorTemplateSchema.COLUMN_ITEM_TEMPLATE_ID, instance.getId());
		values.put(ArmorTemplateSchema.COLUMN_SMALL_COST, instance.getSmallCost());
		values.put(ArmorTemplateSchema.COLUMN_MEDIUM_COST, instance.getMediumCost());
		values.put(ArmorTemplateSchema.COLUMN_BIG_COST, instance.getBigCost());
		values.put(ArmorTemplateSchema.COLUMN_LARGE_COST, instance.getLargeCost());
		values.put(ArmorTemplateSchema.COLUMN_WEIGHT_PERCENT, instance.getWeightPercent());
		values.put(ArmorTemplateSchema.COLUMN_ARMOR_TYPE, instance.getArmorType());

		return values;
	}

	private ContentValues getHerbContentValues(HerbTemplate instance) {
		ContentValues values;

		values = new ContentValues(1);
		values.put(HerbTemplateSchema.COLUMN_NATURALS_TEMPLATE_ID, instance.getId());

		return values;
	}

	private ContentValues getNaturalsContentValues(NaturalsTemplate instance) {
		ContentValues values;

		values = new ContentValues(6);
		values.put(NaturalsTemplateSchema.COLUMN_ITEM_TEMPLATE_ID, instance.getId());
		values.put(NaturalsTemplateSchema.COLUMN_BIOME_ID, instance.getBiome().getId());
		values.put(NaturalsTemplateSchema.COLUMN_FORM_NAME, instance.getForm().name());
		values.put(NaturalsTemplateSchema.COLUMN_PREP_NAME, instance.getPrep().name());
		if(instance.getSeason() != null) {
			values.put(NaturalsTemplateSchema.COLUMN_SEASON, instance.getSeason());
		}
		else {
			values.putNull(NaturalsTemplateSchema.COLUMN_SEASON);
		}
		values.put(NaturalsTemplateSchema.COLUMN_EFFECTS, instance.getEffects());

		return values;
	}

	private ContentValues getPoisonContentValues(PoisonTemplate instance) {
		ContentValues values;

		values = new ContentValues(1);
		values.put(PoisonTemplateSchema.COLUMN_NATURALS_TEMPLATE_ID, instance.getId());

		return values;
	}

	private ContentValues getSubstancesContentValues(SubstanceTemplate instance) {
		ContentValues values;

		values = new ContentValues(7);
		values.put(SubstanceTemplateSchema.COLUMN_ITEM_TEMPLATE_ID, instance.getId());
		values.put(SubstanceTemplateSchema.COLUMN_SUBSTANCE_TYPE_NAME, instance.getSubstanceType().name());
		values.put(SubstanceTemplateSchema.COLUMN_HARDNESS, instance.getHardness());
		values.put(SubstanceTemplateSchema.COLUMN_DESCRIPTION, instance.getDescription());

		return values;
	}

	private ContentValues getWeaponContentValues(WeaponTemplate instance) {
		ContentValues values = new ContentValues(8);

		values.put(WeaponTemplateSchema.COLUMN_ITEM_TEMPLATE_ID, instance.getId());
		values.put(WeaponTemplateSchema.COLUMN_SPECIALIZATION_ID, instance.getCombatSpecialization().getId());
		values.put(WeaponTemplateSchema.COLUMN_DAMAGE_TABLE_ID, instance.getDamageTable().getId());
		values.put(WeaponTemplateSchema.COLUMN_BRACEABLE, instance.isBraceable());
		values.put(WeaponTemplateSchema.COLUMN_FUMBLE, instance.getFumble());
		values.put(WeaponTemplateSchema.COLUMN_LENGTH, instance.getLength());
		if(instance.getSizeAdjustment() != null) {
			values.put(WeaponTemplateSchema.COLUMN_SIZE_ADJUSTMENT, instance.getSizeAdjustment());
		}
		else {
			values.putNull(WeaponTemplateSchema.COLUMN_SIZE_ADJUSTMENT);
		}
		values.put(WeaponTemplateSchema.COLUMN_ATTACK_ID, instance.getAttack().getId());

		return values;
	}
}
