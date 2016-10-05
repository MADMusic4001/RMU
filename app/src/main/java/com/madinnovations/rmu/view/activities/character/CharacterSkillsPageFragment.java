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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.data.entities.character.SkillCostEntry;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.SpecializationCostEntry;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
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
	private ArrayAdapter<Skill>           skillsAdapter;
	private ListView                      skillCostsListView;
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
		skillCostsListView = (ListView)layout.findViewById(R.id.skill_cost_list);
		skillsAdapter =  new ArrayAdapter<>(getActivity(), R.layout.list_profession_category_costs_child_row);

		if(charactersFragment.getCurrentInstance().getProfession() != null) {
			for(final SkillCategory category :
				charactersFragment.getCurrentInstance().getProfession().getAssignableSkillCosts().keySet()) {
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
								skillsAdapter.clear();
								skillsAdapter.addAll(skillCollection);
								skillsAdapter.notifyDataSetChanged();
							}
						});
			}
		}
	}
}
