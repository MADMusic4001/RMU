package com.madinnovations.rmu.controller.rxhandler.common;

import com.madinnovations.rmu.data.dao.common.ParameterDao;
import com.madinnovations.rmu.data.entities.common.Parameter;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Parameter} instances with persistent storage.
 */
public class ParameterRxHandler {
	private ParameterDao dao;

	/**
	 * Creates a new ParameterRxHandler
	 *
	 * @param dao  a ParameterDao instance
	 */
	@Inject
	public ParameterRxHandler(ParameterDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Parameter instance with the given id.
	 *
	 * @param id  the id of the Parameter to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Parameter instance.
	 */
	public Observable<Parameter> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Parameter>() {
					@Override
					public void call(Subscriber<? super Parameter> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Parameter instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Parameter
	 * instances.
	 */
	public Observable<Collection<Parameter>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Parameter>>() {
					@Override
					public void call(Subscriber<? super Collection<Parameter>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a Parameter instance to persistent storage.
	 *
	 * @param stat  the Parameter instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Parameter instance.
	 */
	public Observable<Parameter> save(final Parameter stat) {
		return Observable.create(
				new Observable.OnSubscribe<Parameter>() {
					@Override
					public void call(Subscriber<? super Parameter> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will delete all Parameter instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Parameter instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Parameter instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Parameter instances.
	 */
	public Observable<Collection<Parameter>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Parameter>>() {
					@Override
					public void call(Subscriber<? super Collection<Parameter>> subscriber) {
						try {
							Collection<Parameter> parametersDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(parametersDeleted);
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
