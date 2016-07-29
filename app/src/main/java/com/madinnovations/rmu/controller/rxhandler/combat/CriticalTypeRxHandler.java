package com.madinnovations.rmu.controller.rxhandler.combat;

import com.madinnovations.rmu.data.dao.combat.CriticalTypeDao;
import com.madinnovations.rmu.data.entities.combat.CriticalType;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link CriticalType} instances with persistent storage.
 */
public class CriticalTypeRxHandler {
	private CriticalTypeDao dao;

	/**
	 * Creates a new CriticalTypeRxHandler
	 *
	 * @param dao  a CriticalTypeDao instance
	 */
	@Inject
	public CriticalTypeRxHandler(CriticalTypeDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a CriticalType instance with the given id.
	 *
	 * @param id  the id of the CriticalType to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a CriticalType instance.
	 */
	public Observable<CriticalType> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<CriticalType>() {
					@Override
					public void call(Subscriber<? super CriticalType> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all CriticalType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of CriticalType
	 * instances.
	 */
	public Observable<Collection<CriticalType>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CriticalType>>() {
					@Override
					public void call(Subscriber<? super Collection<CriticalType>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a CriticalType instance to persistent storage.
	 *
	 * @param stat  the CriticalType instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the CriticalType instance.
	 */
	public Observable<CriticalType> save(final CriticalType stat) {
		return Observable.create(
				new Observable.OnSubscribe<CriticalType>() {
					@Override
					public void call(Subscriber<? super CriticalType> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will delete all CriticalType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CriticalType instances.
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
	 * Creates an Observable that, when subscribed to, will delete all CriticalType instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CriticalType instances.
	 */
	public Observable<Collection<CriticalType>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CriticalType>>() {
					@Override
					public void call(Subscriber<? super Collection<CriticalType>> subscriber) {
						try {
							Collection<CriticalType> criticalTypesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(criticalTypesDeleted);
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
