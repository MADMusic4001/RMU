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
package com.madinnovations.rmu.controller.rxhandler.campaign;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SizeRxHandler;
import com.madinnovations.rmu.data.dao.RMUDatabaseHelper;
import com.madinnovations.rmu.data.dao.character.CultureDao;
import com.madinnovations.rmu.data.dao.character.ProfessionDao;
import com.madinnovations.rmu.data.dao.character.RaceDao;
import com.madinnovations.rmu.data.dao.character.serializers.CharacterSerializer;
import com.madinnovations.rmu.data.dao.character.serializers.CultureSerializer;
import com.madinnovations.rmu.data.dao.character.serializers.ProfessionSerializer;
import com.madinnovations.rmu.data.dao.character.serializers.RaceSerializer;
import com.madinnovations.rmu.data.dao.combat.AttackDao;
import com.madinnovations.rmu.data.dao.combat.BodyPartDao;
import com.madinnovations.rmu.data.dao.combat.CriticalResultDao;
import com.madinnovations.rmu.data.dao.combat.CriticalTypeDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultRowDao;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.combat.DiseaseDao;
import com.madinnovations.rmu.data.dao.combat.serializers.AttackSerializer;
import com.madinnovations.rmu.data.dao.combat.serializers.CriticalResultSerializer;
import com.madinnovations.rmu.data.dao.combat.serializers.DamageResultRowSerializer;
import com.madinnovations.rmu.data.dao.combat.serializers.DamageResultSerializer;
import com.madinnovations.rmu.data.dao.combat.serializers.DamageTableSerializer;
import com.madinnovations.rmu.data.dao.common.SizeDao;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
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
import com.madinnovations.rmu.data.dao.item.ItemTemplateDao;
import com.madinnovations.rmu.data.dao.item.serializers.ItemTemplateSerializer;
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.dao.spells.SpellDao;
import com.madinnovations.rmu.data.dao.spells.SpellListDao;
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
import com.madinnovations.rmu.data.entities.combat.CriticalResult;
import com.madinnovations.rmu.data.entities.combat.CriticalType;
import com.madinnovations.rmu.data.entities.combat.DamageResult;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.combat.Disease;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;
import com.madinnovations.rmu.data.entities.creature.CreatureCategory;
import com.madinnovations.rmu.data.entities.creature.CreatureType;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;
import com.madinnovations.rmu.data.entities.creature.Outlook;
import com.madinnovations.rmu.data.entities.object.ArmorTemplate;
import com.madinnovations.rmu.data.entities.object.HerbTemplate;
import com.madinnovations.rmu.data.entities.object.ItemTemplate;
import com.madinnovations.rmu.data.entities.object.PoisonTemplate;
import com.madinnovations.rmu.data.entities.object.SubstanceTemplate;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;
import com.madinnovations.rmu.data.entities.spells.RealmDBO;
import com.madinnovations.rmu.data.entities.spells.Spell;
import com.madinnovations.rmu.data.entities.spells.SpellList;
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
	private static final int NUM_TYPES = 31;
	private static final String TAG = "ImportExportRxHandler";
	private static final String VERSION = "version";
	private AttackDao                   attackDao;
	private AttackSerializer            attackSerializer = new AttackSerializer();
	private BodyPartDao                 bodyPartDao;
	private CharacterSerializer         characterSerializer = new CharacterSerializer();
	private CreatureArchetypeDao        creatureArchetypeDao;
	private CreatureArchetypeSerializer creatureArchetypeSerializer = new CreatureArchetypeSerializer();
	private CreatureCategoryDao         creatureCategoryDao;
	private CreatureCategorySerializer  creatureCategorySerializer = new CreatureCategorySerializer();
	private CreatureTypeDao             creatureTypeDao;
	private CreatureTypeSerializer      creatureTypeSerializer = new CreatureTypeSerializer();
	private CreatureVarietyDao          creatureVarietyDao;
	private CreatureVarietySerializer   creatureVarietySerializer = new CreatureVarietySerializer();
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
	private DiseaseDao                  diseaseDao;
	private ItemTemplateDao             itemTemplateDao;
	private ItemTemplateSerializer      itemTemplateSerializer = new ItemTemplateSerializer();
	private OutlookDao                  outlookDao;
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
	private SpellSubTypeDao             spellSubTypeDao;
	private SpellTypeDao                spellTypeDao;
	private TalentDao                   talentDao;
	private TalentSerializer            talentSerializer = new TalentSerializer();
	private TalentCategoryDao           talentCategoryDao;
	private RMUDatabaseHelper helper;

	/**
	 * Creates a new ImportExportRxHandler instance
	 */
	@Inject
	ImportExportRxHandler(AttackDao attackDao, BodyPartDao bodyPartDao,  CreatureArchetypeDao creatureArchetypeDao,
						  CreatureCategoryDao creatureCategoryDao, CreatureTypeDao creatureTypeDao,
						  CreatureVarietyDao creatureVarietyDao, CriticalResultDao criticalResultDao,
						  CriticalTypeDao criticalTypeDao, CultureDao cultureDao, DamageResultDao damageResultDao,
						  DamageResultRowDao damageResultRowDao, DamageTableDao damageTableDao, DiseaseDao diseaseDao,
						  ItemTemplateDao itemTemplateDao, OutlookDao outlookDao, ProfessionDao professionDao, RaceDao raceDao,
						  RealmDao realmDao, SizeDao sizeDao, SkillDao skillDao, SkillCategoryDao skillCategoryDao,
						  SpecializationDao specializationDao, SpellDao spellDao, SpellListDao spellListDao,
						  SpellSubTypeDao spellSubTypeDao, SpellTypeDao spellTypeDao, TalentDao talentDao,
						  TalentCategoryDao talentCategoryDao, RMUDatabaseHelper helper, AttackRxHandler attackRxHandler,
						  SizeRxHandler sizeRxHandler) {
		this.attackDao = attackDao;
		this.bodyPartDao = bodyPartDao;
		this.creatureArchetypeDao = creatureArchetypeDao;
		this.creatureCategoryDao = creatureCategoryDao;
		this.creatureTypeDao = creatureTypeDao;
		this.creatureVarietyDao = creatureVarietyDao;
		this.criticalResultDao = criticalResultDao;
		this.criticalTypeDao = criticalTypeDao;
		this.cultureDao = cultureDao;
		this.damageResultDao = damageResultDao;
		this.damageResultRowDao = damageResultRowDao;
		this.damageTableDao = damageTableDao;
		this.diseaseDao = diseaseDao;
		this.itemTemplateDao = itemTemplateDao;
		this.outlookDao = outlookDao;
		this.professionDao = professionDao;
		this.raceDao = raceDao;
		this.realmDao = realmDao;
		this.sizeDao = sizeDao;
		this.skillDao = skillDao;
		this.skillCategoryDao = skillCategoryDao;
		this.specializationDao = specializationDao;
		this.spellDao = spellDao;
		this.spellListDao = spellListDao;
		this.spellSubTypeDao = spellSubTypeDao;
		this.spellTypeDao = spellTypeDao;
		this.talentDao = talentDao;
		this.talentCategoryDao = talentCategoryDao;
		this.helper = helper;
		this.creatureVarietySerializer.setAttackRxHandler(attackRxHandler);
		this.creatureVarietySerializer.setSizeRxHandler(sizeRxHandler);
	}

	/**
	 * Creates an Observable that when subscribed to will import the RMU database from the file with the given file name. The 
	 * file
	 * name must be a fully qualified path.
	 *
	 * @param fileName the name of the file to read.
	 * @return an {@link Observable} instance that can be subscribed to in order to read the file.
	 */
	public Observable<Integer> importDatabase(@NonNull final String fileName)
	{
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
							gsonBuilder.registerTypeAdapter(ItemTemplate.class, itemTemplateSerializer);
							gsonBuilder.registerTypeAdapter(Profession.class, professionSerializer);
							gsonBuilder.registerTypeAdapter(Race.class, raceSerializer);
							gsonBuilder.registerTypeAdapter(RealmDBO.class, realmSerializer);
							gsonBuilder.registerTypeAdapter(Skill.class, skillSerializer);
							gsonBuilder.registerTypeAdapter(SkillCategory.class, skillCategorySerializer);
							gsonBuilder.registerTypeAdapter(Specialization.class, specializationSerializer);
							gsonBuilder.registerTypeAdapter(Spell.class, spellSerializer);
							gsonBuilder.registerTypeAdapter(SpellList.class, spellListSerializer);
							gsonBuilder.registerTypeAdapter(Talent.class, talentSerializer);
							gsonBuilder.registerTypeAdapter(WeaponTemplate.class, itemTemplateSerializer);
							gsonBuilder.setLenient();
							final Gson gson = gsonBuilder.create();

							JsonReader jsonReader = gson.newJsonReader(reader);
							jsonReader.beginObject();
							String name = jsonReader.nextName();
							if (!name.equals(VERSION)) {
								throw new RMUAppException("Input file does not begin with '" + VERSION + "'");
							}
							int version = jsonReader.nextInt();
//							if (version != RMUDatabaseHelper.DATABASE_VERSION) {
//								throw new RMUAppException("Input file version mismatch. Expected " +
//																  RMUDatabaseHelper.DATABASE_VERSION + " but found " + version);
//							}

							try {
								db = helper.getWritableDatabase();
								db.setForeignKeyConstraintsEnabled(false);
								db.beginTransaction();
								helper.clearDatabase();
								int numTypesRead = 0;
								while (jsonReader.hasNext()) {
									switch (jsonReader.nextName()) {
										case Disease.JSON_NAME:
											List<Disease> diseases = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												Disease disease = gson.fromJson(jsonReader, Disease.class);
												diseases.add(disease);
											}
											jsonReader.endArray();
											diseaseDao.save(diseases, true);
											diseases = null;
											break;
										case Size.JSON_NAME:
											List<Size> sizes = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												Size size = gson.fromJson(jsonReader, Size.class);
												sizes.add(size);
											}
											jsonReader.endArray();
											sizeDao.save(sizes, true);
											sizes = null;
											break;
										case SkillCategory.JSON_NAME:
											List<SkillCategory> skillCategories = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												SkillCategory skillCategory = gson.fromJson(jsonReader, SkillCategory.class);
												skillCategories.add(skillCategory);
											}
											jsonReader.endArray();
											skillCategoryDao.save(skillCategories, true);
											Log.i(TAG, "Loaded " + skillCategories.size() + " skillCategories.");
											skillCategories = null;
											break;
										case Skill.JSON_NAME:
											List<Skill> skills = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												Skill skill = gson.fromJson(jsonReader, Skill.class);
												skills.add(skill);
											}
											jsonReader.endArray();
											skillDao.save(skills, true);
											Log.i(TAG, "Loaded " + skills.size() + " skills.");
											skills = null;
											break;
										case TalentCategory.JSON_NAME:
											List<TalentCategory> talentCategories = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												TalentCategory talentCategory = gson.fromJson(jsonReader, TalentCategory.class);
												talentCategories.add(talentCategory);
											}
											jsonReader.endArray();
											talentCategoryDao.save(talentCategories, true);
											Log.i(TAG, "Loaded " + talentCategories.size() + " talentCategories.");
											talentCategories = null;
											break;
										case Talent.JSON_NAME:
											List<Talent> talents = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												Talent talent = gson.fromJson(jsonReader, Talent.class);
												talents.add(talent);
											}
											jsonReader.endArray();
											talentDao.save(talents, true);
											Log.i(TAG, "Loaded " + talents.size() + " talents.");
											talents = null;
											break;
										case ItemTemplate.JSON_NAME:
											List<ItemTemplate> itemTemplates = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												ItemTemplate itemTemplate = gson.fromJson(jsonReader, ItemTemplate.class);
												itemTemplates.add(itemTemplate);
											}
											jsonReader.endArray();
											itemTemplateDao.save(itemTemplates, true);
											Log.i(TAG, "Loaded " + itemTemplates.size() + " itemTemplates.");
											itemTemplates = null;
											break;
										case BodyPart.JSON_NAME:
											List<BodyPart> bodyParts = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												BodyPart bodyPart = gson.fromJson(jsonReader, BodyPart.class);
												bodyParts.add(bodyPart);
											}
											jsonReader.endArray();
											bodyPartDao.save(bodyParts, true);
											Log.i(TAG, "Loaded " + bodyParts.size() + " bodyParts.");
											bodyParts = null;
											break;
										case CriticalType.JSON_NAME:
											List<CriticalType> criticalTypes = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												CriticalType criticalType = gson.fromJson(jsonReader, CriticalType.class);
												criticalTypes.add(criticalType);
											}
											jsonReader.endArray();
											criticalTypeDao.save(criticalTypes, true);
											Log.i(TAG, "Loaded " + criticalTypes.size() + " criticalTypes.");
											criticalTypes = null;
											break;
										case CriticalResult.JSON_NAME:
											List<CriticalResult> criticalResults = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												CriticalResult criticalResult = gson.fromJson(jsonReader, CriticalResult.class);
												criticalResults.add(criticalResult);
											}
											jsonReader.endArray();
											criticalResultDao.save(criticalResults, true);
											Log.i(TAG, "Loaded " + criticalResults.size() + " criticalResults.");
											criticalResults = null;
											break;
										case DamageTable.JSON_NAME:
											List<DamageTable> damageTables = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												DamageTable damageTable = gson.fromJson(jsonReader, DamageTable.class);
												damageTables.add(damageTable);
											}
											jsonReader.endArray();
											damageTableDao.save(damageTables, true);
											Log.i(TAG, "Loaded " + damageTables.size() + " damageTables.");
											damageTables = null;
											break;
										case DamageResultRow.JSON_NAME:
											List<DamageResultRow> damageResultRows = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												DamageResultRow damageResultRow = gson.fromJson(jsonReader, DamageResultRow.class);
												damageResultRows.add(damageResultRow);
											}
											jsonReader.endArray();
											damageResultRowDao.save(damageResultRows, true);
											Log.i(TAG, "Loaded " + damageResultRows.size() + " damageResultRows.");
											damageResultRows = null;
											break;
										case DamageResult.JSON_NAME:
											List<DamageResult> damageResults = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												DamageResult damageResult = gson.fromJson(jsonReader, DamageResult.class);
												damageResults.add(damageResult);
											}
											jsonReader.endArray();
											damageResultDao.save(damageResults, true);
											Log.i(TAG, "Loaded " + damageResults.size() + " damageResults.");
											damageResults = null;
											break;
										case CreatureCategory.JSON_NAME:
											List<CreatureCategory> creatureCategories = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												CreatureCategory creatureCategory = gson.fromJson(jsonReader, CreatureCategory.class);
												creatureCategories.add(creatureCategory);
											}
											jsonReader.endArray();
											creatureCategoryDao.save(creatureCategories, true);
											Log.i(TAG, "Loaded " + creatureCategories.size() + " creatureCategories.");
											creatureCategories = null;
											break;
										case CreatureType.JSON_NAME:
											List<CreatureType> creatureTypes = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												CreatureType creatureType = gson.fromJson(jsonReader, CreatureType.class);
												creatureTypes.add(creatureType);
											}
											jsonReader.endArray();
											creatureTypeDao.save(creatureTypes, true);
											Log.i(TAG, "Loaded " + creatureTypes.size() + " creatureTypes.");
											creatureTypes = null;
											break;
										case Outlook.JSON_NAME:
											List<Outlook> outlooks = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												Outlook outlook = gson.fromJson(jsonReader, Outlook.class);
												outlooks.add(outlook);
											}
											jsonReader.endArray();
											outlookDao.save(outlooks, true);
											Log.i(TAG, "Loaded " + outlooks.size() + " outlooks.");
											outlooks = null;
											break;
										case RealmDBO.JSON_NAME:
											List<RealmDBO> realmDBOs = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												RealmDBO realmDBO = gson.fromJson(jsonReader, RealmDBO.class);
												realmDBOs.add(realmDBO);
											}
											jsonReader.endArray();
											realmDao.save(realmDBOs, true);
											Log.i(TAG, "Loaded " + realmDBOs.size() + " realms.");
											realmDBOs = null;
											break;
										case Profession.JSON_NAME:
											List<Profession> professions = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												Profession profession = gson.fromJson(jsonReader, Profession.class);
												professions.add(profession);
											}
											jsonReader.endArray();
											professionDao.save(professions, true);
											Log.i(TAG, "Loaded " + professions.size() + " professions.");
											professions = null;
											break;
										case Culture.JSON_NAME:
											List<Culture> cultures = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												Culture culture = gson.fromJson(jsonReader, Culture.class);
												cultures.add(culture);
											}
											jsonReader.endArray();
											cultureDao.save(cultures, true);
											Log.i(TAG, "Loaded " + cultures.size() + " cultures.");
											cultures = null;
											break;
										case Race.JSON_NAME:
											List<Race> races = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												Race race = gson.fromJson(jsonReader, Race.class);
												races.add(race);
											}
											jsonReader.endArray();
											raceDao.save(races, true);
											Log.i(TAG, "Loaded " + races.size() + " races.");
											races = null;
											break;
										case Specialization.JSON_NAME:
											List<Specialization> specializations = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												Specialization specialization = gson.fromJson(jsonReader, Specialization.class);
												specializations.add(specialization);
											}
											jsonReader.endArray();
											specializationDao.save(specializations, true);
											Log.i(TAG, "Loaded " + specializations.size() + " specializations.");
											specializations = null;
											break;
										case Attack.JSON_NAME:
											List<Attack> attacks = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												Attack attack = gson.fromJson(jsonReader, Attack.class);
												attacks.add(attack);
											}
											jsonReader.endArray();
											attackDao.save(attacks, true);
											Log.i(TAG, "Loaded " + attacks.size() + " attacks.");
											attacks = null;
											break;
										case CreatureArchetype.JSON_NAME:
											List<CreatureArchetype> creatureArchetypes = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												CreatureArchetype creatureArchetype = gson.fromJson(jsonReader, CreatureArchetype.class);
												creatureArchetypes.add(creatureArchetype);
											}
											jsonReader.endArray();
											creatureArchetypeDao.save(creatureArchetypes, true);
											Log.i(TAG, "Loaded " + creatureArchetypes.size() + " creatureArchetypes.");
											creatureArchetypes = null;
											break;
										case SpellSubType.JSON_NAME:
											List<SpellSubType> spellSubTypes = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												SpellSubType spellSubType = gson.fromJson(jsonReader, SpellSubType.class);
												spellSubTypes.add(spellSubType);
											}
											jsonReader.endArray();
											spellSubTypeDao.save(spellSubTypes, true);
											Log.i(TAG, "Loaded " + spellSubTypes.size() + " spellSubTypes.");
											spellSubTypes = null;
											break;
										case SpellType.JSON_NAME:
											List<SpellType> spellTypes = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												SpellType spellType = gson.fromJson(jsonReader, SpellType.class);
												spellTypes.add(spellType);
											}
											jsonReader.endArray();
											spellTypeDao.save(spellTypes, true);
											Log.i(TAG, "Loaded " + spellTypes.size() + " spellTypes.");
											spellTypes = null;
											break;
										case SpellList.JSON_NAME:
											List<SpellList> spellLists = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												SpellList spellList = gson.fromJson(jsonReader, SpellList.class);
												spellLists.add(spellList);
											}
											jsonReader.endArray();
											spellListDao.save(spellLists, true);
											Log.i(TAG, "Loaded " + spellLists.size() + " spellLists.");
											spellLists = null;
											break;
										case Spell.JSON_NAME:
											List<Spell> spells = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												Spell spell = gson.fromJson(jsonReader, Spell.class);
												spells.add(spell);
											}
											jsonReader.endArray();
											spellDao.save(spells, true);
											Log.i(TAG, "Loaded " + spells.size() + " spells.");
											spells = null;
											break;
										case CreatureVariety.JSON_NAME:
											List<CreatureVariety> creatureVarieties = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												CreatureVariety creatureVariety = gson.fromJson(jsonReader, CreatureVariety.class);
												creatureVarieties.add(creatureVariety);
											}
											jsonReader.endArray();
											creatureVarietyDao.save(creatureVarieties, true);
											Log.i(TAG, "Loaded " + creatureVarieties.size() + " creatureVarieties.");
											creatureVarieties = null;
											break;
									}
									if((numTypesRead*10/NUM_TYPES) % 10 < (++numTypesRead*10/NUM_TYPES) % 10) {
										subscriber.onNext((numTypesRead*10/NUM_TYPES % 10) * 10);
									}
								}
								jsonReader.endObject();
								db.setTransactionSuccessful();
							}
							catch(Exception e) {
								Log.e(TAG, "Caught exception", e);
								throw new RMUAppException(e);
							}
							finally {
								if(db != null) {
									db.endTransaction();
									db.setForeignKeyConstraintsEnabled(true);
								}
							}

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
							gsonBuilder.registerTypeAdapter(CreatureArchetype.class, creatureArchetypeSerializer);
							gsonBuilder.registerTypeAdapter(CreatureCategory.class, creatureCategorySerializer);
							gsonBuilder.registerTypeAdapter(CreatureType.class, creatureTypeSerializer);
							gsonBuilder.registerTypeAdapter(CreatureVariety.class, creatureVarietySerializer);
							gsonBuilder.registerTypeAdapter(CriticalResult.class, criticalResultSerializer);
							gsonBuilder.registerTypeAdapter(Culture.class, cultureSerializer);
							gsonBuilder.registerTypeAdapter(DamageResult.class, damageResultSerializer);
							gsonBuilder.registerTypeAdapter(DamageResultRow.class, damageResultRowSerializer);
							gsonBuilder.registerTypeAdapter(DamageTable.class, damageTableSerializer);
							gsonBuilder.registerTypeAdapter(ItemTemplate.class, itemTemplateSerializer);
							gsonBuilder.registerTypeAdapter(ArmorTemplate.class, itemTemplateSerializer);
							gsonBuilder.registerTypeAdapter(HerbTemplate.class, itemTemplateSerializer);
							gsonBuilder.registerTypeAdapter(PoisonTemplate.class, itemTemplateSerializer);
							gsonBuilder.registerTypeAdapter(SubstanceTemplate.class, itemTemplateSerializer);
							gsonBuilder.registerTypeAdapter(WeaponTemplate.class, itemTemplateSerializer);
							gsonBuilder.registerTypeAdapter(Profession.class, professionSerializer);
							gsonBuilder.registerTypeAdapter(Race.class, raceSerializer);
							gsonBuilder.registerTypeAdapter(RealmDBO.class, realmSerializer);
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
									.name(Disease.JSON_NAME)
									.jsonValue(gson.toJson(diseaseDao.getAll()))
									.name(Size.JSON_NAME)
									.jsonValue(gson.toJson(sizeDao.getAll()))
									.name(SkillCategory.JSON_NAME)
									.jsonValue(gson.toJson(skillCategoryDao.getAll()))
							;
							subscriber.onNext(10);
							jsonWriter
									.name(Skill.JSON_NAME)
									.jsonValue(gson.toJson(skillDao.getAll()))
									.name(TalentCategory.JSON_NAME)
									.jsonValue(gson.toJson(talentCategoryDao.getAll()))
									.name(Talent.JSON_NAME)
									.jsonValue(gson.toJson(talentDao.getAll()))
							;
							subscriber.onNext(20);
							jsonWriter
									.name(BodyPart.JSON_NAME)
									.jsonValue(gson.toJson(bodyPartDao.getAll()))
							;
							subscriber.onNext(30);
							jsonWriter
									.name(CriticalType.JSON_NAME)
									.jsonValue(gson.toJson(criticalTypeDao.getAll()))
									.name(CriticalResult.JSON_NAME)
									.jsonValue(gson.toJson(criticalResultDao.getAll()))
									.name(DamageTable.JSON_NAME)
									.jsonValue(gson.toJson(damageTableDao.getAll()))
							;
							subscriber.onNext(40);
							jsonWriter
									.name(DamageResultRow.JSON_NAME)
									.jsonValue(gson.toJson(damageResultRowDao.getAll()))
									.name(DamageResult.JSON_NAME)
									.jsonValue(gson.toJson(damageResultDao.getAll()))
									.name(CreatureCategory.JSON_NAME)
									.jsonValue(gson.toJson(creatureCategoryDao.getAll()))
							;
							subscriber.onNext(50);
							jsonWriter
									.name(CreatureType.JSON_NAME)
									.jsonValue(gson.toJson(creatureTypeDao.getAll()))
									.name(Outlook.JSON_NAME)
									.jsonValue(gson.toJson(outlookDao.getAll()))
									.name(RealmDBO.JSON_NAME)
									.jsonValue(gson.toJson(realmDao.getAll()))
							;
							subscriber.onNext(60);
							jsonWriter
									.name(Profession.JSON_NAME)
									.jsonValue(gson.toJson(professionDao.getAll()))
									.name(Culture.JSON_NAME)
									.jsonValue(gson.toJson(cultureDao.getAll()))
									.name(Race.JSON_NAME)
									.jsonValue(gson.toJson(raceDao.getAll()))
							;
							subscriber.onNext(70);
							jsonWriter
									.name(Specialization.JSON_NAME)
									.jsonValue(gson.toJson(specializationDao.getAll()))
									.name(ItemTemplate.JSON_NAME)
									.jsonValue(gson.toJson(itemTemplateDao.getAll()))
									.name(Attack.JSON_NAME)
									.jsonValue(gson.toJson(attackDao.getAll()))
									.name(CreatureArchetype.JSON_NAME)
									.jsonValue(gson.toJson(creatureArchetypeDao.getAll()))
							;
							subscriber.onNext(80);
							jsonWriter
									.name(SpellSubType.JSON_NAME)
									.jsonValue(gson.toJson(spellSubTypeDao.getAll()))
									.name(SpellType.JSON_NAME)
									.jsonValue(gson.toJson(spellTypeDao.getAll()))
									.name(SpellList.JSON_NAME)
									.jsonValue(gson.toJson(spellListDao.getAll()))
							;
							subscriber.onNext(90);
							jsonWriter
									.name(Spell.JSON_NAME)
									.jsonValue(gson.toJson(spellDao.getAll()))
									.name(CreatureVariety.JSON_NAME)
									.jsonValue(gson.toJson(creatureVarietyDao.getAll()))
									.endObject()
									.flush();
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
