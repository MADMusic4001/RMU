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
package com.madinnovations.rmu.view.activities.play;

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
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.character.CharacterRxHandler;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.CombatSetup;
import com.madinnovations.rmu.view.HexView;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.PlayFragmentModule;
import com.madinnovations.rmu.view.utils.HexDragShadowBuilder;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Handles interactions with the UI for combat.
 */
public class StartCombatFragment extends Fragment implements HexView.Callbacks {
	private static final String TAG = "StartCombatFragment";
	private static final String DRAG_CHARACTER = "drag-character";
	private static final String DRAG_OPPONENT = "drag-opponent";
	@Inject
	protected CharacterRxHandler    characterRxHandler;
	private   HexView               hexView;
	private   ListView              charactersListView;
	private   ListView              opponentsListView;
	private   Collection<Character> characters = null;
	private   Collection<Creature>  opponents = null;
	private   CombatSetup           currentInstance = new CombatSetup();
	// TODO: Re-roll critical option if player successfully used Sense Weakness talent at the beginning of combat.
	// TODO: Add riposte option if player with Riposte talent uses all his OB to parry and parry is effective (no hits delivered). Riposte is weapon skill specific.
	// TODO: Add Opportunistic Strike option when player has the talent and his opponent fumbles.
	// TODO: Add Quickdraw/Quickload option to allow player with the skill for the weapon he is loading/drawing to do so using
	// 0 AP. Track "Pressing the Advantage" talent +5 OB to next attack against the foe when player delivers a critical.
	// TODO: Add fumble increase for opponents attack player with Foiler talent.
	// TODO: Adjust attack size for Enchanced/Lesset Attack talents.
	// TODO: Adjust range penalties when player has Deadeye talent.
	// TODO: Adjust DB for players with Adrenal Defense talent.

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newPlayFragmentComponent(new PlayFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.start_combat_fragment, container, false);

		initHexView(layout);
		initCharactersListView(layout);
		return layout;
	}

	@Override
	public CombatSetup getCombatSetup() {
		return currentInstance;
	}

	private void saveCharacter(Character character) {
		if(character.isValid()) {
			characterRxHandler.save(character)
					.subscribe(new Subscriber<Character>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "onError: Exception caught saving character location", e);
						}
						@Override
						public void onNext(Character character) {
							Toast.makeText(getActivity(), R.string.toast_character_location_saved, Toast.LENGTH_SHORT).show();
						}
					});
		}
	}

	private void initHexView(View layout) {
		hexView = (HexView)layout.findViewById(R.id.hex_view);
		hexView.setCallbacks(this);

		hexView.setOnDragListener(new View.OnDragListener() {
			private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
			private Drawable hoverShape  = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
			private Drawable normalShape = hexView.getBackground();
			private PointF lastCenterPoint = null;

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
							Character character = getCharacter(event);
							Point hexCoordinates = ((HexView)v).getHexCoordinates(pointf);
							((HexView)v).setHighlightHex(hexCoordinates);
							pointf = ((HexView)v).getHighlightCenterPoint();
							if (DRAG_CHARACTER.equals(event.getClipDescription().getLabel())) {
								ClipData.Item item = event.getClipData().getItemAt(0);
								int characterId = Integer.valueOf(item.getText().toString());
								for (Character aCharacter : characters) {
									if (aCharacter.getId() == characterId) {
										character = aCharacter;
										break;
									}
								}
								if(character != null) {
									currentInstance.getCharacterLocations().put(character, pointf);
									if(character.getLocation() != hexCoordinates) {
										character.setLocation(hexCoordinates);
										saveCharacter(character);
									}
								}
							} else if(DRAG_OPPONENT.equals(event.getClipDescription().getLabel())) {
								ClipData.Item item = event.getClipData().getItemAt(0);
								int opponentId = Integer.valueOf(item.getText().toString());
								Creature opponent = null;
								for (Creature anOpponent : opponents) {
									if (anOpponent.getId() == opponentId) {
										opponent = anOpponent;
										break;
									}
								}
								currentInstance.getOpponentLocation().put(opponent, pointf);
							}
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

			private Character getCharacter(DragEvent event) {
				Character character = null;
				if(event.getClipData() != null) {
					ClipData.Item item = event.getClipData().getItemAt(0);
					int characterId = Integer.valueOf(item.getText().toString());
					for (Character aCharacter : characters) {
						if (aCharacter.getId() == characterId) {
							character = aCharacter;
							break;
						}
					}
				}
				return character;
			}

			private void setCharacterLoc(Character character, PointF pointf) {
				PointF centerPoint = hexView.getCenterPoint(pointf);
				currentInstance.getCharacterLocations().put(character, centerPoint);
			}
		});
	}

	private void initCharactersListView(View layout) {
		charactersListView = (ListView)layout.findViewById(R.id.characters_list_view);
		final ArrayAdapter<Character> adapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		charactersListView.setAdapter(adapter);

		if(characters != null) {
			adapter.clear();
			adapter.addAll(characters);
			adapter.notifyDataSetChanged();
		}
		else {
			characterRxHandler.getAll()
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
							adapter.clear();
							adapter.addAll(charactersCollection);
							adapter.notifyDataSetChanged();
						}
					});
		}

		charactersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				ClipData dragData = null;

				Character character = (Character) adapter.getItem(position);
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
}
