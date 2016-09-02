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

import android.util.SparseArray;

import com.madinnovations.rmu.data.dao.combat.CriticalTypeDao;
import com.madinnovations.rmu.data.entities.combat.CriticalType;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link CriticalType} instances with persistent storage.
 */
public class CriticalTypeRxHandler {
	private SparseArray<CriticalType> criticalTypes = null;
	private CriticalTypeDao dao;

	/**
	 * Creates a new CriticalTypeRxHandler
	 *
	 * @param dao  a CriticalTypeDao instance
	 */
	@Inject
	public CriticalTypeRxHandler(CriticalTypeDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a CriticalType instance with the given id.
	 *
	 * @param id  the id of the CriticalType to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a CriticalType instance.
	 */
	public Observable<CriticalType> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<CriticalType>() {
					@Override
					public void call(Subscriber<? super CriticalType> subscriber) {
						try {
							CriticalType criticalType = null;
							if(criticalTypes != null) {
								criticalType = criticalTypes.get(id);
							}
							if(criticalType == null) {
								criticalType = dao.getById(id);
							}
							subscriber.onNext(criticalType);
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all CriticalType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of CriticalType
	 * instances.
	 */
	public Observable<Collection<CriticalType>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CriticalType>>() {
					@Override
					public void call(Subscriber<? super Collection<CriticalType>> subscriber) {
						try {
							Collection<CriticalType> list = null;
							if(criticalTypes == null) {
								list = dao.getAll();
								criticalTypes = new SparseArray<CriticalType>(list.size());
								for(CriticalType criticalType : list) {
									criticalTypes.append(criticalType.getId(), criticalType);
								}
							}
							else {
								list = new ArrayList<CriticalType>(criticalTypes.size());
								for(int i = 0; i < criticalTypes.size(); i++) {
									list.add(criticalTypes.valueAt(i));
								}
							}
							subscriber.onNext(list);
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
	 * Creates an Observable that, when subscribed to, will save a CriticalType instance to persistent storage.
	 *
	 * @param criticalType  the CriticalType instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the CriticalType instance.
	 */
	public Observable<CriticalType> save(final CriticalType criticalType) {
		return Observable.create(
				new Observable.OnSubscribe<CriticalType>() {
					@Override
					public void call(Subscriber<? super CriticalType> subscriber) {
						try {
							dao.save(criticalType);
							if(criticalTypes != null && criticalTypes.get(criticalType.getId()) == null) {
								criticalTypes.append(criticalType.getId(), criticalType);
							}
							subscriber.onNext(criticalType);
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
	 * Creates an Observable that, when subscribed to, will delete all CriticalType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CriticalType instances.
	 */
	public Observable<Boolean> deleteById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Boolean>() {
					@Override
					public void call(Subscriber<? super Boolean> subscriber) {
						try {
							subscriber.onNext(dao.deleteById(id));
							if(criticalTypes != null && criticalTypes.get(id) != null) {
								criticalTypes.remove(id);
							}
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
	 * Creates an Observable that, when subscribed to, will delete all CriticalType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CriticalType instances.
	 */
	public Observable<Collection<CriticalType>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CriticalType>>() {
					@Override
					public void call(Subscriber<? super Collection<CriticalType>> subscriber) {
						try {
							Collection<CriticalType> criticalTypesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(criticalTypesDeleted);
							if(criticalTypes != null) {
								criticalTypes.clear();
							}
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
	 * Queries persistent storage for a CriticalType instance with the given code.
	 *
	 * @param code  the code of the CriticalType to retrieve from persistent storage
	 * @return the CriticalType instance with the given code or null if not found
	 */
	public CriticalType getByCode(final char code) {
		CriticalType result = null;

		if(criticalTypes == null) {
			Collection<CriticalType> list = dao.getAll();
			criticalTypes = new SparseArray<CriticalType>(list.size());
			for(CriticalType criticalType : list) {
				criticalTypes.append(criticalType.getId(), criticalType);
				if(criticalType.getCode() == code) {
					result = criticalType;
				}
			}
		}
		else {
			for(int i = 0; i < criticalTypes.size(); i++) {
				if(criticalTypes.valueAt(i).getCode() == code) {
					result = criticalTypes.valueAt(i);
					break;
				}
			}
		}
		return result;
	}
}
