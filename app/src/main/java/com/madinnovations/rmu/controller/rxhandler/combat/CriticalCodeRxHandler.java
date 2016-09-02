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

import com.madinnovations.rmu.data.dao.combat.CriticalCodeDao;
import com.madinnovations.rmu.data.entities.combat.CriticalCode;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link CriticalCode} instances with persistent storage.
 */
public class CriticalCodeRxHandler {
	private CriticalCodeDao dao;

	/**
	 * Creates a new CriticalCodeRxHandler
	 *
	 * @param dao  a CriticalCodeDao instance
	 */
	@Inject
	public CriticalCodeRxHandler(CriticalCodeDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a CriticalCode instance with the given id.
	 *
	 * @param id  the id of the CriticalCode to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a CriticalCode instance.
	 */
	public Observable<CriticalCode> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<CriticalCode>() {
					@Override
					public void call(Subscriber<? super CriticalCode> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all CriticalCode instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of CriticalCode
	 * instances.
	 */
	public Observable<Collection<CriticalCode>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CriticalCode>>() {
					@Override
					public void call(Subscriber<? super Collection<CriticalCode>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a CriticalCode instance to persistent storage.
	 *
	 * @param talentCategory  the CriticalCode instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the CriticalCode instance.
	 */
	public Observable<CriticalCode> save(final CriticalCode talentCategory) {
		return Observable.create(
				new Observable.OnSubscribe<CriticalCode>() {
					@Override
					public void call(Subscriber<? super CriticalCode> subscriber) {
						try {
							dao.save(talentCategory);
							subscriber.onNext(talentCategory);
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
	 * Creates an Observable that, when subscribed to, will delete all CriticalCode instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CriticalCode instances.
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
	 * Creates an Observable that, when subscribed to, will delete all CriticalCode instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CriticalCode instances.
	 */
	public Observable<Collection<CriticalCode>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CriticalCode>>() {
					@Override
					public void call(Subscriber<? super Collection<CriticalCode>> subscriber) {
						try {
							Collection<CriticalCode> talentCategoriesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(talentCategoriesDeleted);
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
