package com.madinnovations.rmu.controller.rxhandler.combat;

import com.madinnovations.rmu.data.dao.combat.BodyPartDao;
import com.madinnovations.rmu.data.entities.combat.BodyPart;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link BodyPart} instances with persistent storage.
 */
public class BodyPartRxHandler {
	private BodyPartDao dao;

	/**
	 * Creates a new BodyPartRxHandler
	 *
	 * @param dao  a BodyPartDao instance
	 */
	@Inject
	public BodyPartRxHandler(BodyPartDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a BodyPart instance with the given id.
	 *
	 * @param id  the id of the BodyPart to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a BodyPart instance.
	 */
	public Observable<BodyPart> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<BodyPart>() {
					@Override
					public void call(Subscriber<? super BodyPart> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all BodyPart instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of BodyPart
	 * instances.
	 */
	public Observable<Collection<BodyPart>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<BodyPart>>() {
					@Override
					public void call(Subscriber<? super Collection<BodyPart>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a BodyPart instance to persistent storage.
	 *
	 * @param bodyPart  the BodyPart instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the BodyPart instance.
	 */
	public Observable<BodyPart> save(final BodyPart bodyPart) {
		return Observable.create(
				new Observable.OnSubscribe<BodyPart>() {
					@Override
					public void call(Subscriber<? super BodyPart> subscriber) {
						try {
							dao.save(bodyPart);
							subscriber.onNext(bodyPart);
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
	 * Creates an Observable that, when subscribed to, will delete all BodyPart instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the BodyPart instances.
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
	 * Creates an Observable that, when subscribed to, will delete all BodyPart instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the BodyPart instances.
	 */
	public Observable<Collection<BodyPart>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<BodyPart>>() {
					@Override
					public void call(Subscriber<? super Collection<BodyPart>> subscriber) {
						try {
							Collection<BodyPart> bodyPartsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(bodyPartsDeleted);
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
