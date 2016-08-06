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
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.ParameterRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentCategoryRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentRxHandler;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.common.ParameterListAdapter;
import com.madinnovations.rmu.view.adapters.common.SkillSpinnerAdapter;
import com.madinnovations.rmu.view.adapters.common.TalentCategorySpinnerAdapter;
import com.madinnovations.rmu.view.adapters.common.TalentListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for talents.
 */
public class TalentsFragment extends Fragment {
	@Inject
	protected TalentRxHandler talentRxHandler;
	@Inject
	TalentCategoryRxHandler talentCategoryRxHandler;
	@Inject
	SkillRxHandler skillRxHandler;
	@Inject
	ParameterRxHandler parameterRxHandler;
	@Inject
	protected TalentListAdapter listAdapter;
	@Inject
	protected TalentCategorySpinnerAdapter categorySpinnerAdapter;
	@Inject
	protected SkillSpinnerAdapter affectedSkillSpinnerAdapter;
	@Inject
	protected ParameterListAdapter parametersListAdapter;
	@Inject
	protected ParameterListAdapter selectedParametersListAdapter;
	private ListView listView;
	private Spinner  categorySpinner;
	private EditText nameEdit;
	private EditText descriptionEdit;
	private Spinner  affectedSkillSpinner;
	private EditText initialCostEdit;
	private EditText costPerTierEdit;
	private EditText bonusPerTierEdit;
	private CheckBox situationalCheckbox;
	private EditText actionPointsEdit;
	private ListView parametersListview;
	private ListView selectedParametersListview;

