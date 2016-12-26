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
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.character.CharacterRxHandler;
import com.madinnovations.rmu.data.entities.character.Character;
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
public class StartCombatFragment extends Fragment {
	private static final String TAG = "StartCombatFragment";
	private static final String DRAG_CHARACTER = "drag-character";
	private static final String DRAG_OPPONENT = "drag-opponent";
	@Inject
	protected CharacterRxHandler characterRxHandler;
	private   HexView            hexView;
	private   ListView           charactersListView;
	private   ListView           opponentsListView;
	// TODO: Reroll critical option if player successfully used Sense Weakness talent at the beginning of combat.
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

	private void initHexView(View layout) {
		hexView = (HexView)layout.findViewById(R.id.hex_view);

		hexView.setOnDragListener(new View.OnDragListener() {
			@Override
			public boolean onDrag(View view, DragEvent dragEvent) {
				final int action = dragEvent.getAction();

				switch (action) {
					case DragEvent.ACTION_DRAG_STARTED:
						break;
					case DragEvent.ACTION_DRAG_ENTERED:
						break;
					case DragEvent.ACTION_DRAG_LOCATION:
						if(hexView.setHighlightHex(new PointF(dragEvent.getX(), dragEvent.getY()))) {
							hexView.invalidate();
						}
						break;
					case DragEvent.ACTION_DRAG_EXITED:
						break;
					case DragEvent.ACTION_DROP:
						break;
					case DragEvent.ACTION_DRAG_ENDED:
						break;
				}

				return false;
			}
		});
	}

	private void initCharactersListView(View layout) {
		charactersListView = (ListView)layout.findViewById(R.id.characters_list_view);
		final ArrayAdapter<Character> adapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		charactersListView.setAdapter(adapter);

		characterRxHandler.getAll()
				.subscribe(new Subscriber<Collection<Character>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "onError: Exception caught getting all Character instances", e);
					}
					@Override
					public void onNext(Collection<Character> characters) {
						adapter.clear();
						adapter.addAll(characters);
						adapter.notifyDataSetChanged();
					}
				});
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
