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
package com.madinnovations.rmu.controller.rxhandler.combat;

import com.madinnovations.rmu.data.dao.combat.AttackDao;
import com.madinnovations.rmu.data.entities.combat.Attack;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Attack} instances with persistent storage.
 */
public class AttackRxHandler {
	private AttackDao dao;

	/**
	 * Creates a new AttackRxHandler
	 *
	 * @param dao  a AttackDao instance
	 */
	@Inject
	public AttackRxHandler(AttackDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Attack instance with the given id.
	 *
	 * @param id  the id of the Attack to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Attack instance.
	 */
	public Observable<Attack> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Attack>() {
					@Override
					public void call(Subscriber<? super Attack> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Attack instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Attack
	 * instances.
	 */
	public Observable<Collection<Attack>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Attack>>() {
					@Override
					public void call(Subscriber<? super Collection<Attack>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Attack instance to persistent storage.
	 *
	 * @param criticalResult  the Attack instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Attack instance.
	 */
	public Observable<Attack> save(final Attack criticalResult) {
		return Observable.create(
				new Observable.OnSubscribe<Attack>() {
					@Override
					public void call(Subscriber<? super Attack> subscriber) {
						try {
							dao.save(criticalResult);
							subscriber.onNext(criticalResult);
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
	 * Creates an Observable that, when subscribed to, will delete all Attack instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Attack instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Attack instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Attack instances.
	 */
	public Observable<Collection<Attack>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Attack>>() {
					@Override
					public void call(Subscriber<? super Collection<Attack>> subscriber) {
						try {
							Collection<Attack> criticalResultsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(criticalResultsDeleted);
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
