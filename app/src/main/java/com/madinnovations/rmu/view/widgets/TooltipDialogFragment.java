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
package com.madinnovations.rmu.view.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Fragment for handling Dialogs that act as tool tips
 */
public class TooltipDialogFragment extends DialogFragment {
	private TooltipDialogListener listener;
	private CharSequence message;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("RMU", "onClick: listener = " + listener);
				if(listener != null) {
					listener.onOk(TooltipDialogFragment.this);
				}
			}
		});
		return builder.create();
	}

	// Getters and setters
	public void setMessage(CharSequence message) {
		this.message = message;
	}
	public void setListener(TooltipDialogListener listener) {
		this.listener = listener;
	}

	public interface TooltipDialogListener {
		void onOk(DialogFragment dialog);
	}
}
