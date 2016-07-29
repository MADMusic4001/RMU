package com.madinnovations.rmu.controller.rxhandler.combat;

import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.entities.combat.DamageResult;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link DamageResult} instances with persistent storage.
 */
public class DamageResultRxHandler {
	private DamageResultDao dao;

	/**
	 * Creates a new DamageResultRxHandler
	 *
	 * @param dao  a DamageResultDao instance
	 */
	@Inject
	public DamageResultRxHandler(DamageResultDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a DamageResult instance with the given id.
	 *
	 * @param id  the id of the DamageResult to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a DamageResult instance.
	 */
	public Observable<DamageResult> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<DamageResult>() {
					@Override
					public void call(Subscriber<? super DamageResult> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all DamageResult instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of DamageResult
	 * instances.
	 */
	public Observable<Collection<DamageResult>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<DamageResult>>() {
					@Override
					public void call(Subscriber<? super Collection<DamageResult>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a DamageResult instance to persistent storage.
	 *
	 * @param stat  the DamageResult instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the DamageResult instance.
	 */
	public Observable<DamageResult> save(final DamageResult stat) {
		return Observable.create(
				new Observable.OnSubscribe<DamageResult>() {
					@Override
					public void call(Subscriber<? super DamageResult> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will delete all DamageResult instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the DamageResult instances.
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
	 * Creates an Observable that, when subscribed to, will delete all DamageResult instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the DamageResult instances.
	 */
	public Observable<Collection<DamageResult>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<DamageResult>>() {
					@Override
					public void call(Subscriber<? super Collection<DamageResult>> subscriber) {
						try {
							Collection<DamageResult> damageResultsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(damageResultsDeleted);
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
