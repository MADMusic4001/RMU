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
import com.madinnovations.rmu.view.di.modules.FragmentModule;

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
	private   ListView             listView;
	private   EditText             nameEdit;
	private   EditText             descriptionEdit;
	private   EditText             valueEdit;
	private   CheckBox      perTierCheckBox;
	private Parameter    selectedInstance = null;
	private boolean dirty            = false;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newFragmentComponent(new FragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.parameters_fragment, container, false);

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initValueEdit(layout);
		initPerTierCheckBox(layout);
		initListView(layout);

		setHasOptionsMenu(true);

		parameterRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Parameter>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("ParametersFragment", "Exception caught getting all Parameter instances in onCreateView", e);
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

		return layout;
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
			Parameter parameter = new Parameter();
			parameter.setName(getString(R.string.default_parameter_name));
			parameter.setDescription(getString(R.string.default_parameter_description));
			parameter.setValue(Short.valueOf(getString(R.string.default_parameter_value)));
			parameter.setPerTier(Boolean.valueOf(getString(R.string.default_parameter_per_tier)));
			parameterRxHandler.save(parameter)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Parameter>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {
							Log.e("ParametersFragment", "Exception saving new Parameter in onOptionsItemSelected", e);
						}

						@Override
						public void onNext(Parameter savedParameter) {
							listAdapter.add(savedParameter);
							nameEdit.setText(savedParameter.getName());
							descriptionEdit.setText(savedParameter.getDescription());
							valueEdit.setText(String.valueOf(savedParameter.getValue()));
							perTierCheckBox.setChecked(savedParameter.isPerTier());
							selectedInstance = savedParameter;
						}
					});
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
			case R.id.context_delete_parameter:
				parameter = (Parameter)listView.getItemAtPosition(info.position);
				if(parameter != null) {
					parameterRxHandler.deleteById(parameter.getId())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Boolean>() {
								@Override
								public void onCompleted() {

								}

								@Override
								public void onError(Throwable e) {
									Log.e("ParameterFragment", "Exception when deleting: " + parameter, e);
									String toastString = getString(R.string.toast_parameter_delete_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onNext(Boolean success) {
									String toastString;

									if(success) {
										listAdapter.remove(parameter);
										listAdapter.notifyDataSetChanged();
										toastString = getString(R.string.toast_parameter_deleted);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
								}
							});
					return true;
				}
				else {
					return false;
				}
			default:
				return super.onContextItemSelected(item);
		}
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
					nameEdit.setError(getString(R.string.validation_name_required));
				}
				else if (selectedInstance != null && !editable.toString().equals(selectedInstance.getName())) {
					dirty = true;
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newName = nameEdit.getText().toString();
					if (selectedInstance != null && !newName.equals(selectedInstance.getName())) {
						dirty = true;
						selectedInstance.setName(newName);
						parameterRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<Parameter>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										Log.e("ParametersFragment", "Exception saving new Parameter in initNameEdit", e);
										String toastString = getString(R.string.toast_parameter_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onNext(Parameter savedParameter) {
										onSaved(savedParameter);
									}
								});
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
					descriptionEdit.setError(getString(R.string.validation_description_required));
				}
				else if (selectedInstance != null && !editable.toString().equals(selectedInstance.getDescription())) {
					dirty = true;
				}
			}
		});
		descriptionEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newDescription = descriptionEdit.getText().toString();
					if (selectedInstance != null && !newDescription.equals(selectedInstance.getDescription())) {
						dirty = true;
						selectedInstance.setDescription(newDescription);
						parameterRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<Parameter>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										Log.e("ParametersFragment", "Exception saving new Parameter in initDescriptionEdit", e);
										String toastString = getString(R.string.toast_parameter_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onNext(Parameter savedParameter) {
										onSaved(savedParameter);
									}
								});
					}
				}
			}
		});
	}

	private void initValueEdit(View layout) {
		valueEdit = (EditText)layout.findViewById(R.id.value_edit);
		valueEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && valueEdit != null) {
					valueEdit.setError(getString(R.string.validation_value_required));
				}
				else if (selectedInstance != null && Short.valueOf(editable.toString()) != selectedInstance.getValue()) {
					dirty = true;
				}
			}
		});
		valueEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if (valueEdit.getText() != null && valueEdit.getText().length() > 0) {
						final short newValue = Short.valueOf(valueEdit.getText().toString());
						if (selectedInstance != null && newValue != selectedInstance.getValue()) {
							dirty = true;
							selectedInstance.setValue(newValue);
							parameterRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<Parameter>() {
									@Override
									public void onCompleted() {
									}

									@Override
									public void onError(Throwable e) {
										Log.e("ParametersFragment", "Exception saving new Parameter in initValueEdit", e);
										String toastString = getString(R.string.toast_parameter_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}

									@Override
									public void onNext(Parameter savedParameter) {
										onSaved(savedParameter);
									}
								});
						}
					}
				}
			}
		});
	}

	private void initPerTierCheckBox(View layout) {
		perTierCheckBox = (CheckBox) layout.findViewById(R.id.per_tier_checkbox);

		perTierCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dirty = true;
			}
		});
		perTierCheckBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final boolean newPerTier = perTierCheckBox.isChecked();
					if (selectedInstance != null && newPerTier != selectedInstance.isPerTier()) {
						dirty = true;
						selectedInstance.setPerTier(newPerTier);
						parameterRxHandler.save(selectedInstance)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Parameter>() {
								@Override
								public void onCompleted() {
								}

								@Override
								public void onError(Throwable e) {
									Log.e("ParametersFragment", "Exception saving new Parameter in initValueEdit", e);
									String toastString = getString(R.string.toast_parameter_save_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onNext(Parameter savedParameter) {
											onSaved(savedParameter);
										}
							});
					}
				}
			}
		});
	}

	private void onSaved(Parameter parameter) {
		if(getActivity() == null) {
			return;
		}
		String toastString;
		toastString = getString(R.string.toast_parameter_saved);
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

		int position = listAdapter.getPosition(parameter);
		// Add 1 for the header row
		LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
		if (v != null) {
			TextView textView = (TextView) v.findViewById(R.id.name_view);
			if (textView != null) {
				textView.setText(parameter.getName());
			}
			textView = (TextView) v.findViewById(R.id.description_view);
			if (textView != null) {
				textView.setText(parameter.getDescription());
			}
		}
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

		listView.setAdapter(listAdapter);

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(dirty && selectedInstance != null) {
					selectedInstance.setName(nameEdit.getText().toString());
					selectedInstance.setDescription(descriptionEdit.getText().toString());
					selectedInstance.setValue(Short.valueOf(valueEdit.getText().toString()));
					selectedInstance.setPerTier(perTierCheckBox.isChecked());
					parameterRxHandler.save(selectedInstance)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(new Subscriber<Parameter>() {
							@Override
							public void onCompleted() {
							}
							@Override
							public void onError(Throwable e) {
								Log.e("ParametersFragment", "Exception saving new Parameter in initListView", e);
								String toastString = getString(R.string.toast_parameter_save_failed);
								Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
							}
							@Override
							public void onNext(Parameter savedParameter) {
								onSaved(savedParameter);
							}
						});
					dirty = false;
				}

				selectedInstance = (Parameter) listView.getItemAtPosition(position);
				if (selectedInstance != null) {
					nameEdit.setText(selectedInstance.getName());
					descriptionEdit.setText(selectedInstance.getDescription());
					valueEdit.setText(String.valueOf(selectedInstance.getValue()));
					perTierCheckBox.setChecked(selectedInstance.isPerTier());
				}
			}
		});
		registerForContextMenu(listView);
	}
}
