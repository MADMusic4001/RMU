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
package com.madinnovations.rmu.controller.rxhandler.spell;

import com.madinnovations.rmu.data.dao.spells.SpellDao;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.spells.Spell;
import com.madinnovations.rmu.data.entities.spells.SpellList;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Spell} instances with persistent storage.
 */
public class SpellRxHandler {
	private SpellDao dao;

	/**
	 * Creates a new SpellRxHandler
	 *
	 * @param dao  a SpellDao instance
	 */
	@Inject
	public SpellRxHandler(SpellDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Spell instance with the given id.
	 *
	 * @param id  the id of the Spell to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Spell instance.
	 */
	public Observable<Spell> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Spell>() {
					@Override
					public void call(Subscriber<? super Spell> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Spell instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Spell
	 * instances.
	 */
	public Observable<Collection<Spell>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Spell>>() {
					@Override
					public void call(Subscriber<? super Collection<Spell>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Spell instance to persistent storage.
	 *
	 * @param spell  the Spell instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Spell instance.
	 */
	public Observable<Spell> save(final Spell spell) {
		return Observable.create(
				new Observable.OnSubscribe<Spell>() {
					@Override
					public void call(Subscriber<? super Spell> subscriber) {
						try {
							dao.save(spell);
							subscriber.onNext(spell);
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
	 * Creates an Observable that, when subscribed to, will delete all Spell instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Spell instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Spell instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Spell instances.
	 */
	public Observable<Collection<Spell>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Spell>>() {
					@Override
					public void call(Subscriber<? super Collection<Spell>> subscriber) {
						try {
							Collection<Spell> spellsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(spellsDeleted);
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Spell instances that
	 * reference the given SpellList.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Spell instances.
	 */
	public Observable<Collection<Spell>> getSpellsForList(final SpellList filter) {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Spell>>() {
					@Override
					public void call(Subscriber<? super Collection<Spell>> subscriber) {
						try {
							subscriber.onNext(dao.getSpellsForList(filter));
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
