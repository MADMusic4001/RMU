package com.madinnovations.rmu.controller.rxhandler.character;

import com.madinnovations.rmu.data.dao.character.CultureDao;
import com.madinnovations.rmu.data.entities.character.Culture;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Culture} instances with persistent storage.
 */
public class CultureRxHandler {
	private CultureDao dao;

	/**
	 * Creates a new CultureRxHandler
	 *
	 * @param dao  a CultureDao instance
	 */
	@Inject
	public CultureRxHandler(CultureDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Culture instance with the given id.
	 *
	 * @param id  the id of the Culture to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Culture instance.
	 */
	public Observable<Culture> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Culture>() {
					@Override
					public void call(Subscriber<? super Culture> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Culture instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Culture
	 * instances.
	 */
	public Observable<Collection<Culture>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Culture>>() {
					@Override
					public void call(Subscriber<? super Collection<Culture>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Culture instance to persistent storage.
	 *
	 * @param stat  the Culture instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Culture instance.
	 */
	public Observable<Culture> save(final Culture stat) {
		return Observable.create(
				new Observable.OnSubscribe<Culture>() {
					@Override
					public void call(Subscriber<? super Culture> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will delete all Culture instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Culture instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Culture instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Culture instances.
	 */
	public Observable<Collection<Culture>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Culture>>() {
					@Override
					public void call(Subscriber<? super Collection<Culture>> subscriber) {
						try {
							Collection<Culture> culturesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(culturesDeleted);
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
