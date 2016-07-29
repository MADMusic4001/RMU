package com.madinnovations.rmu.controller.rxhandler.creature;

import com.madinnovations.rmu.data.dao.creature.CreatureCategoryDao;
import com.madinnovations.rmu.data.entities.creature.CreatureCategory;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link CreatureCategory} instances with persistent storage.
 */
public class CreatureCategoryRxHandler {
	private CreatureCategoryDao dao;

	/**
	 * Creates a new CreatureCategoryRxHandler
	 *
	 * @param dao  a CreatureCategoryDao instance
	 */
	@Inject
	public CreatureCategoryRxHandler(CreatureCategoryDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a CreatureCategory instance with the given id.
	 *
	 * @param id  the id of the CreatureCategory to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a CreatureCategory instance.
	 */
	public Observable<CreatureCategory> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<CreatureCategory>() {
					@Override
					public void call(Subscriber<? super CreatureCategory> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all CreatureCategory instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of CreatureCategory
	 * instances.
	 */
	public Observable<Collection<CreatureCategory>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CreatureCategory>>() {
					@Override
					public void call(Subscriber<? super Collection<CreatureCategory>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a CreatureCategory instance to persistent storage.
	 *
	 * @param stat  the CreatureCategory instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the CreatureCategory instance.
	 */
	public Observable<CreatureCategory> save(final CreatureCategory stat) {
		return Observable.create(
				new Observable.OnSubscribe<CreatureCategory>() {
					@Override
					public void call(Subscriber<? super CreatureCategory> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will delete all CreatureCategory instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CreatureCategory instances.
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
	 * Creates an Observable that, when subscribed to, will delete all CreatureCategory instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CreatureCategory instances.
	 */
	public Observable<Collection<CreatureCategory>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CreatureCategory>>() {
					@Override
					public void call(Subscriber<? super Collection<CreatureCategory>> subscriber) {
						try {
							Collection<CreatureCategory> creatureCategoriesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(creatureCategoriesDeleted);
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
