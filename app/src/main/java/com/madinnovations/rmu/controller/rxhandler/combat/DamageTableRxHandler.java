package com.madinnovations.rmu.controller.rxhandler.combat;

import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.entities.combat.DamageTable;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link DamageTable} instances with persistent storage.
 */
public class DamageTableRxHandler {
	private DamageTableDao dao;

	/**
	 * Creates a new DamageTableRxHandler
	 *
	 * @param dao  a DamageTableDao instance
	 */
	@Inject
	public DamageTableRxHandler(DamageTableDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a DamageTable instance with the given id.
	 *
	 * @param id  the id of the DamageTable to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a DamageTable instance.
	 */
	public Observable<DamageTable> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<DamageTable>() {
					@Override
					public void call(Subscriber<? super DamageTable> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all DamageTable instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of DamageTable
	 * instances.
	 */
	public Observable<Collection<DamageTable>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<DamageTable>>() {
					@Override
					public void call(Subscriber<? super Collection<DamageTable>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a DamageTable instance to persistent storage.
	 *
	 * @param damageTable  the DamageTable instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the DamageTable instance.
	 */
	public Observable<DamageTable> save(final DamageTable damageTable) {
		return Observable.create(
				new Observable.OnSubscribe<DamageTable>() {
					@Override
					public void call(Subscriber<? super DamageTable> subscriber) {
						try {
							dao.save(damageTable);
							subscriber.onNext(damageTable);
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
	 * Creates an Observable that, when subscribed to, will delete all DamageTable instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the DamageTable instances.
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
	 * Creates an Observable that, when subscribed to, will delete all DamageTable instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the DamageTable instances.
	 */
	public Observable<Collection<DamageTable>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<DamageTable>>() {
					@Override
					public void call(Subscriber<? super Collection<DamageTable>> subscriber) {
						try {
							Collection<DamageTable> damageTablesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(damageTablesDeleted);
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
