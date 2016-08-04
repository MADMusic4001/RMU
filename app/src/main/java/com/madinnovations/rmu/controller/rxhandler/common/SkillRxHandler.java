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

import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.entities.common.Skill;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Skill} instances with persistent storage.
 */
public class SkillRxHandler {
	private SkillDao dao;

	/**
	 * Creates a new SkillRxHandler
	 *
	 * @param dao  a SkillDao instance
	 */
	@Inject
	public SkillRxHandler(SkillDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Skill instance with the given id.
	 *
	 * @param id  the id of the Skill to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Skill instance.
	 */
	public Observable<Skill> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Skill>() {
					@Override
					public void call(Subscriber<? super Skill> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Skill instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Skill
	 * instances.
	 */
	public Observable<Collection<Skill>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Skill>>() {
					@Override
					public void call(Subscriber<? super Collection<Skill>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Skill instance to persistent storage.
	 *
	 * @param skill  the Skill instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Skill instance.
	 */
	public Observable<Skill> save(final Skill skill) {
		return Observable.create(
				new Observable.OnSubscribe<Skill>() {
					@Override
					public void call(Subscriber<? super Skill> subscriber) {
						try {
							dao.save(skill);
							subscriber.onNext(skill);
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
	 * Creates an Observable that, when subscribed to, will delete all Skill instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Skill instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Skill instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Skill instances.
	 */
	public Observable<Collection<Skill>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Skill>>() {
					@Override
					public void call(Subscriber<? super Collection<Skill>> subscriber) {
						try {
							Collection<Skill> skillsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(skillsDeleted);
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
