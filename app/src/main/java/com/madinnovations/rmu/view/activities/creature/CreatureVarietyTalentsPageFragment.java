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
package com.madinnovations.rmu.view.activities.creature;

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
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.common.TalentTier;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.common.TalentTierListAdapter;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Handles interactions with the UI for creature varieties.
 */
public class CreatureVarietyTalentsPageFragment extends Fragment implements TalentTierListAdapter.TalentTiersAdapterCallbacks {
	private static final String TAG = "CVTalentsPage";
	@Inject
	protected AttackRxHandler           attackRxHandler;
	@Inject
	protected SkillRxHandler            skillRxHandler;
	@Inject
	protected SpecializationRxHandler   specializationRxHandler;
	@Inject
	protected SpellRxHandler            spellRxHandler;
	@Inject
	protected SpellListRxHandler        spellListRxHandler;
	@Inject
	protected TalentRxHandler           talentRxHandler;
	private   TalentTierListAdapter     talentTiersAdapter;
	private   CreatureVarietiesFragment creatureVarietiesFragment;
	private   Collection<Talent>        talents = null;
	private   EditText                  currentDpText;

	/**
	 * Creates new CreatureVarietyTalentsPageFragment instance.
	 *
	 * @param creatureVarietiesFragment  the CreatureVarietiesFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static CreatureVarietyTalentsPageFragment newInstance(CreatureVarietiesFragment creatureVarietiesFragment) {
		CreatureVarietyTalentsPageFragment fragment = new CreatureVarietyTalentsPageFragment();
		fragment.creatureVarietiesFragment = creatureVarietiesFragment;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(creatureVarietiesFragment == null) {
			return null;
		}

		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		View fragmentView = inflater.inflate(R.layout.creature_variety_talents_page, container, false);

		currentDpText = (EditText) fragmentView.findViewById(R.id.current_dp_text);
		currentDpText.setText((String.valueOf(creatureVarietiesFragment.getCurrentInstance().getLeftoverDP())));
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
		CreatureVariety creatureVariety = creatureVarietiesFragment.getCurrentInstance();

		if(purchasedThisLevel == 0 && startingTiers == 0) {
			cost = talent.getDpCost();
			result = true;
		}
		else if(purchasedThisLevel + startingTiers < talent.getMaxTier() && purchasedThisLevel < 2) {
			cost = talent.getDpCostPerTier();
			result = true;
		}
		if(result) {
			if (cost < creatureVariety.getLeftoverDP()) {
				creatureVariety.setLeftoverDP((short) (creatureVariety.getLeftoverDP() - cost));
				currentDpText.setText((String.valueOf(creatureVariety.getLeftoverDP())));
				TalentInstance talentInstance = creatureVariety.getTalentsMap().get(talent);
				if(talentInstance == null) {
					talentInstance = new TalentInstance();
					talentInstance.setTalent(talent);
				}
				short oldTiers = talentInstance.getTiers();
				Log.d(TAG, "purchaseTier: oldTiers = " + oldTiers);
				talentInstance.setTiers((short) (oldTiers + 1));
				creatureVariety.getTalentsMap().put(talent, talentInstance);
				creatureVarietiesFragment.saveItem();
			}
			else {
				result = false;
			}
		}

		return result;
	}

	@Override
	public boolean sellTier(Talent talent, short startingTiers, short purchasedThisLevel) {
		boolean result = false;
		short cost = 0;
		CreatureVariety creatureVariety = creatureVarietiesFragment.getCurrentInstance();

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
			creatureVariety.setLeftoverDP((short) (creatureVariety.getLeftoverDP() + cost));
			currentDpText.setText((String.valueOf(creatureVariety.getLeftoverDP())));
			TalentInstance talentInstance = creatureVariety.getTalentsMap().get(talent);
			if(talentInstance != null) {
				short oldTiers = talentInstance.getTiers();
				if (oldTiers > 1) {
					talentInstance.setTiers((short) (oldTiers - 1));
					creatureVariety.getTalentsMap().put(talent, talentInstance);
					creatureVarietiesFragment.saveItem();
				}
				else {
					creatureVariety.getTalentsMap().remove(talent);
					creatureVarietiesFragment.saveItem();
				}
			}
		}

		return result;
	}

	@Override
	public void setParameterValue(Talent talent, Parameter parameter, int value, String enumName) {
		TalentInstance talentInstance = creatureVarietiesFragment.getCurrentInstance().getTalentsMap().get(talent);
		if(talentInstance != null) {
			Object paramValue = talentInstance.getParameterValues().get(parameter);
			if(enumName != null && !enumName.equals(paramValue)) {
				talentInstance.getParameterValues().put(parameter, enumName);
				creatureVarietiesFragment.saveItem();
			}
			else if(enumName == null && (paramValue == null || !Integer.valueOf(value).equals(paramValue))) {
				talentInstance.getParameterValues().put(parameter, value);
				creatureVarietiesFragment.saveItem();
			}
		}
	}

	public boolean copyViewsToItem() {
		return false;
	}

	public void copyItemToViews() {
		CreatureVariety creatureVariety = creatureVarietiesFragment.getCurrentInstance();
		currentDpText.setText((String.valueOf(creatureVariety.getLeftoverDP())));
		copyTalentTiers();
	}

	private void copyTalentTiers() {
		if(talents != null) {
			CreatureVariety creatureVariety = creatureVarietiesFragment.getCurrentInstance();
			List<TalentTier> tiersList = new ArrayList<>();
			for (Talent talent : talents) {
				TalentTier talentTier = new TalentTier();
				talentTier.setTalent(talent);
				short tiers = 0;
				if (creatureVariety.getTalentsMap().get(talent) != null) {
					tiers = creatureVariety.getTalentsMap().get(talent).getTiers();
				}
				talentTier.setTier(tiers);
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
