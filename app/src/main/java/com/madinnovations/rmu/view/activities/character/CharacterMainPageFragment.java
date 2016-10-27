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
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.campaign.CampaignRxHandler;
import com.madinnovations.rmu.controller.rxhandler.character.CultureRxHandler;
import com.madinnovations.rmu.controller.rxhandler.character.ProfessionRxHandler;
import com.madinnovations.rmu.controller.rxhandler.character.RaceRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.RealmRxHandler;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;
import com.madinnovations.rmu.view.utils.EditTextUtils;
import com.madinnovations.rmu.view.utils.SpinnerUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Handles interactions with the UI for character creation.
 */
public class CharacterMainPageFragment extends Fragment implements EditTextUtils.ValuesCallback, SpinnerUtils.ValuesCallback {
	@SuppressWarnings("unused")
	private static final String LOG_TAG = "CharacterMainPageFrag";
	@Inject
	protected CampaignRxHandler        campaignRxHandler;
	@Inject
	protected CultureRxHandler         cultureRxHandler;
	@Inject
	protected ProfessionRxHandler      professionRxHandler;
	@Inject
	protected RaceRxHandler            raceRxHandler;
	@Inject
	protected RealmRxHandler           realmRxHandler;
	private   CharactersFragment       charactersFragment;
	private   TextView                 currentLevelView;
	private   TextView                 experiencePointsView;
	private   TextView                 developmentPointsView;
	private   Button                   levelUpButton;
	private   Spinner                  campaignSpinner;
	private   EditText                 firstNameEdit;
	private   EditText                 lastNameEdit;
	private   EditText                 knownAsEdit;
	private   EditText                 descriptionEdit;
	private   SpinnerUtils<Race>       raceSpinner;
	private   SpinnerUtils<Culture>    cultureSpinner;
	private   SpinnerUtils<Profession> professionSpinner;
	private   SpinnerUtils<Realm>      realmSpinner;
	private   Button                   generateStatsButton;
	private   EditText                 heightEdit;
	private   EditText                 weightEdit;
	private   LinearLayout             newCharacterRow;
	private Map<Statistic, ViewHolder> viewHolderMap = new HashMap<>(Statistic.NUM_STATS);

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

		View layout = inflater.inflate(R.layout.character_main_page_fragment, container, false);

