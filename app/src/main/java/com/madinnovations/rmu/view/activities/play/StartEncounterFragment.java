/*
  Copyright (C) 2016 MadInnovations
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

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.campaign.CampaignRxHandler;
import com.madinnovations.rmu.controller.rxhandler.character.CharacterRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.CriticalResultRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureRxHandler;
import com.madinnovations.rmu.controller.rxhandler.play.EncounterSetupRxHandler;
import com.madinnovations.rmu.data.entities.Position;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.common.Pace;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;
import com.madinnovations.rmu.view.TerrainDragShadowBuilder;
import com.madinnovations.rmu.view.TerrainView;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.PlayFragmentModule;
import com.madinnovations.rmu.view.utils.Boast;
import com.madinnovations.rmu.view.utils.RandomUtils;
import com.madinnovations.rmu.view.widgets.ScaleView;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Handles interactions with the UI for encounters.
 */
public class StartEncounterFragment extends Fragment implements TerrainView.Callbacks, InitiativeDialog.InitiativeDialogListener,
		SelectActionDialog.SelectActionDialogListener, ResolveAttackDialog.ResolveAttackDialogListener {
//	private static final short MAX_INITIATIVE = (short)55;
	private static final String TAG = "StartEncounterFragment";
	@Inject
	protected CampaignRxHandler       campaignRxHandler;
	@Inject
	protected CharacterRxHandler      characterRxHandler;
	@Inject
	protected CreatureRxHandler       creatureRxHandler;
	@Inject
	protected CriticalResultRxHandler criticalResultRxHandler;
	@Inject
	protected SpecializationRxHandler specializationRxHandler;
	@Inject
	protected EncounterSetupRxHandler encounterSetupRxHandler;
	private   TerrainView             terrainView;
	private   Spinner                 campaignSpinner;
	private   Button                  startEncounterButton;
	private   ScaleView               scaleView;
	private   TextView                movingTextView;
	private   Spinner                 paceSpinner;
	private   LinearLayout            movementLayout;
	private ArrayAdapter<Character>   charactersListAdapter = null;
	private ArrayAdapter<Creature>    creaturesListAdapter  = null;
	private ArrayAdapter<Pace>        paceArrayAdapter      = null;
	private Collection<Character>     characters            = null;
	private Collection<Creature>      creatures             = null;
	private EncounterSetup            currentInstance       = new EncounterSetup();
	private boolean                   encounterInProgress   = false;
	private SelectActionDialog        selectActionDialog    = null;
	private ResolveAttackDialog       resolveAttackDialog   = null;
	// TODO: Re-roll critical option if player successfully used Sense Weakness talent at the beginning of combat.
	// TODO: Add riposte option if player with Riposte talent uses all his OB to parry and parry is effective (no hits delivered). Riposte is weapon skill specific.
	// TODO: Add Opportunistic Strike option when player has the talent and his opponent fumbles.
	// TODO: Add Quickdraw/Quickload option to allow player with the skill for the weapon he is loading/drawing to do so using
	// 0 AP. Track "Pressing the Advantage" talent +5 OB to next attack against the foe when player delivers a critical.
	// TODO: Add fumble increase for creatures attack player with Foiler talent.
	// TODO: Adjust attack size for Enchanced/Lesset Attack talents.
	// TODO: Adjust range penalties when player has Deadeye talent.
	// TODO: Adjust DB for players with Adrenal Defense talent.

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newPlayFragmentComponent(new PlayFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.start_encounter_fragment, container, false);

		initCampaignSpinner(layout);
		initStartEncounter(layout);
		initReset(layout);
		initTerrainView(layout);
		scaleView = (ScaleView)layout.findViewById(R.id.scale_view);
		scaleView.setTerrainView(terrainView);
		movementLayout = (LinearLayout)layout.findViewById(R.id.movement_layout);
		movementLayout.setVisibility(View.INVISIBLE);
		movingTextView = (TextView)layout.findViewById(R.id.moving_view);
		initPaceSpinner(layout);
		initCharactersListView(layout);
		initCreaturesListView(layout);
		return layout;
	}

	@Override
	public void onResume() {
		super.onResume();
		encounterSetupRxHandler.getAll();
	}

	@Override
	public EncounterSetup getEncounterSetup() {
		return currentInstance;
	}

	@Override
	public Collection<Character> getCharacters() {
		return characters;
	}

	@Override
	public Collection<Creature> getCreatures() {
		return creatures;
	}

	@Override
	public void enableEncounterButton(boolean enable) {
		startEncounterButton.setEnabled(enable);
	}

	@Override
	public void scaleChanged(float newScaleFactor) {
		scaleView.invalidate();
	}

	@Override
	public void onInitiativeOk(DialogFragment dialog) {
		if(!nextAction()) {
			showInitiativeDialog();
		}
		if(terrainView.isMovementInProgress()) {
			startEncounterButton.setText(getString(R.string.label_continue_combat));
			startEncounterButton.setEnabled(true);
		}
		else {
			startEncounterButton.setText(getString(R.string.label_start_combat));
			startEncounterButton.setEnabled(false);
		}
	}

	@Override
	public void onInitiativeCancel(DialogFragment dialog) {
		encounterInProgress = false;
	}

	@Override
	public void onActionOk(DialogFragment dialog) {
		if(selectActionDialog.copyViewsToItems()) {
			saveItem();
		}
		nextAction();
	}

	@Override
	public void onActionCancel(DialogFragment dialog) {

	}

	@Override
	public void onResolveAttackOk(DialogFragment dialog) {
		if(resolveAttackDialog.copyViewsToItems()) {
			saveItem();
		}
		nextAction();
	}

	@Override
	public void onResolveAttackCancel(DialogFragment dialog) {

	}

	private boolean nextAction() {
		Log.d(TAG, "nextAction: ");
		EncounterRoundInfo encounterRoundInfo = null;
		short currentInitiative = Short.MIN_VALUE;
		short nextInitiative;
		Character character = null;
		Creature creature = null;
		for(Map.Entry<Character, EncounterRoundInfo> entry : currentInstance.getCharacterCombatInfo().entrySet()) {
			if(entry.getValue().getActionPointsRemaining() > 0 || entry.getValue().getActionInProgress() != null) {
				nextInitiative = (short) (entry.getValue().getBaseInitiative() + (entry.getValue().getActionPointsRemaining()
						* 5));
				if (nextInitiative > currentInitiative) {
					currentInitiative = nextInitiative;
					encounterRoundInfo = entry.getValue();
					character = entry.getKey();
					Log.d(TAG, "nextAction: initiative = " + currentInitiative);
					Log.d(TAG, "nextAction: character = " + character.getKnownAs());
				}
			}
		}
		for(Map.Entry<Creature, EncounterRoundInfo> entry : currentInstance.getEnemyCombatInfo().entrySet()) {
			if (entry.getValue().getMovementRemaining() > 0 || entry.getValue().getActionPointsRemaining() > 0 ||
					entry.getValue().getActionInProgress() != null) {
				nextInitiative = (short) (entry.getValue().getBaseInitiative() +
						(entry.getValue().getActionPointsRemaining() * 5));
				if (nextInitiative > currentInitiative) {
					currentInitiative = nextInitiative;
					encounterRoundInfo = entry.getValue();
					character = null;
					creature = entry.getKey();
					Log.d(TAG, "nextAction: initiative = " + currentInitiative);
					Log.d(TAG, "nextAction: creature = " + creature.getCreatureVariety().getName());
				}
			}
		}
		if (encounterRoundInfo != null) {
			if((!terrainView.isMovementInProgress() || (terrainView.isMovementInProgress() &&
					terrainView.getBeingToMove() != encounterRoundInfo.getCombatant())) &&
					encounterRoundInfo.getMovementRemaining() > 0) {
				terrainView.setMovementInProgress(true);
				terrainView.setBeingToMove(encounterRoundInfo.getCombatant());
				String name;
				if(encounterRoundInfo.getCombatant() instanceof  Character) {
					name = ((Character)encounterRoundInfo.getCombatant()).getKnownAs();
				}
				else {
					name = ((Creature)encounterRoundInfo.getCombatant()).getCreatureVariety().getName();
				}
				movingTextView.setText(String.format(getString(R.string.moving_combatant), name));
				loadPaceSpinner(encounterRoundInfo);
				movementLayout.setVisibility(View.VISIBLE);
			}
			else if (encounterRoundInfo.getActionInProgress() != null) {
				terrainView.setMovementInProgress(false);
				terrainView.setBeingToMove(null);
				resolveAttackDialog = new ResolveAttackDialog();
				selectActionDialog = null;
				Bundle bundle = new Bundle();
				bundle.putSerializable(ResolveAttackDialog.CRITICAL_RESULT_RX_HANDLER_ARG_KEY, criticalResultRxHandler);
				bundle.putSerializable(ResolveAttackDialog.ENCOUNTER_SETUP_ARG_KEY, currentInstance);
				bundle.putSerializable(ResolveAttackDialog.COMBAT_INFO_ARG_KEY, encounterRoundInfo);
				if (character != null) {
					bundle.putSerializable(ResolveAttackDialog.CHARACTER_ARG_KEY, character);
				}
				if (creature != null) {
					bundle.putSerializable(ResolveAttackDialog.CREATURE_ARG_KEY, creature);
				}
				resolveAttackDialog.setArguments(bundle);
				resolveAttackDialog.setListener(StartEncounterFragment.this);
				resolveAttackDialog.show(getFragmentManager(), "ActionDialogFragment");
			}
			else {
				selectActionDialog = new SelectActionDialog();
				resolveAttackDialog = null;
				Bundle bundle = new Bundle();
				bundle.putSerializable(SelectActionDialog.ENCOUNTER_SETUP_ARG_KEY, currentInstance);
				bundle.putSerializable(SelectActionDialog.COMBAT_INFO_ARG_KEY, encounterRoundInfo);
				if (character != null) {
					bundle.putSerializable(SelectActionDialog.CHARACTER_ARG_KEY, character);
				}
				if (creature != null) {
					bundle.putSerializable(SelectActionDialog.CREATURE_ARG_KEY, creature);
				}
				selectActionDialog.setArguments(bundle);
				selectActionDialog.setListener(StartEncounterFragment.this);
				selectActionDialog.show(getFragmentManager(), "ActionDialogFragment");
			}
			return true;
		}
		else {
			return false;
		}
	}

	private void copyItemToViews() {
		loadCharacters();
		loadCreatures();
		startEncounterButton.setEnabled(currentInstance.getCurrentInitiative() == 0
											 && !currentInstance.getCharacterCombatInfo().isEmpty()
											 && !currentInstance.getEnemyCombatInfo().isEmpty());
		terrainView.invalidate();
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			encounterSetupRxHandler.save(currentInstance)
					.subscribe(new Subscriber<EncounterSetup>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception caught saving CombatSetup instance", e);
							Boast.showText(getActivity(), R.string.toast_combat_setup_save_failed);
						}
						@Override
						public void onNext(EncounterSetup encounterSetup) {
							Boast.showText(getActivity(), R.string.toast_combat_setup_saved);
						}
					});
		}
	}

	private void initCampaignSpinner(View layout) {
		campaignSpinner = (Spinner)layout.findViewById(R.id.campaign_spinner);
		final ArrayAdapter<Campaign> adapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		campaignSpinner.setAdapter(adapter);

		campaignRxHandler.getAll()
				.subscribe(new Subscriber<Collection<Campaign>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught loading all Campaign instances", e);
					}
					@Override
					public void onNext(Collection<Campaign> campaigns) {
						adapter.addAll(campaigns);
						if(charactersListAdapter != null) {
							loadCharacters();
						}
						if(creaturesListAdapter != null) {
							loadCreatures();
						}
					}
				});

		campaignSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				encounterSetupRxHandler.getMostRecentForCampaign((Campaign)campaignSpinner.getSelectedItem())
						.subscribe(new Subscriber<EncounterSetup>() {
							@Override
							public void onCompleted() {}
							@Override
							public void onError(Throwable e) {
								Log.e(TAG, "Exception caught loading CombatSetup instance", e);
							}
							@Override
							public void onNext(EncounterSetup encounterSetup) {
								if(encounterSetup == null) {
									currentInstance = new EncounterSetup();
									currentInstance.setCampaign((Campaign)campaignSpinner.getSelectedItem());
								}
								else {
									currentInstance = encounterSetup;
								}
								copyItemToViews();
							}
						});
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	private void initStartEncounter(View layout) {
		startEncounterButton = (Button)layout.findViewById(R.id.start_encounter_button);

		startEncounterButton.setEnabled(currentInstance.getCurrentInitiative() == 0
											 && !currentInstance.getCharacterCombatInfo().isEmpty()
											 && !currentInstance.getEnemyCombatInfo().isEmpty());

		startEncounterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(terrainView.isMovementInProgress()) {
					//noinspection SuspiciousMethodCalls
					EncounterRoundInfo encounterRoundInfo = currentInstance.getCharacterCombatInfo()
							.get(terrainView.getBeingToMove());
					if(encounterRoundInfo == null) {
						//noinspection SuspiciousMethodCalls
						encounterRoundInfo = currentInstance.getEnemyCombatInfo().get(terrainView.getBeingToMove());
					}
					if(encounterRoundInfo != null) {
						encounterRoundInfo.setMovementRemaining((short)(encounterRoundInfo.getMovementRemaining() -
																		terrainView.getMovementUsed()));
					}
					terrainView.setMovementInProgress(false);
					terrainView.setBeingToMove(null);
					movingTextView.setText(null);
					movementLayout.setVisibility(View.INVISIBLE);
					nextAction();
				}
				else {
					encounterInProgress = true;
					showInitiativeDialog();
				}
			}
		});
	}

	private void showInitiativeDialog() {
		encounterInProgress = !currentInstance.getEnemyCombatInfo().isEmpty();
		if(encounterInProgress) {
			for (Map.Entry<Character, EncounterRoundInfo> entry : currentInstance.getCharacterCombatInfo().entrySet()) {
				entry.getValue().setInitiativeRoll(RandomUtils.roll2d10());
			}
			for (Map.Entry<Creature, EncounterRoundInfo> entry : currentInstance.getEnemyCombatInfo().entrySet()) {
				entry.getValue().setInitiativeRoll(RandomUtils.roll2d10());
			}
			InitiativeDialog dialog = new InitiativeDialog();
			Bundle bundle = new Bundle();
			bundle.putSerializable(InitiativeDialog.ENCOUNTER_SETUP_ARG_KEY, currentInstance);
			bundle.putSerializable(InitiativeDialog.SPEC_RX_HANDLER_ARG_KEY, specializationRxHandler);
			dialog.setArguments(bundle);
			dialog.setListener(StartEncounterFragment.this);
			dialog.show(getFragmentManager(), "InitiativeDialogFragment");
		}
	}

	private void initReset(View layout) {
		Button resetButton = (Button)layout.findViewById(R.id.reset_button);

		resetButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				terrainView.setMovementInProgress(false);
				terrainView.setBeingToMove(null);
				movementLayout.setVisibility(View.INVISIBLE);
				if (currentInstance.getId() >= 0) {
					encounterSetupRxHandler.deleteById(currentInstance.getId())
							.subscribe(new Subscriber<Boolean>() {
								@Override
								public void onCompleted() {}
								@Override
								public void onError(Throwable e) {
									Log.e(TAG, "Exception caught deleting CombatSetup instance", e);
									Boast.showText(getActivity(), R.string.toast_combat_setup_delete_failed);
								}
								@Override
								public void onNext(Boolean aBoolean) {
									Boast.showText(getActivity(), R.string.toast_combat_setup_deleted);
									encounterSetupRxHandler.getMostRecentForCampaign((Campaign) campaignSpinner.getSelectedItem())
											.subscribe(new Subscriber<EncounterSetup>() {
												@Override
												public void onCompleted() {}
												@Override
												public void onError(Throwable e) {
													Log.e(TAG, "Exception caught getting CombatSetup instance", e);
													Boast.showText(getActivity(), R.string.toast_combat_setup_load_failed);
												}
												@Override
												public void onNext(EncounterSetup encounterSetup) {
													if (encounterSetup == null) {
														currentInstance = new EncounterSetup();
														currentInstance.setCampaign((Campaign) campaignSpinner.getSelectedItem());
													}
													else {
														currentInstance = encounterSetup;
													}
													copyItemToViews();
												}
											});
								}
							});
				}
			}
		});
	}

	private void initTerrainView(View layout) {
		terrainView = (TerrainView)layout.findViewById(R.id.terrain_view);
		terrainView.setCallbacks(this);
	}

	private void initPaceSpinner(View layout) {
		paceSpinner = (Spinner)layout.findViewById(R.id.pace_spinner);
		paceArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		paceSpinner.setAdapter(paceArrayAdapter);

		paceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(terrainView.isMovementInProgress() && terrainView.getBeingToMove() != null) {
					//noinspection SuspiciousMethodCalls
					EncounterRoundInfo encounterRoundInfo = currentInstance.getCharacterCombatInfo().get(
							terrainView.getBeingToMove());
					if(encounterRoundInfo == null) {
						//noinspection SuspiciousMethodCalls
						encounterRoundInfo = currentInstance.getEnemyCombatInfo().get(terrainView.getBeingToMove());
					}
					if(encounterRoundInfo != null) {
						encounterRoundInfo.setPace((Pace)paceSpinner.getSelectedItem());
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initCharactersListView(View layout) {
		ListView charactersListView = (ListView) layout.findViewById(R.id.characters_list_view);
		charactersListAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		charactersListView.setAdapter(charactersListAdapter);

		if(characters != null) {
			charactersListAdapter.clear();
			charactersListAdapter.addAll(characters);
			charactersListAdapter.notifyDataSetChanged();
		}
		else {
			loadCharacters();
		}

		charactersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				ClipData dragData = null;

				Character character = charactersListAdapter.getItem(position);
				if(character != null) {
					String characterIdString = String.valueOf(character.getId());
					ClipData.Item clipDataItem = new ClipData.Item(characterIdString);
					dragData = new ClipData(TerrainView.DRAG_ADD_COMBATANT, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
											clipDataItem);
				}
				TerrainDragShadowBuilder myShadow = new TerrainDragShadowBuilder(
						terrainView, new Position(0, 0, 0), character);

				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					view.startDragAndDrop(dragData, myShadow, character, 0);
				}
				else {
					//noinspection deprecation
					view.startDrag(dragData, myShadow, character, 0);
				}
				return false;
			}
		});
	}

	private void loadCharacters() {
		Campaign campaign = (Campaign)campaignSpinner.getSelectedItem();
		if(campaign != null) {
			characterRxHandler.getAllForCampaign(campaign)
					.subscribe(new Subscriber<Collection<Character>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "onError: Exception caught getting all Character instances", e);
						}
						@Override
						public void onNext(Collection<Character> charactersCollection) {
							characters = charactersCollection;
							if(terrainView != null) {
								terrainView.setCallbacks(StartEncounterFragment.this);
							}
							charactersListAdapter.clear();
							charactersListAdapter.addAll(charactersCollection);
							charactersListAdapter.notifyDataSetChanged();
						}
					});
		}
	}

	private void initCreaturesListView(View layout) {
		ListView creaturesListView = (ListView) layout.findViewById(R.id.opponents_list_view);
		creaturesListAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		creaturesListView.setAdapter(creaturesListAdapter);

		if(creatures != null) {
			creaturesListAdapter.clear();
			creaturesListAdapter.addAll(creatures);
			creaturesListAdapter.notifyDataSetChanged();
		}
		else {
			loadCreatures();
		}

		creaturesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				ClipData dragData = null;

				Creature creature = creaturesListAdapter.getItem(position);
				if(creature != null) {
					String creatureIdString = String.valueOf(creature.getId());
					ClipData.Item clipDataItem = new ClipData.Item(creatureIdString);
					dragData = new ClipData(TerrainView.DRAG_ADD_COMBATANT, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
											clipDataItem);
				}
				TerrainDragShadowBuilder myShadow = new TerrainDragShadowBuilder(terrainView, new Position(0, 0, 0), creature);

				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					view.startDragAndDrop(dragData, myShadow, creature, 0);
				}
				else {
					//noinspection deprecation
					view.startDrag(dragData, myShadow, creature, 0);
				}
				return false;
			}
		});
	}

	private void loadCreatures() {
		Campaign campaign = (Campaign)campaignSpinner.getSelectedItem();
		if(campaign != null) {
			creatureRxHandler.getAllForCampaign(campaign)
					.subscribe(new Subscriber<Collection<Creature>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "onError: Exception caught getting all Creature instances", e);
						}
						@Override
						public void onNext(Collection<Creature> creaturesCollection) {
							creatures = creaturesCollection;
							if(terrainView != null) {
								terrainView.setCallbacks(StartEncounterFragment.this);
							}
							creaturesListAdapter.clear();
							creaturesListAdapter.addAll(creaturesCollection);
							creaturesListAdapter.notifyDataSetChanged();
						}
					});
		}
	}

	private void loadPaceSpinner(EncounterRoundInfo encounterRoundInfo) {
		if(encounterRoundInfo.getPace() == null) {
			encounterRoundInfo.setPace(Pace.WALK);
		}
		int maxPaceOrdinal = encounterRoundInfo.getPace().ordinal() + 3;
		if(maxPaceOrdinal >= Pace.values().length) {
			maxPaceOrdinal = Pace.values().length - 1;
		}
		paceArrayAdapter.clear();
		for(int i = 0; i < maxPaceOrdinal; i++) {
			paceArrayAdapter.add(Pace.values()[i]);
		}
		paceArrayAdapter.notifyDataSetChanged();
	}
}
