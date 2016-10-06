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
package com.madinnovations.rmu.view.activities.character;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.data.entities.character.SkillCostEntry;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.SkillCost;
import com.madinnovations.rmu.data.entities.common.SpecializationCostEntry;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.character.AssignableCostsAdapter;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Handles interactions with the UI for character creation.
 */
public class CharacterSkillsPageFragment extends Fragment {
	private static final String LOG_TAG = "CharacterSkillsPageFrag";
	@Inject
	protected SkillRxHandler              skillRxHandler;
	@Inject
	protected SpecializationRxHandler     specializationRxHandler;
	private CharactersFragment            charactersFragment;
	private AssignableCostsAdapter[]      assignableSkillsAdapters;
	private LinearLayout                  assignableCostLayoutRows;
	private ListView[]                    skillCostsListViews;
	private ListView                      skillRanksListView;
	private ListView                      talentTiersListView;
	private Map<Stat, EditText>           statEditTextMap;
	private List<SpecializationCostEntry> skillCostsMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.character_skills_talents_stats_fragment, container, false);

		initSkillCostsListView(layout);
//		skillRanksListView = initSkillRanksListView(layout);
//		talentTiersListView = initTalentTiersListView(layout);

		return layout;
	}

	@Override
	public void onPause() {
		if(copyViewsToItem()) {
			charactersFragment.saveItem();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@SuppressWarnings("ConstantConditions")
	public boolean copyViewsToItem() {
		boolean changed = false;
		return changed;
	}

	public void copyItemToViews() {
	}

	public void setCharactersFragment(CharactersFragment charactersFragment) {
		this.charactersFragment = charactersFragment;
	}

	private void initSkillCostsListView(View layout) {
		assignableCostLayoutRows = (LinearLayout)layout.findViewById(R.id.assignable_cost_layout_rows);

		changeProfession();
	}

	public void changeProfession() {
		if(charactersFragment.getCurrentInstance().getProfession() != null &&
				charactersFragment.getCurrentInstance().getProfession().getAssignableSkillCosts().size() > 0) {
			int size = charactersFragment.getCurrentInstance().getProfession().getAssignableSkillCosts().size();
			if(skillCostsListViews == null || skillCostsListViews.length < size) {
				size = ((size + 2) / 3) * 3;
				skillCostsListViews = new ListView[size];
				assignableSkillsAdapters = new AssignableCostsAdapter[size];
			}
			for(int i = 0; i < charactersFragment.getCurrentInstance().getProfession().getAssignableSkillCosts().size(); i += 3) {
				addAssignableCostRow(i);
			}
			int i = 0;
			for(final SkillCategory category :
					charactersFragment.getCurrentInstance().getProfession().getAssignableSkillCosts().keySet()) {
				final int index = i++;
				skillCostsListViews[index].setVisibility(View.VISIBLE);
				skillRxHandler.getSkillsForCategory(category)
						.subscribe(new Subscriber<Collection<Skill>>() {
							@Override
							public void onCompleted() {}
							@Override
							public void onError(Throwable e) {
								Log.e(LOG_TAG, "Exception caught getting Skill for category " + category.getName() + ".", e);
							}
							@Override
							public void onNext(Collection<Skill> skillCollection) {
								List<SkillCost> skillCosts = charactersFragment.getCurrentInstance().getProfession()
										.getAssignableSkillCosts().get(category);
								if(skillCollection.size() == skillCosts.size()) {
									assignableSkillsAdapters[index].clear();
									int i = 0;
									for(Skill skill : skillCollection) {
										SkillCostEntry entry = new SkillCostEntry(skill, skillCosts.get(i++));
										assignableSkillsAdapters[index].add(entry);
									}
									Log.d(LOG_TAG, "Loaded " + i + " skillCosts");
									assignableSkillsAdapters[index].notifyDataSetChanged();
								}
							}
						});
			}
			for(int j = i; j < skillCostsListViews.length; j++) {
				skillCostsListViews[j].setVisibility(View.INVISIBLE);
			}
		}
	}

	private void addAssignableCostRow(int startIndex) {
		LayoutInflater.from(getActivity()).inflate(R.layout.assignable_costs_header, assignableCostLayoutRows);
		LinearLayout linearLayout = new LinearLayout(getActivity());
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		for(int i = startIndex; i < startIndex + 3; i++) {
			ListView listView = new ListView(getActivity());
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listView.getLayoutParams();
			if(params == null) {
				params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
				listView.setLayoutParams(params);
			}
			params.weight = 1;
			skillCostsListViews[i] = listView;
			final AssignableCostsAdapter adapter = new AssignableCostsAdapter(getActivity());
			listView.setAdapter(adapter);
			assignableSkillsAdapters[i] = adapter;
			listView.setVisibility(View.INVISIBLE);
			linearLayout.addView(listView);
		}
		assignableCostLayoutRows.addView(linearLayout);
	}
}
