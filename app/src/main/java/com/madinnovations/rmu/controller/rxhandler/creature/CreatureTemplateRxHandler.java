package com.madinnovations.rmu.controller.rxhandler.creature;

import com.madinnovations.rmu.data.dao.creature.CreatureTemplateDao;
import com.madinnovations.rmu.data.entities.creature.CreatureTemplate;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link CreatureTemplate} instances with persistent storage.
 */
public class CreatureTemplateRxHandler {
	private CreatureTemplateDao dao;

	/**
	 * Creates a new CreatureTemplateRxHandler
	 *
	 * @param dao  a CreatureTemplateDao instance
	 */
	@Inject
	public CreatureTemplateRxHandler(CreatureTemplateDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a CreatureTemplate instance with the given id.
	 *
	 * @param id  the id of the CreatureTemplate to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a CreatureTemplate instance.
	 */
	public Observable<CreatureTemplate> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<CreatureTemplate>() {
					@Override
					public void call(Subscriber<? super CreatureTemplate> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all CreatureTemplate instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of CreatureTemplate
	 * instances.
	 */
	public Observable<Collection<CreatureTemplate>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CreatureTemplate>>() {
					@Override
					public void call(Subscriber<? super Collection<CreatureTemplate>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a CreatureTemplate instance to persistent storage.
	 *
	 * @param stat  the CreatureTemplate instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the CreatureTemplate instance.
	 */
	public Observable<CreatureTemplate> save(final CreatureTemplate stat) {
		return Observable.create(
				new Observable.OnSubscribe<CreatureTemplate>() {
					@Override
					public void call(Subscriber<? super CreatureTemplate> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will delete all CreatureTemplate instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CreatureTemplate instances.
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
	 * Creates an Observable that, when subscribed to, will delete all CreatureTemplate instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the CreatureTemplate instances.
	 */
	public Observable<Collection<CreatureTemplate>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<CreatureTemplate>>() {
					@Override
					public void call(Subscriber<? super Collection<CreatureTemplate>> subscriber) {
						try {
							Collection<CreatureTemplate> creatureTemplatesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(creatureTemplatesDeleted);
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
