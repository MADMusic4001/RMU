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

import com.madinnovations.rmu.data.dao.spells.SpellListDao;
import com.madinnovations.rmu.data.entities.spells.SpellList;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link SpellList} instances with persistent storage.
 */
public class SpellListRxHandler {
	private SpellListDao dao;

	/**
	 * Creates a new SpellListRxHandler
	 *
	 * @param dao  a SpellListDao instance
	 */
	@Inject
	public SpellListRxHandler(SpellListDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a SpellList instance with the given id.
	 *
	 * @param id  the id of the SpellList to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a SpellList instance.
	 */
	public Observable<SpellList> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<SpellList>() {
					@Override
					public void call(Subscriber<? super SpellList> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all SpellList instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of SpellList
	 * instances.
	 */
	public Observable<Collection<SpellList>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<SpellList>>() {
					@Override
					public void call(Subscriber<? super Collection<SpellList>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a SpellList instance to persistent storage.
	 *
	 * @param bodyPart  the SpellList instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the SpellList instance.
	 */
	public Observable<SpellList> save(final SpellList bodyPart) {
		return Observable.create(
				new Observable.OnSubscribe<SpellList>() {
					@Override
					public void call(Subscriber<? super SpellList> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will delete all SpellList instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the SpellList instances.
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
	 * Creates an Observable that, when subscribed to, will delete all SpellList instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the SpellList instances.
	 */
	public Observable<Collection<SpellList>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<SpellList>>() {
					@Override
					public void call(Subscriber<? super Collection<SpellList>> subscriber) {
						try {
							Collection<SpellList> bodyPartsDeleted = dao.getAll();
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
