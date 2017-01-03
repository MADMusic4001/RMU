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
package com.madinnovations.rmu.controller.rxhandler.combat;

import android.util.SparseArray;

import com.madinnovations.rmu.data.dao.combat.DiseaseDao;
import com.madinnovations.rmu.data.entities.combat.Disease;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Disease} instances with persistent storage.
 */
public class DiseaseRxHandler {
	private SparseArray<Disease> Diseases = null;
	private DiseaseDao dao;

	/**
	 * Creates a new DiseaseRxHandler
	 *
	 * @param dao  a DiseaseDao instance
	 */
	@Inject
	public DiseaseRxHandler(DiseaseDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Disease instance with the given id.
	 *
	 * @param id  the id of the Disease to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Disease instance.
	 */
	public Observable<Disease> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Disease>() {
					@Override
					public void call(Subscriber<? super Disease> subscriber) {
						try {
							Disease Disease = null;
							if(Diseases != null) {
								Disease = Diseases.get(id);
							}
							if(Disease == null) {
								Disease = dao.getById(id);
							}
							subscriber.onNext(Disease);
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Disease instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Disease
	 * instances.
	 */
	public Observable<Collection<Disease>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Disease>>() {
					@Override
					public void call(Subscriber<? super Collection<Disease>> subscriber) {
						try {
							Collection<Disease> list;
							if(Diseases == null) {
								list = dao.getAll();
								Diseases = new SparseArray<>(list.size());
								for(Disease Disease : list) {
									Diseases.append(Disease.getId(), Disease);
								}
							}
							else {
								list = new ArrayList<>(Diseases.size());
								for(int i = 0; i < Diseases.size(); i++) {
									list.add(Diseases.valueAt(i));
								}
							}
							subscriber.onNext(list);
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
	 * Creates an Observable that, when subscribed to, will save a Disease instance to persistent storage.
	 *
	 * @param Disease  the Disease instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Disease instance.
	 */
	public Observable<Disease> save(final Disease Disease) {
		return Observable.create(
				new Observable.OnSubscribe<Disease>() {
					@Override
					public void call(Subscriber<? super Disease> subscriber) {
						try {
							dao.save(Disease);
							if(Diseases != null && Diseases.get(Disease.getId()) == null) {
								Diseases.append(Disease.getId(), Disease);
							}
							subscriber.onNext(Disease);
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
	 * Creates an Observable that, when subscribed to, will delete all Disease instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Disease instances.
	 */
	public Observable<Boolean> deleteById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Boolean>() {
					@Override
					public void call(Subscriber<? super Boolean> subscriber) {
						try {
							subscriber.onNext(dao.deleteById(id));
							if(Diseases != null && Diseases.get(id) != null) {
								Diseases.remove(id);
							}
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
	 * Creates an Observable that, when subscribed to, will delete all Disease instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Disease instances.
	 */
	public Observable<Collection<Disease>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Disease>>() {
					@Override
					public void call(Subscriber<? super Collection<Disease>> subscriber) {
						try {
							Collection<Disease> DiseasesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(DiseasesDeleted);
							if(Diseases != null) {
								Diseases.clear();
							}
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
