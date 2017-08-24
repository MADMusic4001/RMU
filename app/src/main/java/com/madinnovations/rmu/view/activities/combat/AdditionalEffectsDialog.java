/*
 *
 *   Copyright (C) 2017 MadInnovations
 *   <p/>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   <p/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   <p/>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *
 */

package com.madinnovations.rmu.view.activities.combat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.combat.AdditionalEffect;
import com.madinnovations.rmu.view.adapters.combat.AdditionalEffectsAdapter;

import java.util.List;

/**
 * UI for adding additional effects to critical results or fumbles.
 */
public class AdditionalEffectsDialog extends DialogFragment implements AdditionalEffectsAdapter.AdditionalEffectCallbacks {
	public static final String  ADDITIONAL_EFFECTS_ARG_KEY = "encounterSetup";
	private List<AdditionalEffect> additionalEffects;
	private AdditionalEffectsDialogListener listener = null;
	private AdditionalEffectsAdapter additionalEffectsAdapter;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		//noinspection unchecked
		additionalEffects = (List<AdditionalEffect>) getArguments().getSerializable(ADDITIONAL_EFFECTS_ARG_KEY);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View contentView = inflater.inflate(R.layout.additional_effects_dialog, null);
		initEffectsList(contentView);
		initAddEffectButton(contentView);
		builder.setTitle(R.string.title_additional_effects)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(listener != null) {
							listener.onAdditionalEffectOk(AdditionalEffectsDialog.this);
						}
					}
				})
				.setView(contentView);
		return builder.create();
	}

	@Override
	public void addAdditionalEffect(AdditionalEffect additionalEffect) {
		additionalEffects.add(additionalEffect);
		additionalEffectsAdapter.notifyDataSetChanged();
		listener.addAdditionalEffect(additionalEffect);
	}

	@Override
	public void updateAdditionalEffect(AdditionalEffect additionalEffect) {
		listener.updateAdditionalEffect(additionalEffect);
	}

	@Override
	public void removeAdditionalEffect(AdditionalEffect additionalEffect) {
		additionalEffects.remove(additionalEffect);
		additionalEffectsAdapter.notifyDataSetChanged();
		listener.removeAdditionalEffect(additionalEffect);
	}

	private void initAddEffectButton(View contentView) {
		ImageButton addEffectButton = (ImageButton)contentView.findViewById(R.id.add_effect_button);

		addEffectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				additionalEffects.add(new AdditionalEffect());
				additionalEffectsAdapter.clear();
				additionalEffectsAdapter.addAll(additionalEffects);
				additionalEffectsAdapter.notifyDataSetChanged();
			}
		});
	}

	private void initEffectsList(View contentView) {
		ListView additionalEffectsList = (ListView)contentView.findViewById(R.id.additional_effects_list);
		additionalEffectsAdapter = new AdditionalEffectsAdapter(getActivity(), R.layout.additional_effects_row,
																this);
		additionalEffectsList.setAdapter(additionalEffectsAdapter);
		additionalEffectsAdapter.clear();
		additionalEffectsAdapter.addAll(additionalEffects);
		additionalEffectsAdapter.notifyDataSetChanged();
	}

	// Getters and setters
	public void setListener(AdditionalEffectsDialogListener listener) {
		this.listener = listener;
	}
	public List<AdditionalEffect> getAdditionalEffects() {
		return additionalEffects;
	}

	public interface AdditionalEffectsDialogListener {
		/**
		 * Callback method called when the user clicks the OK button
		 *
		 * @param dialog  the dialog instance executing the callback
		 */
		void onAdditionalEffectOk(DialogFragment dialog);

		/**
		 * Request to add a new AdditionalEffect instance to its parent.
		 *
		 * @param additionalEffect  the new AdditionalEffect instance
		 */
		void addAdditionalEffect(AdditionalEffect additionalEffect);

		/**
		 * Notification that an AdditionalEffect was changed.
		 *
		 * @param additionalEffect  the AdditionalEffect instance that was changed
		 */
		void updateAdditionalEffect(AdditionalEffect additionalEffect);

		/**
		 * Request to remove an AdditionalEffect instance from its parent.
		 *
		 * @param additionalEffect  the AdditionalEffect instance to remove
		 */
		void removeAdditionalEffect(AdditionalEffect additionalEffect);
	}
}
