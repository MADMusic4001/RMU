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
package com.madinnovations.rmu.view.activities.character;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.character.CultureRxHandler;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Handles interactions with the UI for Race talents.
 */
public class RacesCulturesPageFragment extends Fragment {
	@SuppressWarnings("unused")
	private static final String TAG = "RacesCulturesPageFrag";
	@Inject
	protected CultureRxHandler      cultureRxHandler;
	private   RacesFragment         racesFragment;
	private   CheckBox              allowAnyCheckBox;
	private   ArrayAdapter<Culture> culturesArrayAdapter;
	private   ListView              culturesListView;

	/**
	 * Creates a new RacesTalentsPageFragment instance.
	 *
	 * @param racesFragment  the RacesFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static RacesCulturesPageFragment newInstance(RacesFragment racesFragment) {
		Log.d(TAG, "newInstance: ");
		RacesCulturesPageFragment fragment = new RacesCulturesPageFragment();
		fragment.racesFragment = racesFragment;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity) getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		Log.d(TAG, "onCreateView: ");
		View layout = inflater.inflate(R.layout.races_cultures_page, container, false);

		initAllowAnyCheckBox(layout);
		initCulturesListView(layout);
		copyItemToViews();
		return layout;
	}

	// <editor-fold desc="Copy to/from views/entity methods">
	public boolean copyViewsToItem() {
		Race currentInstance = racesFragment.getCurrentInstance();
		boolean changed = false;

		if(allowAnyCheckBox.isChecked() && !currentInstance.getAllowedCultures().isEmpty()) {
			currentInstance.getAllowedCultures().clear();
			changed = true;
		}

		getSelectedCultures();
		return changed;
	}

	public void copyItemToViews() {
		Log.d(TAG, "copyItemToViews: ");
		Race currentInstance = racesFragment.getCurrentInstance();
		allowAnyCheckBox.setChecked(currentInstance.getAllowedCultures().isEmpty());
		culturesListView.setEnabled(!currentInstance.getAllowedCultures().isEmpty());
		setSelectedCultures();
	}
	// </editor-fold>

	private void initAllowAnyCheckBox(View layout) {
		allowAnyCheckBox = (CheckBox)layout.findViewById(R.id.allow_any_check_box);

		allowAnyCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Race race = racesFragment.getCurrentInstance();
				boolean newValue = allowAnyCheckBox.isChecked();
				if(newValue) {
					race.getAllowedCultures().clear();
					culturesListView.setEnabled(false);
				}
				else {
					culturesListView.setEnabled(true);
				}
			}
		});
	}

	private void initCulturesListView(View layout) {
		culturesListView = (ListView)layout.findViewById(R.id.cultures_list_view);
		culturesArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		culturesListView.setAdapter(culturesArrayAdapter);

		cultureRxHandler.getAll()
				.subscribe(new Subscriber<Collection<Culture>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Error loading all Culture instances", e);
					}
					@Override
					public void onNext(Collection<Culture> cultures) {
						culturesArrayAdapter.clear();
						culturesArrayAdapter.addAll(cultures);
						culturesArrayAdapter.notifyDataSetChanged();
					}
				});

		culturesListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				getSelectedCultures();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				getSelectedCultures();
			}
		});
		culturesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				getSelectedCultures();
			}
		});
	}

	private void getSelectedCultures() {
		SparseBooleanArray selectedItems = culturesListView.getCheckedItemPositions();
		List<Culture> cultures = new ArrayList<>(culturesListView.getCheckedItemCount());
		for(int i = 0; i < selectedItems.size(); i++) {
			if(selectedItems.valueAt(i)) {
				cultures.add(culturesArrayAdapter.getItem(selectedItems.keyAt(i)));
			}
		}
		racesFragment.getCurrentInstance().setAllowedCultures(cultures);
		Log.d(TAG, "getSelectedCultures: allowedCultures = " + cultures);
		racesFragment.saveItem();
	}

	private void setSelectedCultures() {
		culturesListView.clearChoices();
		for(Culture culture : racesFragment.getCurrentInstance().getAllowedCultures()) {
			int position = culturesArrayAdapter.getPosition(culture);
			if(position >= 0) {
				culturesListView.setItemChecked(position, true);
			}
		}
		culturesArrayAdapter.notifyDataSetChanged();
	}
}
