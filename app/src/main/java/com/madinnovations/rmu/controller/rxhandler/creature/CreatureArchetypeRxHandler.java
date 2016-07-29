package com.madinnovations.rmu.controller.rxhandler.creature;

import com.madinnovations.rmu.data.dao.creature.CreatureArchetypeDao;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link CreatureArchetype} instances with persistent storage.
 */
public class CreatureArchetypeRxHandler {
	private CreatureArchetypeDao dao;

	/**
	 * Creates a new CreatureArchetypeRxHandler
	 *
	 * @param dao  a CreatureArchetypeDao instance
	 */
	@Inject
	public CreatureArchetypeRxHandler(CreatureArchetypeDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a CreatureArchetype instance with the given id.
	 *
	 * @param id  the id of the CreatureArchetype to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a CreatureArchetype instance.
	 */
	public Observable<CreatureArchetype> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<CreatureArchetype>() {
					@Override
					public void call(Subscriber<? super CreatureArchetype> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all CreatureArchetype instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of CreatureArchetype
	 * instances.
	 */
	public Observable<Collection<CreatureArchetype>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CreatureArchetype>>() {
					@Override
					public void call(Subscriber<? super Collection<CreatureArchetype>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a CreatureArchetype instance to persistent storage.
	 *
	 * @param stat  the CreatureArchetype instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the CreatureArchetype instance.
	 */
	public Observable<CreatureArchetype> save(final CreatureArchetype stat) {
		return Observable.create(
				new Observable.OnSubscribe<CreatureArchetype>() {
					@Override
					public void call(Subscriber<? super CreatureArchetype> subscriber) {
						try {
							dao.save(stat);
							subscriber.onNext(stat);
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
	 * Creates an Observable that, when subscribed to, will delete all CreatureArchetype instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CreatureArchetype instances.
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
	 * Creates an Observable that, when subscribed to, will delete all CreatureArchetype instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CreatureArchetype instances.
	 */
	public Observable<Collection<CreatureArchetype>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CreatureArchetype>>() {
					@Override
					public void call(Subscriber<? super Collection<CreatureArchetype>> subscriber) {
						try {
							Collection<CreatureArchetype> creatureArchetypesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(creatureArchetypesDeleted);
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
