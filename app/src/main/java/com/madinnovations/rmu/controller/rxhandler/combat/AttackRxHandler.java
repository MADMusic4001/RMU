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
package com.madinnovations.rmu.controller.rxhandler.combat;

import android.util.Log;

import com.madinnovations.rmu.data.dao.combat.AttackDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultRowDao;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.DamageResult;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.common.Specialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Attack} instances with persistent storage.
 */
@SuppressWarnings("unused")
public class AttackRxHandler implements Serializable {
	private static final long serialVersionUID = 3392013210046552200L;
	private static final String TAG = "AttackRxHandler";
	private AttackDao dao;
	private DamageResultDao    damageResultDao;
	private DamageResultRowDao damageResultRowDao;

	/**
	 * Creates a new AttackRxHandler
	 *
	 * @param dao  an {@link AttackDao} instance
	 * @param damageResultDao  a {@link DamageResultDao} instance
	 * @param damageResultRowDao  a {@link DamageResultRowDao} instance
	 */
	@Inject
	public AttackRxHandler(AttackDao dao, DamageResultDao damageResultDao, DamageResultRowDao damageResultRowDao) {
		this.dao = dao;
		this.damageResultDao = damageResultDao;
		this.damageResultRowDao = damageResultRowDao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Attack instance with the given id.
	 *
	 * @param id  the id of the Attack to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Attack instance.
	 */
	public Observable<Attack> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Attack>() {
					@Override
					public void call(Subscriber<? super Attack> subscriber) {
						try {
							Attack attack = dao.getById(id);
							subscriber.onNext(attack);
							if(attack != null) {
								DamageTable damageTable = attack.getDamageTable();
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Attack instance with the given code.
	 *
	 * @param code  the code of the Attack to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Attack instance.
	 */
	public Observable<Attack> getByCode(final String code) {
		return Observable.create(
				new Observable.OnSubscribe<Attack>() {
					@Override
					public void call(Subscriber<? super Attack> subscriber) {
						try {
							Attack attack = dao.getByCode(code);
							subscriber.onNext(attack);
							DamageTable damageTable = attack.getDamageTable();
							Collection<DamageResultRow> resultRows = damageResultRowDao.
									getDamageResultRowsForDamageTable(damageTable);
							for (DamageResultRow damageResultRow : resultRows) {
								Collection<DamageResult> results = damageResultDao.getDamageResultsForRow(damageResultRow);
								for (DamageResult damageResult : results) {
									damageResultRow.getResults().put(damageResult.getArmorType(), damageResult);
								}
								damageTable.getResultRows().put(damageResultRow.getRangeHighValue(), damageResultRow);
							}
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).subscribeOn(Schedulers.io())
				.observeOn(Schedulers.immediate());
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Attack instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Attack
	 * instances.
	 */
	public Observable<Collection<Attack>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Attack>>() {
					@Override
					public void call(Subscriber<? super Collection<Attack>> subscriber) {
						try {
							Collection<Attack> attacks = dao.getAll();
							subscriber.onNext(attacks);
							for(Attack attack : attacks) {
								DamageTable damageTable = attack.getDamageTable();
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
	 * Creates an Observable that, when subscribed to, will save a Attack instance to persistent storage.
	 *
	 * @param criticalResult  the Attack instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Attack instance.
	 */
	public Observable<Attack> save(final Attack criticalResult) {
		return Observable.create(
				new Observable.OnSubscribe<Attack>() {
					@Override
					public void call(Subscriber<? super Attack> subscriber) {
						try {
							dao.save(criticalResult);
							subscriber.onNext(criticalResult);
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
	 * Creates an Observable that, when subscribed to, will delete all Attack instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Attack instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Attack instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Attack instances.
	 */
	public Observable<Collection<Attack>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Attack>>() {
					@Override
					public void call(Subscriber<? super Collection<Attack>> subscriber) {
						try {
							Collection<Attack> criticalResultsDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(criticalResultsDeleted);
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Ranged Attack
	 * instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Ranged Attack
	 * instances.
	 */
	public Observable<Collection<Attack>> getRangedAttacks() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Attack>>() {
					@Override
					public void call(Subscriber<? super Collection<Attack>> subscriber) {
						try {
							Collection<Attack> rangedWeaponAttacks = new ArrayList<>();
							Collection<Attack> attacks = dao.getAll();
							for(Attack attack : attacks) {
								if (attack.getSpecialization() != null &&
										attack.getSpecialization().getSkill().getName().equals("Ranged Weapons")) {
									rangedWeaponAttacks.add(attack);
								}
							}
							subscriber.onNext(rangedWeaponAttacks);
							for(Attack attack : rangedWeaponAttacks) {
								DamageTable damageTable = attack.getDamageTable();
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Melee Attack
	 * instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Melee Attack
	 * instances.
	 */
	public Observable<Collection<Attack>> getMeleeAttacks() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Attack>>() {
					@Override
					public void call(Subscriber<? super Collection<Attack>> subscriber) {
						try {
							Collection<Attack> meleeWeaponAttacks = new ArrayList<>();
							Collection<Attack> attacks = dao.getAll();
							for(Attack attack : attacks) {
								if (attack.getSpecialization() != null &&
										attack.getSpecialization().getSkill().getName().equals("Melee Weapons")) {
									meleeWeaponAttacks.add(attack);
								}
							}
							subscriber.onNext(meleeWeaponAttacks);
							for(Attack attack : meleeWeaponAttacks) {
								DamageTable damageTable = attack.getDamageTable();
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for an Attack instance that uses the given
	 * Specialization.
	 *
	 * @param specialization  the specialization of the Attack to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Attack instance.
	 */
	public Observable<Attack> getForSpecialization(final Specialization specialization) {
		return Observable.create(
				new Observable.OnSubscribe<Attack>() {
					@Override
					public void call(Subscriber<? super Attack> subscriber) {
						try {
							Attack attack = dao.getForSpecialization(specialization);
							subscriber.onNext(attack);
							if(attack != null) {
								DamageTable damageTable = attack.getDamageTable();
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
}
