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

import com.madinnovations.rmu.data.dao.common.TalentCategoryDao;
import com.madinnovations.rmu.data.entities.common.TalentCategory;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link TalentCategory} instances with persistent storage.
 */
public class TalentCategoryRxHandler {
	private TalentCategoryDao dao;

	/**
	 * Creates a new TalentCategoryRxHandler
	 * 
	 * @param dao  a TalentCategoryDao instance
	 */
	@Inject
	public TalentCategoryRxHandler(TalentCategoryDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a TalentCategory instance with the given id.
	 *
	 * @param id  the id of the TalentCategory to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a TalentCategory instance.
	 */
	public Observable<TalentCategory> getById(final int id) {
		return Observable.create(
			new Observable.OnSubscribe<TalentCategory>() {
				@Override
				public void call(Subscriber<? super TalentCategory> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all TalentCategory instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of TalentCategory
	 * instances.
	 */
	public Observable<Collection<TalentCategory>> getAll() {
		return Observable.create(
			new Observable.OnSubscribe<Collection<TalentCategory>>() {
				@Override
				public void call(Subscriber<? super Collection<TalentCategory>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a TalentCategory instance to persistent storage.
	 *
	 * @param talentCategory  the TalentCategory instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the TalentCategory instance.
	 */
	public Observable<TalentCategory> save(final TalentCategory talentCategory) {
		return Observable.create(
			new Observable.OnSubscribe<TalentCategory>() {
				@Override
				public void call(Subscriber<? super TalentCategory> subscriber) {
					try {
						dao.save(talentCategory);
						subscriber.onNext(talentCategory);
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
	 * Creates an Observable that, when subscribed to, will delete all TalentCategory instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the TalentCategory instances.
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
	 * Creates an Observable that, when subscribed to, will delete all TalentCategory instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the TalentCategory instances.
	 */
	public Observable<Collection<TalentCategory>> deleteAll() {
		return Observable.create(
			new Observable.OnSubscribe<Collection<TalentCategory>>() {
				@Override
				public void call(Subscriber<? super Collection<TalentCategory>> subscriber) {
					try {
						Collection<TalentCategory> talentCategoriesDeleted = dao.getAll();
						dao.deleteAll();
						subscriber.onNext(talentCategoriesDeleted);
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
