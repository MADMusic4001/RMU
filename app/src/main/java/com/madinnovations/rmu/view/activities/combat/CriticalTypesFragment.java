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
import com.madinnovations.rmu.controller.rxhandler.combat.CriticalTypeRxHandler;
import com.madinnovations.rmu.data.entities.combat.CriticalType;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CombatFragmentModule;
import com.madinnovations.rmu.view.utils.EditTextUtils;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for body parts.
 */
public class CriticalTypesFragment extends Fragment implements TwoFieldListAdapter.GetValues<CriticalType>,
		EditTextUtils.ValuesCallback {
	@Inject
	protected CriticalTypeRxHandler criticalTypeRxHandler;
	private TwoFieldListAdapter<CriticalType> listAdapter;
	private ListView              listView;
	private EditText              codeEdit;
	private EditText              nameEdit;
	private CriticalType currentInstance = new CriticalType();
	private boolean isNew = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCombatFragmentComponent(new CombatFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.critical_types_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_critical_type_code));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_critical_type_description));

		nameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.name_edit,
										  R.string.validation_critical_type_name_required);
		codeEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.code_edit,
										  R.string.validation_critical_type_code_required);
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
		inflater.inflate(R.menu.critical_types_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_critical_type) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new CriticalType();
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
		getActivity().getMenuInflater().inflate(R.menu.critical_type_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final CriticalType criticalType;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_critical_type:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new CriticalType();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_critical_type:
				criticalType = (CriticalType)listView.getItemAtPosition(info.position);
				if(criticalType != null) {
					deleteItem(criticalType);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public CharSequence getField1Value(CriticalType criticalType) {
		return String.valueOf(criticalType.getCode());
	}

	@Override
	public CharSequence getField2Value(CriticalType criticalType) {
		return criticalType.getName();
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		switch (editTextId) {
			case R.id.name_edit:
				result = currentInstance.getName();
				break;
			case R.id.code_edit:
				result = String.valueOf(currentInstance.getCode());
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
			case R.id.code_edit:
				currentInstance.setCode(newString.charAt(0));
				saveItem();
				break;
		}
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		String value;

		value = codeEdit.getText().toString();
		if(!value.isEmpty() && value.charAt(0) != currentInstance.getCode()) {
			currentInstance.setCode(value.charAt(0));
			changed = true;
		}

		value = nameEdit.getText().toString();
		if(value.isEmpty()) {
			value = null;
		}
		if((value == null && currentInstance.getName() != null) ||
				(value != null && !value.equals(currentInstance.getName()))) {
			currentInstance.setName(value);
			changed = true;
		}
		return changed;
	}

	private void copyItemToViews() {
		codeEdit.setText(String.valueOf(currentInstance.getCode()));
		nameEdit.setText(currentInstance.getName());

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
	}

	private void deleteItem(final CriticalType item) {
		criticalTypeRxHandler.deleteById(item.getId())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Boolean>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e("CriticalTypeFragment", "Exception when deleting: " + item, e);
					Toast.makeText(getActivity(), getString(R.string.toast_critical_type_delete_failed), Toast.LENGTH_SHORT).show();
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
							currentInstance = new CriticalType();
							isNew = true;
						}
						copyItemToViews();
						Toast.makeText(getActivity(), getString(R.string.toast_critical_type_deleted), Toast.LENGTH_SHORT).show();
					}
				}
			});
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			criticalTypeRxHandler.save(currentInstance)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<CriticalType>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CriticalTypesFragment", "Exception saving CriticalType", e);
						String toastString = getString(R.string.toast_critical_type_save_failed);
						Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(CriticalType savedCriticalType) {
						if (wasNew) {
							listAdapter.add(savedCriticalType);
							if(savedCriticalType == currentInstance) {
								listView.setSelection(listAdapter.getPosition(savedCriticalType));
								listView.setItemChecked(listAdapter.getPosition(savedCriticalType), true);
							}
							listAdapter.notifyDataSetChanged();
						}
						if (getActivity() != null) {
							String toastString;
							toastString = getString(R.string.toast_critical_type_saved);
							Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

							int position = listAdapter.getPosition(currentInstance);
							LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
							if (v != null) {
								TextView textView = (TextView) v.findViewById(R.id.row_field1);
								textView.setText(String.valueOf(currentInstance.getCode()));
								textView = (TextView) v.findViewById(R.id.row_field2);
								textView.setText(currentInstance.getName());
							}
						}
					}
				});
		}
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		criticalTypeRxHandler.getAll()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Collection<CriticalType>>() {
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
					Log.e("CriticalTypesFragment", "Exception caught getting all CriticalType instances", e);
					Toast.makeText(CriticalTypesFragment.this.getActivity(),
							getString(R.string.toast_critical_types_load_failed),
							Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onNext(Collection<CriticalType> criticalTypes) {
					listAdapter.clear();
					listAdapter.addAll(criticalTypes);
					listAdapter.notifyDataSetChanged();
					if(criticalTypes.size() > 0) {
						listView.setSelection(0);
						listView.setItemChecked(0, true);
						currentInstance = listAdapter.getItem(0);
						isNew = false;
						copyItemToViews();
					}
					String toastString;
					toastString = String.format(getString(R.string.toast_critical_types_loaded), criticalTypes.size());
					Toast.makeText(CriticalTypesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
				}
			});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (CriticalType) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new CriticalType();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}
}
