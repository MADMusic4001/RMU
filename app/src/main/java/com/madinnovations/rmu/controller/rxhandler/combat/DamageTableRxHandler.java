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

import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultRowDao;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultRowSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.DamageResultSchema;
import com.madinnovations.rmu.data.entities.combat.DamageTable;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link DamageTable} instances with persistent storage.
 */
public class DamageTableRxHandler {
	private DamageTableDao     damageTableDao;
	private DamageResultDao    damageResultDao;
	private DamageResultRowDao damageResultRowDao;

	/**
	 * Creates a new DamageTableRxHandler
	 *
	 * @param damageTableDao  a DamageTableDao instance
	 * @param damageResultRowDao  a DamageResultRowDao instance
	 * @param damageResultDao  a DamageResultDao instance
	 */
	@Inject
	DamageTableRxHandler(DamageTableDao damageTableDao, DamageResultRowDao damageResultRowDao, DamageResultDao damageResultDao) {
		this.damageTableDao = damageTableDao;
		this.damageResultRowDao = damageResultRowDao;
		this.damageResultDao = damageResultDao;
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
							subscriber.onNext(damageTableDao.getById(id));
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
							subscriber.onNext(damageTableDao.getAll());
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
							damageTableDao.save(damageTable);
							subscriber.onNext(damageTable);
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
							subscriber.onNext(damageTableDao.deleteById(id));
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
							Collection<DamageTable> damageTablesDeleted = damageTableDao.getAll();
							damageTableDao.deleteAll();
							subscriber.onNext(damageTablesDeleted);
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

	public Observable<Boolean> delete(final DamageTable damageTable) {
		return Observable.create(new Observable.OnSubscribe<Boolean>() {
			@Override
			public void call(Subscriber<? super Boolean> subscriber) {
				try {
					String where = DamageResultSchema.COLUMN_DAMAGE_RESULT_ROW_ID + " = ?";
					damageTableDao.beginTransaction();
					for(int i = 0; i < damageTable.getResultRows().size(); i++) {
						String[] whereArgs = {String.valueOf(damageTable.getResultRows().valueAt(i).getId())};
						damageResultDao.deleteWithFilter(where, whereArgs);
					}
					where = DamageResultRowSchema.COLUMN_DAMAGE_TABLE_ID + " = ?";
					String[] whereArgs = {String.valueOf(damageTable.getId())};
					damageResultRowDao.deleteWithFilter(where, whereArgs);
					damageTableDao.delete(damageTable);
					damageTableDao.endTransaction(true);
					subscriber.onNext(true);
					subscriber.onCompleted();
				}
				catch(Exception e) {
					subscriber.onError(e);
				}
			}
		});
	}
}
