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
package com.madinnovations.rmu.controller.rxhandler.common;

import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.dao.spells.SpellListDao;
import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.spells.SpellList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Skill} instances with persistent storage.
 */
public class SkillRxHandler {
	private SkillDao dao;
	private SkillCategoryDao skillCategoryDao;
	private SpecializationDao specializationDao;
	private SpellListDao spellListDao;

	/**
	 * Creates a new SkillRxHandler
	 *
	 * @param dao  a SkillDao instance
	 * @param skillCategoryDao  a {@link SkillCategoryDao} instance
	 * @param specializationDao  a {@link SpecializationDao} instance
	 * @param spellListDao  a {@link SpellListDao} instance
	 */
	@Inject
	public SkillRxHandler(SkillDao dao, SkillCategoryDao skillCategoryDao, SpecializationDao specializationDao,
						  SpellListDao spellListDao) {
		this.dao = dao;
		this.skillCategoryDao = skillCategoryDao;
		this.specializationDao = specializationDao;
		this.spellListDao = spellListDao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Skill instance with the given id.
	 *
	 * @param id  the id of the Skill to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Skill instance.
	 */
	public Observable<Skill> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Skill>() {
					@Override
					public void call(Subscriber<? super Skill> subscriber) {
						try {
							Skill instance = dao.getById(id);
							if(instance.isRequiresSpecialization()) {
								instance.setSpecializations(specializationDao.getSpecializationsForSkill(instance));
							}
							subscriber.onNext(instance);
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Skill instance with the given name.
	 *
	 * @param name  the name of the Skill to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Skill instance.
	 */
	public Observable<Skill> getByName(final String name) {
		return Observable.create(
				new Observable.OnSubscribe<Skill>() {
					@Override
					public void call(Subscriber<? super Skill> subscriber) {
						try {
							Skill instance = dao.getByName(name);
							if(instance.isRequiresSpecialization()) {
								instance.setSpecializations(specializationDao.getSpecializationsForSkill(instance));
							}
							subscriber.onNext(instance);
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Skill instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Skill
	 * instances.
	 */
	public Observable<Collection<Skill>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Skill>>() {
					@Override
					public void call(Subscriber<? super Collection<Skill>> subscriber) {
						try {
							Collection<Skill> skills = dao.getAll();
							for(Skill skill : skills) {
								if(skill.isRequiresSpecialization()) {
									skill.setSpecializations(specializationDao.getSpecializationsForSkill(skill));
								}
							}
							subscriber.onNext(skills);
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will save a Skill instance to persistent storage.
	 *
	 * @param skill  the Skill instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Skill instance.
	 */
	public Observable<Skill> save(final Skill skill) {
		return Observable.create(
				new Observable.OnSubscribe<Skill>() {
					@Override
					public void call(Subscriber<? super Skill> subscriber) {
						try {
							dao.save(skill);
							subscriber.onNext(skill);
							subscriber.onCompleted();
						}
						catch(Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will delete all Skill instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Skill instances.
	 */
	public Observable<Boolean> deleteById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Boolean>() {
					@Override
					public void call(Subscriber<? super Boolean> subscriber) {
						try {
							subscriber.onNext(dao.deleteById(id));
							subscriber.onCompleted();
						}
						catch(Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will delete all Skill instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Skill instances.
	 */
	public Observable<Collection<Skill>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Skill>>() {
					@Override
					public void call(Subscriber<? super Collection<Skill>> subscriber) {
						try {
							Collection<Skill> skillsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(skillsDeleted);
							subscriber.onCompleted();
						}
						catch(Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Skill instances that
	 * require specialization.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Specialization
	 * instances.
	 */
	public Observable<Collection<Skill>> getSpecializationSkills() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Skill>>() {
					@Override
					public void call(Subscriber<? super Collection<Skill>> subscriber) {
						try {
							Collection<Skill> skills = dao.getSpecializationSkills();
							for(Skill skill : skills) {
								skill.setSpecializations(specializationDao.getSpecializationsForSkill(skill));
							}
							subscriber.onNext(skills);
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Skill instances that
	 * do not require specialization.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of non-Specialization
	 * instances.
	 */
	public Observable<Collection<Skill>> getNonSpecializationSkills() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Skill>>() {
					@Override
					public void call(Subscriber<? super Collection<Skill>> subscriber) {
						try {
							subscriber.onNext(dao.getNonSpecializationSkills());
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Skill instances that
	 * reference the given SkillCategory.
	 *
	 * @param filter  a SkillCategory instance to use as to filter the Skill instances returned by the query
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Skill instances.
	 */
	public Observable<Collection<Skill>> getSkillsForCategory(final SkillCategory filter) {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Skill>>() {
					@Override
					public void call(Subscriber<? super Collection<Skill>> subscriber) {
						try {
							Collection<Skill> skills = dao.getSkillsForCategory(filter);
							for(Skill skill : skills) {
								skill.setSpecializations(specializationDao.getSpecializationsForSkill(skill));
							}
							subscriber.onNext(skills);
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Skill instances that
	 * can be purchased by player characters.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Skill instances.
	 */
	public Observable<Collection<Skill>> getCharacterPurchasableSkills() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Skill>>() {
					@Override
					public void call(Subscriber<? super Collection<Skill>> subscriber) {
						try {
							Collection<Skill> skills = dao.getCharacterPurchasableSkills();
							for(Skill skill : skills) {
								skill.setSpecializations(specializationDao.getSpecializationsForSkill(skill));
							}
							subscriber.onNext(skills);
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Skill instances that
	 * can be purchased by player characters.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Skill instances.
	 */
	public Observable<Collection<DatabaseObject>> getCharacterUsableSkills(final Character filter) {
		return Observable.create(
				new Observable.OnSubscribe<Collection<DatabaseObject>>() {
					@Override
					public void call(Subscriber<? super Collection<DatabaseObject>> subscriber) {
						try {
							Collection<DatabaseObject> databaseObjects = new ArrayList<>();
							Collection<Skill> skills = dao.getCharacterPurchasableSkills();
							for(Skill skill : skills) {
								skill.setSpecializations(specializationDao.getSpecializationsForSkill(skill));
							}
							databaseObjects.addAll(skills);
							databaseObjects.addAll(spellListDao.getAllListsForCharacter(filter));
							subscriber.onNext(databaseObjects);
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Skill instances that
	 * can be professional skills for the given character.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Skill instances.
	 */
	public Observable<Collection<DatabaseObject>> getCharacterProfessionalSkills(final Character filter) {
		return Observable.create(
				new Observable.OnSubscribe<Collection<DatabaseObject>>() {
					@Override
					public void call(Subscriber<? super Collection<DatabaseObject>> subscriber) {
						try {
							Collection<DatabaseObject> databaseObjects = new ArrayList<>();
							Collection<Skill> skills = dao.getCharacterPurchasableSkills();
							for(Skill skill : skills) {
								if(filter.getProfession().getProfessionalSkillCategories().contains(skill.getCategory())) {
									skill.setSpecializations(specializationDao.getSpecializationsForSkill(skill));
									databaseObjects.add(skill);
								}
							}
							Collection<SpellList> spellLists = spellListDao.getAllListsForCharacter(filter);
							for(SpellList spellList : spellLists) {
								if(filter.getProfession().getProfessionalSkillCategories().contains(
										spellList.getSkill().getCategory())) {
									databaseObjects.add(spellList);
								}
							}
							subscriber.onNext(databaseObjects);
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Skill instances that
	 * are combat training skills.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Skill instances.
	 */
	public Observable<Collection<Skill>> getCombatTrainingSkills() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Skill>>() {
					@Override
					public void call(Subscriber<? super Collection<Skill>> subscriber) {
						try {
							List<Skill> skills = new ArrayList<>();
							List<SkillCategory> combatTrainingCategories = skillCategoryDao.getCombatCategories();
							for (SkillCategory skillCategory : combatTrainingCategories) {
								skills.addAll(dao.getSkillsForCategory(skillCategory));
							}
							subscriber.onNext(skills);
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}
}
