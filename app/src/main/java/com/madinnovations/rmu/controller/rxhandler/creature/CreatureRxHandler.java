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
package com.madinnovations.rmu.controller.rxhandler.creature;

import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultRowDao;
import com.madinnovations.rmu.data.dao.creature.CreatureDao;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.combat.DamageResult;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Creature} instances with persistent storage.
 */
public class CreatureRxHandler {
	private CreatureDao        dao;
	private DamageResultDao    damageResultDao;
	private DamageResultRowDao damageResultRowDao;

	/**
	 * Creates a new CreatureRxHandler
	 *
	 * @param dao  a {@link CreatureDao} instance
	 * @param damageResultDao  a {@link DamageResultDao} instance
	 * @param damageResultRowDao  a {@link DamageResultRowDao} instance
	 */
	@Inject
	public CreatureRxHandler(CreatureDao dao, DamageResultDao damageResultDao, DamageResultRowDao damageResultRowDao) {
		this.dao = dao;
		this.damageResultDao = damageResultDao;
		this.damageResultRowDao = damageResultRowDao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Creature instance with the given id.
	 *
	 * @param id  the id of the Creature to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Creature instance.
	 */
	public Observable<Creature> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Creature>() {
					@Override
					public void call(Subscriber<? super Creature> subscriber) {
						try {
							Creature creature = dao.getById(id);
							if(creature != null) {
								if (creature.getMainHandItem() != null && creature.getMainHandItem() instanceof Weapon) {
									loadDamageResults((Weapon) creature.getMainHandItem());
								}
								if (creature.getOffhandItem() != null && creature.getOffhandItem() instanceof Weapon) {
									loadDamageResults((Weapon) creature.getOffhandItem());
								}
							}
							subscriber.onNext(creature);
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Creature instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Creature
	 * instances.
	 */
	public Observable<Collection<Creature>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Creature>>() {
					@Override
					public void call(Subscriber<? super Collection<Creature>> subscriber) {
						try {
							Collection<Creature> creatures = dao.getAll();
							for(Creature creature : creatures) {
								if (creature.getMainHandItem() != null && creature.getMainHandItem() instanceof Weapon) {
									loadDamageResults((Weapon) creature.getMainHandItem());
								}
								if (creature.getOffhandItem() != null && creature.getOffhandItem() instanceof Weapon) {
									loadDamageResults((Weapon) creature.getOffhandItem());
								}
							}
							subscriber.onNext(creatures);
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
	 * Creates an Observable that, when subscribed to, will save a Creature instance to persistent storage.
	 *
	 * @param creature  the Creature instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Creature instance.
	 */
	public Observable<Creature> save(final Creature creature) {
		return Observable.create(
				new Observable.OnSubscribe<Creature>() {
					@Override
					public void call(Subscriber<? super Creature> subscriber) {
						try {
							dao.save(creature);
							subscriber.onNext(creature);
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
	 * Creates an Observable that, when subscribed to, will delete all Creature instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Creature instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Creature instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Creature instances.
	 */
	public Observable<Collection<Creature>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Creature>>() {
					@Override
					public void call(Subscriber<? super Collection<Creature>> subscriber) {
						try {
							Collection<Creature> creaturesDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(creaturesDeleted);
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Creature
	 * instances linked to the given Campaign.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Creature
	 * instances.
	 */
	public Observable<Collection<Creature>> getAllForCampaign(final Campaign campaign) {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Creature>>() {
					@Override
					public void call(Subscriber<? super Collection<Creature>> subscriber) {
						try {
							Collection<Creature> creatures = dao.getAllForCampaign(campaign);
							for(Creature creature : creatures) {
								if (creature.getMainHandItem() != null && creature.getMainHandItem() instanceof Weapon) {
									loadDamageResults((Weapon) creature.getMainHandItem());
								}
								if (creature.getOffhandItem() != null && creature.getOffhandItem() instanceof Weapon) {
									loadDamageResults((Weapon) creature.getOffhandItem());
								}
							}
							subscriber.onNext(creatures);
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

	private void loadDamageResults(@NonNull Weapon weapon) {
		if(weapon.getItemTemplate() != null && weapon.getItemTemplate() instanceof WeaponTemplate) {
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
	}
}
