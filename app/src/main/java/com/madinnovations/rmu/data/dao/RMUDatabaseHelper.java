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
package com.madinnovations.rmu.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.madinnovations.rmu.data.dao.campaign.schemas.CampaignAttackRestrictionsSchema;
import com.madinnovations.rmu.data.dao.campaign.schemas.CampaignSchema;
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
import com.madinnovations.rmu.data.dao.character.schemas.CultureSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSkillRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionAssignableSkillCostSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSkillCategoryCostSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSkillCostSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionalSkillCategoriesSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceCulturesSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceRealmRRModSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceStatModSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceTalentParametersSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceTalentsSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.AdditionalEffectSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.AttackSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalCodeSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalResultSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultRowSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageTableSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.DiseaseSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.FumbleSchema;
import com.madinnovations.rmu.data.dao.common.schemas.BiomeSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCategorySchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCategoryStatsSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillStatsSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SpecializationSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SpecializationStatsSchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentCategorySchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentParametersPerUnitSchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentParametersSchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.ArchetypeLevelsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.ArchetypeSkillsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.ArchetypeSpellsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureArchetypeSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureAttackBonusSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureCategorySchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureCategoryTalentsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSkillBonusSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSpecializationBonusSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSpellListBonusSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTalentParametersSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTalentsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTypeSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTypeTalentsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureVarietySchema;
import com.madinnovations.rmu.data.dao.creature.schemas.OutlookSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyAttacksSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyCriticalCodesSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietySkillsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyStatsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyTalentParametersSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyTalentTiersSchema;
import com.madinnovations.rmu.data.dao.item.schemas.ArmorTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.HerbTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.ItemSchema;
import com.madinnovations.rmu.data.dao.item.schemas.ItemTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.NaturalsTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.PoisonTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.SubstanceTemplateSchema;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponSchema;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponTemplateSchema;
import com.madinnovations.rmu.data.dao.play.schemas.EncounterSetupEncounterInfoSchema;
import com.madinnovations.rmu.data.dao.play.schemas.EncounterSetupSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellAreaOfEffectParamSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellDurationParamSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellListSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellSubTypeSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellTypeSchema;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.combat.BodyLocation;
import com.madinnovations.rmu.data.entities.combat.CriticalType;
import com.madinnovations.rmu.data.entities.item.Weapon;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods that create and upgrade the SQLite database used by RMU.
 */
@Singleton
public class RMUDatabaseHelper extends SQLiteOpenHelper {
	@SuppressWarnings("unused")
	private static final String TAG              = "RMUDatabaseHelper";
	private static final String DATABASE_NAME    = "rmu_db";
	public static final  int    DATABASE_VERSION = 25;

