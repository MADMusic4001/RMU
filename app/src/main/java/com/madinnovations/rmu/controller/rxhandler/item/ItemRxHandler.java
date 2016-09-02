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

import com.madinnovations.rmu.data.dao.item.ItemDao;
import com.madinnovations.rmu.data.entities.object.Item;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Item} instances with persistent storage.
 */
public class ItemRxHandler {
	private ItemDao dao;

	/**
	 * Creates a new ItemRxHandler
	 *
	 * @param dao  a ItemDao instance
	 */
	@Inject
	public ItemRxHandler(ItemDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Item instance with the given id.
	 *
	 * @param id  the id of the Item to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Item instance.
	 */
	public Observable<Item> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Item>() {
					@Override
					public void call(Subscriber<? super Item> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Item instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Item
	 * instances.
	 */
	public Observable<Collection<Item>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Item>>() {
					@Override
					public void call(Subscriber<? super Collection<Item>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Item instance to persistent storage.
	 *
	 * @param item  the Item instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Item instance.
	 */
	public Observable<Item> save(final Item item) {
		return Observable.create(
				new Observable.OnSubscribe<Item>() {
					@Override
					public void call(Subscriber<? super Item> subscriber) {
						try {
							dao.save(item);
							subscriber.onNext(item);
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
	 * Creates an Observable that, when subscribed to, will delete all Item instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Item instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Item instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Item instances.
	 */
	public Observable<Collection<Item>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Item>>() {
					@Override
					public void call(Subscriber<? super Collection<Item>> subscriber) {
						try {
							Collection<Item> statsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(statsDeleted);
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
