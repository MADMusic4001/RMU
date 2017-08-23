/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.controller.rxhandler.combat;

import com.madinnovations.rmu.data.dao.combat.AdditionalEffectDao;
import com.madinnovations.rmu.data.dao.combat.CriticalResultDao;
import com.madinnovations.rmu.data.entities.combat.CriticalResult;
import com.madinnovations.rmu.data.entities.combat.CriticalType;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link CriticalResult} instances with persistent storage.
 */
public class CriticalResultRxHandler implements Serializable{
	private static final long serialVersionUID = 6654310690959624389L;
	private CriticalResultDao dao;
	private AdditionalEffectDao additionalEffectDao;

	/**
	 * Creates a new CriticalResultRxHandler
	 *
	 * @param dao  a CriticalResultDao instance
	 * @param additionalEffectDao  an AdditionalEffectDao instance
	 */
	@Inject
	public CriticalResultRxHandler(CriticalResultDao dao, AdditionalEffectDao additionalEffectDao) {
		this.dao = dao;
		this.additionalEffectDao = additionalEffectDao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a CriticalResult instance with the given id.
	 *
	 * @param id  the id of the CriticalResult to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a CriticalResult instance.
	 */
	public Observable<CriticalResult> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<CriticalResult>() {
					@Override
					public void call(Subscriber<? super CriticalResult> subscriber) {
						try {
							CriticalResult criticalResult = dao.getById(id);
							subscriber.onNext(criticalResult);
							criticalResult.setAdditionalEffects(additionalEffectDao.getForCriticalResult(criticalResult));
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all CriticalResult instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of CriticalResult
	 * instances.
	 */
	public Observable<Collection<CriticalResult>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CriticalResult>>() {
					@Override
					public void call(Subscriber<? super Collection<CriticalResult>> subscriber) {
						try {
							Collection<CriticalResult> results = dao.getAll();
							subscriber.onNext(results);
							for(CriticalResult criticalResult : results) {
								criticalResult.setAdditionalEffects(additionalEffectDao.getForCriticalResult(criticalResult));
							}
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
	 * Creates an Observable that, when subscribed to, will save a CriticalResult instance to persistent storage.
	 *
	 * @param criticalResult  the CriticalResult instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the CriticalResult instance.
	 */
	public Observable<CriticalResult> save(final CriticalResult criticalResult) {
		return Observable.create(
				new Observable.OnSubscribe<CriticalResult>() {
					@Override
					public void call(Subscriber<? super CriticalResult> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will delete all CriticalResult instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CriticalResult instances.
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
	 * Creates an Observable that, when subscribed to, will delete all CriticalResult instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CriticalResult instances.
	 */
	public Observable<Collection<CriticalResult>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CriticalResult>>() {
					@Override
					public void call(Subscriber<? super Collection<CriticalResult>> subscriber) {
						try {
							Collection<CriticalResult> criticalResultsDeleted = dao.getAll();
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

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all CriticalResult instances that
	 * reference the given CriticalType.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of CriticalResult instances.
	 */
	public Observable<Collection<CriticalResult>> getCriticalResultsForCriticalType(final CriticalType filter) {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CriticalResult>>() {
					@Override
					public void call(Subscriber<? super Collection<CriticalResult>> subscriber) {
						try {
							Collection<CriticalResult> results = dao.getCriticalResultsForCriticalType(filter);
							subscriber.onNext(results);
							for(CriticalResult criticalResult : results) {
								criticalResult.setAdditionalEffects(additionalEffectDao.getForCriticalResult(criticalResult));
							}
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

	public Observable<Collection<CriticalResult>> getCriticalResultTableRows(final CriticalType criticalType,
																			 final char severityCode) {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CriticalResult>>() {
					@Override
					public void call(Subscriber<? super Collection<CriticalResult>> subscriber) {
						try {
							Collection<CriticalResult> results = dao.getCriticalResultTableRows(criticalType, severityCode);
							subscriber.onNext(results);
							for(CriticalResult criticalResult : results) {
								criticalResult.setAdditionalEffects(additionalEffectDao.getForCriticalResult(criticalResult));
							}
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
}
