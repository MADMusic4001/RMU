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

import com.madinnovations.rmu.data.dao.spells.SpellSubTypeDao;
import com.madinnovations.rmu.data.entities.spells.SpellSubType;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link SpellSubType} instances with persistent storage.
 */
public class SpellSubTypeRxHandler {
	private SpellSubTypeDao dao;

	/**
	 * Creates a new SpellSubTypeRxHandler
	 *
	 * @param dao  a SpellSubTypeDao instance
	 */
	@Inject
	public SpellSubTypeRxHandler(SpellSubTypeDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a SpellSubType instance with the given id.
	 *
	 * @param id  the id of the SpellSubType to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a SpellSubType instance.
	 */
	public Observable<SpellSubType> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<SpellSubType>() {
					@Override
					public void call(Subscriber<? super SpellSubType> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all SpellSubType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of SpellSubType
	 * instances.
	 */
	public Observable<Collection<SpellSubType>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<SpellSubType>>() {
					@Override
					public void call(Subscriber<? super Collection<SpellSubType>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a SpellSubType instance to persistent storage.
	 *
	 * @param bodyPart  the SpellSubType instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the SpellSubType instance.
	 */
	public Observable<SpellSubType> save(final SpellSubType bodyPart) {
		return Observable.create(
				new Observable.OnSubscribe<SpellSubType>() {
					@Override
					public void call(Subscriber<? super SpellSubType> subscriber) {
						try {
							dao.save(bodyPart);
							subscriber.onNext(bodyPart);
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
	 * Creates an Observable that, when subscribed to, will delete all SpellSubType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the SpellSubType instances.
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
	 * Creates an Observable that, when subscribed to, will delete all SpellSubType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the SpellSubType instances.
	 */
	public Observable<Collection<SpellSubType>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<SpellSubType>>() {
					@Override
					public void call(Subscriber<? super Collection<SpellSubType>> subscriber) {
						try {
							Collection<SpellSubType> bodyPartsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(bodyPartsDeleted);
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
