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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.campaign.CampaignRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SizeRxHandler;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.ItemFragmentModule;
import com.madinnovations.rmu.view.utils.EditTextUtils;
import com.madinnovations.rmu.view.utils.SpinnerUtils;

import javax.inject.Inject;

/**
 * Handles interactions with the UI for items.
 */
public class ItemPaneFragment extends Fragment implements EditTextUtils.ValuesCallback, SpinnerUtils.ValuesCallback {
	@SuppressWarnings("unused")
	private static final String TAG = "ItemPaneFragment";
	@Inject
	protected CampaignRxHandler      campaignRxHandler;
	@Inject
	protected SizeRxHandler          sizeRxHandler;
	private   EditText               nameEdit;
	private   SpinnerUtils<Campaign> campaignSpinnerUtils;
	private   SpinnerUtils<Size>     sizeSpinnerUtils;
	private   EditText               levelEdit;
	private   EditText               historyEdit;
	private   DataAccessInterface    dataAccessInterface;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newItemFragmentComponent(new ItemFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.items_pane, container, false);

		nameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.name_edit, 0);

		campaignSpinnerUtils = new SpinnerUtils<>();
		campaignSpinnerUtils.initSpinner(layout, getActivity(), campaignRxHandler.getAll(), this, R.id.campaign_spinner, null);
		sizeSpinnerUtils = new SpinnerUtils<>();
		sizeSpinnerUtils.initSpinner(layout, getActivity(), sizeRxHandler.getAll(), this, R.id.size_spinner, null);
		levelEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.level_edit, R.string.validation_item_level_required);
		historyEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.history_edit, 0);

		return layout;
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		if(dataAccessInterface != null) {
			switch (editTextId) {
				case R.id.name_edit:
					result = dataAccessInterface.getItem().getName();
					break;
				case R.id.level_edit:
					result = String.valueOf(dataAccessInterface.getItem().getLevel());
					break;
				case R.id.history_edit:
					result = dataAccessInterface.getItem().getHistory();
					break;
			}
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		if(dataAccessInterface != null) {
			switch (editTextId) {
				case R.id.name_edit:
					dataAccessInterface.getItem().setName(newString);
					dataAccessInterface.saveItem();
					break;
				case R.id.level_edit:
					dataAccessInterface.getItem().setLevel(Short.valueOf(newString));
					dataAccessInterface.saveItem();
					break;
				case R.id.history_edit:
					dataAccessInterface.getItem().setHistory(newString);
					dataAccessInterface.saveItem();
					break;
			}
		}
	}

	@Override
	public boolean performLongClick(@IdRes int editTextId) {
		return false;
	}

	@Override
	public Object getValueForSpinner(@IdRes int spinnerId) {
		Object result = null;

		if(dataAccessInterface != null) {
			switch (spinnerId) {
				case R.id.campaign_spinner:
					result = dataAccessInterface.getItem().getCampaign();
					break;
				case R.id.size_spinner:
					result = dataAccessInterface.getItem().getSize();
					break;
			}
		}

		return result;
	}

	@Override
	public void setValueFromSpinner(@IdRes int spinnerId, Object newItem) {
		if(dataAccessInterface != null) {
			switch (spinnerId) {
				case R.id.campaign_spinner:
					dataAccessInterface.getItem().setCampaign((Campaign) newItem);
					dataAccessInterface.saveItem();
					break;
				case R.id.size_spinner:
					dataAccessInterface.getItem().setSize((Size) newItem);
					dataAccessInterface.saveItem();
					break;
			}
		}
	}

	@Override
	public void observerCompleted(@IdRes int spinnerId) {
		switch (spinnerId) {
			case R.id.campaign_spinner:
				break;
			case R.id.size_spinner:
				if(dataAccessInterface.getItem().getSize() == null) {
					for (Size size : sizeSpinnerUtils.getAllItems()) {
						if (size.getCode().equals("V")) {
							dataAccessInterface.getItem().setSize(size);
							dataAccessInterface.saveItem();
							sizeSpinnerUtils.setSelection(size);
							break;
						}
					}
				}
				break;
		}
	}

	public void copyItemToViews() {
		Item currentInstance = dataAccessInterface.getItem();
		nameEdit.setText(currentInstance.getName());
		historyEdit.setText(currentInstance.getHistory());

		Log.d(TAG, "copyItemToViews: item = " + ((Weapon)currentInstance).print());
		if(currentInstance.getCampaign() == null) {
			Log.d(TAG, "copyItemToViews: selected campaign item = " + campaignSpinnerUtils.getSelectedItem());
			if(campaignSpinnerUtils.getSelectedItem() != null) {
				Log.d(TAG, "copyItemToViews: setting campaign value");
				currentInstance.setCampaign(campaignSpinnerUtils.getSelectedItem());
			}
		}
		else {
			campaignSpinnerUtils.setSelection(currentInstance.getCampaign());
		}

		if(currentInstance.getSize() == null) {
			if(sizeSpinnerUtils.getSelectedItem() != null) {
				currentInstance.setSize(sizeSpinnerUtils.getSelectedItem());
			}
		}
		else {
			sizeSpinnerUtils.setSelection(currentInstance.getSize());
		}

		levelEdit.setText(String.valueOf(currentInstance.getLevel()));

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
	}

	// Setters
	public void setDataAccessInterface(
			DataAccessInterface dataAccessInterface) {
		this.dataAccessInterface = dataAccessInterface;
	}

	public interface DataAccessInterface {
		Item getItem();
		void saveItem();
	}
}
