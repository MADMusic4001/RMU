package com.madinnovations.rmu.controller.rxhandler.combat;

import com.madinnovations.rmu.data.dao.combat.CriticalResultDao;
import com.madinnovations.rmu.data.entities.combat.CriticalResult;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link CriticalResult} instances with persistent storage.
 */
public class CriticalResultRxHandler {
	private CriticalResultDao dao;

	/**
	 * Creates a new CriticalResultRxHandler
	 *
	 * @param dao  a CriticalResultDao instance
	 */
	@Inject
	public CriticalResultRxHandler(CriticalResultDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a CriticalResult instance with the given id.
	 *
	 * @param id  the id of the CriticalResult to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a CriticalResult instance.
	 */
	public Observable<CriticalResult> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<CriticalResult>() {
					@Override
					public void call(Subscriber<? super CriticalResult> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all CriticalResult instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of CriticalResult
	 * instances.
	 */
	public Observable<Collection<CriticalResult>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CriticalResult>>() {
					@Override
					public void call(Subscriber<? super Collection<CriticalResult>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a CriticalResult instance to persistent storage.
	 *
	 * @param stat  the CriticalResult instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the CriticalResult instance.
	 */
	public Observable<CriticalResult> save(final CriticalResult stat) {
		return Observable.create(
				new Observable.OnSubscribe<CriticalResult>() {
					@Override
					public void call(Subscriber<? super CriticalResult> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will delete all CriticalResult instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CriticalResult instances.
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
	 * Creates an Observable that, when subscribed to, will delete all CriticalResult instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CriticalResult instances.
	 */
	public Observable<Collection<CriticalResult>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CriticalResult>>() {
					@Override
					public void call(Subscriber<? super Collection<CriticalResult>> subscriber) {
						try {
							Collection<CriticalResult> criticalResultsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(criticalResultsDeleted);
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
