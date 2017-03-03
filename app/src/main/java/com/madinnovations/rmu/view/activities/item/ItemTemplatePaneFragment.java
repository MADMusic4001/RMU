/**
 * Copyright (C) 2017 MadInnovations
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
import com.madinnovations.rmu.data.entities.object.ItemTemplate;
import com.madinnovations.rmu.data.entities.object.Slot;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.ItemFragmentModule;
import com.madinnovations.rmu.view.utils.EditTextUtils;
import com.madinnovations.rmu.view.utils.SpinnerUtils;

import java.util.Arrays;

/**
 * Handles interactions with the UI for item templates.
 */
public class ItemTemplatePaneFragment extends Fragment implements SpinnerUtils.ValuesCallback, EditTextUtils.ValuesCallback {
	@SuppressWarnings("unused")
	private static final String TAG = "ItemTemplatePaneFrag";
	private   EditText                          nameEdit;
	private   EditText                          notesEdit;
	private   EditText                          weightEdit;
	private   SpinnerUtils<Slot>                primarySlotSpinnerUtil;
	private   SpinnerUtils<Slot>                secondarySlotSpinnerUtil;
	private   DataAccessInterface               dataAccessInterface;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newItemFragmentComponent(new ItemFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.item_templates_pane, container, false);

		nameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.name_edit, 0);
		notesEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.notes_edit, 0);
		weightEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.weight_edit, 0);

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
		}
	}

	@Override
	public boolean performLongClick(@IdRes int editTextId) {
		return false;
	}

	public void copyItemToViews() {
		ItemTemplate currentInstance = dataAccessInterface.getItemTemplate();
		nameEdit.setText(currentInstance.getName());
		notesEdit.setText(currentInstance.getNotes());
		weightEdit.setText(String.valueOf(currentInstance.getWeight()));
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
		if(currentInstance.getNotes() != null && !currentInstance.getNotes().isEmpty()) {
			notesEdit.setError(null);
		}
		weightEdit.setError(null);
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