    /**
     * Creates a new RMUDatabaseHelper instance
     *
     * @param context  the Android context to be used by the helper
     */
    @Inject
    public RMUDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
		db.setForeignKeyConstraintsEnabled(false);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("RMUDatabaseHelper", "Creating database...");
        try {
            sqLiteDatabase.beginTransaction();
			sqLiteDatabase.execSQL(DiseaseSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(TalentCategorySchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(SpellTypeSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(SpellSubTypeSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(SkillCategorySchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(RaceSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(OutlookSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(ItemTemplateSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(ItemSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(DamageTableSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CultureSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CriticalCodeSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CreatureTypeSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CreatureCategorySchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(BiomeSchema.TABLE_CREATE);

			sqLiteDatabase.execSQL(EncounterSetupSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(EncounterSetupEncounterInfoSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CreatureVarietySchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(VarietyStatsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(VarietyCriticalCodesSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(SkillCategoryStatsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(SkillSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(WeaponSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(WeaponTemplateSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(SubstanceTemplateSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(PoisonTemplateSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(NaturalsTemplateSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(HerbTemplateSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(ArmorTemplateSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(VarietySkillsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(SkillStatsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CreatureArchetypeSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(ArchetypeLevelsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(ArchetypeSpellsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(ArchetypeSkillsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(TalentSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(TalentParametersPerUnitSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CreatureTypeTalentsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CreatureCategoryTalentsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(VarietyTalentTiersSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(VarietyTalentParametersSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(TalentParametersSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(TalentParametersPerUnitSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(SpecializationSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(SpecializationStatsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CampaignSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CampaignAttackRestrictionsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(DamageResultSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(DamageResultRowSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CriticalResultSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(FumbleSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(AdditionalEffectSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(AttackSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(VarietyAttacksSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(RaceTalentsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(RaceTalentParametersSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(RaceStatModSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(RaceRealmRRModSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(RaceCulturesSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(ProfessionSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(ProfessionSkillCostSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(ProfessionSkillCategoryCostSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(ProfessionAssignableSkillCostSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(ProfessionalSkillCategoriesSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(SpellListSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(SpellSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(SpellDurationParamSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(SpellAreaOfEffectParamSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CultureSkillRanksSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CreatureSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CreatureAttackBonusSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CreatureSkillBonusSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CreatureSpecializationBonusSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CreatureSpellListBonusSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CreatureTalentsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CreatureTalentParametersSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CharacterSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CharacterTalentsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CharacterTalentParametersSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CharacterStatsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CharacterCurrentLevelSkillRanksSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CharacterCurrentLevelSpecializationRanksSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CharacterCurrentLevelSpellListRanksSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CharacterSkillRanksSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CharacterSpecializationRanksSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CharacterSpellListRanksSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CharacterSkillCostsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CharacterItemsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CharacterPurchasedCultureRanksSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CharacterProfessionSkillsSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CharacterKnacksSchema.TABLE_CREATE);
            sqLiteDatabase.setTransactionSuccessful();
        }
        finally {
            sqLiteDatabase.endTransaction();
        }
        Log.i("RMUDatabaseHelper", "Database creation completed. Database located at " + sqLiteDatabase.getPath());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i("RMUDatabaseHelper", "Upgrading database from version " + oldVersion + " to version " + newVersion);
        try {
            sqLiteDatabase.beginTransaction();
            switch (oldVersion) {
				case 24:
					upgrade(sqLiteDatabase);
					break;
            }
            sqLiteDatabase.setTransactionSuccessful();
        }
        catch (Exception ignored) {}
        finally {
            sqLiteDatabase.endTransaction();
        }
    }

	/**
	 * Deletes all the data in the database. The caller is responsible for managing the transaction if desired.
	 */
    public void clearDatabase() {
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		sqLiteDatabase.delete(CharacterKnacksSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CharacterProfessionSkillsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CharacterPurchasedCultureRanksSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CharacterItemsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CharacterSkillCostsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CharacterSkillRanksSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CharacterSpecializationRanksSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CharacterSpellListRanksSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CharacterCurrentLevelSkillRanksSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CharacterCurrentLevelSpecializationRanksSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CharacterCurrentLevelSpellListRanksSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CharacterStatsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CharacterTalentParametersSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CharacterTalentsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CharacterSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CreatureTalentParametersSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CreatureTalentsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CreatureSpellListBonusSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CreatureSpecializationBonusSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CreatureSkillBonusSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CreatureAttackBonusSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CreatureSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CultureSkillRanksSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(SpellAreaOfEffectParamSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(SpellDurationParamSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(SpellSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(SpellListSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(ProfessionalSkillCategoriesSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(ProfessionAssignableSkillCostSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(ProfessionSkillCategoryCostSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(ProfessionSkillCostSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(ProfessionSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(RaceCulturesSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(RaceRealmRRModSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(RaceStatModSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(RaceTalentParametersSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(RaceTalentsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(VarietyAttacksSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(AttackSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(AdditionalEffectSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(FumbleSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CriticalResultSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(DamageResultSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(DamageResultRowSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CampaignAttackRestrictionsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(WeaponSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(ItemSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(ArmorTemplateSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(HerbTemplateSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(PoisonTemplateSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(NaturalsTemplateSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(SubstanceTemplateSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(WeaponTemplateSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(ItemTemplateSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CampaignSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(SpecializationStatsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(TalentParametersPerUnitSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(TalentParametersSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(VarietyTalentParametersSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(VarietyTalentTiersSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CreatureCategoryTalentsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CreatureTypeTalentsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(TalentParametersPerUnitSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(TalentSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(ArchetypeSkillsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(ArchetypeSpellsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(ArchetypeLevelsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CreatureArchetypeSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(SkillStatsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(VarietySkillsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(SpecializationSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(ArmorTemplateSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(HerbTemplateSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(NaturalsTemplateSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(PoisonTemplateSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(SubstanceTemplateSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(WeaponTemplateSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(WeaponSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(SkillSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(SkillCategoryStatsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(VarietyCriticalCodesSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(VarietyStatsSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CreatureVarietySchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(SpellTypeSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(EncounterSetupEncounterInfoSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(EncounterSetupSchema.TABLE_NAME, null, null);

		sqLiteDatabase.delete(BiomeSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CreatureCategorySchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CreatureTypeSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CriticalCodeSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CultureSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(DamageTableSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(OutlookSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(RaceSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(SkillCategorySchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(SpellSubTypeSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(TalentCategorySchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(DiseaseSchema.TABLE_NAME, null, null);
	}

	public void clearCampaignTables(Campaign campaign) {
		final String selectionArgs[] = { String.valueOf(campaign.getId()) };
		String selection = "EXISTS( SELECT NULL FROM " + ItemSchema.TABLE_NAME + " a WHERE a." + ItemSchema.COLUMN_ID + " = " +
				WeaponSchema.COLUMN_ITEM_ID + " AND a." + ItemSchema.COLUMN_CAMPAIGN_ID + " = ?)";

		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		sqLiteDatabase.delete(WeaponSchema.TABLE_NAME, selection, selectionArgs);

		selection = ItemSchema.COLUMN_CAMPAIGN_ID + " = ?";
		sqLiteDatabase.delete(ItemSchema.TABLE_NAME, selection, selectionArgs);

		selection = EncounterSetupSchema.COLUMN_CAMPAIGN_ID + " = ?";
		sqLiteDatabase.delete(EncounterSetupSchema.TABLE_NAME, selection, selectionArgs);

		selection = CharacterSchema.COLUMN_CAMPAIGN_ID + " = ?";
		sqLiteDatabase.delete(CharacterSchema.TABLE_NAME, selection, selectionArgs);

		selection = CampaignSchema.COLUMN_ID + " = ?";
		sqLiteDatabase.delete(CampaignSchema.TABLE_NAME, selection, selectionArgs);
	}

	private BodyLocation getBodyLocation(int bodyPartId) {
		switch (bodyPartId) {
			case 1:
				return BodyLocation.ARM;
			case 2:
				return BodyLocation.CHEST;
			case 3:
				return BodyLocation.GROIN;
			case 4:
				return BodyLocation.HEAD;
			case 5:
				return BodyLocation.LEG;
			default:
				return BodyLocation.ARM;
		}
	}

	private CriticalType getCriticalType(int criticalTypeId) {
		switch (criticalTypeId) {
			case 1:
				return CriticalType.ACID;
			case 2:
				return CriticalType.GRAPPLE;
			case 3:
				return CriticalType.HEAT;
			case 4:
				return CriticalType.IMPACT;
			case 5:
				return CriticalType.CRUSH;
			case 6:
				return CriticalType.ELECTRICITY;
			case 7:
				return CriticalType.COLD;
			case 8:
				return CriticalType.PUNCTURE;
			case 9:
				return CriticalType.SLASH;
			case 10:
				return CriticalType.STRIKE;
			case 11:
				return CriticalType.UNBALANCE;
			default:
				return CriticalType.CRUSH;
		}
	}

	private void upgrade(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.beginTransaction();
		Cursor cursor = null;
		try {
//			cursor = sqLiteDatabase.rawQuery(
//			"SELECT id, bonus, twoHanded"
//					+ " FROM " + WeaponSchema.TABLE_NAME, null);
//
//			if(cursor != null) {
//				cursor.moveToFirst();
//				List<Weapon> weapons = new ArrayList<>(cursor.getCount());
//				while (!cursor.isAfterLast()) {
//					Weapon weapon = cursorToEntity(cursor);
//					weapons.add(weapon);
//					cursor.moveToNext();
//				}
//				sqLiteDatabase.execSQL("DROP TABLE " + WeaponSchema.TABLE_NAME);
				sqLiteDatabase.execSQL(FumbleSchema.TABLE_CREATE);
				sqLiteDatabase.execSQL(AdditionalEffectSchema.TABLE_CREATE);
//				for (Weapon weapon : weapons) {
//					ContentValues values = getContentValues(weapon);
//					sqLiteDatabase.insert(WeaponSchema.TABLE_NAME, null, values);
//				}
//
//				Log.d(TAG, "upgrade: Upgrade completed successfully");
				sqLiteDatabase.setTransactionSuccessful();
//			}
		}
		catch (Exception e) {
			Log.e(TAG, "upgrade: Upgrade failed.", e);
		}
		finally {
			if(cursor != null) {
				cursor.close();
			}
			sqLiteDatabase.endTransaction();
		}
	}

	private Weapon cursorToEntity(Cursor cursor) {
		Weapon instance = new Weapon();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
		instance.setTwoHanded(cursor.getInt(cursor.getColumnIndexOrThrow(
				WeaponSchema.COLUMN_TWO_HANDED)) != 0);
		instance.setBonus(cursor.getShort(cursor.getColumnIndexOrThrow(WeaponSchema.COLUMN_BONUS)));

		return instance;
	}

	private ContentValues getContentValues(Weapon instance) {
		ContentValues values;

		values = new ContentValues(3);
		values.put(WeaponSchema.COLUMN_ITEM_ID, instance.getId());
		values.put(WeaponSchema.COLUMN_BONUS, instance.getBonus());
		values.put(WeaponSchema.COLUMN_TWO_HANDED, instance.isTwoHanded());

		return values;
	}

	private class AttackBonusTemp {
		int varietyId;
		int attackId;
		short bonus;
	}
}
