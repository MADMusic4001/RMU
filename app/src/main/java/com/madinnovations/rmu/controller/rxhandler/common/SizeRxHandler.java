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

import com.madinnovations.rmu.data.dao.common.SizeDao;
import com.madinnovations.rmu.data.entities.common.Size;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Size} instances with persistent storage.
 */
public class SizeRxHandler {
	private SizeDao dao;

	/**
	 * Creates a new SizeRxHandler
	 *
	 * @param dao  a SizeDao instance
	 */
	@Inject
	public SizeRxHandler(SizeDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Size instance with the given id.
	 *
	 * @param id  the id of the Size to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Size instance.
	 */
	public Observable<Size> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Size>() {
					@Override
					public void call(Subscriber<? super Size> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Size instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Size
	 * instances.
	 */
	public Observable<Collection<Size>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Size>>() {
					@Override
					public void call(Subscriber<? super Collection<Size>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Size instance to persistent storage.
	 *
	 * @param size  the Size instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Size instance.
	 */
	public Observable<Size> save(final Size size) {
		return Observable.create(
				new Observable.OnSubscribe<Size>() {
					@Override
					public void call(Subscriber<? super Size> subscriber) {
						try {
							dao.save(size);
							subscriber.onNext(size);
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
	 * Creates an Observable that, when subscribed to, will delete all Size instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Size instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Size instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Size instances.
	 */
	public Observable<Collection<Size>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Size>>() {
					@Override
					public void call(Subscriber<? super Collection<Size>> subscriber) {
						try {
							Collection<Size> sizesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(sizesDeleted);
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
