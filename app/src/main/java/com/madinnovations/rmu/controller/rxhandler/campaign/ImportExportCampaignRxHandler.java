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
package com.madinnovations.rmu.controller.rxhandler.campaign;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.RMUDatabaseHelper;
import com.madinnovations.rmu.data.dao.campaign.CampaignDao;
import com.madinnovations.rmu.data.dao.character.CharacterDao;
import com.madinnovations.rmu.data.dao.character.serializers.CharacterSerializer;
import com.madinnovations.rmu.data.dao.item.ItemDao;
import com.madinnovations.rmu.data.dao.item.serializers.ItemSerializer;
import com.madinnovations.rmu.data.dao.play.EncounterSetupDao;
import com.madinnovations.rmu.data.dao.play.serializers.EncounterSetupSerializer;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;
import com.madinnovations.rmu.view.RMUAppException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Creates reactive observables for importing and exporting a campaign from the database
 */
public class ImportExportCampaignRxHandler {
	private static final int NUM_TYPES = 5;
	private static final String TAG = "ImportExportCampaignRxH";
	private static final String VERSION = "version";
	private CampaignDao              campaignDao;
	private CharacterDao             characterDao;
	private CharacterSerializer      characterSerializer = new CharacterSerializer();
	private EncounterSetupDao        encounterSetupDao;
	private EncounterSetupSerializer encounterSetupSerializer = new EncounterSetupSerializer();
	private ItemDao                  itemDao;
	private ItemSerializer           itemSerializer = new ItemSerializer();
	private RMUDatabaseHelper        helper;

	/**
	 * Creates a new ImportExportRxHandler instance
	 */
	@Inject
	ImportExportCampaignRxHandler(CampaignDao campaignDao, CharacterDao characterDao, EncounterSetupDao encounterSetupDao,
								  ItemDao itemDao, RMUDatabaseHelper helper) {
		this.campaignDao = campaignDao;
		this.characterDao = characterDao;
		this.encounterSetupDao = encounterSetupDao;
		this.itemDao = itemDao;
		this.helper = helper;
	}

