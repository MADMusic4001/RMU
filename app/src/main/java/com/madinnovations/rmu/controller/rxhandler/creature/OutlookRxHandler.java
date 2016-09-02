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
package com.madinnovations.rmu.controller.rxhandler.creature;

import com.madinnovations.rmu.data.dao.creature.OutlookDao;
import com.madinnovations.rmu.data.entities.creature.Outlook;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Outlook} instances with persistent storage.
 */
public class OutlookRxHandler {
	private OutlookDao dao;

	/**
	 * Creates a new OutlookRxHandler
	 *
	 * @param dao  a OutlookDao instance
	 */
	@Inject
	public OutlookRxHandler(OutlookDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Outlook instance with the given id.
	 *
	 * @param id  the id of the Outlook to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Outlook instance.
	 */
	public Observable<Outlook> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Outlook>() {
					@Override
					public void call(Subscriber<? super Outlook> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Outlook instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Outlook
	 * instances.
	 */
	public Observable<Collection<Outlook>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Outlook>>() {
					@Override
					public void call(Subscriber<? super Collection<Outlook>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Outlook instance to persistent storage.
	 *
	 * @param outlook  the Outlook instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Outlook instance.
	 */
	public Observable<Outlook> save(final Outlook outlook) {
		return Observable.create(
				new Observable.OnSubscribe<Outlook>() {
					@Override
					public void call(Subscriber<? super Outlook> subscriber) {
						try {
							dao.save(outlook);
							subscriber.onNext(outlook);
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
	 * Creates an Observable that, when subscribed to, will delete all Outlook instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Outlook instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Outlook instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Outlook instances.
	 */
	public Observable<Collection<Outlook>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Outlook>>() {
					@Override
					public void call(Subscriber<? super Collection<Outlook>> subscriber) {
						try {
							Collection<Outlook> outlooksDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(outlooksDeleted);
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
