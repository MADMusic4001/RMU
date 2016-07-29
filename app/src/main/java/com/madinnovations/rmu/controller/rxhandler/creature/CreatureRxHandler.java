package com.madinnovations.rmu.controller.rxhandler.creature;

import com.madinnovations.rmu.data.dao.creature.CreatureDao;
import com.madinnovations.rmu.data.entities.creature.Creature;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Creature} instances with persistent storage.
 */
public class CreatureRxHandler {
	private CreatureDao dao;

	/**
	 * Creates a new CreatureRxHandler
	 *
	 * @param dao  a CreatureDao instance
	 */
	@Inject
	public CreatureRxHandler(CreatureDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Creature instance with the given id.
	 *
	 * @param id  the id of the Creature to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Creature instance.
	 */
	public Observable<Creature> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Creature>() {
					@Override
					public void call(Subscriber<? super Creature> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Creature instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Creature
	 * instances.
	 */
	public Observable<Collection<Creature>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Creature>>() {
					@Override
					public void call(Subscriber<? super Collection<Creature>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Creature instance to persistent storage.
	 *
	 * @param stat  the Creature instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Creature instance.
	 */
	public Observable<Creature> save(final Creature stat) {
		return Observable.create(
				new Observable.OnSubscribe<Creature>() {
					@Override
					public void call(Subscriber<? super Creature> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will delete all Creature instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Creature instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Creature instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Creature instances.
	 */
	public Observable<Collection<Creature>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Creature>>() {
					@Override
					public void call(Subscriber<? super Collection<Creature>> subscriber) {
						try {
							Collection<Creature> creaturesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(creaturesDeleted);
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
