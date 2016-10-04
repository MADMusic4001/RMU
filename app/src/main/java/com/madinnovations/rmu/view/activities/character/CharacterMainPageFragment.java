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
import android.widget.EditText;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.character.CultureRxHandler;
import com.madinnovations.rmu.controller.rxhandler.character.ProfessionRxHandler;
import com.madinnovations.rmu.controller.rxhandler.character.RaceRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.RealmRxHandler;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;
import com.madinnovations.rmu.view.utils.EditTextUtils;
import com.madinnovations.rmu.view.utils.SpinnerUtils;

import javax.inject.Inject;

/**
 * Handles interactions with the UI for character creation.
 */
public class CharacterMainPageFragment extends Fragment implements EditTextUtils.ValuesCallback, SpinnerUtils.ValuesCallback {
	@Inject
	protected CultureRxHandler       cultureRxHandler;
	@Inject
	protected ProfessionRxHandler    professionRxHandler;
	@Inject
	protected RaceRxHandler          raceRxHandler;
	@Inject
	protected RealmRxHandler         realmRxHandler;
	private CharactersFragment       charactersFragment;
	private EditText                 currentLevelEdit;
	private EditText                 firstNameEdit;
	private EditText                 lastNameEdit;
	private EditText                 knownAsEdit;
	private EditText                 descriptionEdit;
	private SpinnerUtils<Race>       raceSpinner;
	private SpinnerUtils<Culture>    cultureSpinner;
	private SpinnerUtils<Profession> professionSpinner;
	private SpinnerUtils<Realm>      realmSpinner;
	private EditText                 heightEdit;
	private EditText                 weightEdit;
	private EditText                 currentHitsEdit;
	private EditText                 maxHitsEdit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.character_main_page_fragment, container, false);

		currentLevelEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.current_level_edit,
				R.string.validation_character_current_level_required);
		firstNameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.first_name_edit,
				R.string.validation_character_first_name_required);
		lastNameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.last_name_edit,
				R.string.validation_character_last_name_required);
		knownAsEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.known_as_edit,
				R.string.validation_character_known_as_required);
		descriptionEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.description_edit,
				R.string.validation_character_description_required);
		raceSpinner = new SpinnerUtils<>();
		raceSpinner.initSpinner(layout, getActivity(), raceRxHandler.getAll(), this, R.id.race_spinner, null);
		cultureSpinner = new SpinnerUtils<>();
		cultureSpinner.initSpinner(layout, getActivity(), cultureRxHandler.getAll(), this, R.id.culture_spinner, null);
		professionSpinner= new SpinnerUtils<>();
		professionSpinner.initSpinner(layout, getActivity(), professionRxHandler.getAll(), this, R.id.profession_spinner, null);
		realmSpinner = new SpinnerUtils<>();
		realmSpinner.initSpinner(layout, getActivity(), realmRxHandler.getAll(), this, R.id.realm_spinner, null);
		heightEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.height_edit, R.string.validation_character_height_required);
		weightEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.weight_edit, R.string.validation_character_weight_required);
		currentHitsEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.current_hits_edit,
				R.string.validation_character_current_hits_required);
		maxHitsEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.max_hits_edit,
				R.string.validation_character_max_hits_required);

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

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		switch (editTextId) {
			case R.id.current_level_edit:
				result = String.valueOf(charactersFragment.getCurrentInstance().getCurrentLevel());
				break;
			case R.id.first_name_edit:
				result = charactersFragment.getCurrentInstance().getFirstName();
				break;
			case R.id.last_name_edit:
				result = charactersFragment.getCurrentInstance().getLastName();
				break;
			case R.id.known_as_edit:
				result = charactersFragment.getCurrentInstance().getKnownAs();
				break;
			case R.id.description_edit:
				result = charactersFragment.getCurrentInstance().getDescription();
				break;
			case R.id.height_edit:
				result = String.valueOf(charactersFragment.getCurrentInstance().getHeight());
				break;
			case R.id.weight_edit:
				result = String.valueOf(charactersFragment.getCurrentInstance().getWeight());
				break;
			case R.id.current_hits_edit:
				result = String.valueOf(charactersFragment.getCurrentInstance().getCurrentHits());
				break;
			case R.id.max_hits_edit:
				result = String.valueOf(charactersFragment.getCurrentInstance().getMaxHits());
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		short newShort;

		switch (editTextId) {
			case R.id.current_level_edit:
				newShort = Short.valueOf(newString);
				if(newShort != charactersFragment.getCurrentInstance().getCurrentLevel()) {
					charactersFragment.getCurrentInstance().setCurrentLevel(newShort);
					charactersFragment.saveItem();
				}
				break;
			case R.id.first_name_edit:
				if(!newString.equals(charactersFragment.getCurrentInstance().getFirstName())) {
					charactersFragment.getCurrentInstance().setFirstName(newString);
					charactersFragment.saveItem();
				}
				break;
			case R.id.last_name_edit:
				if(!newString.equals(charactersFragment.getCurrentInstance().getLastName())) {
					charactersFragment.getCurrentInstance().setLastName(newString);
					charactersFragment.saveItem();
				}
				break;
			case R.id.known_as_edit:
				if(!newString.equals(charactersFragment.getCurrentInstance().getKnownAs())) {
					charactersFragment.getCurrentInstance().setKnownAs(newString);
					charactersFragment.saveItem();
				}
				break;
			case R.id.description_edit:
				if(!newString.equals(charactersFragment.getCurrentInstance().getDescription())) {
					charactersFragment.getCurrentInstance().setDescription(newString);
					charactersFragment.saveItem();
				}
				break;
			case R.id.height_edit:
				newShort = Short.valueOf(newString);
				if(newShort != charactersFragment.getCurrentInstance().getHeight()) {
					charactersFragment.getCurrentInstance().setHeight(newShort);
					charactersFragment.saveItem();
				}
				break;
			case R.id.weight_edit:
				newShort = Short.valueOf(newString);
				if(newShort != charactersFragment.getCurrentInstance().getWeight()) {
					charactersFragment.getCurrentInstance().setWeight(newShort);
					charactersFragment.saveItem();
				}
				break;
			case R.id.current_hits_edit:
				newShort = Short.valueOf(newString);
				if(newShort != charactersFragment.getCurrentInstance().getCurrentHits()) {
					charactersFragment.getCurrentInstance().setCurrentHits(newShort);
					charactersFragment.saveItem();
				}
				break;
			case R.id.max_hits_edit:
				newShort = Short.valueOf(newString);
				if(newShort != charactersFragment.getCurrentInstance().getMaxHits()) {
					charactersFragment.getCurrentInstance().setMaxHits(newShort);
					charactersFragment.saveItem();
				}
				break;
		}
	}

	@Override
	public Object getValueForSpinner(@IdRes int spinnerId) {
		Object result = null;

		switch (spinnerId) {
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
		switch (spinnerId) {
			case R.id.race_spinner:
				charactersFragment.getCurrentInstance().setRace((Race)newItem);
				charactersFragment.saveItem();
				break;
			case R.id.culture_spinner:
				charactersFragment.getCurrentInstance().setCulture((Culture)newItem);
				charactersFragment.saveItem();
				break;
			case R.id.profession_spinner:
				charactersFragment.getCurrentInstance().setProfession((Profession) newItem);
				charactersFragment.saveItem();
				break;
			case R.id.realm_spinner:
				charactersFragment.getCurrentInstance().setRealm((Realm)newItem);
				charactersFragment.saveItem();
				break;
		}
	}

	@SuppressWarnings("ConstantConditions")
	public boolean copyViewsToItem() {
		boolean changed = false;
		String newString;
		short newShort;

		if(currentLevelEdit.getText().length() > 0) {
			newShort = Short.valueOf(currentLevelEdit.getText().toString());
			if(newShort != charactersFragment.getCurrentInstance().getCurrentLevel()) {
				charactersFragment.getCurrentInstance().setCurrentLevel(newShort);
				changed = true;
			}
		}
		newString = firstNameEdit.getText().toString();
		if(!newString.equals(charactersFragment.getCurrentInstance().getFirstName())) {
			charactersFragment.getCurrentInstance().setFirstName(newString);
			changed = true;
		}
		newString = lastNameEdit.getText().toString();
		if(!newString.equals(charactersFragment.getCurrentInstance().getLastName())) {
			charactersFragment.getCurrentInstance().setLastName(newString);
			changed = true;
		}
		newString = knownAsEdit.getText().toString();
		if(!newString.equals(charactersFragment.getCurrentInstance().getKnownAs())) {
			charactersFragment.getCurrentInstance().setKnownAs(newString);
			changed = true;
		}
		newString = descriptionEdit.getText().toString();
		if(!newString.equals(charactersFragment.getCurrentInstance().getDescription())) {
			charactersFragment.getCurrentInstance().setDescription(newString);
			changed = true;
		}
		Race newRace = raceSpinner.getSelectedItem();
		Race oldRace = charactersFragment.getCurrentInstance().getRace();
		if((newRace != null && !newRace.equals(oldRace)) || (oldRace != null && !oldRace.equals(newRace))) {
			charactersFragment.getCurrentInstance().setRace(newRace);
			changed = true;
		}
		Culture newCulture = cultureSpinner.getSelectedItem();
		Culture oldCulture = charactersFragment.getCurrentInstance().getCulture();
		if((newCulture != null && !newCulture.equals(oldCulture)) || (oldCulture != null && !oldCulture.equals(newCulture))) {
			charactersFragment.getCurrentInstance().setCulture(newCulture);
			changed = true;
		}
		Profession newProfession = professionSpinner.getSelectedItem();
		Profession oldProfession = charactersFragment.getCurrentInstance().getProfession();
		if((newProfession != null && !newProfession.equals(oldProfession)) ||
				(oldProfession != null && !oldProfession.equals(newProfession))) {
			charactersFragment.getCurrentInstance().setProfession(newProfession);
			changed = true;
		}
		Realm newRealm = realmSpinner.getSelectedItem();
		Realm oldRealm = charactersFragment.getCurrentInstance().getRealm();
		if((newRealm != null && !newRealm.equals(oldRealm)) || (oldRealm != null && !oldRealm.equals(newRealm))) {
			charactersFragment.getCurrentInstance().setRealm(newRealm);
			changed = true;
		}
		if(currentLevelEdit.getText().length() > 0) {
			newShort = Short.valueOf(currentLevelEdit.getText().toString());
			if(newShort != charactersFragment.getCurrentInstance().getHeight()) {
				charactersFragment.getCurrentInstance().setHeight(newShort);
				changed = true;
			}
		}
		if(currentLevelEdit.getText().length() > 0) {
			newShort = Short.valueOf(currentLevelEdit.getText().toString());
			if(newShort != charactersFragment.getCurrentInstance().getWeight()) {
				charactersFragment.getCurrentInstance().setWeight(newShort);
				changed = true;
			}
		}
		if(currentLevelEdit.getText().length() > 0) {
			newShort = Short.valueOf(currentLevelEdit.getText().toString());
			if(newShort != charactersFragment.getCurrentInstance().getCurrentHits()) {
				charactersFragment.getCurrentInstance().setCurrentHits(newShort);
				changed = true;
			}
		}
		if(currentLevelEdit.getText().length() > 0) {
			newShort = Short.valueOf(currentLevelEdit.getText().toString());
			if(newShort != charactersFragment.getCurrentInstance().getMaxHits()) {
				charactersFragment.getCurrentInstance().setMaxHits(newShort);
				changed = true;
			}
		}

		return changed;
	}

	public void copyItemToViews() {
		currentLevelEdit.setText(String.valueOf(charactersFragment.getCurrentInstance().getCurrentLevel()));
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
		currentHitsEdit.setText(String.valueOf(charactersFragment.getCurrentInstance().getCurrentHits()));
		maxHitsEdit.setText(String.valueOf(charactersFragment.getCurrentInstance().getMaxHits()));
	}

	public void setCharactersFragment(CharactersFragment charactersFragment) {
		this.charactersFragment = charactersFragment;
	}
}
