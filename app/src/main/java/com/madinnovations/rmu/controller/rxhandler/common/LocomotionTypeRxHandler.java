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

import com.madinnovations.rmu.data.dao.common.LocomotionTypeDao;
import com.madinnovations.rmu.data.entities.common.LocomotionType;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link LocomotionType} instances with persistent storage.
 */
public class LocomotionTypeRxHandler {
	private LocomotionTypeDao dao;

	/**
	 * Creates a new LocomotionTypeRxHandler
	 *
	 * @param dao  a LocomotionTypeDao instance
	 */
	@Inject
	public LocomotionTypeRxHandler(LocomotionTypeDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a LocomotionType instance with the given id.
	 *
	 * @param id  the id of the LocomotionType to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a LocomotionType instance.
	 */
	public Observable<LocomotionType> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<LocomotionType>() {
					@Override
					public void call(Subscriber<? super LocomotionType> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all LocomotionType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of LocomotionType
	 * instances.
	 */
	public Observable<Collection<LocomotionType>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<LocomotionType>>() {
					@Override
					public void call(Subscriber<? super Collection<LocomotionType>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a LocomotionType instance to persistent storage.
	 *
	 * @param stat  the LocomotionType instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the LocomotionType instance.
	 */
	public Observable<LocomotionType> save(final LocomotionType stat) {
		return Observable.create(
				new Observable.OnSubscribe<LocomotionType>() {
					@Override
					public void call(Subscriber<? super LocomotionType> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will delete all LocomotionType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the LocomotionType instances.
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
	 * Creates an Observable that, when subscribed to, will delete all LocomotionType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the LocomotionType instances.
	 */
	public Observable<Collection<LocomotionType>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<LocomotionType>>() {
					@Override
					public void call(Subscriber<? super Collection<LocomotionType>> subscriber) {
						try {
							Collection<LocomotionType> worldsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(worldsDeleted);
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
