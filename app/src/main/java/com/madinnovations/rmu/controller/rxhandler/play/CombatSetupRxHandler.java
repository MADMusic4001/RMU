/**
 * Copyright (C) 2017 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.controller.rxhandler.play;

import com.madinnovations.rmu.data.dao.play.CombatSetupDao;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.play.CombatSetup;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link CombatSetup} instances with persistent storage.
 */
public class CombatSetupRxHandler {
	private CombatSetupDao dao;

	/**
	 * Creates a new CombatSetupRxHandler
	 *
	 * @param dao  a CombatSetupDao instance
	 */
	@Inject
	public CombatSetupRxHandler(CombatSetupDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a CombatSetup instance with the given id.
	 *
	 * @param id  the id of the CombatSetup to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a CombatSetup instance.
	 */
	public Observable<CombatSetup> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<CombatSetup>() {
					@Override
					public void call(Subscriber<? super CombatSetup> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all CombatSetup instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of CombatSetup
	 * instances.
	 */
	public Observable<Collection<CombatSetup>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CombatSetup>>() {
					@Override
					public void call(Subscriber<? super Collection<CombatSetup>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a CombatSetup instance to persistent storage.
	 *
	 * @param stat  the CombatSetup instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the CombatSetup instance.
	 */
	public Observable<CombatSetup> save(final CombatSetup stat) {
		return Observable.create(
				new Observable.OnSubscribe<CombatSetup>() {
					@Override
					public void call(Subscriber<? super CombatSetup> subscriber) {
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
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will delete all CombatSetup instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CombatSetup instances.
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
	 * Creates an Observable that, when subscribed to, will delete all CombatSetup instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CombatSetup instances.
	 */
	public Observable<Collection<CombatSetup>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CombatSetup>>() {
					@Override
					public void call(Subscriber<? super Collection<CombatSetup>> subscriber) {
						try {
							Collection<CombatSetup> CombatSetupsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(CombatSetupsDeleted);
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
	 * Retrieves a CombatSetup object from persistent storage.
	 *
	 * @param campaign  the {@link Campaign} for the CombatSetup instance
	 * @return the most recent CombatSetup for the given campaign
	 */
	public Observable<CombatSetup> getMostRecentForCampaign(final Campaign campaign) {
		return Observable.create(new Observable.OnSubscribe<CombatSetup>() {
			@Override
			public void call(Subscriber<? super CombatSetup> subscriber) {
				try {
					CombatSetup combatSetup = dao.getMostRecentForCampaign(campaign);
					subscriber.onNext(combatSetup);
					subscriber.onCompleted();
				}
				catch (Exception e) {
					subscriber.onError(e);
				}
			}
		}).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}
}
