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
import com.google.gson.GsonBuilder;
import com.madinnovations.rmu.data.dao.character.CharacterDao;
import com.madinnovations.rmu.data.dao.character.CultureDao;
import com.madinnovations.rmu.data.dao.character.ProfessionDao;
import com.madinnovations.rmu.data.dao.character.RaceDao;
import com.madinnovations.rmu.data.dao.character.serializers.CharacterSerializer;
import com.madinnovations.rmu.data.dao.combat.BodyPartDao;
import com.madinnovations.rmu.data.dao.combat.CriticalCodeDao;
import com.madinnovations.rmu.data.dao.combat.CriticalTypeDao;
import com.madinnovations.rmu.data.dao.common.LocomotionTypeDao;
import com.madinnovations.rmu.data.dao.common.ParameterDao;
import com.madinnovations.rmu.data.dao.common.SizeDao;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.TalentCategoryDao;
import com.madinnovations.rmu.data.dao.common.serializers.SkillCategorySerializer;
import com.madinnovations.rmu.data.dao.creature.CreatureCategoryDao;
import com.madinnovations.rmu.data.dao.creature.OutlookDao;
import com.madinnovations.rmu.data.dao.item.ItemDao;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.common.SkillCategory;

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
	private BodyPartDao         bodyPartDao;
	private CharacterDao        characterDao;
	private CreatureCategoryDao creatureCategoryDao;
	private CriticalCodeDao     criticalCodeDao;
	private CriticalTypeDao     criticalTypeDao;
	private CultureDao          cultureDao;
	private ItemDao             itemDao;
	private LocomotionTypeDao   locomotionTypeDao;
	private OutlookDao          outlookDao;
	private ParameterDao        parameterDao;
	private ProfessionDao       professionDao;
	private RaceDao             raceDao;
	private SizeDao             sizeDao;
	private SkillDao            skillDao;
	private SkillCategoryDao    skillCategoryDao;
	private StatDao             statDao;
	private TalentCategoryDao   talentCategoryDao;

	/**
	 * Creates a new ImportExportRxHandler instance
	 */
	@Inject
	public ImportExportRxHandler(BodyPartDao bodyPartDao, CharacterDao characterDao, CreatureCategoryDao creatureCategoryDao,
								 CriticalCodeDao criticalCodeDao, CriticalTypeDao criticalTypeDao, CultureDao cultureDao,
								 ItemDao itemDao, LocomotionTypeDao locomotionTypeDao, OutlookDao outlookDao,
								 ParameterDao parameterDao, ProfessionDao professionDao, RaceDao raceDao, SizeDao sizeDao,
								 SkillDao skillDao, SkillCategoryDao skillCategoryDao, StatDao statDao,
								 TalentCategoryDao talentCategoryDao) {
		this.bodyPartDao = bodyPartDao;
		this.characterDao = characterDao;
		this.creatureCategoryDao = creatureCategoryDao;
		this.criticalCodeDao = criticalCodeDao;
		this.criticalTypeDao = criticalTypeDao;
		this.cultureDao = cultureDao;
		this.itemDao = itemDao;
		this.locomotionTypeDao = locomotionTypeDao;
		this.outlookDao = outlookDao;
		this.parameterDao = parameterDao;
		this.professionDao = professionDao;
		this.raceDao = raceDao;
		this.sizeDao = sizeDao;
		this.skillDao = skillDao;
		this.skillCategoryDao = skillCategoryDao;
		this.statDao = statDao;
		this.talentCategoryDao = talentCategoryDao;
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
							final GsonBuilder gsonBuilder = new GsonBuilder();
							gsonBuilder.registerTypeAdapter(Character.class, new CharacterSerializer());
							gsonBuilder.registerTypeAdapter(SkillCategory.class, new SkillCategorySerializer());
							final Gson gson = gsonBuilder.create();
							gson.toJson(statDao.getAll(), writer);
							gson.toJson(locomotionTypeDao.getAll(), writer);
							gson.toJson(parameterDao.getAll(), writer);
							gson.toJson(sizeDao.getAll(), writer);
							gson.toJson(talentCategoryDao.getAll(), writer);
							gson.toJson(itemDao.getAll(), writer);
							gson.toJson(bodyPartDao.getAll(), writer);
							gson.toJson(criticalCodeDao.getAll(), writer);
							gson.toJson(criticalTypeDao.getAll(), writer);
							gson.toJson(creatureCategoryDao.getAll(), writer);
							gson.toJson(outlookDao.getAll(), writer);
//							gson.toJson(characterDao.getAll(), writer);
							subscriber.onNext(1);
							gson.toJson(professionDao.getAll(), writer);
							subscriber.onNext(2);
							gson.toJson(skillCategoryDao.getAll(), writer);
							subscriber.onNext(3);
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
							writer.flush();
							writer.close();
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
