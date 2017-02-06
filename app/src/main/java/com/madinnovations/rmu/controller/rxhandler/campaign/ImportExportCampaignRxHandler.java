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
package com.madinnovations.rmu.controller.rxhandler.campaign;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.RMUDatabaseHelper;
import com.madinnovations.rmu.data.dao.character.CharacterDao;
import com.madinnovations.rmu.data.dao.character.serializers.CharacterSerializer;
import com.madinnovations.rmu.data.dao.play.CombatSetupDao;
import com.madinnovations.rmu.data.dao.play.serializers.CombatSetupSerializer;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.play.CombatSetup;
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
	private static final int NUM_TYPES = 31;
	private static final String TAG = "ImportExportCampaignRxH";
	private static final String VERSION = "version";
	private CharacterDao                characterDao;
	private CharacterSerializer         characterSerializer = new CharacterSerializer();
	private CombatSetupDao              combatSetupDao;
	private CombatSetupSerializer       combatSetupSerializer = new CombatSetupSerializer();
	private RMUDatabaseHelper helper;

	/**
	 * Creates a new ImportExportRxHandler instance
	 */
	@Inject
	ImportExportCampaignRxHandler(CharacterDao characterDao, CombatSetupDao combatSetupDao, RMUDatabaseHelper helper) {
		this.characterDao = characterDao;
		this.combatSetupDao = combatSetupDao;
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
							gsonBuilder.registerTypeAdapter(CombatSetup.class, combatSetupSerializer);
							gsonBuilder.setLenient();
							final Gson gson = gsonBuilder.create();

							JsonReader jsonReader = gson.newJsonReader(reader);
							jsonReader.beginObject();
							String name = jsonReader.nextName();
							if (!name.equals(VERSION)) {
								throw new RMUAppException("Input file does not begin with '" + VERSION + "'");
							}
							int version = jsonReader.nextInt();
							if (version != RMUDatabaseHelper.DATABASE_VERSION) {
								throw new RMUAppException("Input file version mismatch. Expected " +
																  RMUDatabaseHelper.DATABASE_VERSION + " but found " + version);
							}

							try {
								db = helper.getWritableDatabase();
								db.beginTransaction();
								helper.clearDatabase();
								int numTypesRead = 0;
								while (jsonReader.hasNext()) {
									switch (jsonReader.nextName()) {
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
										case CombatSetup.JSON_NAME:
											List<CombatSetup> combatSetups = new ArrayList<>();
											jsonReader.beginArray();
											while (jsonReader.hasNext()) {
												CombatSetup combatSetup = gson.fromJson(jsonReader, CombatSetup.class);
												combatSetups.add(combatSetup);
											}
											jsonReader.endArray();
											combatSetupDao.save(combatSetups, true);
											Log.i(TAG, "Loaded " + combatSetups.size() + " combatSetups.");
											combatSetups = null;
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
	public Observable<Integer> exportDatabase(@NonNull final File targetFile) {
		return Observable.create(
				new Observable.OnSubscribe<Integer>() {
					@Override
					public void call(Subscriber<? super Integer> subscriber) {
						try {
							BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
							final GsonBuilder gsonBuilder = new GsonBuilder();
							gsonBuilder.registerTypeAdapter(Character.class, characterSerializer);
							gsonBuilder.registerTypeAdapter(CombatSetup.class, combatSetupSerializer);
							final Gson gson = gsonBuilder.create();

							JsonWriter jsonWriter = gson.newJsonWriter(writer);
							jsonWriter.beginObject()
									.name(VERSION)
									.value(RMUDatabaseHelper.DATABASE_VERSION)
									.name(Character.JSON_NAME)
									.jsonValue(gson.toJson(characterDao.getAll()))
									.name(CombatSetup.JSON_NAME)
									.jsonValue(gson.toJson(combatSetupDao.getAll()))
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