	private Talent currentInstance = new Talent();
	private boolean          isNew            = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.talents_fragment, container, false);

		initCategorySpinner(layout);
		initNameEdit(layout);
		initDescriptionEdit(layout);
		initAffectedSkillSpinner(layout);
		initInitialCostEdit(layout);
		initCostPerTierEdit(layout);
		initBonusPerTierEdit(layout);
		initSituationalCheckbox(layout);
		initActionPointsEdit(layout);
		initParametersListview(layout);
		initAddParameterButton(layout);
		initRemoveParameterButton(layout);
		initSelectedParametersListview(layout);
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
		inflater.inflate(R.menu.talents_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_talent) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Talent();
			isNew = true;
			copyItemToViews();
			listView.clearChoices();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.talent_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Talent talent;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_talent:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Talent();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				return true;
			case R.id.context_delete_talent:
				talent = (Talent)listView.getItemAtPosition(info.position);
				if(talent != null) {
					deleteItem(talent);
					return true;
				}
		}
		return super.onContextItemSelected(item);
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		short newShort;

		TalentCategory newCategory = categorySpinnerAdapter.getItem(categorySpinner.getSelectedItemPosition());
		if((newCategory == null && currentInstance.getCategory() != null) ||
				(newCategory != null && !newCategory.equals(currentInstance.getCategory()))) {
			currentInstance.setCategory(newCategory);
			changed = true;
		}

		String newValue = nameEdit.getText().toString();
		if(newValue.isEmpty()) {
			newValue = null;
		}
		if((newValue == null && currentInstance.getName() != null) ||
				(newValue != null && !newValue.equals(currentInstance.getName()))) {
			currentInstance.setName(newValue);
			changed = true;
		}

		newValue = descriptionEdit.getText().toString();
		if(newValue.isEmpty()) {
			newValue = null;
		}
		if((newValue == null && currentInstance.getDescription() != null) ||
				(newValue != null && !newValue.equals(currentInstance.getDescription()))) {
			currentInstance.setDescription(newValue);
			changed = true;
		}

		Skill newSkill = affectedSkillSpinnerAdapter.getItem(affectedSkillSpinner.getSelectedItemPosition());
		if((newSkill == null && currentInstance.getAffectedSkill() != null) ||
				(newSkill != null && !newSkill.equals(currentInstance.getAffectedSkill()))) {
			currentInstance.setAffectedSkill(newSkill);
			changed = true;
		}

		if(initialCostEdit.getText().length() > 0) {
			newShort = Short.valueOf(initialCostEdit.getText().toString());
			if(newShort != currentInstance.getInitialCost()) {
				currentInstance.setInitialCost(newShort);
				changed = true;
			}
		}

		if(costPerTierEdit.getText().length() > 0) {
			newShort = Short.valueOf(costPerTierEdit.getText().toString());
			if(newShort != currentInstance.getCostPerTier()) {
				currentInstance.setCostPerTier(newShort);
				changed = true;
			}
		}

		if(bonusPerTierEdit.getText().length() > 0) {
			newShort = Short.valueOf(bonusPerTierEdit.getText().toString());
			if(newShort != currentInstance.getBonusPerTier()) {
				currentInstance.setBonusPerTier(newShort);
				changed = true;
			}
		}

		if(situationalCheckbox.isChecked() != currentInstance.isSituational()) {
			currentInstance.setSituational(situationalCheckbox.isChecked());
			changed = true;
		}

		if(actionPointsEdit.getText().length() > 0) {
			newShort = Short.valueOf(actionPointsEdit.getText().toString());
			if(newShort != currentInstance.getActionPoints()) {
				currentInstance.setActionPoints(newShort);
				changed = true;
			}
		}

		boolean updateParameters = false;
		if(selectedParametersListAdapter.getCount() != currentInstance.getParameters().size()) {
			updateParameters = true;
		}
		else {
			for(Parameter parameter : currentInstance.getParameters()) {
				if(selectedParametersListAdapter.getPosition(parameter) == -1) {
					updateParameters = true;
					break;
				}
			}
		}
		if(updateParameters) {
			currentInstance.getParameters().clear();
			for(int i = 0; i < selectedParametersListAdapter.getCount(); i++) {
				currentInstance.getParameters().add(selectedParametersListAdapter.getItem(i));
			}
			changed = true;
		}

		return changed;
	}

	private void copyItemToViews() {
		categorySpinner.setSelection(categorySpinnerAdapter.getPosition(currentInstance.getCategory()));
		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());
		affectedSkillSpinner.setSelection(affectedSkillSpinnerAdapter.getPosition(currentInstance.getAffectedSkill()));
		initialCostEdit.setText(String.valueOf(currentInstance.getInitialCost()));
		costPerTierEdit.setText(String.valueOf(currentInstance.getCostPerTier()));
		bonusPerTierEdit.setText(String.valueOf(currentInstance.getBonusPerTier()));
		situationalCheckbox.setChecked(currentInstance.isSituational());
		actionPointsEdit.setText(String.valueOf(currentInstance.getActionPoints()));
		selectedParametersListAdapter.clear();
		selectedParametersListAdapter.addAll(currentInstance.getParameters());
		selectedParametersListAdapter.notifyDataSetChanged();

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
			talentRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Talent>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("TalentsFragment", "Exception saving new Talent: " + currentInstance, e);
							Toast.makeText(getActivity(), getString(R.string.toast_talent_save_failed), Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Talent savedItem) {
							if (wasNew) {
								listAdapter.add(savedItem);
								listAdapter.notifyDataSetChanged();
								listView.setSelection(listAdapter.getPosition(savedItem));
								listView.setItemChecked(listAdapter.getPosition(savedItem), true);
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), getString(R.string.toast_talent_saved), Toast.LENGTH_SHORT).show();
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

	private void deleteItem(@NonNull final Talent item) {
		talentRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("TalentsFragment", "Exception when deleting: " + item, e);
						String toastString = getString(R.string.toast_talent_delete_failed);
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
								currentInstance = new Talent();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_talent_category_deleted), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void initCategorySpinner(View layout) {
		categorySpinner = (Spinner)layout.findViewById(R.id.category_spinner);
		categorySpinner.setAdapter(categorySpinnerAdapter);

		talentCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<TalentCategory>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("TalentsFragment", "Exception caught getting all TalentCategory instances", e);
					}
					@Override
					public void onNext(Collection<TalentCategory> items) {
						categorySpinnerAdapter.clear();
						categorySpinnerAdapter.addAll(items);
						categorySpinnerAdapter.notifyDataSetChanged();
					}
				});

		categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(currentInstance.getCategory() == null || categorySpinnerAdapter.getPosition(currentInstance.getCategory()) != position) {
					currentInstance.setCategory(categorySpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getCategory() != null) {
					currentInstance.setCategory(null);
					saveItem();
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
					nameEdit.setError(getString(R.string.validation_name_required));
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newName = nameEdit.getText().toString();
					if (!newName.equals(currentInstance.getName())) {
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
					descriptionEdit.setError(getString(R.string.validation_description_required));
				}
			}
		});
		descriptionEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newDescription = descriptionEdit.getText().toString();
					if (!newDescription.equals(currentInstance.getDescription())) {
						currentInstance.setDescription(newDescription);
						saveItem();
					}
				}
			}
		});
	}

	private void initAffectedSkillSpinner(View layout) {
		affectedSkillSpinner = (Spinner)layout.findViewById(R.id.affected_skill_spinner);
		affectedSkillSpinner.setAdapter(affectedSkillSpinnerAdapter);

		skillRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Skill>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("TalentsFragment", "Exception caught getting all TalentCategory instances", e);
					}
					@Override
					public void onNext(Collection<Skill> items) {
						affectedSkillSpinnerAdapter.clear();
						affectedSkillSpinnerAdapter.addAll(items);
						affectedSkillSpinnerAdapter.notifyDataSetChanged();
					}
				});

		affectedSkillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(currentInstance.getCategory() == null || affectedSkillSpinnerAdapter.getPosition(currentInstance.getAffectedSkill()) != position) {
					currentInstance.setAffectedSkill(affectedSkillSpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getAffectedSkill() != null) {
					currentInstance.setAffectedSkill(null);
					saveItem();
				}
			}
		});
	}

	private void initInitialCostEdit(View layout) {
		initialCostEdit = (EditText)layout.findViewById(R.id.initial_cost_edit);
		initialCostEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && initialCostEdit != null) {
					initialCostEdit.setError(getString(R.string.validation_talent_initial_cost_required));
				}
			}
		});
		initialCostEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(initialCostEdit.getText().length() > 0) {
						short newHits = Short.valueOf(initialCostEdit.getText().toString());
						if (newHits != currentInstance.getInitialCost()) {
							currentInstance.setInitialCost(newHits);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initCostPerTierEdit(View layout) {
		costPerTierEdit = (EditText)layout.findViewById(R.id.cost_per_tier_edit);
		costPerTierEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && costPerTierEdit != null) {
					costPerTierEdit.setError(getString(R.string.validation_talent_cost_per_tier_required));
				}
			}
		});
		costPerTierEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(costPerTierEdit.getText().length() > 0) {
						short newHits = Short.valueOf(costPerTierEdit.getText().toString());
						if (newHits != currentInstance.getCostPerTier()) {
							currentInstance.setCostPerTier(newHits);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initBonusPerTierEdit(View layout) {
		bonusPerTierEdit = (EditText)layout.findViewById(R.id.bonus_per_tier_edit);
		bonusPerTierEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && bonusPerTierEdit != null) {
					bonusPerTierEdit.setError(getString(R.string.validation_talent_bonus_per_tier_required));
				}
			}
		});
		bonusPerTierEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(bonusPerTierEdit.getText().length() > 0) {
						short newHits = Short.valueOf(bonusPerTierEdit.getText().toString());
						if (newHits != currentInstance.getBonusPerTier()) {
							currentInstance.setBonusPerTier(newHits);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initSituationalCheckbox(View layout) {
		situationalCheckbox = (CheckBox) layout.findViewById(R.id.situational_check_box);
		situationalCheckbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				currentInstance.setSituational(situationalCheckbox.isChecked());
				saveItem();
			}
		});
	}

	private void initActionPointsEdit(View layout) {
		actionPointsEdit = (EditText)layout.findViewById(R.id.action_points_edit);
		actionPointsEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && actionPointsEdit != null) {
					actionPointsEdit.setError(getString(R.string.validation_hits_required));
				}
			}
		});
		actionPointsEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(actionPointsEdit.getText().length() > 0) {
						short newHits = Short.valueOf(actionPointsEdit.getText().toString());
						if (newHits != currentInstance.getActionPoints()) {
							currentInstance.setActionPoints(newHits);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initParametersListview(View layout) {
		parametersListview = (ListView) layout.findViewById(R.id.parameters_list);

		parametersListview.setAdapter(parametersListAdapter);

		parameterRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Parameter>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("TalentsFragment", "Exception caught getting all Parameter instances in initParametersListview", e);
					}
					@Override
					public void onNext(Collection<Parameter> items) {
						parametersListAdapter.clear();
						parametersListAdapter.addAll(items);
						parametersListAdapter.notifyDataSetChanged();
					}
				});
	}

	private void initAddParameterButton(View layout) {
		Button addParameterButton = (Button) layout.findViewById(R.id.add_parameter_button);

		addParameterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SparseBooleanArray checkedList = parametersListview.getCheckedItemPositions();
				for(int i = 0; i < checkedList.size(); i++) {
					if(checkedList.get(i)) {
						selectedParametersListAdapter.add(parametersListAdapter.getItem(i));
					}
				}
				selectedParametersListAdapter.notifyDataSetChanged();
			}
		});
	}

	private void initRemoveParameterButton(View layout) {
		Button removeParameterButton = (Button) layout.findViewById(R.id.remove_parameter_button);

		removeParameterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SparseBooleanArray checkedList = selectedParametersListview.getCheckedItemPositions();
				for(int i = 0; i < checkedList.size(); i++) {
					if(checkedList.get(i)) {
						selectedParametersListAdapter.remove(selectedParametersListAdapter.getItem(i));
					}
				}
				selectedParametersListAdapter.notifyDataSetChanged();
			}
		});
	}

	private void initSelectedParametersListview(View layout) {
		selectedParametersListview = (ListView) layout.findViewById(R.id.selected_parameters_list);
		selectedParametersListview.setAdapter(selectedParametersListAdapter);

		selectedParametersListAdapter.clear();
		selectedParametersListAdapter.addAll(currentInstance.getParameters());
		selectedParametersListAdapter.notifyDataSetChanged();
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

		listView.setAdapter(listAdapter);

		talentRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Talent>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("TalentsFragment", "Exception caught getting all Talent instances", e);
						Toast.makeText(TalentsFragment.this.getActivity(),
								getString(R.string.toast_talents_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Talent> talents) {
						listAdapter.clear();
						listAdapter.addAll(talents);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_talents_loaded), talents.size());
						Toast.makeText(TalentsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Talent) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Talent();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}
}
