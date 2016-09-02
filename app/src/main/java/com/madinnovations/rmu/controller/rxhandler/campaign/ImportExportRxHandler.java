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
package com.madinnovations.rmu.controller.rxhandler.campaign;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.madinnovations.rmu.data.dao.character.CharacterDao;
import com.madinnovations.rmu.data.dao.character.CultureDao;
import com.madinnovations.rmu.data.dao.character.ProfessionDao;
import com.madinnovations.rmu.data.dao.character.RaceDao;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterSkillsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterStatsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CharacterTalentsSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSchema;
import com.madinnovations.rmu.data.dao.character.schemas.CultureSkillRanksSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSchema;
import com.madinnovations.rmu.data.dao.character.schemas.ProfessionSkillCostSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceLocomotionSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.AttackSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.BodyPartSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalCodeSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalResultSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalTypeSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultRowSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageTableSchema;
import com.madinnovations.rmu.data.dao.common.schemas.LocomotionTypeSchema;
import com.madinnovations.rmu.data.dao.common.schemas.ParameterSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SizeSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCategorySchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillCategoryStatsSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SkillStatsSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SpecializationSchema;
import com.madinnovations.rmu.data.dao.common.schemas.SpecializationStatsSchema;
import com.madinnovations.rmu.data.dao.common.schemas.StatSchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentCategorySchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentParametersSchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.ArchetypeSkillsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.ArchetypeSpellsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureArchetypeSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureCategorySchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureTypeSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.CreatureVarietySchema;
import com.madinnovations.rmu.data.dao.creature.schemas.OutlookSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyCriticalCodesSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietySkillsSchema;
import com.madinnovations.rmu.data.dao.creature.schemas.VarietyStatsSchema;
import com.madinnovations.rmu.data.dao.item.schemas.ItemSchema;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.RealmSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellListSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellListTypeSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellSchema;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observables for importing and exporting the database
 */
public class ImportExportRxHandler {
	private CharacterDao characterDao;
	private CultureDao cultureDao;
	private ProfessionDao professionDao;
	private RaceDao raceDao;

	/**
	 * Creates a new ImportExportRxHandler instance
	 */
	@Inject
	public ImportExportRxHandler(CharacterDao characterDao, CultureDao cultureDao, ProfessionDao professionDao, RaceDao raceDao) {
		this.characterDao = characterDao;
		this.cultureDao = cultureDao;
		this.professionDao = professionDao;
		this.raceDao = raceDao;
	}

	/**
	 * Creates an Observable that when subscribed to will import the RMU database from the file with the given file name. The file
	 * name must be a fully qualified path.
	 *
	 * @param fileName  the name of the file to read.
	 * @return an {@link Observable} instance that can be subscribed to in order to read the file.
	 */
	public Observable<Integer> importDatabase(@NonNull String fileName) {
		return Observable.create(
				new Observable.OnSubscribe<Integer>() {
					@Override
					public void call(Subscriber<? super Integer> subscriber) {
						try {
							subscriber.onNext(100);
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io());
	}

	/**
	 * Creates an Observable that when subscribed to will export the RMU database to the file with the given file name. The file
	 * name must be a fully qualified path.
	 *
	 * @param targetFile  the file to write to
	 * @return an {@link Observable} instance that can be subscribed to in order to write the file.
	 */
	public Observable<Integer> exportDatabase(@NonNull final File targetFile) {
		return Observable.create(
				new Observable.OnSubscribe<Integer>() {
					@Override
					public void call(Subscriber<? super Integer> subscriber) {
						try {
							BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
							Gson gson = new Gson();
							gson.toJson(characterDao.getAll(), writer);
							subscriber.onNext(1);
//							sqLiteDatabase.execSQL(CharacterSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(CharacterSkillsSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(CharacterStatsSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(CharacterTalentsSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(CultureSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(CultureSkillRanksSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(CultureSkillRanksSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(ProfessionSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(ProfessionSkillCostSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(RaceLocomotionSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(RaceSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(AttackSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(BodyPartSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(CriticalCodeSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(CriticalResultSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(CriticalTypeSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(DamageResultSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(DamageResultRowSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(DamageTableSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(LocomotionTypeSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(ParameterSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(SizeSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(SkillCategorySchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(SkillCategoryStatsSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(SkillSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(SkillStatsSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(SpecializationSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(SpecializationStatsSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(StatSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(TalentCategorySchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(TalentParametersSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(TalentSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(ArchetypeSkillsSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(ArchetypeSpellsSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(CreatureArchetypeSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(CreatureCategorySchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(CreatureSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(CreatureTypeSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(CreatureVarietySchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(VarietyCriticalCodesSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(VarietyStatsSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(VarietySkillsSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(OutlookSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(ItemSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(WeaponSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(RealmSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(SpellListSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(SpellListTypeSchema.TABLE_CREATE);
//							sqLiteDatabase.execSQL(SpellSchema.TABLE_CREATE);
							subscriber.onNext(100);
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io());
	}

}
