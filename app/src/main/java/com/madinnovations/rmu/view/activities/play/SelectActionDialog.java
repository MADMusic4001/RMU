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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.character.CharacterRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureRxHandler;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.combat.Action;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.CombatRoundInfo;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.PlayFragmentModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Fragment to handle interaction with the Initiative dialog.
 */
public class SelectActionDialog extends DialogFragment {
	private static final String TAG = "SelectActionDialog";
	public static final String COMBAT_INFO_ARG_KEY = "combatInfo";
	public static final String CHARACTER_ARG_KEY = "character";
	public static final String CREATURE_ARG_KEY = "creature";
	@Inject
	protected CharacterRxHandler characterRxHandler;
	@Inject
	protected CreatureRxHandler  creatureRxHandler;
	private   CombatRoundInfo    combatRoundInfo;
	private Character                  character = null;
	private Creature                   creature  = null;
	private SelectActionDialogListener listener  = null;
	private ArrayAdapter<Action> actionArrayAdapter;
	private List<String> listItems = new ArrayList<>();
	private ArrayAdapter<String> opponentsListAdapter;
	private Spinner actionsSpinner;
	private Spinner actionPointsSpinner;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newPlayFragmentComponent(new PlayFragmentModule(this)).injectInto(this);

		LayoutInflater inflater = getActivity().getLayoutInflater();

		combatRoundInfo = (CombatRoundInfo) getArguments().getSerializable(COMBAT_INFO_ARG_KEY);
		character = (Character)getArguments().getSerializable(CHARACTER_ARG_KEY);
		creature = (Creature)getArguments().getSerializable(CREATURE_ARG_KEY);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View contentView = inflater.inflate(R.layout.select_action_dialog, null);

		TextView nameView = (TextView)contentView.findViewById(R.id.name_view);
		if(character != null) {
			nameView.setText(character.getFullName());
		}
		else if(creature != null) {
			nameView.setText(creature.getCreatureVariety().getName());
		}

		opponentsListAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		ListView opponentsListView = (ListView)contentView.findViewById(R.id.opponents_list_view);
		opponentsListView.setAdapter(opponentsListAdapter);
		initOpponentsList();

		initActionsSpinner(contentView);

		builder.setTitle(R.string.title_action)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						listener.onActionOk(SelectActionDialog.this);
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						listener.onActionCancel(SelectActionDialog.this);
					}
				})
				.setView(contentView);
		return builder.create();
	}

	public boolean copyViewsToItems() {
		boolean result = false;
		Action selectedAction = (Action)actionsSpinner.getSelectedItem();
		Short selectedActionPoints = (Short)actionPointsSpinner.getSelectedItem();

		if(!Action.AUTO.equals(selectedAction)
				&& !selectedAction.equals(combatRoundInfo.getActionInProgress())
				&& selectedActionPoints <= combatRoundInfo.getActionPointsRemaining()) {
			result = true;
			combatRoundInfo.setActionInProgress(selectedAction);
			combatRoundInfo.setActionPointsRemaining((short)(combatRoundInfo.getActionPointsRemaining() - selectedActionPoints));
		}

		return result;
	}

	private void initOpponentsList() {
		if(character != null) {
			creatureRxHandler.getAllForCampaign(character.getCampaign())
					.subscribe(new Subscriber<Collection<Creature>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception caught loading Creatures", e);
						}
						@Override
						public void onNext(Collection<Creature> creatures) {
							List<String> names = new ArrayList<>(creatures.size());
							for(Creature creature : creatures) {
								names.add(creature.getCreatureVariety().getName());
							}
							Collections.sort(names);
							opponentsListAdapter.clear();
							opponentsListAdapter.addAll(names);
							opponentsListAdapter.notifyDataSetChanged();
						}
					});
		}
		else if(creature != null) {
			characterRxHandler.getAllForCampaign(creature.getCampaign())
					.subscribe(new Subscriber<Collection<Character>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception caught loading Characters", e);
						}
						@Override
						public void onNext(Collection<Character> characters) {
							List<String> names = new ArrayList<>(characters.size());
							for(Character character: characters) {
								names.add(character.getFullName());
							}
							Collections.sort(names);
							opponentsListAdapter.clear();
							opponentsListAdapter.addAll(names);
							opponentsListAdapter.notifyDataSetChanged();
						}
					});
		}
	}

	private void initActionsSpinner(View layout) {
		actionsSpinner = (Spinner)layout.findViewById(R.id.action_spinner);
		actionArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		actionsSpinner.setAdapter(actionArrayAdapter);

		actionArrayAdapter.clear();
		actionArrayAdapter.addAll(Action.values());
		actionArrayAdapter.notifyDataSetChanged();

		actionPointsSpinner = (Spinner)layout.findViewById(R.id.action_points_spinner);
		final ArrayAdapter<Short> actionPointsArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		actionPointsSpinner.setAdapter(actionPointsArrayAdapter);
		initActionPointsSpinner((Action)actionsSpinner.getSelectedItem(), actionPointsArrayAdapter);

		actionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Action action = actionArrayAdapter.getItem(position);
				if (action != null) {
					initActionPointsSpinner(action, actionPointsArrayAdapter);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});

		actionPointsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initActionPointsSpinner(Action action, ArrayAdapter<Short> actionPointsArrayAdapter) {
		if(action != null) {
			actionPointsArrayAdapter.clear();
			for(short i = action.getMaxActionPoints(); i >= action.getMinActionPoints(); i--) {
				actionPointsArrayAdapter.add(i);
			}
			actionPointsArrayAdapter.notifyDataSetChanged();
		}

	}

	// Getters and setters
	public SelectActionDialogListener getListener() {
		return listener;
	}
	public void setListener(SelectActionDialogListener listener) {
		this.listener = listener;
	}
	public Character getCharacter() {
		return character;
	}
	public Creature getCreature() {
		return creature;
	}
	public CombatRoundInfo getCombatRoundInfo() {
		return combatRoundInfo;
	}

	public interface SelectActionDialogListener {
		/**
		 * Callback method called when the user clicks the OK button
		 *
		 * @param dialog  the dialog instance executing the callback
		 */
		void onActionOk(DialogFragment dialog);

		/**
		 * Callback method called when the user clicks the Cancel button
		 *
		 * @param dialog  the dialog instance executing the callback
		 */
		void onActionCancel(DialogFragment dialog);
	}
}
