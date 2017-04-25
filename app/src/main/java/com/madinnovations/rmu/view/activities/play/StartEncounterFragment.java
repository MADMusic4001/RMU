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
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.campaign.CampaignRxHandler;
import com.madinnovations.rmu.controller.rxhandler.character.CharacterRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureRxHandler;
import com.madinnovations.rmu.controller.rxhandler.play.EncounterSetupRxHandler;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.CombatRoundInfo;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;
import com.madinnovations.rmu.view.HexView;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.PlayFragmentModule;
import com.madinnovations.rmu.view.utils.Boast;
import com.madinnovations.rmu.view.utils.HexDragShadowBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Handles interactions with the UI for encounters.
 */
public class StartEncounterFragment extends Fragment implements HexView.Callbacks, InitiativeDialog.InitiativeDialogListener,
		ActionDialog.ActionDialogListener {
//	private static final short MAX_INITIATIVE = (short)55;
	private static final String TAG = "StartEncounterFragment";
	private static final String DRAG_CHARACTER = "drag-character";
	private static final String DRAG_OPPONENT = "drag-opponent";
	@Inject
	protected CampaignRxHandler       campaignRxHandler;
	@Inject
	protected CharacterRxHandler      characterRxHandler;
	@Inject
	protected CreatureRxHandler       creatureRxHandler;
	@Inject
	protected EncounterSetupRxHandler encounterSetupRxHandler;
	private   HexView                 hexView;
	private   Spinner                 campaignSpinner;
	private   Button                  startEncounterButton;
	private ArrayAdapter<Character>   charactersListAdapter = null;
	private ArrayAdapter<Creature>    creaturesListAdapter  = null;
	private Collection<Character>     characters            = null;
	private Collection<Creature>      creatures             = null;
	private EncounterSetup            currentInstance       = new EncounterSetup();
	private boolean                   encounterInProgress;
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
		initHexView(layout);
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
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.start_combat_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		HexView.HexViewContextMenuInfo info = (HexView.HexViewContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_remove_character:
				for(Map.Entry<Character, CombatRoundInfo> entry : currentInstance.getCharacterCombatInfo().entrySet()) {
					if(entry.getValue().getHexCoordinate().equals(info.hexCoordinates)) {
						currentInstance.getCharacterCombatInfo().remove(entry.getKey());
						startEncounterButton.setEnabled(!currentInstance.getCharacterCombatInfo().isEmpty()
															 && !currentInstance.getCreatureCombatInfo().isEmpty());
						hexView.invalidate();
						break;
					}
				}
				return true;
			case R.id.context_remove_opponent:
				for(Map.Entry<Creature, CombatRoundInfo> entry : currentInstance.getCreatureCombatInfo().entrySet()) {
					if(entry.getValue().getHexCoordinate().equals(info.hexCoordinates)) {
						currentInstance.getCreatureCombatInfo().remove(entry.getKey());
						startEncounterButton.setEnabled(!currentInstance.getCharacterCombatInfo().isEmpty()
															 && !currentInstance.getCreatureCombatInfo().isEmpty());
						hexView.invalidate();
						break;
					}
				}
				return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public EncounterSetup getCombatSetup() {
		return currentInstance;
	}

	@Override
	public void onInitiativeOk(DialogFragment dialog) {
		if(!nextAction()) {
			showInitiativeDialog();
		}
		startEncounterButton.setEnabled(false);
	}

	@Override
	public void onInitiativeCancel(DialogFragment dialog) {
		encounterInProgress = false;
	}

	@Override
	public void onActionOk(DialogFragment dialog) {
		nextAction();
	}

	@Override
	public void onActionCancel(DialogFragment dialog) {

	}

	private boolean nextAction() {
//		boolean changed = false;
		CombatRoundInfo combatRoundInfo = null;
		short currentInitiative = Short.MIN_VALUE;
		short nextInitiative;
		Character character = null;
		Creature creature = null;
		for(Map.Entry<Character, CombatRoundInfo> entry : currentInstance.getCharacterCombatInfo().entrySet()) {
			nextInitiative = (short)(entry.getValue().getBaseInitiative() + (entry.getValue().getActionPointsRemaining() * 5));
			if(nextInitiative > currentInitiative) {
				currentInitiative = nextInitiative;
				combatRoundInfo = entry.getValue();
				character = entry.getKey();
			}
		}
		for(Map.Entry<Creature, CombatRoundInfo> entry : currentInstance.getCreatureCombatInfo().entrySet()) {
			nextInitiative = (short)(entry.getValue().getBaseInitiative() + (entry.getValue().getActionPointsRemaining() * 5));
			if(nextInitiative > currentInitiative) {
				currentInitiative = nextInitiative;
				combatRoundInfo = entry.getValue();
				character = null;
				creature = entry.getKey();
			}
		}
		if(combatRoundInfo != null) {
			ActionDialog actionDialog = new ActionDialog();
			Bundle bundle = new Bundle();
			bundle.putSerializable(ActionDialog.COMBAT_INFO_ARG_KEY, combatRoundInfo);
			if(character != null) {
				bundle.putSerializable(ActionDialog.CHARACTER_ARG_KEY, character);
			}
			if(creature != null) {
				bundle.putSerializable(ActionDialog.CREATURE_ARG_KEY, creature);
			}
			actionDialog.setArguments(bundle);
			actionDialog.setListener(StartEncounterFragment.this);
			actionDialog.show(getFragmentManager(), "ActionDialogFragment");
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
											 && !currentInstance.getCreatureCombatInfo().isEmpty());
		hexView.invalidate();
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
											 && !currentInstance.getCreatureCombatInfo().isEmpty());

		startEncounterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				encounterInProgress = true;
				showInitiativeDialog();
			}
		});
	}

	private void showInitiativeDialog() {
		Random random = new Random();
		encounterInProgress = !currentInstance.getCharacterCombatInfo().isEmpty() &&
				!currentInstance.getCreatureCombatInfo().isEmpty();
		if(encounterInProgress) {
			for (Map.Entry<Character, CombatRoundInfo> entry : currentInstance.getCharacterCombatInfo().entrySet()) {
				short initiativeRoll = (short) (random.nextInt(10) + random.nextInt(10) + 2);
				entry.getValue().setInitiativeRoll(initiativeRoll);
			}
			for (Map.Entry<Creature, CombatRoundInfo> entry : currentInstance.getCreatureCombatInfo().entrySet()) {
				short initiativeRoll = (short) (random.nextInt(10) + random.nextInt(10) + 2);
				entry.getValue().setInitiativeRoll(initiativeRoll);
			}
			InitiativeDialog dialog = new InitiativeDialog();
			Bundle bundle = new Bundle();
			bundle.putSerializable(InitiativeDialog.COMBAT_SETUP_ARG_KEY, currentInstance);
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
				if (currentInstance.getId() >= 0) {
					encounterSetupRxHandler.deleteById(currentInstance.getId())
							.subscribe(new Subscriber<Boolean>() {
								@Override
								public void onCompleted() {
								}

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
												public void onCompleted() {
												}

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

	private void initHexView(View layout) {
		hexView = (HexView)layout.findViewById(R.id.hex_view);
		hexView.setCallbacks(this);

		registerForContextMenu(hexView);

		hexView.setOnDragListener(new View.OnDragListener() {
			private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(),
																	   R.drawable.drag_target_background, null);
			private Drawable hoverShape  = ResourcesCompat.getDrawable(getActivity().getResources(),
																	   R.drawable.drag_hover_background, null);
			private Drawable normalShape = hexView.getBackground();

			@Override
			public boolean onDrag(View v, DragEvent event) {
				final int action = event.getAction();
				PointF pointf = new PointF(event.getX(), event.getY());

				switch (action) {
					case DragEvent.ACTION_DRAG_STARTED:
						if(event.getClipDescription() != null && (DRAG_CHARACTER.equals(event.getClipDescription().getLabel())
								|| DRAG_OPPONENT.equals(event.getClipDescription().getLabel()))) {
							v.setBackground(targetShape);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DRAG_ENTERED:
						if(event.getClipDescription() != null && (DRAG_CHARACTER.equals(event.getClipDescription().getLabel())
								|| DRAG_OPPONENT.equals(event.getClipDescription().getLabel()))) {
							v.setBackground(hoverShape);
							((HexView)v).setHighlightHex(pointf);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DRAG_LOCATION:
						if(event.getClipDescription() != null && (DRAG_CHARACTER.equals(event.getClipDescription().getLabel())
								|| DRAG_OPPONENT.equals(event.getClipDescription().getLabel()))) {
							((HexView)v).setHighlightHex(pointf);
							v.invalidate();
						}
						break;
					case DragEvent.ACTION_DRAG_EXITED:
						if(event.getClipDescription() != null && (DRAG_CHARACTER.equals(event.getClipDescription().getLabel())
								|| DRAG_OPPONENT.equals(event.getClipDescription().getLabel()))) {
							((HexView)v).clearHighlightCenterPoint();
							v.setBackground(targetShape);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DROP:
						if(event.getClipDescription() != null) {
							Point hexCoordinates = ((HexView)v).getHexCoordinates(pointf);
							((HexView)v).setHighlightHex(hexCoordinates);
							if (DRAG_CHARACTER.equals(event.getClipDescription().getLabel())) {
								ClipData.Item item = event.getClipData().getItemAt(0);
								int characterId = Integer.valueOf(item.getText().toString());
								for (Character aCharacter : characters) {
									if (aCharacter.getId() == characterId) {
										CombatRoundInfo combatRoundInfo = currentInstance.getCharacterCombatInfo().get(aCharacter);
										if(combatRoundInfo == null) {
											combatRoundInfo = new CombatRoundInfo();
										}
										combatRoundInfo.setHexCoordinate(hexCoordinates);
										currentInstance.getCharacterCombatInfo().put(aCharacter, combatRoundInfo);
										startEncounterButton.setEnabled(!currentInstance.getCreatureCombatInfo().isEmpty());
										break;
									}
								}
							} else if(DRAG_OPPONENT.equals(event.getClipDescription().getLabel())) {
								ClipData.Item item = event.getClipData().getItemAt(0);
								int opponentId = Integer.valueOf(item.getText().toString());
								for (Creature anOpponent : creatures) {
									if (anOpponent.getId() == opponentId) {
										CombatRoundInfo combatRoundInfo = currentInstance.getCreatureCombatInfo().get(anOpponent);
										if(combatRoundInfo == null) {
											combatRoundInfo = new CombatRoundInfo();
										}
										combatRoundInfo.setHexCoordinate(hexCoordinates);
										currentInstance.getCreatureCombatInfo().put(anOpponent, combatRoundInfo);
										startEncounterButton.setEnabled(!currentInstance.getCharacterCombatInfo().isEmpty());
										break;
									}
								}
							}
							((HexView)v).clearHighlightCenterPoint();
							v.setBackground(normalShape);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DRAG_ENDED:
						v.setBackground(normalShape);
						v.invalidate();
						break;
				}
				return true;
			}
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
					dragData = new ClipData(DRAG_CHARACTER, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
				}
				HexDragShadowBuilder myShadow = new HexDragShadowBuilder(hexView);

				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					view.startDragAndDrop(dragData, myShadow, null, 0);
				}
				else {
					//noinspection deprecation
					view.startDrag(dragData, myShadow, null, 0);
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
						public void onCompleted() {
						}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "onError: Exception caught getting all Character instances", e);
						}
						@Override
						public void onNext(Collection<Character> charactersCollection) {
							characters = charactersCollection;
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
					dragData = new ClipData(DRAG_OPPONENT, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
				}
				HexDragShadowBuilder myShadow = new HexDragShadowBuilder(hexView);

				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					view.startDragAndDrop(dragData, myShadow, null, 0);
				}
				else {
					//noinspection deprecation
					view.startDrag(dragData, myShadow, null, 0);
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
						public void onCompleted() {
						}

						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "onError: Exception caught getting all Creature instances", e);
						}

						@Override
						public void onNext(Collection<Creature> creaturesCollection) {
							creatures = creaturesCollection;
							creaturesListAdapter.clear();
							creaturesListAdapter.addAll(creaturesCollection);
							creaturesListAdapter.notifyDataSetChanged();
						}
					});
		}
	}
}
