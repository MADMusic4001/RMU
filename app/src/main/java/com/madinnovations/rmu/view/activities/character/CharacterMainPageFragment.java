/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.view.activities.character;

import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.campaign.CampaignRxHandler;
import com.madinnovations.rmu.controller.rxhandler.character.CultureRxHandler;
import com.madinnovations.rmu.controller.rxhandler.character.ProfessionRxHandler;
import com.madinnovations.rmu.controller.rxhandler.character.RaceRxHandler;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.view.RMUDragShadowBuilder;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;
import com.madinnovations.rmu.view.utils.Boast;
import com.madinnovations.rmu.view.utils.EditTextUtils;
import com.madinnovations.rmu.view.utils.SpinnerUtils;
import com.madinnovations.rmu.view.utils.TextInputLayoutUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Handles interactions with the UI for character creation.
 */
public class CharacterMainPageFragment extends Fragment implements EditTextUtils.ValuesCallback, SpinnerUtils.ValuesCallback,
		TextInputLayoutUtils.ValuesCallback {
	@SuppressWarnings("unused")
	private static final String TAG = "CharacterMainPageFrag";
	private static final String DRAG_SWAP_STATS = "swap-stats";
	@Inject
	protected CampaignRxHandler        campaignRxHandler;
	@Inject
	protected CultureRxHandler         cultureRxHandler;
	@Inject
	protected ProfessionRxHandler      professionRxHandler;
	@Inject
	protected RaceRxHandler            raceRxHandler;
	private   CharactersFragment       charactersFragment;
	private   EditText                 currentLevelView;
	private   EditText                 experiencePointsView;
	private   EditText                 developmentPointsView;
	private   Button                   levelUpButton;
	private   Spinner                  campaignSpinner;
	private   EditText                 firstNameEdit;
	private   EditText                 lastNameEdit;
	private   EditText                 knownAsEdit;
	private   EditText                 descriptionEdit;
	private   SpinnerUtils<Race>       raceSpinnerUtils;
	private   SpinnerUtils<Culture>    cultureSpinnerUtils;
	private   SpinnerUtils<Profession> professionSpinnerUtils;
	private   SpinnerUtils<String>     realmSpinnerUtils;
	private   SpinnerUtils<String>     realm2SpinnerUtils;
	private   Button                   generateStatsButton;
	private   EditText                 heightEdit;
	private   EditText                 weightEdit;
	private   TextInputLayout          statPointsLabel;
	private   EditText                 statPointsView;
//	private   LinearLayout             newCharacterRow;
	private Map<Statistic, ViewHolder> viewHolderMap = new HashMap<>(Statistic.NUM_STATS);
	private   String                   noRealmName = null;

	/**
	 * Creates new CharacterMainPageFragment instance.
	 *
	 * @param charactersFragment  the CharactersFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static CharacterMainPageFragment newInstance(CharactersFragment charactersFragment) {
		CharacterMainPageFragment fragment = new CharacterMainPageFragment();
		fragment.charactersFragment = charactersFragment;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(charactersFragment == null) {
			return  null;
		}

		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.character_main_page, container, false);

		currentLevelView = (EditText)layout.findViewById(R.id.current_level_view);
		experiencePointsView = (EditText)layout.findViewById(R.id.experience_points_view);
		developmentPointsView = (EditText)layout.findViewById(R.id.development_points_view);
		initLevelUpButton(layout);
		campaignSpinner = new SpinnerUtils<Campaign>().initSpinner(layout, getActivity(), campaignRxHandler.getAll(), this,
				R.id.campaign_spinner, null);
		campaignSpinner.setEnabled(charactersFragment.getCurrentInstance().getCurrentLevel() == 0);
		firstNameEdit = TextInputLayoutUtils.initEdit(layout, getActivity(), this, R.id.first_name_textInputLayout,
				R.id.first_name_edit, R.string.validation_character_first_name_required);
		firstNameEdit.setOnHoverListener(new View.OnHoverListener() {
			@Override
			public boolean onHover(View v, MotionEvent event) {
				Toast.makeText(CharacterMainPageFragment.this.getActivity(), R.string.hint_character_first_name,
							   Toast.LENGTH_LONG).show();
				return false;
			}
		});
		lastNameEdit = TextInputLayoutUtils.initEdit(layout, getActivity(), this, R.id.last_name_textInputLayout,
													 R.id.last_name_edit, R.string.validation_character_last_name_required);
		knownAsEdit = TextInputLayoutUtils.initEdit(layout, getActivity(), this, R.id.known_as_textInputLayout,
													R.id.known_as_edit, R.string.validation_character_known_as_required);
		descriptionEdit = TextInputLayoutUtils.initEdit(layout, getActivity(), this, R.id.notes_textInputLayout, R.id.notes_edit,
				R.string.validation_character_description_required);
		raceSpinnerUtils = new SpinnerUtils<>();
		raceSpinnerUtils.initSpinner(layout, getActivity(), raceRxHandler.getAll(), this, R.id.race_spinner, null);
		cultureSpinnerUtils = new SpinnerUtils<>();
		cultureSpinnerUtils.initSpinner(layout, getActivity(), cultureRxHandler.getAll(), this, R.id.culture_spinner, null);
		professionSpinnerUtils = new SpinnerUtils<>();
		professionSpinnerUtils.initSpinner(layout, getActivity(), professionRxHandler.getAll(), this, R.id.profession_spinner,
										   null);
		List<String> realms = new ArrayList<>(3);
		realms.add(Realm.CHANNELING.toString());
		realms.add(Realm.ESSENCE.toString());
		realms.add(Realm.MENTALISM.toString());
		realmSpinnerUtils = new SpinnerUtils<>();
		realmSpinnerUtils.initSpinner(layout, getActivity(), realms, this, R.id.realm_spinner, null);
		realm2SpinnerUtils = new SpinnerUtils<>();
		noRealmName = getString(R.string.label_no_realm);
		realm2SpinnerUtils.initSpinner(layout, getActivity(), realms, this, R.id.realm2_spinner, noRealmName);
		heightEdit = TextInputLayoutUtils.initEdit(layout, getActivity(), this, R.id.height_textInputLayout, R.id.height_edit,
											R.string.validation_character_height_required);
		weightEdit = TextInputLayoutUtils.initEdit(layout, getActivity(), this, R.id.weight_textInputLayout, R.id.weight_edit,
											R.string.validation_character_weight_required);
		statPointsLabel = (TextInputLayout)layout.findViewById(R.id.stat_points_textInputLayout);
		statPointsView = (EditText)layout.findViewById(R.id.stat_points_view);
		setStatPointsViews();
		for(Statistic statistic : Statistic.getAllStats()) {
			viewHolderMap.put(statistic, new ViewHolder());
		}
		initGenerateStatsButton(layout);
		initStatViews(layout);
		initStatsRows(layout);

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
		raceSpinnerUtils.onResume(getActivity());
		cultureSpinnerUtils.onResume(getActivity());
		professionSpinnerUtils.onResume(getActivity());
		realmSpinnerUtils.onResume(getActivity());
		realm2SpinnerUtils.onResume(getActivity());
		copyItemToViews();
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		switch (editTextId) {
			case R.id.first_name_edit:
				result = charactersFragment.getCurrentInstance().getFirstName();
				break;
			case R.id.last_name_edit:
				result = charactersFragment.getCurrentInstance().getLastName();
				break;
			case R.id.known_as_edit:
				result = charactersFragment.getCurrentInstance().getKnownAs();
				break;
			case R.id.notes_edit:
				result = charactersFragment.getCurrentInstance().getDescription();
				break;
			case R.id.height_edit:
				result = String.valueOf(charactersFragment.getCurrentInstance().getHeight());
				break;
			case R.id.weight_edit:
				result = String.valueOf(charactersFragment.getCurrentInstance().getWeight());
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		short newShort;

		switch (editTextId) {
			case R.id.first_name_edit:
				charactersFragment.getCurrentInstance().setFirstName(newString);
				charactersFragment.saveItem();
				break;
			case R.id.last_name_edit:
				charactersFragment.getCurrentInstance().setLastName(newString);
				charactersFragment.saveItem();
				break;
			case R.id.known_as_edit:
				charactersFragment.getCurrentInstance().setKnownAs(newString);
				charactersFragment.saveItem();
				break;
			case R.id.notes_edit:
				charactersFragment.getCurrentInstance().setDescription(newString);
				charactersFragment.saveItem();
				break;
			case R.id.height_edit:
				newShort = Short.valueOf(newString);
				charactersFragment.getCurrentInstance().setHeight(newShort);
				charactersFragment.saveItem();
				break;
			case R.id.weight_edit:
				newShort = Short.valueOf(newString);
				charactersFragment.getCurrentInstance().setWeight(newShort);
				charactersFragment.saveItem();
				break;
		}
	}

	@Override
	public boolean performLongClick(@IdRes int editTextId) {
		boolean handled = false;

		switch (editTextId) {
			case R.id.height_edit:
				Boast.makeText(getActivity(), String.format(
						getString(R.string.average_height_tooltip),
						charactersFragment.getCurrentInstance().getRace().getAverageHeight()),
							   Toast.LENGTH_LONG).show(true);
				handled = true;
				break;
			case R.id.weight_edit:
				Boast.makeText(getActivity(), String.format(
						getString(R.string.average_weight_tooltip),
						charactersFragment.getCurrentInstance().getRace().getAverageWeight(),
						charactersFragment.getCurrentInstance().getRace().getPoundsPerInch()),
							   Toast.LENGTH_LONG).show(true);
				handled = true;
				break;
		}

		return handled;
	}

	@Override
	public Object getValueForSpinner(@IdRes int spinnerId) {
		Object result = null;

		switch (spinnerId) {
			case R.id.campaign_spinner:
				result = charactersFragment.getCurrentInstance().getCampaign();
				break;
			case R.id.race_spinner:
				result = charactersFragment.getCurrentInstance().getRace();
				break;
			case R.id.culture_spinner:
				result = charactersFragment.getCurrentInstance().getCulture();
				break;
			case R.id.profession_spinner:
				result = charactersFragment.getCurrentInstance().getProfession();
				break;
			case R.id.realm_spinner:
				result = charactersFragment.getCurrentInstance().getRealm();
				break;
			case R.id.realm2_spinner:
				result = charactersFragment.getCurrentInstance().getRealm2();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromSpinner(@IdRes int spinnerId, Object newItem) {
		Character character = charactersFragment.getCurrentInstance();

		switch (spinnerId) {
			case R.id.campaign_spinner:
				if(character.getExperiencePoints() == 0) {
					Campaign newCampaign = (Campaign) newItem;
					character.setCampaign(newCampaign);
					character.generateStats();
					character.setStatPurchasePoints(character.getCampaign().getPowerLevel().getStatPoints());
					generateStatsButton.setEnabled(character.getCurrentLevel() == 0 && character.getCampaign() != null
														   && !character.getCampaign().isBuyStats());
					int visibility = View.GONE;
					if(character.getExperiencePoints() == 0 && character.getCampaign() != null &&
							character.getCampaign().isBuyStats()) {
						visibility = View.VISIBLE;
					}
					setStatPointsViews();
					for (Statistic statistic : Statistic.getAllStats()) {
						viewHolderMap.get(statistic).sellButton.setVisibility(visibility);
						viewHolderMap.get(statistic).buyButton.setVisibility(visibility);
						if(visibility == View.VISIBLE) {
							viewHolderMap.get(statistic).increaseTempButton.setVisibility(View.GONE);
						}
						else {
							viewHolderMap.get(statistic).increaseTempButton.setVisibility(View.VISIBLE);
							boolean enable = character.getCurrentLevel() == (character.getExperiencePoints() / 10000)
									&& character.getCampaign() != null && !character.isStatIncreased(statistic)
									&& (character.statsIncreasedCount() < 2 || character.getCurrentDevelopmentPoints() >= 5);
							viewHolderMap.get(statistic).increaseTempButton.setEnabled(enable);
						}
						viewHolderMap.get(statistic).tempStatView.setText(String.valueOf(
								character.getStatTemps().get(statistic)));
						viewHolderMap.get(statistic).potentialStatView.setText(String.valueOf(
								character.getStatPotentials().get(statistic)));
					}
					charactersFragment.saveItem();
				}
				break;
			case R.id.race_spinner:
				Race newRace = (Race)newItem;
				Race oldRace = character.getRace();
				short bonusDP = newRace.getBonusDevelopmentPoints();
				if(oldRace != null) {
					bonusDP -= oldRace.getBonusDevelopmentPoints();
				}
				if((character.getCurrentDevelopmentPoints() + bonusDP) > 0) {
					character.setCurrentDevelopmentPoints((short)(character.getCurrentDevelopmentPoints() + bonusDP));
				}
				else {
					character.setCurrentDevelopmentPoints((short)(Character.INITIAL_DP + bonusDP));
					character.getCurrentLevelSkillRanks().clear();
					character.getCurrentLevelSpecializationRanks().clear();
					character.getCurrentLevelTalentTiers().clear();
					character.getSkillRanks().clear();
					character.getSpecializationRanks().clear();
				}
				character.getTalentInstances().clear();
				for(TalentInstance talentInstance : newRace.getTalentsAndFlawsList()) {
					TalentInstance newTalentInstance = new TalentInstance();
					newTalentInstance.setTalent(talentInstance.getTalent());
					newTalentInstance.setTiers(talentInstance.getTiers());
					newTalentInstance.getParameterValues().putAll(talentInstance.getParameterValues());
					character.getTalentInstances().add(newTalentInstance);
				}
				developmentPointsView.setText(String.valueOf(character.getCurrentDevelopmentPoints()));
				character.setRace(newRace);
				cultureSpinnerUtils.getAdapter().clear();
				if(character.getRace().getAllowedCultures() == null || character.getRace().getAllowedCultures().isEmpty()) {
					cultureSpinnerUtils.getAdapter().addAll(cultureSpinnerUtils.getAllItems());
				}
				else {
					cultureSpinnerUtils.getAdapter().addAll(character.getRace().getAllowedCultures());
				}
				cultureSpinnerUtils.setSelection(character.getCulture());
				charactersFragment.saveItem();
				copyItemToViews();
				break;
			case R.id.culture_spinner:
				character.setCulture((Culture)newItem);
				charactersFragment.saveItem();
				break;
			case R.id.profession_spinner:
				character.setProfession((Profession) newItem);
				charactersFragment.changeProfession();
				charactersFragment.saveItem();
				break;
			case R.id.realm_spinner:
				Realm newRealm = Realm.getRealmWithName((String)newItem);
				character.setRealm(newRealm);
				charactersFragment.saveItem();
				break;
			case R.id.realm2_spinner:
				newRealm = Realm.getRealmWithName((String)newItem);
				character.setRealm2(newRealm);
				charactersFragment.saveItem();
				break;
		}
	}

	@Override
	public void observerCompleted(@IdRes int spinnerId) {}

	/**
	 * Get currently selected Campaign or null if none is selected.
	 *
	 * @return the currently selected Campaign or null if none is selected.
	 */
	public Campaign getCampaign() {
		Campaign campaign = null;

		if(campaignSpinner.getSelectedItem() != null) {
			campaign = (Campaign)campaignSpinner.getSelectedItem();
		}

		return campaign;
	}

	/**
	 * Get currently selected Race or null if none is selected.
	 *
	 * @return the currently selected Race or null if none is selected.
	 */
	public Race getRace() {
		Race race = null;

		if(raceSpinnerUtils.getSelectedItem() != null) {
			race = raceSpinnerUtils.getSelectedItem();
		}

		return race;
	}

	/**
	 * Get currently selected Culture or null if none is selected.
	 *
	 * @return the currently selected Culture or null if none is selected.
	 */
	public Culture getCulture() {
		Culture culture = null;

		if(cultureSpinnerUtils.getSelectedItem() != null) {
			culture = cultureSpinnerUtils.getSelectedItem();
		}

		return culture;
	}

	@SuppressWarnings("ConstantConditions")
	public boolean copyViewsToItem() {
		boolean changed = false;

		if(charactersFragment != null) {
			Character character = charactersFragment.getCurrentInstance();
			String newString;
			short newShort;

			Campaign newCampaign = (Campaign) campaignSpinner.getSelectedItem();
			if (newCampaign != null && !newCampaign.equals(character.getCampaign()) ||
					(newCampaign == null && character.getCampaign() != null)) {
				character.setCampaign(newCampaign);
				changed = true;
			}

			newString = firstNameEdit.getText().toString();
			if (!newString.equals(character.getFirstName())) {
				character.setFirstName(newString);
				changed = true;
			}
			newString = lastNameEdit.getText().toString();
			if (!newString.equals(character.getLastName())) {
				character.setLastName(newString);
				changed = true;
			}
			newString = knownAsEdit.getText().toString();
			if (!newString.equals(character.getKnownAs())) {
				character.setKnownAs(newString);
				changed = true;
			}
			newString = descriptionEdit.getText().toString();
			if (!newString.equals(character.getDescription())) {
				character.setDescription(newString);
				changed = true;
			}
			Race newRace = raceSpinnerUtils.getSelectedItem();
			Race oldRace = character.getRace();
			if ((newRace != null && !newRace.equals(oldRace)) || (oldRace != null && !oldRace.equals(newRace))) {
				character.setRace(newRace);
				changed = true;
			}
			Culture newCulture = cultureSpinnerUtils.getSelectedItem();
			Culture oldCulture = character.getCulture();
			if ((newCulture != null && !newCulture.equals(oldCulture)) || (oldCulture != null && !oldCulture.equals(
					newCulture))) {
				character.setCulture(newCulture);
				changed = true;
			}
			Profession newProfession = professionSpinnerUtils.getSelectedItem();
			Profession oldProfession = character.getProfession();
			if ((newProfession != null && !newProfession.equals(oldProfession)) ||
					(oldProfession != null && !oldProfession.equals(newProfession))) {
				character.setProfession(newProfession);
				changed = true;
			}
			String newRealmName = realmSpinnerUtils.getSelectedItem();
			Realm newRealm = Realm.getRealmWithName(newRealmName);
			Realm oldRealm = character.getRealm();
			if ((newRealm != null && !newRealm.equals(oldRealm)) || (oldRealm != null && !oldRealm.equals(newRealm))) {
				character.setRealm(newRealm);
				changed = true;
			}
			newRealmName = realm2SpinnerUtils.getSelectedItem();
			newRealm = Realm.getRealmWithName(newRealmName);
			oldRealm = character.getRealm2();
			if((newRealm != null && !newRealm.equals(oldRealm)) || (oldRealm != null && !oldRealm.equals(newRealm))) {
				character.setRealm2(newRealm);
				changed = true;
			}
			else if(newRealm == null && oldRealm != null) {
				character.setRealm2(null);
				changed = true;
			}
			if (heightEdit.getText().length() > 0) {
				newShort = Short.valueOf(heightEdit.getText().toString());
				if (newShort != character.getHeight()) {
					character.setHeight(newShort);
					changed = true;
				}
			}
			if (weightEdit.getText().length() > 0) {
				newShort = Short.valueOf(weightEdit.getText().toString());
				if (newShort != character.getWeight()) {
					character.setWeight(newShort);
					changed = true;
				}
			}
		}

		return changed;
	}

	public void copyItemToViews() {
		if(charactersFragment != null) {
			Character character = charactersFragment.getCurrentInstance();

			short currentLevel = character.getCurrentLevel();
			currentLevelView.setText(String.valueOf(currentLevel));
			experiencePointsView.setText(String.valueOf(character.getExperiencePoints()));
			developmentPointsView.setText(String.valueOf(character.getCurrentDevelopmentPoints()));
			levelUpButton.setEnabled((currentLevel < (short) (character.getExperiencePoints() / 10000)));

			firstNameEdit.setText(character.getFirstName());
			lastNameEdit.setText(character.getLastName());
			knownAsEdit.setText(character.getKnownAs());
			descriptionEdit.setText(character.getDescription());
			raceSpinnerUtils.setSelection(character.getRace());
			cultureSpinnerUtils.setSelection(character.getCulture());
			professionSpinnerUtils.setSelection(character.getProfession());
			if(character.getRealm() != null) {
				realmSpinnerUtils.setSelection(character.getRealm().toString());
			}
			if(character.getRealm2() == null) {
				realm2SpinnerUtils.setSelection(noRealmName);
			}
			else {
				realm2SpinnerUtils.setSelection(character.getRealm2().toString());
			}
			heightEdit.setText(String.valueOf(character.getHeight()));
			weightEdit.setText(String.valueOf(character.getWeight()));

			if (character.getFirstName() == null) {
				firstNameEdit.setError(getString(R.string.validation_character_first_name_required));
			}
			else {
				firstNameEdit.setError(null);
			}
			if (character.getLastName() == null) {
				lastNameEdit.setError(getString(R.string.validation_character_last_name_required));
			}
			else {
				lastNameEdit.setError(null);
			}
			if (character.getKnownAs() == null) {
				knownAsEdit.setError(getString(R.string.validation_character_known_as_required));
			}
			else {
				knownAsEdit.setError(null);
			}
			if (character.getDescription() == null) {
				descriptionEdit.setError(getString(R.string.validation_character_description_required));
			}
			else {
				descriptionEdit.setError(null);
			}

			setStatPointsViews();

			boolean enable = character.getExperiencePoints() == 0 && character.getCampaign() != null
					&& character.getCampaign().isBuyStats();
			for (Map.Entry<Statistic, ViewHolder> entry : viewHolderMap.entrySet()) {
				entry.getValue().tempStatView.setText(String.valueOf(character.getStatTemps().get(entry.getKey())));
				entry.getValue().potentialStatView.setText(String.valueOf(character.getStatPotentials().get(entry.getKey())));
				entry.getValue().buyButton.setEnabled(enable);
				entry.getValue().sellButton.setEnabled(enable);
			}

			generateStatsButton.setEnabled(character.getCurrentLevel() == 0 && character.getCampaign() != null
												   && !character.getCampaign().isBuyStats());

			if (character.getExperiencePoints() > 0) {
//				newCharacterRow.setVisibility(View.GONE);
				firstNameEdit.setEnabled(false);
				lastNameEdit.setEnabled(false);
				raceSpinnerUtils.getSpinner().setEnabled(false);
				cultureSpinnerUtils.getSpinner().setEnabled(false);
				professionSpinnerUtils.getSpinner().setEnabled(false);
				realmSpinnerUtils.getSpinner().setEnabled(false);
				heightEdit.setEnabled(false);
				weightEdit.setEnabled(false);
				campaignSpinner.setEnabled(false);
			}
//			else {
//				newCharacterRow.setVisibility(View.VISIBLE);
//			}
		}
	}

	private void initLevelUpButton(View layout) {
		levelUpButton = (Button)layout.findViewById(R.id.level_up_button);

		levelUpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Character character = charactersFragment.getCurrentInstance();
				if(character.getCurrentLevel() < ((short)(character.getExperiencePoints()/10000) - 1)) {
					character.setCurrentLevel((short)(character.getCurrentLevel() + 1));
					character.setCurrentDevelopmentPoints((short)(character.getCurrentDevelopmentPoints() + 50));
					charactersFragment.saveItem();
				}
			}
		});
	}

	private void initGenerateStatsButton(View layout) {
//		newCharacterRow = (LinearLayout)layout.findViewById(R.id.new_character_row);
		generateStatsButton = (Button)layout.findViewById(R.id.generate_stats_button);
		Character character = charactersFragment.getCurrentInstance();
		generateStatsButton.setEnabled(character.getCurrentLevel() == 0 && character.getCampaign() != null
											   &&!character.getCampaign().isBuyStats());
		generateStatsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Character character = charactersFragment.getCurrentInstance();
				if(character.getCurrentLevel() == 0) {
					character.generateStats();
					charactersFragment.saveItem();
					for(Statistic statistic : Statistic.getAllStats()) {
						viewHolderMap.get(statistic).tempStatView.setText(String.valueOf(
								character.getStatTemps().get(statistic)));
						viewHolderMap.get(statistic).potentialStatView.setText(String.valueOf(
								character.getStatPotentials().get(statistic)));
					}
				}
			}
		});
	}

	private void initStatViews(View layout) {
		initStatButton(layout, R.id.agility_increment, R.id.agility_decrement,
					   R.id.increase_agility_button, Statistic.AGILITY);
		initStatButton(layout, R.id.constitution_increment, R.id.constitution_decrement,
					   R.id.increase_constitution_button, Statistic.CONSTITUTION);
		initStatButton(layout, R.id.empathy_increment, R.id.empathy_decrement,
					   R.id.increase_empathy_button, Statistic.EMPATHY);
		initStatButton(layout, R.id.intuition_increment, R.id.intuition_decrement,
					   R.id.increase_intuition_button, Statistic.INTUITION);
		initStatButton(layout, R.id.memory_increment, R.id.memory_decrement,
					   R.id.increase_memory_button, Statistic.MEMORY);
		initStatButton(layout, R.id.presence_increment, R.id.presence_decrement,
					   R.id.increase_presence_button, Statistic.PRESENCE);
		initStatButton(layout, R.id.quickness_increment, R.id.quickness_decrement,
					   R.id.increase_quickness_button, Statistic.QUICKNESS);
		initStatButton(layout, R.id.reasoning_increment, R.id.reasoning_decrement,
					   R.id.increase_reasoning_button, Statistic.REASONING);
		initStatButton(layout, R.id.self_discipline_increment, R.id.self_discipline_decrement,
					   R.id.increase_self_discipline_button, Statistic.SELF_DISCIPLINE);
		initStatButton(layout, R.id.strength_increment, R.id.strength_decrement,
					   R.id.increase_strength_button, Statistic.STRENGTH);
	}

	private void initStatButton(View layout, @IdRes int buyButtonId, @IdRes int sellButtonId, @IdRes int increaseTempButtonId,
								final Statistic statistic) {
		Character character = charactersFragment.getCurrentInstance();
		ImageButton buyButton = (ImageButton)layout.findViewById(buyButtonId);
		ImageButton sellButton = (ImageButton)layout.findViewById(sellButtonId);
		Button increaseTempButton = (Button)layout.findViewById(increaseTempButtonId);
		if((character.getExperiencePoints() == 0 &&
				character.getCampaign()!= null &&
				character.getCampaign().isBuyStats())) {
			buyButton.setVisibility(View.VISIBLE);
			sellButton.setVisibility(View.VISIBLE);
			increaseTempButton.setVisibility(View.GONE);
		}
		else {
			buyButton.setVisibility(View.GONE);
			sellButton.setVisibility(View.GONE);
			increaseTempButton.setVisibility(View.VISIBLE);
		}

		boolean enabled = character.getCurrentLevel() >= 1 && !character.isStatIncreased(statistic) &&
				(character.statsIncreasedCount() < 2 || character.getCurrentDevelopmentPoints() > 5);
		increaseTempButton.setEnabled(enabled);
		viewHolderMap.get(statistic).buyButton = buyButton;
		viewHolderMap.get(statistic).sellButton = sellButton;
		viewHolderMap.get(statistic).increaseTempButton = increaseTempButton;

		buyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				buyStat(statistic);
			}
		});
		sellButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sellStat(statistic);
			}
		});
		increaseTempButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				increaseTemp(statistic);
			}
		});
	}

	private void buyStat(Statistic statistic) {
		Character character = charactersFragment.getCurrentInstance();
		short statPurchasePoints = character.getStatPurchasePoints();

		if(statPurchasePoints > 0) {
			short currentBonus = Statistic.getBonus(character.getStatTemps().get(statistic));
			if(currentBonus < 15) {
				currentBonus += 1;
				short newTemp = Statistic.getRangeForBonus(currentBonus)[1];
				character.getStatTemps().put(statistic, newTemp);
				viewHolderMap.get(statistic).tempStatView.setText(String.valueOf(newTemp));
				statPurchasePoints -= 1;
				character.setStatPurchasePoints(statPurchasePoints);
				statPointsView.setText(String.valueOf(statPurchasePoints));
				charactersFragment.saveItem();
			}
		}
	}

	private void sellStat(Statistic statistic) {
		Character character = charactersFragment.getCurrentInstance();
		short statPurchasePoints = character.getStatPurchasePoints();

		short currentBonus = Statistic.getBonus(character.getStatTemps().get(statistic));
		if(currentBonus > -15) {
			currentBonus -= 1;
			short newTemp = Statistic.getRangeForBonus(currentBonus)[1];
			character.getStatTemps().put(statistic, newTemp);
			viewHolderMap.get(statistic).tempStatView.setText(String.valueOf(newTemp));
			statPurchasePoints += 1;
			character.setStatPurchasePoints(statPurchasePoints);
			statPointsView.setText(String.valueOf(statPurchasePoints));
			charactersFragment.saveItem();
		}
	}

	private void increaseTemp(Statistic statistic) {
		Character character = charactersFragment.getCurrentInstance();
		if(!character.isStatIncreased(statistic)) {
			if(character.statsIncreasedCount() < 2 || character.getCurrentDevelopmentPoints() >= 5) {
				short gain = statistic.statGain(character.getStatTemps().get(statistic));
				Boast.makeText(getActivity(),
							   String.format(getString(R.string.toast_stat_gain), statistic.getName(), gain),
							   Toast.LENGTH_LONG).show(true);
				if(character.statsIncreasedCount() >= 2) {
					character.setCurrentDevelopmentPoints((short)(character.getCurrentDevelopmentPoints() - 5));
				}
				character.addStatIncrease(statistic);
				short newTemp = (short)(character.getStatTemps().get(statistic) + gain);
				if(newTemp > character.getStatPotentials().get(statistic)) {
					newTemp = character.getStatPotentials().get(statistic);
				}
				character.getStatTemps().put(statistic, newTemp);
				charactersFragment.saveItem();
			}
		}
	}

	private void initStatsRows(View layout) {
		initStatRow(layout, R.id.agility_label, Statistic.AGILITY, R.id.agility_temp_view,
					R.id.agility_potential_view, R.id.agility_drag_group);
		initStatRow(layout, R.id.constitution_label, Statistic.CONSTITUTION, R.id.constitution_temp_view,
					R.id.constitution_potential_view, R.id.constitution_drag_group);
		initStatRow(layout, R.id.empathy_label, Statistic.EMPATHY, R.id.empathy_temp_view,
					R.id.empathy_potential_view, R.id.empathy_drag_group);
		initStatRow(layout, R.id.intuition_label, Statistic.INTUITION, R.id.intuition_temp_view,
					R.id.intuition_potential_view, R.id.intuition_drag_group);
		initStatRow(layout, R.id.memory_label, Statistic.MEMORY, R.id.memory_temp_view,
					R.id.memory_potential_view, R.id.memory_drag_group);
		initStatRow(layout, R.id.presence_label, Statistic.PRESENCE, R.id.presence_temp_view,
					R.id.presence_potential_view, R.id.presence_drag_group);
		initStatRow(layout, R.id.quickness_label, Statistic.QUICKNESS, R.id.quickness_temp_view,
					R.id.quickness_potential_view, R.id.quickness_drag_group);
		initStatRow(layout, R.id.reasoning_label, Statistic.REASONING, R.id.reasoning_temp_view,
					R.id.reasoning_potential_view, R.id.reasoning_drag_group);
		initStatRow(layout, R.id.self_discipline_label, Statistic.SELF_DISCIPLINE, R.id.self_discipline_temp_view,
					R.id.self_discipline_potential_view, R.id.self_discipline_drag_group);
		initStatRow(layout, R.id.strength_label, Statistic.STRENGTH, R.id.strength_temp_view,
					R.id.strength_potential_view, R.id.strength_drag_group);
	}

	private void initStatRow(View layout, @IdRes int labelViewId, final Statistic statistic, @IdRes int tempStatViewId,
							 @IdRes int potStatViewId, @IdRes int dragGroupId) {
		LinearLayout dragGroup;

		TextView textView = (TextView) layout.findViewById(labelViewId);
		textView.setText(statistic.toString());
		textView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				Boast.makeText(getActivity(), getString(statistic.getDescriptionId()), Toast.LENGTH_LONG).show(true);
				return true;
			}
		});
		EditText editText = (EditText) layout.findViewById(tempStatViewId);
		viewHolderMap.get(statistic).tempStatView = editText;
		editText = (EditText) layout.findViewById(potStatViewId);
		viewHolderMap.get(statistic).potentialStatView = editText;
		if(charactersFragment.getCurrentInstance().getExperiencePoints() == 0) {
			dragGroup = (LinearLayout) layout.findViewById(dragGroupId);
			dragGroup.setOnLongClickListener(new StatGroupLongClickListener(dragGroup, statistic,
																			tempStatViewId,
																			potStatViewId));
			dragGroup.setOnDragListener(new StatSwapDragListener(statistic));
		}
	}

	private void setStatPointsViews() {
		Character character = charactersFragment.getCurrentInstance();
		int visible = View.GONE;
		if(character != null && character.getCampaign() != null && character.getExperiencePoints() == 0
				&& character.getCampaign().isBuyStats()) {
			visible = View.VISIBLE;
			statPointsView.setText(String.valueOf(character.getStatPurchasePoints()));
		}
		statPointsLabel.setVisibility(visible);
		statPointsView.setVisibility(visible);
	}

	private class StatGroupLongClickListener implements View.OnLongClickListener {
		LinearLayout dragView;
		Statistic statistic;
		@IdRes int tempStatId;
		@IdRes int potentialStatId;

		/**
		 * Creates a new StatGroupLongClickListener instance.
		 *
		 * @param dragView  the LinearLayout being dragged
		 * @param statistic  the Statistic instance being dragged
		 * @param tempStatId  the resource ID of the EditText displaying the temp stat value being dragged
		 * @param potentialStatId  the resource ID of the EditText displaying the potential stat value being dragged
		 */
		StatGroupLongClickListener(LinearLayout dragView, Statistic statistic, int tempStatId, int potentialStatId) {
			this.dragView = dragView;
			this.statistic = statistic;
			this.tempStatId = tempStatId;
			this.potentialStatId = potentialStatId;
		}

		@Override
		public boolean onLongClick(View v) {
			ClipData dragData;
			List<View> dragViews = new ArrayList<>(1);
			dragViews.add(dragView);

			String statisticName = statistic.name();
			ClipData.Item statNameDataItem = new ClipData.Item(statisticName);
			dragData = new ClipData(DRAG_SWAP_STATS, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, statNameDataItem);

			String tempViewId = String.valueOf(tempStatId);
			ClipData.Item tempStatViewItem = new ClipData.Item(tempViewId);
			dragData.addItem(tempStatViewItem);

			String potentialViewId = String.valueOf(potentialStatId);
			ClipData.Item potentialStatViewItem = new ClipData.Item(potentialViewId);
			dragData.addItem(potentialStatViewItem);

			View.DragShadowBuilder myShadow = new RMUDragShadowBuilder(dragViews);

			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				v.startDragAndDrop(dragData, myShadow, null, 0);
			}
			else {
				//noinspection deprecation
				v.startDrag(dragData, myShadow, null, 0);
			}
			return false;
		}
	}

	class ViewHolder {
		EditText    tempStatView;
		EditText    potentialStatView;
		ImageButton buyButton;
		ImageButton sellButton;
		Button      increaseTempButton;
	}

	protected class StatSwapDragListener implements View.OnDragListener {
		private Statistic destStatistic;
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(),
																   R.drawable.drag_target_background, null);
		private Drawable hoverShape = ResourcesCompat.getDrawable(getActivity().getResources(),
																  R.drawable.drag_hover_background, null);
		private Drawable normalShape = ResourcesCompat.getDrawable(getActivity().getResources(),
																   R.drawable.drag_normal_background, null);

		StatSwapDragListener(Statistic destStatistic) {
			this.destStatistic = destStatistic;
		}

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch(action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription() != null && DRAG_SWAP_STATS.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
						break;
					}
					return false;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription() != null && DRAG_SWAP_STATS.equals(event.getClipDescription().getLabel())) {
						v.setBackground(hoverShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					if(event.getClipDescription() != null && DRAG_SWAP_STATS.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription() != null && DRAG_SWAP_STATS.equals(event.getClipDescription().getLabel())) {
						Character character = charactersFragment.getCurrentInstance();
						ClipData.Item item = event.getClipData().getItemAt(0);
						// We just send attack ID but since that is the only field used in the Attack.equals method we can
						// create a temporary attack and set its id field then use the new Attack to find the position of
						// the actual Attack instance in the adapter
						Statistic srcStatistic = Statistic.valueOf(item.getText().toString());
						short srcTempStatValue = character.getStatTemps().get(srcStatistic);
						short srcPotentialStatValue = character.getStatPotentials().get(srcStatistic);
						short destTempStatValue = character.getStatTemps().get(destStatistic);
						short destPotentialStatValue = character.getStatPotentials().get(destStatistic);
						if(srcTempStatValue != destTempStatValue || srcPotentialStatValue != destPotentialStatValue) {
							viewHolderMap.get(destStatistic).tempStatView.setText(String.valueOf(srcTempStatValue));
							character.getStatTemps().put(srcStatistic, destTempStatValue);
							viewHolderMap.get(destStatistic).potentialStatView.setText(String.valueOf(srcPotentialStatValue));
							character.getStatPotentials().put(srcStatistic, destPotentialStatValue);
							viewHolderMap.get(srcStatistic).tempStatView.setText(String.valueOf(destTempStatValue));
							character.getStatTemps().put(destStatistic, srcTempStatValue);
							viewHolderMap.get(srcStatistic).potentialStatView.setText(String.valueOf(destPotentialStatValue));
							character.getStatPotentials().put(destStatistic, srcPotentialStatValue);
							charactersFragment.saveItem();
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
	}
}
