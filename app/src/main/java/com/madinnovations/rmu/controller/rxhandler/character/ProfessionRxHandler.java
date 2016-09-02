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
package com.madinnovations.rmu.controller.rxhandler.character;

import com.madinnovations.rmu.data.dao.character.ProfessionDao;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.character.ProfessionSkillCategoryCost;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Profession} instances with persistent storage.
 */
public class ProfessionRxHandler {
	private ProfessionDao dao;

	/**
	 * Creates a new ProfessionRxHandler
	 *
	 * @param dao  a ProfessionDao instance
	 */
	@Inject
	public ProfessionRxHandler(ProfessionDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Profession instance with the given id.
	 *
	 * @param id  the id of the Profession to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Profession instance.
	 */
	public Observable<Profession> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Profession>() {
					@Override
					public void call(Subscriber<? super Profession> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Profession instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Profession
	 * instances.
	 */
	public Observable<Collection<Profession>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Profession>>() {
					@Override
					public void call(Subscriber<? super Collection<Profession>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Profession instance to persistent storage.
	 *
	 * @param profession  the Profession instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Profession instance.
	 */
	public Observable<Profession> save(final Profession profession) {
		return Observable.create(
				new Observable.OnSubscribe<Profession>() {
					@Override
					public void call(Subscriber<? super Profession> subscriber) {
						try {
							dao.save(profession);
							subscriber.onNext(profession);
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
	 * Creates an Observable that, when subscribed to, will delete all Profession instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Profession instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Profession instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Profession instances.
	 */
	public Observable<Collection<Profession>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Profession>>() {
					@Override
					public void call(Subscriber<? super Collection<Profession>> subscriber) {
						try {
							Collection<Profession> professionsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(professionsDeleted);
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

	public Observable<ProfessionSkillCategoryCost> saveSkillCategoryCost(final ProfessionSkillCategoryCost professionSkillCategoryCost) {
		return Observable.create(
				new Observable.OnSubscribe<ProfessionSkillCategoryCost>() {
					@Override
					public void call(Subscriber<? super ProfessionSkillCategoryCost> subscriber) {
						try {
							dao.saveSkillCategoryCost(professionSkillCategoryCost);
							subscriber.onNext(professionSkillCategoryCost);
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
