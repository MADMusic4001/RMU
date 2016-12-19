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
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Generic EditText handler code
 */
public final class EditTextUtils {

	/**
	 * Initializes an EditText with afterTextChanged and onFocusChange method implementations.
	 *
	 * @param layout  the layout that contains the EditText to be initialized
	 * @param context  an android Context that can be used to obtain resources, etc.
	 * @param valuesCallback  an implementation of the {@link ValuesCallback} interface that can be used to update the object backing the UI
	 * @param editTextId  the resource ID of the EditText to be initialized
	 * @param errorStringId  the resource ID of a string that can be used to display an error when the EditText is empty. And id of 0
	 *                     indicates that an empty EditText is not an error
	 * @return  the {@link EditText} instance that was initialized or null if not found.
	 */
	public static EditText initEdit(@NonNull View layout, @NonNull final Context context,
									@NonNull final ValuesCallback valuesCallback, @IdRes final int editTextId,
									@StringRes final int errorStringId) {
		final EditText editText = (EditText)layout.findViewById(editTextId);
		if(editText != null) {
			initEdit(editText, context, valuesCallback, editTextId, errorStringId);
		}

		return editText;
	}

	/**
	 * Initializes an EditText with afterTextChanged and onFocusChange method implementations.
	 *
	 * @param context  an android Context that can be used to obtain resources, etc.
	 * @param valuesCallback  an implementation of the {@link ValuesCallback} interface that can be used to update the object backing the UI
	 * @param editTextId  the resource ID of the EditText to be initialized
	 * @param errorStringId  the resource ID of a string that can be used to display an error when the EditText is empty. And id of 0
	 *                     indicates that an empty EditText is not an error
	 */
	public static void initEdit(@NonNull final EditText editText, @NonNull final Context context,
									@NonNull final ValuesCallback valuesCallback, @IdRes final int editTextId,
									@StringRes final int errorStringId) {
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && errorStringId != 0) {
					editText.setError(context.getString(errorStringId));
				}
			}
		});
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if (!hasFocus) {
					if(errorStringId == 0 || editText.getText().length() > 0) {
						String newName = editText.getText().toString();
						String oldName = valuesCallback.getValueForEditText(editTextId);

						if (!newName.equals(oldName)) {
							valuesCallback.setValueFromEditText(editTextId, newName);
						}
					}
				}
			}
		});
		editText.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				return valuesCallback.performLongClick(editTextId);
			}
		});
	}

	/**
	 * Defines methods used to get and set the value of the backing object.
	 */
	public interface ValuesCallback {
		/**
		 * Gets the current value of the field in the backing object.
		 *
		 * @param editTextId  the resource id of the EditText instance
		 * @return  the current value of the field in the backing object as a String.
		 */
		String getValueForEditText(@IdRes int editTextId);

		/**
		 * Sets the current value of the field in the backing object.
		 *
		 * @param editTextId  the resource id of the EditText instance
		 * @param newString  the new value of the field as a String
		 */
		void setValueFromEditText(@IdRes int editTextId, String newString);

		/**
		 * Called when a long click occurs for the given EditText.
		 *
		 * @param editTextId  the resource id of the EditText instance that was long clicked
		 * @return  true if the long click was handled, otherwise false.
		 */
		boolean performLongClick(@IdRes int editTextId);
	}
}
