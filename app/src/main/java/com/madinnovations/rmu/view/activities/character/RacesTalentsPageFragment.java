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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellListRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellRxHandler;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.common.TalentTier;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.common.TalentTierListAdapter;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Handles interactions with the UI for Race talents.
 */
public class RacesTalentsPageFragment extends Fragment implements TalentTierListAdapter.TalentTiersAdapterCallbacks {
	@SuppressWarnings("unused")
	private static final String TAG = "RacesTalentsPageFrag";
	@Inject
	protected AttackRxHandler         attackRxHandler;
	@Inject
	protected SkillRxHandler          skillRxHandler;
	@Inject
	protected SpecializationRxHandler specializationRxHandler;
	@Inject
	protected SpellRxHandler          spellRxHandler;
	@Inject
	protected SpellListRxHandler      spellListRxHandler;
	@Inject
	protected TalentRxHandler         talentRxHandler;
	private   TalentTierListAdapter   talentsAdapter;
	private   RacesFragment           racesFragment;
	private   Collection<Talent>      talentsList = new ArrayList<>();

	/**
	 * Creates a new RacesTalentsPageFragment instance.
	 *
	 * @param racesFragment  the RacesFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static RacesTalentsPageFragment newInstance(RacesFragment racesFragment) {
		RacesTalentsPageFragment fragment = new RacesTalentsPageFragment();
		fragment.racesFragment = racesFragment;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity) getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.races_talents_page, container, false);

		initTalentListView(layout);
		return layout;
	}

	@Override
	public boolean purchaseTier(Talent talent, short startingTiers, short purchasedThisLevel) {
		boolean result;

		if(purchasedThisLevel >= talent.getMaxTier()) {
			result = false;
		}
		else {
			TalentInstance talentInstance = racesFragment.getCurrentInstance().getTalentsAndFlawsMap().get(talent);
			if(talentInstance == null) {
				talentInstance = new TalentInstance();
				talentInstance.setTalent(talent);
			}
			talentInstance.setTiers((short)(purchasedThisLevel + 1));
			racesFragment.getCurrentInstance().getTalentsAndFlawsMap().put(talent, talentInstance);
			racesFragment.saveItem();
			result = true;
		}

		return result;
	}

	@Override
	public boolean sellTier(Talent talent, short startingTiers, short purchasedThisLevel) {
		boolean result;

		if(purchasedThisLevel == 0) {
			result = false;
		}
		else {
			short newValue = (short)(purchasedThisLevel - 1);
			if(newValue > 0) {
				TalentInstance talentInstance = racesFragment.getCurrentInstance().getTalentsAndFlawsMap().get(talent);
				if(talentInstance == null) {
					talentInstance = new TalentInstance();
					talentInstance.setTalent(talent);
				}
				talentInstance.setTiers(newValue);
				racesFragment.getCurrentInstance().getTalentsAndFlawsMap().put(talent, talentInstance);
			}
			else {
				racesFragment.getCurrentInstance().getTalentsAndFlawsMap().remove(talent);
			}
			racesFragment.saveItem();
			result = true;
		}

		return result;
	}

	@Override
	public void setParameterValue(Talent talent, Parameter parameter, int value, String enumName) {

	}

	// <editor-fold desc="Copy to/from views/entity methods">
	public boolean copyViewsToItem() {
		return false;
	}

	public void copyItemToViews() {
		copyTalentsToView();
	}
	// </editor-fold>

	private void initTalentListView(View layout) {
		ListView talentsListView = (ListView) layout.findViewById(R.id.talents_list_view);
		talentsAdapter = new TalentTierListAdapter(getActivity(), this, attackRxHandler, skillRxHandler,
												   specializationRxHandler, spellRxHandler, spellListRxHandler);
		talentsListView.setAdapter(talentsAdapter);

		if(talentsList.isEmpty()) {
			talentRxHandler.getAll()
					.subscribe(new Subscriber<Collection<Talent>>() {
						@Override
						public void onCompleted() {
							copyTalentsToView();
						}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception caught loading all Talent instances", e);
						}
						@Override
						public void onNext(Collection<Talent> talents) {
							talentsList = talents;
						}
					});
		}
	}

	private void copyTalentsToView() {
		Map<Talent, TalentInstance> talentInstanceMap = racesFragment.getCurrentInstance().getTalentsAndFlawsMap();
		talentsAdapter.clear();
		for (Talent talent : talentsList) {
			TalentInstance talentInstance = talentInstanceMap.get(talent);
			TalentTier talentTier = new TalentTier(talent, talentInstance == null ? 0 : talentInstance.getTiers());
			talentTier.setEndingTiers(talentTier.getTier());
			talentsAdapter.add(talentTier);
		}
		talentsAdapter.notifyDataSetChanged();
	}
}
