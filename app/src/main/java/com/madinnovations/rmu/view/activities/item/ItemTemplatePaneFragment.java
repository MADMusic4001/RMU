/*
  Copyright (C) 2017 MadInnovations
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.view.activities.item;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.common.ManeuverDifficulty;
import com.madinnovations.rmu.data.entities.item.Cost;
import com.madinnovations.rmu.data.entities.item.ItemTemplate;
import com.madinnovations.rmu.data.entities.item.MoneyUnit;
import com.madinnovations.rmu.data.entities.item.Slot;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.ItemFragmentModule;
import com.madinnovations.rmu.view.utils.SpinnerUtils;
import com.madinnovations.rmu.view.utils.TextInputLayoutUtils;

import java.util.Arrays;

/**
 * Handles interactions with the UI for item templates.
 */
public class ItemTemplatePaneFragment extends Fragment implements SpinnerUtils.ValuesCallback,
		TextInputLayoutUtils.ValuesCallback {
	@SuppressWarnings("unused")
	private static final String TAG = "ItemTemplatePaneFrag";
	private   EditText                         nameEdit;
	private   EditText                         notesEdit;
	private   EditText                         weightEdit;
	private   EditText                         strengthEdit;
	private   EditText                         baseCostEdit;
	private   SpinnerUtils<MoneyUnit>          monetaryUnitSpinnerUtil;
	private   EditText                         constructionTimeEdit;
	private   SpinnerUtils<ManeuverDifficulty> maneuverDifficultySpinnerUtil;
	private   SpinnerUtils<Slot>               primarySlotSpinnerUtil;
	private   SpinnerUtils<Slot>               secondarySlotSpinnerUtil;
	private   DataAccessInterface              dataAccessInterface;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newItemFragmentComponent(new ItemFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.item_templates_pane, container, false);

		nameEdit = TextInputLayoutUtils.initEdit(layout, getActivity(), this, R.id.name_textInputLayout,
												 R.id.name_edit, R.string.validation_item_name_required);
		notesEdit = TextInputLayoutUtils.initEdit(layout, getActivity(), this, R.id.notes_textInputLayout,
												  R.id.notes_edit, 0);
		weightEdit = TextInputLayoutUtils.initEdit(layout, getActivity(), this, R.id.weight_textInputLayout,
												   R.id.weight_edit, R.string.validation_item_weight_required);
		baseCostEdit = TextInputLayoutUtils.initEdit(layout, getActivity(), this, R.id.base_cost_textInputLayout,
													 R.id.base_cost_edit, R.string.validation_item_base_cost_required);
		strengthEdit = TextInputLayoutUtils.initEdit(layout, getActivity(), this, R.id.strength_textInputLayout,
													 R.id.strength_edit, R.string.validation_item_strength_required);
		constructionTimeEdit = TextInputLayoutUtils.initEdit(layout, getActivity(), this, R.id.construction_time_textInputLayout,
															 R.id.construction_time_edit,
															 R.string.validation_item_construction_time_required);

		monetaryUnitSpinnerUtil = new SpinnerUtils<>();
		monetaryUnitSpinnerUtil.initSpinner(layout, getActivity(), Arrays.asList(MoneyUnit.values()), this,
											R.id.monetary_unit_spinner, null);
		maneuverDifficultySpinnerUtil = new SpinnerUtils<>();
		maneuverDifficultySpinnerUtil.initSpinner(layout, getActivity(), Arrays.asList(ManeuverDifficulty.values()), this,
										   R.id.maneuver_difficulty_spinner, null);
		primarySlotSpinnerUtil = new SpinnerUtils<>();
		primarySlotSpinnerUtil.initSpinner(layout, getActivity(), Arrays.asList(Slot.getAnyOrAll()), this,
										   R.id.primary_slot_spinner, null);
		secondarySlotSpinnerUtil = new SpinnerUtils<>();
		secondarySlotSpinnerUtil.initSpinner(layout, getActivity(), Arrays.asList(Slot.getAnyOrNone()), this,
										   R.id.secondary_slot_spinner, null);

		return layout;
	}

	@Override
	public Object getValueForSpinner(@IdRes int spinnerId) {
		Object result = null;

		switch (spinnerId) {
			case R.id.monetary_unit_spinner:
				if(dataAccessInterface.getItemTemplate() != null && dataAccessInterface.getItemTemplate().getBaseCost() != null) {
					result = dataAccessInterface.getItemTemplate().getBaseCost().getUnit();
				}
				else {
					result = MoneyUnit.BRONZE_COIN;
				}
				break;
			case R.id.maneuver_difficulty_spinner:
				if(dataAccessInterface.getItemTemplate() != null) {
					result = dataAccessInterface.getItemTemplate().getManeuverDifficulty();
				}
				break;
			case R.id.primary_slot_spinner:
				if(dataAccessInterface.getItemTemplate() != null) {
					result = dataAccessInterface.getItemTemplate().getPrimarySlot();
				}
				if (result == null) {
					result = Slot.ANY;
				}
				break;
			case R.id.secondary_slot_spinner:
				if(dataAccessInterface.getItemTemplate() != null) {
					result = dataAccessInterface.getItemTemplate().getSecondarySlot();
				}
				if(result == null) {
					result = Slot.NONE;
				}
				break;
		}

		return result;
	}

	@Override
	public void setValueFromSpinner(@IdRes int spinnerId, Object newItem) {
		switch (spinnerId) {
			case R.id.monetary_unit_spinner:
				if(dataAccessInterface.getItemTemplate().getBaseCost() != null) {
					dataAccessInterface.getItemTemplate().getBaseCost().setUnit((MoneyUnit) newItem);
				}
				else {
					dataAccessInterface.getItemTemplate().setBaseCost(new Cost((short)0, (MoneyUnit)newItem));
				}
				break;
			case R.id.maneuver_difficulty_spinner:
				dataAccessInterface.getItemTemplate().setManeuverDifficulty((ManeuverDifficulty) newItem);
				dataAccessInterface.saveItem();
				break;
			case R.id.primary_slot_spinner:
				dataAccessInterface.getItemTemplate().setPrimarySlot((Slot)newItem);
				dataAccessInterface.saveItem();
				break;
			case R.id.secondary_slot_spinner:
				dataAccessInterface.getItemTemplate().setSecondarySlot((Slot)newItem);
				dataAccessInterface.saveItem();
				break;
		}
	}

	@Override
	public void observerCompleted(@IdRes int spinnerId) {
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		switch (editTextId) {
			case R.id.weight_edit:
				result = String.valueOf(dataAccessInterface.getItemTemplate().getWeight());
				break;
			case R.id.name_edit:
				result = dataAccessInterface.getItemTemplate().getName();
				break;
			case R.id.notes_edit:
				result = dataAccessInterface.getItemTemplate().getNotes();
				break;
			case R.id.base_cost_edit:
				result = String.valueOf(dataAccessInterface.getItemTemplate().getBaseCost());
				break;
			case R.id.strength_edit:
				result = String.valueOf(dataAccessInterface.getItemTemplate().getStrength());
				break;
			case R.id.construction_time_edit:
				result = String.valueOf(dataAccessInterface.getItemTemplate().getConstructionTime());
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		switch (editTextId) {
			case R.id.weight_edit:
				dataAccessInterface.getItemTemplate().setWeight(Float.valueOf(newString));
				dataAccessInterface.saveItem();
				break;
			case R.id.name_edit:
				dataAccessInterface.getItemTemplate().setName(newString);
				dataAccessInterface.saveItem();
				break;
			case R.id.notes_edit:
				dataAccessInterface.getItemTemplate().setNotes(newString);
				dataAccessInterface.saveItem();
				break;
			case R.id.base_cost_edit:
				if(dataAccessInterface.getItemTemplate().getBaseCost() != null) {
					dataAccessInterface.getItemTemplate().getBaseCost().setValue(Short.valueOf(newString));
				}
				else {
					dataAccessInterface.getItemTemplate().setBaseCost(new Cost(Short.valueOf(newString), MoneyUnit.SILVER_COIN));
					monetaryUnitSpinnerUtil.setSelection(MoneyUnit.SILVER_COIN);
				}
				dataAccessInterface.saveItem();
				break;
			case R.id.strength_edit:
				dataAccessInterface.getItemTemplate().setStrength(Short.valueOf(newString));
				dataAccessInterface.saveItem();
				break;
			case R.id.construction_time_edit:
				dataAccessInterface.getItemTemplate().setConstructionTime(Integer.valueOf(newString));
				dataAccessInterface.saveItem();
				break;
		}
	}

	@Override
	public boolean performLongClick(@IdRes int editTextId) {
		return false;
	}

	public boolean copyViewsToItem() {
		boolean changed = false;
		ItemTemplate currentInstance = dataAccessInterface.getItemTemplate();

		String newString = nameEdit.getText().toString();
		if(!newString.equals(currentInstance.getName())) {
			currentInstance.setName(newString);
			changed = true;
		}

		newString = notesEdit.getText().toString();
		if(!newString.equals(currentInstance.getNotes())) {
			currentInstance.setNotes(newString);
			changed = true;
		}

		newString = weightEdit.getText().toString();
		float newFloat = Float.valueOf(newString);
		if(newFloat != currentInstance.getWeight()) {
			currentInstance.setWeight(newFloat);
			changed = true;
		}

		newString = baseCostEdit.getText().toString();
		short newShort= Short.valueOf(newString);
		if(currentInstance.getBaseCost() == null) {
			currentInstance.setBaseCost(new Cost(newShort, MoneyUnit.SILVER_COIN));
			changed = true;
		}
		else if(newShort != currentInstance.getBaseCost().getValue()) {
			currentInstance.getBaseCost().setValue(newShort);
			changed = true;
		}

		MoneyUnit newMoneyUnit = monetaryUnitSpinnerUtil.getSelectedItem();
		if(!newMoneyUnit.equals(currentInstance.getBaseCost().getUnit())) {
			currentInstance.getBaseCost().setUnit(newMoneyUnit);
			changed = true;
		}

		newString = strengthEdit.getText().toString();
		newShort = Short.valueOf(newString);
		if(newShort != currentInstance.getStrength()) {
			currentInstance.setStrength(newShort);
			changed = true;
		}

		newString = constructionTimeEdit.getText().toString();
		int newInt = Integer.valueOf(newString);
		if(newInt != currentInstance.getConstructionTime()) {
			currentInstance.setConstructionTime(newInt);
			changed = true;
		}

		ManeuverDifficulty newManeuverDifficulty = maneuverDifficultySpinnerUtil.getSelectedItem();
		if(!newManeuverDifficulty.equals(currentInstance.getManeuverDifficulty())) {
			currentInstance.setManeuverDifficulty(newManeuverDifficulty);
			changed = true;
		}

		Slot newSlot = primarySlotSpinnerUtil.getSelectedItem();
		if(!newSlot.equals(currentInstance.getPrimarySlot())) {
			currentInstance.setPrimarySlot(newSlot);
			changed = true;
		}

		newSlot = secondarySlotSpinnerUtil.getSelectedItem();
		if(!newSlot.equals(currentInstance.getSecondarySlot())) {
			currentInstance.setSecondarySlot(newSlot);
			changed = true;
		}

		return changed;
	}

	public void copyItemToViews() {
		ItemTemplate currentInstance = dataAccessInterface.getItemTemplate();
		nameEdit.setText(currentInstance.getName());
		notesEdit.setText(currentInstance.getNotes());
		weightEdit.setText(String.valueOf(currentInstance.getWeight()));
		if(currentInstance.getBaseCost() != null) {
			baseCostEdit.setText(String.valueOf(currentInstance.getBaseCost().getValue()));
			monetaryUnitSpinnerUtil.setSelection(currentInstance.getBaseCost().getUnit());
		}
		else {
			baseCostEdit.setText("");
		}
		strengthEdit.setText(String.valueOf(currentInstance.getStrength()));
		constructionTimeEdit.setText(String.valueOf(currentInstance.getConstructionTime()));
		if(currentInstance.getManeuverDifficulty() == null) {
			maneuverDifficultySpinnerUtil.setSelection(ManeuverDifficulty.MEDIUM);
		}
		else {
			maneuverDifficultySpinnerUtil.setSelection(currentInstance.getManeuverDifficulty());
		}
		if(currentInstance.getPrimarySlot() == null) {
			primarySlotSpinnerUtil.setSelection(Slot.ANY);
		}
		else {
			primarySlotSpinnerUtil.setSelection(currentInstance.getPrimarySlot());
		}
		if(currentInstance.getSecondarySlot() == null) {
			secondarySlotSpinnerUtil.setSelection(Slot.NONE);
		}
		else {
			secondarySlotSpinnerUtil.setSelection(currentInstance.getSecondarySlot());
		}

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		weightEdit.setError(null);
		baseCostEdit.setError(null);
		strengthEdit.setError(null);
		constructionTimeEdit.setError(null);
	}

	// Setters
	public void setDataAccessInterface(
			DataAccessInterface dataAccessInterface) {
		this.dataAccessInterface = dataAccessInterface;
	}

	public interface DataAccessInterface {
		ItemTemplate getItemTemplate();
		void saveItem();
	}
}
