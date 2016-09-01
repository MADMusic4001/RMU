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

import android.support.annotation.NonNull;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Creates reactive observables for importing and exporting the database
 */
public class ImportExportRxHandler {
	/**
	 * Creates a new ImportExportRxHandler instance
	 */
	@Inject
	public ImportExportRxHandler() {
	}

	/**
	 * Creates an Observable that when subscribed to will import the RMU database from the file with the given file name. The file name must
	 * be a fully qualified path.
	 *
	 * @param fileName  the name of the file to read.
	 * @return an {@link Observable} instance that can be subscribed to in order to read the file.
	 */
	public Observable<Boolean> importDatabase(@NonNull String fileName) {
		return Observable.create(
				new Observable.OnSubscribe<Boolean>() {
					@Override
					public void call(Subscriber<? super Boolean> subscriber) {
						try {

						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		);
	}

	/**
	 * Creates an Observable that when subscribed to will export the RMU database to the file with the given file name. The file name must
	 * be a fully qualified path.
	 *
	 * @param fileName  the name of the file to write to.
	 * @return an {@link Observable} instance that can be subscribed to in order to write the file.
	 */
	public Observable<Boolean> exportDatabase(@NonNull String fileName) {
		return Observable.create(
				new Observable.OnSubscribe<Boolean>() {
					@Override
					public void call(Subscriber<? super Boolean> subscriber) {
						try {

							subscriber.onNext(true);
							subscriber.onCompleted();
						}
						catch (Exception e) {
							subscriber.onError(e);
						}
					}
				}
		);
	}

}
