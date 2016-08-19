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
package com.madinnovations.rmu.controller.rxhandler;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Creates reactive observable for requesting File operations.
 */
public class FileRxHandler {
	private Context context;

	/**
	 * Creates a new FileRxHandler instance
	 */
	@Inject
	public FileRxHandler(Context context) {
		this.context = context;
	}

	/**
	 * Creates an Observable that when subscribed to will read text from the android external files documents directory for the RMU app.
	 *
	 * @param fileName  the name of the file to read.
	 * @return an {@link Observable} instance that can be subscribed to in order to read text from a file.
	 */
	public Observable<String> readFile(final String fileName) {
		return Observable.create(
			new Observable.OnSubscribe<String>() {
				@Override
				public void call(Subscriber<? super String> subscriber) {
					FileInputStream inputStream = null;
					try {
						File dir = getImportExportDir();
						File file = new File(dir, fileName);
						inputStream = new FileInputStream(file);
						int size = inputStream.available();
						byte[] buffer = new byte[size];
						//noinspection ResultOfMethodCallIgnored
						inputStream.read(buffer);
						inputStream.close();
						subscriber.onNext(new String(buffer, "UTF-8"));
						subscriber.onCompleted();
					}
					catch (Exception e) {
						subscriber.onError(e);
					}
					finally {
						if(inputStream != null) {
							try {
								inputStream.close();
							}
							catch (IOException ignored) {}
						}
					}
				}
			}
		);
	}

	public Observable<Boolean> writeFile(final String fileName, final String data) {
		return Observable.create(
			new Observable.OnSubscribe<Boolean>() {
				@Override
				public void call(Subscriber<? super Boolean> subscriber) {
					FileOutputStream outputStream = null;
					try {
						File dir = getImportExportDir();
						File file = new File(dir, fileName);
						outputStream = new FileOutputStream(file);
						outputStream.write(data.getBytes());
						outputStream.close();
						subscriber.onNext(true);
						subscriber.onCompleted();
					}
					catch (Exception e) {
						subscriber.onError(e);
					}
					finally {
						if(outputStream != null) {
							try {
								outputStream.close();
							}
							catch (IOException ignored) {}
						}
					}
				}
			}
		);
	}

	private File getImportExportDir() {
		File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "RMU");
		if(!dir.exists()) {
			if (!dir.mkdirs()) {
				Log.e("RMU", "Directory not created");
			}
		}
		return dir;
	}
}
