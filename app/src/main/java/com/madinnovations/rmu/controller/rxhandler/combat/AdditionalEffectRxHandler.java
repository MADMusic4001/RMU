/*
 *
 *   Copyright (C) 2017 MadInnovations
 *   <p/>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   <p/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   <p/>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  
 *
 */
package com.madinnovations.rmu.controller.rxhandler.combat;

import com.madinnovations.rmu.data.dao.combat.AdditionalEffectDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultRowDao;
import com.madinnovations.rmu.data.entities.combat.AdditionalEffect;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link AdditionalEffect} instances with persistent storage.
 */
@SuppressWarnings("unused")
public class AdditionalEffectRxHandler implements Serializable {
	private static final long serialVersionUID = 3392013210046552200L;
	private static final String TAG = "AdditionalEffectRxHandler";
	private AdditionalEffectDao dao;
	private DamageResultDao    damageResultDao;
	private DamageResultRowDao damageResultRowDao;

	/**
	 * Creates a new AdditionalEffectRxHandler
	 *
	 * @param dao  an {@link AdditionalEffectDao} instance
	 */
	@Inject
	public AdditionalEffectRxHandler(AdditionalEffectDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a AdditionalEffect instance with the given id.
	 *
	 * @param id  the id of the AdditionalEffect to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a AdditionalEffect instance.
	 */
	public Observable<AdditionalEffect> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<AdditionalEffect>() {
					@Override
					public void call(Subscriber<? super AdditionalEffect> subscriber) {
						try {
							AdditionalEffect attack = dao.getById(id);
							subscriber.onNext(attack);
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all AdditionalEffect instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of AdditionalEffect
	 * instances.
	 */
	public Observable<Collection<AdditionalEffect>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<AdditionalEffect>>() {
					@Override
					public void call(Subscriber<? super Collection<AdditionalEffect>> subscriber) {
						try {
							Collection<AdditionalEffect> attacks = dao.getAll();
							subscriber.onNext(attacks);
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
	 * Creates an Observable that, when subscribed to, will save a AdditionalEffect instance to persistent storage.
	 *
	 * @param additionalEffect  the AdditionalEffect instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the AdditionalEffect instance.
	 */
	public Observable<AdditionalEffect> save(final AdditionalEffect additionalEffect) {
		return Observable.create(
				new Observable.OnSubscribe<AdditionalEffect>() {
					@Override
					public void call(Subscriber<? super AdditionalEffect> subscriber) {
						try {
							dao.save(additionalEffect);
							subscriber.onNext(additionalEffect);
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
	 * Creates an Observable that, when subscribed to, will delete all AdditionalEffect instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the AdditionalEffect instances.
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
	 * Creates an Observable that, when subscribed to, will delete all AdditionalEffect instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the AdditionalEffect instances.
	 */
	public Observable<Collection<AdditionalEffect>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<AdditionalEffect>>() {
					@Override
					public void call(Subscriber<? super Collection<AdditionalEffect>> subscriber) {
						try {
							Collection<AdditionalEffect> criticalResultsDeleted = dao.getAll();
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
