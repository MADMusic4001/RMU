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

import com.madinnovations.rmu.data.dao.creature.CreatureTypeDao;
import com.madinnovations.rmu.data.entities.creature.CreatureCategory;
import com.madinnovations.rmu.data.entities.creature.CreatureType;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link CreatureType} instances with persistent storage.
 */
public class CreatureTypeRxHandler {
	private CreatureTypeDao dao;

	/**
	 * Creates a new CreatureTypeRxHandler
	 *
	 * @param dao  a CreatureTypeDao instance
	 */
	@Inject
	public CreatureTypeRxHandler(CreatureTypeDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a CreatureType instance with the given id.
	 *
	 * @param id  the id of the CreatureType to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a CreatureType instance.
	 */
	public Observable<CreatureType> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<CreatureType>() {
					@Override
					public void call(Subscriber<? super CreatureType> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all CreatureType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of CreatureType
	 * instances.
	 */
	public Observable<Collection<CreatureType>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CreatureType>>() {
					@Override
					public void call(Subscriber<? super Collection<CreatureType>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a CreatureType instance to persistent storage.
	 *
	 * @param creatureType  the CreatureType instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the CreatureType instance.
	 */
	public Observable<CreatureType> save(final CreatureType creatureType) {
		return Observable.create(
				new Observable.OnSubscribe<CreatureType>() {
					@Override
					public void call(Subscriber<? super CreatureType> subscriber) {
						try {
							dao.save(creatureType);
							subscriber.onNext(creatureType);
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
	 * Creates an Observable that, when subscribed to, will delete all CreatureType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CreatureType instances.
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
	 * Creates an Observable that, when subscribed to, will delete all CreatureType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CreatureType instances.
	 */
	public Observable<Collection<CreatureType>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CreatureType>>() {
					@Override
					public void call(Subscriber<? super Collection<CreatureType>> subscriber) {
						try {
							Collection<CreatureType> creatureTypesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(creatureTypesDeleted);
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all CreatureType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of CreatureType
	 * instances.
	 */
	public Observable<Collection<CreatureType>> getTypesForCategory(final CreatureCategory creatureCategory) {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CreatureType>>() {
					@Override
					public void call(Subscriber<? super Collection<CreatureType>> subscriber) {
						try {
							subscriber.onNext(dao.getTypesForCategory(creatureCategory));
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
