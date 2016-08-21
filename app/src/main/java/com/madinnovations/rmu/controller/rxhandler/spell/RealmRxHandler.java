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

import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.entities.spells.Realm;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Realm} instances with persistent storage.
 */
public class RealmRxHandler {
	private RealmDao dao;

	/**
	 * Creates a new RealmRxHandler
	 *
	 * @param dao  a RealmDao instance
	 */
	@Inject
	public RealmRxHandler(RealmDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Realm instance with the given id.
	 *
	 * @param id  the id of the Realm to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Realm instance.
	 */
	public Observable<Realm> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Realm>() {
					@Override
					public void call(Subscriber<? super Realm> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Realm instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Realm
	 * instances.
	 */
	public Observable<Collection<Realm>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Realm>>() {
					@Override
					public void call(Subscriber<? super Collection<Realm>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Realm instance to persistent storage.
	 *
	 * @param stat  the Realm instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Realm instance.
	 */
	public Observable<Realm> save(final Realm stat) {
		return Observable.create(
				new Observable.OnSubscribe<Realm>() {
					@Override
					public void call(Subscriber<? super Realm> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will delete all Realm instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Realm instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Realm instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Realm instances.
	 */
	public Observable<Collection<Realm>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Realm>>() {
					@Override
					public void call(Subscriber<? super Collection<Realm>> subscriber) {
						try {
							Collection<Realm> RealmsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(RealmsDeleted);
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
