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
package com.madinnovations.rmu.controller.rxhandler.character;

import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.character.CharacterDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultRowDao;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.combat.DamageResult;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observable for requesting operations on {@link Character} instances with persistent storage.
 */
public class CharacterRxHandler {
	private CharacterDao       dao;
	private DamageResultDao    damageResultDao;
	private DamageResultRowDao damageResultRowDao;

	/**
	 * Creates a new CharacterRxHandler
	 *
	 * @param dao  a {@link CharacterDao} instance
	 * @param damageResultDao  a {@link DamageResultDao} instance
	 * @param damageResultRowDao  a {@link DamageResultRowDao} instance
	 */
	@Inject
	public CharacterRxHandler(CharacterDao dao, DamageResultDao damageResultDao, DamageResultRowDao damageResultRowDao) {
		this.dao = dao;
		this.damageResultDao = damageResultDao;
		this.damageResultRowDao = damageResultRowDao;
	}

	/**
	 * Creates an Observable that, when subscribed to, will query persistent storage for a Character instance with the given id.
	 *
	 * @param id  the id of the Character to retrieve from persistent storage
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a Character instance.
	 */
	public Observable<Character> getById(final int id) {
		return Observable.create(
				new Observable.OnSubscribe<Character>() {
					@Override
					public void call(Subscriber<? super Character> subscriber) {
						try {
							Character character = dao.getById(id);
							if(character != null) {
								if (character.getMainHandItem() != null && character.getMainHandItem() instanceof Weapon) {
									loadDamageResults((Weapon) character.getMainHandItem());
								}
								if (character.getOffhandItem() != null && character.getOffhandItem() instanceof Weapon) {
									loadDamageResults((Weapon) character.getOffhandItem());
								}
							}
							subscriber.onNext(character);
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Character instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Character
	 * instances.
	 */
	public Observable<Collection<Character>> getAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Character>>() {
					@Override
					public void call(Subscriber<? super Collection<Character>> subscriber) {
						try {
							Collection<Character> characters = dao.getAll();
							for(Character character : characters) {
								if(character.getMainHandItem() != null && character.getMainHandItem() instanceof Weapon) {
									loadDamageResults((Weapon)character.getMainHandItem());
								}
								if(character.getOffhandItem() != null && character.getOffhandItem() instanceof Weapon) {
									loadDamageResults((Weapon)character.getOffhandItem());
								}
							}
							subscriber.onNext(characters);
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
	 * Creates an Observable that, when subscribed to, will save a Character instance to persistent storage.
	 *
	 * @param character  the Character instance to be saved
	 * @return an {@link Observable} instance that can be subscribed to in order to save the Character instance.
	 */
	public Observable<Character> save(final Character character) {
		return Observable.create(
				new Observable.OnSubscribe<Character>() {
					@Override
					public void call(Subscriber<? super Character> subscriber) {
						try {
							dao.save(character);
							subscriber.onNext(character);
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
	 * Creates an Observable that, when subscribed to, will delete all Character instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Character instances.
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
	 * Creates an Observable that, when subscribed to, will delete all Character instances.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to delete the Character instances.
	 */
	public Observable<Collection<Character>> deleteAll() {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Character>>() {
					@Override
					public void call(Subscriber<? super Collection<Character>> subscriber) {
						try {
							Collection<Character> charactersDeleted = dao.getAll();
							dao.deleteAll();
							subscriber.onNext(charactersDeleted);
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
	 * Creates an Observable that, when subscribed to, will query persistent storage for a collection of all Character
	 * instances linked to the given Campaign.
	 *
	 * @return an {@link Observable} instance that can be subscribed to in order to retrieve a collection of Character
	 * instances.
	 */
	public Observable<Collection<Character>> getAllForCampaign(final Campaign campaign) {
		return Observable.create(
				new Observable.OnSubscribe<Collection<Character>>() {
					@Override
					public void call(Subscriber<? super Collection<Character>> subscriber) {
						try {
							Collection<Character> characters = dao.getAllForCampaign(campaign);
							for(Character character : characters) {
								if(character.getMainHandItem() != null && character.getMainHandItem() instanceof Weapon) {
									loadDamageResults((Weapon)character.getMainHandItem());
								}
								if(character.getOffhandItem() != null && character.getOffhandItem() instanceof Weapon) {
									loadDamageResults((Weapon)character.getOffhandItem());
								}
							}
							subscriber.onNext(characters);
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
