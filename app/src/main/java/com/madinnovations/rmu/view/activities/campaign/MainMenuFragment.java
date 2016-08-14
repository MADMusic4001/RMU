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
	private int currentGroup = -1;

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

	private void initListView(final ExpandableListView listView) {
		createMenuData();
		ExpandableListAdapter adapter = new MainMenuListAdapter(this.getActivity(), groupNames, groupItems);
		listView.setAdapter(adapter);
		listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int i) {
				if(currentGroup != -1) {
					listView.collapseGroup(currentGroup);
				}
				currentGroup = i;
			}
		});
		listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				switch (groupPosition) {
					case 0:
						switch (childPosition) {
							case 0:
//								((CampaignActivity)getActivity()).showCharacters();
								break;
							case 1:
//								((CampaignActivity)getActivity()).showCultures();
								break;
							case 2:
//								((CampaignActivity)getActivity()).showProfessions();
								break;
							case 3:
//								((CampaignActivity)getActivity()).showRaces();
								break;
						}
						break;
					case 1:
						switch (childPosition) {
							case 0:
								((CampaignActivity)getActivity()).showCreatureArchetypes();
								break;
							case 1:
								((CampaignActivity)getActivity()).showCreatureCategories();
								break;
							case 2:
//								((CampaignActivity)getActivity()).showCreatures();
								break;
							case 3:
								((CampaignActivity)getActivity()).showCreatureTypes();
								break;
							case 4:
								((CampaignActivity)getActivity()).showCreatureVarieties();
								break;
							case 5:
								((CampaignActivity)getActivity()).showOutlooks();
								break;
						}
						break;
					case 2:
						switch (childPosition) {
							case 0:
								((CampaignActivity)getActivity()).showBodyParts();
								break;
							case 1:
								((CampaignActivity)getActivity()).showCriticalCodes();
								break;
							case 2:
								((CampaignActivity)getActivity()).showCriticalResults();
								break;
							case 3:
//								((CampaignActivity)getActivity()).showCriticalTypes();
								break;
							case 4:
//								((CampaignActivity)getActivity()).showDamageResults();
								break;
							case 5:
//								((CampaignActivity)getActivity()).showDamageTables();
								break;
						}
						break;
					case 3:
						switch (childPosition) {
							case 0:
								((CampaignActivity)getActivity()).showLocomotionTypes();
								break;
							case 1:
								((CampaignActivity)getActivity()).showParameters();
								break;
							case 2:
								((CampaignActivity)getActivity()).showSizes();
								break;
							case 3:
								((CampaignActivity)getActivity()).showSkillCategories();
								break;
							case 4:
								((CampaignActivity)getActivity()).showSkills();
								break;
							case 5:
								((CampaignActivity)getActivity()).showSpecializations();
								break;
							case 6:
								((CampaignActivity)getActivity()).showStats();
								break;
							case 7:
								((CampaignActivity)getActivity()).showTalentCategories();
								break;
							case 8:
								((CampaignActivity)getActivity()).showTalents();
								break;
						}
						break;
					case 4:
						switch (childPosition) {
							case 0:
								((CampaignActivity)getActivity()).showItems();
								break;
							case 1:
//								((CampaignActivity)getActivity()).showWeapons();
								break;
						}
						break;
					case 5:
						switch (childPosition) {
							case 0:
//								((CampaignActivity)getActivity()).showGenerateEncounters();
								break;
							case 1:
//								((CampaignActivity)getActivity()).showGenerateLoot();
								break;
							case 2:
//								((CampaignActivity)getActivity()).showAttemptManeuver();
								break;
							case 3:
//								((CampaignActivity)getActivity()).showResolveCombat();
								break;
							case 4:
//								((CampaignActivity)getActivity()).showAdvanceTime();
								break;
						}
					case 6:
						((CampaignActivity)getActivity()).showAbout();
						break;
				}
				return false;
			}
		});
	}

	private void createMenuData() {
		groupNames = new ArrayList<>(7);
		groupItems = new HashMap<>(5);

		groupNames.add(getString(R.string.manage_character_data));
		groupNames.add(getString(R.string.manage_creature_data));
		groupNames.add(getString(R.string.manage_combat_data));
		groupNames.add(getString(R.string.manage_common_data));
		groupNames.add(getString(R.string.manage_item_data));
		groupNames.add(getString(R.string.play));
		groupNames.add(getString(R.string.about_rmu));

		List<String> characterItems = new ArrayList<>(4);
		characterItems.add(getString(R.string.manage_character));
		characterItems.add(getString(R.string.manage_culture));
		characterItems.add(getString(R.string.manage_profession));
		characterItems.add(getString(R.string.manage_race));

		List<String> creatureItems = new ArrayList<>(6);
		creatureItems.add(getString(R.string.manage_archetypes));
		creatureItems.add(getString(R.string.manage_categories));
		creatureItems.add(getString(R.string.manage_creatures));
		creatureItems.add(getString(R.string.manage_types));
		creatureItems.add(getString(R.string.manage_varieties));
		creatureItems.add(getString(R.string.manage_outlooks));

		List<String> combatItems = new ArrayList<>(6);
		combatItems.add(getString(R.string.manage_body_parts));
		combatItems.add(getString(R.string.manage_critical_codes));
		combatItems.add(getString(R.string.manage_critical_results));
		combatItems.add(getString(R.string.manage_critical_types));
		combatItems.add(getString(R.string.manage_damage_results));
		combatItems.add(getString(R.string.manage_damage_tables));

		List<String> commonDataItems = new ArrayList<>(9);
		commonDataItems.add(getString(R.string.manage_locomotion_types));
		commonDataItems.add(getString(R.string.manage_parameters));
		commonDataItems.add(getString(R.string.manage_sizes));
		commonDataItems.add(getString(R.string.manage_skill_categories));
		commonDataItems.add(getString(R.string.manage_skills));
		commonDataItems.add(getString(R.string.manage_specializations));
		commonDataItems.add(getString(R.string.manage_stats));
		commonDataItems.add(getString(R.string.manage_talent_categories));
		commonDataItems.add(getString(R.string.manage_talents));

		List<String> itemItems = new ArrayList<>(2);
		itemItems.add(getString(R.string.manage_items));
		itemItems.add(getString(R.string.manage_weapons));

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
		groupItems.put(groupNames.get(2), combatItems);
		groupItems.put(groupNames.get(3), commonDataItems);
		groupItems.put(groupNames.get(4), itemItems);
		groupItems.put(groupNames.get(5), playItems);
		groupItems.put(groupNames.get(6), aboutItems);
	}
}
