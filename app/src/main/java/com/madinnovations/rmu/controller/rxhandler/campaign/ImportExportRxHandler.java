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
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.RMUDatabaseHelper;
import com.madinnovations.rmu.data.dao.character.CharacterDao;
import com.madinnovations.rmu.data.dao.character.CultureDao;
import com.madinnovations.rmu.data.dao.character.ProfessionDao;
import com.madinnovations.rmu.data.dao.character.RaceDao;
import com.madinnovations.rmu.data.dao.character.serializers.CharacterSerializer;
import com.madinnovations.rmu.data.dao.character.serializers.CultureSerializer;
import com.madinnovations.rmu.data.dao.character.serializers.ProfessionSerializer;
import com.madinnovations.rmu.data.dao.character.serializers.RaceSerializer;
import com.madinnovations.rmu.data.dao.combat.AttackDao;
import com.madinnovations.rmu.data.dao.combat.BodyPartDao;
import com.madinnovations.rmu.data.dao.combat.CriticalCodeDao;
import com.madinnovations.rmu.data.dao.combat.CriticalResultDao;
import com.madinnovations.rmu.data.dao.combat.CriticalTypeDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultRowDao;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.combat.serializers.AttackSerializer;
import com.madinnovations.rmu.data.dao.combat.serializers.CriticalResultSerializer;
import com.madinnovations.rmu.data.dao.combat.serializers.DamageResultRowSerializer;
import com.madinnovations.rmu.data.dao.combat.serializers.DamageTableSerializer;
import com.madinnovations.rmu.data.dao.common.LocomotionTypeDao;
import com.madinnovations.rmu.data.dao.common.ParameterDao;
import com.madinnovations.rmu.data.dao.common.SizeDao;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.TalentCategoryDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.common.serializers.SkillCategorySerializer;
import com.madinnovations.rmu.data.dao.common.serializers.SpecializationSerializer;
import com.madinnovations.rmu.data.dao.creature.CreatureArchetypeDao;
import com.madinnovations.rmu.data.dao.creature.CreatureCategoryDao;
import com.madinnovations.rmu.data.dao.creature.CreatureTypeDao;
import com.madinnovations.rmu.data.dao.creature.CreatureVarietyDao;
import com.madinnovations.rmu.data.dao.creature.OutlookDao;
import com.madinnovations.rmu.data.dao.creature.serializers.CreatureArchetypeSerializer;
import com.madinnovations.rmu.data.dao.creature.serializers.CreatureTypeSerializer;
import com.madinnovations.rmu.data.dao.creature.serializers.CreatureVarietySerializer;
import com.madinnovations.rmu.data.dao.item.ItemDao;
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.dao.spells.SpellDao;
import com.madinnovations.rmu.data.dao.spells.SpellListDao;
import com.madinnovations.rmu.data.dao.spells.SpellListTypeDao;
import com.madinnovations.rmu.data.dao.spells.serializers.RealmSerializer;
import com.madinnovations.rmu.data.dao.spells.serializers.SpellListSerializer;
import com.madinnovations.rmu.data.dao.spells.serializers.SpellSerializer;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.BodyPart;
import com.madinnovations.rmu.data.entities.combat.CriticalCode;
import com.madinnovations.rmu.data.entities.combat.CriticalResult;
import com.madinnovations.rmu.data.entities.combat.CriticalType;
import com.madinnovations.rmu.data.entities.combat.DamageResult;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.common.LocomotionType;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;
import com.madinnovations.rmu.data.entities.creature.CreatureCategory;
import com.madinnovations.rmu.data.entities.creature.CreatureType;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;
import com.madinnovations.rmu.data.entities.creature.Outlook;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.data.entities.spells.Spell;
import com.madinnovations.rmu.data.entities.spells.SpellList;
import com.madinnovations.rmu.data.entities.spells.SpellListType;
import com.madinnovations.rmu.view.RMUAppException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observables for importing and exporting the database
 */
