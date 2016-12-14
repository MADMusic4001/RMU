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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellListRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellRxHandler;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.common.TalentTier;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.common.TalentTierListAdapter;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Handles interactions with the UI for character creation.
 */
public class CharacterTalentsPageFragment extends Fragment implements TalentTierListAdapter.TalentTiersAdapterCallbacks {
	private static final String TAG = "CharacterTalentsPage";
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
	private   TalentTierListAdapter   talentTiersAdapter;
	private   CharactersFragment      charactersFragment;
	private   Collection<Talent>      talents = null;
	private   EditText                currentDpText;

	/**
	 * Creates new CharacterTalentsPageFragment instance.
	 *
	 * @param charactersFragment  the CharactersFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static CharacterTalentsPageFragment newInstance(CharactersFragment charactersFragment) {
		CharacterTalentsPageFragment fragment = new CharacterTalentsPageFragment();
		fragment.charactersFragment = charactersFragment;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(charactersFragment == null) {
			return null;
		}

		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View fragmentView = inflater.inflate(R.layout.character_talents_page, container, false);

		currentDpText = (EditText) fragmentView.findViewById(R.id.current_dp_text);
		currentDpText.setText((String.valueOf(charactersFragment.getCurrentInstance().getCurrentDevelopmentPoints())));
		initTalentTiersListView(fragmentView);

		fragmentView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Log.d(TAG, "onLongClick: ");
				return false;
			}
		});

		return fragmentView;
	}

	@Override
	public boolean purchaseTier(Talent talent, short startingTiers, short purchasedThisLevel) {
		boolean result = false;
		short cost = 0;
		Character character = charactersFragment.getCurrentInstance();

		if(character.getCurrentLevel() == 1 || character.getCampaign().isAllowTalentsBeyondFirst()) {
			if(purchasedThisLevel == 0 && startingTiers == 0) {
				cost = talent.getDpCost();
				result = true;
			}
			else if(purchasedThisLevel + startingTiers < talent.getMaxTier() && purchasedThisLevel < 2) {
				cost = talent.getDpCostPerTier();
				result = true;
			}
			if(result) {
				if (cost < character.getCurrentDevelopmentPoints()) {
					character.setCurrentDevelopmentPoints((short) (character.getCurrentDevelopmentPoints() - cost));
					currentDpText.setText((String.valueOf(character.getCurrentDevelopmentPoints())));
					TalentInstance talentInstance = character.getTalentInstances().get(talent);
					if(talentInstance == null) {
						talentInstance = new TalentInstance();
						talentInstance.setTalent(talent);
					}
					short oldTiers = talentInstance.getTiers();
					Log.d(TAG, "purchaseTier: oldTiers = " + oldTiers);
					talentInstance.setTiers((short) (oldTiers + 1));
					character.getTalentInstances().put(talent, talentInstance);
					character.getCurrentLevelTalentTiers().put(talent, purchasedThisLevel);
					charactersFragment.saveItem();
				}
				else {
					result = false;
				}
			}
		}

		return result;
	}

	@Override
	public boolean sellTier(Talent talent, short startingTiers, short purchasedThisLevel) {
		boolean result = false;
		short cost = 0;
		Character character = charactersFragment.getCurrentInstance();

		Log.d(TAG, "sellTier: purchasedThisLevel = " + purchasedThisLevel);
		if(startingTiers == 0 && purchasedThisLevel == 1) {
			cost = talent.getDpCost();
			result = true;
		}
		else if (purchasedThisLevel > 0) {
			cost = talent.getDpCostPerTier();
			result = true;
		}
		if(result) {
			character.setCurrentDevelopmentPoints((short) (character.getCurrentDevelopmentPoints() + cost));
			currentDpText.setText((String.valueOf(character.getCurrentDevelopmentPoints())));
			TalentInstance talentInstance = character.getTalentInstances().get(talent);
			character.getCurrentLevelTalentTiers().put(talent, purchasedThisLevel);
			if(talentInstance != null) {
				short oldTiers = talentInstance.getTiers();
				if (oldTiers > 1) {
					talentInstance.setTiers((short) (oldTiers - 1));
					character.getTalentInstances().put(talent, talentInstance);
					charactersFragment.saveItem();
				}
				else {
					character.getTalentInstances().remove(talent);
					charactersFragment.saveItem();
				}
			}
		}

		return result;
	}

	@Override
	public void setParameterValue(Talent talent, Parameter parameter, int value, String enumName) {
		TalentInstance talentInstance = charactersFragment.getCurrentInstance().getTalentInstances().get(talent);
		if(talentInstance != null) {
			Object paramValue = talentInstance.getParameterValues().get(parameter);
			if(enumName != null && !enumName.equals(paramValue)) {
				talentInstance.getParameterValues().put(parameter, enumName);
				charactersFragment.saveItem();
			}
			else if(enumName == null && (paramValue == null || !Integer.valueOf(value).equals(paramValue))) {
				talentInstance.getParameterValues().put(parameter, value);
				charactersFragment.saveItem();
			}
		}
	}

	public boolean copyViewsToItem() {
		return false;
	}

	public void copyItemToViews() {
		Character character = charactersFragment.getCurrentInstance();
		currentDpText.setText((String.valueOf(character.getCurrentDevelopmentPoints())));
		copyTalentTiers();
	}

	private void copyTalentTiers() {
		if(talents != null) {
			Character character = charactersFragment.getCurrentInstance();
			List<TalentTier> tiersList = new ArrayList<>();
			for (Talent talent : talents) {
				TalentTier talentTier = new TalentTier();
				talentTier.setTalent(talent);
				short tiers = 0;
				if (character.getTalentInstances().get(talent) != null) {
					tiers = character.getTalentInstances().get(talent).getTiers();
				}
				talentTier.setTier(tiers);
				Short purchasedThisLevel = character.getCurrentLevelTalentTiers().get(talent);
				if(purchasedThisLevel == null) {
					purchasedThisLevel = (short)0;
				}
				talentTier.setStartingTiers(purchasedThisLevel);
				talentTier.setEndingTiers((short)(tiers - purchasedThisLevel));
				tiersList.add(talentTier);
			}
			talentTiersAdapter.addAll(tiersList);
			talentTiersAdapter.notifyDataSetChanged();
		}
	}

	private void initTalentTiersListView(final View layout) {
		ListView talentTiersListView = (ListView) layout.findViewById(R.id.talent_tiers_list);
		talentTiersAdapter = new TalentTierListAdapter(getActivity(), this, attackRxHandler, skillRxHandler,
													   specializationRxHandler, spellRxHandler, spellListRxHandler);
		talentTiersListView.setAdapter(talentTiersAdapter);

		if(talents == null) {
			talentRxHandler.getAll()
					.subscribe(new Subscriber<Collection<Talent>>() {
						@Override
						public void onCompleted() {
						}

						@Override
						public void onError(Throwable e) {
							Log.d(TAG, "Exception caught getting all Talent instances.", e);
						}

						@Override
						public void onNext(Collection<Talent> talentCollection) {
							talents = talentCollection;
							copyTalentTiers();
						}
					});
		}
		else {
			copyTalentTiers();
		}

		talentTiersListView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Log.d(TAG, "onLongClick: ");
				TextView popupContent = new TextView(getActivity());
				popupContent.setMinimumWidth(layout.getWidth()/2);
				popupContent.setMinimumHeight(layout.getHeight()/2);
				TalentTier talentTier = ((TalentTierListAdapter.TalentTierViewHolder)v.getTag()).getTalentTier();
				popupContent.setText(talentTier.getTalent().getDescription());
				PopupWindow popupWindow = new PopupWindow(popupContent);
				popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
				return true;
			}
		});

		registerForContextMenu(talentTiersListView);
	}
}
