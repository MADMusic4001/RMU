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
import com.madinnovations.rmu.data.entities.common.SkillCategory;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link SkillCategory} instances with persistent storage.
 */
public class SkillCategoryRxHandler {
	private SkillCategoryDao dao;

	/**
	 * Creates a new SkillCategoryRxHandler
	 *
	 * @param dao  a SkillCategoryDao instance
	 */
	@Inject
	public SkillCategoryRxHandler(SkillCategoryDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a SkillCategory instance with the given id.
	 *
	 * @param id  the id of the SkillCategory to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a SkillCategory instance.
	 */
	public Observable<SkillCategory> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<SkillCategory>() {
					@Override
					public void call(Subscriber<? super SkillCategory> subscriber) {
						try {
							subscriber.onNext(dao.getById(id));
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io());
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all SkillCategory instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of SkillCategory
	 * instances.
	 */
	public Observable<Collection<SkillCategory>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<SkillCategory>>() {
					@Override
					public void call(Subscriber<? super Collection<SkillCategory>> subscriber) {
						try {
							subscriber.onNext(dao.getAll());
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io());
	}

	/**
	 * Creates an Observable that, when subscribed to, will save a SkillCategory instance to persistent storage.
	 *
	 * @param stat  the SkillCategory instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the SkillCategory instance.
	 */
	public Observable<SkillCategory> save(final SkillCategory stat) {
		return Observable.create(
				new Observable.OnSubscribe<SkillCategory>() {
					@Override
					public void call(Subscriber<? super SkillCategory> subscriber) {
						try {
							dao.save(stat);
							subscriber.onNext(stat);
							subscriber.onCompleted();
						}
						catch(Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io());
	}

	/**
	 * Creates an Observable that, when subscribed to, will delete all SkillCategory instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the SkillCategory instances.
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
		).subscribeOn(Schedulers.io());
	}

	/**
	 * Creates an Observable that, when subscribed to, will delete all SkillCategory instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the SkillCategory instances.
	 */
	public Observable<Collection<SkillCategory>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<SkillCategory>>() {
					@Override
					public void call(Subscriber<? super Collection<SkillCategory>> subscriber) {
						try {
							Collection<SkillCategory> skillCategoriesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(skillCategoriesDeleted);
							subscriber.onCompleted();
						}
						catch(Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io());
	}
}
