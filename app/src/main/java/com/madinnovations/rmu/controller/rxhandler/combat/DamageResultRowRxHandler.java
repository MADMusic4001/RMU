/**
 * Copyright (C) 2016 MadInnovations
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.controller.rxhandler.combat;

import com.madinnovations.rmu.data.dao.RMUDatabaseHelper;
import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultRowDao;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultRowSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultSchema;
import com.madinnovations.rmu.data.entities.combat.DamageResult;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.data.entities.combat.DamageTable;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link DamageResultRow} instances with persistent storage.
 */
public class DamageResultRowRxHandler {
	private RMUDatabaseHelper helper;
	private DamageResultRowDao dao;
	private DamageResultDao damageResultDao;
	private DamageTableDao damageTableDao;

	/**
	 * Creates a new DamageResultRowRxHandler
	 *
	 * @param dao  a DamageResultRowDao instance
	 */
	@Inject
	public DamageResultRowRxHandler(RMUDatabaseHelper helper, DamageResultRowDao dao) {
		this.helper = helper;
		this.dao = dao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a DamageResultRow instance with the given id.
	 *
	 * @param id  the id of the DamageResultRow to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a DamageResultRow instance.
	 */
	public Observable<DamageResultRow> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<DamageResultRow>() {
					@Override
					public void call(Subscriber<? super DamageResultRow> subscriber) {
						try {
							subscriber.onNext(dao.getById(id));
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all DamageResultRow instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of DamageResultRow
	 * instances.
	 */
	public Observable<Collection<DamageResultRow>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<DamageResultRow>>() {
					@Override
					public void call(Subscriber<? super Collection<DamageResultRow>> subscriber) {
						try {
							subscriber.onNext(dao.getAll());
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will save a DamageResultRow instance to persistent storage.
	 *
	 * @param damageResultRow  the DamageResultRow instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the DamageResultRow instance.
	 */
	public Observable<DamageResultRow> save(final DamageResultRow damageResultRow) {
		return Observable.create(
				new Observable.OnSubscribe<DamageResultRow>() {
					@Override
					public void call(Subscriber<? super DamageResultRow> subscriber) {
						try {
							dao.save(damageResultRow);
							subscriber.onNext(damageResultRow);
							subscriber.onCompleted();
						}
						catch(Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will delete all DamageResultRow instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the DamageResultRow instances.
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
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will delete all DamageResultRow instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the DamageResultRow instances.
	 */
	public Observable<Collection<DamageResultRow>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<DamageResultRow>>() {
					@Override
					public void call(Subscriber<? super Collection<DamageResultRow>> subscriber) {
						try {
							Collection<DamageResultRow> damageResultRowsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(damageResultRowsDeleted);
							subscriber.onCompleted();
						}
						catch(Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all DamageResultRow instances that
	 * reference the given DamageTable.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of DamageResultRow instances.
	 */
	public Observable<Collection<DamageResultRow>> getDamageResultRowsForDamageTable(final DamageTable filter) {
		return Observable.create(
				new Observable.OnSubscribe<Collection<DamageResultRow>>() {
					@Override
					public void call(Subscriber<? super Collection<DamageResultRow>> subscriber) {
						try {
							Collection<DamageResultRow> rows = dao.getDamageResultRowsForDamageTable(filter);
							subscriber.onNext(rows);
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}


	/**
	 * Creates an Observable that, when subscribed to, will delete all DamageResultRow instances meeting the given criteria.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the DamageResultRow instances.
	 */
	public Observable<Collection<DamageResultRow>> deleteAllForDamageTable(final DamageTable damageTable) {
		return Observable.create(
				new Observable.OnSubscribe<Collection<DamageResultRow>>() {
					@Override
					public void call(Subscriber<? super Collection<DamageResultRow>> subscriber) {
						try {
							helper.getWritableDatabase().beginTransaction();
							helper.getWritableDatabase().execSQL(
									"delete from " + DamageResultSchema.TABLE_NAME
											+ " where " + DamageResultSchema.COLUMN_ID
											+ " in  (Select " + DamageResultRowSchema.COLUMN_ID  + " from "
											+ DamageResultRowSchema.TABLE_NAME + " where "
											+ DamageResultRowSchema.COLUMN_DAMAGE_TABLE_ID + " = "
											+ damageTable.getId());
							helper.getWritableDatabase().execSQL(
									"delete from damage_results_row where id in "
																		 + "(Select id FROM damage_result_rows where damageTableId = )" + damageTable.getId());
							subscriber.onNext(null);
							subscriber.onCompleted();
						}
						catch(Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}
}
