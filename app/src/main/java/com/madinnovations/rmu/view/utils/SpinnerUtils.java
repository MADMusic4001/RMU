/**
 * Copyright (C) 2016 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.view.utils;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.madinnovations.rmu.R;

import java.util.Collection;

import rx.Observable;
import rx.Subscriber;

/**
 * Generic Spinner handler code
 */
public final class SpinnerUtils<T> {
	private static final String LOG_TAG = "SpinnerUtils";
	private Spinner spinner;
	private ArrayAdapter<T> adapter;

	/**
	 * Initializes a Spinner with onItemSelected method implementation.
	 *
	 * @param layout  the layout that contains the EditText to be initialized
	 * @param context  an android Context that can be used to obtain resources, etc.
	 * @param valuesCallback  an implementation of the {@link SpinnerUtils.ValuesCallback} interface that can be used to update the object
	 *                        backing the UI
	 * @param spinnerId  the resource ID of the Spinner to be initialized
	 * @return  the {@link Spinner} instance that was initialized or null if not found.
	 */
	public Spinner initSpinner(@NonNull View layout, @NonNull final Context context, @NonNull Observable<Collection<T>> loader,
							   @NonNull final SpinnerUtils.ValuesCallback valuesCallback, @IdRes final int spinnerId,
							   T dummmyInstance) {
		spinner = (Spinner) layout.findViewById(spinnerId);
		if(spinner != null) {
			initSpinner(spinner, context, loader, valuesCallback, spinnerId, dummmyInstance);
		}

		return spinner;
	}

	/**
	 * Initializes a Spinner with onItemSelected method implementations.
	 *
	 * @param spinner  the Spinner instance to use
	 * @param context  an android Context that can be used to obtain resources, etc.
	 * @param valuesCallback  an implementation of the {@link SpinnerUtils.ValuesCallback} interface that can be used to update the object
	 *                        backing the UI
	 * @param spinnerId  the resource ID of the Spinner to be initialized
	 */
	@SuppressWarnings("WeakerAccess")
	public void initSpinner(@NonNull final Spinner spinner, @NonNull final Context context,
							@NonNull Observable<Collection<T>> loader,
							@NonNull final SpinnerUtils.ValuesCallback valuesCallback, @IdRes final int spinnerId,
							final T dummyInstance) {
		this.spinner = spinner;
		adapter = new ArrayAdapter<>(context, R.layout.spinner_row);
		spinner.setAdapter(adapter);

		loader.subscribe(new Subscriber<Collection<T>>() {
			@Override
			public void onCompleted() {}
			@Override
			public void onError(Throwable e) {
				Log.e(LOG_TAG, "Exception caught loading spinner data.", e);
			}
			@Override
			public void onNext(Collection<T> ts) {
				adapter.clear();
				if(dummyInstance != null) {
					adapter.add(dummyInstance);
				}
				adapter.addAll(ts);
				adapter.notifyDataSetChanged();
			}
		});

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				T newItem = adapter.getItem(position);
				if(newItem != null && !newItem.equals(valuesCallback.getValueForSpinner(spinnerId))) {
					valuesCallback.setValueFromSpinner(spinnerId, newItem);
				}
			}
			@SuppressWarnings("unchecked")
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(valuesCallback.getValueForSpinner(spinnerId) != null) {
					valuesCallback.setValueFromSpinner(spinnerId, null);
				}
			}
		});
	}

	/**
	 * Gets the currently selected instance or null if not item is selected.
	 *
	 * @return the currently selected item.
	 */
	public T getSelectedItem() {
		T selectedItem = null;
		int position = spinner.getSelectedItemPosition();
		if(position >= 0) {
			selectedItem = adapter.getItem(position);
		}
		return selectedItem;
	}

	/**
	 * Sets the current selection.
	 *
	 * @param t  the item to be set as the current selection
	 */
	public void setSelection(T t) {
		spinner.setSelection(adapter.getPosition(t));
	}

	// Getters
	public Spinner getSpinner() {
		return spinner;
	}
	public ArrayAdapter<T> getAdapter() {
		return adapter;
	}

	/**
	 * Defines methods used to get and set the value of the backing object.
	 */
	public interface ValuesCallback<T> {
		/**
		 * Gets the current value of the field in the backing object.
		 *
		 * @param spinnerId  the resource id of the Spinner instance
		 * @return  the current value of the field in the backing object as a String.
		 */
		T getValueForSpinner(@IdRes int spinnerId);

		/**
		 * Sets the current value of the field in the backing object.
		 *
		 * @param spinnerId  the resource id of the EditText instance
		 * @param newItem  the selected item of the field
		 */
		void setValueFromSpinner(@IdRes int spinnerId, T newItem);
	}
}