public class ImportExportRxHandler {
	private static final String VERSION = "version";
	private AttackDao                   attackDao;
	private AttackSerializer            attackSerializer;
	private BodyPartDao                 bodyPartDao;
	private CharacterDao                characterDao;
	private CharacterSerializer         characterSerializer;
	private CreatureArchetypeDao        creatureArchetypeDao;
	private CreatureArchetypeSerializer creatureArchetypeSerializer;
	private CreatureCategoryDao         creatureCategoryDao;
	private CreatureTypeDao             creatureTypeDao;
	private CreatureTypeSerializer      creatureTypeSerializer;
	private CreatureVarietyDao          creatureVarietyDao;
	private CreatureVarietySerializer   creatureVarietySerializer;
	private CriticalCodeDao             criticalCodeDao;
	private CriticalResultDao           criticalResultDao;
	private CriticalResultSerializer    criticalResultSerializer;
	private CriticalTypeDao             criticalTypeDao;
	private CultureDao                  cultureDao;
	private CultureSerializer           cultureSerializer;
	private DamageResultDao             damageResultDao;
	private DamageResultRowDao          damageResultRowDao;
	private DamageResultRowSerializer   damageResultRowSerializer;
	private DamageTableDao              damageTableDao;
	private DamageTableSerializer       damageTableSerializer;
	private ItemDao                     itemDao;
	private LocomotionTypeDao           locomotionTypeDao;
	private OutlookDao                  outlookDao;
	private ParameterDao                parameterDao;
	private ProfessionDao               professionDao;
	private ProfessionSerializer        professionSerializer;
	private RaceDao                     raceDao;
	private RaceSerializer              raceSerializer;
	private RealmDao                    realmDao;
	private RealmSerializer             realmSerializer;
	private SizeDao                     sizeDao;
	private SkillDao                    skillDao;
	private SkillCategoryDao            skillCategoryDao;
	private SkillCategorySerializer     skillCategorySerializer;
	private SpecializationDao           specializationDao;
	private SpecializationSerializer    specializationSerializer;
	private SpellDao                    spellDao;
	private SpellSerializer             spellSerializer;
	private SpellListDao                spellListDao;
	private SpellListSerializer         spellListSerializer;
	private SpellListTypeDao            spellListTypeDao;
	private StatDao                     statDao;
	private TalentDao                   talentDao;
	private TalentCategoryDao           talentCategoryDao;

