/*
  Copyright (C) 2015 MadMusic4001
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
package com.madinnovations.rmu.view.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.FileRxHandler;
import com.madinnovations.rmu.controller.rxhandler.campaign.CampaignRxHandler;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.CampaignFragmentModule;
import com.madinnovations.rmu.view.utils.SpinnerUtils;

import javax.inject.Inject;

/**
 * UI for file selection dialog
 */
public class CampaignSelectorDialogFragment extends DialogFragment implements SpinnerUtils.ValuesCallback {
	@Inject
	protected FileRxHandler                fileRxHandler;
	@Inject
	protected CampaignRxHandler            campaignRxHandler;
	private CampaignSelectorDialogListener listener;
	private Campaign                       currentSelection;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View contentView = inflater.inflate(R.layout.campaign_selector_dialog, null);

		SpinnerUtils<Campaign> campaignSpinnerUtils = new SpinnerUtils<>();
		campaignSpinnerUtils.initSpinner(contentView, getActivity(), campaignRxHandler.getAll(), this, R.id.campaign_spinner,
										 null);

		return builder.setTitle(R.string.title_choose_export_campaign)
				.setView(contentView)
				.setNegativeButton(android.R.string.cancel, null)
				.setPositiveButton(android.R.string.ok,
								   new DialogInterface.OnClickListener() {
									   @Override
									   public void onClick(DialogInterface dialog1, int which) {
										   listener.onCampaignSelected(currentSelection);
									   }
								   })
				.create();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((CampaignActivity)activity).getActivityComponent().
				newCampaignFragmentComponent(new CampaignFragmentModule(this)).injectInto(this);
		try {
			listener = (CampaignSelectorDialogListener) activity;
		}
		catch (ClassCastException ex) {
			throw new ClassCastException(
					activity.getClass().getName() + " must implement "
							+ "FileSelectorDialogFragment.FileSelectorDialogListener.");
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		((CampaignActivity)getActivity()).getActivityComponent().
				newCampaignFragmentComponent(new CampaignFragmentModule(this)).injectInto(this);
		try {
			listener = (CampaignSelectorDialogListener) context;
		}
		catch (ClassCastException ex) {
			throw new ClassCastException(
					context.getClass().getName() + " must implement "
							+ "FileSelectorDialogFragment.FileSelectorDialogListener.");
		}
	}

	@Override
	public Object getValueForSpinner(@IdRes int spinnerId) {
		Object result = null;

		switch (spinnerId) {
			case R.id.campaign_spinner:
				result = currentSelection;
				break;
		}
		return result;
	}

	@Override
	public void setValueFromSpinner(@IdRes int spinnerId, Object newItem) {
		switch (spinnerId) {
			case R.id.campaign_spinner:
				currentSelection = (Campaign)newItem;
				break;
		}
	}

	@Override
	public void observerCompleted(@IdRes int spinnerId) {}

	public interface CampaignSelectorDialogListener {
		/**
		 * Notifies the listener of the name of the campaign when the dialog id closed with the OK button.
		 *
		 * @param campaign  the selected campaign instance
		 */
		void onCampaignSelected(Campaign campaign);
	}
}
