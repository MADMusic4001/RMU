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

import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Talent} instances with persistent storage.
 */
public class TalentRxHandler {
	private TalentDao dao;

	/**
	 * Creates a new TalentRxHandler
	 *
	 * @param dao  a TalentDao instance
	 */
	@Inject
	public TalentRxHandler(TalentDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Talent instance with the given id.
	 *
	 * @param id  the id of the Talent to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Talent instance.
	 */
	public Observable<Talent> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Talent>() {
					@Override
					public void call(Subscriber<? super Talent> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Talent instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Talent
	 * instances.
	 */
	public Observable<Collection<Talent>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Talent>>() {
					@Override
					public void call(Subscriber<? super Collection<Talent>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Talent instance to persistent storage.
	 *
	 * @param talent  the Talent instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Talent instance.
	 */
	public Observable<Talent> save(final Talent talent) {
		return Observable.create(
				new Observable.OnSubscribe<Talent>() {
					@Override
					public void call(Subscriber<? super Talent> subscriber) {
						try {
							dao.save(talent);
							subscriber.onNext(talent);
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
	 * Creates an Observable that, when subscribed to, will delete all Talent instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Talent instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Talent instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Talent instances.
	 */
	public Observable<Collection<Talent>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Talent>>() {
					@Override
					public void call(Subscriber<? super Collection<Talent>> subscriber) {
						try {
							Collection<Talent> talentsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(talentsDeleted);
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Talent instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Talent instances.
	 */
	public Observable<Collection<Talent>> getTalentsForTalentCategory(final TalentCategory filter) {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Talent>>() {
					@Override
					public void call(Subscriber<? super Collection<Talent>> subscriber) {
						try {
							subscriber.onNext(dao.getTalentsForTalentCategory(filter));
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