	/**
	 * Creates a new ImportExportRxHandler instance
	 */
	@Inject
	public ImportExportRxHandler(AttackDao attackDao, AttackSerializer attackSerializer, BodyPartDao bodyPartDao,
								 CharacterDao characterDao, CharacterSerializer characterSerializer,
								 CreatureArchetypeDao creatureArchetypeDao,
								 CreatureArchetypeSerializer creatureArchetypeSerializer, CreatureCategoryDao 
											 creatureCategoryDao,
								 CreatureTypeDao creatureTypeDao, CreatureTypeSerializer creatureTypeSerializer,
								 CreatureVarietyDao creatureVarietyDao, CreatureVarietySerializer creatureVarietySerializer,
								 CriticalCodeDao criticalCodeDao, CriticalResultDao criticalResultDao,
								 CriticalResultSerializer criticalResultSerializer, CriticalTypeDao criticalTypeDao,
								 CultureDao cultureDao, CultureSerializer cultureSerializer, DamageResultDao damageResultDao,
								 DamageResultRowDao damageResultRowDao, DamageResultRowSerializer damageResultRowSerializer,
								 DamageTableDao damageTableDao, DamageTableSerializer damageTableSerializer, ItemDao itemDao,
								 LocomotionTypeDao locomotionTypeDao, OutlookDao outlookDao, ParameterDao parameterDao,
								 ProfessionDao professionDao, ProfessionSerializer professionSerializer, RaceDao raceDao,
								 RaceSerializer raceSerializer, RealmDao realmDao, RealmSerializer realmSerializer,
								 SizeDao sizeDao, SkillDao skillDao, SkillCategoryDao skillCategoryDao,
								 SkillCategorySerializer skillCategorySerializer, SpecializationDao specializationDao,
								 SpecializationSerializer specializationSerializer, SpellDao spellDao,
								 SpellSerializer spellSerializer, SpellListDao spellListDao,
								 SpellListSerializer spellListSerializer, SpellListTypeDao spellListTypeDao, StatDao statDao,
								 TalentDao talentDao, TalentCategoryDao talentCategoryDao) {
		this.attackDao = attackDao;
		this.attackSerializer = attackSerializer;
		this.bodyPartDao = bodyPartDao;
		this.characterDao = characterDao;
		this.characterSerializer = characterSerializer;
		this.creatureArchetypeDao = creatureArchetypeDao;
		this.creatureArchetypeSerializer = creatureArchetypeSerializer;
		this.creatureCategoryDao = creatureCategoryDao;
		this.creatureTypeDao = creatureTypeDao;
		this.creatureTypeSerializer = creatureTypeSerializer;
		this.creatureVarietyDao = creatureVarietyDao;
		this.creatureVarietySerializer = creatureVarietySerializer;
		this.criticalCodeDao = criticalCodeDao;
		this.criticalResultDao = criticalResultDao;
		this.criticalResultSerializer = criticalResultSerializer;
		this.criticalTypeDao = criticalTypeDao;
		this.cultureDao = cultureDao;
		this.cultureSerializer = cultureSerializer;
		this.damageResultDao = damageResultDao;
		this.damageResultRowDao = damageResultRowDao;
		this.damageResultRowSerializer = damageResultRowSerializer;
		this.damageTableDao = damageTableDao;
		this.damageTableSerializer = damageTableSerializer;
		this.itemDao = itemDao;
		this.locomotionTypeDao = locomotionTypeDao;
		this.outlookDao = outlookDao;
		this.parameterDao = parameterDao;
		this.professionDao = professionDao;
		this.professionSerializer = professionSerializer;
		this.raceDao = raceDao;
		this.raceSerializer = raceSerializer;
		this.realmDao = realmDao;
		this.realmSerializer = realmSerializer;
		this.sizeDao = sizeDao;
		this.skillDao = skillDao;
		this.skillCategoryDao = skillCategoryDao;
		this.skillCategorySerializer = skillCategorySerializer;
		this.specializationDao = specializationDao;
		this.specializationSerializer = specializationSerializer;
		this.spellDao = spellDao;
		this.spellSerializer = spellSerializer;
		this.spellListDao = spellListDao;
		this.spellListSerializer = spellListSerializer;
		this.spellListTypeDao = spellListTypeDao;
		this.statDao = statDao;
		this.talentDao = talentDao;
		this.talentCategoryDao = talentCategoryDao;
	}