		currentLevelView = (TextView)layout.findViewById(R.id.current_level_view);
		experiencePointsView = (TextView)layout.findViewById(R.id.experience_points_view);
		developmentPointsView = (TextView)layout.findViewById(R.id.development_points_view);
		initLevelUpButton(layout);
		campaignSpinner = new SpinnerUtils<Campaign>().initSpinner(layout, getActivity(), campaignRxHandler.getAll(), this,
				R.id.campaign_spinner, null);
		campaignSpinner.setEnabled(charactersFragment.getCurrentInstance().getCurrentLevel() == 0);
		firstNameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.first_name_edit,
				R.string.validation_character_first_name_required);
		lastNameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.last_name_edit,
				R.string.validation_character_last_name_required);
		knownAsEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.known_as_edit,
				R.string.validation_character_known_as_required);
		descriptionEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.notes_edit,
				R.string.validation_character_description_required);
		raceSpinner = new SpinnerUtils<>();
		raceSpinner.initSpinner(layout, getActivity(), raceRxHandler.getAll(), this, R.id.race_spinner, null);
		cultureSpinner = new SpinnerUtils<>();
		cultureSpinner.initSpinner(layout, getActivity(), cultureRxHandler.getAll(), this, R.id.culture_spinner, null);
		professionSpinner= new SpinnerUtils<>();
		professionSpinner.initSpinner(layout, getActivity(), professionRxHandler.getAll(), this, R.id.profession_spinner, null);
		realmSpinner = new SpinnerUtils<>();
		realmSpinner.initSpinner(layout, getActivity(), realmRxHandler.getAll(), this, R.id.realm_spinner, null);
		heightEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.height_edit,
											R.string.validation_character_height_required);
		weightEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.weight_edit,
											R.string.validation_character_weight_required);
		for(Statistic statistic : Statistic.getAllStats()) {
			viewHolderMap.put(statistic, new ViewHolder());
		}
		initGenerateStatsButton(layout);
		initPurchaseButtons(layout);
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
		}

		return result;
	}

	@Override
	public void setValueFromSpinner(@IdRes int spinnerId, Object newItem) {
		Character character = charactersFragment.getCurrentInstance();

		switch (spinnerId) {
			case R.id.campaign_spinner:
				Campaign oldCampaign = character.getCampaign();
				Campaign newCampaign = (Campaign)newItem;
				character.setCampaign(newCampaign);
				if(oldCampaign == null || oldCampaign.isBuyStats() != newCampaign.isBuyStats()) {
					character.generateStats();
					character.setStatPurchasePoints(character.getCampaign().getPowerLevel().getStatPoints());
					generateStatsButton.setEnabled(character.getCurrentLevel() == 0 && character.getCampaign() != null
														   &&!character.getCampaign().isBuyStats());
					boolean enable = character.getCurrentLevel() == 0 && character.getCampaign() != null &&
							character.getCampaign().isBuyStats();
					for(Statistic statistic : Statistic.getAllStats()) {
						viewHolderMap.get(statistic).sellButton.setEnabled(enable);
						viewHolderMap.get(statistic).buyButton.setEnabled(enable);
						viewHolderMap.get(statistic).tempStatView.setText(String.valueOf(
								character.getStatTemps().get(statistic)));
						viewHolderMap.get(statistic).potentialStatView.setText(String.valueOf(
								character.getStatPotentials().get(statistic)));
					}
				}
				charactersFragment.saveItem();
				break;
			case R.id.race_spinner:
				Race newRace = (Race)newItem;
				Race oldRace = character.getRace();
				short bonusDP = newRace.getBonusDevelopmentPoints();
				if(oldRace != null) {
					bonusDP -= oldRace.getBonusDevelopmentPoints();
				}
				if(character.getCurrentDevelopmentPoints() > bonusDP) {
					character.setCurrentDevelopmentPoints((short)(character.getCurrentDevelopmentPoints() - bonusDP));
				}
				else {
					character.setCurrentDevelopmentPoints((short)(Character.INITIAL_DP + newRace.getBonusDevelopmentPoints()));
					character.setCurrentLevelSkillRanks(new HashMap<Skill, Short>());
					character.setCurrentLevelSpecializationRanks(new HashMap<Specialization, Short>());
					character.setSkillRanks(new HashMap<Skill, Short>());
					character.setSpecializationRanks(new HashMap<Specialization, Short>());
					character.setTalentTiers(new HashMap<Talent, Short>());
				}
				character.setRace(newRace);
				charactersFragment.saveItem();
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
				character.setRealm((Realm)newItem);
				charactersFragment.saveItem();
				break;
		}
	}

	@SuppressWarnings("ConstantConditions")
	public boolean copyViewsToItem() {
		Character character = charactersFragment.getCurrentInstance();
		boolean changed = false;
		String newString;
		short newShort;

		Campaign newCampaign = (Campaign)campaignSpinner.getSelectedItem();
		if(newCampaign != null && !newCampaign.equals(character.getCampaign()) ||
				(newCampaign == null && character.getCampaign() != null)) {
			character.setCampaign(newCampaign);
			changed = true;
		}

		newString = firstNameEdit.getText().toString();
		if(!newString.equals(character.getFirstName())) {
			character.setFirstName(newString);
			changed = true;
		}
		newString = lastNameEdit.getText().toString();
		if(!newString.equals(character.getLastName())) {
			character.setLastName(newString);
			changed = true;
		}
		newString = knownAsEdit.getText().toString();
		if(!newString.equals(character.getKnownAs())) {
			character.setKnownAs(newString);
			changed = true;
		}
		newString = descriptionEdit.getText().toString();
		if(!newString.equals(character.getDescription())) {
			character.setDescription(newString);
			changed = true;
		}
		Race newRace = raceSpinner.getSelectedItem();
		Race oldRace = character.getRace();
		if((newRace != null && !newRace.equals(oldRace)) || (oldRace != null && !oldRace.equals(newRace))) {
			character.setRace(newRace);
			changed = true;
		}
		Culture newCulture = cultureSpinner.getSelectedItem();
		Culture oldCulture = character.getCulture();
		if((newCulture != null && !newCulture.equals(oldCulture)) || (oldCulture != null && !oldCulture.equals(newCulture))) {
			character.setCulture(newCulture);
			changed = true;
		}
		Profession newProfession = professionSpinner.getSelectedItem();
		Profession oldProfession = character.getProfession();
		if((newProfession != null && !newProfession.equals(oldProfession)) ||
				(oldProfession != null && !oldProfession.equals(newProfession))) {
			character.setProfession(newProfession);
			changed = true;
		}
		Realm newRealm = realmSpinner.getSelectedItem();
		Realm oldRealm = character.getRealm();
		if((newRealm != null && !newRealm.equals(oldRealm)) || (oldRealm != null && !oldRealm.equals(newRealm))) {
			character.setRealm(newRealm);
			changed = true;
		}
		if(heightEdit.getText().length() > 0) {
			newShort = Short.valueOf(heightEdit.getText().toString());
			if(newShort != character.getHeight()) {
				character.setHeight(newShort);
				changed = true;
			}
		}
		if(weightEdit.getText().length() > 0) {
			newShort = Short.valueOf(weightEdit.getText().toString());
			if(newShort != character.getWeight()) {
				character.setWeight(newShort);
				changed = true;
			}
		}

		return changed;
	}

	public void copyItemToViews() {
		Character character = charactersFragment.getCurrentInstance();

		short currentLevel = character.getCurrentLevel();
		currentLevelView.setText(String.valueOf(currentLevel));
		if(currentLevel > 0) {
			newCharacterRow.setVisibility(View.GONE);
		}
		else {
			newCharacterRow.setVisibility(View.VISIBLE);
		}
		experiencePointsView.setText(String.valueOf(character.getExperiencePoints()));
		developmentPointsView.setText(String.valueOf(character.getCurrentDevelopmentPoints()));
		levelUpButton.setEnabled((currentLevel < (short)(character.getExperiencePoints()/10000)));

		firstNameEdit.setText(charactersFragment.getCurrentInstance().getFirstName());
		lastNameEdit.setText(charactersFragment.getCurrentInstance().getLastName());
		knownAsEdit.setText(charactersFragment.getCurrentInstance().getKnownAs());
		descriptionEdit.setText(charactersFragment.getCurrentInstance().getDescription());
		raceSpinner.setSelection(charactersFragment.getCurrentInstance().getRace());
		cultureSpinner.setSelection(charactersFragment.getCurrentInstance().getCulture());
		professionSpinner.setSelection(charactersFragment.getCurrentInstance().getProfession());
		realmSpinner.setSelection(charactersFragment.getCurrentInstance().getRealm());
		heightEdit.setText(String.valueOf(charactersFragment.getCurrentInstance().getHeight()));
		weightEdit.setText(String.valueOf(charactersFragment.getCurrentInstance().getWeight()));
		campaignSpinner.setEnabled(character.getCurrentLevel() == 0);

		boolean enable = character.getCurrentLevel() == 0 && character.getCampaign() != null && character.getCampaign()
				.isBuyStats();
		for(Map.Entry<Statistic, ViewHolder> entry : viewHolderMap.entrySet()) {
			entry.getValue().tempStatView.setText(String.valueOf(character.getStatTemps().get(entry.getKey())));
			entry.getValue().potentialStatView.setText(String.valueOf(character.getStatPotentials().get(entry.getKey())));
			entry.getValue().buyButton.setEnabled(enable);
			entry.getValue().sellButton.setEnabled(enable);
		}

		generateStatsButton.setEnabled(character.getCurrentLevel() == 0 && character.getCampaign() != null
				&&!character.getCampaign().isBuyStats());
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
				}
			}
		});
	}

	private void initGenerateStatsButton(View layout) {
		newCharacterRow = (LinearLayout)layout.findViewById(R.id.new_character_row);
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

	private void initPurchaseButtons(View layout) {
		initPurchaseButtons(layout, R.id.agility_increment, R.id.agility_decrement, Statistic.AGILITY);
		initPurchaseButtons(layout, R.id.constitution_increment, R.id.constitution_decrement, Statistic.CONSTITUTION);
		initPurchaseButtons(layout, R.id.empathy_increment, R.id.empathy_decrement, Statistic.EMPATHY);
		initPurchaseButtons(layout, R.id.intuition_increment, R.id.intuition_decrement, Statistic.INTUITION);
		initPurchaseButtons(layout, R.id.memory_increment, R.id.memory_decrement, Statistic.MEMORY);
		initPurchaseButtons(layout, R.id.presence_increment, R.id.presence_decrement, Statistic.PRESENCE);
		initPurchaseButtons(layout, R.id.quickness_increment, R.id.quickness_decrement, Statistic.QUICKNESS);
		initPurchaseButtons(layout, R.id.reasoning_increment, R.id.reasoning_decrement, Statistic.REASONING);
		initPurchaseButtons(layout, R.id.self_discipline_increment, R.id.self_discipline_decrement, Statistic.SELF_DISCIPLINE);
		initPurchaseButtons(layout, R.id.strength_increment, R.id.strength_decrement, Statistic.STRENGTH);
	}

	private void initPurchaseButtons(View layout, @IdRes int buyButtonId, @IdRes int sellButtonId, final Statistic statistic) {
		ImageButton buyButton = (ImageButton)layout.findViewById(buyButtonId);
		ImageButton sellButton = (ImageButton)layout.findViewById(sellButtonId);
		boolean enable = (charactersFragment.getCurrentInstance().getCurrentLevel() == 0 &&
				charactersFragment.getCurrentInstance().getCampaign()!= null &&
				charactersFragment.getCurrentInstance().getCampaign().isBuyStats());
		buyButton.setEnabled(enable);
		sellButton.setEnabled(enable);
		viewHolderMap.get(statistic).buyButton = buyButton;
		viewHolderMap.get(statistic).sellButton = sellButton;

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
		}
	}

	private void initStatsRows(View layout) {
		TextView textView = (TextView) layout.findViewById(R.id.agility_label);
		textView.setText(Statistic.AGILITY.toString());
		textView = (TextView) layout.findViewById(R.id.agility_temp_view);
		viewHolderMap.get(Statistic.AGILITY).tempStatView = textView;
		textView = (TextView) layout.findViewById(R.id.agility_potential_view);
		viewHolderMap.get(Statistic.AGILITY).potentialStatView = textView;

		textView = (TextView) layout.findViewById(R.id.constitution_label);
		textView.setText(Statistic.CONSTITUTION.toString());
		textView = (TextView) layout.findViewById(R.id.constitution_temp_view);
		viewHolderMap.get(Statistic.CONSTITUTION).tempStatView = textView;
		textView = (TextView) layout.findViewById(R.id.constitution_potential_view);
		viewHolderMap.get(Statistic.CONSTITUTION).potentialStatView = textView;

		textView = (TextView) layout.findViewById(R.id.empathy_label);
		textView.setText(Statistic.EMPATHY.toString());
		textView = (TextView) layout.findViewById(R.id.empathy_temp_view);
		viewHolderMap.get(Statistic.EMPATHY).tempStatView = textView;
		textView = (TextView) layout.findViewById(R.id.empathy_potential_view);
		viewHolderMap.get(Statistic.EMPATHY).potentialStatView = textView;

		textView = (TextView) layout.findViewById(R.id.intuition_label);
		textView.setText(Statistic.INTUITION.toString());
		textView = (TextView) layout.findViewById(R.id.intuition_temp_view);
		viewHolderMap.get(Statistic.INTUITION).tempStatView = textView;
		textView = (TextView) layout.findViewById(R.id.intuition_potential_view);
		viewHolderMap.get(Statistic.INTUITION).potentialStatView = textView;

		textView = (TextView) layout.findViewById(R.id.memory_label);
		textView.setText(Statistic.MEMORY.toString());
		textView = (TextView) layout.findViewById(R.id.memory_temp_view);
		viewHolderMap.get(Statistic.MEMORY).tempStatView = textView;
		textView = (TextView) layout.findViewById(R.id.memory_potential_view);
		viewHolderMap.get(Statistic.MEMORY).potentialStatView = textView;

		textView = (TextView) layout.findViewById(R.id.presence_label);
		textView.setText(Statistic.PRESENCE.toString());
		textView = (TextView) layout.findViewById(R.id.presence_temp_view);
		viewHolderMap.get(Statistic.PRESENCE).tempStatView = textView;
		textView = (TextView) layout.findViewById(R.id.presence_potential_view);
		viewHolderMap.get(Statistic.PRESENCE).potentialStatView = textView;

		textView = (TextView) layout.findViewById(R.id.quickness_label);
		textView.setText(Statistic.QUICKNESS.toString());
		textView = (TextView) layout.findViewById(R.id.quickness_temp_view);
		viewHolderMap.get(Statistic.QUICKNESS).tempStatView = textView;
		textView = (TextView) layout.findViewById(R.id.quickness_potential_view);
		viewHolderMap.get(Statistic.QUICKNESS).potentialStatView = textView;

		textView = (TextView) layout.findViewById(R.id.reasoning_label);
		textView.setText(Statistic.REASONING.toString());
		textView = (TextView) layout.findViewById(R.id.reasoning_temp_view);
		viewHolderMap.get(Statistic.REASONING).tempStatView = textView;
		textView = (TextView) layout.findViewById(R.id.reasoning_potential_view);
		viewHolderMap.get(Statistic.REASONING).potentialStatView = textView;

		textView = (TextView) layout.findViewById(R.id.self_discipline_label);
		textView.setText(Statistic.SELF_DISCIPLINE.toString());
		textView = (TextView) layout.findViewById(R.id.self_discipline_temp_view);
		viewHolderMap.get(Statistic.SELF_DISCIPLINE).tempStatView = textView;
		textView = (TextView) layout.findViewById(R.id.self_discipline_potential_view);
		viewHolderMap.get(Statistic.SELF_DISCIPLINE).potentialStatView = textView;

		textView = (TextView) layout.findViewById(R.id.strength_label);
		textView.setText(Statistic.STRENGTH.toString());
		textView = (TextView) layout.findViewById(R.id.strength_temp_view);
		viewHolderMap.get(Statistic.STRENGTH).tempStatView = textView;
		textView = (TextView) layout.findViewById(R.id.strength_potential_view);
		viewHolderMap.get(Statistic.STRENGTH).potentialStatView = textView;
	}

	class ViewHolder {
		TextView tempStatView;
		TextView potentialStatView;
		ImageButton buyButton;
		ImageButton sellButton;
	}
}
