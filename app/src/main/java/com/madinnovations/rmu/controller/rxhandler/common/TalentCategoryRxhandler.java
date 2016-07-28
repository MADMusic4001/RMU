package com.madinnovations.rmu.controller.rxhandler.common;

import android.util.Log;

import com.madinnovations.rmu.data.dao.common.TalentCategoryDao;
import com.madinnovations.rmu.data.entities.common.TalentCategory;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link TalentCategory} instances with persistent storage.
 */
public class TalentCategoryRxHandler {
	private TalentCategoryDao dao;

	/**
	 * Creates a new TalentCategoryRxHandler
	 * 
	 * @param dao  a TalentCategoryDao instance
	 */
	@Inject
	public TalentCategoryRxHandler(TalentCategoryDao dao) {
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a TalentCategory instance with the given id.
	 *
	 * @param id  the id of the TalentCategory to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a TalentCategory instance.
	 */
	public Observable<TalentCategory> getById(final int id) {
		return Observable.create(
			new Observable.OnSubscribe<TalentCategory>() {
				@Override
				public void call(Subscriber<? super TalentCategory> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all TalentCategory instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of TalentCategory
	 * instances.
	 */
	public Observable<Collection<TalentCategory>> getAll() {
		Log.e("WorldHandler", "getting talentCategories");
		return Observable.create(
			new Observable.OnSubscribe<Collection<TalentCategory>>() {
				@Override
				public void call(Subscriber<? super Collection<TalentCategory>> subscriber) {
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
	 * Creates an Observable that, when subscribed to, will save a TalentCategory instance to persistent storage.
	 *
	 * @param talentCategory  the TalentCategory instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the TalentCategory instance.
	 */
	public Observable<TalentCategory> save(final TalentCategory talentCategory) {
		return Observable.create(
			new Observable.OnSubscribe<TalentCategory>() {
				@Override
				public void call(Subscriber<? super TalentCategory> subscriber) {
					try {
						dao.save(talentCategory);
						subscriber.onNext(talentCategory);
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
	 * Creates an Observable that, when subscribed to, will delete all TalentCategory instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the TalentCategory instances.
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
	 * Creates an Observable that, when subscribed to, will delete all TalentCategory instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the TalentCategory instances.
	 */
	public Observable<Collection<TalentCategory>> deleteAll() {
		return Observable.create(
			new Observable.OnSubscribe<Collection<TalentCategory>>() {
				@Override
				public void call(Subscriber<? super Collection<TalentCategory>> subscriber) {
					try {
						Collection<TalentCategory> talentCategoriesDeleted = dao.getAll();
						dao.deleteAll();
						subscriber.onNext(talentCategoriesDeleted);
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
