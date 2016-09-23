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

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;

/**
 * Generic CheckBox handler code
 */
public class CheckBoxUtils {

	/**
	 * Initializes a CheckBox with an onClick method implementation.
	 *
	 * @param layout  the layout that contains the CheckBox to be initialized
	 * @param valuesCallback  an implementation of the {@link ValuesCallback} interface that can be used to update the object backing the UI
	 * @param checkBoxId  the resource ID of the CheckBox to be initialized
	 * @return  the {@link CheckBox} instance that was initialized or null if not found.
	 */
	public static CheckBox initCheckBox(@NonNull View layout, @NonNull final ValuesCallback valuesCallback,
										@IdRes final int checkBoxId) {
		final CheckBox checkBox = (CheckBox)layout.findViewById(checkBoxId);
		if(checkBox != null) {
			initCheckBox(checkBox, valuesCallback, checkBoxId);
		}

		return checkBox;
	}

	/**
	 * Initializes a CheckBox with an onClick method implementation.
	 *
	 * @param checkBox  a CheckBox instance
	 * @param valuesCallback  an implementation of the {@link ValuesCallback} interface that can be used to update the object backing the UI
	 * @param checkBoxId  the resource ID of the CheckBox to be initialized
	 */
	public static void initCheckBox(@NonNull final CheckBox checkBox, @NonNull final ValuesCallback valuesCallback,
									@IdRes final int checkBoxId) {
		checkBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(checkBox.isChecked() != valuesCallback.getValueForCheckBox(checkBoxId)) {
					valuesCallback.setValueFromCheckBox(checkBoxId, checkBox.isChecked());
				}
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
		 * @param checkBoxId  the resource id of the CheckBox instance
		 * @return  the current value of the field in the backing object.
		 */
		boolean getValueForCheckBox(@IdRes int checkBoxId);

		/**
		 * Sets the current value of the field in the backing object.
		 *
		 * @param checkBoxId  the resource id of the CheckBox instance
		 * @param newBoolean  the new value of the field
		 */
		void setValueFromCheckBox(@IdRes int checkBoxId, boolean newBoolean);
	}
}
