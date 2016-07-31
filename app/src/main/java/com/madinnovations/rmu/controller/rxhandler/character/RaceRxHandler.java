package com.madinnovations.rmu.controller.rxhandler.character;

import com.madinnovations.rmu.data.dao.character.RaceDao;
import com.madinnovations.rmu.data.entities.character.Race;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Race} instances with persistent storage.
 */
public class RaceRxHandler {
	private RaceDao dao;

	/**
	 * Creates a new RaceRxHandler
	 *
	 * @param dao  a RaceDao instance
	 */
	@Inject
	public RaceRxHandler(RaceDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Race instance with the given id.
	 *
	 * @param id  the id of the Race to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Race instance.
	 */
	public Observable<Race> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Race>() {
					@Override
					public void call(Subscriber<? super Race> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Race instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Race
	 * instances.
	 */
	public Observable<Collection<Race>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Race>>() {
					@Override
					public void call(Subscriber<? super Collection<Race>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Race instance to persistent storage.
	 *
	 * @param race  the Race instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Race instance.
	 */
	public Observable<Race> save(final Race race) {
		return Observable.create(
				new Observable.OnSubscribe<Race>() {
					@Override
					public void call(Subscriber<? super Race> subscriber) {
						try {
							dao.save(race);
							subscriber.onNext(race);
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
	 * Creates an Observable that, when subscribed to, will delete all Race instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Race instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Race instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Race instances.
	 */
	public Observable<Collection<Race>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Race>>() {
					@Override
					public void call(Subscriber<? super Collection<Race>> subscriber) {
						try {
							Collection<Race> racesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(racesDeleted);
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
