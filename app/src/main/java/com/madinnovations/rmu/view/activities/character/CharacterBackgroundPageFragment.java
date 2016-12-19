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
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;
import com.madinnovations.rmu.view.utils.EditTextUtils;

/**
 * Handles interactions with the UI for character creation.
 */
public class CharacterBackgroundPageFragment extends Fragment implements EditTextUtils.ValuesCallback {
	@SuppressWarnings("unused")
	private static final String TAG = "CharacterBackgroundPage";
	private CharactersFragment charactersFragment;
	private EditText hairColorEdit;
	private EditText hairStyleEdit;
	private EditText eyeColorEdit;
	private EditText skinComplexionEdit;
	private EditText facialFeaturesEdit;
	private EditText identifyingMarksEdit;
	private EditText personalityEdit;
	private EditText mannerismsEdit;
	private EditText familyInformationEdit;
	private EditText hometownEdit;

	/**
	 * Creates a new CharacterBackgroundPageFragment instance.
	 *
	 * @param charactersFragment  the CharactersFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static CharacterBackgroundPageFragment newInstance(CharactersFragment charactersFragment) {
		CharacterBackgroundPageFragment fragment = new CharacterBackgroundPageFragment();
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

		View layout = inflater.inflate(R.layout.character_background_page, container, false);

		Character character = charactersFragment.getCurrentInstance();
		hairColorEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.hair_color_edit, 0);
		hairColorEdit.setText(character.getHairColor());
		hairStyleEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.hair_style_edit, 0);
		hairStyleEdit.setText(character.getHairStyle());
		eyeColorEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.eye_color_edit, 0);
		eyeColorEdit.setText(character.getEyeColor());
		skinComplexionEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.skin_complexion_edit, 0);
		skinComplexionEdit.setText(character.getSkinComplexion());
		facialFeaturesEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.facial_features_edit, 0);
		facialFeaturesEdit.setText(character.getFacialFeatures());
		identifyingMarksEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.identifying_marks_edit, 0);
		identifyingMarksEdit.setText(character.getIdentifyingMarks());
		personalityEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.personality_edit, 0);
		personalityEdit.setText(character.getPersonality());
		mannerismsEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.mannerisms_edit, 0);
		mannerismsEdit.setText(character.getMannerisms());
		familyInformationEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.family_info_edit, 0);
		familyInformationEdit.setText(character.getFamilyInfo());
		hometownEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.hometown_edit, 0);
		hometownEdit.setText(character.getHometown());
		return layout;
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		switch (editTextId) {
			case R.id.hair_color_edit:
				result = charactersFragment.getCurrentInstance().getHairColor();
				break;
			case R.id.hair_style_edit:
				result = charactersFragment.getCurrentInstance().getHairStyle();
				break;
			case R.id.eye_color_edit:
				result = charactersFragment.getCurrentInstance().getEyeColor();
				break;
			case R.id.skin_complexion_edit:
				result = charactersFragment.getCurrentInstance().getSkinComplexion();
				break;
			case R.id.facial_features_edit:
				result = charactersFragment.getCurrentInstance().getFacialFeatures();
				break;
			case R.id.identifying_marks_edit:
				result = charactersFragment.getCurrentInstance().getIdentifyingMarks();
				break;
			case R.id.personality_edit:
				result = charactersFragment.getCurrentInstance().getPersonality();
				break;
			case R.id.mannerisms_edit:
				result = charactersFragment.getCurrentInstance().getMannerisms();
				break;
			case R.id.family_info_edit:
				result = charactersFragment.getCurrentInstance().getFamilyInfo();
				break;
			case R.id.hometown_edit:
				result = charactersFragment.getCurrentInstance().getHometown();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		switch (editTextId) {
			case R.id.hair_color_edit:
				if(!newString.equals(charactersFragment.getCurrentInstance().getHairColor())) {
					charactersFragment.getCurrentInstance().setHairColor(newString);
					charactersFragment.saveItem();
				}
				break;
			case R.id.hair_style_edit:
				if(!newString.equals(charactersFragment.getCurrentInstance().getHairStyle())) {
					charactersFragment.getCurrentInstance().setHairStyle(newString);
					charactersFragment.saveItem();
				}
				break;
			case R.id.eye_color_edit:
				if(!newString.equals(charactersFragment.getCurrentInstance().getEyeColor())) {
					charactersFragment.getCurrentInstance().setEyeColor(newString);
					charactersFragment.saveItem();
				}
				break;
			case R.id.skin_complexion_edit:
				if(!newString.equals(charactersFragment.getCurrentInstance().getSkinComplexion())) {
					charactersFragment.getCurrentInstance().setSkinComplexion(newString);
					charactersFragment.saveItem();
				}
				break;
			case R.id.facial_features_edit:
				if(!newString.equals(charactersFragment.getCurrentInstance().getFacialFeatures())) {
					charactersFragment.getCurrentInstance().setFacialFeatures(newString);
					charactersFragment.saveItem();
				}
				break;
			case R.id.identifying_marks_edit:
				if(!newString.equals(charactersFragment.getCurrentInstance().getIdentifyingMarks())) {
					charactersFragment.getCurrentInstance().setIdentifyingMarks(newString);
					charactersFragment.saveItem();
				}
				break;
			case R.id.personality_edit:
				if(!newString.equals(charactersFragment.getCurrentInstance().getPersonality())) {
					charactersFragment.getCurrentInstance().setPersonality(newString);
					charactersFragment.saveItem();
				}
				break;
			case R.id.mannerisms_edit:
				if(!newString.equals(charactersFragment.getCurrentInstance().getMannerisms())) {
					charactersFragment.getCurrentInstance().setMannerisms(newString);
					charactersFragment.saveItem();
				}
				break;
			case R.id.family_info_edit:
				if(!newString.equals(charactersFragment.getCurrentInstance().getFamilyInfo())) {
					charactersFragment.getCurrentInstance().setFamilyInfo(newString);
					charactersFragment.saveItem();
				}
				break;
			case R.id.hometown_edit:
				if(!newString.equals(charactersFragment.getCurrentInstance().getHometown())) {
					charactersFragment.getCurrentInstance().setHometown(newString);
					charactersFragment.saveItem();
				}
				break;
		}
	}

	@Override
	public boolean performLongClick(@IdRes int editTextId) {
		return false;
	}

	/**
	 * Copies the values in the views on this page to the current Character instance for the Character UI.
	 *
	 * @return  false if no values in the current Character instance were modified, otherwise true.
	 */
	@SuppressWarnings("ConstantConditions")
	public boolean copyViewsToItem() {
		boolean changed = false;
		String newString;

		newString = hairColorEdit.getText().toString();
		if(!newString.equals(charactersFragment.getCurrentInstance().getHairColor())) {
			charactersFragment.getCurrentInstance().setHairColor(newString);
			changed = true;
		}
		newString = hairStyleEdit.getText().toString();
		if(!newString.equals(charactersFragment.getCurrentInstance().getHairStyle())) {
			charactersFragment.getCurrentInstance().setHairStyle(newString);
			changed = true;
		}
		newString = eyeColorEdit.getText().toString();
		if(!newString.equals(charactersFragment.getCurrentInstance().getEyeColor())) {
			charactersFragment.getCurrentInstance().setEyeColor(newString);
			changed = true;
		}
		newString = skinComplexionEdit.getText().toString();
		if(!newString.equals(charactersFragment.getCurrentInstance().getSkinComplexion())) {
			charactersFragment.getCurrentInstance().setSkinComplexion(newString);
			changed = true;
		}
		newString = facialFeaturesEdit.getText().toString();
		if(!newString.equals(charactersFragment.getCurrentInstance().getFacialFeatures())) {
			charactersFragment.getCurrentInstance().setFacialFeatures(newString);
			changed = true;
		}
		newString = identifyingMarksEdit.getText().toString();
		if(!newString.equals(charactersFragment.getCurrentInstance().getIdentifyingMarks())) {
			charactersFragment.getCurrentInstance().setIdentifyingMarks(newString);
			changed = true;
		}
		newString = personalityEdit.getText().toString();
		if(!newString.equals(charactersFragment.getCurrentInstance().getPersonality())) {
			charactersFragment.getCurrentInstance().setPersonality(newString);
			changed = true;
		}
		newString = mannerismsEdit.getText().toString();
		if(!newString.equals(charactersFragment.getCurrentInstance().getMannerisms())) {
			charactersFragment.getCurrentInstance().setMannerisms(newString);
			changed = true;
		}
		newString = familyInformationEdit.getText().toString();
		if(!newString.equals(charactersFragment.getCurrentInstance().getFamilyInfo())) {
			charactersFragment.getCurrentInstance().setFamilyInfo(newString);
			changed = true;
		}
		newString = hometownEdit.getText().toString();
		if(!newString.equals(charactersFragment.getCurrentInstance().getHometown())) {
			charactersFragment.getCurrentInstance().setHometown(newString);
			changed = true;
		}

		return changed;
	}

	/**
	 * Copies the current Character instance values to the views on this page.
	 */
	public void copyItemToViews() {
		hairColorEdit.setText(charactersFragment.getCurrentInstance().getHairColor());
		hairStyleEdit.setText(charactersFragment.getCurrentInstance().getHairStyle());
		eyeColorEdit.setText(charactersFragment.getCurrentInstance().getEyeColor());
		skinComplexionEdit.setText(charactersFragment.getCurrentInstance().getSkinComplexion());
		facialFeaturesEdit.setText(charactersFragment.getCurrentInstance().getFacialFeatures());
		identifyingMarksEdit.setText(charactersFragment.getCurrentInstance().getIdentifyingMarks());
		personalityEdit.setText(charactersFragment.getCurrentInstance().getPersonality());
		mannerismsEdit.setText(charactersFragment.getCurrentInstance().getMannerisms());
		familyInformationEdit.setText(charactersFragment.getCurrentInstance().getFamilyInfo());
		hometownEdit.setText(charactersFragment.getCurrentInstance().getHometown());
	}
}