	/**
	 * Creates an Observable that when subscribed to will import the RMU database from the file with the given file name. The 
	 * file
	 * name must be a fully qualified path.
	 *
	 * @param fileName the name of the file to read.
	 * @return an {@link Observable} instance that can be subscribed to in order to read the file.
	 */
	public Observable<Integer> importDatabase(@NonNull final String fileName)
	{
		return Observable.create(
				new Observable.OnSubscribe<Integer>() {
					@SuppressWarnings("UnusedAssignment")
					@Override
					public void call(Subscriber<? super Integer> subscriber) {
						SQLiteDatabase db = null;
						try {
							BufferedReader reader = new BufferedReader(new FileReader(fileName));
							final GsonBuilder gsonBuilder = new GsonBuilder();
							gsonBuilder.registerTypeAdapter(Character.class, characterSerializer);
							gsonBuilder.registerTypeAdapter(EncounterSetup.class, encounterSetupSerializer);
							gsonBuilder.registerTypeAdapter(Item.class, itemSerializer);
							gsonBuilder.registerTypeAdapter(Weapon.class, itemSerializer);
							gsonBuilder.setLenient();
							final Gson gson = gsonBuilder.create();

							JsonReader jsonReader = gson.newJsonReader(reader);
							jsonReader.beginObject();
							String name = jsonReader.nextName();
							if (!name.equals(VERSION)) {
								throw new RMUAppException("Input file does not begin with '" + VERSION + "'");
							}
							int version = jsonReader.nextInt();
//							if (version != RMUDatabaseHelper.DATABASE_VERSION) {
//								throw new RMUAppException("Input file version mismatch. Expected " +
//																  RMUDatabaseHelper.DATABASE_VERSION + " but found " + version);
//							}

							try {
								db = helper.getWritableDatabase();
								db.beginTransaction();
								int numTypesRead = 0;
								while (jsonReader.hasNext()) {
									switch (jsonReader.nextName()) {
										case Campaign.JSON_NAME:
											Campaign campaign = gson.fromJson(jsonReader, Campaign.class);
											helper.clearCampaignTables(campaign);
											campaignDao.save(campaign, true);
											Log.i(TAG, "Campaign loaded.");
											break;
										case Character.JSON_NAME:
											List<Character> characters = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												Character character = gson.fromJson(jsonReader, Character.class);
												characters.add(character);
											}
											jsonReader.endArray();
											characterDao.save(characters, true);
											Log.i(TAG, "Loaded " + characters.size() + " characters.");
											characters = null;
											break;
										case "CombatSetups":
										case EncounterSetup.JSON_NAME:
											List<EncounterSetup> encounterSetups = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												EncounterSetup encounterSetup = gson.fromJson(jsonReader, EncounterSetup.class);
												encounterSetups.add(encounterSetup);
											}
											jsonReader.endArray();
											encounterSetupDao.save(encounterSetups, true);
											Log.i(TAG, "Loaded " + encounterSetups.size() + " encounterSetups.");
											encounterSetups = null;
											break;
										case Item.JSON_NAME:
											List<Item> items = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												Item item = gson.fromJson(jsonReader, Item.class);
												items.add(item);
											}
											jsonReader.endArray();
											itemDao.save(items, true);
											Log.i(TAG, "Loaded " + items.size() + " items.");
											items = null;
											break;
									}
									if((numTypesRead*10/NUM_TYPES) % 10 < (++numTypesRead*10/NUM_TYPES) % 10) {
										subscriber.onNext((numTypesRead*10/NUM_TYPES % 10) * 10);
									}
								}
								jsonReader.endObject();
								db.setTransactionSuccessful();
							}
							catch(Exception e) {
								Log.e(TAG, "Caught exception", e);
								throw new RMUAppException(e);
							}
							finally {
								if(db != null) {
									db.endTransaction();
								}
							}

							reader.close();
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).onBackpressureDrop()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io());
	}

	/**
	 * Creates an Observable that when subscribed to will export the RMU database to the file with the given file name. The file
	 * name must be a fully qualified path.
	 *
	 * @param targetFile the file to write to
	 * @return an {@link Observable} instance that can be subscribed to in order to write the file.
	 */
	public Observable<Integer> exportDatabase(@NonNull final File targetFile, @NonNull final Campaign campaign) {
		return Observable.create(
				new Observable.OnSubscribe<Integer>() {
					@Override
					public void call(Subscriber<? super Integer> subscriber) {
						try {
							BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
							final GsonBuilder gsonBuilder = new GsonBuilder();
							gsonBuilder.registerTypeAdapter(Character.class, characterSerializer);
							gsonBuilder.registerTypeAdapter(EncounterSetup.class, encounterSetupSerializer);
							gsonBuilder.registerTypeAdapter(Item.class, itemSerializer);
							gsonBuilder.registerTypeAdapter(Weapon.class, itemSerializer);
							final Gson gson = gsonBuilder.create();

							JsonWriter jsonWriter = gson.newJsonWriter(writer);
							jsonWriter.beginObject()
									.name(VERSION)
									.value(RMUDatabaseHelper.DATABASE_VERSION);
							subscriber.onNext(20);
							jsonWriter.name(Campaign.JSON_NAME)
									.jsonValue(gson.toJson(campaignDao.getById(campaign.getId())));
							subscriber.onNext(40);
							jsonWriter.name(EncounterSetup.JSON_NAME)
									.jsonValue(gson.toJson(encounterSetupDao.getAllForCampaign(campaign)));
							subscriber.onNext(60);
							jsonWriter.name(Item.JSON_NAME)
									.jsonValue(gson.toJson(itemDao.getAllForCampaign(campaign)));
							subscriber.onNext(80);
							jsonWriter.name(Character.JSON_NAME)
									.jsonValue(gson.toJson(characterDao.getAllForCampaign(campaign)))
									.endObject()
									.flush();
							subscriber.onNext(100);
							writer.flush();
							writer.close();
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		).onBackpressureDrop()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io());
	}
}
