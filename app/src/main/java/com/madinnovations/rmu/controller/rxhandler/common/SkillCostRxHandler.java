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

import com.madinnovations.rmu.data.dao.common.SkillCostDao;
import com.madinnovations.rmu.data.entities.common.SkillCost;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link SkillCost} instances with persistent storage.
 */
public class SkillCostRxHandler {
	private SkillCostDao dao;

	/**
	 * Creates a new SkillCostRxHandler
	 *
	 * @param dao  a SkillCostDao instance
	 */
	@Inject
	public SkillCostRxHandler(SkillCostDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a SkillCost instance with the given id.
	 *
	 * @param id  the id of the SkillCost to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a SkillCost instance.
	 */
	public Observable<SkillCost> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<SkillCost>() {
					@Override
					public void call(Subscriber<? super SkillCost> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all SkillCost instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of SkillCost
	 * instances.
	 */
	public Observable<Collection<SkillCost>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<SkillCost>>() {
					@Override
					public void call(Subscriber<? super Collection<SkillCost>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a SkillCost instance to persistent storage.
	 *
	 * @param skillCost  the SkillCost instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the SkillCost instance.
	 */
	public Observable<SkillCost> save(final SkillCost skillCost) {
		return Observable.create(
				new Observable.OnSubscribe<SkillCost>() {
					@Override
					public void call(Subscriber<? super SkillCost> subscriber) {
						try {
							dao.save(skillCost);
							subscriber.onNext(skillCost);
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
	 * Creates an Observable that, when subscribed to, will delete all SkillCost instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the SkillCost instances.
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
	 * Creates an Observable that, when subscribed to, will delete all SkillCost instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the SkillCost instances.
	 */
	public Observable<Collection<SkillCost>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<SkillCost>>() {
					@Override
					public void call(Subscriber<? super Collection<SkillCost>> subscriber) {
						try {
							Collection<SkillCost> skillCostsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(skillCostsDeleted);
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
