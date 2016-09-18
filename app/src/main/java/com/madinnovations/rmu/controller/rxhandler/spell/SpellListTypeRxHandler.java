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

import com.madinnovations.rmu.data.dao.spells.SpellListTypeDao;
import com.madinnovations.rmu.data.entities.spells.SpellListType;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link SpellListType} instances with persistent storage.
 */
public class SpellListTypeRxHandler {
	private SpellListTypeDao dao;

	/**
	 * Creates a new SpellListTypeRxHandler
	 *
	 * @param dao  a SpellListTypeDao instance
	 */
	@Inject
	public SpellListTypeRxHandler(SpellListTypeDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a SpellListType instance with the given id.
	 *
	 * @param id  the id of the SpellListType to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a SpellListType instance.
	 */
	public Observable<SpellListType> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<SpellListType>() {
					@Override
					public void call(Subscriber<? super SpellListType> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all SpellListType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of SpellListType
	 * instances.
	 */
	public Observable<Collection<SpellListType>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<SpellListType>>() {
					@Override
					public void call(Subscriber<? super Collection<SpellListType>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a SpellListType instance to persistent storage.
	 *
	 * @param stat  the SpellListType instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the SpellListType instance.
	 */
	public Observable<SpellListType> save(final SpellListType stat) {
		return Observable.create(
				new Observable.OnSubscribe<SpellListType>() {
					@Override
					public void call(Subscriber<? super SpellListType> subscriber) {
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
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will delete all SpellListType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the SpellListType instances.
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
	 * Creates an Observable that, when subscribed to, will delete all SpellListType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the SpellListType instances.
	 */
	public Observable<Collection<SpellListType>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<SpellListType>>() {
					@Override
					public void call(Subscriber<? super Collection<SpellListType>> subscriber) {
						try {
							Collection<SpellListType> spellListTypesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(spellListTypesDeleted);
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
