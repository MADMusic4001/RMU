package com.madinnovations.rmu.controller.rxhandler.common;

import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.entities.common.Stat;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Stat} instances with persistent storage.
 */
public class StatRxHandler {
	private StatDao dao;

	/**
	 * Creates a new StatRxHandler
	 *
	 * @param dao  a StatDao instance
	 */
	@Inject
	public StatRxHandler(StatDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Stat instance with the given id.
	 *
	 * @param id  the id of the Stat to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Stat instance.
	 */
	public Observable<Stat> getById(final int id) {
		return Observable.create(
			new Observable.OnSubscribe<Stat>() {
				@Override
				public void call(Subscriber<? super Stat> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Stat instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Stat
	 * instances.
	 */
	public Observable<Collection<Stat>> getAll() {
		return Observable.create(
			new Observable.OnSubscribe<Collection<Stat>>() {
				@Override
				public void call(Subscriber<? super Collection<Stat>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Stat instance to persistent storage.
	 *
	 * @param stat  the Stat instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Stat instance.
	 */
	public Observable<Stat> save(final Stat stat) {
		return Observable.create(
			new Observable.OnSubscribe<Stat>() {
				@Override
				public void call(Subscriber<? super Stat> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will delete all Stat instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Stat instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Stat instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Stat instances.
	 */
	public Observable<Collection<Stat>> deleteAll() {
		return Observable.create(
			new Observable.OnSubscribe<Collection<Stat>>() {
				@Override
				public void call(Subscriber<? super Collection<Stat>> subscriber) {
					try {
						Collection<Stat> worldsDeleted = dao.getAll();
						dao.deleteAll();
						subscriber.onNext(worldsDeleted);
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
