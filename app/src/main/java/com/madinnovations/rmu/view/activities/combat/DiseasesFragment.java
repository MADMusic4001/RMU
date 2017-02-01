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
package com.madinnovations.rmu.view.activities.combat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.DiseaseRxHandler;
import com.madinnovations.rmu.data.entities.combat.Disease;
import com.madinnovations.rmu.data.entities.combat.Severity;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.ThreeFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CombatFragmentModule;
import com.madinnovations.rmu.view.utils.EditTextUtils;
import com.madinnovations.rmu.view.utils.SpinnerUtils;

import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for disease.
 */
public class DiseasesFragment extends Fragment implements ThreeFieldListAdapter.GetValues<Disease>,
		EditTextUtils.ValuesCallback, SpinnerUtils.ValuesCallback {
	private static final String TAG = "DiseasesFragment";
	@Inject
	protected DiseaseRxHandler               diseaseRxHandler;
	private   ThreeFieldListAdapter<Disease> listAdapter;
	private   ListView                       listView;
	private   EditText                       nameEdit;
	private   SpinnerUtils<Severity>         severitySpinner;
	private   EditText                       minDurationEdit;
	private   EditText                       maxDurationEdit;
	private   EditText                       effectsEdit;
	private   Disease                        currentInstance = new Disease();
	private   boolean                        isNew = true;

	// <editor-fold desc="method overrides/implementations">
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCombatFragmentComponent(new CombatFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.diseases_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(R.string.label_disease_name);
		((TextView)layout.findViewById(R.id.header_field2)).setText(R.string.label_disease_severity);
		((TextView)layout.findViewById(R.id.header_field3)).setText(R.string.label_disease_effects);

		nameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.name_edit,
										  R.string.validation_disease_name_required);
		minDurationEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.min_duration_edit, 0);
		maxDurationEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.max_duration_edit, 0);
		effectsEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.effects_edit,
												 R.string.validation_disease_effects_required);
		severitySpinner = new SpinnerUtils<>();
		severitySpinner.initSpinner(layout, getActivity(), Arrays.asList(Severity.values()), this, R.id.severity_spinner, null);
		initListView(layout);

		setHasOptionsMenu(true);

		return layout;
	}

	@Override
	public void onPause() {
		if(copyViewsToItem()) {
			saveItem();
		}
		super.onPause();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.diseases_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_disease) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Disease();
			isNew = true;
			copyItemToViews();
			listView.clearChoices();
			listAdapter.notifyDataSetChanged();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.disease_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Disease disease;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_disease:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Disease();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_disease:
				disease = (Disease)listView.getItemAtPosition(info.position);
				if(disease != null) {
					deleteItem(disease);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public CharSequence getField1Value(Disease disease) {
		return disease.getName();
	}

	@Override
	public CharSequence getField2Value(Disease disease) {
		return disease.getSeverity().toString();
	}

	@Override
	public CharSequence getField3Value(Disease disease) {
		return disease.getEffects();
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		switch (editTextId) {
			case R.id.name_edit:
				result = currentInstance.getName();
				break;
			case R.id.min_duration_edit:
				if(currentInstance.getMinDurationDays() != null) {
					result = String.valueOf(currentInstance.getMinDurationDays());
				}
				break;
			case R.id.max_duration_edit:
				if(currentInstance.getMaxDurationDays() != null) {
					result = String.valueOf(currentInstance.getMaxDurationDays());
				}
				break;
			case R.id.effects_edit:
				result = currentInstance.getEffects();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		switch (editTextId) {
			case R.id.name_edit:
				currentInstance.setName(newString);
				saveItem();
				break;
			case R.id.min_duration_edit:
				if(newString == null || newString.isEmpty()) {
					currentInstance.setMinDurationDays(null);
				}
				else {
					currentInstance.setMinDurationDays(Short.valueOf(newString));
				}
				saveItem();
				break;
			case R.id.max_duration_edit:
				if(newString == null || newString.isEmpty()) {
					currentInstance.setMaxDurationDays(null);
				}
				else {
					currentInstance.setMaxDurationDays(Short.valueOf(newString));
				}
				saveItem();
				break;
			case R.id.effects_edit:
				currentInstance.setEffects(newString);
				saveItem();
				break;
		}
	}

	@Override
	public boolean performLongClick(@IdRes int editTextId) {
		return false;
	}

	@Override
	public Object getValueForSpinner(@IdRes int spinnerId) {
		Object result = null;

		switch (spinnerId) {
			case R.id.severity_spinner:
				result = currentInstance.getSeverity();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromSpinner(@IdRes int spinnerId, Object newItem) {
		switch (spinnerId) {
			case R.id.severity_spinner:
				currentInstance.setSeverity((Severity)newItem);
				saveItem();
				break;
		}
	}
	// </editor-fold>

	// <editor-fold desc="Copy to/from views/entity methods">
	private boolean copyViewsToItem() {
		boolean changed = false;
		Short shortValue;

		View currentFocusView = getActivity().getCurrentFocus();
		if(currentFocusView != null) {
			currentFocusView.clearFocus();
		}

		String value = nameEdit.getText().toString();
		if(value.isEmpty()) {
			value = null;
		}
		if((value == null && currentInstance.getName() != null) ||
				(value != null && !value.equals(currentInstance.getName()))) {
			currentInstance.setName(value);
			changed = true;
		}

		Severity newSeverity = severitySpinner.getSelectedItem();
		if(!newSeverity.equals(currentInstance.getSeverity())) {
			currentInstance.setSeverity(newSeverity);
			changed = true;
		}

		value = minDurationEdit.getText().toString();
		if(value.isEmpty()) {
			shortValue = null;
		}
		else {
			shortValue = Short.valueOf(value);
		}
		if((shortValue == null && currentInstance.getMinDurationDays() != null) ||
				(shortValue != null && !shortValue.equals(currentInstance.getMinDurationDays()))) {
			if(shortValue == null) {
				currentInstance.setMinDurationDays(null);
			}
			else {
				currentInstance.setMinDurationDays(shortValue);
			}
			changed = true;
		}

		value = maxDurationEdit.getText().toString();
		if(value.isEmpty()) {
			shortValue = null;
		}
		else {
			shortValue = Short.valueOf(value);
		}
		if((shortValue == null && currentInstance.getMaxDurationDays() != null) ||
				(shortValue != null && !shortValue.equals(currentInstance.getMaxDurationDays()))) {
			if(shortValue == null) {
				currentInstance.setMaxDurationDays(null);
			}
			else {
				currentInstance.setMaxDurationDays(shortValue);
			}
			changed = true;
		}

		value = effectsEdit.getText().toString();
		if(value.isEmpty()) {
			value = null;
		}
		if((value == null && currentInstance.getEffects() != null) ||
				(value != null && !value.equals(currentInstance.getEffects()))) {
			currentInstance.setEffects(value);
			changed = true;
		}

		return changed;
	}

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		severitySpinner.setSelection(currentInstance.getSeverity());
		if(currentInstance.getMinDurationDays() != null) {
			minDurationEdit.setText(String.valueOf(currentInstance.getMinDurationDays()));
		}
		else {
			minDurationEdit.setText(null);
		}
		if(currentInstance.getMaxDurationDays() != null) {
			maxDurationEdit.setText(String.valueOf(currentInstance.getMaxDurationDays()));
		}
		else {
			maxDurationEdit.setText(null);
		}
		effectsEdit.setText(currentInstance.getEffects());
		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getEffects() != null && !currentInstance.getEffects().isEmpty()) {
			effectsEdit.setError(null);
		}
	}
	// </editor-fold>

	// <editor-fold desc="Save/delete entity methods">
	private void deleteItem(final Disease item) {
		diseaseRxHandler.deleteById(item.getId())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Boolean>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e(TAG, "Exception when deleting: " + item, e);
					Toast.makeText(getActivity(), R.string.toast_disease_delete_failed, Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onNext(Boolean success) {
					if(success) {
						int position = listAdapter.getPosition(item);
						if(position == listAdapter.getCount() -1) {
							position--;
						}
						listAdapter.remove(item);
						listAdapter.notifyDataSetChanged();
						if(position >= 0) {
							listView.setSelection(position);
							listView.setItemChecked(position, true);
							currentInstance = listAdapter.getItem(position);
						}
						else {
							currentInstance = new Disease();
							isNew = true;
						}
						copyItemToViews();
						Toast.makeText(getActivity(), R.string.toast_disease_deleted, Toast.LENGTH_SHORT).show();
					}
				}
			});
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			diseaseRxHandler.save(currentInstance)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Disease>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception saving Disease", e);
						Toast.makeText(getActivity(), R.string.toast_disease_save_failed, Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Disease savedDisease) {
						if (wasNew) {
							listAdapter.add(savedDisease);
							if(savedDisease == currentInstance) {
								listView.setSelection(listAdapter.getPosition(savedDisease));
								listView.setItemChecked(listAdapter.getPosition(savedDisease), true);
							}
							listAdapter.notifyDataSetChanged();
						}
						if (getActivity() != null) {
							Toast.makeText(getActivity(), R.string.toast_disease_saved, Toast.LENGTH_SHORT).show();

							int position = listAdapter.getPosition(currentInstance);
							LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
							if (v != null) {
								TextView textView = (TextView) v.findViewById(R.id.row_field1);
								textView.setText(currentInstance.getName());
								textView = (TextView) v.findViewById(R.id.row_field2);
								textView.setText(currentInstance.getSeverity().toString());
								textView = (TextView) v.findViewById(R.id.row_field3);
								textView.setText(currentInstance.getEffects());
							}
						}
					}
				});
		}
	}
	// </editor-fold>

	// <editor-fold desc="Widget initialization methods">
	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new ThreeFieldListAdapter<>(this.getActivity(), 1, 1, 5, this);
		listView.setAdapter(listAdapter);

		diseaseRxHandler.getAll()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Collection<Disease>>() {
				@Override
				public void onCompleted() {
					if(listAdapter.getCount() > 0) {
						currentInstance = listAdapter.getItem(0);
						isNew = false;
						listView.setSelection(0);
						listView.setItemChecked(0, true);
						listAdapter.notifyDataSetChanged();
						copyItemToViews();
					}
				}
				@Override
				public void onError(Throwable e) {
					Log.e(TAG, "Exception caught getting all Disease instances", e);
					Toast.makeText(DiseasesFragment.this.getActivity(),
							R.string.toast_diseases_load_failed,
							Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onNext(Collection<Disease> diseases) {
					listAdapter.clear();
					listAdapter.addAll(diseases);
					listAdapter.notifyDataSetChanged();
					if(diseases.size() > 0) {
						listView.setSelection(0);
						listView.setItemChecked(0, true);
						currentInstance = listAdapter.getItem(0);
						isNew = false;
						copyItemToViews();
					}
					String toastString;
					toastString = String.format(getString(R.string.toast_diseases_loaded), diseases.size());
					Toast.makeText(DiseasesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
				}
			});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Disease) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Disease();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}
	// </editor-fold>
}