	/**
	 * Creates an Observable that when subscribed to will import the RMU database from the file with the given file name. The 
	 * file
	 * name must be a fully qualified path.
	 *
	 * @param fileName the name of the file to read.
	 * @return an {@link Observable} instance that can be subscribed to in order to read the file.
	 */
	public Observable<Integer> importDatabase(@NonNull final String fileName) {
		return Observable.create(
				new Observable.OnSubscribe<Integer>() {
					@Override
					public void call(Subscriber<? super Integer> subscriber) {
						try {
							BufferedReader reader = new BufferedReader(new FileReader(fileName));
							final GsonBuilder gsonBuilder = new GsonBuilder();
							gsonBuilder.registerTypeAdapter(Attack.class, attackSerializer);
							gsonBuilder.registerTypeAdapter(Character.class, characterSerializer);
							gsonBuilder.registerTypeAdapter(CreatureArchetype.class, creatureArchetypeSerializer);
							gsonBuilder.registerTypeAdapter(CreatureType.class, creatureTypeSerializer);
							gsonBuilder.registerTypeAdapter(CreatureVariety.class, creatureVarietySerializer);
							gsonBuilder.registerTypeAdapter(CriticalResult.class, criticalResultSerializer);
							gsonBuilder.registerTypeAdapter(Culture.class, cultureSerializer);
							gsonBuilder.registerTypeAdapter(DamageResultRow.class, damageResultRowSerializer);
							gsonBuilder.registerTypeAdapter(DamageTable.class, damageTableSerializer);
							gsonBuilder.registerTypeAdapter(Profession.class, professionSerializer);
							gsonBuilder.registerTypeAdapter(Race.class, raceSerializer);
							gsonBuilder.registerTypeAdapter(Realm.class, realmSerializer);
							gsonBuilder.registerTypeAdapter(SkillCategory.class, skillCategorySerializer);
							gsonBuilder.registerTypeAdapter(Specialization.class, specializationSerializer);
							gsonBuilder.registerTypeAdapter(Spell.class, spellSerializer);
							gsonBuilder.registerTypeAdapter(SpellList.class, spellListSerializer);
							gsonBuilder.setLenient();
							final Gson gson = gsonBuilder.create();

							JsonReader jsonReader = gson.newJsonReader(reader);
							jsonReader.beginObject();
							String name = jsonReader.nextName();
							if (!name.equals(VERSION)) {
								throw new RMUAppException("Input file does not begin with '" + VERSION + "'");
							}
							int version = jsonReader.nextInt();
							if (version != RMUDatabaseHelper.DATABASE_VERSION) {
								throw new RMUAppException("Input file version mismatch. Expected " +
																  RMUDatabaseHelper.DATABASE_VERSION + " but found " + version);
							}
							jsonReader.endObject();

							List<Stat> stats = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								Stat stat = gson.fromJson(jsonReader, Stat.class);
								stats.add(stat);
							}
							jsonReader.endArray();
							Log.d("RMU", "stats = " + stats);
							subscriber.onNext(3);

							List<LocomotionType> locomotionTypes = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								LocomotionType locomotionType = gson.fromJson(reader, LocomotionType.class);
							}
							jsonReader.endArray();
							subscriber.onNext(6);

							List<Parameter> parameters = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								Parameter parameter = gson.fromJson(reader, Parameter.class);
							}
							jsonReader.endArray();
							subscriber.onNext(10);

							List<Size> sizes = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								Size size = gson.fromJson(reader, Size.class);
							}
							jsonReader.endArray();
							subscriber.onNext(13);

							List<TalentCategory> talentCategories = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								TalentCategory talentCategory = gson.fromJson(reader, TalentCategory.class);
							}
							jsonReader.endArray();
							subscriber.onNext(16);

