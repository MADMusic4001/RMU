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
import com.madinnovations.rmu.data.dao.combat.schemas.AttackSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalCodeSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalResultSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultRowSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageTableSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.DiseaseSchema;
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
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureCategorySchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureCategoryTalentsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSkillRanksSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSpecializationRanksSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSpellListRanksSchema;
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
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.combat.BodyLocation;
import com.madinnovations.rmu.data.entities.combat.CriticalType;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.spells.Realm;

import java.util.ArrayList;
import java.util.List;

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
	public static final  int    DATABASE_VERSION = 19;

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
			sqLiteDatabase.execSQL(CreatureSkillRanksSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CreatureSpecializationRanksSchema.TABLE_CREATE);
			sqLiteDatabase.execSQL(CreatureSpellListRanksSchema.TABLE_CREATE);
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
				case 18:
					upgrade(sqLiteDatabase);
					break;
            }
            sqLiteDatabase.setTransactionSuccessful();
        }
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
		sqLiteDatabase.delete(CreatureSpellListRanksSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CreatureSpecializationRanksSchema.TABLE_NAME, null, null);
		sqLiteDatabase.delete(CreatureSkillRanksSchema.TABLE_NAME, null, null);
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
				WeaponSchema.COLUMN_ID + " AND a." + ItemSchema.COLUMN_CAMPAIGN_ID + " = ?)";

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
			cursor = sqLiteDatabase.rawQuery(
			"SELECT id, campaignId, currentLevel, experiencePoints, firstName, lastName, knownAs, description, hairColor," +
			"hairStyle, eyeColor, skinComplexion, facialFeatures, identifyingMarks, personality, mannerisms, hometown," +
			"familyInfo, raceId, cultureId, professionId, realm, realm2, realm3, height, weight, currentHPLoss," +
			"currentDevelopmentPoints, currentFatigue, currentPPLoss, statIncreases, mainHandItem, offhandItem, shirtItem," +
			"pantsItem, headItem, chestItem, armsItem, legsItem, feetItem, backItem, backpackItem"
					+ " FROM " + CharacterSchema.TABLE_NAME, null);

			if(cursor != null) {
				cursor.moveToFirst();
				List<Character> characters = new ArrayList<>(cursor.getCount());
				while (!cursor.isAfterLast()) {
					Character character = cursorToCharacterEntity(cursor);
					characters.add(character);
					cursor.moveToNext();
				}
				sqLiteDatabase.execSQL("DROP TABLE " + CharacterSchema.TABLE_NAME);
				sqLiteDatabase.execSQL(CharacterSchema.TABLE_CREATE);
				for (Character character : characters) {
					ContentValues values = getCharacterContentValues(character);
					sqLiteDatabase.insert(CharacterSchema.TABLE_NAME, null, values);
				}
			}

			cursor = sqLiteDatabase.rawQuery("SELECT id,campaignId,creatureVarietyId,level,maxHits,currentHits," +
											 "creatureArchetypeId,currentDevelopmentPoints,numCreatures"
													 + " FROM " + CreatureSchema.TABLE_NAME, null);
			if(cursor != null) {
				cursor.moveToFirst();
				List<Creature> creatures = new ArrayList<>(cursor.getCount());
				while (!cursor.isAfterLast()) {
					Creature creature = cursorToEntity(cursor);
					creatures.add(creature);
					cursor.moveToNext();
				}
				sqLiteDatabase.execSQL("DROP TABLE " + CreatureSchema.TABLE_NAME);
				sqLiteDatabase.execSQL(CreatureSchema.TABLE_CREATE);
				for (Creature creature : creatures) {
					ContentValues values = getContentValues(creature);
					sqLiteDatabase.insert(CreatureSchema.TABLE_NAME, null, values);
				}

				sqLiteDatabase.setTransactionSuccessful();
			}
		}
		finally {
			if(cursor != null) {
				cursor.close();
			}
			sqLiteDatabase.endTransaction();
		}
	}

	private Character cursorToCharacterEntity(Cursor cursor) {
		Character instance = new Character();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_ID)));
		instance.setCampaign(new Campaign(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_CAMPAIGN_ID))));
		instance.setCurrentLevel(cursor.getShort(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_CURRENT_LEVEL)));
		instance.setExperiencePoints(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_EXPERIENCE_POINTS)));
		instance.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_FIRST_NAME)));
		instance.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_LAST_NAME)));
		instance.setKnownAs(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_KNOWN_AS)));
		instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_DESCRIPTION)));
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_HAIR_COLOR))) {
			instance.setHairColor(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_HAIR_COLOR)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_HAIR_STYLE))) {
			instance.setHairStyle(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_HAIR_STYLE)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_EYE_COLOR))) {
			instance.setEyeColor(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_EYE_COLOR)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_SKIN_COMPLEXION))) {
			instance.setSkinComplexion(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_SKIN_COMPLEXION)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_FACIAL_FEATURES))) {
			instance.setFacialFeatures(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_FACIAL_FEATURES)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_IDENTIFYING_MARKS))) {
			instance.setIdentifyingMarks(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_IDENTIFYING_MARKS)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_PERSONALITY))) {
			instance.setPersonality(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_PERSONALITY)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_MANNERISMS))) {
			instance.setMannerisms(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_MANNERISMS)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_HOMETOWN))) {
			instance.setHometown(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_HOMETOWN)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_FAMILY_INFO))) {
			instance.setFamilyInfo(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_FAMILY_INFO)));
		}
		instance.setRace(new Race(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_RACE_ID))));
		instance.setCulture(new Culture(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_CULTURE_ID))));
		instance.setProfession(new Profession(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_PROFESSION_ID))));
		instance.setRealm(Realm.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_REALM))));
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_REALM2))) {
			instance.setRealm2(Realm.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_REALM2))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_REALM3))) {
			instance.setRealm3(Realm.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_REALM3))));
		}
		instance.setHeight(cursor.getShort(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_HEIGHT)));
		instance.setWeight(cursor.getShort(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_WEIGHT)));
		instance.setHitPointLoss(cursor.getShort(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_CURRENT_HP_LOSS)));
		instance.setCurrentDevelopmentPoints(cursor.getShort(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_CURRENT_DEVELOPMENT_POINTS)));
		instance.setFatigue(cursor.getShort(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_CURRENT_FATIGUE)));
		instance.setPowerPointLoss(cursor.getShort(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_CURRENT_PP_LOSS)));
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_MAIN_HAND_ITEM_ID))) {
			instance.setMainHandItem(new Item(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_MAIN_HAND_ITEM_ID)
			)));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_OFFHAND_ITEM_ID))) {
			instance.setOffhandItem(new Item(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_OFFHAND_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_SHIRT_ITEM_ID))) {
			instance.setShirtItem(new Item(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_SHIRT_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_PANTS_ITEM_ID))) {
			instance.setPantsItem(new Item(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_PANTS_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_HEAD_ITEM_ID))) {
			instance.setHeadItem(new Item(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_HEAD_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_CHEST_ITEM_ID))) {
			instance.setChestItem(new Item(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_CHEST_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_ARMS_ITEM_ID))) {
			instance.setArmsItem(new Item(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_ARMS_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_LEGS_ITEM_ID))) {
			instance.setLegsItem(new Item(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_LEGS_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_FEET_ITEM_ID))) {
			instance.setFeetItem(new Item(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_FEET_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_BACK_ITEM_ID))) {
			instance.setBackItem(new Item(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_BACK_ITEM_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_BACKPACK_ITEM_ID))) {
			instance.setBackpackItem(new Item(cursor.getInt(cursor.getColumnIndexOrThrow(CharacterSchema.COLUMN_BACKPACK_ITEM_ID))));
		}

		return instance;
	}

	private Creature cursorToEntity(Cursor cursor) {
		Creature instance = new Creature();

		instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(CreatureSchema.COLUMN_ID)));
		instance.setCampaign(new Campaign(cursor.getInt(cursor.getColumnIndexOrThrow(CreatureSchema.COLUMN_CAMPAIGN_ID))));
		instance.setCreatureVariety(new CreatureVariety(cursor.getInt(cursor.getColumnIndexOrThrow(
				CreatureSchema.COLUMN_CREATURE_VARIETY_ID))));
		instance.setCurrentLevel(cursor.getShort(cursor.getColumnIndexOrThrow(CreatureSchema.COLUMN_LEVEL)));
		instance.setMaxHits(cursor.getShort(cursor.getColumnIndexOrThrow(CreatureSchema.COLUMN_MAX_HITS)));
		instance.setCurrentHits(cursor.getShort(cursor.getColumnIndexOrThrow(CreatureSchema.COLUMN_CURRENT_HITS)));
		instance.setCurrentDevelopmentPoints(cursor.getShort(cursor.getColumnIndexOrThrow(CreatureSchema.COLUMN_CURRENT_DPS)));
		instance.setNumCreatures(cursor.getShort(cursor.getColumnIndexOrThrow(CreatureSchema.COLUMN_NUM_CREATURES)));
		instance.setArchetype(new CreatureArchetype(cursor.getInt(cursor.getColumnIndexOrThrow(
				CreatureSchema.COLUMN_CREATURE_ARCHETYPE_ID))));

		return instance;
	}

	private ContentValues getContentValues(Creature instance) {
		ContentValues values;

		values = new ContentValues(8);
		values.put(CreatureSchema.COLUMN_ID, instance.getId());
		values.put(CreatureSchema.COLUMN_CAMPAIGN_ID, instance.getCampaign().getId());
		values.put(CreatureSchema.COLUMN_CREATURE_VARIETY_ID, instance.getCreatureVariety().getId());
		values.putNull(CreatureSchema.COLUMN_CREATURE_ARCHETYPE_ID);
		values.put(CreatureSchema.COLUMN_MAX_HITS, instance.getMaxHits());
		values.put(CreatureSchema.COLUMN_CURRENT_HITS, instance.getCurrentHits());
		values.put(CreatureSchema.COLUMN_LEVEL, instance.getCurrentLevel());
		values.put(CreatureSchema.COLUMN_CURRENT_DPS, instance.getCurrentDevelopmentPoints());
		values.put(CreatureSchema.COLUMN_BASE_MOVEMENT_RATE, instance.getBaseMovementRate());
		values.put(CreatureSchema.COLUMN_NUM_CREATURES, instance.getNumCreatures());

		return values;
	}

	private ContentValues getCharacterContentValues(Character instance) {
		ContentValues values;

		if(instance.getId() != -1) {
			values = new ContentValues(43);
			values.put(CharacterSchema.COLUMN_ID, instance.getId());
		}
		else {
			values = new ContentValues(42);
		}
		values.put(CharacterSchema.COLUMN_CAMPAIGN_ID, instance.getCampaign().getId());
		values.put(CharacterSchema.COLUMN_CURRENT_LEVEL, instance.getCurrentLevel());
		values.put(CharacterSchema.COLUMN_EXPERIENCE_POINTS, instance.getExperiencePoints());
		values.put(CharacterSchema.COLUMN_FIRST_NAME, instance.getFirstName());
		values.put(CharacterSchema.COLUMN_LAST_NAME, instance.getLastName());
		values.put(CharacterSchema.COLUMN_KNOWN_AS, instance.getKnownAs());
		values.put(CharacterSchema.COLUMN_DESCRIPTION, instance.getDescription());
		if(instance.getHairColor() == null) {
			values.putNull(CharacterSchema.COLUMN_HAIR_COLOR);
		}
		else {
			values.put(CharacterSchema.COLUMN_HAIR_COLOR, instance.getHairColor());
		}
		if(instance.getHairStyle() == null) {
			values.putNull(CharacterSchema.COLUMN_HAIR_STYLE);
		}
		else {
			values.put(CharacterSchema.COLUMN_HAIR_STYLE, instance.getHairStyle());
		}
		if(instance.getEyeColor() == null) {
			values.putNull(CharacterSchema.COLUMN_EYE_COLOR);
		}
		else {
			values.put(CharacterSchema.COLUMN_EYE_COLOR, instance.getEyeColor());
		}
		if(instance.getSkinComplexion() == null) {
			values.putNull(CharacterSchema.COLUMN_SKIN_COMPLEXION);
		}
		else {
			values.put(CharacterSchema.COLUMN_SKIN_COMPLEXION, instance.getSkinComplexion());
		}
		if(instance.getFacialFeatures() == null) {
			values.putNull(CharacterSchema.COLUMN_FACIAL_FEATURES);
		}
		else {
			values.put(CharacterSchema.COLUMN_FACIAL_FEATURES, instance.getFacialFeatures());
		}
		if(instance.getIdentifyingMarks() == null) {
			values.putNull(CharacterSchema.COLUMN_IDENTIFYING_MARKS);
		}
		else {
			values.put(CharacterSchema.COLUMN_IDENTIFYING_MARKS, instance.getIdentifyingMarks());
		}
		if(instance.getPersonality() == null) {
			values.putNull(CharacterSchema.COLUMN_PERSONALITY);
		}
		else {
			values.put(CharacterSchema.COLUMN_PERSONALITY, instance.getPersonality());
		}
		if(instance.getMannerisms() == null) {
			values.putNull(CharacterSchema.COLUMN_MANNERISMS);
		}
		else {
			values.put(CharacterSchema.COLUMN_MANNERISMS, instance.getMannerisms());
		}
		if(instance.getFamilyInfo() == null) {
			values.putNull(CharacterSchema.COLUMN_FAMILY_INFO);
		}
		else {
			values.put(CharacterSchema.COLUMN_FAMILY_INFO, instance.getFamilyInfo());
		}
		if(instance.getHometown() == null) {
			values.putNull(CharacterSchema.COLUMN_HOMETOWN);
		}
		else {
			values.put(CharacterSchema.COLUMN_HOMETOWN, instance.getHometown());
		}
		values.put(CharacterSchema.COLUMN_RACE_ID, instance.getRace().getId());
		values.put(CharacterSchema.COLUMN_CULTURE_ID, instance.getCulture().getId());
		values.put(CharacterSchema.COLUMN_PROFESSION_ID, instance.getProfession().getId());
		values.put(CharacterSchema.COLUMN_REALM, instance.getRealm().name());
		if(instance.getRealm2() == null) {
			values.putNull(CharacterSchema.COLUMN_REALM2);
		}
		else {
			values.put(CharacterSchema.COLUMN_REALM2, instance.getRealm2().name());
		}
		if(instance.getRealm3() == null) {
			values.putNull(CharacterSchema.COLUMN_REALM3);
		}
		else {
			values.put(CharacterSchema.COLUMN_REALM3, instance.getRealm3().name());
		}
		values.put(CharacterSchema.COLUMN_HEIGHT, instance.getHeight());
		values.put(CharacterSchema.COLUMN_WEIGHT, instance.getWeight());
		values.put(CharacterSchema.COLUMN_CURRENT_HP_LOSS, instance.getHitPointLoss());
		values.put(CharacterSchema.COLUMN_CURRENT_DEVELOPMENT_POINTS, instance.getCurrentDevelopmentPoints());
		values.put(CharacterSchema.COLUMN_CURRENT_FATIGUE, instance.getFatigue());
		values.put(CharacterSchema.COLUMN_CURRENT_PP_LOSS, instance.getPowerPointLoss());
		values.put(CharacterSchema.COLUMN_BASE_MOVEMENT_RATE, instance.getBaseMovementRate());
		values.put(CharacterSchema.COLUMN_STAT_INCREASES, instance.getStatIncreases());
		if(instance.getMainHandItem() == null) {
			values.putNull(CharacterSchema.COLUMN_MAIN_HAND_ITEM_ID);
		}
		else {
			values.put(CharacterSchema.COLUMN_MAIN_HAND_ITEM_ID, instance.getMainHandItem().getId());
		}
		if(instance.getOffhandItem() == null) {
			values.putNull(CharacterSchema.COLUMN_OFFHAND_ITEM_ID);
		}
		else {
			values.put(CharacterSchema.COLUMN_OFFHAND_ITEM_ID, instance.getOffhandItem().getId());
		}
		if(instance.getShirtItem() == null) {
			values.putNull(CharacterSchema.COLUMN_SHIRT_ITEM_ID);
		}
		else {
			values.put(CharacterSchema.COLUMN_SHIRT_ITEM_ID, instance.getShirtItem().getId());
		}
		if(instance.getPantsItem() == null) {
			values.putNull(CharacterSchema.COLUMN_PANTS_ITEM_ID);
		}
		else {
			values.put(CharacterSchema.COLUMN_PANTS_ITEM_ID, instance.getPantsItem().getId());
		}
		if(instance.getHeadItem() == null) {
			values.putNull(CharacterSchema.COLUMN_HEAD_ITEM_ID);
		}
		else {
			values.put(CharacterSchema.COLUMN_HEAD_ITEM_ID, instance.getHeadItem().getId());
		}
		if(instance.getChestItem() == null) {
			values.putNull(CharacterSchema.COLUMN_CHEST_ITEM_ID);
		}
		else {
			values.put(CharacterSchema.COLUMN_CHEST_ITEM_ID, instance.getChestItem().getId());
		}
		if(instance.getArmsItem() == null) {
			values.putNull(CharacterSchema.COLUMN_ARMS_ITEM_ID);
		}
		else {
			values.put(CharacterSchema.COLUMN_ARMS_ITEM_ID, instance.getArmsItem().getId());
		}
		if(instance.getLegsItem() == null) {
			values.putNull(CharacterSchema.COLUMN_LEGS_ITEM_ID);
		}
		else {
			values.put(CharacterSchema.COLUMN_LEGS_ITEM_ID, instance.getLegsItem().getId());
		}
		if(instance.getFeetItem() == null) {
			values.putNull(CharacterSchema.COLUMN_FEET_ITEM_ID);
		}
		else {
			values.put(CharacterSchema.COLUMN_FEET_ITEM_ID, instance.getFeetItem().getId());
		}
		if(instance.getBackItem() == null) {
			values.putNull(CharacterSchema.COLUMN_BACK_ITEM_ID);
		}
		else {
			values.put(CharacterSchema.COLUMN_BACK_ITEM_ID, instance.getBackItem().getId());
		}
		if(instance.getBackpackItem() == null) {
			values.putNull(CharacterSchema.COLUMN_BACKPACK_ITEM_ID);
		}
		else {
			values.put(CharacterSchema.COLUMN_BACKPACK_ITEM_ID, instance.getBackpackItem().getId());
		}

		return values;
	}
}
