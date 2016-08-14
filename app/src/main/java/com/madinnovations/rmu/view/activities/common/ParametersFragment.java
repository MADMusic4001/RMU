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
package com.madinnovations.rmu.view.activities.common;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.ParameterRxHandler;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.common.ParameterListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for parameters.
 */
public class ParametersFragment extends Fragment {
	@Inject
	protected ParameterRxHandler   parameterRxHandler;
	@Inject
	protected ParameterListAdapter listAdapter;
	private ListView listView;
	private EditText nameEdit;
	private EditText descriptionEdit;
	private EditText baseValueEdit;
	private EditText perValueEdit;
	private CheckBox perTierCheckBox;
	private CheckBox perLevelCheckBox;
	private Parameter currentInstance = new Parameter();
	private boolean isNew            = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.parameters_fragment, container, false);

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initBaseValueEdit(layout);
		initPerValueEdit(layout);
		initPerLevelCheckBox(layout);
		initPerTierCheckBox(layout);
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
		inflater.inflate(R.menu.parameters_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_parameter) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Parameter();
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
		getActivity().getMenuInflater().inflate(R.menu.parameter_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Parameter parameter;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_parameter:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Parameter();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_parameter:
				parameter = (Parameter) listView.getItemAtPosition(info.position);
				if(parameter != null) {
					deleteItem(parameter);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		String value = nameEdit.getText().toString();
		if(value.isEmpty()) {
			value = null;
		}
		if((value == null && currentInstance.getName() != null) ||
				(value != null && !value.equals(currentInstance.getName()))) {
			currentInstance.setName(value);
			changed = true;
		}

		value = descriptionEdit.getText().toString();
		if(value.isEmpty()) {
			value = null;
		}
		if((value == null && currentInstance.getDescription() != null) ||
				(value != null && !value.equals(currentInstance.getDescription()))) {
			currentInstance.setName(value);
			changed = true;
		}

		value = baseValueEdit.getText().toString();
		if(value.isEmpty()) {
			value = null;
		}
		if((value == null && currentInstance.getBaseValue() != null) ||
				(value != null && !value.equals(currentInstance.getBaseValue()))) {
			currentInstance.setBaseValue(value);
			changed = true;
		}

		value = perValueEdit.getText().toString();
		if(value.isEmpty()) {
			value = null;
		}
		if((value == null && currentInstance.getValuePerLevelOrTier() != null) ||
				(value != null && !value.equals(currentInstance.getValuePerLevelOrTier()))) {
			currentInstance.setValuePerLevelOrTier(value);
			changed = true;
		}

		if(perLevelCheckBox.isChecked() != currentInstance.isPerLevel()) {
			currentInstance.setPerLevel(perLevelCheckBox.isChecked());
			changed = true;
		}

		if(perTierCheckBox.isChecked() != currentInstance.isPerTier()) {
			currentInstance.setPerTier(perTierCheckBox.isChecked());
			changed = true;
		}

		return changed;
	}

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());
		baseValueEdit.setText(currentInstance.getBaseValue());
		perValueEdit.setText(currentInstance.getValuePerLevelOrTier());
		perLevelCheckBox.setChecked(currentInstance.isPerLevel());
		perTierCheckBox.setChecked(currentInstance.isPerTier());

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			parameterRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Parameter>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("ParametersFragment", "Exception saving new Parameter: " + currentInstance, e);
							Toast.makeText(getActivity(), getString(R.string.toast_parameter_save_failed), Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Parameter savedItem) {
							if (wasNew) {
								listAdapter.add(savedItem);
								if(savedItem == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedItem));
									listView.setItemChecked(listAdapter.getPosition(savedItem), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), getString(R.string.toast_parameter_saved), Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedItem);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.name_view);
									textView.setText(savedItem.getName());
									textView = (TextView) v.findViewById(R.id.description_view);
									textView.setText(savedItem.getDescription());
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final Parameter item) {
		parameterRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("ParametersFragment", "Exception when deleting: " + item, e);
						String toastString = getString(R.string.toast_parameter_delete_failed);
						Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
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
								currentInstance = new Parameter();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_parameter_deleted), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void initNameEdit(View layout) {
		nameEdit = (EditText)layout.findViewById(R.id.name_edit);
		nameEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && nameEdit != null) {
					nameEdit.setError(getString(R.string.validation_parameter_name_required));
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newName = nameEdit.getText().toString();
					if (currentInstance != null && !newName.equals(currentInstance.getName())) {
						currentInstance.setName(newName);
						saveItem();
					}
				}
			}
		});
	}

	private void initDescriptionEdit(View layout) {
		descriptionEdit = (EditText)layout.findViewById(R.id.description_edit);
		descriptionEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && descriptionEdit != null) {
					descriptionEdit.setError(getString(R.string.validation_parameter_description_required));
				}
			}
		});
		descriptionEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newDescription = descriptionEdit.getText().toString();
					if (currentInstance != null && !newDescription.equals(currentInstance.getDescription())) {
						currentInstance.setDescription(newDescription);
						saveItem();
					}
				}
			}
		});
	}

	private void initBaseValueEdit(View layout) {
		baseValueEdit = (EditText)layout.findViewById(R.id.base_value_edit);
		baseValueEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if (baseValueEdit.getText() != null) {
						final String newValue = baseValueEdit.getText().toString();
						if (!newValue.equals(currentInstance.getBaseValue())) {
							currentInstance.setBaseValue(newValue);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initPerValueEdit(View layout) {
		perValueEdit = (EditText)layout.findViewById(R.id.per_value_edit);
		perValueEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if (perValueEdit.getText() != null) {
						final String newValue = perValueEdit.getText().toString();
						if (!newValue.equals(currentInstance.getValuePerLevelOrTier())) {
							currentInstance.setValuePerLevelOrTier(newValue);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initPerLevelCheckBox(View layout) {
		perLevelCheckBox = (CheckBox) layout.findViewById(R.id.per_level_checkbox);

		perLevelCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				currentInstance.setPerTier(perLevelCheckBox.isChecked());
				saveItem();
			}
		});
	}

	private void initPerTierCheckBox(View layout) {
		perTierCheckBox = (CheckBox) layout.findViewById(R.id.per_tier_checkbox);

		perTierCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				currentInstance.setPerTier(perTierCheckBox.isChecked());
				saveItem();
			}
		});
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

		listView.setAdapter(listAdapter);

		parameterRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Parameter>>() {
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
						Log.e("ParametersFragment", "Exception caught getting all Parameter instances", e);
						Toast.makeText(ParametersFragment.this.getActivity(), getString(R.string.toast_parameters_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Parameter> parameters) {
						listAdapter.clear();
						listAdapter.addAll(parameters);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_parameters_loaded), parameters.size());
						Toast.makeText(ParametersFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Parameter) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Parameter();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}
}
