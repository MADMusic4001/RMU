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
package com.madinnovations.rmu.controller.rxhandler.campaign;

import com.madinnovations.rmu.data.dao.campaign.CampaignDao;
import com.madinnovations.rmu.data.entities.campaign.Campaign;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Campaign} instances with persistent storage.
 */
public class CampaignRxHandler {
	private CampaignDao dao;

	/**
	 * Creates a new CampaignRxHandler
	 *
	 * @param dao  a CampaignDao instance
	 */
	@Inject
	public CampaignRxHandler(CampaignDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Campaign instance with the given id.
	 *
	 * @param id  the id of the Campaign to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Campaign instance.
	 */
	public Observable<Campaign> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Campaign>() {
					@Override
					public void call(Subscriber<? super Campaign> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Campaign instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Campaign
	 * instances.
	 */
	public Observable<Collection<Campaign>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Campaign>>() {
					@Override
					public void call(Subscriber<? super Collection<Campaign>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Campaign instance to persistent storage.
	 *
	 * @param culture  the Campaign instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Campaign instance.
	 */
	public Observable<Campaign> save(final Campaign culture) {
		return Observable.create(
				new Observable.OnSubscribe<Campaign>() {
					@Override
					public void call(Subscriber<? super Campaign> subscriber) {
						try {
							dao.save(culture);
							subscriber.onNext(culture);
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
	 * Creates an Observable that, when subscribed to, will delete all Campaign instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Campaign instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Campaign instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Campaign instances.
	 */
	public Observable<Collection<Campaign>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Campaign>>() {
					@Override
					public void call(Subscriber<? super Collection<Campaign>> subscriber) {
						try {
							Collection<Campaign> culturesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(culturesDeleted);
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
