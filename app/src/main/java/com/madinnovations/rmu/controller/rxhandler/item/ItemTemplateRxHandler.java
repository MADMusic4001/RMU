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

import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.item.ItemTemplateDao;
import com.madinnovations.rmu.data.entities.item.ItemTemplate;
import com.madinnovations.rmu.data.entities.item.Slot;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link ItemTemplate} instances with persistent storage.
 */
public class ItemTemplateRxHandler {
	private ItemTemplateDao dao;

	/**
	 * Creates a new ItemTemplateRxHandler
	 *
	 * @param dao  a ItemTemplateDao instance
	 */
	@Inject
	public ItemTemplateRxHandler(ItemTemplateDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a ItemTemplate instance with the given id.
	 *
	 * @param id  the id of the ItemTemplate to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a ItemTemplate instance.
	 */
	public Observable<ItemTemplate> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<ItemTemplate>() {
					@Override
					public void call(Subscriber<? super ItemTemplate> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all ItemTemplate instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of ItemTemplate
	 * instances.
	 */
	public Observable<Collection<ItemTemplate>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<ItemTemplate>>() {
					@Override
					public void call(Subscriber<? super Collection<ItemTemplate>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a ItemTemplate instance to persistent storage.
	 *
	 * @param item  the ItemTemplate instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the ItemTemplate instance.
	 */
	public Observable<ItemTemplate> save(final ItemTemplate item) {
		return Observable.create(
				new Observable.OnSubscribe<ItemTemplate>() {
					@Override
					public void call(Subscriber<? super ItemTemplate> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will delete all ItemTemplate instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the ItemTemplate instances.
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
	 * Creates an Observable that, when subscribed to, will delete all ItemTemplate instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the ItemTemplate instances.
	 */
	public Observable<Collection<ItemTemplate>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<ItemTemplate>>() {
					@Override
					public void call(Subscriber<? super Collection<ItemTemplate>> subscriber) {
						try {
							Collection<ItemTemplate> statsDeleted = dao.getAll();
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

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all ItemTemplate
	 * instances that can be equipped in the given slot.
	 *
	 * @param slot  a {@link Slot} instance
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of ItemTemplate
	 * instances.
	 */
	public Observable<Collection<ItemTemplate>> getAllForSlot(@NonNull final Slot slot) {
		return Observable.create(new Observable.OnSubscribe<Collection<ItemTemplate>>() {
			@Override
			public void call(Subscriber<? super Collection<ItemTemplate>> subscriber) {
				try {
					Collection<ItemTemplate> items = dao.getAllForSlot(slot);
					subscriber.onNext(items);
					subscriber.onCompleted();
				}
				catch(Exception e) {
					subscriber.onError(e);
				}
			}
		}).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all ItemTemplate
	 * instances that have no subclass.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of ItemTemplate
	 * instances.
	 */
	public Observable<Collection<ItemTemplate>> getAllWithoutSubclass() {
		return Observable.create(
				new SyncOnSubscribe<Integer, Collection<ItemTemplate>>() {
					@Override
					protected Integer generateState() {
						return null;
					}
					@Override
					protected Integer next(Integer state, Observer<? super Collection<ItemTemplate>> observer) {
						try {
							Collection<ItemTemplate> items = dao.getAllWithoutSubclass();
							observer.onNext(items);
						} catch (Exception e) {
							Exceptions.throwOrReport(e, observer);
						}
						observer.onCompleted();
						return null;
					}
				}).subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread());
	}
}
