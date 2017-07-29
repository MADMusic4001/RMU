/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.controller.rxhandler.item;

import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultRowDao;
import com.madinnovations.rmu.data.dao.item.WeaponDao;
import com.madinnovations.rmu.data.entities.combat.DamageResult;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.object.Slot;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Weapon} instances with persistent storage.
 */
public class WeaponRxHandler {
	private WeaponDao dao;
	private DamageResultDao damageResultDao;
	private DamageResultRowDao damageResultRowDao;

	/**
	 * Creates a new WeaponRxHandler
	 *
	 * @param dao  a {@link WeaponDao} instance
	 * @param damageResultDao  a {@link DamageResultDao} instance
	 * @param damageResultRowDao  a {@link DamageResultRowDao} instance
	 */
	@Inject
	public WeaponRxHandler(WeaponDao dao, DamageResultDao damageResultDao, DamageResultRowDao damageResultRowDao) {
		this.dao = dao;
		this.damageResultDao = damageResultDao;
		this.damageResultRowDao = damageResultRowDao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Weapon instance with the given id.
	 *
	 * @param id  the id of the Weapon to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Weapon instance.
	 */
	public Observable<Weapon> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Weapon>() {
					@Override
					public void call(Subscriber<? super Weapon> subscriber) {
						try {
							Weapon weapon = dao.getById(id);
							if(weapon != null && weapon.getItemTemplate() != null &&
									weapon.getItemTemplate() instanceof WeaponTemplate) {
								DamageTable damageTable = ((WeaponTemplate) weapon.getItemTemplate()).getDamageTable();
								Collection<DamageResultRow> resultRows = damageResultRowDao.
										getDamageResultRowsForDamageTable(damageTable);
								for (DamageResultRow damageResultRow : resultRows) {
									Collection<DamageResult> results = damageResultDao.getDamageResultsForRow(damageResultRow);
									for (DamageResult damageResult : results) {
										damageResultRow.getResults().put(damageResult.getArmorType(), damageResult);
									}
									damageTable.getResultRows().put(damageResultRow.getRangeHighValue(), damageResultRow);
								}
							}
							subscriber.onNext(weapon);
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Weapon instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Weapon
	 * instances.
	 */
	public Observable<Collection<Weapon>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Weapon>>() {
					@Override
					public void call(Subscriber<? super Collection<Weapon>> subscriber) {
						try {
							Collection<Weapon> weapons = dao.getAll();
							for(Weapon weapon : weapons) {
								DamageTable damageTable = ((WeaponTemplate)weapon.getItemTemplate()).getDamageTable();
								Collection<DamageResultRow> resultRows = damageResultRowDao.
										getDamageResultRowsForDamageTable(damageTable);
								for(DamageResultRow damageResultRow : resultRows) {
									Collection<DamageResult> results = damageResultDao.getDamageResultsForRow(damageResultRow);
									for(DamageResult damageResult : results) {
										damageResultRow.getResults().put(damageResult.getArmorType(), damageResult);
									}
									damageTable.getResultRows().put(damageResultRow.getRangeHighValue(), damageResultRow);
								}
							}
							subscriber.onNext(weapons);
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
	 * Creates an Observable that, when subscribed to, will save a Weapon instance to persistent storage.
	 *
	 * @param weapon  the Weapon instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Weapon instance.
	 */
	public Observable<Weapon> save(final Weapon weapon) {
		return Observable.create(
				new Observable.OnSubscribe<Weapon>() {
					@Override
					public void call(Subscriber<? super Weapon> subscriber) {
						try {
							dao.save(weapon);
							subscriber.onNext(weapon);
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
	 * Creates an Observable that, when subscribed to, will delete all Weapon instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Weapon instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Weapon instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Weapon instances.
	 */
	public Observable<Collection<Weapon>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Weapon>>() {
					@Override
					public void call(Subscriber<? super Collection<Weapon>> subscriber) {
						try {
							Collection<Weapon> weaponsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(weaponsDeleted);
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Weapon instances
	 * that can be equipped in the given slot.
	 *
	 * @param slot  a {@link Slot} instance
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Weapon
	 * instances.
	 */
	public Observable<Collection<Weapon>> getAllForSlot(@NonNull final Slot slot) {
		return Observable.create(new Observable.OnSubscribe<Collection<Weapon>>() {
			@Override
			public void call(Subscriber<? super Collection<Weapon>> subscriber) {
				try {
					Collection<Weapon> weapons = dao.getAllForSlot(slot);
					for(Weapon weapon : weapons) {
						DamageTable damageTable = ((WeaponTemplate)weapon.getItemTemplate()).getDamageTable();
						Collection<DamageResultRow> resultRows = damageResultRowDao.
								getDamageResultRowsForDamageTable(damageTable);
						for(DamageResultRow damageResultRow : resultRows) {
							Collection<DamageResult> results = damageResultDao.getDamageResultsForRow(damageResultRow);
							for(DamageResult damageResult : results) {
								damageResultRow.getResults().put(damageResult.getArmorType(), damageResult);
							}
							damageTable.getResultRows().put(damageResultRow.getRangeHighValue(), damageResultRow);
						}
					}
					subscriber.onNext(weapons);
					subscriber.onCompleted();
				}
				catch(Exception e) {
					subscriber.onError(e);
				}
			}
		}).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}
}