							List<Talent> talents = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								Talent talent = gson.fromJson(reader, Talent.class);
							}
							jsonReader.endArray();
							subscriber.onNext(20);

							List<Item> items = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								Item item = gson.fromJson(reader, Item.class);
							}
							jsonReader.endArray();
							subscriber.onNext(23);

							List<BodyPart> bodyParts = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								BodyPart bodyPart = gson.fromJson(reader, BodyPart.class);
							}
							jsonReader.endArray();
							subscriber.onNext(26);

							List<CriticalCode> criticalCodeList = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								CriticalCode criticalCode = gson.fromJson(reader, CriticalCode.class);
							}
							jsonReader.endArray();
							subscriber.onNext(30);

							List<CriticalType> criticalTypes = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								CriticalType criticalType = gson.fromJson(reader, CriticalType.class);
							}
							jsonReader.endArray();
							subscriber.onNext(33);

							List<CriticalResult> criticalResults = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								CriticalResult criticalResult = gson.fromJson(reader, CriticalResult.class);
							}
							jsonReader.endArray();
							subscriber.onNext(36);

							List<DamageResult> damageResults = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								DamageResult damageResult = gson.fromJson(reader, DamageResult.class);
							}
							jsonReader.endArray();
							subscriber.onNext(40);

							List<DamageResultRow> damageResultRows = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								DamageResultRow damageResultRow = gson.fromJson(reader, DamageResultRow.class);
							}
							jsonReader.endArray();
							subscriber.onNext(43);

							List<DamageTable> damageTables = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								DamageTable damageTable = gson.fromJson(reader, DamageTable.class);
							}
							jsonReader.endArray();
							subscriber.onNext(46);

							List<CreatureCategory> creatureCategories = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								CreatureCategory creatureCategory = gson.fromJson(reader, CreatureCategory.class);
							}
							jsonReader.endArray();
							subscriber.onNext(49);

							List<CreatureType> creatureTypes = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								CreatureType creatureType = gson.fromJson(reader, CreatureType.class);
							}
							jsonReader.endArray();
							subscriber.onNext(52);

							List<Outlook> outlooks = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								Outlook outlook = gson.fromJson(reader, Outlook.class);
							}
							jsonReader.endArray();
							subscriber.onNext(55);

							List<SkillCategory> skillCategories = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								SkillCategory skillCategory = gson.fromJson(reader, SkillCategory.class);
							}
							jsonReader.endArray();
							subscriber.onNext(59);

							List<Skill> skills = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								Skill skill = gson.fromJson(reader, Skill.class);
							}
							jsonReader.endArray();
							subscriber.onNext(62);

							List<Profession> professions = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								Profession profession = gson.fromJson(reader, Profession.class);
							}
							jsonReader.endArray();
							subscriber.onNext(65);

							List<Culture> cultures = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								Culture culture = gson.fromJson(reader, Culture.class);
							}
							jsonReader.endArray();
							subscriber.onNext(69);

							List<Race> races = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								Race race = gson.fromJson(reader, Race.class);
							}
							jsonReader.endArray();
							subscriber.onNext(72);

							List<Specialization> specializations = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								Specialization specialization = gson.fromJson(reader, Specialization.class);
							}
							jsonReader.endArray();
							subscriber.onNext(75);

							List<Attack> attacks = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								Attack attack = gson.fromJson(reader, Attack.class);
							}
							jsonReader.endArray();
							subscriber.onNext(78);

							List<CreatureArchetype> creatureArchetypes = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								CreatureArchetype creatureArchetype = gson.fromJson(reader, CreatureArchetype.class);
							}
							jsonReader.endArray();
							subscriber.onNext(81);

							List<Realm> realms = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								Realm realm = gson.fromJson(reader, Realm.class);
							}
							jsonReader.endArray();
							subscriber.onNext(84);

							List<SpellList> spellLists = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								SpellList spellList = gson.fromJson(reader, SpellList.class);
							}
							jsonReader.endArray();
							subscriber.onNext(87);

							List<SpellListType> spellListTypes = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								SpellListType spellListType = gson.fromJson(reader, SpellListType.class);
							}
							jsonReader.endArray();
							subscriber.onNext(90);

							List<Spell> spells = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								Spell spell = gson.fromJson(reader, Spell.class);
							}
							jsonReader.endArray();
							subscriber.onNext(93);

							List<Character> characters = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								Character character = gson.fromJson(reader, Character.class);
							}
							jsonReader.endArray();
							subscriber.onNext(96);

							List<CreatureVariety> creatureVarieties = new ArrayList<>();
							jsonReader.beginArray();
							while (jsonReader.hasNext()) {
								CreatureVariety creatureVariety = gson.fromJson(reader, CreatureVariety.class);
							}
							jsonReader.endArray();
							subscriber.onNext(100);

							reader.close();
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).onBackpressureDrop()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io());
	}

	/**
	 * Creates an Observable that when subscribed to will export the RMU database to the file with the given file name. The file
	 * name must be a fully qualified path.
	 *
	 * @param targetFile the file to write to
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
							gsonBuilder.registerTypeAdapter(Attack.class, attackSerializer);
							gsonBuilder.registerTypeAdapter(Character.class, characterSerializer);
							gsonBuilder.registerTypeAdapter(CreatureArchetype.class, creatureArchetypeSerializer);
							gsonBuilder.registerTypeAdapter(CreatureType.class, creatureTypeSerializer);
							gsonBuilder.registerTypeAdapter(CreatureVariety.class, creatureVarietySerializer);
							gsonBuilder.registerTypeAdapter(CriticalResult.class, criticalResultSerializer);
							gsonBuilder.registerTypeAdapter(Culture.class, cultureSerializer);
							gsonBuilder.registerTypeAdapter(DamageResultRow.class, damageResultRowSerializer);
							gsonBuilder.registerTypeAdapter(DamageTable.class, damageTableSerializer);
							gsonBuilder.registerTypeAdapter(Profession.class, professionSerializer);
							gsonBuilder.registerTypeAdapter(Race.class, raceSerializer);
							gsonBuilder.registerTypeAdapter(Realm.class, realmSerializer);
							gsonBuilder.registerTypeAdapter(SkillCategory.class, skillCategorySerializer);
							gsonBuilder.registerTypeAdapter(Specialization.class, specializationSerializer);
							gsonBuilder.registerTypeAdapter(Spell.class, spellSerializer);
							gsonBuilder.registerTypeAdapter(SpellList.class, spellListSerializer);
							final Gson gson = gsonBuilder.create();

							JsonWriter jsonWriter = gson.newJsonWriter(writer);
							jsonWriter.beginObject()
									.name(VERSION)
									.value(RMUDatabaseHelper.DATABASE_VERSION)
									.endObject()
									.flush();
							gson.toJson(statDao.getAll(), writer);
							subscriber.onNext(3);
							gson.toJson(locomotionTypeDao.getAll(), writer);
							subscriber.onNext(6);
							gson.toJson(parameterDao.getAll(), writer);
							subscriber.onNext(10);
							gson.toJson(sizeDao.getAll(), writer);
							subscriber.onNext(13);
							gson.toJson(talentCategoryDao.getAll(), writer);
							subscriber.onNext(16);
							gson.toJson(talentDao.getAll(), writer);
							subscriber.onNext(20);
							gson.toJson(itemDao.getAll(), writer);
							subscriber.onNext(23);
							gson.toJson(bodyPartDao.getAll(), writer);
							subscriber.onNext(26);
							gson.toJson(criticalCodeDao.getAll(), writer);
							subscriber.onNext(30);
							gson.toJson(criticalTypeDao.getAll(), writer);
							subscriber.onNext(33);
							gson.toJson(criticalResultDao.getAll(), writer);
							subscriber.onNext(36);
							gson.toJson(damageResultDao.getAll(), writer);
							subscriber.onNext(40);
							gson.toJson(damageResultRowDao.getAll(), writer);
							subscriber.onNext(43);
							gson.toJson(damageTableDao.getAll(), writer);
							subscriber.onNext(46);
							gson.toJson(creatureCategoryDao.getAll(), writer);
							subscriber.onNext(49);
							gson.toJson(creatureTypeDao.getAll(), writer);
							subscriber.onNext(52);
							gson.toJson(outlookDao.getAll(), writer);
							subscriber.onNext(55);
							gson.toJson(skillCategoryDao.getAll(), writer);
							subscriber.onNext(59);
							gson.toJson(skillDao.getAll(), writer);
							subscriber.onNext(62);
							gson.toJson(professionDao.getAll(), writer);
							subscriber.onNext(65);
							gson.toJson(cultureDao.getAll(), writer);
							subscriber.onNext(69);
							gson.toJson(raceDao.getAll(), writer);
							subscriber.onNext(72);
							gson.toJson(specializationDao.getAll(), writer);
							subscriber.onNext(75);
							gson.toJson(attackDao.getAll(), writer);
							subscriber.onNext(78);
							gson.toJson(creatureArchetypeDao.getAll(), writer);
							subscriber.onNext(81);
							gson.toJson(realmDao.getAll(), writer);
							subscriber.onNext(84);
							gson.toJson(spellListDao.getAll(), writer);
							subscriber.onNext(87);
							gson.toJson(spellListTypeDao.getAll(), writer);
							subscriber.onNext(90);
							gson.toJson(spellDao.getAll(), writer);
							subscriber.onNext(93);
							gson.toJson(characterDao.getAll(), writer);
							subscriber.onNext(96);
							gson.toJson(creatureVarietyDao.getAll(), writer);
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
		).onBackpressureDrop()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io());
	}
}
