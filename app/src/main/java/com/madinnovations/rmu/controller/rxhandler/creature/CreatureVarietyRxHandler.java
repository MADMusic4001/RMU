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
package com.madinnovations.rmu.controller.rxhandler.creature;

import com.madinnovations.rmu.data.dao.creature.CreatureVarietyDao;
import com.madinnovations.rmu.data.entities.creature.CreatureType;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link CreatureVariety} instances with persistent storage.
 */
public class CreatureVarietyRxHandler {
	private CreatureVarietyDao dao;

	/**
	 * Creates a new CreatureVarietyRxHandler
	 *
	 * @param dao  a CreatureVarietyDao instance
	 */
	@Inject
	public CreatureVarietyRxHandler(CreatureVarietyDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a CreatureVariety instance with the given id.
	 *
	 * @param id  the id of the CreatureVariety to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a CreatureVariety instance.
	 */
	public Observable<CreatureVariety> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<CreatureVariety>() {
					@Override
					public void call(Subscriber<? super CreatureVariety> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all CreatureVariety instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of CreatureVariety
	 * instances.
	 */
	public Observable<Collection<CreatureVariety>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CreatureVariety>>() {
					@Override
					public void call(Subscriber<? super Collection<CreatureVariety>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a CreatureVariety instance to persistent storage.
	 *
	 * @param creatureVariety  the CreatureVariety instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the CreatureVariety instance.
	 */
	public Observable<CreatureVariety> save(final CreatureVariety creatureVariety) {
		return Observable.create(
				new Observable.OnSubscribe<CreatureVariety>() {
					@Override
					public void call(Subscriber<? super CreatureVariety> subscriber) {
						try {
							dao.save(creatureVariety);
							subscriber.onNext(creatureVariety);
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
	 * Creates an Observable that, when subscribed to, will delete all CreatureVariety instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CreatureVariety instances.
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
	 * Creates an Observable that, when subscribed to, will delete all CreatureVariety instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CreatureVariety instances.
	 */
	public Observable<Collection<CreatureVariety>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CreatureVariety>>() {
					@Override
					public void call(Subscriber<? super Collection<CreatureVariety>> subscriber) {
						try {
							Collection<CreatureVariety> creatureVarietiesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(creatureVarietiesDeleted);
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all CreatureVariety instances.
	 *
	 * @param creatureType  the CreatureType that all the instances will be part of
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of CreatureVariety
	 * instances.
	 */
	public Observable<Collection<CreatureVariety>> getVarietiesForType(final CreatureType creatureType) {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CreatureVariety>>() {
					@Override
					public void call(Subscriber<? super Collection<CreatureVariety>> subscriber) {
						try {
							subscriber.onNext(dao.getVarietiesForType(creatureType));
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
