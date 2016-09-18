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
package com.madinnovations.rmu.controller.rxhandler.spell;

import com.madinnovations.rmu.data.dao.spells.SpellTypeDao;
import com.madinnovations.rmu.data.entities.spells.SpellType;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link SpellType} instances with persistent storage.
 */
public class SpellTypeRxHandler {
	private SpellTypeDao dao;

	/**
	 * Creates a new SpellTypeRxHandler
	 *
	 * @param dao  a SpellTypeDao instance
	 */
	@Inject
	public SpellTypeRxHandler(SpellTypeDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a SpellType instance with the given id.
	 *
	 * @param id  the id of the SpellType to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a SpellType instance.
	 */
	public Observable<SpellType> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<SpellType>() {
					@Override
					public void call(Subscriber<? super SpellType> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all SpellType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of SpellType
	 * instances.
	 */
	public Observable<Collection<SpellType>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<SpellType>>() {
					@Override
					public void call(Subscriber<? super Collection<SpellType>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a SpellType instance to persistent storage.
	 *
	 * @param spellType  the SpellType instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the SpellType instance.
	 */
	public Observable<SpellType> save(final SpellType spellType) {
		return Observable.create(
				new Observable.OnSubscribe<SpellType>() {
					@Override
					public void call(Subscriber<? super SpellType> subscriber) {
						try {
							dao.save(spellType);
							subscriber.onNext(spellType);
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
	 * Creates an Observable that, when subscribed to, will delete all SpellType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the SpellType instances.
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
	 * Creates an Observable that, when subscribed to, will delete all SpellType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the SpellType instances.
	 */
	public Observable<Collection<SpellType>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<SpellType>>() {
					@Override
					public void call(Subscriber<? super Collection<SpellType>> subscriber) {
						try {
							Collection<SpellType> spellTypesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(spellTypesDeleted);
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
}
