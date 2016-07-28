/**
 * Copyright (C) 2016 MadInnovations
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.view.activities.campaign;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.adapters.MainMenuListAdapter;
import com.madinnovations.rmu.view.di.modules.FragmentModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragment class for interacting with the Menu section of the campaign activity.
 */
public class MainMenuFragment extends Fragment {
	private List<String> groupNames;
	private Map<String, List<String>> groupItems;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newFragmentComponent(new FragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.campaign_main_menu_fragment, container, false);

		ExpandableListView listView = (ExpandableListView) layout.findViewById(R.id.menu_list);
		initListView(listView);

		return layout;
	}

	private void initListView(ExpandableListView listView) {
		createMenuData();
		ExpandableListAdapter adapter = new MainMenuListAdapter(this.getActivity(), groupNames, groupItems);
		listView.setAdapter(adapter);
		listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				switch (groupPosition) {
					case 0:
						switch (childPosition) {
							case 0:
								break;
							case 1:
								break;
							case 2:
								break;
							case 3:
								break;
						}
						break;
					case 1:
						switch (childPosition) {
							case 0:
								break;
							case 1:
								break;
							case 2:
								break;
							case 3:
								break;
							case 4:
								break;
						}
						break;
					case 2:
						switch (childPosition) {
							case 0:
								((CampaignActivity)getActivity()).showTalentCategories();
								break;
							case 1:
								break;
							case 2:
								break;
							case 3:
								break;
							case 4:
								break;
							case 5:
								((CampaignActivity)getActivity()).showStats();
								break;
						}
						break;
					case 3:
						switch (childPosition) {
							case 0:
								break;
							case 1:
								break;
							case 2:
								break;
							case 3:
								break;
						}
						break;
					case 4:
						((CampaignActivity)getActivity()).showAbout();
						break;
				}
				return false;
			}
		});
	}

	private void createMenuData() {
		groupNames = new ArrayList<>(5);
		groupItems = new HashMap<>(5);

		groupNames.add(getString(R.string.manage_character_data));
		groupNames.add(getString(R.string.manage_creature_data));
		groupNames.add(getString(R.string.manage_global_data));
		groupNames.add(getString(R.string.play));
		groupNames.add(getString(R.string.about_rmu));

		List<String> characterItems = new ArrayList<>(4);
		characterItems.add(getString(R.string.manage_character));
		characterItems.add(getString(R.string.manage_race));
		characterItems.add(getString(R.string.manage_culture));
		characterItems.add(getString(R.string.manage_profession));

		List<String> creatureItems = new ArrayList<>(5);
		creatureItems.add(getString(R.string.manage_archetypes));
		creatureItems.add(getString(R.string.manage_categories));
		creatureItems.add(getString(R.string.manage_types));
		creatureItems.add(getString(R.string.manage_varieties));
		creatureItems.add(getString(R.string.manage_critical_codes));

		List<String> globalDataItems = new ArrayList<>(6);
		globalDataItems.add(getString(R.string.manage_talent_categories));
		globalDataItems.add(getString(R.string.manage_talents));
		globalDataItems.add(getString(R.string.manage_sizes));
		globalDataItems.add(getString(R.string.manage_skill_categories));
		globalDataItems.add(getString(R.string.manage_skills));
		globalDataItems.add(getString(R.string.manage_stats));

		List<String> playItems = new ArrayList<>(5);
		playItems.add(getString(R.string.generate_encounter));
		playItems.add(getString(R.string.generate_loot));
		playItems.add(getString(R.string.attempt_maneuver));
		playItems.add(getString(R.string.start_combat));
		playItems.add(getString(R.string.advance_time));

		List<String> aboutItems = new ArrayList<>(1);
		aboutItems.add(getString(R.string.about_rmu));

		groupItems.put(groupNames.get(0), characterItems);
		groupItems.put(groupNames.get(1), creatureItems);
		groupItems.put(groupNames.get(2), globalDataItems);
		groupItems.put(groupNames.get(3), playItems);
		groupItems.put(groupNames.get(4), aboutItems);
	}
}
