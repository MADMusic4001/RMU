/*
  Copyright (C) 2017 MadInnovations
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.view.activities.play;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;
import com.madinnovations.rmu.view.adapters.play.InitiativeListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Fragment to handle interaction with the Initiative dialog.
 */
@SuppressWarnings("unused")
public class InitiativeDialog extends DialogFragment {
	private static final String TAG                     = "InitiativeDialog";
	public static final String  ENCOUNTER_SETUP_ARG_KEY = "encounterSetup";
	public static final String  SPEC_RX_HANDLER_ARG_KEY = "specializationRxHandler";
	private SpecializationRxHandler specializationRxHandler;
	private EncounterSetup encounterSetup;
	private InitiativeDialogListener listener = null;
	private InitiativeListAdapter initiativeListAdapter;
	private List<EncounterRoundInfo> listItems = new ArrayList<>();

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		encounterSetup = (EncounterSetup) getArguments().getSerializable(ENCOUNTER_SETUP_ARG_KEY);
		specializationRxHandler = (SpecializationRxHandler)getArguments().getSerializable(SPEC_RX_HANDLER_ARG_KEY);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View contentView = inflater.inflate(R.layout.initiative_dialog, null);
		ListView combatantsListView = (ListView)contentView.findViewById(R.id.combatants_list_view);
		initiativeListAdapter = new InitiativeListAdapter(getActivity(), encounterSetup, specializationRxHandler);
		initInitiativeList();
		combatantsListView.setAdapter(initiativeListAdapter);
		builder.setTitle(R.string.title_initiative)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						listener.onInitiativeOk(InitiativeDialog.this);
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						listener.onInitiativeCancel(InitiativeDialog.this);
					}
				})
				.setView(contentView);
		return builder.create();
	}

	private void initInitiativeList() {
		for(Map.Entry<Character, EncounterRoundInfo> entry : encounterSetup.getCharacterCombatInfo().entrySet()) {
			EncounterRoundInfo listItem = entry.getValue();
			short total;
			total = listItem.getInitiativeRoll();
			total += entry.getKey().getTotalStatBonus(Statistic.QUICKNESS);
			total += entry.getKey().getInitiativeModifications();
			listItem.setBaseInitiative(total);
			listItems.add(listItem);
		}
		for(Map.Entry<Creature, EncounterRoundInfo> entry : encounterSetup.getEnemyCombatInfo().entrySet()) {
			EncounterRoundInfo listItem = entry.getValue();
			short total;
			total = listItem.getInitiativeRoll();
			Short quickness = entry.getKey().getCreatureVariety().getRacialStatBonuses().get(Statistic.QUICKNESS);
			if(quickness != null) {
				total += quickness;
			}
			total += entry.getKey().getInitiativePenalty();
			listItem.setBaseInitiative(total);
			listItems.add(listItem);
		}
		Collections.sort(listItems);
		initiativeListAdapter.addAll(listItems);
	}

	// Getters and setters
	public InitiativeDialogListener getListener() {
		return listener;
	}
	public void setListener(InitiativeDialogListener listener) {
		this.listener = listener;
	}

	public interface InitiativeDialogListener {
		/**
		 * Callback method called when the user clicks the OK button
		 *
		 * @param dialog  the dialog instance executing the callback
		 */
		void onInitiativeOk(DialogFragment dialog);

		/**
		 * Callback method called when the user clicks the Cancel button
		 *
		 * @param dialog  the dialog instance executing the callback
		 */
		void onInitiativeCancel(DialogFragment dialog);
	}
}
