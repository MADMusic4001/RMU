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
package com.madinnovations.rmu.controller.rxhandler.item;

import com.madinnovations.rmu.data.dao.item.WeaponDao;
import com.madinnovations.rmu.data.entities.object.Weapon;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Weapon} instances with persistent storage.
 */
public class WeaponRxHandler {
	private WeaponDao dao;

	/**
	 * Creates a new WeaponRxHandler
	 *
	 * @param dao  a WeaponDao instance
	 */
	@Inject
	public WeaponRxHandler(WeaponDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Weapon instance with the given id.
	 *
	 * @param id  the id of the Weapon to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Weapon instance.
	 */
	public Observable<Weapon> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Weapon>() {
					@Override
					public void call(Subscriber<? super Weapon> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Weapon instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Weapon
	 * instances.
	 */
	public Observable<Collection<Weapon>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Weapon>>() {
					@Override
					public void call(Subscriber<? super Collection<Weapon>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Weapon instance to persistent storage.
	 *
	 * @param weapon  the Weapon instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Weapon instance.
	 */
	public Observable<Weapon> save(final Weapon weapon) {
		return Observable.create(
				new Observable.OnSubscribe<Weapon>() {
					@Override
					public void call(Subscriber<? super Weapon> subscriber) {
						try {
							dao.save(weapon);
							subscriber.onNext(weapon);
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
	 * Creates an Observable that, when subscribed to, will delete all Weapon instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Weapon instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Weapon instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Weapon instances.
	 */
	public Observable<Collection<Weapon>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Weapon>>() {
					@Override
					public void call(Subscriber<? super Collection<Weapon>> subscriber) {
						try {
							Collection<Weapon> weaponsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(weaponsDeleted);
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
