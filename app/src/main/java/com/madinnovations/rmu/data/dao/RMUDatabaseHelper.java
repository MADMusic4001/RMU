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
package com.madinnovations.rmu.data.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.madinnovations.rmu.data.dao.character.schemas.CharacterSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterStatsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterTalentsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSkillBlackListSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSkillCostSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSkillRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSkillWhiteListSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSkillCostSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceMovementSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.BodyPartSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalCodeSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalResultSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalTypeSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageTableSchema;
import com.madinnovations.rmu.data.dao.common.schemas.LocomotionTypeSchema;
import com.madinnovations.rmu.data.dao.common.schemas.ParameterSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SizeSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCategorySchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCostSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillSchema;
import com.madinnovations.rmu.data.dao.common.schemas.StatSchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentCategorySchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentParametersSchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.ArchetypeSkillsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.ArchetypeSpellsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureArchetypeSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureCategorySchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureStatsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTypeSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureVarietySchema;
import com.madinnovations.rmu.data.dao.item.schemas.ItemSchema;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponSchema;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods that create and upgrade the SQLite database used by RMU.
 */
@Singleton
public class RMUDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "rmu_db";
    private static final int DATABASE_VERSION = 1;

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
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("RMUDatabaseHelper", "Creating database...");
        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.execSQL(CharacterSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CharacterSkillsSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CharacterStatsSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CharacterTalentsSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CultureSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CultureSkillBlackListSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CultureSkillCostSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CultureSkillRanksSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CultureSkillWhiteListSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(ProfessionSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(ProfessionSkillCostSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(RaceMovementSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(RaceSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(BodyPartSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CriticalCodeSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CriticalResultSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CriticalTypeSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(DamageResultSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(DamageTableSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(LocomotionTypeSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(ParameterSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(SizeSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(SkillCategorySchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(SkillCostSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(SkillSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(StatSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(TalentCategorySchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(TalentParametersSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(TalentSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(ArchetypeSkillsSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(ArchetypeSpellsSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CreatureArchetypeSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CreatureCategorySchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CreatureSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CreatureStatsSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CreatureTypeSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(CreatureVarietySchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(ItemSchema.TABLE_CREATE);
            sqLiteDatabase.execSQL(WeaponSchema.TABLE_CREATE);
            sqLiteDatabase.setTransactionSuccessful();
        }
        finally {
            sqLiteDatabase.endTransaction();
        }
        Log.d("RMUDatabaseHelper", "Database creation completed. Database located at " + sqLiteDatabase.getPath());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d("RMUDatabaseHelper", "Upgrading database from version " + oldVersion + " to version " + newVersion);
    }
}
