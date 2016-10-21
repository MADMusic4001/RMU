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
package com.madinnovations.rmu.controller.rxhandler.common;

import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Specialization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Specialization} instances with persistent storage.
 */
public class SpecializationRxHandler {
	private SpecializationDao dao;
	private SkillDao skillDao;
	private SkillCategoryDao skillCategoryDao;

	/**
	 * Creates a new SpecializationRxHandler
	 *
	 * @param dao  a SpecializationDao instance
	 */
	@Inject
	SpecializationRxHandler(SpecializationDao dao, SkillDao skillDao, SkillCategoryDao skillCategoryDao) {
		this.dao = dao;
		this.skillDao = skillDao;
		this.skillCategoryDao = skillCategoryDao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Specialization instance with the given
	 * id.
	 *
	 * @param id  the id of the Specialization to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Specialization instance.
	 */
	public Observable<Specialization> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Specialization>() {
					@Override
					public void call(Subscriber<? super Specialization> subscriber) {
						try {
							subscriber.onNext(dao.getById(id));
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Specialization
	 * instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Specialization
	 * instances.
	 */
	public Observable<Collection<Specialization>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Specialization>>() {
					@Override
					public void call(Subscriber<? super Collection<Specialization>> subscriber) {
						try {
							subscriber.onNext(dao.getAll());
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
	 * Creates an Observable that, when subscribed to, will save a Specialization instance to persistent storage.
	 *
	 * @param specialization  the Specialization instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Specialization instance.
	 */
	public Observable<Specialization> save(final Specialization specialization) {
		return Observable.create(
				new Observable.OnSubscribe<Specialization>() {
					@Override
					public void call(Subscriber<? super Specialization> subscriber) {
						try {
							dao.save(specialization);
							subscriber.onNext(specialization);
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
	 * Creates an Observable that, when subscribed to, will delete all Specialization instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Specialization instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Specialization instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Specialization instances.
	 */
	public Observable<Collection<Specialization>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Specialization>>() {
					@Override
					public void call(Subscriber<? super Collection<Specialization>> subscriber) {
						try {
							Collection<Specialization> specializationsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(specializationsDeleted);
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Specialization
	 * instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Specialization
	 * instances.
	 */
	public Observable<Collection<Specialization>> getSpecializationsForSkill(final Skill filter) {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Specialization>>() {
					@Override
					public void call(Subscriber<? super Collection<Specialization>> subscriber) {
						try {
							subscriber.onNext(dao.getSpecializationsForSkill(filter));
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Specialization
	 * instances from combat skill categories.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Specialization
	 * instances.
	 */
	public Observable<Collection<Specialization>> getAllAttackSpecializations() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Specialization>>() {
					@Override
					public void call(Subscriber<? super Collection<Specialization>> subscriber) {
						try {
							List<SkillCategory> skillCategories = skillCategoryDao.getCombatCategories();
							if (skillCategories != null) {
								List<Skill> skills = new ArrayList<>();
								for (SkillCategory category : skillCategories) {
									skills.addAll(skillDao.getSkillsForCategory(category));
									List<Specialization> specializations = new ArrayList<>();
									for (Skill skill : skills) {
										specializations.addAll(dao.getSpecializationsForSkill(skill));
									}
									subscriber.onNext(specializations);
								}
							}
							subscriber.onCompleted();
						} catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Specialization
	 * instances that a player character can purchase.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Specialization
	 * instances.
	 */
	public Observable<Collection<Specialization>> getCharacterPurchasableSpecializations() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Specialization>>() {
					@Override
					public void call(Subscriber<? super Collection<Specialization>> subscriber) {
						try {
							subscriber.onNext(dao.getCharacterPurchasableSpecializations());
							subscriber.onCompleted();
						} catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}
}
