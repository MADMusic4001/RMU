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

import android.database.sqlite.SQLiteDatabase;
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
import com.madinnovations.rmu.data.dao.combat.serializers.DamageResultSerializer;
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
import com.madinnovations.rmu.data.dao.common.serializers.SkillSerializer;
import com.madinnovations.rmu.data.dao.common.serializers.SpecializationSerializer;
import com.madinnovations.rmu.data.dao.common.serializers.TalentSerializer;
import com.madinnovations.rmu.data.dao.creature.CreatureArchetypeDao;
import com.madinnovations.rmu.data.dao.creature.CreatureCategoryDao;
import com.madinnovations.rmu.data.dao.creature.CreatureTypeDao;
import com.madinnovations.rmu.data.dao.creature.CreatureVarietyDao;
import com.madinnovations.rmu.data.dao.creature.OutlookDao;
import com.madinnovations.rmu.data.dao.creature.serializers.CreatureArchetypeSerializer;
import com.madinnovations.rmu.data.dao.creature.serializers.CreatureCategorySerializer;
import com.madinnovations.rmu.data.dao.creature.serializers.CreatureTypeSerializer;
import com.madinnovations.rmu.data.dao.creature.serializers.CreatureVarietySerializer;
import com.madinnovations.rmu.data.dao.item.ItemDao;
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.dao.spells.SpellDao;
import com.madinnovations.rmu.data.dao.spells.SpellListDao;
import com.madinnovations.rmu.data.dao.spells.SpellListTypeDao;
import com.madinnovations.rmu.data.dao.spells.SpellSubTypeDao;
import com.madinnovations.rmu.data.dao.spells.SpellTypeDao;
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
import com.madinnovations.rmu.data.entities.spells.SpellSubType;
import com.madinnovations.rmu.data.entities.spells.SpellType;
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
	private static final String LOG_TAG = "ImportExportRxHandler";
	private static final String VERSION = "version";
	private AttackDao                   attackDao;
	private AttackSerializer            attackSerializer = new AttackSerializer();
	private BodyPartDao                 bodyPartDao;
	private CharacterDao                characterDao;
	private CharacterSerializer         characterSerializer = new CharacterSerializer();
	private CreatureArchetypeDao        creatureArchetypeDao;
	private CreatureArchetypeSerializer creatureArchetypeSerializer = new CreatureArchetypeSerializer();
	private CreatureCategoryDao         creatureCategoryDao;
	private CreatureCategorySerializer  creatureCategorySerializer = new CreatureCategorySerializer();
	private CreatureTypeDao             creatureTypeDao;
	private CreatureTypeSerializer      creatureTypeSerializer = new CreatureTypeSerializer();
	private CreatureVarietyDao          creatureVarietyDao;
	private CreatureVarietySerializer   creatureVarietySerializer = new CreatureVarietySerializer();
	private CriticalCodeDao             criticalCodeDao;
	private CriticalResultDao           criticalResultDao;
	private CriticalResultSerializer    criticalResultSerializer = new CriticalResultSerializer();
	private CriticalTypeDao             criticalTypeDao;
	private CultureDao                  cultureDao;
	private CultureSerializer           cultureSerializer= new CultureSerializer();
	private DamageResultDao             damageResultDao;
	private DamageResultSerializer      damageResultSerializer = new DamageResultSerializer();
	private DamageResultRowDao          damageResultRowDao;
	private DamageResultRowSerializer   damageResultRowSerializer =  new DamageResultRowSerializer();
	private DamageTableDao              damageTableDao;
	private DamageTableSerializer       damageTableSerializer = new DamageTableSerializer();
	private ItemDao                     itemDao;
	private LocomotionTypeDao           locomotionTypeDao;
	private OutlookDao                  outlookDao;
	private ParameterDao                parameterDao;
	private ProfessionDao               professionDao;
	private ProfessionSerializer        professionSerializer = new ProfessionSerializer();
	private RaceDao                     raceDao;
	private RaceSerializer              raceSerializer =  new RaceSerializer();
	private RealmDao                    realmDao;
	private RealmSerializer             realmSerializer = new RealmSerializer();
	private SizeDao                     sizeDao;
	private SkillDao                    skillDao;
	private SkillSerializer             skillSerializer = new SkillSerializer();
	private SkillCategoryDao            skillCategoryDao;
	private SkillCategorySerializer     skillCategorySerializer = new SkillCategorySerializer();
	private SpecializationDao           specializationDao;
	private SpecializationSerializer    specializationSerializer = new SpecializationSerializer();
	private SpellDao                    spellDao;
	private SpellSerializer             spellSerializer = new SpellSerializer();
	private SpellListDao                spellListDao;
	private SpellListSerializer         spellListSerializer = new SpellListSerializer();
	private SpellListTypeDao            spellListTypeDao;
	private SpellSubTypeDao             spellSubTypeDao;
	private SpellTypeDao                spellTypeDao;
	private StatDao                     statDao;
	private TalentDao                   talentDao;
	private TalentSerializer            talentSerializer = new TalentSerializer();
	private TalentCategoryDao           talentCategoryDao;
	private RMUDatabaseHelper           helper;

	/**
	 * Creates a new ImportExportRxHandler instance
	 */
	@Inject
	public ImportExportRxHandler(AttackDao attackDao, BodyPartDao bodyPartDao, CharacterDao characterDao,
								 CreatureArchetypeDao creatureArchetypeDao, CreatureCategoryDao creatureCategoryDao,
								 CreatureTypeDao creatureTypeDao, CreatureVarietyDao creatureVarietyDao,
								 CriticalCodeDao criticalCodeDao, CriticalResultDao criticalResultDao,
								 CriticalTypeDao criticalTypeDao, CultureDao cultureDao, DamageResultDao damageResultDao,
								 DamageResultRowDao damageResultRowDao, DamageTableDao damageTableDao, ItemDao itemDao,
								 LocomotionTypeDao locomotionTypeDao, OutlookDao outlookDao, ParameterDao parameterDao,
								 ProfessionDao professionDao, RaceDao raceDao, RealmDao realmDao, SizeDao sizeDao,
								 SkillDao skillDao, SkillCategoryDao skillCategoryDao, SpecializationDao specializationDao,
								 SpellDao spellDao, SpellListDao spellListDao, SpellListTypeDao spellListTypeDao,
								 SpellSubTypeDao spellSubTypeDao, SpellTypeDao spellTypeDao, StatDao statDao, TalentDao talentDao,
								 TalentCategoryDao talentCategoryDao,RMUDatabaseHelper helper) {
		this.attackDao = attackDao;
		this.bodyPartDao = bodyPartDao;
		this.characterDao = characterDao;
		this.creatureArchetypeDao = creatureArchetypeDao;
		this.creatureCategoryDao = creatureCategoryDao;
		this.creatureTypeDao = creatureTypeDao;
		this.creatureVarietyDao = creatureVarietyDao;
		this.criticalCodeDao = criticalCodeDao;
		this.criticalResultDao = criticalResultDao;
		this.criticalTypeDao = criticalTypeDao;
		this.cultureDao = cultureDao;
		this.damageResultDao = damageResultDao;
		this.damageResultRowDao = damageResultRowDao;
		this.damageTableDao = damageTableDao;
		this.itemDao = itemDao;
		this.locomotionTypeDao = locomotionTypeDao;
		this.outlookDao = outlookDao;
		this.parameterDao = parameterDao;
		this.professionDao = professionDao;
		this.raceDao = raceDao;
		this.realmDao = realmDao;
		this.sizeDao = sizeDao;
		this.skillDao = skillDao;
		this.skillCategoryDao = skillCategoryDao;
		this.specializationDao = specializationDao;
		this.spellDao = spellDao;
		this.spellListDao = spellListDao;
		this.spellListTypeDao = spellListTypeDao;
		this.spellSubTypeDao = spellSubTypeDao;
		this.spellTypeDao = spellTypeDao;
		this.statDao = statDao;
		this.talentDao = talentDao;
		this.talentCategoryDao = talentCategoryDao;
		this.helper = helper;
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
					@SuppressWarnings("UnusedAssignment")
					@Override
					public void call(Subscriber<? super Integer> subscriber) {
						SQLiteDatabase db = null;
						try {
							BufferedReader reader = new BufferedReader(new FileReader(fileName));
							final GsonBuilder gsonBuilder = new GsonBuilder();
							gsonBuilder.registerTypeAdapter(Attack.class, attackSerializer);
							gsonBuilder.registerTypeAdapter(Character.class, characterSerializer);
							gsonBuilder.registerTypeAdapter(CreatureArchetype.class, creatureArchetypeSerializer);
							gsonBuilder.registerTypeAdapter(CreatureCategory.class, creatureCategorySerializer);
							gsonBuilder.registerTypeAdapter(CreatureType.class, creatureTypeSerializer);
							gsonBuilder.registerTypeAdapter(CreatureVariety.class, creatureVarietySerializer);
							gsonBuilder.registerTypeAdapter(CriticalResult.class, criticalResultSerializer);
							gsonBuilder.registerTypeAdapter(Culture.class, cultureSerializer);
							gsonBuilder.registerTypeAdapter(DamageResult.class, damageResultSerializer);
							gsonBuilder.registerTypeAdapter(DamageResultRow.class, damageResultRowSerializer);
							gsonBuilder.registerTypeAdapter(DamageTable.class, damageTableSerializer);
							gsonBuilder.registerTypeAdapter(Profession.class, professionSerializer);
							gsonBuilder.registerTypeAdapter(Race.class, raceSerializer);
							gsonBuilder.registerTypeAdapter(Realm.class, realmSerializer);
							gsonBuilder.registerTypeAdapter(Skill.class, skillSerializer);
							gsonBuilder.registerTypeAdapter(SkillCategory.class, skillCategorySerializer);
							gsonBuilder.registerTypeAdapter(Specialization.class, specializationSerializer);
							gsonBuilder.registerTypeAdapter(Spell.class, spellSerializer);
							gsonBuilder.registerTypeAdapter(SpellList.class, spellListSerializer);
							gsonBuilder.registerTypeAdapter(Talent.class, talentSerializer);
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

							try {
								db = helper.getWritableDatabase();
								db.beginTransaction();
								helper.clearDatabase();

								List<Stat> stats = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									Stat stat = gson.fromJson(jsonReader, Stat.class);
									stats.add(stat);
								}
								jsonReader.endArray();
								statDao.save(stats, true);
								Log.d(LOG_TAG, "Loaded " + stats.size() + " stats.");
								stats = null;

								List<LocomotionType> locomotionTypes = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									LocomotionType locomotionType = gson.fromJson(jsonReader, LocomotionType.class);
									locomotionTypes.add(locomotionType);
								}
								jsonReader.endArray();
								locomotionTypeDao.save(locomotionTypes, true);
								Log.d(LOG_TAG, "Loaded " + locomotionTypes.size() + " locomotionTypes.");
								locomotionTypes = null;

								List<Parameter> parameters = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									Parameter parameter = gson.fromJson(jsonReader, Parameter.class);
									parameters.add(parameter);
								}
								jsonReader.endArray();
								parameterDao.save(parameters, true);
								Log.d(LOG_TAG, "Loaded " + parameters.size() + " parameters.");
								parameters = null;
								subscriber.onNext(10);

								List<Size> sizes = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									Size size = gson.fromJson(jsonReader, Size.class);
									sizes.add(size);
								}
								jsonReader.endArray();
								sizeDao.save(sizes, true);
								Log.d(LOG_TAG, "Loaded " + sizes.size() + " sizes.");
								sizes = null;

								List<SkillCategory> skillCategories = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									SkillCategory skillCategory = gson.fromJson(jsonReader, SkillCategory.class);
									skillCategories.add(skillCategory);
								}
								jsonReader.endArray();
								skillCategoryDao.save(skillCategories, true);
								Log.d(LOG_TAG, "Loaded " + skillCategories.size() + " skillCategories.");
								skillCategories = null;

								List<Skill> skills = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									Skill skill = gson.fromJson(jsonReader, Skill.class);
									skills.add(skill);
								}
								jsonReader.endArray();
								skillDao.save(skills, true);
								Log.d(LOG_TAG, "Loaded " + skills.size() + " skills.");
								skills = null;
								subscriber.onNext(20);

								List<TalentCategory> talentCategories = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									TalentCategory talentCategory = gson.fromJson(jsonReader, TalentCategory.class);
									talentCategories.add(talentCategory);
								}
								jsonReader.endArray();
								talentCategoryDao.save(talentCategories, true);
								Log.d(LOG_TAG, "Loaded " + talentCategories.size() + " talentCategories.");
								talentCategories = null;

								List<Talent> talents = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									Talent talent = gson.fromJson(jsonReader, Talent.class);
									talents.add(talent);
								}
								jsonReader.endArray();
								talentDao.save(talents, true);
								Log.d(LOG_TAG, "Loaded " + talents.size() + " talents.");
								talents = null;

								List<Item> items = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									Item item = gson.fromJson(jsonReader, Item.class);
									items.add(item);
								}
								jsonReader.endArray();
								itemDao.save(items, true);
								Log.d(LOG_TAG, "Loaded " + items.size() + " items.");
								items = null;

								List<BodyPart> bodyParts = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									BodyPart bodyPart = gson.fromJson(jsonReader, BodyPart.class);
									bodyParts.add(bodyPart);
								}
								jsonReader.endArray();
								bodyPartDao.save(bodyParts, true);
								Log.d(LOG_TAG, "Loaded " + bodyParts.size() + " bodyParts.");
								bodyParts = null;
								subscriber.onNext(30);

								List<CriticalCode> criticalCodes = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									CriticalCode criticalCode = gson.fromJson(jsonReader, CriticalCode.class);
									criticalCodes.add(criticalCode);
								}
								jsonReader.endArray();
								criticalCodeDao.save(criticalCodes, true);
								Log.d(LOG_TAG, "Loaded " + criticalCodes.size() + " criticalCodes.");
								criticalCodes = null;

								List<CriticalType> criticalTypes = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									CriticalType criticalType = gson.fromJson(jsonReader, CriticalType.class);
									criticalTypes.add(criticalType);
								}
								jsonReader.endArray();
								criticalTypeDao.save(criticalTypes, true);
								Log.d(LOG_TAG, "Loaded " + criticalTypes.size() + " criticalTypes.");
								criticalTypes = null;

								List<CriticalResult> criticalResults = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									CriticalResult criticalResult = gson.fromJson(jsonReader, CriticalResult.class);
									criticalResults.add(criticalResult);
								}
								jsonReader.endArray();
								criticalResultDao.save(criticalResults, true);
								Log.d(LOG_TAG, "Loaded " + criticalResults.size() + " criticalResults.");
								criticalResults = null;
								subscriber.onNext(40);

								List<DamageResult> damageResults = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									DamageResult damageResult = gson.fromJson(jsonReader, DamageResult.class);
									damageResults.add(damageResult);
								}
								damageResultSerializer = null;
								jsonReader.endArray();
								damageResultDao.save(damageResults, true);
								Log.d(LOG_TAG, "Loaded " + damageResults.size() + " damageResults.");
								damageResults = null;

								List<DamageTable> damageTables = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									DamageTable damageTable = gson.fromJson(jsonReader, DamageTable.class);
									damageTables.add(damageTable);
								}
								jsonReader.endArray();
								damageTableDao.save(damageTables, true);
								Log.d(LOG_TAG, "Loaded " + damageTables.size() + " damageTables.");
								damageTables = null;

								List<DamageResultRow> damageResultRows = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									DamageResultRow damageResultRow = gson.fromJson(jsonReader, DamageResultRow.class);
									damageResultRows.add(damageResultRow);
								}
								jsonReader.endArray();
								damageResultRowDao.save(damageResultRows, true);
								Log.d(LOG_TAG, "Loaded " + damageResultRows.size() + " damageResultRows.");
								damageResultRows = null;
								subscriber.onNext(50);

								List<CreatureCategory> creatureCategories = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									CreatureCategory creatureCategory = gson.fromJson(jsonReader, CreatureCategory.class);
									creatureCategories.add(creatureCategory);
								}
								jsonReader.endArray();
								creatureCategoryDao.save(creatureCategories, true);
								Log.d(LOG_TAG, "Loaded " + creatureCategories.size() + " creatureCategories.");
								creatureCategories = null;

								List<CreatureType> creatureTypes = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									CreatureType creatureType = gson.fromJson(jsonReader, CreatureType.class);
									creatureTypes.add(creatureType);
								}
								jsonReader.endArray();
								creatureTypeDao.save(creatureTypes, true);
								Log.d(LOG_TAG, "Loaded " + creatureTypes.size() + " creatureTypes.");
								creatureTypes = null;

								List<Outlook> outlooks = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									Outlook outlook = gson.fromJson(jsonReader, Outlook.class);
									outlooks.add(outlook);
								}
								jsonReader.endArray();
								outlookDao.save(outlooks, true);
								Log.d(LOG_TAG, "Loaded " + outlooks.size() + " outlooks.");
								outlooks = null;

								List<Realm> realms = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									Realm realm = gson.fromJson(jsonReader, Realm.class);
									realms.add(realm);
								}
								jsonReader.endArray();
								realmDao.save(realms, true);
								Log.d(LOG_TAG, "Loaded " + realms.size() + " realms.");
								realms = null;
								subscriber.onNext(60);

								List<Profession> professions = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									Profession profession = gson.fromJson(jsonReader, Profession.class);
									professions.add(profession);
								}
								jsonReader.endArray();
								professionDao.save(professions, true);
								Log.d(LOG_TAG, "Loaded " + professions.size() + " professions.");
								professions = null;

								List<Culture> cultures = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									Culture culture = gson.fromJson(jsonReader, Culture.class);
									cultures.add(culture);
								}
								jsonReader.endArray();
								cultureDao.save(cultures, true);
								Log.d(LOG_TAG, "Loaded " + cultures.size() + " cultures.");
								cultures = null;

								List<Race> races = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									Race race = gson.fromJson(jsonReader, Race.class);
									races.add(race);
								}
								jsonReader.endArray();
								raceDao.save(races, true);
								Log.d(LOG_TAG, "Loaded " + races.size() + " races.");
								races = null;
								subscriber.onNext(70);

								List<Specialization> specializations = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									Specialization specialization = gson.fromJson(jsonReader, Specialization.class);
									specializations.add(specialization);
								}
								jsonReader.endArray();
								specializationDao.save(specializations, true);
								Log.d(LOG_TAG, "Loaded " + specializations.size() + " specializations.");
								specializations = null;

								List<Attack> attacks = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									Attack attack = gson.fromJson(jsonReader, Attack.class);
									attacks.add(attack);
								}
								jsonReader.endArray();
								attackDao.save(attacks, true);
								Log.d(LOG_TAG, "Loaded " + attacks.size() + " attacks.");
								attacks = null;

								List<CreatureArchetype> creatureArchetypes = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									CreatureArchetype creatureArchetype = gson.fromJson(jsonReader, CreatureArchetype.class);
									creatureArchetypes.add(creatureArchetype);
								}
								jsonReader.endArray();
								creatureArchetypeDao.save(creatureArchetypes, true);
								Log.d(LOG_TAG, "Loaded " + creatureArchetypes.size() + " creatureArchetypes.");
								creatureArchetypes = null;

								List<SpellListType> spellListTypes = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									SpellListType spellListType = gson.fromJson(jsonReader, SpellListType.class);
									spellListTypes.add(spellListType);
								}
								jsonReader.endArray();
								spellListTypeDao.save(spellListTypes, true);
								Log.d(LOG_TAG, "Loaded " + spellListTypes.size() + " spellListTypes.");
								spellListTypes = null;
								subscriber.onNext(80);

								List<SpellSubType> spellSubTypes = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									SpellSubType spellSubType = gson.fromJson(jsonReader, SpellSubType.class);
									spellSubTypes.add(spellSubType);
								}
								jsonReader.endArray();
								spellSubTypeDao.save(spellSubTypes, true);
								Log.d(LOG_TAG, "Loaded " + spellSubTypes.size() + " spellSubTypes.");
								spellSubTypes = null;

								List<SpellType> spellTypes = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									SpellType spellType = gson.fromJson(jsonReader, SpellType.class);
									spellTypes.add(spellType);
								}
								jsonReader.endArray();
								spellTypeDao.save(spellTypes, true);
								Log.d(LOG_TAG, "Loaded " + spellTypes.size() + " spellTypes.");
								spellTypes = null;

								List<SpellList> spellLists = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									SpellList spellList = gson.fromJson(jsonReader, SpellList.class);
									spellLists.add(spellList);
								}
								jsonReader.endArray();
								spellListDao.save(spellLists, true);
								Log.d(LOG_TAG, "Loaded " + spellLists.size() + " spellLists.");
								spellLists = null;
								subscriber.onNext(90);

								List<Spell> spells = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									Spell spell = gson.fromJson(jsonReader, Spell.class);
									spells.add(spell);
								}
								jsonReader.endArray();
								spellDao.save(spells, true);
								Log.d(LOG_TAG, "Loaded " + spells.size() + " spells.");
								spells = null;

								List<Character> characters = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									Character character = gson.fromJson(jsonReader, Character.class);
									characters.add(character);
								}
								jsonReader.endArray();
								characterDao.save(characters, true);
								Log.d(LOG_TAG, "characters = " + characters);
								characters = null;

								List<CreatureVariety> creatureVarieties = new ArrayList<>();
								jsonReader.beginArray();
								while (jsonReader.hasNext()) {
									CreatureVariety creatureVariety = gson.fromJson(jsonReader, CreatureVariety.class);
									creatureVarieties.add(creatureVariety);
								}
								jsonReader.endArray();
								creatureVarietyDao.save(creatureVarieties, true);
								Log.d(LOG_TAG, "creatureVarieties = " + creatureVarieties);
								creatureVarieties = null;
								db.setTransactionSuccessful();
							}
							catch(Exception e) {
								Log.e(LOG_TAG, "Caught exception", e);
								throw new RMUAppException(e);
							}
							finally {
								if(db != null) {
									db.endTransaction();
								}
							}
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
							gsonBuilder.registerTypeAdapter(CreatureCategory.class, creatureCategorySerializer);
							gsonBuilder.registerTypeAdapter(CreatureType.class, creatureTypeSerializer);
							gsonBuilder.registerTypeAdapter(CreatureVariety.class, creatureVarietySerializer);
							gsonBuilder.registerTypeAdapter(CriticalResult.class, criticalResultSerializer);
							gsonBuilder.registerTypeAdapter(Culture.class, cultureSerializer);
							try {
								gsonBuilder.registerTypeAdapter(DamageResult.class, damageResultSerializer);
							}
							catch (IllegalArgumentException e) {
								Log.e(LOG_TAG, "Exception caught registering TypeAdapter " + damageResultSerializer, e);
							}
							gsonBuilder.registerTypeAdapter(DamageResultRow.class, damageResultRowSerializer);
							gsonBuilder.registerTypeAdapter(DamageTable.class, damageTableSerializer);
							gsonBuilder.registerTypeAdapter(Profession.class, professionSerializer);
							gsonBuilder.registerTypeAdapter(Race.class, raceSerializer);
							gsonBuilder.registerTypeAdapter(Realm.class, realmSerializer);
							gsonBuilder.registerTypeAdapter(Skill.class, skillSerializer);
							gsonBuilder.registerTypeAdapter(SkillCategory.class, skillCategorySerializer);
							gsonBuilder.registerTypeAdapter(Specialization.class, specializationSerializer);
							gsonBuilder.registerTypeAdapter(Spell.class, spellSerializer);
							gsonBuilder.registerTypeAdapter(SpellList.class, spellListSerializer);
							gsonBuilder.registerTypeAdapter(Talent.class, talentSerializer);
							final Gson gson = gsonBuilder.create();

							JsonWriter jsonWriter = gson.newJsonWriter(writer);
							jsonWriter.beginObject()
									.name(VERSION)
									.value(RMUDatabaseHelper.DATABASE_VERSION)
									.endObject()
									.flush();
							gson.toJson(statDao.getAll(), writer);
							gson.toJson(locomotionTypeDao.getAll(), writer);
							gson.toJson(parameterDao.getAll(), writer);
							subscriber.onNext(10);
							gson.toJson(sizeDao.getAll(), writer);
							gson.toJson(skillCategoryDao.getAll(), writer);
							gson.toJson(skillDao.getAll(), writer);
							subscriber.onNext(20);
							gson.toJson(talentCategoryDao.getAll(), writer);
							gson.toJson(talentDao.getAll(), writer);
							gson.toJson(itemDao.getAll(), writer);
							gson.toJson(bodyPartDao.getAll(), writer);
							subscriber.onNext(30);
							gson.toJson(criticalCodeDao.getAll(), writer);
							gson.toJson(criticalTypeDao.getAll(), writer);
							gson.toJson(criticalResultDao.getAll(), writer);
							subscriber.onNext(40);
							gson.toJson(damageResultDao.getAll(), writer);
							gson.toJson(damageTableDao.getAll(), writer);
							gson.toJson(damageResultRowDao.getAll(), writer);
							subscriber.onNext(50);
							gson.toJson(creatureCategoryDao.getAll(), writer);
							gson.toJson(creatureTypeDao.getAll(), writer);
							gson.toJson(outlookDao.getAll(), writer);
							gson.toJson(realmDao.getAll(), writer);
							subscriber.onNext(60);
							gson.toJson(professionDao.getAll(), writer);
							gson.toJson(cultureDao.getAll(), writer);
							gson.toJson(raceDao.getAll(), writer);
							subscriber.onNext(70);
							gson.toJson(specializationDao.getAll(), writer);
							gson.toJson(attackDao.getAll(), writer);
							gson.toJson(creatureArchetypeDao.getAll(), writer);
							gson.toJson(spellListTypeDao.getAll(), writer);
							subscriber.onNext(80);
							gson.toJson(spellSubTypeDao.getAll(), writer);
							gson.toJson(spellTypeDao.getAll(), writer);
							gson.toJson(spellListDao.getAll(), writer);
							subscriber.onNext(90);
							gson.toJson(spellDao.getAll(), writer);
							gson.toJson(characterDao.getAll(), writer);
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
